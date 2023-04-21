package org.dyno.visual.swing.widgets;

import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;

@SuppressWarnings("unchecked")
public class JButtonAdapter extends TextWidgetAdapter {

    @Override
    public Class getWidgetClass() {
        return JButton.class;
    }

    @Override
    public IAdapter getParent() {
        JButton jb = (JButton) getWidget();
        DefaultButtonModel dbm = (DefaultButtonModel) jb.getModel();
        ButtonGroup bg = dbm.getGroup();
        if (bg != null) {
            for (InvisibleAdapter invisible : getRootAdapter().getInvisibles()) {
                if (invisible instanceof ButtonGroupAdapter) {
                    if (bg == ((ButtonGroupAdapter) invisible).getButtonGroup()) return invisible;
                }
            }
        }
        return super.getParent();
    }

    @Override
    public void deleteNotify() {
        JButton jb = (JButton) getWidget();
        DefaultButtonModel dbm = (DefaultButtonModel) jb.getModel();
        ButtonGroup bg = dbm.getGroup();
        if (bg != null) {
            bg.remove(jb);
        }
    }
}
