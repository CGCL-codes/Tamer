package com.chuidiang.editores.paneles;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.apache.log4j.Logger;
import com.chuidiang.editores.comun.InterfaceEdicionDatos;
import com.chuidiang.editores.primitivos.errores.ErrorConAviso;
import com.chuidiang.editores.primitivos.errores.InterfaceTratamientoError;

/**
 * JDialog con boton de Aceptar y boton de Cancelar.<br>
 * Tienen un hueco para meter dentro un panel que implemente
 * InterfaceEdicionDatos.
 */
public class DialogoAceptarCancelar<Tipo> extends JDialog implements InterfaceAceptarCancelarEdicion<Tipo> {

    /** Comment for <code>serialVersionUID</code> */
    private static final long serialVersionUID = 4121133649547507506L;

    /** Texto del boton de Aceptar por defecto */
    private static final String TEXTO_ACEPTAR = "Aceptar";

    /** Texto del boton de Cancelar por defecto */
    private static final String TEXTO_CANCELAR = "Cancelar";

    /** Logger para la clase */
    private Logger log = Logger.getLogger(DialogoAceptarCancelar.class);

    /** Atributo de la Clase. Boton de Aceptar. */
    protected JButton JBuAceptar = null;

    /** Atributo de la Clase. Boton de Cancelar. */
    protected JButton JBuCancelar = null;

    /**
    * Panel interno a la ventana de Aceptar/Cancelar.<br>
    * El componenteInterno deberia ser ademas un Component de java
    */
    private InterfaceEdicionDatos<Tipo> componenteInterno = null;

    /** Lista de suscriptores al boton de aceptar */
    protected LinkedList<ActionListener> suscriptoresAceptar = new LinkedList<ActionListener>();

    /** Lista de suscriptores al boton de cancelar */
    protected LinkedList<ActionListener> suscriptoresCancelar = new LinkedList<ActionListener>();

    /**
    * Indica si la ventana se ocultar� acutom�ticamente cuando se pulse aceptar
    */
    private boolean autoOcultarAlAceptar = true;

    /**
    * Indica si la ventana se ocultara automaticamente cuando se pulse cancelar
    */
    private boolean autoOcultarAlCancelar = true;

