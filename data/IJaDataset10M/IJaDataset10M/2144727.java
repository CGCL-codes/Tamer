package onepoint.project.modules.project_planning;

import java.util.HashMap;
import onepoint.express.XExtendedComponent;
import onepoint.express.server.XDefaultComponentHandler;
import onepoint.log.XLog;
import onepoint.log.XLogFactory;
import onepoint.project.modules.project_planning.components.OpProjectComponent;
import onepoint.project.util.OpProjectCalendar;
import onepoint.xml.XContext;

public class OpProjectComponentHandler extends XDefaultComponentHandler {

    private static final XLog logger = XLogFactory.getLogger(OpProjectComponentHandler.class);

    public static final String GANTT_BOX = "gantt-box";

    public static final String PROJECT_GANTT_BOX = "project-gantt-box";

    public static final String GANTT_MAP = "gantt-map";

    public static final String UTILIZATION_BOX = "utilization-box";

    protected static final String ON_ACTIVITY_DETAILS = "on-activity-details";

    protected static final String ON_PROJECT_DETAILS = "on-project-details";

    protected static final String CATEGORY_COLOR_SET_REF = "category-color-set-ref";

    private static final String GANTT_BOX_REF = "gantt-box-ref";

    private static final String HISTORY_REF = "history-ref";

    private static final String TIME_UNIT = "time-unit";

    private static final String GANTT_CAPTION_LEFT = "caption-left";

    private static final String GANTT_CAPTION_RIGHT = "caption-right";

    private static final String RESOURCE_TABLE_ID = "resource-table";

    private static final String ALTERNATE_DETAILS_FORM_REF = "alternate-details-form-ref";

    private static final String ON_ACTIVITY_SELECT = "on-activity-select";

    protected byte _parseTimeUnitValue(String value) {
        if ((value != null)) {
            if (value.equals("days")) {
                return OpProjectCalendar.DAYS;
            } else if (value.equals("weeks")) {
                return OpProjectCalendar.WEEKS;
            } else if (value.equals("months")) {
                return OpProjectCalendar.MONTHS;
            }
        }
        return -1;
    }

    public Object newNode(XContext context, String name, HashMap attributes) {
        logger.debug("OpProjectComponentHandler.newNode() : name = " + name);
        OpProjectComponent component = null;
        Object value;
        if (name == GANTT_BOX) {
            component = new OpProjectComponent(OpProjectComponent.GANTT_BOX);
            _parseScrollBoxAttributes(component, attributes);
            value = attributes.get(DATA_SET_REF);
            if ((value != null) && (value instanceof String)) {
                component.setDataSetRef((String) value);
            }
            value = attributes.get(HISTORY_REF);
            if ((value != null) && (value instanceof String)) {
                component.setHistoryRef((String) value);
            }
            value = attributes.get(TIME_UNIT);
            component.setTimeUnit(_parseTimeUnitValue((String) value));
            value = attributes.get(GANTT_CAPTION_LEFT);
            if ((value != null) && (value instanceof String)) {
                component.setCaptionLeft((String) value);
            }
            value = attributes.get(GANTT_CAPTION_RIGHT);
            if ((value != null) && (value instanceof String)) {
                component.setCaptionRight((String) value);
            }
            value = attributes.get(ON_ACTIVITY_DETAILS);
            if ((value != null) && (value instanceof String)) {
                component.setOnActivityDetails((String) value);
            }
            value = attributes.get(CATEGORY_COLOR_SET_REF);
            if ((value != null) && (value instanceof String)) {
                component.setCategoryColorSetRef((String) value);
            }
            value = attributes.get(ON_ACTIVITY_SELECT);
            if ((value != null) && (value instanceof String)) {
                component.setOnActivitySelect((String) value);
            }
            value = attributes.get(ROW_HEIGHT);
            if ((value != null) && (value instanceof String)) {
                if (value.equals(VARIABLE)) {
                    component.setRowHeight(XExtendedComponent.VARIABLE_ROW_HEIGHT);
                } else {
                    component.setRowHeight(Integer.parseInt((String) value));
                }
            }
        } else if (name == PROJECT_GANTT_BOX) {
            component = new OpProjectComponent(OpProjectComponent.PROJECT_GANTT_BOX);
            _parseScrollBoxAttributes(component, attributes);
            value = attributes.get(DATA_SET_REF);
            if ((value != null) && (value instanceof String)) {
                component.setDataSetRef((String) value);
            }
            value = attributes.get(TIME_UNIT);
            component.setTimeUnit(_parseTimeUnitValue((String) value));
            value = attributes.get(GANTT_CAPTION_LEFT);
            if ((value != null) && (value instanceof String)) {
                component.setCaptionLeft((String) value);
            }
            value = attributes.get(GANTT_CAPTION_RIGHT);
            if ((value != null) && (value instanceof String)) {
                component.setCaptionRight((String) value);
            }
            value = attributes.get(ON_PROJECT_DETAILS);
            if ((value != null) && (value instanceof String)) {
                component.setOnProjectDetails((String) value);
            }
            value = attributes.get(CATEGORY_COLOR_SET_REF);
            if ((value != null) && (value instanceof String)) {
                component.setCategoryColorSetRef((String) value);
            }
        } else if (name == GANTT_MAP) {
            component = new OpProjectComponent(OpProjectComponent.GANTT_MAP);
            value = attributes.get(GANTT_BOX_REF);
            if ((value != null) && (value instanceof String)) {
                component.setGanttBoxRef((String) value);
            }
        } else if (name == UTILIZATION_BOX) {
            component = new OpProjectComponent(OpProjectComponent.UTILIZATION_BOX);
            _parseScrollBoxAttributes(component, attributes);
            value = attributes.get(DATA_SET_REF);
            if ((value != null) && (value instanceof String)) {
                component.setDataSetRef((String) value);
            }
            value = attributes.get(TIME_UNIT);
            component.setTimeUnit(_parseTimeUnitValue((String) value));
            value = attributes.get(RESOURCE_TABLE_ID);
            if ((value != null) && (value instanceof String)) {
                component.setResourceTableId((String) value);
            }
            value = attributes.get(ALTERNATE_DETAILS_FORM_REF);
            if ((value != null) && (value instanceof String)) {
                component.setAlternateDetailsFormRef((String) value);
            }
            value = attributes.get(ROW_HEIGHT);
            if ((value != null) && (value instanceof String)) {
                if (value.equals(VARIABLE)) {
                    component.setRowHeight(XExtendedComponent.VARIABLE_ROW_HEIGHT);
                } else {
                    component.setRowHeight(Integer.parseInt((String) value));
                }
            }
        }
        parseCommonAttributes(component, attributes);
        logger.debug("/OpProjectComponentHandler.newNode() : component = " + component);
        return component;
    }
}
