package lablog.gui.editors;

import javax.swing.JPanel;
import lablog.util.actions.LablogAction;
import lablog.util.orm.base.ORMBase;

/**
 * A panel for editing an element.
 */
@SuppressWarnings("serial")
public abstract class AbstractEditor<T extends ORMBase> extends JPanel {

    private Class<T> elementClass;

    private String title;

    /**
	 * Default constructor.
	 * This constructor should only be used by the {@link EditorFactory} or by 
	 * subclasses.
	 */
    public AbstractEditor(Class<T> elementClass) {
        this(elementClass, "");
    }

    /**
	 * Extended constructor.
	 * This constructor should only be used by the {@link EditorFactory} or by 
	 * subclasses.
	 * 
	 * @param layout A layout manager handling this view.
	 */
    public AbstractEditor(Class<T> elementClass, String title) {
        super();
        this.elementClass = elementClass;
        this.title = "Editor: " + title;
        initGUI();
    }

    /**
	 * Set the element, that should be edited by this editor.
	 * 
	 * @param element The element to edit.
	 */
    public abstract void setElement(T element);

    /**
	 * Get the edited element.
	 * All properties are updated with the values of the editor.
	 * 
	 * @return The edited element.
	 */
    public abstract T getElement();

    /**
	 * Gets the class of the managed Element.
	 * 
	 * @return The class of the managed Element.
	 */
    public Class<T> getElementClass() {
        return elementClass;
    }

    /**
	 * Gets the title of this editor.
	 * 
	 * @return The title of this editor.
	 */
    public String getTitle() {
        return title;
    }

    public LablogAction getSaveAction() {
        return null;
    }

    /**
	 * 
	 */
    protected abstract void initGUI();
}
