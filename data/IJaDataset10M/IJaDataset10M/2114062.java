package org.eclipse.ui.forms.article.rcp;

/**
 * @author dejan
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TypeOne extends NamedObject {

    public static final String P_CHOICE = "choice";

    public static final String P_FLAG = "flag";

    public static final String P_TEXT = "text";

    public static final String[] CHOICES = { Messages.getString("TypeOne.c1"), Messages.getString("TypeOne.c2"), Messages.getString("TypeOne.c3"), Messages.getString("TypeOne.c4") };

    private int choice = 0;

    private String text;

    private boolean flag;

    /**
	 * @param name
	 */
    public TypeOne(String name, int choice, boolean flag, String text) {
        super(name);
        this.flag = flag;
        this.text = text;
        this.choice = choice;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
        model.fireModelChanged(new Object[] { this }, IModelListener.CHANGED, P_CHOICE);
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
        model.fireModelChanged(new Object[] { this }, IModelListener.CHANGED, P_FLAG);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        model.fireModelChanged(new Object[] { this }, IModelListener.CHANGED, P_TEXT);
    }
}
