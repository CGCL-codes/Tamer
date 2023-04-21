package tm.displayEngine;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Vector;
import tm.interfaces.DisplayContextInterface;
import tm.interfaces.ImageSourceInterface;
import tm.subWindowPkg.SmallButton;
import tm.subWindowPkg.ToolBar;

public class ExpressionDisplay extends DisplayAdapter {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3372985987803053351L;

    private static final int LEFTMARGIN = 4;

    private static final int TOPMARGIN = 2;

    public static final char MARKER1 = '￿';

    public static final char MARKER2 = '￾';

    public static final char MARKER3 = '�';

    public static final char MARKER4 = '￼';

    public static final char ENDMARKER = '￻';

    String theExpression = "";

    int advances[];

    public ExpressionDisplay(DisplayManager dm, String configId) {
        super(dm, configId);
        ImageSourceInterface imageSource = context.getImageSource();
        SmallButton[] buttons = new SmallButton[2];
        buttons[0] = new SmallButton(SmallButton.BACKUP, imageSource);
        buttons[0].setToolTipText("Backup Expression Engine");
        buttons[1] = new SmallButton(SmallButton.ADVANCE, imageSource);
        buttons[1].setToolTipText("Step Expression Engine");
        toolBar = new ToolBar(buttons, "West");
        mySubWindow.addToolBar(toolBar);
        setPreferredSize(this.getViewportSize());
    }

    public void refresh() {
        theExpression = commandProcessor.getExpression();
        super.refresh();
    }

    public void buttonPushed(int i) {
        if (i == 0) commandProcessor.goBack();
        if (i == 1) commandProcessor.goForward();
    }

    public void drawArea(Graphics2D g) {
        int advance = LEFTMARGIN;
        g.setFont(context.getDisplayFont());
        if (theExpression.length() > 0) {
            char tempArray[] = theExpression.toCharArray();
            FontMetrics fm = g.getFontMetrics();
            advances = fm.getWidths();
            int baseline = TOPMARGIN + fm.getAscent();
            g.setColor(Color.black);
            Color currColor = Color.black;
            boolean currUnderline = false;
            Vector attrStack = new Vector();
            for (int i = 0, sz = theExpression.length(); i < sz; ++i) {
                char c = tempArray[i];
                switch(c) {
                    case MARKER1:
                        attrStack.addElement(currColor);
                        currColor = Color.red;
                        g.setColor(currColor);
                        break;
                    case MARKER2:
                        attrStack.addElement(new Boolean(currUnderline));
                        currUnderline = false;
                        break;
                    case MARKER3:
                        attrStack.addElement(new Boolean(currUnderline));
                        currUnderline = true;
                        break;
                    case MARKER4:
                        attrStack.addElement(currColor);
                        currColor = Color.blue;
                        g.setColor(currColor);
                        break;
                    case ENDMARKER:
                        Object temp = attrStack.elementAt(attrStack.size() - 1);
                        if (temp instanceof Color) {
                            currColor = (Color) temp;
                            g.setColor(currColor);
                        } else {
                            currUnderline = ((Boolean) temp).booleanValue();
                        }
                        attrStack.removeElementAt(attrStack.size() - 1);
                        break;
                    default:
                        g.drawChars(tempArray, i, 1, advance, baseline);
                        if (currUnderline) {
                            g.setColor(Color.black);
                            g.fillRect(advance, baseline + 2, advances[c], +3);
                            g.setColor(currColor);
                        }
                        advance += advances[c];
                }
            }
        }
    }
}
