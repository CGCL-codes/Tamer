package net.sf.edevtools.lib.baselib.ui.actions.commonactions;

import java.util.List;
import javax.swing.Action;
import net.sf.edevtools.lib.baselib.nonui.listener.IBeanPropertyChangeProvider;
import org.eclipse.jface.action.IAction;

/**
 * @author Christoph Graupner
 * @param <UICompType>
 * 
 */
public interface ICommonAction<UICompType, UIActionType> extends Runnable, IBeanPropertyChangeProvider {

    public static final String P_NAME = Action.NAME;

    public static final String P_TYPE = "type";

    public static final String P_ACCELERATOR_KEY = Action.ACCELERATOR_KEY;

    public static final String P_COMMAND_KEY = Action.ACTION_COMMAND_KEY;

    public static final String P_DEFAULT = Action.DEFAULT;

    public static final String P_DESCRIPTION = Action.LONG_DESCRIPTION;

    public static final String P_MNEMONIC_KEY = Action.MNEMONIC_KEY;

    public static final String P_TOOLTIP = Action.SHORT_DESCRIPTION;

    public static final String P_ICON = Action.SMALL_ICON;

    public static final String PROP_CORE = "core";

    public static final String P_ENABLED = IAction.ENABLED;

    public static final String P_SUCCESS = IAction.RESULT;

    public static final int AS_PUSH_BUTTON = IAction.AS_PUSH_BUTTON;

    public static final int AS_CHECK_BOX = IAction.AS_CHECK_BOX;

    public static final int AS_RADIO_BUTTON = IAction.AS_RADIO_BUTTON;

    public <T> T attach(T aComponent);

    public <T> T detach(T aComponent);

    public ActionCore getActionCore();

    public List<UICompType> getComponents();

    public Object getIconImage();

    public UIActionType getImpl();

    public String getText();

    public String getToolTipText();

    public int getType();

    public boolean isEnabled();

    public void setActionCore(ActionCore aRunnableCore);

    public void setDescription(String aText);

    public void setEnabled(boolean aValue);

    public void setIconImage(Object aImage);

    public void setText(String aText);

    public void setToolTipText(String aText);
}
