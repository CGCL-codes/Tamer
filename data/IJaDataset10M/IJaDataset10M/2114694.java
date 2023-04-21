package ar.edu.fesf.view.pages.generic;

import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import ar.edu.fesf.controllers.AjaxNamedAction;
import ar.edu.fesf.controllers.GenericDataProvider;

public class ActionsPanel<T> extends Panel {

    private static final long serialVersionUID = 1L;

    public ActionsPanel(final String id, final T object, final List<AjaxNamedAction<T>> ajaxCallbacks) {
        super(id);
        this.initialize(object, ajaxCallbacks);
    }

    private void initialize(final T object, final List<AjaxNamedAction<T>> ajaxCallbacks) {
        DataView<AjaxNamedAction<T>> dataView = new DataView<AjaxNamedAction<T>>("actions", new GenericDataProvider<AjaxNamedAction<T>>(ajaxCallbacks)) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final Item<AjaxNamedAction<T>> item) {
                final AjaxNamedAction<T> action = item.getModelObject();
                AjaxFallbackLink<T> ajaxFallbackLink = new AjaxFallbackLink<T>("action") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        action.apply(target, object);
                    }
                };
                ajaxFallbackLink.add(new Label("linkText", new Model<String>(action.getName())));
                item.add(ajaxFallbackLink);
            }
        };
        this.add(dataView);
    }
}
