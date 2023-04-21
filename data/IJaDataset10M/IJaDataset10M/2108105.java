package org.gwm.splice.client.toolbar;

import org.gwm.splice.client.desktop.DesktopManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class ToolBarButton extends FocusPanel implements IActionWidget {

    private String name;

    private int actionID;

    private Image image;

    private Label label;

    private String inactiveStyle;

    private boolean disabled = false;

    public ToolBarButton(String imageName, String name, int actionID) {
        this(imageName, name, actionID, null, null, false);
    }

    public ToolBarButton(String imageName, String name, int actionID, String labelText, String tooltip, boolean showBorders) {
        super();
        if (showBorders) {
            inactiveStyle = "toolbarButton-over";
        } else {
            inactiveStyle = "toolbarButton";
        }
        if (imageName != null) {
            image = new Image(DesktopManager.getInstance().getSmallIconUrl(imageName));
            image.setStyleName("toolbarButtonImage");
        }
        if (labelText != null) {
            label = new Label(labelText);
            label.setStyleName("toolbarButtonLabel");
        }
        if (label != null) {
            HorizontalPanel hp = new HorizontalPanel();
            if (image != null) {
                hp.add(image);
            }
            hp.add(label);
            hp.setHorizontalAlignment(hp.ALIGN_CENTER);
            hp.setVerticalAlignment(hp.ALIGN_MIDDLE);
            setWidget(hp);
        } else if (image != null) {
            setWidget(image);
        }
        this.name = name;
        this.actionID = actionID;
        if (tooltip != null) {
            setTitle(tooltip);
        }
        setStyleName(inactiveStyle);
        addMouseListener(new MouseListener() {

            public void onMouseEnter(Widget sender) {
                if (!disabled) setStyleName("toolbarButton-over");
            }

            public void onMouseMove(Widget sender, int x, int y) {
                if (!disabled) setStyleName("toolbarButton-over");
            }

            public void onMouseLeave(Widget sender) {
                if (!disabled) setStyleName(inactiveStyle);
            }

            public void onMouseDown(Widget sender, int x, int y) {
                if (!disabled) setStyleName("toolbarButton-pressed");
            }

            public void onMouseUp(Widget sender, int x, int y) {
                if (!disabled) setStyleName("toolbarButton-over");
            }
        });
    }

    /**
	 * Stop firefox from doing default image stuff
	 */
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        DOM.eventPreventDefault(event);
    }

    public int getActionID() {
        return actionID;
    }

    public void setActionID(int actionID) {
        this.actionID = actionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        if (disabled) {
            setStyleName("toolbarButton-disabled");
        } else {
            setStyleName(inactiveStyle);
        }
    }
}
