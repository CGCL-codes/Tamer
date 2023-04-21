package spacefaring.ui;

import spacefaring.ui.controls.ButtonView;
import spacefaring.ui.controls.TextView;
import spacefaring.ui.controls.ScrollView;
import spacefaring.events.*;
import spacefaring.ui.layout.XYTechnique;
import spacefaring.util.IntPoint2D;

/**
 *
 * @author Administrator
 */
public class MessageView extends WindowView implements Listener {

    public ButtonView buttonok;

    public TextView textview;

    public ScrollView textscroller;

    /**
     * Standard width and height for the message view.
     */
    public static final int w = 390, h = 300;

    /**
     * The string that is to be shown on screen
     */
    protected String messagetext;

    /**
     * Creates a new instance of MessageView
     */
    public MessageView(ViewController vcontroller) {
        super("MessageView", vcontroller);
        setLayoutTechnique(new XYTechnique());
        IntPoint2D size = vc.getDisplaySize();
        size = size.divide(2f);
        setPos((int) (size.x - w / 2f), (int) (size.y - h / 2f));
        setSize(w, h);
        buttonok = new ButtonView("OK", "buttonok", w - getPaddingLeft() - getPaddingRight() - 50, h - getPaddingTop() - getPaddingBottom() - 20, 50, 20);
        Events.registerListener(this, buttonok);
        textview = new TextView("", "textview", DisplayController.NormalTextStyle);
        textview.setName("textview");
        textscroller = new ScrollView("textscroller");
        textscroller.setMinMax(0, 100);
        textscroller.setBarLength(69);
        textscroller.setBarPos(0);
        textscroller.setHorizontal(false);
        Events.registerListener(this, textscroller);
        textscroller.setContainerPos(360, 0);
        textscroller.setSize(10, 230);
        textview.setContainerPos(0, 0);
        textview.setSize(360, 230);
        addChild(textscroller);
        addChild(textview);
        addChild(buttonok);
        setTitle("Message");
        positionAndStrechThisAndChildren(getPosition(), getSize());
    }

    public void adaptToConfig(ViewController.GUIConfiguration guicon) {
        IntPoint2D size = vc.getDisplaySize();
        size = size.divide(2f);
        if (vc.getMessageMode()) {
            setPos((int) (size.x - w / 2f), (int) (size.y - h / 2f));
            setDesiredSize(w, h);
            setVisible(true);
            setNeedsRepaint();
        } else {
            setVisible(false);
        }
    }

    public String getMessageText() {
        return messagetext;
    }

    public void setMessageText(String newtext) {
        messagetext = newtext;
        textview.setText(messagetext);
        textview.setLineOffset(0);
        textscroller.setMaxVal(textview.getInternalHeight());
        textscroller.setBarPos(0);
        textscroller.setBarLength(textview.getHeight() - 2);
        if (textview.getLinesCount() <= textview.getFullyVisibleLineCount()) {
            textscroller.setVisible(false);
        } else {
            textscroller.setVisible(true);
        }
        setNeedsRepaint();
    }

    @Override
    public void processEvent(Event e, Object eventsource) {
        if (e instanceof ScrollEvent && eventsource == textscroller) {
            textview.setLineOffset(textscroller.getBarPos());
        }
        if (e instanceof ClickEvent && eventsource == buttonok) {
            vc.setMessageMode(false);
            this.close();
        }
    }
}
