package camara;

import javax.microedition.lcdui.Displayable;
import javax.microedition.media.*;
import javax.microedition.media.control.*;
import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Graphics;
import paciente.Funciones;
import paciente.Inicio;

/** Activa la camara del movil para poder capturar una foto.
 *
 * @author grupo InftelD 2009.
 */
public class CameraCanvas extends Canvas implements CommandListener, Runnable {

    private Inicio inicio;

    private final Command exitCommand;

    private Command captureCommand = null;

    private Player player = null;

    private VideoControl videoControl = null;

    private boolean active = false;

    private byte[] foto;

    private String message2 = null;

    private String message1 = null;

    private Funciones func;

    /** Constructor
     *
     * @param inicio midlet principal.
     * @param f clase funciones para realizar alguna llamada a esta.
     * @throws java.io.IOException
     * @throws javax.microedition.media.MediaException
     */
    public CameraCanvas(Inicio inicio, Funciones f) throws IOException, MediaException {
        this.inicio = inicio;
        this.func = f;
        exitCommand = new Command("Volver", Command.EXIT, 1);
        addCommand(exitCommand);
        setCommandListener(this);
        try {
            player = (Player) Manager.createPlayer("capture://video");
            player.realize();
            videoControl = (VideoControl) (player.getControl("VideoControl"));
            if (videoControl == null) {
                discardPlayer();
                message1 = "Unsupported:";
                message2 = "Can't get videocontrol";
            } else {
                videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
                int canvasWidth = getWidth();
                int canvasHeight = getHeight();
                int displayWidth = videoControl.getDisplayWidth();
                int displayHeight = videoControl.getDisplayHeight();
                int x = (canvasWidth - displayWidth) / 2;
                int y = (canvasHeight - displayHeight) / 2;
                videoControl.setDisplayLocation(x, y);
                captureCommand = new Command("Capturar", Command.SCREEN, 1);
                addCommand(captureCommand);
            }
        } catch (IOException ioe) {
            discardPlayer();
            message1 = "IOException:";
            message2 = ioe.getMessage();
        } catch (SecurityException se) {
            discardPlayer();
            message1 = "SecurityException";
            message2 = se.getMessage();
        }
    }

    private void discardPlayer() {
        if (player != null) {
            player.close();
            player = null;
        }
        videoControl = null;
    }

    /** Metodo para realizar la representacion grafica de la pantalla.
     *
     * @param g
     */
    public void paint(Graphics g) {
        g.setColor(0x75BFD6);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (message1 != null) {
            g.setColor(0x00000000);
            g.drawString(message1, 1, 1, Graphics.TOP | Graphics.LEFT);
            g.drawString(message2, 1, 1 + g.getFont().getHeight(), Graphics.TOP | Graphics.LEFT);
        }
    }

    /** Metodo para iniciar la clase.
     * 
     * @throws javax.microedition.media.MediaException
     */
    public synchronized void start() throws MediaException {
        if ((player != null) && !active) {
            try {
                player.start();
                videoControl.setVisible(true);
            } catch (SecurityException se) {
                message1 = "SecurityException";
                message2 = se.getMessage();
            }
            active = true;
        }
    }

    /** Para la hebra
     *
     * @throws javax.microedition.media.MediaException
     */
    public synchronized void stop() throws MediaException {
        if ((player != null) && active) {
            videoControl.setVisible(false);
            player.stop();
            active = false;
        }
    }

    /** Funcion commandAction, para manejar los command.
     *
     * @param c comando que pulsamos.
     * @param d displayable donde actuamos
     */
    public void commandAction(Command c, Displayable d) {
        if (c == exitCommand) {
            func.cameraCanvasExit();
        } else if (c == captureCommand) {
            Thread t = new Thread(this);
            t.start();
        }
    }

    /** Ejecuta la hebra.
     *
     */
    public void run() {
        takeSnapshot();
    }

    /** Metodo que se ejecuta cuando pulsamos una tecla en el movil.
     ** Segun el boton pulsado realizamos una accion u otra.
     *
     * @param keyCode indica la tecla que se pulsa.
     */
    public void keyPressed(int keyCode) {
        if (keyCode == CameraCanvas.FIRE) {
            takeSnapshot();
        }
    }

    private void takeSnapshot() {
        if (player != null) {
            try {
                foto = videoControl.getSnapshot(null);
                func.cameraCanvasCaptured(foto);
            } catch (MediaException me) {
                message1 = "MediaException:";
                message2 = me.getMessage();
            }
        }
    }
}
