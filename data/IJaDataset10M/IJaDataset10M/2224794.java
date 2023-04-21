package deduced.viewer.editor;

import javax.swing.BorderFactory;
import deduced.PropertyCollection;
import deduced.controller.PropertyCollectionController;
import deduced.controller.PropertyController;
import deduced.viewer.TypeIconMap;

/**
 * TextFieldSetter
 * 
 * @author Duff
 */
public class PropertyListEditor extends PropertyEditorImpl {

    public static interface PropertyListEditorPanel extends PropertyEditorPanel {

        public InstanceEditorPanel getEditorPanel();
    }

    public static class DefaultPropertyListEditorPanel extends DefaultEditorPanel implements PropertyListEditorPanel {

        private InstanceEditorPanel _editorPanel;

        public DefaultPropertyListEditorPanel(boolean createDelete, PropertyEditorFactory factory) {
            super(createDelete);
            _editorPanel = new InstanceEditorPanel(factory);
            _editorPanel.getRootPanel().setBorder(BorderFactory.createRaisedBevelBorder());
            addComponent(_editorPanel.getRootPanel());
        }

        /**
         * (non-Javadoc)
         * 
         * @see deduced.viewer.editor.PropertyListEditor.PropertyListEditorPanel#getEditorPanel()
         */
        public InstanceEditorPanel getEditorPanel() {
            return _editorPanel;
        }
    }

    private PropertyCollectionController _observedController;

    private final PropertyEditorFactory _factory;

    public PropertyListEditor(PropertyEditorFactory factory) {
        _factory = factory;
        setEditorPanel(createEditionComponent());
    }

    public Object getCurrentComponentValue() {
        return getCurrentValue();
    }

    /**
     * (non-Javadoc)
     * 
     * @see deduced.viewer.editor.old.PropertyEditor#updatedDisplayedResult()
     */
    protected void updatedDisplayedResult() {
        if (isBlockingUpdates()) {
            return;
        }
        PropertyCollectionController observedController = getObservedController();
        if (observedController == _observedController) {
            return;
        }
        _observedController = observedController;
        InstanceEditorPanel listEditorPanel = getListEditorPanel();
        if (listEditorPanel != null) {
            listEditorPanel.setController(_observedController);
        }
    }

    public InstanceEditorPanel getListEditorPanel() {
        InstanceEditorPanel retVal = null;
        PropertyEditorPanel editPanel = getEditorPanel();
        if (editPanel instanceof PropertyListEditorPanel) {
            retVal = ((PropertyListEditorPanel) editPanel).getEditorPanel();
        }
        return retVal;
    }

    private PropertyCollectionController getObservedController() {
        PropertyController controller = getPropertyController();
        PropertyCollectionController collectionController = null;
        Object value = null;
        if (controller == null) {
            return null;
        }
        value = controller.getPropertyValue();
        if (!(value instanceof PropertyCollection)) {
            return null;
        }
        collectionController = (PropertyCollectionController) controller.getParent().getParent();
        PropertyCollectionController retVal = collectionController.getModelController((PropertyCollection) value);
        return retVal;
    }

    protected void updateDisplayComponent(PropertyEditorPanel oldComponent, PropertyEditorPanel newComponent) {
        if (newComponent != null) {
            PropertyListEditorPanel newListEditor = (PropertyListEditorPanel) newComponent;
            InstanceEditorPanel editorPanel = newListEditor.getEditorPanel();
            editorPanel.setController(_observedController);
            editorPanel.setTypeIconMap(getTypeIconMap());
        }
    }

    public PropertyEditorPanel createEditionComponent() {
        return new DefaultPropertyListEditorPanel(true, _factory);
    }

    /**
     * (non-Javadoc)
     * 
     * @see deduced.viewer.editor.PropertyEditor#setTypeIconMap(deduced.viewer.TypeIconMap)
     */
    public void setTypeIconMap(TypeIconMap typeIconMap) {
        super.setTypeIconMap(typeIconMap);
        InstanceEditorPanel listEditorPanel = getListEditorPanel();
        if (listEditorPanel != null) {
            listEditorPanel.setTypeIconMap(getTypeIconMap());
        }
    }
}
