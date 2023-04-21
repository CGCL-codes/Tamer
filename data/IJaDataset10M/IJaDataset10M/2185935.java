package casa.component.basic;

import java.net.URL;
import anima.message.Message;
import anima.message.MessageSingle;
import anima.message.MessageWrite;
import anima.util.IOUtil;
import anima.visual.VisualComponent;
import casa.component.image.ImageCollectionComponent;
import casa.component.image.ImageCollectionOperation;

public class StateOperation extends ImageCollectionOperation {

    public static final String STATE_IMAGE = StateOperation.class.getResource("state-strip.gif").toString();

    public static final int STATE_FRAME_WIDTH = 128, STATE_FRAME_HEIGHT = 128;

    public static final boolean HORIZONTAL_STRIP = true;

    public static final int INITIAL_STATE = 1;

    protected int state = INITIAL_STATE;

    public StateOperation() {
        this(true);
    }

    public StateOperation(boolean isVisual) {
        super(false);
        if (isVisual) setVisual(new ImageCollectionComponent(IOUtil.findURLWithoutException(STATE_IMAGE), STATE_FRAME_WIDTH, STATE_FRAME_HEIGHT, HORIZONTAL_STRIP));
    }

    public StateOperation(URL source, int frameWidth, int frameHeight, boolean horizontalStrip, int state) {
        super(source, frameWidth, frameHeight, horizontalStrip);
        setState(state);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        if (visual != null) ((ImageCollectionComponent) visual).setCurrent(state - 1);
    }

    public void action(Message theMessage) {
        fireAnimaEvent(VisualComponent.MESSAGE_ACTION, new MessageSingle(VisualComponent.MESSAGE_ACTION, String.valueOf(state), this));
    }

    public boolean localMessage(Message theMessage) {
        boolean found = super.localMessage(theMessage);
        String mess = theMessage.getLabel();
        if (theMessage instanceof MessageWrite && mess.equalsIgnoreCase("state")) {
            setState(((MessageWrite) theMessage).getValueInt());
            found = true;
        }
        return found;
    }
}
