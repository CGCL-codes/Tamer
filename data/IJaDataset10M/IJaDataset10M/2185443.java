package net.sf.rcpforms.widgets2;

import java.util.Set;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.event.IEventKey;
import org.eclipse.swt.widgets.Widget;

public interface IRCPWidget2 extends IRCPBeanControl2 {

    /** @see boolean */
    public static final String PROP_IS_ENABLED = "isEnabled";

    /** @see boolean */
    public static final String PROP_IS_VISIBLE = "isVisible";

    /** @see type */
    public static final String PROP_IS_EDITABLE = "isEditable";

    /** @see boolean */
    public static final String PROP_IS_MANDATORY = "isMandatory";

    /** @see boolean */
    public static final String PROP_IS_RECOMMENDED = "isRecommended";

    /** @see boolean */
    public static final String PROP_IS_INFO_STATE = "isInfoState";

    /** @see Boolean */
    public static final String PROP_CUSTOM_STATE = "customState";

    /** @see boolean */
    public static final String PROP_STATE = "state";

    /** XXX */
    public void setIsVisible(boolean visible);

    public boolean getIsVisible();

    /** XXX */
    public boolean getIsEnabled();

    public void setIsEnabled(boolean enabled);

    /** XXX */
    public void setIsEditable(boolean editable);

    public boolean getIsEditable();

    /** XXX */
    public void setIsMandatory(boolean mandatory);

    public boolean getIsMandatory();

    /** XXX */
    public void setIsRecommended(boolean recommended);

    public boolean getIsRecommended();

    /** XXX */
    public boolean getIsInfoState();

    public void setIsInfoState(boolean newIsInfoState);

    /** XXX */
    public void setState(EControlState stateKey, boolean customState);

    public boolean getState(EControlState stateKey);

    /** XXX */
    public void setCustomState(Object stateKey, Boolean customState);

    public Boolean getCustomState(Object stateKey);

    public Widget getSWTWidget();

    public boolean isContainer();

    public Set<IEventKey> getActionSupportedKeys();
}
