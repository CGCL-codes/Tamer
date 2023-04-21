package com.patientis.framework.controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.CellRendererPane;
import javax.swing.FocusManager;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.MaskFormatter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.ComboBoxEditor;
import com.patientis.business.common.ICustomController;
import com.patientis.client.action.BaseAction;
import com.patientis.client.service.common.BaseService;
import com.patientis.framework.api.services.ReferenceServer;
import com.patientis.framework.controls.custom.ISUnits;
import com.patientis.framework.controls.exceptions.ISInvalidBindException;
import com.patientis.framework.locale.FormatUtil;
import com.patientis.framework.locale.KeyMapUtil;
import com.patientis.framework.logging.Log;
import com.patientis.framework.scripting.IMediateMessages;
import com.patientis.framework.scripting.IReceiveMessage;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.framework.scripting.ServiceUtility;
import com.patientis.framework.utility.SwingUtil;
import com.patientis.model.reference.ActionReference;
import com.patientis.model.reference.RefModel;
import com.patientis.model.security.ApplicationControlColumnModel;
import com.patientis.model.security.ApplicationControlModel;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.clinical.FormRecordModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.common.DisplayModel;
import com.patientis.model.common.IBaseModel;
import com.patientis.framework.controls.listeners.ISActionListener;
import com.patientis.framework.controls.table.IEditTableCell;
import com.patientis.framework.controls.table.IRenderTableCell;
import com.patientis.framework.controls.table.ISTableCellEditor;

/**
 * ISTextField encapsulates the JTextField for forms
 *
 * Design Patterns: <a href="/functionality/rm/1000072.html">Control Panel</a>
 * <br/>
 */
public class ISTextField extends JTextField implements IComponent, IEditTableCell, ComboBoxEditor, IRenderTableCell {

    private static final long serialVersionUID = 1L;

    /**
	 * Reference to this
	 */
    private ISTextField self = this;

    /**
	 * Flags if this text field is being used as a table cell editor
	 */
    private boolean isCellEditor = false;

    /**
	 * Weak reference to the model listener
	 */
    protected WeakReference<ISActionListener> modelListener = null;

    /**
	 * Default text in box
	 */
    private String instructionText = null;

    /**
	 * Keeps track of value
	 */
    private DisplayModel displayValue = null;

    /**
	 * Display zero or an empty string if false
	 */
    private boolean displayZero = false;

    /**
	 * Date format
	 */
    private SimpleDateFormat dateFormat = null;

    /**
	 * Date format
	 */
    private MaskFormatter numberFormat = null;

    /**
	 * 
	 */
    private String format = null;

    /**
	 * 
	 */
    private ISTextSearchDocument searchTextDocument = null;

    /**
	 * 
	 */
    private boolean customRender = false;

    /**
	 * 
	 */
    protected ApplicationControlModel acm = null;

    /**
	 * 
	 */
    protected ICustomController controller = null;

    /**
	 * 
	 */
    protected IMediateMessages mediator = null;

    /**
	 * 
	 */
    private boolean isNumber = false;

    /**
	 * 
	 */
    protected ISUnits units = new ISUnits(null);

    protected IBaseModel mainModel = null;

    /**
	 * 
	 */
    private ColorState colorState = new ColorState();

    /**
	 * Default constructor
	 */
    public ISTextField() {
        super(12);
        customize();
    }

    /**
	 * Default constructor
	 * 
	 * @param text
	 */
    public ISTextField(String text) {
        super(text);
        customize();
    }

    /**
	 * Default constructor
	 * 
	 * @param columns
	 */
    public ISTextField(int columns) {
        super(columns);
        customize();
    }

    /**
	 * Default constructor
	 * 
	 * @param text
	 * @param columns
	 */
    public ISTextField(String text, int columns) {
        super(text, columns);
        customize();
    }

