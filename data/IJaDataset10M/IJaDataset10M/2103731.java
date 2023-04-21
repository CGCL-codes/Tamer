package com.celiosilva.swingDK.frames;

import com.celiosilva.swingDK.config.WindowNaming;
import com.celiosilva.swingDK.dataAbstractFields.SwingDKConstants;
import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Esta classe tem a responsabilidade de herdar de JInternalFrame, adicionando alguns atributos padr�o para assim facilitar o
 * desenvolvimento e a parte de testes. A id�ia � fazer com que cada componente JInternalFrame seja f�cilmente testado.
 * @author celio@celiosilva.com
 */
public class InternalFrame extends JInternalFrame implements SwingDKConstants {

    private static int Layer = 0;

    private boolean exitOnEsc;

    static {
        try {
            if (UIManager.getLookAndFeel().getName().equalsIgnoreCase("Metal")) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        }
    }

    private boolean modalMode = false;

    private boolean dragMode = false;

    private static WindowNaming windowNaming;

    private JButton exitButton;

    private Component owner;

    private static int ordemCascata = 1;

    /**
     * Cria uma janela padr�o do tipo JInternalFrame
     */
    public InternalFrame() {
        this("Sistema de Gest�o Lar Frederico Ozanam", false, true, false, true);
    }

    public InternalFrame(Component owner) {
        this();
        if (owner != null) {
            this.setModalMode(true);
            this.setOwner(owner);
        }
    }

    public void reInitComponents() {
    }

    public void setExitOnEsc(boolean exitOnEsc) {
        this.exitOnEsc = exitOnEsc;
    }

    public boolean isExitOnEsc() {
        return exitOnEsc;
    }

