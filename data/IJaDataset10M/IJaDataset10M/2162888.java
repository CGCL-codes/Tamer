package swg.gui.common;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Stack;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import swg.SWGAide;
import swg.gui.SWGFrame;

/**
 * This class is used py several GUI components to show their help pages, or the
 * general help page if no particular page is set
 * 
 * @see #pushHelpPage(URL)
 * @see #removeHelpPage(URL)
 * @see #showHelp()
 * @see #showHelpPage(URL)
 * @author <a href="mailto:simongronlund@gmail.com">Simon Gronlund</a> aka
 *         Europe-Chimaera.Zimoon
 */
@SuppressWarnings("serial")
public class SWGHelpScreen extends JDialog {

    /**
     * The owner of this object
     */
    private final SWGFrame frame;

    /**
     * A stack of the help pages to show
     */
    private Stack<URL> pageStack;

    /**
     * The editor pane for the content
     */
    private JEditorPane textPane;

    /**
     * Creates a dialog for help screens with a editor pane. This class will
     * present the topmost URL at its stack of help pages, assuming each GUI
     * view adds its own help page when gaining focus. The bottommost help page
     * is the general help page, which can never be removed from the stack.
     * 
     * @param frame the frame of this application
     */
    public SWGHelpScreen(SWGFrame frame) {
        super(frame, false);
        this.frame = frame;
        pageStack = new Stack<URL>();
        pageStack.add(makeDefaultURL());
        textPane = new JEditorPane();
        textPane.setEditable(false);
        JScrollPane jsp = new JScrollPane(textPane);
        jsp.setMinimumSize(new Dimension(150, 120));
        int vs = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        jsp.setVerticalScrollBarPolicy(vs);
        add(jsp);
        getGlassPane().addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyTyped(e);
            }
        });
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                doClosing();
            }
        });
        ActionListener escapeAction = new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                doClosing();
                setVisible(false);
            }
        };
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        int cond = JComponent.WHEN_IN_FOCUSED_WINDOW;
        getRootPane().registerKeyboardAction(escapeAction, key, cond);
    }

    /**
     * Executes some routines when this dialog is closing
     */
    void doClosing() {
        SWGFrame.getPrefsKeeper().add("helpScreenLocation", getLocation());
        SWGFrame.getPrefsKeeper().add("helpScreenSize", getSize());
    }

    /**
     * Returns the URL for the topmost object at the stack of help pages
     * 
     * @return the URL for the topmost object at the stack of help pages
     */
    private URL getURL() {
        return pageStack.peek();
    }

    /**
     * Returns an URL for the default help page to show
     * 
     * @return an URL for the default help page to show, <code>null</code> if
     *         not found
     */
    private URL makeDefaultURL() {
        return SWGAide.class.getResource("docs/help_general_en.html");
    }

    /**
     * Pushes <code>helpPage</code> onto the stack of help pages which will
     * become the topmost item
     * 
     * @param helpPage an URL to the help page to push onto the stack of help
     *        pages
     */
    public void pushHelpPage(URL helpPage) {
        pageStack.push(helpPage);
    }

    /**
     * Removes <code>helpPage</code> from the stack of help pages, even if it is
     * not the topmost page
     * 
     * @param helpPage the help page to remove from the stack
     */
    public void removeHelpPage(URL helpPage) {
        while (pageStack.remove(helpPage)) continue;
    }

    /**
     * Sets this objects location on screen and its size
     */
    private void setAppearance() {
        Dimension dim = (Dimension) SWGFrame.getPrefsKeeper().get("helpScreenSize");
        Point loc = (Point) SWGFrame.getPrefsKeeper().get("helpScreenLocation");
        if (dim == null || loc == null) {
            dim = frame.getSize();
            int w = dim.width > 400 ? 400 : dim.width - 50;
            int h = dim.height > 500 ? 500 : dim.height - 50;
            dim = new Dimension(w, h);
            loc = frame.getLocationOnScreen();
            loc.x += ((dim.width - w) >> 1);
            loc.y += ((dim.height - h) >> 1);
        }
        setSize(dim);
        setPreferredSize(dim);
        loc = SWGGuiUtils.ensureOnScreen(loc, this.getSize());
        setLocation(loc);
        setVisible(true);
    }

    /**
     * Shows this object with the topmost URL on the stack. A view with its own
     * help page first pushes a page onto the stack when it gains focus, and
     * removes its help page when it looses focus.
     */
    public void showHelp() {
        showHelpPage(getURL());
    }

    /**
     * Shows this object with the URL at <code>index</code> of the stack.
     * 
     * @param index the index for the URL in the stack of help pages
     */
    public void showHelp(int index) {
        showHelpPage(pageStack.get(index));
    }

    /**
     * Shows this object with the content of <code>page</code>. This method does
     * not add the URL to the stack nor removes it, hence this method is useful
     * for situations when a there is no focused views that "owns" a help page,
     * such as dialog screens or other static objects.
     * 
     * @param page the URL for the page to show
     */
    public void showHelpPage(URL page) {
        try {
            textPane.setPage(page);
        } catch (IOException e) {
            SWGAide.printError("SWGHelpScreen:show:", e);
            textPane.setText(e.getMessage());
        }
        setAppearance();
    }
}
