package cn.myapps.mobile.element;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import cn.myapps.mobile.util.ActivityType;
import cn.myapps.ui.AbstractRow;
import cn.myapps.ui.Component;
import cn.myapps.util.StringUtil;

public class MbDialogView extends MbView {

    public MbDialogView() {
        super();
    }

    public void commandAction(Command c, Displayable d) {
        if (c instanceof MbMenuItem) {
            MbMenuItem mbc = (MbMenuItem) c;
            MbAction action = mbc.getAction();
            if (action != null) {
                if (action.getType().equals(ActivityType.CMD_DIALOGVIEW) || action.getType().equals(ActivityType.CMD_DISPLAYVIEW)) {
                    action.addParameter(getParameters());
                    getService().doDialogView(action.getParameterArray());
                } else if (action.getType().equals(ActivityType.CMD_DISPLAY_SEARCHFORM)) {
                    getService().doDisplaySearchForm(getParameters());
                } else if (action.getType().equals(ActivityType.CMD_LINKVALUES)) {
                    fire();
                }
            }
        } else if (c == cmdBack) {
            getService().back();
        }
    }

    protected void fire() {
        String fieldName = mainForm.getCurrentField();
        Component item = (Component) mainForm.getFormField(fieldName);
        if (selectRowIndex > 0) {
            AbstractRow tr = (AbstractRow) tRs.elementAt(selectRowIndex);
            if (tr != null) {
                IMbField field = getField("_mapStr");
                if (field != null) {
                    String mapStr = field.getString();
                    String[] map;
                    try {
                        map = StringUtil.split(mapStr, ";");
                        for (int i = 0; map != null && i < map.length; i++) {
                            String[] fields = StringUtil.split(map[i], ":");
                            field = mainForm.getFormField(fields[1]);
                            field.setValue(tr.getTextByName(fields[0]));
                        }
                        ((MbButton) item).setItemState(MbButton.ITEM_CHANGED);
                    } catch (Exception e) {
                        ((MbButton) item).setItemState(MbButton.ITEM_NORMAL);
                    }
                }
            }
        }
        getService().back();
        mainForm.itemStateChanged(item);
    }
}
