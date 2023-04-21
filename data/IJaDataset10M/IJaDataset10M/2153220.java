package org.argouml.uml.cognitive.critics;

import javax.swing.Icon;
import org.argouml.cognitive.Designer;
import ru.novosoft.uml.foundation.core.MModelElement;

public class CrIllegalName extends CrUML {

    public CrIllegalName() {
        setHeadline("Choose a Legal Name");
        addSupportedDecision(CrUML.decNAMING);
        addTrigger("name");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(dm instanceof MModelElement)) return NO_PROBLEM;
        MModelElement me = (MModelElement) dm;
        String meName = me.getName();
        if (meName == null || meName.equals("")) return NO_PROBLEM;
        String nameStr = meName;
        int len = nameStr.length();
        for (int i = 0; i < len; i++) {
            char c = nameStr.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') return PROBLEM_FOUND;
        }
        return NO_PROBLEM;
    }

    public Icon getClarifier() {
        return ClClassName.TheInstance;
    }
}
