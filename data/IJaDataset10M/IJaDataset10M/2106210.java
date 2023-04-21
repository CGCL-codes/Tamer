package converter.gui.dialog;

import converter.gui.SippXMLConverter;
import converter.res.interfaces.IConstants;
import java.awt.Point;
import java.util.StringTokenizer;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author Christian
 */
public class Validate extends JDialog implements IConstants {

    JFrame parent;

    private JEditorPane errorsUAC = new JEditorPane();

    private JEditorPane errorsUAS = new JEditorPane();

    private JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    private String colorUAS;

    private String colorUAC;

    private String type[];

    private String message[];

    public Validate(String[] type, String[] message, JFrame parent) {
        this.parent = parent;
        this.type = type;
        this.message = message;
        makeLayout();
        Point location = parent.getLocation();
        int width = (parent.getWidth() - this.getWidth()) / 2;
        int height = (parent.getSize().height - this.getHeight()) / 2;
        setLocation(width < 0 ? (int) (screenWidth - this.getWidth()) / 2 : width + location.x, height < 0 ? (int) (screenHeight - this.getHeight()) / 2 : height + location.y);
        addListeners();
        setTitle("XML validation");
    }

    public void makeLayout() {
        errorsUAC.setContentType("text/html");
        errorsUAS.setContentType("text/html");
        errorsUAC.setEditable(false);
        errorsUAS.setEditable(false);
        updatePane(type, message);
        sp.setLeftComponent(new JScrollPane(errorsUAC));
        sp.setRightComponent(new JScrollPane(errorsUAS));
        add(sp);
        setSize(600, 200);
        setVisible(true);
        sp.setDividerLocation(0.5);
    }

    public void addListeners() {
        errorsUAC.addHyperlinkListener(new GoToListener());
        errorsUAS.addHyperlinkListener(new GoToListener());
    }

    public void updatePane(String[] type, String[] message) {
        colorUAC = "#33CC00";
        colorUAS = "#33CC00";
        if (type[0].startsWith("Warning")) colorUAC = "#FFFF00"; else if (type[0].startsWith("Error")) colorUAC = "#FF0000"; else if (type[0].startsWith("Fatal Error")) colorUAC = "#660000";
        if (type[1].startsWith("Warning")) colorUAS = "#FFFF00"; else if (type[1].startsWith("Error")) colorUAS = "#FF0000"; else if (type[1].startsWith("Fatal Error")) colorUAS = "#660000";
        if (type[0].isEmpty()) errorsUAC.setText("<center><h3><u>User Agent Client</u></h3></center><font color=" + colorUAC + ">XML file OK!</font>"); else errorsUAC.setText("<center><h3><u>User Agent Client</u></h3></center><a href='" + type[0].replaceFirst("(^.+@)(\\d+:\\d+$)", "$2") + "'>" + type[0] + "</a><br/><font color=" + colorUAC + ">" + message[0] + "</font>");
        if (type[1].isEmpty()) errorsUAS.setText("<center><h3><u>User Agent Server</u></h3></center><font color=" + colorUAC + ">XML file OK!</font>"); else errorsUAS.setText("<center><h3><u>User Agent Server</u></h3></center><a href='" + type[1].replaceFirst("(^.+@)(\\d+:\\d+$)", "$2") + "'>" + type[1] + "</a><br/><font color=" + colorUAS + ">" + message[1] + "</font>");
    }

    class GoToListener implements HyperlinkListener {

        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == EventType.ACTIVATED) {
                StringTokenizer st = new StringTokenizer(e.getDescription(), ":");
                int lineNumber = Integer.parseInt(st.nextToken());
                int position = Integer.parseInt(st.nextToken());
                if (e.getSource() == errorsUAC) {
                    ((SippXMLConverter) parent).getTpText().setSelectedIndex(0);
                    ((SippXMLConverter) parent).getTaUAC().setCaretPosition(((SippXMLConverter) parent).getTaUAC().getLineStartOffset(lineNumber - 1) + position - 1);
                } else {
                    ((SippXMLConverter) parent).getTpText().setSelectedIndex(1);
                    ((SippXMLConverter) parent).getTaUAS().setCaretPosition(((SippXMLConverter) parent).getTaUAS().getLineStartOffset(lineNumber - 1) + position - 1);
                }
            }
        }
    }
}
