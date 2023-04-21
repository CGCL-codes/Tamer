package krowdix.interfaz;

import java.awt.BorderLayout;
import java.awt.KeyboardFocusManager;
import javax.swing.JFrame;
import krowdix.control.Controlador;

/**
 * La Interfaz es la ventana de la aplicación. Tendrá una {@link Botonera} en la
 * parte superior y un {@link AreaTrabajo} en la parte inferior. Además, habrá
 * otras dos ventanas, {@link VentanaHerramientas} y
 * {@link VentanaVisualizacion} que permitirán realizar otras tareas en la red u
 * obtener información.
 * 
 * La Interfaz está implementada usando un patrón Singleton, en ningún momento
 * se usará más de una interfaz en una instancia de la aplicación. Para obtener
 * una instancia de la interfaz se debe usar el método {@link #getInterfaz()}.
 * 
 * @author Daniel Alonso Fernández
 */
public class Interfaz extends JFrame {

    /**
	 * Instancia única de la interfaz.
	 */
    private static Interfaz interfaz;

    private static final long serialVersionUID = 1L;

    /**
	 * Devuelve una instancia de la Interfaz.
	 * 
	 * Todas las llamadas a este método durante la ejecución del programa
	 * devolverán la misma Interfaz, por lo que si ésta tiene que ser usada por
	 * varios objetos, estos últimos pueden invocar directamente al método en
	 * lugar de guardar punteros.
	 * 
	 * @return una instancia única de la Interfaz
	 */
    public static Interfaz getInterfaz() {
        if (interfaz == null) {
            interfaz = new Interfaz();
        }
        return interfaz;
    }

    /**
	 * Cadena de texto que guarda el nombre de la base de datos con la que se
	 * está trabajando en este momento. Si no se está trabajando con una base de
	 * datos, será <code>null</code>.
	 */
    public String bdActual;

    /**
	 * Constructor por defecto. Es privado porque para obtener la instancia de
	 * la interfaz hay que realizar una llamada a {@link #getInterfaz()}.
	 * 
	 * El constructor registra al {@link Controlador} como manejador para los
	 * eventos de teclado que reciba la interfaz, y prepara todo para empezar a
	 * trabajar. La interfaz no llega a hacerse visible, para ello hay que
	 * llamar a {@link #setVisible(boolean)}.
	 */
    private Interfaz() {
        super("Krowdix");
        init();
        crearComponentes();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(Controlador.getControlador());
        setLocation(50, 50);
    }

    /**
	 * Actualiza la interfaz para reflejar un cambio que se haya producido en la
	 * red. Este método repinta el grafo de red y actualiza el contenido de la
	 * {@link VentanaHerramientas} y la {@link VentanitaEdicionNodo}.
	 */
    public void actualizar() {
        AreaTrabajo.getAreaTrabajo().repaint();
        VentanaHerramientas.getVentanaHerramientas().actualizar();
        VentanitaEdicionNodo.getVentanitaEdicionNodo().actualizar();
    }

    /**
	 * Sobrecarga del método clone() para evitar que haya más de una instancia
	 * de la Interfaz.
	 */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
	 * Crea los componentes de la interfaz. Estos componentes son la
	 * {@link Botonera}, que estará en la parte superior, y él
	 * {@link AreaTrabajo}, que estará en la parte inferior. Además, despliega
	 * la botonera secundaria correspondiente al botón de crear una nueva red y
	 * muestra un mensaje de bienvenida.
	 */
    private void crearComponentes() {
        setLayout(new BorderLayout());
        add(Botonera.getBotonera(), BorderLayout.NORTH);
        add(AreaTrabajo.getAreaTrabajo(), BorderLayout.CENTER);
        Botonera.getBotonera().nuevaRed.doClick();
        Botonera.getBotonera().textoPanelSecundario.setText("Bienvenido a Krowdix. " + Botonera.getBotonera().textoPanelSecundario.getText());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
    }

    /**
	 * Inicia las variables de la interfaz.
	 */
    private void init() {
        bdActual = null;
        Controlador.getControlador().detenerSimulacion();
    }

    /**
	 * Reinicia la interfaz y propaga la llamada al {@link Controlador}.
	 * 
	 * @see Controlador#reiniciar()
	 */
    public void reiniciar() {
        init();
        Controlador.getControlador().reiniciar();
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        VentanaHerramientas.getVentanaHerramientas().setVisible(b);
        VentanaVisualizacion.getVentanaVisualizacion().setVisible(b);
    }
}
