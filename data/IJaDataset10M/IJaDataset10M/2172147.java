package net.sourceforge.easywicket.wrapper;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import net.sourceforge.easywicket.BaseWrapper;
import net.sourceforge.easywicket.EasyWicket;

public class TextAreaWrapper extends BaseWrapper {

    @Override
    protected Component createInstance(String widgetId, Class<? extends Component> widgetClass, EasyWicket annot, MarkupContainer parentWidget) {
        return util.createInstance(widgetClass, widgetId);
    }
}
