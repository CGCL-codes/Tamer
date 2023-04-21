package onepoint.project.modules.project_planning.components;

import java.io.IOException;
import onepoint.express.XExpressProxy;
import onepoint.project.modules.project.components.OpGanttValidator;
import onepoint.project.util.OpHashProvider;
import onepoint.script.interpreter.XInterpreterException;

/**
 * @author gmesaric
 */
public class OpProjectComponentProxy extends XExpressProxy {

    public OpProjectComponentProxy() {
    }

    private static final String PROJECT_COMPONENT = OpProjectComponent.class.getName().intern();

    private static final String GANTT_VALIDATOR = OpGanttValidator.class.getName().intern();

    private static final String HASH_PROVIDER = OpHashProvider.class.getName().intern();

    private static final Class GANTT_VALIDATOR_CLASS = OpGanttValidator.class;

    private static final Class PROJECT_COMPONENT_CLASS = OpProjectComponent.class;

    private static final Class HASH_PROVIDER_CLASS = OpHashProvider.class;

    private static final String SET_TIME_UNIT = "setTimeUnit".intern();

    private static final String PERCENT_ASSIGNED = "percentAssigned".intern();

    private static final String GET_RESOURCE_NAME = "getResourceName".intern();

    private static final String CHANGE_TOOL = "changeTool".intern();

    private static final String RESET_CALENDAR = "resetCalendar".intern();

    private static final String SET_VIEW_TYPE = "setViewType".intern();

    private static final String SET_CAPTION_LEFT = "setCaptionLeft".intern();

    private static final String SET_CAPTION_RIGHT = "setCaptionRight".intern();

    private static final String CALCULATE_HASH = "calculateHash";

    private static final String[] _class_names = { PROJECT_COMPONENT, GANTT_VALIDATOR, HASH_PROVIDER };

    private static final Class[] _classes = { PROJECT_COMPONENT_CLASS, GANTT_VALIDATOR_CLASS, HASH_PROVIDER_CLASS };

    public Class[] getClasses() {
        return _classes;
    }

    public String[] getClassNames() {
        return _class_names;
    }

    public Object newInstance(String class_name) throws XInterpreterException {
        throw new XInterpreterException("No class name " + class_name + " defined in this proxy");
    }

    public Object invokeMethod(String class_name, String method_name, Object[] arguments) throws XInterpreterException, IOException {
        if (class_name == GANTT_VALIDATOR) {
            if (method_name == PERCENT_ASSIGNED) {
                if (arguments.length == 1) {
                    return new Double(OpGanttValidator.percentageAssigned((String) arguments[0]));
                } else {
                    throw new XInterpreterException("Wrong number of arguments for method " + class_name + "." + method_name);
                }
            } else if (method_name == GET_RESOURCE_NAME) {
                if (arguments.length == 2) {
                    return OpGanttValidator.getResourceName((String) arguments[0], (String) arguments[1]);
                } else {
                    throw new XInterpreterException("Wrong number of arguments for method " + class_name + "." + method_name);
                }
            } else {
                throw new XInterpreterException("Class OpGanttValidator does not define a method named " + method_name);
            }
        } else if (class_name == HASH_PROVIDER) {
            if (method_name.equals(CALCULATE_HASH)) {
                if (arguments.length == 2) {
                    return new OpHashProvider().calculateHash((String) arguments[0], (String) arguments[1]);
                } else {
                    throw new XInterpreterException("Wrong number of arguments for method " + class_name + "." + method_name);
                }
            } else {
                throw new XInterpreterException("Class Console does not define a method named " + method_name);
            }
        } else {
            return super.invokeMethod(class_name, method_name, arguments);
        }
    }

    public Object invokeMethod(Object object, String method_name, Object[] arguments) throws XInterpreterException, IOException {
        if (object instanceof OpProjectComponent) {
            OpProjectComponent component = (OpProjectComponent) object;
            if (method_name == SET_TIME_UNIT) {
                if (arguments.length == 1) {
                    component.setTimeUnit(((Integer) (arguments[0])).byteValue());
                } else {
                    throw new XInterpreterException("Wrong number of arguments for method " + object.getClass().getName() + "." + method_name);
                }
                return null;
            } else if (method_name == CHANGE_TOOL) {
                if (arguments.length == 1) {
                    component.changeTool((String) (arguments[0]));
                } else {
                    throw new XInterpreterException("Wrong number of arguments for method " + object.getClass().getName() + "." + method_name);
                }
                return null;
            } else if (method_name == RESET_CALENDAR) {
                if (arguments.length == 0) {
                    component.resetCalendar();
                } else {
                    throw new XInterpreterException("Wrong number of arguments for method " + object.getClass().getName() + "." + method_name);
                }
                return null;
            } else if (method_name == SET_VIEW_TYPE) {
                if (arguments.length == 1) {
                    component.setViewType(((Integer) (arguments[0])).intValue());
                } else {
                    throw new XInterpreterException("Wrong number of arguments for method " + object.getClass().getName() + "." + method_name);
                }
                return null;
            } else if (SET_CAPTION_LEFT.equals(method_name)) {
                if (arguments.length == 1) {
                    component.setCaptionLeft((String) arguments[0]);
                } else {
                    throw new XInterpreterException("Wrong number of arguments for method " + object.getClass().getName() + "." + method_name);
                }
                return null;
            } else if (SET_CAPTION_RIGHT.equals(method_name)) {
                if (arguments.length == 1) {
                    component.setCaptionRight((String) arguments[0]);
                } else {
                    throw new XInterpreterException("Wrong number of arguments for method " + object.getClass().getName() + "." + method_name);
                }
                return null;
            } else {
                return super.invokeMethod(object, method_name, arguments);
            }
        } else {
            return super.invokeMethod(object, method_name, arguments);
        }
    }
}
