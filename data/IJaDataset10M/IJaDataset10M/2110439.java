package takatuka.optimizer.deadCodeRemoval.logic.classAndMethod;

import java.util.*;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.logic.util.*;
import takatuka.optimizer.cpGlobalization.logic.util.*;
import takatuka.optimizer.deadCodeRemoval.dataObj.*;

/**
 * <p>Title: </p>
 * <p>Description:
 * If a method is kept in a superclass then that method is kept in all
 * the sub-classes which are kept  and also in all the super-classes of
 * those sub-classes.
 * 
 *
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class PassTwo {

    private static final PassTwo myObj = new PassTwo();

    private PassTwo() {
    }

    public static PassTwo getInstanceOf() {
        return myObj;
    }

    public void execute() {
        try {
            MCMarkPhase mcMarkPhase = MCMarkPhase.getInstanceOf();
            HashSet<MethodExamineListEntry> setOfMethodKept = mcMarkPhase.getAllMethodKept();
            HashSet<MethodExamineListEntry> alreadyChecked = new HashSet<MethodExamineListEntry>();
            Iterator<MethodExamineListEntry> it = setOfMethodKept.iterator();
            while (it.hasNext()) {
                MethodExamineListEntry entry = it.next();
                if (alreadyChecked.contains(entry)) {
                    continue;
                }
                alreadyChecked.add(entry);
                DCClassFile cFile = entry.getMethodClass();
                HashSet<ClassFile> subClasses = new HashSet();
                if (!cFile.getAccessFlags().isInterface()) {
                    subClasses.addAll(mcMarkPhase.getAllSubClasses(cFile));
                } else {
                    Oracle oracle = Oracle.getInstanceOf();
                    oracle.getAllImplementations(cFile, subClasses);
                }
                execute(entry, subClasses);
            }
            MCMarkPhase.getInstanceOf().execute();
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
    }

    private void execute(MethodExamineListEntry methodExamineEntry, HashSet<ClassFile> subClasses) {
        DCMethodInfo methodInfo = methodExamineEntry.getMethodInfo();
        Oracle oracle = Oracle.getInstanceOf();
        if (methodInfo.getAccessFlags().isStatic()) {
            return;
        }
        Iterator<ClassFile> subClassesIt = subClasses.iterator();
        while (subClassesIt.hasNext()) {
            DCClassFile cFile = (DCClassFile) subClassesIt.next();
            if (cFile.isKeepPerUserRequest() || cFile.getInstanceAccess()) {
                FieldInfo method = oracle.getMethodOrField(cFile, methodInfo, true);
                if (method == null) {
                    continue;
                }
                keepTheMethodAndAddInTheExamineList(method);
            }
        }
    }

    private void keepTheMethodAndAddInTheExamineList(FieldInfo methodToKeep) {
        Oracle oracle = Oracle.getInstanceOf();
        DCMethodInfo dcMethodToKeep = (DCMethodInfo) methodToKeep;
        ClassFile cFile = methodToKeep.getClassFile();
        String fullyQualifiedName = cFile.getFullyQualifiedClassName();
        MultiplePoolsFacade cp = cFile.getConstantPool();
        ClassFile.currentClassToWorkOn = cFile;
        String methodDesc = oracle.methodOrFieldDescription(methodToKeep, cp);
        String methodName = oracle.methodOrFieldName(methodToKeep, cp);
        MethodExamineListEntry newMethodExamineEntry = new MethodExamineListEntry(fullyQualifiedName, methodName, methodDesc, true, false);
        if (ExamineList.getInstanceOf().add(newMethodExamineEntry)) {
            MCMarkPhase.debugOutput("Method added to examine in PassTwo =" + newMethodExamineEntry);
        }
    }
}
