package net.sourceforge.jruntimedesigner.actions.templates;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.JRuntimeDesignerJavaBeansModel;
import net.sourceforge.jruntimedesigner.JRuntimeDesignerModel;
import net.sourceforge.jruntimedesigner.actions.AbstractWidgetAction;
import net.sourceforge.jruntimedesigner.widgets.IWidget;
import net.sourceforge.jruntimedesigner.widgets.JavaBeansWidgetAdapter;
import net.sourceforge.jruntimedesigner.widgets.template.TemplatesProvider;

/**
 * Materializes template.
 * 
 * @author DLeszyk
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 182 $ $Date: 2009-08-21 11:14:20 -0400 (Fri, 21 Aug 2009) $
 * @since 1.0
 */
public class TemplateAction extends AbstractWidgetAction {

    public static final String NAME = "Template";

    private final TemplatesProvider tempFromFileProvider;

    public TemplateAction(JRuntimeDesignerController controller) {
        super(NAME, controller);
        updateActionState();
        this.tempFromFileProvider = controller.getTemplateProvider();
    }

    protected void updateActionState() {
        setEnabled(controller.isDesignMode());
    }

    public void doAction(ActionEvent e) throws Exception {
        JRuntimeDesignerModel model = controller.getModel();
        JRuntimeDesignerJavaBeansModel jbModel = (JRuntimeDesignerJavaBeansModel) model;
        List<?> list = tempFromFileProvider.materialize();
        if (!list.isEmpty()) {
            JavaBeansWidgetAdapter widget = (JavaBeansWidgetAdapter) list.get(0);
            Point currentLocation = controller.getPanel().getCurrentLocation();
            widget.setLocation(currentLocation);
            jbModel.materializeWidgets(list);
            controller.renderNewWidgets(Arrays.<IWidget>asList(widget));
        }
    }
}
