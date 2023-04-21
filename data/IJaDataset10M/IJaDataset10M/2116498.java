package com.patientis.framework.controls;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JPanel;
import com.patientis.business.common.ICustomController;
import com.patientis.framework.controls.forms.IContainer;
import com.patientis.framework.controls.forms.IModelControlled;
import com.patientis.framework.controls.listeners.ISActionListener;
import com.patientis.framework.controls.menus.ISMenuBuilder;
import com.patientis.framework.scripting.IMediateMessages;
import com.patientis.framework.scripting.ISMediator;
import com.patientis.model.common.BaseModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.common.ModelReference;

/**
 * Panel extends JPanel and implements IModelControlled to bind to the container model
 *
 * Design Patterns: <a href="/functionality/rm/1000072.html">Control Panel</a>
 * <a href="/functionality/rm/1000055.html">Form Panel Hierarchy</a>
 * <br/>
 */
public class ISPanel extends JPanel implements IModelControlled, IComponent {

    private static final long serialVersionUID = 1L;

    /**
	 * Weak reference to the container model
	 */
    private WeakReference<IContainer> containerRef = null;

    /**
	 * 
	 */
    private String instructionText = null;

    /**
	 * 
	 */
    private BufferedImage backgroundImage = null;

    /**
	 * Default constructor
	 */
    public ISPanel() {
        super(new BorderLayout());
        init();
    }

    /**
	 * @param layout
	 */
    public ISPanel(LayoutManager layout) {
        super(layout);
        init();
    }

    /**
	 * @param isDoubleBuffered
	 */
    public ISPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        init();
    }

    /**
	 * @param layout
	 * @param isDoubleBuffered
	 */
    public ISPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        init();
    }

    /**
	 * Memory management
	 */
    private void init() {
        ISMemory.getInstance().add(this);
    }

    /**
**
	 * @see com.patientis.framework.controls.forms.IModelControlled#setModel(com.patientis.framework.controls.forms.IContainer)
	 */
    public void setModel(IContainer containerModel) throws Exception {
        if (containerModel == null) {
            throw new NullPointerException();
        }
        this.containerRef = new WeakReference<IContainer>(containerModel);
        initialize();
    }

    /**
	 * Bind container
	 * 
	 * @throws Exception
	 */
    private void initialize() throws Exception {
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
	 * Apply all bound model properties
	 */
    private void applyModel() {
        if (containerRef.get() != null) {
            if (containerRef.get().getColorBackground() != null) {
                this.setBackground(containerRef.get().getColorBackground());
            }
            this.setPreferredSize(containerRef.get().getPreferredSize());
            this.setMinimumSize(containerRef.get().getMinimumSize());
            this.setSize(containerRef.get().getSize());
            this.setVisible(containerRef.get().isVisible());
        }
    }

    /**
**
	 * @see com.patientis.framework.controls.forms.IModelControlled#addContainer(java.awt.Container, com.patientis.framework.controls.forms.IContainer)
	 */
    public void addContainer(Container container, IContainer icontainer, ISMediator mediator) {
        if (icontainer.isScrollpane()) {
            this.add(new ISScrollPane(container, true), icontainer.getLayoutConstraint());
        } else {
            this.add(container, icontainer.getLayoutConstraint());
        }
    }

    /**
	 * @see java.lang.Object#finalize()
	 */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        ISMemory.getInstance().remove(this);
    }

    /**
	 * Override painting
	 * 
	 */
    private IComponentPainter componentPainter = null;

    /**
	 * @param componentPainter the componentPainter to set
	 */
    public void setComponentPainter(IComponentPainter componentPainter) {
        this.componentPainter = componentPainter;
    }

    /**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
    @Override
    protected void paintComponent(Graphics g) {
        if (componentPainter != null) {
            componentPainter.prePaintComponent(g, this);
        }
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 1, 1, null);
        }
        if (componentPainter != null) {
            componentPainter.postPaintComponent(g, this);
        }
    }

    /**
	 * Add child containers to a dialog
	 * 
	 * @param childContainers
	 * @throws Exception
	 */
    public void addContainers(List<IContainer> childContainers, ISMediator mediator, BaseModel model) throws Exception {
        for (IContainer cm : childContainers) {
            if (cm.isScrollpane()) {
                add(new ISScrollPane(cm.getInstance(mediator, model)), cm.getLayoutConstraint());
            } else {
                add(cm.getInstance(mediator, model), cm.getLayoutConstraint());
            }
        }
    }

    /**
	 * @return the backgroundImage
	 */
    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    /**
	 * @param backgroundImage the backgroundImage to set
	 */
    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#addActionListener(java.awt.event.ActionListener)
	 */
    @Override
    public void addActionListener(ActionListener l) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#addNotifyModelListener(com.patientis.framework.controls.listeners.ISActionListener)
	 */
    @Override
    public void addNotifyModelListener(ISActionListener listener) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#getValue()
	 */
    @Override
    public Object getValue() throws Exception {
        return null;
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#isEditable()
	 */
    @Override
    public boolean isEditable() {
        return false;
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#isIconEnabled()
	 */
    @Override
    public boolean isIconEnabled() {
        return false;
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#release()
	 */
    @Override
    public void release() {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setControlProperties(com.patientis.framework.controls.IControl, com.patientis.model.common.IBaseModel, com.patientis.model.common.IBaseModel, com.patientis.framework.scripting.IMediateMessages)
	 */
    @Override
    public void setControlProperties(IControl control, IBaseModel mainModel, IBaseModel controlModel, IMediateMessages mediator) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setDisplayZero(boolean)
	 */
    @Override
    public void setDisplayZero(boolean displayZero) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setEditable(boolean)
	 */
    @Override
    public void setEditable(boolean editable) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setFormat(java.lang.String)
	 */
    @Override
    public void setFormat(String format) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setIcon(javax.swing.Icon)
	 */
    @Override
    public void setIcon(Icon icon) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setMediator(com.patientis.framework.scripting.IMediateMessages, com.patientis.framework.controls.IControlInteraction)
	 */
    @Override
    public void setMediator(IMediateMessages mediator, IControlInteraction control) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setValue(java.lang.Object)
	 */
    @Override
    public void setValue(Object value) throws Exception {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setValueAction(com.patientis.framework.scripting.IMediateMessages, int, int)
	 */
    @Override
    public void setValueAction(IMediateMessages mediator, int context, int defaultActionRefId) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#updateModelListener()
	 */
    @Override
    public void updateModelListener() throws Exception {
    }

    @Override
    public void savingForm() throws Exception {
    }

    @Override
    public ICustomController getCustomController() {
        return null;
    }

    @Override
    public void setCustomController(ICustomController controller) {
    }
}
