package jsbsim.models.flight_control;

import java.util.ArrayList;
import java.util.Iterator;
import jsbsim.FGUnitConverter;
import jsbsim.enums.SwitchLogic;
import jsbsim.math.FGCondition;
import jsbsim.models.FGFCS;
import jsbsim.models.atmosphere.FGFCSComponent;
import org.jdom.Element;

/** Encapsulates a switch for the flight control system.

The switch component models a switch - either on/off or a multi-choice rotary
switch. The switch can represent a physical cockpit switch, or can represent a
logical switch, where several conditions might need to be satisfied before a
particular state is reached. The value of the switch - the output value for the
component - is chosen depending on the state of the switch. Each switch is
comprised of one or more tests. Each test has a value associated with it. The
first test that evaluates to true will set the output value of the switch
according to the value parameter belonging to that test. Each test contains one
or more conditions, which each must be logically related (if there are more than
one) given the value of the logic attribute, and which takes the form:

property conditional property|value

e.g.

qbar ge 21.0

or,

roll_rate == pitch_rate

Within a test, additional tests can be specified, which allows for
complex groupings of logical comparisons. Each test contains
additional conditions, as well as possibly additional tests.

@code
<switch name="switch1">
<default value="{property|value}"/>
<test logic="{AND|OR}" value="{property|value}">
{property} {conditional} {property|value}
<test logic="{AND|OR}">
{property} {conditional} {property|value}
...
</test>
...
</test>
<test logic="{AND|OR}" value="{property|value}">
{property} {conditional} {property|value}
...
</test>
...
[<output> {property} </output>]
</switch>
@endcode

Here's an example:

@code
<switch name="roll a/p autoswitch">
<default value="0.0"/>
<test value="fcs/roll-ap-error-summer">
ap/attitude_hold == 1
</test>
</switch>
@endcode

Note: In the "logic" attribute, "AND" is the default logic, if none is supplied.

The above example specifies that the default value of the component (i.e. the
output property of the component, addressed by the property, ap/roll-ap-autoswitch)
is 0.0.  If or when the attitude hold switch is selected (property
ap/attitude_hold takes the value 1), the value of the switch component will be
whatever value fcs/roll-ap-error-summer is.

The switch component is defined as follows (see the API documentation for more
information):

@code
<switch name="switch1">
<default value="{property|value}"/>
<test logic="{AND|OR}" value="{property|value}">
{property} {conditional} {property|value}
<test logic="{AND|OR}">
{property} {conditional} {property|value}
...
</test>
...
</test>
<test logic="{AND|OR}" value="{property|value}">
{property} {conditional} {property|value}
...
</test>
...
</switch>
@endcode
@author Jon S. Berndt
@version $Id: FGSwitch.h,v 1.11 2008/05/01 01:03:14 dpculp Exp $
 */
public class FGSwitch extends FGFCSComponent {

    private ArrayList<SwitchTest> tests = new ArrayList<SwitchTest>();

    /** Constructor
    @param fcs a pointer to the parent FGFCS class
    @param element a pointer to the Element (from the config file XML tree)
    that represents this switch component */
    public FGSwitch(FGFCS fcs, Element element) {
        super(fcs, element);
        String value, logic;
        SwitchTest current_test;
        super.bind();
        Iterator<Element> children = element.getChildren().iterator();
        while (children.hasNext()) {
            Element test_element = children.next();
            if ("default".equals(test_element.getName())) {
                current_test = new SwitchTest();
                current_test.Logic = SwitchLogic.eDefault;
                tests.add(current_test);
            } else if ("test".equals(test_element.getName())) {
                current_test = new SwitchTest();
                logic = test_element.getAttributeValue("logic");
                if ("OR".equals(logic)) {
                    current_test.Logic = SwitchLogic.eOR;
                } else if ("AND".equals(logic)) {
                    current_test.Logic = SwitchLogic.eAND;
                } else if (null == logic) {
                    current_test.Logic = SwitchLogic.eAND;
                } else {
                    System.err.println("Unrecognized LOGIC token " + logic + " in switch component: " + Name);
                }
                String data_lines[] = FGUnitConverter.GetDataLinesArray(test_element);
                int i = -1, count = data_lines.length;
                while (++i < count) {
                    current_test.conditions.add(new FGCondition(data_lines[i], PropertyManager));
                }
                Iterator<Element> conditions = test_element.getChildren().iterator();
                while (conditions.hasNext()) {
                    Element condition = conditions.next();
                    current_test.conditions.add(new FGCondition(condition, PropertyManager));
                }
                tests.add(current_test);
            }
            if (!"output".equals(test_element.getName()) && !"description".equals(test_element.getName())) {
                value = test_element.getAttributeValue("value");
                if (value.isEmpty()) {
                    System.err.println("No VALUE supplied for switch component: " + Name);
                } else {
                    current_test = new SwitchTest();
                    try {
                        current_test.OutputVal = Double.valueOf(value);
                    } catch (Exception ex) {
                        System.err.println(ex.getMessage());
                        if ('-' == value.charAt(0)) {
                            current_test.sign = -1.0f;
                            value = value.replaceFirst("-", "");
                        } else {
                            current_test.sign = 1.0f;
                        }
                        current_test.OutputProp = PropertyManager.GetNode(value);
                    }
                }
            }
        }
        Debug(0);
    }

