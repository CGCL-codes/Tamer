package org.eclipse.jface.action;

import nill.NullInterface;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;

public interface IAction extends NullInterface {

    String getId();

    ImageDescriptor getImageDescriptor();

    ImageDescriptor getHoverImageDescriptor();

    ImageDescriptor getDisabledImageDescriptor();

    String getText();

    String getToolTipText();

    boolean isEnabled();

    void setEnabled(boolean calculateEnabled);

    boolean isChecked();

    HelpListener getHelpListener();

    void addPropertyChangeListener(IPropertyChangeListener listener);

    String getAccelerator();

    IMenuCreator getMenuCreator();

    int getStyle();

    void removePropertyChangeListener(IPropertyChangeListener listener);

    void runWithEvent(Event e);

    void setChecked(boolean b);

    void run();

    /**
     * Action style constant (value <code>0</code>) indicating action style 
     * is not specified yet. By default, the action will assume a push button
     * style. If <code>setChecked</code> is called, then the style will change
     * to a check box, or if <code>setMenuCreator</code> is called, then the
     * style will change to a drop down menu.
     * 
     * @since 2.1
     */
    public static int AS_UNSPECIFIED = 0x00;

    /**
     * Action style constant (value <code>1</code>) indicating action is 
     * a simple push button.
     */
    public static int AS_PUSH_BUTTON = 0x01;

    /**
     * Action style constant (value <code>2</code>) indicating action is 
     * a check box (or a toggle button).
     */
    public static int AS_CHECK_BOX = 0x02;

    /**
     * Action style constant (value <code>4</code>) indicating action is 
     * a drop down menu.
     */
    public static int AS_DROP_DOWN_MENU = 0x04;

    /**
     * Action style constant (value <code>8</code>) indicating action is 
     * a radio button.
     * 
     * @since 2.1
     */
    public static int AS_RADIO_BUTTON = 0x08;

    /**
     * Property name of an action's text (value <code>"text"</code>).
     */
    public static final String TEXT = "text";

    /**
     * Property name of an action's enabled state
     * (value <code>"enabled"</code>).
     */
    public static final String ENABLED = "enabled";

    /**
     * Property name of an action's image (value <code>"image"</code>).
     */
    public static final String IMAGE = "image";

    /**
     * Property name of an action's tooltip text (value <code>"toolTipText"</code>).
     */
    public static final String TOOL_TIP_TEXT = "toolTipText";

    /**
     * Property name of an action's description (value <code>"description"</code>).
     * Typically the description is shown as a (longer) help text in the status line.
     */
    public static final String DESCRIPTION = "description";

    /**
     * Property name of an action's checked status (value
     * <code>"checked"</code>). Applicable when the style is
     * <code>AS_CHECK_BOX</code> or <code>AS_RADIO_BUTTON</code>.
     */
    public static final String CHECKED = "checked";

    /**
     * Property name of an action's success/fail result
     * (value <code>"result"</code>). The values are
     * <code>Boolean.TRUE</code> if running the action succeeded and 
     * <code>Boolean.FALSE</code> if running the action failed or did not
     * complete.
     * <p>
     * Not all actions report whether they succeed or fail. This property
     * is provided for use by actions that may be invoked by clients that can
     * take advantage of this information when present (for example, actions
     * used in cheat sheets). Clients should always assume that running the
     * action succeeded in the absence of notification to the contrary. 
     * </p>
     * 
     * @since 3.0
     */
    public static final String RESULT = "result";
}
