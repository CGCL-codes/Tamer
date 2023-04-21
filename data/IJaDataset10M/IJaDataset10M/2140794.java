package com.patientis.framework.controls;

import javax.swing.JRadioButton;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.ref.WeakReference;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.patientis.business.common.ICustomController;
import com.patientis.client.action.BaseAction;
import com.patientis.framework.controls.exceptions.ISInvalidBindException;
import com.patientis.framework.locale.FormatUtil;
import com.patientis.framework.logging.Log;
import com.patientis.framework.scripting.IMediateMessages;
import com.patientis.framework.scripting.IReceiveMessage;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.reference.ActionReference;
import com.patientis.model.security.ApplicationControlColumnModel;
import com.patientis.framework.controls.listeners.ISActionListener;
import com.patientis.framework.controls.table.IEditTableCell;
import com.patientis.framework.controls.table.IRenderTableCell;
import com.patientis.framework.controls.table.ISTableCellEditor;

/**
 * One line class description
 *
 * 
 *   
 */
public class ISRadioButton extends JRadioButton implements IComponent, IEditTableCell {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * stateChanged fires with mouse over so this reduces to single call on selection
	 */
    private boolean isSelected = false;

    private Object value = null;

    /**
	 * Self reference
	 */
    private ISRadioButton self = this;

    /**
	 * 
	 */
    public ISRadioButton() {
        super();
        initialize();
    }

    /**
	 * @param icon
	 */
    public ISRadioButton(Icon icon) {
        super(icon);
        initialize();
    }

    /**
	 * @param text
	 * @param icon
	 */
    public ISRadioButton(String text, Icon icon) {
        super(text, icon);
        initialize();
    }

    /**
	 * @param text
	 */
    public ISRadioButton(String text) {
        super(text);
        initialize();
    }

