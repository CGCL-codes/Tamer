package util.editor;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.*;
import java.awt.Component;
import java.awt.Font;
import java.awt.Color;

/**
 * A simple text editor.
 *
 * @author Carlos Rueda
 */
public class Editor {

    /** El �rea de texto utilizado para la edici�n. */
    protected AreaTexto textoArchivo;

    /** �Est� guardado el contenido actual? */
    protected boolean discoAlDia;

    /**
	 * Es por cambio interno (no de usuario) la
	 * actualizaci�n del TextArea?
	 * Se vio en alguna JVM que el m�todo setText()
	 * (o sea, cambio interno) provoca el evento. Este
	 * flag ayuda a verificar de qu� se trata en cada
	 * caso.
	 */
    protected boolean cambioInterno;

    /**
	 * Cadena para la opcion de busqueda.
	 */
    protected String cadenaBusqueda;

    /**
	 * Interact�a con el usuario para llevar el cursor hasta donde 
	 * esta una cadena.
	 */
    public void buscar(String prompt, String not_found) {
        String lin = javax.swing.JOptionPane.showInputDialog(frame, prompt, "", javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (lin != null) {
            int pos = textoArchivo.buscar(lin, textoArchivo.getCaretPosition());
            if (pos >= 0) {
                traerAlFrente();
                cadenaBusqueda = lin;
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, not_found + " " + lin, "", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
	 * Busca la siguiente aparicion de una cadena previamente buscada.
	 */
    public void buscarSiguiente(String prompt, String not_found) {
        if (cadenaBusqueda == null) {
            buscar(prompt, not_found);
            return;
        }
        int pos = textoArchivo.buscar(cadenaBusqueda, textoArchivo.getCaretPosition());
        if (pos >= 0) {
            traerAlFrente();
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, not_found + " " + cadenaBusqueda, "", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
	 * copy
	 */
    public void copy() {
        textoArchivo.copy();
    }

    /**
	 * cut
	 */
    public void cut() {
        textoArchivo.cut();
    }

    /**
	 * obtiene el texto.  2001-01-29
	 */
    public String getText() {
        return textoArchivo.getText();
    }

    /**
	 * Interact�a con el usuario para ir a una linea
	 */
    public void irALinea(String prompt) {
        String lin = javax.swing.JOptionPane.showInputDialog(frame, prompt, "", javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (lin != null) {
            try {
                int num = Integer.parseInt(lin);
                textoArchivo.irALinea(num);
                traerAlFrente();
            } catch (NumberFormatException ex) {
            }
        }
    }

    /**
	 * paste
	 */
    public void paste() {
        textoArchivo.paste();
    }

    public void setSelectionColor(Color color) {
        textoArchivo.setSelectionColor(color);
    }

    /**
	 * Selecciona un trozo.
	 */
    public void select(int start, int end) {
        textoArchivo.select(start, end);
        String s = textoArchivo.getText();
        int len = textoArchivo.getText().length();
        if (start >= len) {
            textoArchivo.setCaretPosition(len);
        }
    }

    /**
	 * selectAll
	 */
    public void selectAll() {
        textoArchivo.selectAll();
    }

    public void textoModificado() {
        if (cambioInterno) {
            cambioInterno = false;
        } else {
            discoAlDia = false;
            edListener.estaModificado();
        }
    }

    /**
	 * Hace que el area de texto obtenga el foco
	 * de los eventos.
	 */
    public void traerAlFrente() {
        textoArchivo.traerAlFrente();
    }

    /** Observador de este editor. */
    protected EditorListener edListener;

    /** Mi contenedor principal utilizado para algunos dialogos. */
    protected JFrame frame;

    /**
	 * Obtiene el componente visual de este editor.
	 */
    public Component comoComponent() {
        return textoArchivo.comoComponent();
    }

    /**
	 * Crea un editor.
	 *
	 * @param frame Para dialogos.
	 * @param areaTexto Area de trabajo para edicion.
	 * @param edListener Observador de este editor.
	 * @throws NullPointerException si edListener == null.
	 */
    public Editor(JFrame frame, AreaTexto areaTexto, EditorListener edListener) {
        this.frame = frame;
        this.edListener = edListener;
        if (edListener == null) throw new NullPointerException("null EditorListener");
        this.textoArchivo = areaTexto;
        discoAlDia = true;
        cambioInterno = false;
        cadenaBusqueda = null;
    }

    /**
	 * Obtiene el area de texto manipulada.
	 *
	 * @return El area de texto manipulada.
	 */
    public AreaTexto obtAreaTexto() {
        return textoArchivo;
    }

    /**
	 * Incrementa o decrementa el tamano del font actual
	 * de esta area de texto.
	 */
    public void changeFontSize(int inc) {
        textoArchivo.changeFontSize(inc);
    }

    public void setEditable(boolean editable) {
        textoArchivo.setEditable(editable);
    }

    public void setCaretPosition(int pos) {
        textoArchivo.setCaretPosition(pos);
    }
}
