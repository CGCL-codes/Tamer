package org.dcm4chee.web.common.behaviours;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;

/**
 * @author Franz Willer <franz.willer@gmail.com>
 * @version $Revision$ $Date$
 * @since Jun 14, 2011
 */
public class SelectableTableRowBehaviour extends AjaxEventBehavior {

    private static final long serialVersionUID = 1L;

    private IModel<Boolean> model;

    private CheckBox chkBox;

    private String unselectedClass, selectedClass;

    private SelectableTableRowBehaviour(String unselectedClass, String selectedClass) {
        super("onclick");
        this.unselectedClass = unselectedClass;
        this.selectedClass = selectedClass;
    }

    public SelectableTableRowBehaviour(IModel<Boolean> model, String unselectedClass, String selectedClass) {
        this(unselectedClass, selectedClass);
        this.model = model;
    }

    public SelectableTableRowBehaviour(CheckBox chkBox, String unselectedClass, String selectedClass) {
        this(unselectedClass, selectedClass);
        this.chkBox = chkBox;
        chkBox.setOutputMarkupId(true);
    }

    @Override
    protected void onEvent(AjaxRequestTarget target) {
        model.setObject(!model.getObject());
        target.addComponent(getComponent());
    }

    @Override
    protected void onComponentTag(final ComponentTag tag) {
        super.onComponentTag(tag);
        if (model == null) model = chkBox.getModel();
        if (model.getObject()) {
            if (selectedClass != null) tag.put("class", selectedClass);
        } else if (unselectedClass != null) {
            tag.put("class", unselectedClass);
        }
    }

    @Override
    protected CharSequence getPreconditionScript() {
        return "if (event == null || !event.ctrlKey) return false;var t = (event.target) ? event.target : (event.srcElement) ? event.srcElement : null;" + "if (t.nodeType == 3) t = t.parentNode;" + " return t.nodeName == 'TD' || t.nodeName == 'SPAN'";
    }
}
