package mipt.aaf.edit.data.link;

import mipt.aaf.edit.data.DataChangeListener;
import mipt.aaf.edit.data.DataEditor;
import mipt.aaf.edit.data.DefaultDataEditorChanger;

/**
 * DefaultDataEditListener that can save also child Data (one-to-many links)
 * If your editor is not instance of ChildDataProcessor set appropriate DataSetChanger
 * @author Evdokimov
 */
public class ChildDataEditorChanger extends DefaultDataEditorChanger {

    protected DataSetChanger setChanger;

    /**
	 * 
	 */
    public ChildDataEditorChanger() {
    }

    /**
	 * @param listener
	 */
    public ChildDataEditorChanger(DataChangeListener listener) {
        super(listener);
    }

    protected boolean changeData(DataEditor editor) {
        if (!getSetChanger().changeDataChilds(editor)) return false;
        return super.changeData(editor);
    }

    /**
	 * 
	 */
    public final DataSetChanger getSetChanger() {
        if (setChanger == null) setChanger = initSetChanger();
        return setChanger;
    }

    /**
	 * 
	 */
    protected DataSetChanger initSetChanger() {
        return new DataSetChanger();
    }

    /**
	 * @param setChanger
	 */
    public void setSetChanger(DataSetChanger setChanger) {
        this.setChanger = setChanger;
    }
}
