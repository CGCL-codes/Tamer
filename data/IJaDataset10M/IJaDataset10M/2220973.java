package za.co.me23.chat;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Jean-Pierre
 */
public abstract class MessageScreen extends Screen {

    public MessageScreen(String iCaption, String iLeft, String iMiddle, String iRight) {
        super(iCaption, iLeft, iMiddle, iRight, 'm');
    }

    public final void moveDown() {
        if (messages.size() > 0) if (viewLine < countItems() - 1) viewLine++;
    }

    public final void moveUp() {
        if (messages.size() > 0) if (viewLine > 0) viewLine--;
    }

    public final int countItems() {
        int count = 0;
        for (int i = 0; i < messages.size(); i++) count += get(i).items.size();
        return count;
    }

    public final void add(Message iMessage) {
        messages.addElement(iMessage);
        newFlag = false;
    }

    public final void removeAll() {
        messages.removeAllElements();
        newFlag = true;
    }

    public final Message get(int iIndex) {
        return (Message) messages.elementAt(iIndex);
    }

    public final void paintContent(Graphics g) {
        if (newFlag) {
            new SystemMessage("<<Empty>>").paint(g, 0, 0);
        } else {
            int line = -viewLine;
            for (int i = 0; i < messages.size(); i++) for (int j = 0; j < get(i).items.size(); j++) if (line >= 0) get(i).paint(g, line++, j); else line++;
        }
        new ScrollBar(countItems(), MAX, viewLine).paint(g);
    }

    public Vector messages = new Vector();

    private int viewLine = 0;

    private boolean newFlag = true;

    private static int MAX = Utility.getMaxLineItems();
}
