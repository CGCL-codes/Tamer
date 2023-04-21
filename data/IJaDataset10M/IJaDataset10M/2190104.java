package javax.swing;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;

/**
 * A top-level container that is usually used in web browsers.
 *
 * @author original author unknown
 */
public class JApplet extends Applet implements RootPaneContainer, Accessible {

    /**
   * Provides accessibility support for <code>JApplet</code>.
   */
    protected class AccessibleJApplet extends Applet.AccessibleApplet {

        /**
     * Creates a new instance of <code>AccessibleJApplet</code>.
     */
        public AccessibleJApplet() {
            super();
        }
    }

    /**
   * The accessible context for this <code>JApplet</code>.
   */
    protected AccessibleContext accessibleContext;

    private static final long serialVersionUID = 7269359214497372587L;

    protected JRootPane rootPane;

    /**
   * @specnote rootPaneCheckingEnabled is false to comply with J2SE 5.0
   */
    protected boolean rootPaneCheckingEnabled = false;

    public JApplet() {
        super.setLayout(new BorderLayout(1, 1));
        getRootPane();
        setRootPaneCheckingEnabled(true);
    }

    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }

    public void setLayout(LayoutManager manager) {
        if (isRootPaneCheckingEnabled()) getContentPane().setLayout(manager); else super.setLayout(manager);
    }

    public void setLayeredPane(JLayeredPane layeredPane) {
        getRootPane().setLayeredPane(layeredPane);
    }

    public JLayeredPane getLayeredPane() {
        return getRootPane().getLayeredPane();
    }

    public JRootPane getRootPane() {
        if (rootPane == null) setRootPane(createRootPane());
        return rootPane;
    }

    protected void setRootPane(JRootPane root) {
        if (rootPane != null) remove(rootPane);
        rootPane = root;
        add(rootPane, BorderLayout.CENTER);
    }

    protected JRootPane createRootPane() {
        return new JRootPane();
    }

    public Container getContentPane() {
        return getRootPane().getContentPane();
    }

    public void setContentPane(Container contentPane) {
        getRootPane().setContentPane(contentPane);
    }

    public Component getGlassPane() {
        return getRootPane().getGlassPane();
    }

    public void setGlassPane(Component glassPane) {
        getRootPane().setGlassPane(glassPane);
    }

    protected void addImpl(Component comp, Object constraints, int index) {
        if (isRootPaneCheckingEnabled()) getContentPane().add(comp, constraints, index); else super.addImpl(comp, constraints, index);
    }

    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) accessibleContext = new AccessibleJApplet();
        return accessibleContext;
    }

    public JMenuBar getJMenuBar() {
        return getRootPane().getJMenuBar();
    }

    public void setJMenuBar(JMenuBar menubar) {
        getRootPane().setJMenuBar(menubar);
    }

    protected String paramString() {
        return super.paramString();
    }

    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
    }

    public void remove(Component comp) {
        if (comp == rootPane) super.remove(rootPane); else getContentPane().remove(comp);
    }

    protected boolean isRootPaneCheckingEnabled() {
        return rootPaneCheckingEnabled;
    }

    protected void setRootPaneCheckingEnabled(boolean enabled) {
        rootPaneCheckingEnabled = enabled;
    }

    public void update(Graphics g) {
        paint(g);
    }
}