    /**
	 * Constructor initialization
	 */
    private void initialize() {
        ISMemory.getInstance().add(this);
        if (FormatUtil.hasDefaultFont()) setFont(FormatUtil.getDefaultFont());
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
**
	 * @see com.patientis.framework.controls.IComponent#addNotifyModelListener(java.awt.event.ActionListener)
	 */
    public void addNotifyModelListener(final ISActionListener listener) {
        modelListener = new WeakReference<ISActionListener>(listener);
        this.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (isSelected() != isSelected) {
                    ActionEvent a = new ActionEvent(e.getSource(), 0, null);
                    listener.actionPerformed(a);
                }
                isSelected = isSelected();
            }
        });
    }

    /**
**
	 * @see com.patientis.framework.controls.IComponent#getValue()
	 */
    public Object getValue() throws Exception {
        return value;
    }

    /**
**
	 * @see com.patientis.framework.controls.IComponent#setEditable(boolean)
	 */
    public void setEditable(boolean editable) {
        super.setEnabled(editable);
    }

    /**
**
	 * @see com.patientis.framework.controls.IComponent#setValue(java.lang.Object)
	 */
    public void setValue(Object value) throws Exception {
        this.value = value;
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setMediator(com.patientis.framework.scripting.IMediateMessages, int, int)
	 */
    public void setMediator(final IMediateMessages mediator, final IControlInteraction control) {
        addDefaultAction(mediator, control.getContextRefId(), control.getDefaultActionRefId(), control.getActionViewRefId());
        if (control.getSelectActionRefId() > 0) {
            Log.warn(new ISInvalidBindException(String.valueOf(control.getSelectActionRefId())));
        }
        setValueAction(mediator, control.getContextRefId(), control.getDefaultActionRefId());
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setValueAction(com.patientis.framework.scripting.IMediateMessages, int, int)
	 */
    public void setValueAction(final IMediateMessages mediator, final int context, final int defaultActionRefId) {
        mediator.register(new IReceiveMessage() {

            public boolean receive(ISEvent event, Object value) throws Exception {
                switch(event) {
                    case SETVALUEACTION:
                        BaseAction action = (BaseAction) value;
                        if (context > 0 && action.getContextRefId() == context) {
                            setValue(action.getValue());
                            return true;
                        } else if (defaultActionRefId > 0 && action.getActionReference() != null && action.getActionReference().getRefId() == defaultActionRefId) {
                            setValue(action.getValue());
                            return true;
                        }
                }
                return false;
            }
        }, this);
    }

    /**
	 * Add action on text field enter
	 * 
	 * @param mediator
	 * @param defaultAction
	 */
    private void addDefaultAction(final IMediateMessages mediator, final int context, final int defaultAction, final long actionViewRefId) {
        if (defaultAction > 0) {
            this.addActionListener(new ISActionListener() {

                public void actionExecuted(ActionEvent e) throws Exception {
                    try {
                        BaseAction action = new BaseAction();
                        action.setSource(self);
                        action.setContextRefId(context);
                        action.setActionReference(ActionReference.get(new Long(defaultAction)));
                        action.setActionViewRefId(actionViewRefId);
                        action.setActionEvent(e);
                        action.setValue(getValue());
                        ISTextController.applyChangesToModels();
                        mediator.receive(ISEvent.EXECUTEACTION, action);
                    } catch (Exception ex) {
                        Log.exception(ex);
                    }
                }
            });
        }
    }

    /**
	 * @see com.patientis.framework.controls.table.IEditTableCell#cancelCellEditing()
	 */
    public void cancelCellEditing() {
    }

    /**
	 * @see com.patientis.framework.controls.table.IEditTableCell#prepareForKeystroke(java.awt.event.KeyEvent)
	 */
    public void prepareForKeystroke(KeyEvent e) {
    }

    /**
	 * @see com.patientis.framework.controls.table.IEditTableCell#prepareToBeCellEditor()
	 */
    public void prepareToBeCellEditor() {
        setBorder(BorderFactory.createEmptyBorder());
    }

    /**
	 * @see com.patientis.framework.controls.table.IEditTableCell#prepareToEdit()
	 */
    public void prepareToEdit() {
    }

    /**
	 * @see com.patientis.framework.controls.table.IRenderTableCell#prepareToBeCellRenderer()
	 */
    public void prepareToBeCellRenderer() {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setFormat(java.lang.String)
	 */
    public void setFormat(String format) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#isIconEnabled()
	 */
    public boolean isIconEnabled() {
        return true;
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#release()
	 */
    public void release() {
    }

    public void setDisplayZero(boolean displayZero) {
    }

    /**
	 * Override painting
	 * 
	 */
    private IComponentPainter componentPainter = null;

    /**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
    @Override
    protected void paintComponent(Graphics g) {
        if (componentPainter != null) {
            componentPainter.prePaintComponent(g, this);
        }
        super.paintComponent(g);
        if (componentPainter != null) {
            componentPainter.postPaintComponent(g, this);
        }
    }

    /**
	 * @param componentPainter the componentPainter to set
	 */
    public void setComponentPainter(IComponentPainter componentPainter) {
        this.componentPainter = componentPainter;
    }

    @Override
    public void prepareToBeCellEditor(ISTableCellEditor cellEditor) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setControlProperties(com.patientis.framework.controls.IControl)
	 */
    @Override
    public void setControlProperties(IControl control, IBaseModel mainModel, IBaseModel controlModel, IMediateMessages mediator) {
    }

    /**
	 * Weak reference to the model listener
	 */
    protected WeakReference<ISActionListener> modelListener = null;

    /**
	 * 
	 * @throws Exception
	 */
    public void updateModelListener() throws Exception {
        if (modelListener != null && modelListener.get() != null) {
            ActionEvent actionEvent = new ActionEvent(this, 0, null);
            modelListener.get().actionExecuted(actionEvent);
        }
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    /**
	 * 
	 * @param column
	 */
    public void initialize(ApplicationControlColumnModel column) {
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
