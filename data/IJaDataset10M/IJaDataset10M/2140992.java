package tm.javaLang;

import java.util.Hashtable;
import javax.swing.text.html.HTML;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.Stepper;
import tm.javaLang.ast.Java_StepperBuiltIn;
import tm.javaLang.ast.Java_StepperBuiltInGeneric;
import tm.utilities.Assert;

/** Build steppers for built-in (native) functions.
 * @author theo
 */
public class BuiltInStepperFactory {

    private static Hashtable<String, Stepper> ht = new Hashtable<String, Stepper>();

    /**
     * @param decl The declaration of the method
     * @return
     */
    public static Stepper makeStepper(Declaration decl) {
        ScopedName fqn = decl.getName();
        String fullName = fqn.getName();
        String methodName = fqn.getTerminalId();
        fqn.removeTerminalId();
        String className = fqn.getName();
        fqn.append(methodName);
        Stepper stepper = ht.get(fullName);
        if (stepper != null) return stepper;
        if (methodName.equals("read") && className.equals("java.io.InputStream")) {
            stepper = new Java_StepperBuiltIn(Java_StepperBuiltIn.GET);
        } else if (methodName.equals("print") && className.equals("java.io.PrintStream")) {
            stepper = new Java_StepperBuiltIn(Java_StepperBuiltIn.OUTPUT);
        } else if (className.equals("tm.scripting.ScriptManager")) {
            if (methodName.equals("relay")) {
                stepper = new Java_StepperBuiltIn(Java_StepperBuiltIn.SCRIPT_RELAY);
            } else if (methodName.equals("relayRtnInt")) {
                stepper = new Java_StepperBuiltIn(Java_StepperBuiltIn.SCRIPT_RELAY_RTN_INT);
            } else if (methodName.equals("relayRtnDouble")) {
                stepper = new Java_StepperBuiltIn(Java_StepperBuiltIn.SCRIPT_RELAY_RTN_DOUBLE);
            } else if (methodName.equals("snapShot")) {
                stepper = new Java_StepperBuiltIn(Java_StepperBuiltIn.SCRIPT_SNAPSHOT);
            } else if (methodName.equals("setReference")) {
                stepper = new Java_StepperBuiltIn(Java_StepperBuiltIn.SCRIPT_REFERENCE);
            } else if (methodName.equals("compareReference")) {
                stepper = new Java_StepperBuiltIn(Java_StepperBuiltIn.SCRIPT_COMPARE_REF);
            } else stepper = new Java_StepperBuiltInGeneric(className, methodName);
        } else {
            stepper = new Java_StepperBuiltInGeneric(className, methodName);
        }
        ht.put(fullName, stepper);
        return stepper;
    }
}
