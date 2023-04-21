package paciente;

import consultas.Login;
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

/** MIDlet principal, que implementa CommandListener para añadir comandos.
 *
 * @author grupo InftelD 2009.
 */
public class Inicio extends MIDlet implements CommandListener {

    private Display pantalla;

    private Form portada;

    private String idsession;

    private Image imagen;

    private Alert alerta, confirmacion;

    private Form flogin;

    private TextField usuario;

    private TextField passwd;

    private Command salir, aceptar;

    private static final String dir = "192.168.183.66:8080";

    /** MIDlet principal.
     *
     */
    public Inicio() throws IOException {
        pantalla = Display.getDisplay(this);
        flogin = new Form("Control Acceso");
        imagen = Image.createImage("/logo.png");
        usuario = new TextField("usuario", "", 10, TextField.ANY);
        passwd = new TextField("contraseña", "", 10, TextField.ANY | TextField.PASSWORD);
        aceptar = new Command("Enviar", Command.OK, 0);
        salir = new Command("Salir", Command.EXIT, 0);
        flogin.append(imagen);
        flogin.append(usuario);
        flogin.append(passwd);
        flogin.addCommand(aceptar);
        flogin.addCommand(salir);
        flogin.setCommandListener(this);
        confirmacion = new Alert("Confirmacion", "", null, AlertType.CONFIRMATION);
        confirmacion.setTimeout(2000);
        portada = new Form("Portada");
        portada.append(imagen);
        alerta = new Alert("Error", "No se puede cargar la imagen", null, AlertType.ERROR);
        alerta.setTimeout(1000);
    }

    /** Ejecuta el midlet.
     *
     */
    public void startApp() {
        pantalla = Display.getDisplay(this);
        pantalla.setCurrent(portada);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        pantalla.setCurrent(flogin);
    }

    /**
     *
     */
    public void pauseApp() {
    }

    /**
     *
     * @param unconditional
     */
    public void destroyApp(boolean unconditional) {
    }

    /** Funcion commandAction, para manejar los command.
     *
     * @param c comando que pulsamos.
     * @param displayable donde actuamos.
     */
    public void commandAction(Command c, Displayable displayable) {
        if (c == salir) {
            destroyApp(false);
            notifyDestroyed();
        } else if (c == aceptar) {
            Thread t = new Thread(new Login(usuario.getString(), passwd.getString(), this));
            t.start();
        }
    }

    /** Getter para obtener el atributo idSession.
     *
     * @return número de session con la que nos conectamos al hacer el login.
     */
    public String getIdsession() {
        return idsession;
    }

    /** Setter para añadir el atributo idSession.
     *
     * @param idsession numero de session con la que nos conectamos al hacer el login.
     */
    public void setIdsession(String idsession) {
        this.idsession = idsession;
    }

    /** Getter para obtener la dir del servidor
     *
     * @return String
     */
    public static String getDir() {
        return dir;
    }
}
