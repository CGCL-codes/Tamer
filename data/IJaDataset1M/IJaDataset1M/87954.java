package com.sh.architecture.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultTreeModel;
import com.sh.architecture.pattern.IVisitor;
import com.sh.architecture.util.EventListenerSupport;

/**
 * Abstract tree model
 * 
 * @author sursini
 */
public class AbstractTreeModel<NodeEntity extends NodeModel<?>> extends DefaultTreeModel implements IModel<NodeEntity> {

    private static final long serialVersionUID = 1L;

    private EventListenerSupport<ChangeListener, ChangeEvent> changeSupport;

    protected PropertyChangeSupport propertyChangeSupport;

    public AbstractTreeModel() {
        super(null);
        System.out.println("Create Tree model...");
        propertyChangeSupport = new PropertyChangeSupport(this);
        changeSupport = new EventListenerSupport<ChangeListener, ChangeEvent>() {

            @Override
            protected void fireEvent(ChangeListener listener, ChangeEvent event) {
                System.out.println("Fire event :" + event);
            }
        };
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void addChangeListener(ChangeListener changeListener) {
        changeSupport.addListener(changeListener);
    }

    @Override
    public NodeEntity getEntity() {
        return null;
    }

    @Override
    public void setEntity(NodeEntity entity) {
    }

    public void setRoot(NodeEntity root) {
        super.setRoot(root);
        attachListeners(root);
    }

    private void attachListeners(NodeEntity root) {
        ListenerAttacher listenerAttacher = new ListenerAttacher(this);
        root.accept(listenerAttacher);
    }

    class ListenerAttacher implements IVisitor {

        private final AbstractTreeModel<?> treeModel;

        public ListenerAttacher(AbstractTreeModel<?> treeModel) {
            super();
            this.treeModel = treeModel;
        }

        @Override
        public void visit(Object object) {
            if (object instanceof NodeModel) {
                for (PropertyChangeListener listener : treeModel.propertyChangeSupport.getPropertyChangeListeners()) {
                    ((NodeEntity) object).addPropertyChangeListener(listener);
                }
                for (ChangeListener listener : treeModel.changeSupport.getChangeListeners()) {
                    ((NodeEntity) object).addChangeListener(listener);
                }
            }
        }
    }
}
