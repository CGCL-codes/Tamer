package net.sf.snver.gui.riena.ui;

import net.sf.snver.gui.riena.util.SnverUIConstant;
import org.eclipse.riena.ui.ridgets.uibinding.IMappingCondition;
import org.eclipse.swt.widgets.Text;

public class MyDecimalMappingCondition implements IMappingCondition {

    @Override
    public boolean isMatch(Object widget) {
        boolean result = false;
        if (widget instanceof Text) {
            final Text text = (Text) widget;
            result = SnverUIConstant.RIDGET_MY_DECIMAL_TEXT.equals(text.getData(SnverUIConstant.UI_TYPE));
        }
        return result;
    }
}
