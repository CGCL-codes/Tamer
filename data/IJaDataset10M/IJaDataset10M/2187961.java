package jeliot.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import jeliot.util.DebugUtil;
import jeliot.util.ResourceBundles;
import jeliot.util.UserProperties;

/**
 * OutputConsole is a text area on which the output of a user's
 * program is printed.
 *
 * @author Pekka Uronen
 * @author Niko Myller
 */
public class OutputConsole extends JTextPane {

    /**
     * The resource bundle for gui package
     */
    private static UserProperties propertiesBundle = ResourceBundles.getGuiUserProperties();

    /**
     * The resource bundle for gui package
     */
    private static ResourceBundle messageBundle = ResourceBundles.getGuiMessageResourceBundle();

    /** A scroll pane that contains the output console. */
    public final JScrollPane container = new JScrollPane(this) {

        public Dimension getMaximumSize() {
            Dimension sms = super.getMaximumSize();
            return new Dimension(sms.width, model == null ? sms.height : model.getMaximumSize().height);
        }

        public Dimension getPreferredSize() {
            Dimension sps = super.getPreferredSize();
            return new Dimension(sps.width, model == null ? sps.height : model.getPreferredSize().height);
        }
    };

    /** A component that is queried for the preferred and maximum height of the console. */
    private Component model;

    /** The console's popup menu has one choice for emptying the console. */
    private JPopupMenu menu = new JPopupMenu();

    {
        JMenuItem menuItem;
        menuItem = new JMenuItem(messageBundle.getString("popup_menu.clear"));
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setText("");
            }
        });
    }

    private Font font;

    private StyledDocument doc;

    /** Creates a new output console.
      *
      *@param   model   The model is a component that is queried to set
      *         console's preferred and maximum height. May be null, in
      *         which case it has no effect (no error to be null).
      */
    public OutputConsole(Component model) {
        this.model = model;
        this.font = new Font(propertiesBundle.getStringProperty("font.output.family"), Font.PLAIN, Integer.parseInt(propertiesBundle.getStringProperty("font.output.size")));
        setFont(font);
        setEditable(false);
        doc = getStyledDocument();
        addStylesToDocument(doc);
        TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), messageBundle.getString("title.output"));
        title.setTitlePosition(TitledBorder.ABOVE_TOP);
        container.setBorder(title);
        addMouseListener(new MouseListener() {

            public void mousePressed(MouseEvent evt) {
                maybeShowPopup(evt);
            }

            public void mouseReleased(MouseEvent evt) {
                maybeShowPopup(evt);
            }

            public void mouseClicked(MouseEvent evt) {
                maybeShowPopup(evt);
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }
        });
    }

    /**
     * 
     * @param str
     */
    public void output(String str) {
        try {
            doc.insertString(doc.getLength(), str, doc.getStyle("bold"));
        } catch (BadLocationException e) {
            DebugUtil.handleThrowable(e);
        }
        this.setCaretPosition(doc.getLength());
    }

    /**
     * 
     * @param str
     */
    public void input(String str) {
        try {
            doc.insertString(doc.getLength(), str, doc.getStyle("italic"));
        } catch (BadLocationException e) {
            DebugUtil.handleThrowable(e);
        }
        this.setCaretPosition(doc.getLength());
    }

    /** Checks if a mouse click is a popup menu trigger and if
      * it is, shows the popup menu.
      *
      * @param  evt The mouse event that is supposed to be a popup menu trigger.
      */
    private void maybeShowPopup(MouseEvent evt) {
        if (!evt.isPopupTrigger()) {
            return;
        }
        menu.show(this, evt.getX(), evt.getY());
    }

    protected void addStylesToDocument(StyledDocument doc) {
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(regular, this.font.getFamily());
        StyleConstants.setFontSize(regular, this.font.getSize());
        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);
        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);
        s = doc.addStyle("small", regular);
        StyleConstants.setFontSize(s, this.font.getSize() - 2);
        s = doc.addStyle("large", regular);
        StyleConstants.setFontSize(s, this.font.getSize() + 4);
    }
}
