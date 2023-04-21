package kvitter;

import java.util.Vector;
import java.util.ArrayList;
import java.util.*;
import com.trolltech.qt.gui.*;
import com.trolltech.qt.core.*;
import com.trolltech.qt.*;
import thinktank.twitter.Twitter;
import thinktank.twitter.Twitter.Message;
import thinktank.twitter.Twitter.Status;
import thinktank.twitter.Twitter.User;

public class TabContent {

    private String icon;

    private QWidget widget;

    public TabContent(String icon, QWidget widget) {
        this.icon = icon;
        this.widget = widget;
    }

    public String getIcon() {
        return icon;
    }

    public QWidget getWidget() {
        return widget;
    }
}