    /**
	 * Default constructor
	 * 
	 * @param doc
	 * @param text
	 * @param columns
	 */
    public ISTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        customize();
    }

    /**
	 * Automatically select all the text in the control on focus
	 *
	 */
    public void customize() {
        if (FormatUtil.hasDefaultFont()) setFont(FormatUtil.getDefaultFont());
        ISMemory.getInstance().add(this);
        this.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                try {
                    if (!isCellEditor) {
                        colorState.saveState(ISTextField.this);
                        selectAll();
                        try {
                            scrollRectToVisible(SwingUtil.getDefaultScrollRectangle());
                        } catch (Exception ex) {
                            Log.exception(ex);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            public void focusLost(FocusEvent e) {
                colorState.restoreState(ISTextField.this);
            }
        });
    }

    public void setValue(final Object value) throws Exception {
        if (javax.swing.SwingUtilities.isEventDispatchThread()) {
            setValueEDT(value);
        } else {
            new Exception().printStackTrace();
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    try {
                        setValueEDT(value);
                        Log.warn("EDT Thread");
                    } catch (Exception ex) {
                        Log.exception(ex);
                    }
                }
            });
        }
    }

    /**
	 **
	 * @see com.patientis.framework.controls.IComponent#setValue(java.lang.Object)
	 */
    public void setValueEDT(Object value) throws Exception {
        isNumber = false;
        if (value instanceof DisplayModel) {
            this.displayValue = new DisplayModel();
            this.displayValue.copyAllFrom((DisplayModel) value);
        } else {
            this.displayValue = null;
        }
        if (dateFormat != null && value != null) {
            try {
                setText(dateFormat.format(com.patientis.framework.locale.CalendarUtility.parse(value.toString()).getTime()));
            } catch (Exception ex) {
                setText(Converter.convertDisplayString(value));
            }
        } else if (numberFormat != null && value != null && Converter.isNumber(Converter.convertDisplayString(value))) {
            try {
                setText(numberFormat.valueToString(Converter.convertDisplayString(value)));
                isNumber = true;
            } catch (Exception ex) {
                setText(Converter.convertDisplayString(value));
            }
        } else {
            String newText = Converter.convertString(value);
            int newPosition = 0;
            if (newText != null && getCaretPosition() > 0 && getCaretPosition() < newText.length()) {
                newPosition = getCaretPosition();
            }
            setText(newText);
            setCaretPosition(newPosition);
            getCaret().setDot(0);
        }
    }

    /**
**
	 * @see com.patientis.framework.controls.IComponent#getValue()
	 */
    public Object getValue() throws Exception {
        String s = getText();
        if (s == null) {
            return null;
        } else if (s.trim().equals("")) {
            return null;
        } else {
            if (this.displayValue != null) {
                this.displayValue.setDisplay(s.trim());
                return this.displayValue;
            } else if (this.dateFormat != null) {
                try {
                    return com.patientis.framework.locale.CalendarUtility.parse(s).getTime();
                } catch (Exception ex) {
                    return getText();
                }
            } else if (this.numberFormat != null && this.format != null) {
                if (Converter.isNumber(s)) {
                    try {
                        String textValue = s;
                        for (int i = 0; i < this.format.length(); i++) {
                            String f = this.format.substring(i, i + 1);
                            textValue = textValue.replace(f, "");
                        }
                        setText(numberFormat.valueToString(Converter.convertDisplayString(Converter.convertDouble(s))));
                        return textValue;
                    } catch (Exception ex) {
                        return getText();
                    }
                } else {
                    return getText();
                }
            } else {
                return s.trim();
            }
        }
    }

    /**
**
	 * @see com.patientis.framework.controls.IComponent#addNotifyModelListener(java.awt.event.ActionListener)
	 */
    public void addNotifyModelListener(final ISActionListener listener) {
        modelListener = new WeakReference<ISActionListener>(listener);
        this.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
                ActionEvent a = new ActionEvent(e.getSource(), e.getID(), null);
                listener.actionPerformed(a);
            }
        });
        this.addActionListener(listener);
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setMediator(com.patientis.framework.scripting.IMediateMessages, int, int)
	 */
    public void setMediator(final IMediateMessages mediator, final IControlInteraction control) {
        this.mediator = mediator;
        addDefaultAction(mediator, control.getContextRefId(), control.getDefaultActionRefId(), control.getActionViewRefId());
        if (control.getSelectActionRefId() > 0) {
            Log.warn(new ISInvalidBindException(String.valueOf(control.getSelectActionRefId())));
        }
        addInstructionText(control);
        setFont(control);
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    if (e.isPopupTrigger() || javax.swing.SwingUtilities.isRightMouseButton(e)) {
                        ServiceUtility.editApplicationControl(e.getComponent(), control.getApplicationControlId());
                    }
                } catch (Exception ex) {
                    Log.exception(ex);
                }
            }
        });
    }

    /**
	 * 
	 * @param control
	 */
    private void setFont(IControlInteraction control) {
        if (control.getFontSize() > 0) {
            Font font = new Font(getFont().getFontName(), getFont().getStyle(), control.getFontSize());
            setFont(font);
        }
        if (control.getFontColor() != 0) {
            setForeground(new Color(control.getFontColor()));
        }
    }

    /**
	 * Add instruction text to this control
	 * 
	 * @param control
	 */
    private void addInstructionText(final IControlInteraction control) {
        if (control.getInstructionText() != null && Converter.isEmpty(getText())) {
            instructionText = control.getInstructionText();
            this.getDocument().addDocumentListener(new DocumentListener() {

                public void changedUpdate(DocumentEvent e) {
                    if (instructionText != null) clearInstructionText();
                }

                public void insertUpdate(DocumentEvent e) {
                    if (instructionText != null) clearInstructionText();
                }

                public void removeUpdate(DocumentEvent e) {
                    if (instructionText != null) clearInstructionText();
                }
            });
            com.patientis.framework.concurrency.SwingWorker sw = new com.patientis.framework.concurrency.SwingWorker(null) {

                @Override
                protected void doNonUILogic() throws Exception {
                    Thread.sleep(5000);
                    clearInstructionText();
                }

                @Override
                protected void doUIUpdateLogic() throws Exception {
                }
            };
            sw.start();
        }
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
                        if (modelListener != null && modelListener.get() != null) {
                            ActionEvent a = new ActionEvent(e.getSource(), e.getID(), null);
                            modelListener.get().actionPerformed(a);
                        }
                        BaseAction action = new BaseAction();
                        action.setSource(self);
                        action.setContextRefId(context);
                        action.setActionReference(ActionReference.get(new Long(defaultAction)));
                        action.setActionViewRefId(actionViewRefId);
                        action.setActionEvent(e);
                        action.setValue(getText());
                        if (getValue() instanceof DisplayModel) {
                            action.setBaseModel(new RefModel((DisplayModel) getValue()));
                        }
                        mediator.receive(ISEvent.EXECUTEACTION, action);
                    } catch (Exception ex) {
                        Log.exception(ex);
                    }
                }
            });
        }
        setValueAction(mediator, context, defaultAction);
    }

    /**
	 * 
	 */
    public void executeDefaultAction() {
        ActionListener[] listeners = getActionListeners();
        for (ActionListener l : listeners) {
            try {
                ActionEvent event = new ActionEvent(this, 0, null);
                l.actionPerformed(event);
            } catch (Exception ex) {
                Log.exception(ex);
            }
        }
    }

    /**
	 * No action to cancel
	 */
    public void cancelCellEditing() {
    }

    /**
	 * @see com.patientis.framework.controls.table.IEditTableCell#prepareToBeCellEditor()
	 */
    public void prepareToBeCellEditor(final ISTableCellEditor cellEditor) {
        this.isCellEditor = true;
        ISTextController.stopMonitor(this);
        setBorder(BorderFactory.createEmptyBorder());
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        getInputMap().put(enterKeyStroke, "ENTER");
        ActionMap am = getActionMap();
        ActionMap parentMap = am.getParent();
        Action action = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cellEditor.enterPressed();
            }
        };
        am.put("ENTER", action);
    }

    /**
	 * @see javax.swing.ComboBoxEditor#getEditorComponent()
	 */
    public Component getEditorComponent() {
        return this;
    }

    /**
	 * @see javax.swing.ComboBoxEditor#getItem()
	 */
    public Object getItem() {
        try {
            return getValue();
        } catch (Exception ex) {
            Log.exception(ex);
            return null;
        }
    }

    /**
	 * @see javax.swing.ComboBoxEditor#setItem(java.lang.Object)
	 */
    public void setItem(Object value) {
        try {
            setValue(value);
        } catch (Exception ex) {
            Log.exception(ex);
        }
    }

    /**
	 * No action
	 * 
	 * @see com.patientis.framework.controls.table.IEditTableCell#prepareForKeystroke(java.awt.event.KeyEvent)
	 */
    public void prepareForKeystroke(KeyEvent e) {
    }

    /**
	 * Select all the text
	 * 
	 * @see com.patientis.framework.controls.table.IEditTableCell#prepareToEdit()
	 */
    public void prepareToEdit() {
        this.selectAll();
    }

    /**
	 * @see com.patientis.framework.controls.table.IRenderTableCell#prepareToBeCellRenderer()
	 */
    public void prepareToBeCellRenderer() {
    }

    /**
	 * @see java.lang.Object#finalize()
	 */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        ISTextController.stopMonitor(this);
        ISMemory.getInstance().remove(this);
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setFormat(java.lang.String)
	 */
    public void setFormat(String format) {
        if (Converter.isNotEmpty(format)) {
            this.format = format;
            if (FormatUtil.isDateFormat(format)) {
                dateFormat = new SimpleDateFormat(format);
            } else if (FormatUtil.isNumberFormat(format)) {
                try {
                    numberFormat = new MaskFormatter(format);
                    numberFormat.setValidCharacters("0123456789");
                    numberFormat.setAllowsInvalid(true);
                    numberFormat.setOverwriteMode(true);
                    numberFormat.setValueContainsLiteralCharacters(false);
                } catch (Exception ex) {
                    Log.exception(ex);
                }
            }
        }
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#isIconEnabled()
	 */
    public boolean isIconEnabled() {
        return false;
    }

    /**
	 * No effect
	 * 
	 * @see com.patientis.framework.controls.IComponent#setIcon(javax.swing.Icon)
	 */
    public void setIcon(Icon icon) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#release()
	 */
    public void release() {
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
        String text = getText();
        if (instructionText != null && Converter.isNotEmpty(text)) {
            instructionText = null;
        }
        if (instructionText != null) {
            g.setColor(java.awt.Color.LIGHT_GRAY);
            g.drawString(instructionText, 5, 12);
        }
        if (componentPainter != null) {
            componentPainter.postPaintComponent(g, this);
        }
    }

    /**
	 * clear instruction text from the control
	 */
    private void clearInstructionText() {
        if (instructionText != null) {
            instructionText = null;
            repaint();
        }
    }

    /**
	 * @return the displayZero
	 */
    public boolean isDisplayZero() {
        return displayZero;
    }

    /**
	 * @param displayZero the displayZero to set
	 */
    public void setDisplayZero(boolean displayZero) {
        this.displayZero = displayZero;
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
	 * @param search the search to set
	 */
    public ISTextDynamicThread setSearch(ITextSearch search) throws Exception {
        return setSearch(search, null);
    }

    /**
	 * @param search the search to set
	 */
    public ISTextDynamicThread setSearch(ITextSearch search, ISComboBox combo) throws Exception {
        if (search != null) {
            searchTextDocument = new ISTextSearchDocument();
            setDocument(searchTextDocument);
            searchTextDocument.setField(this);
            searchTextDocument.setSearch(search);
            ISTextDynamicThread thread = ISTextDynamicThread.getInstance(searchTextDocument, combo);
            return thread;
        } else {
            return null;
        }
    }

    @Override
    public void prepareToBeCellEditor() {
    }

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

    /**
	 * @see com.patientis.framework.controls.IComponent#setControlProperties(com.patientis.framework.controls.IControl)
	 */
    @Override
    public void setControlProperties(IControl control, IBaseModel mainModel, IBaseModel controlModel, IMediateMessages mediator) {
        acm = (ApplicationControlModel) control;
        this.mainModel = mainModel;
        if (acm.isTextControllerMonitor()) {
            ISTextController.monitor(this);
        }
        Object previousValue = null;
        try {
            previousValue = getValue();
            units = new ISUnits(acm);
            if (previousValue != null && units.hasDatabaseConversion()) {
                setValue(previousValue);
            }
        } catch (Exception ex) {
            Log.exception(ex);
        }
        if (acm.getTextFieldSizeColumns() > 0) {
            setColumns(acm.getTextFieldSizeColumns());
        }
    }

    /**
	 * 
	 * @param column
	 */
    public void initialize(ApplicationControlColumnModel column) {
    }

    /**
	 * @see com.patientis.framework.controls.table.IRenderTableCell#getComponentDisplay(java.lang.Object, boolean, boolean, javax.swing.JComponent)
	 */
    @Override
    public Component getComponentDisplay(JTable table, Object value, boolean isSelected, boolean hasFocus, JComponent defaultRenderDisplay, int row, int col) {
        return null;
    }

    /**
	 * @see com.patientis.framework.controls.table.IRenderTableCell#isCustomRender()
	 */
    @Override
    public boolean isCustomRender() {
        return customRender;
    }

    @Override
    public void savingForm() throws Exception {
    }

    /**
	 * 
	 * @return
	 */
    public FormRecordModel getFormRecordModel() throws Exception {
        if (mainModel instanceof FormModel) {
            if (acm.getRecordItemRef().isNotNew()) {
                Object baseModel = acm.getBaseModel((FormModel) mainModel, ReferenceServer.createServiceCall());
                if (baseModel instanceof FormRecordModel) {
                    FormRecordModel record = (FormRecordModel) baseModel;
                    return record;
                }
            }
        }
        return null;
    }

    /**
	 * 
	 * @return
	 * @throws Exception
	 */
    public boolean hasFormRecordModel() throws Exception {
        return getFormRecordModel() != null;
    }

    /**
     * Forwards the <code>scrollRectToVisible()</code> message to the
     * <code>JComponent</code>'s parent. Components that can service
     * the request, such as <code>JViewport</code>,
     * override this method and perform the scrolling.
     *
     * @param aRect the visible <code>Rectangle</code>
     * @see JViewport
     */
    public void scrollRectToVisible(Rectangle aRect) {
        Container parent;
        int dx = getX(), dy = getY();
        for (parent = getParent(); !(parent == null) && !(parent instanceof JComponent) && !(parent instanceof CellRendererPane); parent = parent.getParent()) {
            Rectangle bounds = parent.getBounds();
            dx += bounds.x;
            dy += bounds.y;
        }
        if (!(parent == null) && !(parent instanceof CellRendererPane)) {
            aRect.x += dx;
            aRect.y += dy;
            ((JComponent) parent).scrollRectToVisible(aRect);
            aRect.x -= dx;
            aRect.y -= dy;
        }
    }

    @Override
    public ICustomController getCustomController() {
        return controller;
    }

    @Override
    public void setCustomController(ICustomController controller) {
        this.controller = controller;
    }
}
