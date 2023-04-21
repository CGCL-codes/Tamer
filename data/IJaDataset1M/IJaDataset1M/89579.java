package gmenu;

import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import util.colores;

/** Clase gmArchivo para el menu Archivo */
class gmArchivo extends JMenu {

    /** opcion nuevo del menu */
    private gmOpcionMenu gmnuevo;

    /** opcion abrir del menu*/
    private gmOpcionMenu gmabrir;

    /** opcion cerrar del menu*/
    private gmOpcionMenu gmcerr;

    /** opcion guardar del menu*/
    private gmOpcionMenu gmguarda;

    /** opcion guardar como del menu*/
    private gmOpcionMenu gmguardc;

    /** opcion imprimir del menu*/
    private gmOpcionMenu gmimprime;

    /** opcion configurar impresion del menu*/
    private gmOpcionMenu gmconfig;

    /** opcion propiedades del menu*/
    private gmOpcionMenu gmpropi;

    /** opcion salir del menu*/
    private gmOpcionMenu gmsalir;

    /** Constructor por defecto de la clase */
    public gmArchivo() {
    }

    /** Constructor gmArchivo
	 * @param oyente
	 * @param cade el estado actual
	 */
    public gmArchivo(ActionListener oyente, MouseListener oyenteRaton, String cade) {
        setText("Archivo");
        setMnemonic('A');
        setBackground(colores.fondoBarras);
        gmnuevo = new gmOpcionMenu();
        gmnuevo.estableceValores("Nuevo", KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK), oyente, oyenteRaton, ",99,", cade, 'N');
        add(gmnuevo);
        gmabrir = new gmOpcionMenu();
        gmabrir.estableceValores("Abrir", KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK), oyente, oyenteRaton, ",99,", cade, 'A');
        add(gmabrir);
        gmcerr = new gmOpcionMenu();
        gmcerr.estableceValores("Cerrar", KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_MASK), oyente, oyenteRaton, ",12,", cade, 'C');
        add(gmcerr);
        add(new JSeparator());
        gmguarda = new gmOpcionMenu();
        gmguarda.estableceValores("Guardar", KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK), oyente, oyenteRaton, ",18,", cade, 'G');
        add(gmguarda);
        gmguardc = new gmOpcionMenu();
        gmguardc.estableceValores("Guardar como...", KeyStroke.getKeyStroke(KeyEvent.VK_F12, InputEvent.ALT_MASK), oyente, oyenteRaton, ",9,10,", cade, 'o');
        add(gmguardc);
        add(new JSeparator());
        gmconfig = new gmOpcionMenu();
        gmconfig.estableceValores("Configurar impresion", null, oyente, oyenteRaton, ",99,", cade, 'r');
        add(gmconfig);
        gmimprime = new gmOpcionMenu();
        gmimprime.estableceValores("Imprimir", KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK), oyente, oyenteRaton, ",1,4,5,6,7,8,9,10,", cade, 'I');
        add(gmimprime);
        add(new JSeparator());
        gmpropi = new gmOpcionMenu();
        gmpropi.estableceValores("Propiedades ", null, oyente, oyenteRaton, ",12,", cade, 'P');
        add(gmpropi);
        add(new JSeparator());
        gmsalir = new gmOpcionMenu();
        gmsalir.estableceValores("Salir", KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK), oyente, oyenteRaton, ",99,", cade, 'S');
        add(gmsalir);
    }

    /** funcion habilita que permite habilitar y deshabilitar las opciones del menu
	 * @param esta estado actual
	 */
    public void habilita(String esta) {
        gmnuevo.establece(",99,", esta);
        gmabrir.establece(",99,", esta);
        gmcerr.establece(",12,", esta);
        gmguarda.establece(",9,10,", esta);
        gmguardc.establece(",9,10,", esta);
        gmconfig.establece(",99,", esta);
        gmimprime.establece(",1,4,5,6,7,8,9,10,", esta);
        gmpropi.establece(",12,", esta);
        gmsalir.establece(",99,", esta);
    }
}
