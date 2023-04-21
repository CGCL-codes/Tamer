package loroedi;

import loroedi.help.HelpManager;
import loroedi.laf.LookAndFeel;
import loro.*;
import util.editor.*;
import loroedi.EDI;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.util.*;
import javax.swing.*;

/**
 * VIEJO Entorno de desarrollo Integrado para Loro.
 *
 * @author Carlos Rueda
 * @version $Id: LoroEDI.java,v 1.3 2003/05/24 21:15:28 carueda Exp $
 */
public class LoroEDI {

    static final String prefix_title = "Simple LoroEDI";

    /**
	 * El controlador principal.
	 */
    LoroControl loroControl;

    /**
	 */
    public LoroEDI(JFrame frame, LoroControl loroControl) {
        super();
        this.loroControl = loroControl;
        EDI edi = new EDI(prefix_title, frame, loroControl);
        edi.display();
    }

    /**
	 * Programa principal.
	 */
    public static void main(String args[]) {
        if (args.length > 0) {
            if (args[0].equals("-version")) {
                System.out.println(Info.obtTituloEDI());
            } else {
                System.out.println(obtUso());
            }
            System.exit(0);
        }
        try {
            JFrame frame = new JFrame();
            iniciar(frame);
            LookAndFeel.setLookAndFeel();
            LoroEDI loroEDI = new LoroEDI(frame, new LoroControl());
            loroEDI.despacharPrimeraVez();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showOptionDialog(null, ex.getMessage(), "Error de inicio", javax.swing.JOptionPane.DEFAULT_OPTION, javax.swing.JOptionPane.ERROR_MESSAGE, null, null, null);
            System.exit(1);
        }
    }

    public javax.swing.Icon obtImagenAPropositoDe() {
        return Util.getIcon("img/splash.gif");
    }

    /** Implementaci�n para Editable. */
    public String obtMensajeAPropositoDe() {
        return Info.obtNombre() + "\n" + "Versi�n " + Info.obtVersion() + "\n" + " \n" + "Un Sistema de Programaci�n Did�ctico\n" + " \n" + "http://loro.sf.net\n" + " \n" + "Copyright� 1999-2002 Carlos Rueda\n" + "Universidad Aut�noma de Manizales\n" + "Manizales - Colombia\n" + " \n" + "Este programa es software libre y usted puede\n" + "redistribuirlo si lo desea. Se ofrece con la\n" + "esperanza que sea �til, pero sin ning�n tipo\n" + "de garant�a. Por favor, lea la licencia de uso.\n" + " \n";
    }

    /** Implementaci�n para Editable. */
    public String obtTitulo() {
        return Info.obtNombre();
    }

    static String obtUso() {
        return Info.obtTituloEDI() + "\n" + "USO:\n" + "   loroedi\n" + "       Lanza el entorno de desarrollo.\n" + "   loroedi -ayuda\n" + "       Despliega esta ayuda y termina.\n" + "   loroedi -version\n" + "       Muestra version del sistema y termina.\n";
    }

    /**
	 * Pone nombre para el archivo en edici�n.
	 * Se actualiza preferencia "ultimo archivo editado".
	 *
	 * @param nomArchivo El nombre a poner.
	 */
    public void ponNombreArchivo(String nomArchivo) {
        Preferencias.ponPreferencia(Preferencias.RECENT, nomArchivo);
    }

    /**
	 * En caso de ser la primera ejecuci�n de la versi�n actual,
	 * realiza las siguientes operaciones:
	 *
	 * <ul>
	 *	<li> Verifica el n�cleo de Loro.
	 *	<li> Verifica la configuraci�n dada al n�cleo.
	 *	<li> Genera documentaci�n de unidades de apoyo
	 *		  en LoroPreferencias.DOC_DIR.
	 *	<li> Abre la ventana de ayuda general.
	 *	<li> Verifica configuraci�n dada al n�cleo de Loro.
	 * </ul>
	 *
	 * @throws Exception   Si hay algun problema.
	 */
    private void despacharPrimeraVez() throws Exception {
        if (Configuracion.getVC() == 0) {
            Loro.verificarNucleo();
            String docDir = Preferencias.obtPreferencia(Preferencias.DOC_DIR);
            loroControl.documentarUnidadesDeApoyo(docDir);
            loroedi.help.HelpManager.displayHelp();
            loroControl.verificarConfiguracionNucleo(false);
        }
    }

    /**
	 * Ejecuta las operaciones finales para la terminaci�n normal de
	 * LoroEDI.
	 *
	 * <ul>
	 *  <li> Almacena las preferencias LoroEDI.
	 *  <li> Almacena la configuraci�n LoroEDI.
	 *  <li> Cierra el n�cleo.
	 * </ul>
	 *
	 * @throws Throwable Si se presenta alg�n problema de finalizaci�n.
	 */
    public static void finalizar() throws Throwable {
        Preferencias.store();
        Configuracion.store();
        Loro.cerrar();
    }

    /**
	 * Ejecuta las operaciones iniciales para que LoroEDI
	 * pueda funcionar normalmente.
	 *
	 * Los pasos realizados son:
	 * <ul>
	 *  <li> Muestra ventana de presentaci�n (splash)
	 *  <li> Carga configuraci�n LoroEDI.
	 *  <li> Carga preferencias LoroEDI.
	 *  <li> Configura el n�cleo Loro.
	 * </ul>
	 *
	 * @throws Exception Si se presenta alg�n problema de inicio.
	 */
    public static void iniciar(JFrame frame) throws Exception {
        Splash.showSplash(frame);
        Configuracion.load();
        Preferencias.load();
        String ext_dir = Configuracion.getProperty(Configuracion.DIR) + "/lib/ext/";
        String paths_dir = Configuracion.getProperty(Configuracion.DIR) + "/lib/prs/";
        Loro.configurar(ext_dir, paths_dir);
        String oro_dir = Preferencias.obtPreferencia(Preferencias.ORO_DIR);
        ICompilador compilador = Loro.obtCompilador();
        compilador.ponDirectorioDestino(oro_dir);
        System.setErr(System.out);
    }
}
