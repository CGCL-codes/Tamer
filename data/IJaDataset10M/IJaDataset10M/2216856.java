package AGO.Controlador;

import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import AGO.IO.SystemStream;
import AGO.Modelo.AModel;
import AGO.Modelo.Estructuras.Instrumento;
import AGO.Vista.Frames.AWindow;
import java.util.Vector;

public class Controller {

    public AWindow window = null;

    public AModel model = null;

    /**
	 * Launch the application
	 * @param args
	 */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    new Controller();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Controller() {
        window = new AWindow(this);
        model = new AModel(window);
        this.window.setVisible(true);
    }

    public Vector<Instrumento> getInstrumentos() {
        return null;
    }

    public Vector<String> getInstrumentosName() {
        return null;
    }

    public Vector<String> getDBInstrumentosName() {
        Vector<String> res = new Vector<String>();
        res.add("prueba1");
        return res;
    }

    public void addInstrumento(String name) {
        this.model.addInstrumento(name);
    }

    public Vector<String> getDBMonedasName() {
        Vector<String> prueb = new Vector<String>();
        prueb.add("try");
        return prueb;
    }

    public Vector<String> getMonedasName() {
        return null;
    }

    public static void changeStdOut() {
        try {
            PrintStream out = new PrintStream(new FileOutputStream(AGO.Util.Assert.getPath(), true));
            PrintStream tee = new SystemStream(System.out, out);
            System.setOut(tee);
            tee = new SystemStream(System.err, out);
            System.setErr(tee);
        } catch (FileNotFoundException e) {
        }
    }

    public void addEstrategia(String name, Vector<String> instrName) {
        this.model.addEstrategia(name, instrName);
    }

    public void setMonedaPrincipal(String name) {
        this.model.setMonedaPrincipal(name);
    }
}
