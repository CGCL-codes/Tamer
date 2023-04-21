package org.osis.monito.cameraControl;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.IntBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.osis.gpl.Lengua;
import org.osis.monito.cameras.CameraException;
import org.osis.monito.windows.Options;
import sun.misc.BASE64Encoder;

public class MjpegBean extends JComponent implements Runnable, ChangeListener {

    private String mjpgURL;

    private javax.swing.JButton btnPlay;

    private frmSettings settings;

    private Runnable updater;

    private String username = "";

    private String password = "";

    private String base64authorization = null;

    private int i = 0;

    private IntBuffer bufferSave;

    private Image image = null;

    private Image imagerez = null;

    private boolean initCompleted = false;

    private HttpURLConnection huc = null;

    private MjpegParser parser;

    private boolean locked;

    private boolean connected = false;

    private boolean retry = true;

    private Image CachedImage;

    public MjpegBean() {
        setForeground(Color.YELLOW);
        setFont(new Font("Dialog", Font.BOLD, 12));
        setBackground(Color.BLACK);
        updater = new Runnable() {

            public void run() {
                repaint();
            }
        };
    }

    /**
     * Intenta conectarse a la camara todas las veces que sea necesario
     * hasta conectarse.
     * Si se conecta pone la variable connected en true y termina
     * Si el operador cancela la operacion de conexion se invoca el metodo
     * disconnect de esta clase el cual pone la variable retry en flase
     * para que este metodo termine.     
     */
    public void connect() {
        do {
            try {
                URL u = new URL(mjpgURL);
                URLConnection uc = u.openConnection();
                uc.setConnectTimeout(30000);
                uc.setReadTimeout(30000);
                uc.setUseCaches(true);
                huc = (HttpURLConnection) uc;
                retry = true;
                if (getUsername() != null && getPassword() != null) {
                    base64authorization = encodeUsernameAndPasswordInBase64(getUsername(), getPassword());
                    huc.setDoInput(true);
                    huc.setRequestProperty("Authorization", base64authorization);
                    huc.connect();
                }
                String boundary = getBoundary();
                createParser(boundary);
            } catch (Exception e) {
                huc.disconnect();
                stateOff("ERRMpegCnx");
            }
        } while (connected == false && retry == true);
    }

    /** Cambia el estado a descoenctado y avisa por que cambio */
    private void stateOff(String msg) {
        connected = false;
        mensaje(msg);
        btnPlay.setIcon(new ImageIcon(getClass().getResource("/Iconos/playB.png")));
    }

    /** Cambia el estado a conectado */
    private void stateOn() {
        btnPlay.setIcon(new ImageIcon(getClass().getResource("/Iconos/playW.png")));
        mensaje("iniciada");
        connected = true;
    }

    /** 
     Cuando se presiona el boton de Stop en el Visor, CamaraLogic invoca
     a este metodo para detener la visualizacion de la camara.
     Este metodo desconecta, pone las variables de estado correspondientes.
     
     En caso de que el sistema no se hubiese conectado y tampoco pueda,
     el metodo connect reintentara conectarse hasta que el operador 
     presione el boton de stop y se invoque a este metodo para que ponga
     la variable retry en false.
     
     @see visor
     @see CameraLogic
     
    */
    public void disconnect() {
        retry = false;
        try {
            if (connected) {
                parser.setCanceled(true);
                huc.disconnect();
                stateOff("desconectada");
            }
        } catch (Exception e) {
            mensaje("ERRMpegNoDet");
        }
    }

    /** 
     Comienza la conexion 
     
     Despues de conectarse interpreta el flujo de entrada con el metodo parse de la 
     clase MjpegParser.
     
     Si se desconecta o se producen problemas en la coenexion este metodo no termina
     sino que trata de reconectarse para que no se pierda informacion.
     Esto es muy util para camaras conectadas mediante enlaces no muy buenos, como 
     wireless o internet.
     
     @see MjpegParser
     
     */
    public void run() {
        if (settings != null) {
            connect();
            try {
                do {
                    this.connect();
                    parser.parse();
                    if (connected) {
                        MessageBus.putError(Lengua.text("RECONECTAR"));
                        parser.setCanceled(true);
                        huc.disconnect();
                    }
                } while (connected);
                this.fireChange();
            } catch (java.lang.NullPointerException ex) {
                stateOff("ERRNotConect");
                connected = false;
            }
        } else {
            stateOff("ERRNotConfig");
        }
    }

