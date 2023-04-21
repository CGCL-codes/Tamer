package inbox;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import com.alsutton.xmlparser.objectmodel.Node;
import UIPackage.GlogColor;
import UIPackage.GlogFont;
import UIPackage.GlogItem;

/**
 * This is the friend object.
 * @author Ray
 *
 */
public class Message extends GlogItem {

    private String body;

    private String from;

    private String received;

    private String subject;

    /**
	 * Constructor
	 * @param username
	 */
    public Message(Node message) {
        if (message == null) {
            body = "";
            from = "";
            received = "";
            subject = "";
        } else {
            Vector fList = message.getChildrenByName("from");
            if (fList != null) {
                from = ((Node) fList.elementAt(0)).getText();
            }
            Vector bList = message.getChildrenByName("body");
            if (fList != null) {
                body = ((Node) bList.elementAt(0)).getText();
            }
            Vector sList = message.getChildrenByName("subject");
            if (fList != null) {
                subject = ((Node) sList.elementAt(0)).getText();
            }
            Vector rList = message.getChildrenByName("received");
            if (fList != null) {
                received = ((Node) rList.elementAt(0)).getText();
            }
        }
    }

    /**
	 * Draw...
	 * @return the HEIGHT!! 
	 */
    public int draw(Graphics g, int x, int y, int width, int scrollY, int style) {
        int height = 0;
        if (style == 0) {
            height = Font.getDefaultFont().getHeight();
            g.setColor(GlogColor.PALEBLUE);
            g.fillRoundRect(x, y + scrollY, width, height, 10, 10);
            g.setColor(GlogColor.SKYBLUE);
            g.drawString(from, x + 4, y + scrollY, Graphics.LEFT | Graphics.TOP);
            g.setFont(GlogFont.small);
            g.drawString(received, width, y + scrollY, Graphics.RIGHT | Graphics.TOP);
            g.setFont(Font.getDefaultFont());
            return height;
        } else if (style == 1) {
            height = Font.getDefaultFont().getHeight();
            g.setColor(GlogColor.WHITE);
            g.fillRoundRect(x, y + scrollY, width, height, 10, 10);
            g.setColor(GlogColor.BLACK);
            g.drawString(from, x + 4, y + scrollY, Graphics.LEFT | Graphics.TOP);
            g.setFont(GlogFont.small);
            g.drawString(received, width, y + scrollY, Graphics.RIGHT | Graphics.TOP);
            g.setFont(Font.getDefaultFont());
            return height;
        } else if (style == 2) {
            height = 0;
            height = Font.getDefaultFont().getBaselinePosition() * 3;
            g.setColor(GlogColor.SKYBLUE);
            g.fillRoundRect(x, y + scrollY, width, Font.getDefaultFont().getBaselinePosition() * 3, 10, 10);
            g.setColor(GlogColor.WHITE);
            g.fillRoundRect(x + 1, y + scrollY + 1, width - 3, Font.getDefaultFont().getBaselinePosition() * 3 - 3, 10, 10);
            g.setColor(GlogColor.PINK);
            g.drawString(from, x + 4, y + scrollY, Graphics.LEFT | Graphics.TOP);
            g.setFont(GlogFont.small);
            g.setColor(GlogColor.BLACK);
            g.drawString(received, width - 2, y + scrollY, Graphics.RIGHT | Graphics.TOP);
            g.setFont(Font.getDefaultFont());
            int offy = Font.getDefaultFont().getHeight();
            g.drawString(subject, x + 5, y + offy + scrollY, Graphics.LEFT | Graphics.TOP);
            return height;
        }
        return height;
    }

    /**
	 * ??
	 * @param text
	 * @param maxW
	 * @return
	 */
    private Vector stringToArray(String text, int maxW) {
        maxW -= Font.getDefaultFont().stringWidth("---");
        int i = 0;
        int begin = 0;
        Vector v = new Vector();
        while (i < text.length()) {
            while (Font.getDefaultFont().substringWidth(text, begin, i - begin) < maxW) {
                i++;
                if (i >= text.length()) break;
            }
            if (i == text.length()) {
                v.addElement(text.substring(begin, i));
            } else if (text.substring(i, i).equals(" ")) {
                v.addElement(text.substring(begin, i));
            } else {
                v.addElement(text.substring(begin, i) + "-");
            }
            begin = i;
        }
        return v;
    }

    /**
	 * @return the from
	 */
    public String getFrom() {
        return from;
    }

    /**
	 * @return the subject
	 */
    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
