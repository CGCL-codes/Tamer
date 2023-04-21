package orcajo.azada.table.actions;

import orcajo.azada.table.ktable.TableSetting;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import com.tonbeller.jpivot.table.AxisConfig;

public class ShowSpanAction extends Action {

    private TableSetting setting;

    public ShowSpanAction(String text, ImageDescriptor image, TableSetting setting) {
        super(text, IAction.AS_CHECK_BOX);
        setImageDescriptor(image);
        this.setting = setting;
        updateChecked();
    }

    public void updateChecked() {
        if (isChecked() != getSettingChecked()) {
            setChecked(getSettingChecked());
        }
    }

    private boolean getSettingChecked() {
        return setting.getMemberSpanRow() == AxisConfig.HIERARCHY_SPAN && setting.getMemberSpanCol() == AxisConfig.HIERARCHY_SPAN;
    }

    public void run() {
        boolean settingChecked = getSettingChecked();
        if (super.isChecked() != settingChecked) {
            if (super.isChecked()) {
                setting.setMemberSpanRow(AxisConfig.HIERARCHY_SPAN);
                setting.setMemberSpanCol(AxisConfig.HIERARCHY_SPAN);
            } else {
                setting.setMemberSpanRow(AxisConfig.HIERARCHY_THEN_POSITION_SPAN);
                setting.setMemberSpanCol(AxisConfig.HIERARCHY_THEN_POSITION_SPAN);
            }
            setting.fireViewSettingChanged();
        }
    }
}
