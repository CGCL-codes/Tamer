package royere.cwi.layout;

import royere.cwi.framework.RoyereMessageHandler;

public interface LayoutMessageHandler extends RoyereMessageHandler {

    public void handleLayoutMessage(LayoutMessage m);
}
