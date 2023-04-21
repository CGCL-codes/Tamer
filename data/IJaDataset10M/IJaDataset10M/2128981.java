package dk.hewison.client.mvc;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Hewison (creator)
 * @author $LastChangedBy: john.hewison $ $LastChangedDate: 2009-02-09 07:39:39 -0500 (Mon, 09 Feb 2009) $
 * @version $Revision: 363 $
 */
public abstract class AbstractAction implements Action {

    private int id;

    private Controller controller;

    private List<NetlWidget> netlWidgetList = new ArrayList<NetlWidget>();

    private boolean enabled;

    private boolean visible = true;

    private String text;

    private String disabledTooltip;

    private String activeTooltip;

    protected AbstractAction(Controller controller, int id, String text) {
        this.controller = controller;
        this.id = id;
        this.text = text;
    }

    protected AbstractAction(int id, Controller controller, String text, String activeTooltip, String disabledTooltip) {
        this.id = id;
        this.controller = controller;
        this.text = text;
        this.activeTooltip = activeTooltip;
        this.disabledTooltip = disabledTooltip;
    }

    protected AbstractAction(Controller controller) {
        this.controller = controller;
    }

    public int getId() {
        return id;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void execute() {
        doExecute();
        controller.updateActions();
    }

    public abstract void doExecute();

    public boolean isEnabled() {
        return true;
    }

    public boolean isVisible() {
        return true;
    }

    public void updateState() {
        setEnabled(isEnabled());
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (netlWidgetList != null) {
            for (NetlWidget netlWidget : netlWidgetList) {
                updateWidget(netlWidget);
            }
        }
    }

    private void updateWidget(NetlWidget netlWidget) {
        netlWidget.setEnabled(enabled);
        if (disabledTooltip != null && !enabled) {
            netlWidget.setTitle(disabledTooltip);
        } else if (activeTooltip != null) {
            netlWidget.setTitle(activeTooltip);
        }
    }

    public final void setVisible(boolean visible) {
        this.visible = visible;
        if (netlWidgetList != null) {
            for (NetlWidget netlWidget : netlWidgetList) {
                if (netlWidget instanceof Widget) {
                    netlWidget.setVisible(visible);
                }
            }
        }
    }

    public void setText(String text) {
        this.text = text;
        if (netlWidgetList != null) {
            for (NetlWidget netlWidget : netlWidgetList) {
                if (netlWidget instanceof HasText) {
                    netlWidget.setText(text);
                }
            }
        }
    }

    public String getText() {
        return text;
    }

    public String getDisabledTooltip() {
        return disabledTooltip;
    }

    public String getActiveTooltip() {
        return activeTooltip;
    }

    public void addNetlWidget(NetlWidget netlWidget) {
        netlWidgetList.add(netlWidget);
        netlWidget.setText(text);
        netlWidget.setEnabled(enabled);
        netlWidget.setVisible(visible);
        updateWidget(netlWidget);
    }

    public void removeCommandListener(NetlWidget netlWidget) {
        netlWidgetList.remove(netlWidget);
    }
}
