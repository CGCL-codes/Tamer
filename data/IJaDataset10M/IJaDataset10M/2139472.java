package org.jcvi.vics.web.gwt.common.client.ui.renderers;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.jcvi.vics.model.tasks.Task;
import org.jcvi.vics.model.vo.ParameterVO;

/**
 * @author Michael Press
 */
public class UnknownParameterRenderer extends ParameterRenderer {

    public UnknownParameterRenderer(ParameterVO param, String key, Task task) {
        super(param, key, task);
    }

    protected Widget createPanel() {
        Label label = new Label();
        label.setText("Error: Unknown parameter type " + getValueObject().getType());
        label.setStyleName("error");
        return label;
    }
}
