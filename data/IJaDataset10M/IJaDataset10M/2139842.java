package es.aeat.eett.rubik.toolBarNavi;

import javax.swing.Action;
import es.aeat.eett.rubik.core.tableSetting.TableSettingEvent;

/**
 * @author f00992
 *
 */
class ButtonAncestorSpan extends ButtonAncestorTableSetting {

    private static final long serialVersionUID = 7607851337080150884L;

    /**
	 * @param acion
	 * @throws Exception
	 */
    public ButtonAncestorSpan(Action acion) throws Exception {
        super(acion);
    }

    /**
	 * @see es.aeat.eett.rubik.core.tableSetting.TableSettingListener#tableSettingChanged(es.aeat.eett.rubik.core.tableSetting.TableSettingEvent)
	 */
    public void tableSettingChanged(TableSettingEvent event) {
        if (isSelected()) {
            if (tableSetting.getModeMemberSpanRow() != 1 || tableSetting.getModeMemberSpanCol() != 1) setSelected(false);
        } else {
            if (tableSetting.getModeMemberSpanRow() == 1 && tableSetting.getModeMemberSpanCol() == 1) setSelected(true);
        }
    }
}
