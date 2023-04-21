package com.patientis.framework.controls.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLWriter;
import jsyntaxpane.DefaultSyntaxKit;
import com.patientis.business.common.ICustomController;
import com.patientis.client.action.BaseAction;
import com.patientis.client.service.clinical.ClinicalService;
import com.patientis.framework.controls.IComponent;
import com.patientis.framework.controls.IControl;
import com.patientis.framework.controls.IControlInteraction;
import com.patientis.framework.controls.ISTextController;
import com.patientis.framework.controls.exceptions.ISInvalidBindException;
import com.patientis.framework.controls.listeners.ISActionListener;
import com.patientis.framework.locale.FormatUtil;
import com.patientis.framework.logging.Log;
import com.patientis.framework.scripting.IMediateMessages;
import com.patientis.framework.scripting.IReceiveMessage;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.model.clinical.TermModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DisplayModel;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.security.ApplicationControlModel;

/**
 * @author gcaulton
 *
 */
public class ISHtmlTextPane extends JTextPane implements IComponent {

    private static final long serialVersionUID = 1L;

    /**
	 * Weak reference to the model listener
	 */
    protected WeakReference<ISActionListener> modelListener = null;

    /**
	 * 
	 */
    public ISHtmlTextPane() {
        init();
    }

    /**
	 * 
	 */
    public void init() {
        if (FormatUtil.hasDefaultFont()) setFont(FormatUtil.getDefaultFont());
        setEditorKit(new ISHtmlEditorKit());
    }

    /**
	 * Set the document text
	 */
    public void setValue(Object value) throws Exception {
        if (value == null) {
            setText("");
        } else {
            String newText = value.toString();
            int newPosition = 0;
            if (getCaretPosition() > 0 && getCaretPosition() < newText.length()) {
                newPosition = getCaretPosition();
            }
            setText(value.toString());
            try {
                setCaretPosition(newPosition);
            } catch (IllegalArgumentException arg) {
            }
        }
    }

    @Override
    public String getText() {
        try {
            final StringWriter sw = new StringWriter();
            HTMLWriter w = new HTMLWriter(sw, (HTMLDocument) getDocument());
            w.write();
            return convertTextToHtml(super.getText());
        } catch (Exception ex) {
            return super.getText();
        }
    }

    /**
	 * Return the document text or null if empty
	 * 
	 */
    public Object getValue() throws Exception {
        String s = getText();
        if (s == null) {
            return null;
        } else if (s.equals("")) {
            return null;
        } else {
            return s;
        }
    }

    /**
	 * The listener will be executed when focus leaves the text component
	 * 
	 * @param listener
	 */
    public void addNotifyModelListener(final ISActionListener listener) {
        modelListener = new WeakReference<ISActionListener>(listener);
        this.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                setCaretPosition(0);
            }

