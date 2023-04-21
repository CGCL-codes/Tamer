package net.sf.rcpforms.widgetwrapper.compatibility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Logger;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.internal.forms.widgets.FormHeading;

/**
 * @see IMessageManager
 */
public class MessageManager implements IMessageManager {

    private static final Logger LOG = Logger.getLogger(MessageManager.class.getName());

    private static final DefaultPrefixProvider DEFAULT_PREFIX_PROVIDER = new DefaultPrefixProvider();

    private ArrayList messages = new ArrayList();

    private Hashtable decorators = new Hashtable();

    private boolean autoUpdate = true;

    private ScrolledForm scrolledForm;

    private IMessagePrefixProvider prefixProvider = DEFAULT_PREFIX_PROVIDER;

    private int decorationPosition = SWT.LEFT | SWT.BOTTOM;

    private static FieldDecoration standardError = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);

    private static FieldDecoration standardWarning = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_WARNING);

    private static FieldDecoration standardInformation = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_WARNING);

    private static final String[] SINGLE_MESSAGE_SUMMARY_KEYS = { Messages.MessageManager_sMessageSummary, Messages.MessageManager_sMessageSummary, Messages.MessageManager_sWarningSummary, Messages.MessageManager_sErrorSummary };

    private static final String[] MULTIPLE_MESSAGE_SUMMARY_KEYS = { Messages.MessageManager_pMessageSummary, Messages.MessageManager_pMessageSummary, Messages.MessageManager_pWarningSummary, Messages.MessageManager_pErrorSummary };

    static class Message implements IMessage {

        Control control;

        Object data;

        Object key;

        String message;

        int type;

        String prefix;

        Message(Object key, String message, int type, Object data) {
            this.key = key;
            this.message = message;
            this.type = type;
            this.data = data;
        }

        public Object getKey() {
            return key;
        }

        public String getMessage() {
            return message;
        }

        public int getMessageType() {
            return type;
        }

        public Control getControl() {
            return control;
        }

        public Object getData() {
            return data;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    static class DefaultPrefixProvider implements IMessagePrefixProvider {

        public String getPrefix(Control c) {
            Composite parent = c.getParent();
            Control[] siblings = parent.getChildren();
            for (int i = 0; i < siblings.length; i++) {
                if (siblings[i] == c) {
                    for (int j = i - 1; j >= 0; j--) {
                        Control label = siblings[j];
                        String ltext = null;
                        if (label instanceof Label) {
                            ltext = ((Label) label).getText();
                        } else if (label instanceof Hyperlink) {
                            ltext = ((Hyperlink) label).getText();
                        } else if (label instanceof CLabel) {
                            ltext = ((CLabel) label).getText();
                        }
                        if (ltext != null) {
                            if (!ltext.endsWith(":")) return ltext + ": ";
                            return ltext + " ";
                        }
                    }
                    break;
                }
            }
            return null;
        }
    }

    class ControlDecorator {

        private ControlDecoration decoration;

        private ArrayList controlMessages = new ArrayList();

        private String prefix;

        ControlDecorator(Control control) {
            this.decoration = new ControlDecoration(control, decorationPosition, scrolledForm.getBody());
        }

        public boolean isDisposed() {
            return decoration.getControl() == null;
        }

        void updatePrefix() {
            prefix = null;
        }

        void updatePosition() {
            Control control = decoration.getControl();
            decoration.dispose();
            this.decoration = new ControlDecoration(control, decorationPosition, scrolledForm.getBody());
            update();
        }

        String getPrefix() {
            if (prefix == null) createPrefix();
            return prefix;
        }

        private void createPrefix() {
            if (prefixProvider == null) {
                prefix = "";
                return;
            }
            prefix = prefixProvider.getPrefix(decoration.getControl());
            if (prefix == null) prefix = "";
        }

        void addAll(ArrayList target) {
            target.addAll(controlMessages);
        }

        void addMessage(Object key, String text, Object data, int type) {
            Message message = MessageManager.this.addMessage(getPrefix(), key, text, data, type, controlMessages);
            message.control = decoration.getControl();
            if (isAutoUpdate()) update();
        }

        boolean removeMessage(Object key) {
            Message message = findMessage(key, controlMessages);
            if (message != null) {
                controlMessages.remove(message);
                if (isAutoUpdate()) update();
            }
            return message != null;
        }

        boolean removeMessages() {
            if (controlMessages.isEmpty()) return false;
            controlMessages.clear();
            if (isAutoUpdate()) update();
            return true;
        }

        public void update() {
            if (controlMessages.isEmpty()) {
                decoration.setDescriptionText(null);
                decoration.hide();
            } else {
                ArrayList peers = createPeers(controlMessages);
                int type = ((IMessage) peers.get(0)).getMessageType();
                String description = createDetails(createPeers(peers), true);
                if (type == IMessageProvider.ERROR) decoration.setImage(standardError.getImage()); else if (type == IMessageProvider.WARNING) decoration.setImage(standardWarning.getImage()); else if (type == IMessageProvider.INFORMATION) decoration.setImage(standardInformation.getImage());
                decoration.setDescriptionText(description);
                decoration.show();
            }
        }
    }

    /**
     * Creates a new instance of the message manager that will work with the provided form.
     *
     * @param scrolledForm the form to control
     */
    public MessageManager(ScrolledForm scrolledForm) {
        this.scrolledForm = scrolledForm;
    }

    public void addMessage(Object key, String messageText, Object data, int type) {
        addMessage(null, key, messageText, data, type, messages);
        if (isAutoUpdate()) updateForm();
    }

    public void addMessage(Object key, String messageText, Object data, int type, Control control) {
        ControlDecorator dec = (ControlDecorator) decorators.get(control);
        if (dec == null) {
            dec = new ControlDecorator(control);
            decorators.put(control, dec);
        }
        dec.addMessage(key, messageText, data, type);
        if (isAutoUpdate()) updateForm();
    }

    public void removeMessage(Object key) {
        Message message = findMessage(key, messages);
        if (message != null) {
            messages.remove(message);
            if (isAutoUpdate()) updateForm();
        }
    }

    public void removeMessages() {
        if (!messages.isEmpty()) {
            messages.clear();
            if (isAutoUpdate()) updateForm();
        }
    }

    public void removeMessage(Object key, Control control) {
        ControlDecorator dec = (ControlDecorator) decorators.get(control);
        if (dec == null) return;
        if (dec.removeMessage(key)) if (isAutoUpdate()) updateForm();
    }

    public void removeMessages(Control control) {
        ControlDecorator dec = (ControlDecorator) decorators.get(control);
        if (dec != null) {
            if (dec.removeMessages()) {
                if (isAutoUpdate()) updateForm();
            }
        }
    }

    public void removeAllMessages() {
        boolean needsUpdate = false;
        for (Enumeration enm = decorators.elements(); enm.hasMoreElements(); ) {
            ControlDecorator control = (ControlDecorator) enm.nextElement();
            if (control.removeMessages()) needsUpdate = true;
        }
        if (!messages.isEmpty()) {
            messages.clear();
            needsUpdate = true;
        }
        if (needsUpdate && isAutoUpdate()) updateForm();
    }

    private Message addMessage(String prefix, Object key, String messageText, Object data, int type, ArrayList list) {
        Message message = findMessage(key, list);
        if (message == null) {
            message = new Message(key, messageText, type, data);
            message.prefix = prefix;
            list.add(message);
        } else {
            message.message = messageText;
            message.type = type;
            message.data = data;
        }
        return message;
    }

    private Message findMessage(Object key, ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            Message message = (Message) list.get(i);
            if (message.getKey().equals(key)) return message;
        }
        return null;
    }

    public void update() {
        for (Iterator iter = decorators.values().iterator(); iter.hasNext(); ) {
            ControlDecorator dec = (ControlDecorator) iter.next();
            dec.update();
        }
        updateForm();
    }

    private void updateForm() {
        ArrayList mergedList = new ArrayList();
        mergedList.addAll(messages);
        for (Enumeration enm = decorators.elements(); enm.hasMoreElements(); ) {
            ControlDecorator dec = (ControlDecorator) enm.nextElement();
            dec.addAll(mergedList);
        }
        update(mergedList);
    }

    private void setMessage(ScrolledForm scrolledForm, String message, int type) {
        setMessage(scrolledForm, message, type, null);
    }

    private void setMessage(ScrolledForm scrolledForm, String message, int type, IMessage[] details) {
        if (details != null) {
            String longMessage = message;
            for (IMessage detailMsg : details) {
                longMessage += "\n" + detailMsg.getMessage();
            }
            FormHeading head = (FormHeading) scrolledForm.getForm().getHead();
            scrolledForm.getForm().setMessage(message, type);
            hackTooltipIntoMessageArea(head, longMessage);
        } else {
            scrolledForm.getForm().setMessage(message, type);
        }
        scrolledForm.reflow(true);
    }

    private void hackTooltipIntoMessageArea(FormHeading head, String longMessage) {
        Field messageAreaField;
        try {
            messageAreaField = head.getClass().getDeclaredField("messageArea");
            messageAreaField.setAccessible(true);
            Object messageArea = messageAreaField.get(head);
            Field labelField = messageArea.getClass().getDeclaredField("label");
            labelField.setAccessible(true);
            CLabel label = (CLabel) labelField.get(messageArea);
            label.setToolTipText(longMessage);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.warning(e.getMessage());
        }
    }

    private void update(ArrayList mergedList) {
        pruneControlDecorators();
        if (scrolledForm.getForm().getHead().getBounds().height == 0 || mergedList.isEmpty() || mergedList == null) {
            setMessage(scrolledForm, null, IMessageProvider.NONE);
            return;
        }
        ArrayList peers = createPeers(mergedList);
        int maxType = ((IMessage) peers.get(0)).getMessageType();
        String messageText;
        IMessage[] array = (IMessage[]) peers.toArray(new IMessage[peers.size()]);
        if (peers.size() == 1 && ((Message) peers.get(0)).prefix == null) {
            IMessage message = (IMessage) peers.get(0);
            messageText = message.getMessage();
            setMessage(scrolledForm, messageText, maxType, array);
        } else {
            if (peers.size() > 1) messageText = Messages.bind(MULTIPLE_MESSAGE_SUMMARY_KEYS[maxType], new String[] { peers.size() + "" }); else messageText = SINGLE_MESSAGE_SUMMARY_KEYS[maxType];
            setMessage(scrolledForm, messageText, maxType, array);
        }
    }

    private static String getFullMessage(IMessage message) {
        if (message.getPrefix() == null) return message.getMessage();
        return message.getPrefix() + message.getMessage();
    }

    private ArrayList createPeers(ArrayList messages) {
        ArrayList peers = new ArrayList();
        int maxType = 0;
        for (int i = 0; i < messages.size(); i++) {
            Message message = (Message) messages.get(i);
            if (message.type > maxType) {
                peers.clear();
                maxType = message.type;
            }
            if (message.type == maxType) peers.add(message);
        }
        return peers;
    }

    private String createDetails(ArrayList messages, boolean excludePrefix) {
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        for (int i = 0; i < messages.size(); i++) {
            if (i > 0) out.println();
            IMessage m = (IMessage) messages.get(i);
            out.print(excludePrefix ? m.getMessage() : getFullMessage(m));
        }
        out.flush();
        return sw.toString();
    }

    public static String createDetails(IMessage[] messages) {
        if (messages == null || messages.length == 0) return null;
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        for (int i = 0; i < messages.length; i++) {
            if (i > 0) out.println();
            out.print(getFullMessage(messages[i]));
        }
        out.flush();
        return sw.toString();
    }

    public String createSummary(IMessage[] messages) {
        return createDetails(messages);
    }

    private void pruneControlDecorators() {
        for (Iterator iter = decorators.values().iterator(); iter.hasNext(); ) {
            ControlDecorator dec = (ControlDecorator) iter.next();
            if (dec.isDisposed()) iter.remove();
        }
    }

    public IMessagePrefixProvider getMessagePrefixProvider() {
        return prefixProvider;
    }

    public void setMessagePrefixProvider(IMessagePrefixProvider provider) {
        this.prefixProvider = provider;
        for (Iterator iter = decorators.values().iterator(); iter.hasNext(); ) {
            ControlDecorator dec = (ControlDecorator) iter.next();
            dec.updatePrefix();
        }
    }

    public int getDecorationPosition() {
        return decorationPosition;
    }

    public void setDecorationPosition(int position) {
        this.decorationPosition = position;
        for (Iterator iter = decorators.values().iterator(); iter.hasNext(); ) {
            ControlDecorator dec = (ControlDecorator) iter.next();
            dec.updatePosition();
        }
    }

    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    public void setAutoUpdate(boolean autoUpdate) {
        boolean needsUpdate = !this.autoUpdate && autoUpdate;
        this.autoUpdate = autoUpdate;
        if (needsUpdate) update();
    }
}
