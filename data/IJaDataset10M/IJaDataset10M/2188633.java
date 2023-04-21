package app.classes.actions;

import app.ui.IAppContext;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class ClassRelationShip extends AbstractClassRelationShip implements IClassRelationShip {

    private SuperClassRelationShip superClass;

    private ArrayList<Class> classNamesList;

    private ArrayList<SubClassRelationShip> subClassesList;

    public SuperClassRelationShip getSuperClass() {
        return superClass;
    }

    public ArrayList<SubClassRelationShip> getSubClassesList() {
        return subClassesList;
    }

    public void setSuperClass(SuperClassRelationShip superClassRelationShip) {
        superClass = superClassRelationShip;
    }

    public void setSubClassesList(ArrayList<SubClassRelationShip> subClassRelationShip) {
        subClassesList = subClassRelationShip;
    }

    public ClassRelationShip(IAppContext appContext) {
        super(appContext);
        subClassesList = new ArrayList<SubClassRelationShip>();
        initSuperClass();
        initSubClasses();
    }

    private void initSuperClass() {
        Class superClassTemp = getLoadedClass().getSuperclass();
        if (superClassTemp != null) {
            getAppContext().getDrawingManager().getClassRelationShipManager().setCurrentClassName(superClassTemp.getName());
            superClass = new SuperClassRelationShip(getAppContext());
            if (superClass.getClassName().startsWith("java")) {
                superClass = null;
            }
        }
    }

    private void initSubClasses() {
        classNamesList = getAppContext().getGUIManager().getMainWindow().getClassNamesList();
        for (Class classNameTemp : classNamesList) {
            Class superclass = classNameTemp.getSuperclass();
            if (superclass != null) {
                String superClassNameTemp = superclass.getName();
                if (superClassNameTemp.equals(getClassName())) {
                    getAppContext().getDrawingManager().getClassRelationShipManager().setCurrentClassName(classNameTemp.getName());
                    subClassesList.add(new SubClassRelationShip(getAppContext()));
                }
            }
        }
    }
}
