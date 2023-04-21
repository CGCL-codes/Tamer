package com.patientis.framework.controls.forms;

import java.awt.BorderLayout;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.JToolBar;
import com.patientis.model.common.ModelReference;
import com.patientis.framework.controls.ISScrollPane;
import com.patientis.framework.scripting.ISMediator;

/**
 * ToolBar extends JToolBar and implements IModelControlled to bind to the container model
 *
 * <br/>Design Patterns: <a href="/functionality/rm/1000055.html">Form Panel Hierarchy</a>
 * <br/>
 */
public class ISToolBar extends JToolBar implements IModelControlled {

    private static final long serialVersionUID = 1L;

    /**
	 * Weak reference to the container model
	 */
    private WeakReference<IContainer> containerRef = null;

    /**
	 * 
	 */
    public ISToolBar() {
    }

    /**
	 * @param orientation
	 */
    public ISToolBar(int orientation) {
        super(orientation);
    }

    /**
**
	 * @see com.patientis.framework.controls.forms.IModelControlled#setModel(com.patientis.framework.controls.forms.ContainerModel)
	 */
    public void setModel(IContainer containerModel) {
        if (containerModel == null) {
            throw new NullPointerException();
        }
        this.containerRef = new WeakReference<IContainer>(containerModel);
        initialize();
    }

    /**
	 * Bind container
	 */
    private void initialize() {
        this.setLayout(new BorderLayout());
        if (containerRef.get() != null) {
            containerRef.get().addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    if (String.valueOf(ModelReference.BASE).equals(evt.getPropertyName())) {
                        applyModel();
                    }
                }
            });
            applyModel();
        }
    }

    /**
	 * Apply bound properties
	 */
    public void applyModel() {
        if (containerRef.get() != null) {
            this.setPreferredSize(containerRef.get().getPreferredSize());
            if (containerRef.get().getColorBackground() != null) {
                this.setBackground(containerRef.get().getColorBackground());
            }
            this.setMinimumSize(containerRef.get().getMinimumSize());
            this.setSize(containerRef.get().getSize());
            this.setVisible(containerRef.get().isVisible());
        }
    }

    /**
	 * @see com.patientis.framework.controls.forms.IModelControlled#addContainer(java.awt.Container, com.patientis.framework.controls.forms.ContainerModel)
	 */
    public void addContainer(Container container, IContainer containerModel, ISMediator mediator) {
        if (containerModel.isScrollpane()) {
            this.add(new ISScrollPane(container), containerModel.getLayoutConstraint());
        } else {
            this.add(container, containerModel.getLayoutConstraint());
        }
    }
}
