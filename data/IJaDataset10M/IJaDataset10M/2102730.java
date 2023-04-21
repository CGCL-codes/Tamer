package redes.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class JanelaPrincipal extends JFrame {

    private JPanel mainPane = null;

    private JSplitPane paneLogs = null;

    private JScrollPane scrollPaneEsquerda = null;

    private JScrollPane scrollPaneDireita = null;

    private JTextArea textAreaLogIn = null;

    private JTextArea textAreaLogOut = null;

    private JPanel panelComandos = null;

    /**
     * This is the default constructor
     */
    public JanelaPrincipal() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(537, 510);
        this.setContentPane(getMainPane());
        this.setTitle("Trabalho de Redes :: Alvaro & Pedro & Marcelo v0.0.0.1beta");
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getMainPane() {
        if (mainPane == null) {
            mainPane = new JPanel();
            mainPane.setLayout(new BorderLayout());
            mainPane.setEnabled(false);
            mainPane.add(getPaneLogs(), java.awt.BorderLayout.CENTER);
            mainPane.add(getPanelComandos(), java.awt.BorderLayout.SOUTH);
        }
        return mainPane;
    }

    /**
     * This method initializes paneLogs	
     * 	
     * @return javax.swing.JSplitPane	
     */
    private JSplitPane getPaneLogs() {
        if (paneLogs == null) {
            paneLogs = new JSplitPane();
            paneLogs.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            paneLogs.setOneTouchExpandable(true);
            paneLogs.setResizeWeight(0.5D);
            paneLogs.setPreferredSize(new java.awt.Dimension(200, 5));
            paneLogs.setLeftComponent(getScrollPaneEsquerda());
            paneLogs.setRightComponent(getScrollPaneDireita());
            paneLogs.setOrientation(javax.swing.JSplitPane.HORIZONTAL_SPLIT);
        }
        return paneLogs;
    }

    /**
     * This method initializes scrollPaneDireita	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getScrollPaneEsquerda() {
        if (scrollPaneEsquerda == null) {
            scrollPaneEsquerda = new JScrollPane();
            scrollPaneEsquerda.setPreferredSize(new java.awt.Dimension(50, 3));
            scrollPaneEsquerda.setViewportView(getTextAreaLogIn());
        }
        return scrollPaneEsquerda;
    }

    /**
     * This method initializes scrollPaneDireita	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getScrollPaneDireita() {
        if (scrollPaneDireita == null) {
            scrollPaneDireita = new JScrollPane();
            scrollPaneDireita.setPreferredSize(new java.awt.Dimension(50, 3));
            scrollPaneDireita.setViewportView(getTextAreaLogOut());
        }
        return scrollPaneDireita;
    }

    /**
     * This method initializes textAreaLogIn	
     * 	
     * @return javax.swing.JTextArea	
     */
    private JTextArea getTextAreaLogIn() {
        if (textAreaLogIn == null) {
            textAreaLogIn = new JTextArea();
        }
        return textAreaLogIn;
    }

    /**
     * This method initializes textAreaLogOut	
     * 	
     * @return javax.swing.JTextArea	
     */
    private JTextArea getTextAreaLogOut() {
        if (textAreaLogOut == null) {
            textAreaLogOut = new JTextArea();
        }
        return textAreaLogOut;
    }

    /**
     * This method initializes panelComandos	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getPanelComandos() {
        if (panelComandos == null) {
            panelComandos = new JPanel();
            panelComandos.setPreferredSize(new java.awt.Dimension(10, 200));
        }
        return panelComandos;
    }
}