    /**
     * Cria uma janela do tipo JInternalFrame com todos os atributos passados pelo construtor
     * @param title
     * @param resizable
     * @param closable
     * @param maximizable
     * @param iconafiable
     * @return
     */
    public InternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconafiable) {
        super(title, resizable, closable, maximizable, iconafiable);
        if (this.getWindowNaming() != null) {
            this.setTitle(this.getWindowNaming().getWindowName(this));
        }
        setIcon();
    }

    public void centerScreen() {
        if (!isModalMode()) {
            int width = (int) (this.getBounds().getWidth());
            int height = (int) (this.getBounds().getHeight());
            Dimension screenSize = this.getParent().getSize();
            int freeSpace = ((int) (screenSize.height * ((0.036) * getOrdemCascata())));
            setOrdemCascata(getOrdemCascata() + 1);
            setBounds((screenSize.width - width - freeSpace), (int) (freeSpace), width, height);
            if (getOrdemCascata() > 4) {
                setOrdemCascata(1);
            }
        } else {
            int width = (int) (this.getBounds().getWidth());
            int height = (int) (this.getBounds().getHeight());
            double x = ((this.getOwner().getBounds().getWidth() - this.getBounds().getWidth()) / 2) + this.getOwner().getLocation().getX();
            double y = ((this.getOwner().getBounds().getHeight() - this.getBounds().getHeight()) / 2) + this.getOwner().getLocation().getY();
            if (this.getOwner().getBounds().getHeight() < height) setBounds(((int) x), ((int) this.getOwner().getLocation().getY()), width, height); else setBounds(((int) x), ((int) y), width, height);
        }
    }

    public void setExitActionListener() {
        if (this.getExitButton() != null) {
            this.getExitButton().addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
        }
    }

    public Component getOwner() {
        return owner;
    }

    public void setOwner(Component owner) {
        this.owner = owner;
    }

    public boolean isModalMode() {
        return modalMode;
    }

    public void setModalMode(boolean modalMode) {
        this.modalMode = modalMode;
    }

    public static WindowNaming getWindowNaming() {
        return windowNaming;
    }

    public static void setWindowNaming(WindowNaming windowNamin) {
        windowNaming = windowNamin;
    }

    /**
     * Criar um teste padr�o para este JInternalFrame. Este m�todo cria um JFrame padr�o e um JLayeredPane para ent�o adicionar
     * este componente ao JLayeredPane. A id�ia � criar um modo padr�o para descrever os testes sem muito trabalha na instancia��o
     * de um JFrame.
     */
    public void testMe() {
        JFrame janela = new JFrame("SwingDKInternalFrameTest");
        JDesktopPane pane = new JDesktopPane();
        janela.getContentPane().add(pane);
        pane.add(this);
        janela.setExtendedState(JFrame.MAXIMIZED_BOTH);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setVisible(true);
        this.setVisible(true);
    }

    private void setIcon() {
        Icon icon = new ImageIcon(getClass().getResource("/com/celiosilva/swingDK/images/icon.png"));
        this.setFrameIcon(icon);
    }

    public void setExitButton(JButton exitButton) {
        this.exitButton = exitButton;
        setExitActionListener();
    }

    public JButton getExitButton() {
        return exitButton;
    }

    /**
     * Adiciona uma nova janela em tempo de execu��o. Este m�todo localiza o JLayeredPane respons�vel pela adi��o de novos
     * InternalFrames e adiciona o componente passado como argumento
     * @param window nova janela que ser� adiciona � este mesmo componente.
     */
    public void addWindow(InternalFrame window) {
        this.getParent().add(window);
        window.centerScreen();
    }

    private void showModal() {
        JInternalFrame owner = null;
        boolean maximum = false;
        if (this.getOwner() instanceof JInternalFrame) {
            owner = (JInternalFrame) this.getOwner();
            maximum = owner.isMaximum();
        }
        show();
        try {
            owner.setMaximum(maximum);
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }
        startModal();
    }

    public void setSelected(boolean selected) throws PropertyVetoException {
        super.setSelected(true);
    }

    public void setVisible(boolean visible) {
        if (visible && modalMode) {
            this.setIconifiable(false);
            this.showModal();
        } else {
            if (!visible && modalMode) stopModal();
            super.setVisible(visible);
        }
    }

    private synchronized void startModal() {
        modalMode = true;
        if (isVisible() && !isShowing()) {
            Container parent = this.getParent();
            while (parent != null) {
                if (parent.isVisible() == false) {
                    parent.setVisible(true);
                }
                parent = parent.getParent();
            }
        }
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                EventQueue theQueue = getToolkit().getSystemEventQueue();
                while (isVisible()) {
                    AWTEvent event = theQueue.getNextEvent();
                    Object src = event.getSource();
                    if (src instanceof Component && event instanceof MouseEvent) {
                        MouseEvent me = (MouseEvent) event;
                        Point point = javax.swing.SwingUtilities.convertPoint((Component) src, me.getPoint(), this);
                        Dimension size = this.getSize();
                        if (dragMode || (point.x >= 0 && point.y >= 0 && point.x < size.width && point.y < size.height)) {
                            ((Component) src).dispatchEvent(event);
                            if (!dragMode && me.getID() == MouseEvent.MOUSE_PRESSED) dragMode = true;
                        }
                        if (dragMode && me.getID() == MouseEvent.MOUSE_RELEASED) dragMode = false;
                    } else if (event instanceof ActiveEvent) {
                        ((ActiveEvent) event).dispatch();
                    } else if (src instanceof Component) {
                        ((Component) src).dispatchEvent(event);
                    } else if (src instanceof MenuComponent) {
                        ((MenuComponent) src).dispatchEvent(event);
                    } else {
                        System.err.println("unable to dispatch event: " + event);
                    }
                }
            } else while (isVisible()) wait();
        } catch (InterruptedException e) {
        }
    }

    private synchronized void stopModal() {
        modalMode = false;
        notifyAll();
    }

    public static int getOrdemCascata() {
        return ordemCascata;
    }

    public static void setOrdemCascata(int ordemCasc) {
        ordemCascata = ordemCasc;
    }

    public int getCurrentLayer() {
        return Layer;
    }

    public int getNextLayer() {
        return this.getCurrentLayer() + 1;
    }
}