    @Override
    public void delete() {
        super.delete();
        tests.clear();
        tests = null;
    }

    /** Executes the switch logic.
    @return true - always*/
    @Override
    public boolean Run() {
        boolean pass = false;
        double default_output = 0.0;
        for (int i = 0; i < tests.size(); i++) {
            switch(tests.get(i).Logic) {
                case SwitchLogic.eDefault:
                    default_output = tests.get(i).GetValue();
                    break;
                case SwitchLogic.eAND:
                    pass = true;
                    for (int j = 0; j < tests.get(i).conditions.size(); j++) {
                        if (!tests.get(i).conditions.get(j).Evaluate()) {
                            pass = false;
                        }
                    }
                    break;
                case SwitchLogic.eOR:
                    pass = false;
                    for (int j = 0; j < tests.get(i).conditions.size(); j++) {
                        if (tests.get(i).conditions.get(j).Evaluate()) {
                            pass = true;
                        }
                    }
                    break;
                default:
                    System.err.println("Invalid logic test");
            }
            if (pass) {
                Output = tests.get(i).GetValue();
                break;
            }
        }
        if (!pass) {
            Output = default_output;
        }
        Clip();
        if (IsOutput) {
            SetOutput();
        }
        return true;
    }

    @Override
    public void Debug(int from) {
        if (debug_lvl <= 0) {
            return;
        }
        if ((debug_lvl & 1) > 0) {
            if (from == 0) {
                String comp = "", scratch;
                String indent = "\t";
                boolean first = false;
                if (null == tests) return;
                for (int i = 0; i < tests.size(); i++) {
                    scratch = " if ";
                    switch(tests.get(i).Logic) {
                        case SwitchLogic.elUndef:
                            comp = " UNSET ";
                            System.err.println("Unset logic for test condition");
                            break;
                        case SwitchLogic.eAND:
                            comp = " AND ";
                            break;
                        case SwitchLogic.eOR:
                            comp = " OR ";
                            break;
                        case SwitchLogic.eDefault:
                            scratch = " by default.";
                            break;
                        default:
                            comp = " UNKNOWN ";
                            System.err.println("Unknown logic for test condition");
                    }
                    if (tests.get(i).OutputProp != null) {
                        if (tests.get(i).sign < 0) {
                            System.out.println(indent + "Switch VALUE is - " + tests.get(i).OutputProp.GetName() + scratch);
                        } else {
                            System.out.println(indent + "Switch VALUE is " + tests.get(i).OutputProp.GetName() + scratch);
                        }
                    } else {
                        System.out.println(indent + "Switch VALUE is " + tests.get(i).OutputVal + scratch);
                    }
                    first = true;
                    for (int j = 0; j < tests.get(i).conditions.size(); j++) {
                        if (!first) {
                            System.out.println(indent + comp + " ");
                        } else {
                            System.out.println(indent + " ");
                        }
                        first = false;
                        tests.get(i).conditions.get(j).PrintCondition();
                    }
                }
                if (IsOutput) {
                    System.out.println("      OUTPUT: " + OutputNode.GetName());
                }
            }
        }
        if ((debug_lvl & 2) > 0) {
            switch(from) {
                case 0:
                    System.out.println("Instantiated: FGSwitch");
                    break;
                case 1:
                    System.out.println("Destroyed:    FGSwitch");
                    break;
            }
        }
        if ((debug_lvl & 64) > 0) {
            if (from == 0) {
                System.out.println("$Id$");
            }
        }
    }
}
