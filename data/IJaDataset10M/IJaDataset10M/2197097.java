package wicket.in.action.chapter14.section_14_4;

import java.util.Arrays;
import java.util.Collections;
import org.apache.wicket.Application;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.protocol.http.IRequestLogger;
import org.apache.wicket.protocol.http.RequestLogger.RequestData;
import wicket.in.action.AbstractBasePage;

/**
 * @author dashorst
 */
public class Index extends AbstractBasePage {

    public Index() {
        CompoundPropertyModel model = new CompoundPropertyModel(this);
        setModel(model);
        add(new CheckBox("application.requestLoggerSettings.requestLoggerEnabled") {

            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }
        });
        add(new TextField("application.requestLoggerSettings.requestsWindowSize", Integer.class).add(new AjaxFormComponentUpdatingBehavior("onchange") {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        }));
        add(new Label("reqsWindow", model.bind("application.requestLoggerSettings.requestsWindowSize")));
        LoadableDetachableModel ldm = new LoadableDetachableModel() {

            @Override
            protected Object load() {
                IRequestLogger requestLogger = Application.get().getRequestLogger();
                return requestLogger == null ? Collections.EMPTY_LIST : Arrays.asList(requestLogger.getLiveSessions());
            }
        };
        add(new PropertyListView("sessions", ldm) {

            @Override
            protected void populateItem(ListItem item) {
                item.add(new DateLabel("startDate", new PatternDateConverter("yyyy-MM-dd HH:mm", false)));
                item.add(new Label("sessionId"));
                item.add(new Label("sessionInfo"));
                item.add(new Label("sessionSize"));
                item.add(new DateLabel("lastActive", new PatternDateConverter("yyyy-MM-dd HH:mm", false)));
                item.add(new Label("numberOfRequests"));
                item.add(new Label("totalTimeTaken"));
            }
        });
        ldm = new LoadableDetachableModel() {

            @Override
            protected Object load() {
                IRequestLogger requestLogger = Application.get().getRequestLogger();
                return requestLogger == null ? Collections.EMPTY_LIST : requestLogger.getRequests();
            }
        };
        add(new PropertyListView("requests", ldm) {

            @Override
            protected void populateItem(ListItem item) {
                RequestData data = (RequestData) item.getModelObject();
                item.add(new DateLabel("startDate", new PatternDateConverter("yyyy-MM-dd HH:mm:ss.SSS", false)));
                item.add(new Label("sessionId"));
                item.add(new Label("timeTaken"));
                item.add(new Label("sessionSize"));
                item.add(new Label("sessionInfo"));
                item.add(new Label("activeRequest"));
            }
        });
    }
}
