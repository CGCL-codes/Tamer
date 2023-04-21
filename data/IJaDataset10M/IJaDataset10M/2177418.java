package net.cepra.timecard.client.activity.editor;

import java.util.Date;
import java.util.List;
import net.cepra.core.client.logon.ClientContext;
import net.cepra.timecard.client.activity.search.Statistics;
import net.cepra.timecard.common.activity.ActivityEditList;
import net.cepra.timecard.common.activity.ActivityStatistic;
import net.cepra.timecard.domain.Activity;
import com.gwtaf.client.action.Action;
import com.gwtaf.client.action.IAction;
import com.gwtaf.client.actionadapter.ContextActionAdapter;
import com.gwtaf.client.appstate.IAppState;
import com.gwtaf.client.appstate.IAppStateFactory;
import com.gwtaf.client.controller.common.IActionList;
import com.gwtaf.client.controller.editor.Editor;
import com.gwtaf.client.controller.editor.IEditContext;
import com.gwtaf.client.datacontext.IDataChangeListener;
import com.gwtaf.client.permission.Permission;
import com.gwtaf.client.selectioncontext.EditorGridSelectionContext;
import com.gwtaf.client.selectioncontext.ISelectionContext;
import com.gwtaf.client.util.IParameterMap;
import com.gwtaf.common.message.IMessage;
import com.gwtaf.common.message.MessageLevel;
import com.gwtaf.common.model.EditList;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Record.Operation;
import com.gwtext.client.widgets.Panel;

public class ActivityEditor extends Editor<EditList<Activity>> {

    public static final String ID = "ActivityEditor";

    public static IAppStateFactory FACTORY = new IAppStateFactory() {

        public IAppState create() {
            return new ActivityEditor();
        }

        public String getID() {
            return ID;
        }

        public Object decodeParams(IParameterMap params) {
            Date date = params.getDate("day");
            if (date == null) return null;
            return new StartParam(params.getInt("res"), date);
        }

        public void encodeParams(IParameterMap params, Object obj) {
            if (obj instanceof StartParam) {
                StartParam sm = (StartParam) obj;
                params.setDate("day", sm.day.getTime());
                params.setInt("resid", sm.resourceId);
            }
        }
    };

    private Statistics statistic;

    private ActivityGrid activityGrid;

    private ISelectionContext selectionContext;

    private final IAction actionAdd = new Action("Neuer Eintrag", "table_add-icon") {

        public void execute(Object... data) {
            createActivity(data.length == 0 ? null : (Activity) data[0]);
        }
    };

    private final IAction actionRemove = new Action("Eintrag l&ouml;schen", "table_delete-icon") {

        public void execute(Object... data) {
            deleteActivity((Activity) data[0]);
        }

        public Permission getPermission(Object... data) {
            return data != null && data.length == 1 && data[0] != null ? Permission.ALLOWED : Permission.UNALLOWED;
        }
    };

    private final IAction actionDuplicate = new Action("Eintrag dublizieren", "table_multiple-icon") {

        public void execute(Object... data) {
            duplicateActivity((Activity) data[0]);
        }

        public Permission getPermission(Object... data) {
            return data != null && data.length == 1 && data[0] != null ? Permission.ALLOWED : Permission.UNALLOWED;
        }
    };

    private ActivityEditContext editContext = new ActivityEditContext(this);

    private ActivityStore activityStore = new ActivityStore();

    private ActivityStatistic as;

    public ActivityEditor() {
        activityStore.setList(editContext);
        editContext.addChangeListener(new IDataChangeListener() {

            public void onDataChange() {
                updateStatistic();
            }
        });
        as = new ActivityStatistic();
    }

    protected void createActivity(Activity activity) {
        Activity newAct;
        if (activity != null) newAct = editContext.getData().create(activity); else newAct = editContext.getData().create(ClientContext.get().getLogonUser());
        getActivityStore().add(newAct);
        activityGrid.select(newAct);
        editContext.notifyChange();
    }

    protected void duplicateActivity(Activity activity) {
        Activity copyAct = editContext.getData().copy(activity);
        getActivityStore().add(copyAct);
        activityGrid.select(copyAct);
        editContext.notifyChange();
    }

    protected void deleteActivity(Activity data) {
        editContext.getData().setToDelete(data);
        getActivityStore().removeByField(ActivityStore.activity, data);
        editContext.notifyChange();
    }

    public String getTitle() {
        return "Zeiterfassung";
    }

    @Override
    protected void addNavigatorPanels(List<Panel> panels) {
        super.addNavigatorPanels(panels);
        panels.add(statistic = new Statistics());
    }

    @Override
    protected Panel createContentPanel() {
        return activityGrid = new ActivityGrid(this);
    }

    @Override
    protected void postCreate() {
        super.postCreate();
        getMessagePanel().addDoubleClickAction(new Action() {

            public void execute(Object... data) {
                IMessage msg = (IMessage) data[0];
                activityGrid.selectActivity((Activity) msg.getData());
            }
        });
    }

    public void onUpdate(Record record, Operation operation, String field) {
        getActivityStore().onUpdate(record, operation, field);
        getEditContext().notify(getActivityStore().isDirty(), !MessageLevel.isError(getEditContext().validate().getWorstLevel()));
        updateStatistic();
        triggerValidate();
    }

    public void updateStatistic() {
        List<Activity> elements = editContext.getData().getElements();
        as.setRequiredHoursPerDay(8 * 3600);
        as.update(elements.toArray(new Activity[elements.size()]));
        getStatistic().update(as);
    }

    protected Statistics getStatistic() {
        return statistic;
    }

    public void addActions(IActionList actionList) {
        super.addActions(actionList);
        ISelectionContext ctx = getSelectionContext();
        actionList.addAction(actionAdd);
        actionList.addAction(new ContextActionAdapter(actionDuplicate, ctx));
        actionList.addAction(new ContextActionAdapter(actionRemove, ctx));
    }

    private ISelectionContext getSelectionContext() {
        if (selectionContext == null) selectionContext = new EditorGridSelectionContext(activityGrid.getGrid(), ActivityStore.activity.getName());
        return selectionContext;
    }

    public IEditContext<ActivityEditList> getEditContext() {
        return editContext;
    }

    public ActivityStore getActivityStore() {
        return activityStore;
    }
}
