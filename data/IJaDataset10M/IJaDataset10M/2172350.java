package q.zik.swing;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Knob.
 * 
 * @author Quentin
 */
public final class Knob extends JComponent {

    /**
   * 
   */
    private static final long serialVersionUID = 2297630754126034047L;

    private static final String CLASS_UI_ID = "KnobUI";

    private KnobModel model;

    private final ChangeEvent changeEvent = new ChangeEvent(this);

    private final EventListenerList listenerList = new EventListenerList();

    private final ChangeListener changeListener = new ChangeListener() {

        @Override
        public void stateChanged(final ChangeEvent pChangeevent) {
            repaint();
            fireChangeEvent();
        }
    };

    public Knob(final KnobModel pModel) {
        super();
        model = pModel;
        model.addChangeListener(changeListener);
        updateUI();
    }

    public int getValue() {
        return model.getValue();
    }

    public void setValue(final int val) {
        final double oldValue = model.getValue();
        if (oldValue == val) {
            return;
        }
        model.setValue(val);
        updateUI();
    }

    public void addChangeListener(final ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(final ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    public ChangeListener[] getChangeListeners() {
        return listenerList.getListeners(ChangeListener.class);
    }

    protected void fireChangeEvent() {
        final Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    public KnobUI getUI() {
        return (KnobUI) ui;
    }

    @Override
    public void updateUI() {
        if (UIManager.get(getUIClassID()) == null) {
            setUI(new KnobUI());
        } else {
            setUI(UIManager.getUI(this));
        }
    }

    @Override
    public String getUIClassID() {
        return CLASS_UI_ID;
    }

    public KnobModel getModel() {
        return model;
    }

    public void setModel(final KnobModel newModel) {
        final KnobModel oldModel = getModel();
        if (oldModel != null) {
            oldModel.removeChangeListener(changeListener);
        }
        model = newModel;
        if (newModel != null) {
            newModel.addChangeListener(changeListener);
        }
        firePropertyChange("model", oldModel, model);
    }

    /**
     * True if the slider knob is being dragged.
     * 
     * @return the value of the models valueIsAdjusting property
     * @see #setValueIsAdjusting
     */
    public boolean isValueAdjusting() {
        return getModel().isValueAdjusting();
    }

    /**
     * Sets the models valueIsAdjusting property. Slider look and feel implementations should set this property to true
     * when a knob drag begins, and to false when the drag ends. The slider model will not generate ChangeEvents while
     * valueIsAdjusting is true.
     * 
     * @see #getValueIsAdjusting
     * @beaninfo expert: true description: True if the slider knob is being dragged.
     */
    public void setValueIsAdjusting(final boolean isAdjusting) {
        model.setValueIsAdjusting(isAdjusting);
    }
}
