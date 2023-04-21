package org.argouml.util;

import java.util.ArrayList;
import java.util.List;
import javax.jmi.model.MofClass;
import javax.jmi.model.Reference;
import javax.jmi.reflect.RefObject;
import javax.swing.Action;
import org.argouml.ui.ArgoJMenu;
import org.argouml.uml.diagram.ui.ActionFindDesignRationale;
import br.ucam.kuabaSubsystem.util.MofHelper;

public class PopUpHelper {

    private static List<RefObject> visitedRefObjects = new ArrayList<RefObject>();

    public static ArgoJMenu buildDesignRationalePopUp(RefObject source) {
        visitedRefObjects.clear();
        return build(source);
    }

    public static ArgoJMenu build(RefObject source) {
        visitedRefObjects.add(source);
        String name = "";
        if (source.refGetValue("name") != null) name = (String) source.refGetValue("name");
        ArgoJMenu designRationaleMenu = new ArgoJMenu(name);
        Action ownerDesignRationale = new ActionFindDesignRationale(source);
        designRationaleMenu.add(ownerDesignRationale);
        MofClass ownerMofClass = (MofClass) source.refMetaObject();
        List<Reference> references = MofHelper.referencesOfClass(ownerMofClass, true);
        for (Reference reference : references) {
            if (source.refGetValue(reference.getName()) instanceof List) {
                List<RefObject> refObjects = (List<RefObject>) source.refGetValue(reference.getName());
                for (RefObject refValue : refObjects) {
                    Action action = new ActionFindDesignRationale(refValue);
                    ArgoJMenu menu = null;
                    if (!visitedRefObjects.contains(refValue)) {
                        menu = build(refValue);
                        designRationaleMenu.add(menu);
                    }
                }
            }
            if (source.refGetValue(reference.getName()) instanceof RefObject) {
                RefObject refValue = (RefObject) source.refGetValue(reference.getName());
                Action action = new ActionFindDesignRationale(refValue);
                ArgoJMenu menu = null;
                if (!visitedRefObjects.contains(refValue)) {
                    menu = build(refValue);
                    designRationaleMenu.add(menu);
                }
            }
        }
        return designRationaleMenu;
    }
}