    /**
    * Accion del boton de aceptar.<br>
    * Comprueba que los datos del panel escritos por el usuario son correctos.
    * Si son correctos, avisa a los suscriptores de aceptar y autooculta la
    * ventana si hay que hacerlo. Si no son correctos, llama al gestion de
    * error.
    */
    ActionListener listenerAceptar = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            StringBuffer error = new StringBuffer();
            if (componenteInterno != null) {
                if (componenteInterno.validaDato(error)) {
                    avisaSuscriptores(suscriptoresAceptar);
                    if (autoOcultarAlAceptar) {
                        setVisible(false);
                    }
                } else if (tratamientoError != null) {
                    tratamientoError.tomaError(error.toString(), DialogoAceptarCancelar.this);
                }
            }
        }
    };

    /** Clase para el tratamiento de errores */
    private InterfaceTratamientoError<Tipo> tratamientoError = new ErrorConAviso<Tipo>();

    /**
    * Accion del boton de cancelar.<br>
    * avisa a los suscriptores de cancelar y si hay que autoocultar la
    * ventana, lo hace.
    */
    private ActionListener listenerCancelar = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            canceladaVentana();
        }
    };

    /**
    * Para la x de arriba de la derecha de la ventana, que se trate igual que
    * el cancelar
    */
    private WindowListener windowListener = new WindowAdapter() {

        public void windowClosing(WindowEvent e) {
            canceladaVentana();
        }
    };

    /**
    * Constructor sin parametros,debe ser invocado previamente al m�todo
    * tomaComponente.
    */
    public DialogoAceptarCancelar() {
        inicializar();
    }

    /**
    * Crea la ventana con el padre que se le pasa.
    *
    * @param padre Dialog padre de esta ventana.<br>
    */
    public DialogoAceptarCancelar(Dialog padre) {
        super(padre);
        inicializar();
    }

    /**
    * Crea la ventana con el padre que se le pasa.
    *
    * @param padre Frame padre de esta ventana.<br>
    */
    public DialogoAceptarCancelar(Frame padre) {
        super(padre);
        inicializar();
    }

    /**
    * Constructor con parametros, recibe la ventana en la cual se editan los
    * datos, y el titulo de la ventana. La ventana debe ser Component y tener
    * implementada InterfaceEdicionDatos, si no es asi la za la Excepcion
    * ClassCastException.
    *
    * @param panelEditor Panel de edici�n que va dentro de la ventana.<br>
    * @param title Titulo para la ventana.<br>
    */
    public DialogoAceptarCancelar(InterfaceEdicionDatos<Tipo> panelEditor, String title) {
        inicializar();
        setTitle(title);
        setComponenteInterno(panelEditor);
    }

    /**
    * Constructor con parametros, recibe la ventana en la cual se editan los
    * datos, el titulo de la ventana, un boolean para indicar si quiere que
    * esta ventana sea modal(no se tiene acceso a nada que no sea esta ventana
    * hasta cerrarla) o no. La ventana debe ser un Component e implementar el
    * InterfaceEdicionDatos. Lanza la Excepcion ClassCastException.
    *
    * @param panelEditor Panel que va dentro de la ventana.<br>
    * @param modal Si la ventana es modal.<br>
    * @param title Titulo para la ventana.<br>
    * @param posicion Posicion en pantalla.<br>
    */
    public DialogoAceptarCancelar(InterfaceEdicionDatos<Tipo> panelEditor, boolean modal, String title, Point posicion) {
        inicializar();
        setModal(modal);
        setTitle(title);
        setLocation(posicion);
        setComponenteInterno(panelEditor);
    }

    /**
    * Constructor con parametros, recibe la ventana en la cual se editan los
    * datos, el titulo de la ventana, un boolean para indicar si quiere que
    * esta ventana sea modal(no se tiene acceso a nada que no sea esta ventana
    * hasta cerrarla) o no. La ventana debe ser un Component e implementar el
    * InterfaceEdicionDatos. Lanza la Excepcion ClassCastException.
    *
    * @param padre Dialog padre de la ventana.<br>
    * @param panelEditor editor interno.<br>
    * @param modal Si la ventana es modal.<br>
    * @param title Titulo para la ventana.<br>
    * @param posicion Posicion de la ventana en pantalla.<br>
    */
    public DialogoAceptarCancelar(Dialog padre, InterfaceEdicionDatos<Tipo> panelEditor, boolean modal, String title, Point posicion) {
        super(padre);
        inicializar();
        setModal(modal);
        setTitle(title);
        setLocation(posicion);
        setComponenteInterno(panelEditor);
    }

    /**
    * Constructor con parametros, recibe la ventana en la cual se editan los
    * datos, el titulo de la ventana, un boolean para indicar si quiere que
    * esta ventana sea modal(no se tiene acceso a nada que no sea esta ventana
    * hasta cerrarla) o no. La ventana debe ser un Component e implementar el
    * InterfaceEdicionDatos. Lanza la Excepcion ClassCastException.
    *
    * @param padre Frame padre de la ventana.<br>
    * @param panelEditor editor interno.<br>
    * @param modal si la ventana es o no modal.<br>
    * @param title Titulo de la ventana.<br>
    * @param posicion Posicion en pantalla.<br>
    */
    public DialogoAceptarCancelar(Frame padre, InterfaceEdicionDatos<Tipo> panelEditor, boolean modal, String title, Point posicion) {
        super(padre);
        inicializar();
        setModal(modal);
        setTitle(title);
        setLocation(posicion);
        setComponenteInterno(panelEditor);
    }

    /**
    * Indica si la ventana debe ocultarse autom�ticamente al pulsar el bot�n de aceptar.<br>
    * Por defecto es true.
    *
    * @param autoOcultarAlAceptar true si queremos que se oculte
    *        automaticamente.<br>
    */
    public void setAutoOcultarAlAceptar(boolean autoOcultarAlAceptar) {
        this.autoOcultarAlAceptar = autoOcultarAlAceptar;
    }

    /**
    * Devuelve si la ventana se oculta automaticamente al pulsar el boton de
    * aceptar.<br>
    *
    * @return true si la ventana se oculta automaticamente.<br>
    */
    public boolean isAutoOcultarAlAceptar() {
        return autoOcultarAlAceptar;
    }

    /**
    * Indica si la ventana se debe ocultar automaticamente al pulsar el boton
    * de cancelar.<br>
    * Por defecto es true.
    *
    * @param autoOcultarAlCancelar true si queremos que se oculte
    *        automaticamente.<br>
    */
    public void setAutoOcultarAlCancelar(boolean autoOcultarAlCancelar) {
        this.autoOcultarAlCancelar = autoOcultarAlCancelar;
    }

    /**
    * Devuelve si la ventana se oculta automaticamente al pulsar el boton de
    * cancelar.<br>
    *
    * @return true si se oculta automaticamente.<br>
    */
    public boolean isAutoOcultarAlCancelar() {
        return autoOcultarAlCancelar;
    }

    /**
    * Metodo a traves del que se recibe y guarda el componente. Invoca al
    * metodo inicializar.
    *
    * @param panelEditor El panel de edicion interno.<br>
    */
    public void setComponenteInterno(InterfaceEdicionDatos<Tipo> panelEditor) {
        if (!(panelEditor instanceof Component)) {
            log.warn("El panelEditor que se pasa no es un Component. Se ignora");
        }
        if (componenteInterno != null) {
            getContentPane().remove((Component) componenteInterno);
        }
        componenteInterno = panelEditor;
        getContentPane().add((Component) componenteInterno, this.dameGridBagConstraintsEditorInterno());
    }

    /**
    * Devuelve el panel con el editor de esta ventana de Aceptar/Cancelar
    *
    * @return El editor interno.<br>
    */
    public InterfaceEdicionDatos<Tipo> getComponenteInterno() {
        return componenteInterno;
    }

    /**
    * Muestra en la ventana de edici�n de datos el dato que se le pasa.<br>
    * Lo hace llamando al m�todo tomaDatos() del editor interno.
    *
    * @param elemento Un dato que entienda el editor interno.<br>
    */
    public void setDato(Tipo elemento) {
        if (componenteInterno == null) {
            log.warn("El panel interno es null. Se ignora el dato pasado");
            return;
        }
        componenteInterno.setDato(elemento);
    }

    /**
    * Devuelve los datos escritos por el operador en la ventana de edici�n. Lo
    * hace llamando al m�todo getDato() de la componenteInterno que tiene en
    * su interior. Dicha ventana no puede ser null porque el constructor de la
    * clase no lo admite.
    *
    * @param dato Lo rellena el editor interno si es posible.<br>
    *
    * @return El dato recogido del editor interno.<br>
    */
    public Tipo getDato() {
        if (componenteInterno == null) {
            return null;
        }
        return componenteInterno.getDato();
    }

    /**
    * Texto para el bot�n de aceptar
    *
    * @param textoAceptar texto para el boton de aceptar.<br>
    */
    public void setTextoAceptar(String textoAceptar) {
        if (JBuAceptar != null) {
            JBuAceptar.setText(textoAceptar);
        }
    }

    /**
    * Devuelve el texto del bot�n de aceptar
    *
    * @return el texto del boton de aceptar.<br>
    */
    public String getTextoAceptar() {
        if (JBuAceptar != null) {
            return JBuAceptar.getText();
        }
        return TEXTO_ACEPTAR;
    }

    /**
    * Texto para el bot�n de cancelar
    *
    * @param textoCancelar Texto para el boton de cancelar.<br>
    */
    public void setTextoCancelar(String textoCancelar) {
        if (JBuCancelar != null) {
            JBuCancelar.setText(textoCancelar);
        }
    }

    /**
    * Devuelve el texto del bot�n de cancelar
    *
    * @return El texto del boton de cancelar.<br>
    */
    public String getTextoCancelar() {
        if (JBuCancelar != null) {
            return JBuCancelar.getText();
        }
        return TEXTO_CANCELAR;
    }

    /**
    * Se le pasa la clase de tratamiento de error que tratar� el error cuando
    * validaDato() devuelve false.<br>
    *
    * @param tratamientoError Clase que trata el error.
    */
    public void setTratamientoError(InterfaceTratamientoError<Tipo> tratamientoError) {
        this.tratamientoError = tratamientoError;
    }

    /**
    * Devuelve la clase que trata el error cuando el validaDato() es false.
    *
    * @return La clase de tratamiento de error.
    */
    public InterfaceTratamientoError<Tipo> getTratamientoError() {
        return tratamientoError;
    }

    /**
    * Devuelve una nueva ventana DialogoAceptarCancelar cuyo padre sea la
    * ventana a la que pertenece el componente que se pasa.<br>
    * Busca la ventana padre del componente que se pasa. Si no lo hay,
    * devuelve una ventana de AceptarCancelar sin padre.<br>
    *
    * @param padre Un componente de la ventana que se desea que sea padre.
    *
    * @return Un AceptarCancelar hijo.
    */
    public static DialogoAceptarCancelar dameNuevaInstancia(Component padre) {
        Window ventanaPadre;
        if (padre instanceof Window) ventanaPadre = (Window) padre; else ventanaPadre = SwingUtilities.getWindowAncestor(padre);
        if (ventanaPadre instanceof Dialog) {
            return new DialogoAceptarCancelar((Dialog) ventanaPadre);
        }
        if (ventanaPadre instanceof Frame) {
            return new DialogoAceptarCancelar((Frame) ventanaPadre);
        }
        return new DialogoAceptarCancelar();
    }

    /**
    * Metodo para a?adir Listener a la ventana. Recibe dos parametros, el
    * primero es un ActionListener para que se le avise y el segundo es el
    * componente de esta ventana al cual se quiere a?adir.
    *
    * @param listener Anade un listener a la accion de aceptar o de
    *        cancelar.<br>
    * @param aceptarOCancelar Puede ser InterfaceAceptarCancelarEdicion.ACEPTAR
    *        o InterfaceAceptarCancelarEdicion.CANCELAR.<br>
    */
    public void addActionListener(ActionListener listener, int aceptarOCancelar) {
        switch(aceptarOCancelar) {
            case InterfaceAceptarCancelarEdicion.ACEPTAR:
                suscriptoresAceptar.add(listener);
                break;
            case InterfaceAceptarCancelarEdicion.CANCELAR:
                suscriptoresCancelar.add(listener);
                break;
        }
    }

    /**
    * Proporciona el boton aceptar de la ventana.
    *
    * @return El boton de aceptar.<br>
    */
    public JButton getBotonAceptar() {
        return this.JBuAceptar;
    }

    /**
    * Proporciona el boton cancelar de la ventana.
    *
    * @return El boton de cancelar.<br>
    */
    public JButton dameBotonCancelar() {
        return this.JBuCancelar;
    }

    /**
    * Hace la ventana editable o no, segun el boolean. Las clases hijas haran
    * que el operador no pueda modificar los datos de la ventana si el valor
    * de este boolean es false. Por defecto las ventanas deberan ser
    * editables.
    *
    * @param editable Si debe ser o no editable.<br>
    */
    public void hazEditable(boolean editable) {
        if (componenteInterno != null) {
            componenteInterno.hazEditable(editable);
        }
        JBuAceptar.setEnabled(editable);
    }

    /**
    * Elimina el action listener que se pasa de la lista de suscriptores de
    * aceptar o de cancelar, segun el segundo parametro.<br>
    *
    * @param listener Listener a eliminar.<br>
    * @param aceptarOCancelar Puede ser InterfaceAceptarCancelarEdicion.ACEPTAR
    *        o InterfaceAceptarCancelarEdicion.CANCELAR.<br>
    */
    public void removeActionListener(ActionListener listener, int aceptarOCancelar) {
        switch(aceptarOCancelar) {
            case InterfaceAceptarCancelarEdicion.ACEPTAR:
                suscriptoresAceptar.remove(listener);
                break;
            case InterfaceAceptarCancelarEdicion.CANCELAR:
                suscriptoresCancelar.remove(listener);
                break;
        }
    }

    /**
    * Elimina todos los suscriptores de aceptar o de cancelar, segun el valor
    * del segundo parametro.<br>
    *
    * @param aceptarOCancelar Puede ser InterfaceAceptarCancelarEdicion.ACEPTAR
    *        o InterfaceAceptarCancelarEdicion.CANCELAR.<br>
    */
    public void removeAllActionListener(int aceptarOCancelar) {
        switch(aceptarOCancelar) {
            case InterfaceAceptarCancelarEdicion.ACEPTAR:
                suscriptoresAceptar.clear();
                break;
            case InterfaceAceptarCancelarEdicion.CANCELAR:
                suscriptoresCancelar.clear();
                break;
        }
    }

    /**
    * Verifica los datos escritos por el operador en la ventana de edicion.
    * Llama al metodo validaDato() del editor interno y devuelve lo que
    * devuelve dicho metodo.
    *
    * @param error Se rellena con el error producido.<br>
    *
    * @return true si todos los datos escritos por el operador son
    *         correctos.<br>
    */
    public boolean validaDato(StringBuffer error) {
        return componenteInterno.validaDato(error);
    }

    /**
    * Avisa a los suscriptores de la lista que se le pasa de que ha habido un
    * aceptar o un cancelar.<br>
    *
    * @param listaSuscriptores Lista de suscriptores
    */
    protected void avisaSuscriptores(LinkedList<ActionListener> listaSuscriptores) {
        int i;
        for (i = 0; i < listaSuscriptores.size(); i++) {
            listaSuscriptores.get(i).actionPerformed(null);
        }
    }

    /**
    * Crea los botones de Aceptar y Cancelar. No coloca el panel de edicion
    * interno.
    */
    protected void inicializar() {
        if (JBuAceptar != null) {
            return;
        }
        JBuAceptar = new JButton(this.getTextoAceptar());
        JBuCancelar = new JButton(this.getTextoCancelar());
        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(JBuAceptar, dameGridBagConstraintsAceptar());
        getContentPane().add(JBuCancelar, dameGridBagConstraintsCancelar());
        JBuAceptar.addActionListener(listenerAceptar);
        JBuCancelar.addActionListener(listenerCancelar);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(windowListener);
        this.pack();
    }

    /**
    * Metodo al que se llama cuando se pulsa cancelar o se cierra la
    * ventana.<br>
    */
    private void canceladaVentana() {
        int resultado = JOptionPane.showConfirmDialog(this, new JLabel("<html>Se perderán los datos modificados.<br>" + "¿ Desea cancelar la edicion ?.</html>"));
        if (resultado != JOptionPane.YES_OPTION) {
            return;
        }
        avisaSuscriptores(suscriptoresCancelar);
        if (autoOcultarAlCancelar) {
            setVisible(false);
        }
    }

    /**
    * Metodo que devuelve los Recursos del Boton de Aceptar.
    *
    * @return El GridBagConstraints para el boton de Aceptar.<br>
    */
    private GridBagConstraints dameGridBagConstraintsAceptar() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.insets = new Insets(2, 2, 2, 2);
        return c;
    }

    /**
    * Metodo que devuelve los Recursos del Boton de Cancelar.
    *
    * @return El GridBagConstraints para el boton de cancelar.<br>
    */
    private GridBagConstraints dameGridBagConstraintsCancelar() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.insets = new Insets(2, 2, 2, 2);
        return c;
    }

    /**
    * Metodo que devuelve los Recursos de la Ventana de Edicion de Datos.
    *
    * @return El GridBagConstraints para el editor interno.<br>
    */
    private GridBagConstraints dameGridBagConstraintsEditorInterno() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(2, 2, 2, 2);
        c.fill = GridBagConstraints.BOTH;
        return c;
    }
}
