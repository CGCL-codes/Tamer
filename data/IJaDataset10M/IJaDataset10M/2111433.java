package ontool.app.modelview;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import ontool.model.ClassModel;
import ontool.model.FunctionModel;
import ontool.model.Model;
import ontool.model.ModelEvent;
import ontool.model.ModelObserver;
import ontool.model.NameableModel;
import org.apache.log4j.Category;

/**
 * This class provides a SN-class code editor.
 *
 * @author <a href="mailto:asrgomes@dca.fee.unicamp.br">Antonio S.R. Gomes<a/>
 * @version $Id: ClassCodeEditor.java,v 1.1 2003/10/22 03:06:43 asrgomes Exp $
 */
class ClassCodeEditor extends AbstractCodeEditor implements ModelObserver {

    protected static final Category cat = Category.getInstance(ClassCodeEditor.class);

    private ClassModel classModel;

    protected ClassCodeEditor(ClassModel classModel) {
        super("Class " + classModel.getName());
        this.classModel = classModel;
        classModel.addObserver(this);
    }

    /**
	 * Creates all editors.
	 */
    private void initEditors() {
        addEditor(new init_TextWrapper(classModel), "init");
        addEditor(new clean_TextWrapper(classModel), "clean");
        addEditor(new classBody_TextWrapper(classModel), "<class body>");
        for (Iterator i = classModel.getChildren(FunctionModel.class).iterator(); i.hasNext(); ) {
            FunctionModel fm = (FunctionModel) i.next();
            addEditor(new match_TextWrapper(fm), fm.getName() + "(eval)");
            addEditor(new perform_TextWrapper(fm), fm.getName() + "(perform)");
        }
    }

    private static Map lockedClasses = new Hashtable();

    public static ClassCodeEditor open(ClassModel classModel) {
        ClassCodeEditor cce;
        if (lockedClasses.containsKey(classModel)) {
            cce = (ClassCodeEditor) lockedClasses.get(classModel);
            cce.toFront();
        } else {
            cce = new ClassCodeEditor(classModel);
            cce.initEditors();
            cce.pack();
            lockedClasses.put(classModel, cce);
        }
        return cce;
    }

    public void dispose() {
        lockedClasses.remove(classModel);
        classModel.removeObserver(this);
        super.dispose();
    }

    /**
	 * Updates this object with model events.
	 *
	 * @param me model event 
	 */
    public void update(ModelEvent me) {
        if (me.isType(Model.BEING_DESTROYED)) {
            forceDispose();
        } else if (me.isType(Model.ADDED_CHILD)) {
        } else if (me.isType(Model.REMOVED_CHILD)) {
        } else if (me.isType(NameableModel.SET_NAME)) {
        }
    }

    /**
	 * Custom text wrapper for the init method.
	 */
    private class init_TextWrapper implements TextWrapper {

        private ClassModel cm;

        public init_TextWrapper(ClassModel cm) {
            this.cm = cm;
        }

        public void setText(String text) {
            cm.setInitImpl(text);
        }

        public String getText() {
            return cm.getInitImpl();
        }
    }

    private class clean_TextWrapper implements TextWrapper {

        private ClassModel cm;

        public clean_TextWrapper(ClassModel cm) {
            this.cm = cm;
        }

        public void setText(String text) {
            cm.setCleanImpl(text);
        }

        public String getText() {
            return cm.getCleanImpl();
        }
    }

    private class perform_TextWrapper implements TextWrapper {

        private FunctionModel fm;

        public perform_TextWrapper(FunctionModel fm) {
            this.fm = fm;
        }

        public void setText(String text) {
            fm.setPerformImpl(text);
        }

        public String getText() {
            return fm.getPerformImpl();
        }
    }

    private class match_TextWrapper implements TextWrapper {

        private FunctionModel fm;

        public match_TextWrapper(FunctionModel fm) {
            this.fm = fm;
        }

        public void setText(String text) {
            fm.setMatchImpl(text);
        }

        public String getText() {
            return fm.getMatchImpl();
        }
    }

    private class classBody_TextWrapper implements TextWrapper {

        private ClassModel cm;

        public classBody_TextWrapper(ClassModel cm) {
            this.cm = cm;
        }

        public void setText(String text) {
            cm.setClassBody(text);
        }

        public String getText() {
            return cm.getClassBody();
        }
    }
}
