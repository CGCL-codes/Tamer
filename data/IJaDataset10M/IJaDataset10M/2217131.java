package freemind.main;

import java.awt.Image;
import javax.swing.ImageIcon;

public interface FeedBack {

    void progress(int act, String messageId);

    int getActualValue();

    void setMaximumValue(int max);

    void increase(String messageId);
}
