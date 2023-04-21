package mn.more.wits.client.gui;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import mn.more.wits.client.util.FormatUtil;
import net.mygwt.ui.client.Registry;

/**
 * @author <a href="mailto:mike.liu@aptechmongolia.edu.mn">Mike Liu</a>
 * @version $Id: StatusBar.java 5 2008-09-01 12:08:42Z mikeliucc $
 */
public class StatusBar extends HorizontalPanel {

    public static final String ID = "status-bar";

    private static final String STYLE_ERROR_TEXT = "lbl-status-error";

    private static final String STYLE_STATUS_TEXT = "lbl-status";

    private Image left = new Image("images/icons/window.png");

    private Label center = new Label("", false);

    private Label right = new Label("", false);

    private Timer clock = new Timer() {

        public void run() {
            right.setText(FormatUtil.formatLongDate(System.currentTimeMillis()));
        }
    };

    public StatusBar() {
        setStyleName(ID);
        setVerticalAlignment(ALIGN_MIDDLE);
        left.setStyleName("img-status");
        center.setStyleName(STYLE_STATUS_TEXT);
        right.setStyleName("lbl-status-date");
        add(left);
        add(center);
        add(right);
        setCellHorizontalAlignment(left, ALIGN_LEFT);
        setCellHorizontalAlignment(center, ALIGN_CENTER);
        setCellHorizontalAlignment(right, ALIGN_RIGHT);
        setCellVerticalAlignment(left, ALIGN_MIDDLE);
        setCellVerticalAlignment(center, ALIGN_MIDDLE);
        setCellVerticalAlignment(right, ALIGN_MIDDLE);
        setCellWidth(left, "35px");
        setCellWidth(right, "65px");
        setCellHeight(left, "35px");
        setCellHeight(right, "35px");
        setCellHeight(center, "35px");
        clock.scheduleRepeating(1000);
        Registry.register(ID, this);
    }

    public void setStatus(String message) {
        center.setStyleName(STYLE_STATUS_TEXT);
        center.setText(message);
    }

    public void setError(String message) {
        center.setStyleName(STYLE_ERROR_TEXT);
        center.setText(message);
    }

    public void setStatusImage(String imgUrl) {
        left.setUrl(imgUrl);
    }

    public void setStatusImage(Image img) {
        left.setUrl(img.getUrl());
    }
}