    public void stateChanged(ChangeEvent e) {
        byte[] segment = parser.getSegment();
        if (segment.length > 0) {
            try {
                locked = true;
                image = ImageIO.read(new ByteArrayInputStream(segment));
                imagerez = resizeImage(image, this.getWidth(), this.getHeight());
                setCachedImage(imagerez);
                locked = false;
                EventQueue.invokeLater(updater);
            } catch (IOException e1) {
                stateOff("ERRMpegImage");
                if (Options.isDebug()) System.out.println("Mala Imagen");
            }
        }
    }

    /**
     * Obtiene la imagen que actualmente esta disponible.
     * En caso de que se este tomando una imagen nueva el metodo 
     * hace esperar hasta que la imagen este totalmente disponible
     * Este es un metodo sincrinizado
     */
    public synchronized Image getImage() {
        while (locked == true || imagerez == null) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        notifyAll();
        return imagerez;
    }

    /**
     * Pone la nueva imagen en la cache. Bloquea la lectura de
     * la imagen para lograr sincronizacion
     */
    private synchronized void setCachedImage(Image cache) {
        CachedImage = cache;
        notifyAll();
    }

    public Image resizeImage(Image src, int Width, int Height) {
        try {
            int srcWidth = src.getWidth(null);
            int srcHeight = src.getHeight(null);
            Image tmp = createImage(Width, srcHeight);
            Graphics g = tmp.getGraphics();
            int ratio = (srcWidth << 16) / Width;
            int pos = ratio / 2;
            for (int x = 1; x < Width; x++) {
                g.setClip(x, 0, 1, srcHeight);
                g.drawImage(src, x - (pos >> 16), 0, null);
                pos += ratio;
            }
            Image resizedImage = createImage(Width, Height);
            g = resizedImage.getGraphics();
            ratio = (srcHeight << 16) / Height;
            pos = ratio / 2;
            for (int y = 0; y < Height; y++) {
                g.setClip(0, y, Width, 1);
                g.drawImage(tmp, 0, y - (pos >> 16), null);
                pos += ratio;
            }
            return resizedImage;
        } catch (NullPointerException ex) {
            Image resizedImage = createImage(Height, Width);
            return resizedImage;
        }
    }

    /** used to set the image on the panel */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagerez != null) {
            g.drawImage(imagerez, 0, 0, this);
        }
        i++;
        bufferSave = IntBuffer.allocate(200000);
    }

    /**
     * Controla si esta conectado a la camara o no
     */
    public boolean isPlaying() {
        return this.connected;
    }

    /**
     * Setea el formulario de configuracion asociado al
     * el objeto MjpegBean
     */
    public void setSettings(frmSettings setting) throws CameraException {
        settings = setting;
        mjpgURL = settings.getUrl();
    }

    /**
     * Setea el boton de play asociado al visor para mostrar
     * los cambios por la pantalla
     */
    public void setButons(JButton btnPlay) {
        this.btnPlay = btnPlay;
    }

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) listenerList.getListeners(ChangeListener.class);
    }

    /**
     Si se desconecta la camara completamente y no se reproduce nada
     se disparan notificaciones a los listerners de la clase para que
     se enteren que no se corto la reproduccion.
     
     CamaraLogic es un Listener
     
     @see CamaraLogic
    */
    protected void fireChange() {
        Object[] listeners = listenerList.getListenerList();
        ChangeEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (e == null) e = new ChangeEvent(this);
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * encodes username and password in Base64-encoding
     */
    private String encodeUsernameAndPasswordInBase64(String usern, String psswd) {
        String s = usern + ":" + psswd;
        String encs = (new BASE64Encoder()).encode(s.getBytes());
        return "Basic " + encs;
    }

    /**
     Crea el parser durante el intento de conexion
     */
    private void createParser(final String boundary) throws IOException {
        InputStream is = huc.getInputStream();
        parser = new MjpegParser(is, boundary);
        parser.addChangeListener(this);
        stateOn();
    }

    private String getBoundary() {
        String boundary = "--boundary";
        try {
            String contentType = huc.getContentType();
            Pattern pattern = Pattern.compile("boundary=(.*)$");
            Matcher matcher = pattern.matcher(contentType);
            matcher.find();
            boundary = matcher.group(1);
        } catch (Exception e) {
            ;
        }
        return boundary;
    }

    /** Muestra los mensajes con un formato adecuado */
    private void mensaje(String msg) {
        MessageBus.putMessage("CAM [" + this.settings.getCamId() + "] " + Lengua.text(msg));
    }
}
