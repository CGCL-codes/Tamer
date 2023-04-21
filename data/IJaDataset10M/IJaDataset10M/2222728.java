package ps.client.gui.util.textpanel;

import java.awt.Color;

public interface MessageLineFormater {

    public TextLine formatMessageLine(String msg, int size, Color color);

    public void setTimeStamp(boolean b);

    public boolean isTimeStamp();
}