            public void focusLost(FocusEvent e) {
                ActionEvent a = new ActionEvent(e.getSource(), e.getID(), null);
                listener.actionPerformed(a);
            }
        });
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setMediator(com.patientis.framework.scripting.IMediateMessages, int, int)
	 */
    public void setMediator(final IMediateMessages mediator, final IControlInteraction control) {
        if (control.getDefaultActionRefId() > 0) {
            Log.warn(new ISInvalidBindException(String.valueOf(control.getDefaultActionRefId())));
        }
        if (control.getSelectActionRefId() > 0) {
            Log.warn(new ISInvalidBindException(String.valueOf(control.getSelectActionRefId())));
        }
        setValueAction(mediator, control.getContextRefId(), control.getDefaultActionRefId());
        setEditorActions(mediator, control.getContextRefId(), control.getDefaultActionRefId());
        setFont(control);
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
	 * @see com.patientis.framework.controls.IComponent#setFormat(java.lang.String)
	 */
    public void setFormat(String format) {
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#addActionListener(java.awt.event.ActionListener)
	 */
    public void addActionListener(ActionListener l) {
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
        ISTextController.stopMonitor(this);
    }

    /**
	 * 
	 */
    public void setDisplayZero(boolean displayZero) {
    }

    /**
	 * 
	 */
    public void setJavaFormat() {
        DefaultSyntaxKit.initKit();
        setContentType("text/java");
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
	 * @see com.patientis.framework.controls.IComponent#setControlProperties(com.patientis.framework.controls.IControl)
	 */
    @Override
    public void setControlProperties(IControl control, IBaseModel mainModel, IBaseModel controlModel, IMediateMessages mediator) {
        ApplicationControlModel acm = (ApplicationControlModel) control;
        if (acm.isTextControllerMonitor()) {
            ISTextController.monitor(this);
        }
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

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        ISTextController.stopMonitor(this);
    }

    /**
	 * This one can paste also from Word 2000 etc. 
	 * Strange tags and comments are removed.
	 */
    public void paste() {
        Clipboard clipboard = getToolkit().getSystemClipboard();
        final Transferable content = clipboard.getContents(this);
        Transferable newContent = new Transferable() {

            public DataFlavor[] getTransferDataFlavors() {
                DataFlavor[] flavors = content.getTransferDataFlavors();
                List<DataFlavor> myFlavorList = new ArrayList<DataFlavor>(flavors.length);
                for (int i = 0; i < flavors.length; i++) {
                    DataFlavor flavor = flavors[i];
                    String mimeType = flavor.getMimeType();
                    if (mimeType.indexOf("String") >= 0) {
                        myFlavorList.add(flavor);
                    }
                }
                DataFlavor[] myFlavors = new DataFlavor[myFlavorList.size()];
                for (int i = 0; i < myFlavorList.size(); i++) {
                    DataFlavor flavor = (DataFlavor) myFlavorList.get(i);
                    myFlavors[i] = flavor;
                }
                return myFlavors;
            }

            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return content.isDataFlavorSupported(flavor);
            }

            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                String mimeType = flavor.getMimeType();
                if (mimeType.indexOf("String") < 0 || mimeType.indexOf("html") < 0) {
                    return content.getTransferData(flavor);
                } else {
                    String data = (String) content.getTransferData(flavor);
                    data = data.substring(data.indexOf("<body"), data.indexOf("</body>") + 7).trim();
                    data = convertTextToHtml(data);
                    return data;
                }
            }
        };
        clipboard.setContents(newContent, null);
        super.paste();
    }

    /**
	 * 
	 * @param text
	 * @return
	 */
    public String convertTextToHtml(String text) {
        if (Converter.isNotEmpty(text)) {
            String s = text.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
            s = s.replaceAll("<p.*>", "");
            s = s.replaceAll("</p>", "");
            s = s.replaceAll("<body.*>", "");
            s = s.replaceAll("</body>", "");
            s = s.replaceAll("<!-.*>", "").trim();
            s = s.replace("\n", "");
            return s.trim();
        } else {
            return text;
        }
    }

    /**
	 * @see com.patientis.framework.controls.IComponent#setValueAction(com.patientis.framework.scripting.IMediateMessages, int, int)
	 */
    public void setEditorActions(final IMediateMessages mediator, final int context, final int defaultActionRefId) {
        mediator.register(new IReceiveMessage() {

            public boolean receive(ISEvent event, Object value) throws Exception {
                switch(event) {
                    case EXECUTEACTION:
                        BaseAction action = (BaseAction) value;
                        if (context > 0 && action.getContextRefId() == context) {
                            switch(action.getActionReference()) {
                                case EDITORFONTSIZE:
                                    if (action.getValue() != null && Converter.isNotEmpty(Converter.convertDisplayString(action.getValue()))) {
                                        String selectedText = getSelectedText();
                                        if (Converter.isNotEmpty(selectedText)) {
                                            String newText = "<font size=\"" + action.getValue() + "\">" + getSelectedText() + "</font>";
                                            replaceHtml(newText);
                                        }
                                    }
                                    break;
                                case EDITORAPPENDTEMPLATE:
                                    if (action.getValue() != null && action.getValue() instanceof DisplayModel) {
                                        long termId = ((DisplayModel) action.getValue()).getId();
                                        if (termId > 0) {
                                            TermModel term = ClinicalService.getTerm(termId);
                                            String newText = term.getTemplateText();
                                            if (Converter.isEmpty(newText)) {
                                                newText = term.getTermName();
                                            } else {
                                                if (newText.matches("<.*>")) {
                                                } else {
                                                    newText = newText.trim().replace("\n", "<br>");
                                                }
                                            }
                                            replaceHtml(newText);
                                        }
                                    }
                                    break;
                            }
                            return true;
                        }
                        return false;
                }
                return false;
            }
        }, this);
    }

    /**
	 * 
	 * @param newTextToReplace
	 */
    public void replaceHtml(String newTextToReplace) {
        try {
            String tmp = "...~...";
            replaceSelection(tmp);
            String oldHtml = Converter.convertDisplayString(getValue());
            String newHtml = oldHtml.replace(tmp, newTextToReplace);
            setValue(newHtml);
        } catch (Exception ex) {
            Log.exception(ex);
        }
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
