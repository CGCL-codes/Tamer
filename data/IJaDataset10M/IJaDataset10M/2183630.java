package onepoint.project.modules.work.forms;

import onepoint.express.XComponent;
import onepoint.express.XValidator;
import onepoint.express.server.XFormProvider;
import onepoint.persistence.OpBroker;
import onepoint.persistence.OpQuery;
import onepoint.project.OpProjectSession;
import onepoint.project.modules.project.OpActivity;
import onepoint.project.modules.resource.OpResource;
import onepoint.project.modules.user.OpUser;
import onepoint.project.modules.work.OpWorkSlip;
import onepoint.project.modules.work.OpWorkSlipDataSetFactory;
import onepoint.service.server.XSession;
import onepoint.util.XCalendar;
import java.sql.Date;
import java.util.*;

/**
 * Form provider for Work Slips.
 */
public class OpWorkSlipsFormProvider implements XFormProvider {

    public static final String WORK_SLIP_SET = "WorkSlipSet";

    public static final int NUMBER_COLUMN_INDEX = 0;

    public static final int RESOURCE_COLUMN_INDEX = 1;

    public static final int DATE_COLUMN_INDEX = 2;

    public static final String WORK_SLIPS_QUERY = "select workSlip from OpWorkSlip as workSlip where " + "workSlip.Creator.ID = ? " + "and workSlip.Date >= ? " + "order by workSlip.Date desc";

    private static final String NEW_WORK_SLIP_BUTTON = "NewWorkSlip";

    private static final String EDIT_WORK_SLIP_BUTTON = "EditWorkSlip";

    private static final String INFO_WORK_SLIP_BUTTON = "InfoWorkSlip";

    private static final String DELETE_WORK_SLIP_BUTTON = "DeleteWorkSlip";

    private static final String PERIOD_CHOICE_ID = "period_choice_id";

    private static final String PERIOD_CHOICE_FIELD = "PeriodChooser";

    private static final String PERIOD_STARTING_WITH_CURRENT_MONTH = "current";

    private static final String PERIOD_STARTING_WITH_PREVIOUS_MONTH = "previous";

    private static final String PERIOD_STARTING_WITH_CURRENT_YEAR = "year";

    public void prepareForm(XSession s, XComponent form, HashMap parameters) {
        XComponent editButton = form.findComponent(EDIT_WORK_SLIP_BUTTON);
        editButton.setEnabled(false);
        XComponent infoButton = form.findComponent(INFO_WORK_SLIP_BUTTON);
        infoButton.setEnabled(false);
        XComponent deleteButton = form.findComponent(DELETE_WORK_SLIP_BUTTON);
        deleteButton.setEnabled(false);
        OpProjectSession session = (OpProjectSession) s;
        OpBroker broker = session.newBroker();
        XComponent data_set = form.findComponent(WORK_SLIP_SET);
        XComponent data_row;
        XComponent data_cell;
        String period = getPeriodID(parameters, form, session);
        if (period == null) {
            period = PERIOD_STARTING_WITH_CURRENT_MONTH;
        }
        OpQuery query = broker.newQuery(WORK_SLIPS_QUERY);
        query.setLong(0, session.getUserID());
        Calendar calendar = XCalendar.getDefaultCalendar().getCalendar();
        calendar.setTime(XCalendar.today());
        if (period.equals(PERIOD_STARTING_WITH_CURRENT_MONTH)) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        } else if (period.equals(PERIOD_STARTING_WITH_PREVIOUS_MONTH)) {
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        } else if (period.equals(PERIOD_STARTING_WITH_CURRENT_YEAR)) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, 0);
        } else {
            calendar.setTime(new Date(0));
        }
        query.setDate(1, new Date(calendar.getTime().getTime()));
        Iterator work_slips = broker.iterate(query);
        OpWorkSlip work_slip;
        while (work_slips.hasNext()) {
            work_slip = (OpWorkSlip) (work_slips.next());
            data_row = new XComponent(XComponent.DATA_ROW);
            data_row.setStringValue(work_slip.locator());
            data_set.addChild(data_row);
            data_cell = new XComponent(XComponent.DATA_CELL);
            data_cell.setIntValue(work_slip.getNumber());
            data_row.addChild(data_cell);
            data_cell = new XComponent(XComponent.DATA_CELL);
            data_cell.setValue(work_slip.getDate());
            data_row.addChild(data_cell);
        }
        XComponent newWorkSlipButton = form.findComponent(NEW_WORK_SLIP_BUTTON);
        long sessionUserID = session.getUserID();
        OpUser user = (OpUser) (broker.getObject(OpUser.class, sessionUserID));
        Set userResources = user.getResources();
        if (userResources.isEmpty()) {
            broker.close();
            newWorkSlipButton.setEnabled(false);
            return;
        }
        List resourceIds = new ArrayList();
        OpResource resource;
        for (Iterator it = userResources.iterator(); it.hasNext(); ) {
            resource = (OpResource) it.next();
            resourceIds.add(new Long(resource.getID()));
        }
        List types = new ArrayList();
        types.add(new Byte(OpActivity.STANDARD));
        types.add(new Byte(OpActivity.TASK));
        types.add(new Byte(OpActivity.MILESTONE));
        types.add(new Byte(OpActivity.ADHOC_TASK));
        Iterator result = OpWorkSlipDataSetFactory.getAssignments(broker, resourceIds, types, null, OpWorkSlipDataSetFactory.ALL_PROJECTS_ID);
        if (!result.hasNext()) {
            broker.close();
            newWorkSlipButton.setEnabled(false);
            return;
        }
        broker.close();
    }

    /**
    * Gets the period choosed id (the period id for work slips).
    *
    * @param parameters Form parameters
    * @param form       Work Slip Form
    * @param session    Current session
    * @return The period choice ID.
    */
    private String getPeriodID(Map parameters, XComponent form, OpProjectSession session) {
        String period = (String) parameters.get(PERIOD_CHOICE_ID);
        if (period == null) {
            XComponent field = form.findComponent(PERIOD_CHOICE_FIELD);
            if (field != null) {
                String choice = field.getStringValue();
                if (choice == null) {
                    Map stateMap = session.getComponentStateMap(form.getID());
                    if (stateMap != null) {
                        Integer index = (Integer) stateMap.get(PERIOD_CHOICE_FIELD);
                        if (index != null) {
                            XComponent dataSet = field.getDataSetComponent();
                            if (dataSet != null) {
                                XComponent row = (XComponent) dataSet.getChild(index.intValue());
                                choice = row.getStringValue();
                            }
                        }
                    }
                }
                if (choice != null) {
                    period = XValidator.choiceID(choice);
                }
            }
        }
        return period;
    }
}
