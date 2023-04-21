package net.tourbook.ui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.IMessagePrefixProvider;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.Hyperlink;

/**
 * @see IMessageManager
 */
public class MessageManager implements IMessageManager {

    private static final DefaultPrefixProvider DEFAULT_PREFIX_PROVIDER = new DefaultPrefixProvider();

    private ArrayList messages = new ArrayList();

    private Hashtable decorators = new Hashtable();

    private boolean autoUpdate = true;

    private Form scrolledForm;

    private IMessagePrefixProvider prefixProvider = DEFAULT_PREFIX_PROVIDER;

    private int decorationPosition = SWT.LEFT | SWT.BOTTOM;

    private static FieldDecoration standardError = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);

    private static FieldDecoration standardWarning = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_WARNING);

    private static FieldDecoration standardInformation = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION);

    private static final String[] SINGLE_MESSAGE_SUMMARY_KEYS = { Messages.message_manager_sMessageSummary, Messages.message_manager_sMessageSummary, Messages.message_manager_sWarningSummary, Messages.message_manager_sErrorSummary };

    private static final String[] MULTIPLE_MESSAGE_SUMMARY_KEYS = { Messages.message_manager_pMessageSummary, Messages.message_manager_pMessageSummary, Messages.message_manager_pWarningSummary, Messages.message_manager_pErrorSummary };

    class ControlDecorator {

        private ControlDecoration decoration;

        private ArrayList controlMessages = new ArrayList();

        private String prefix;

        ControlDecorator(final Control control) {
            this.decoration = new ControlDecoration(control, decorationPosition, scrolledForm.getBody());
        }

        void addAll(final ArrayList target) {
            target.addAll(controlMessages);
        }

        void addMessage(final Object key, final String text, final Object data, final int type) {
            final Message message = MessageManager.this.addMessage(getPrefix(), key, text, data, type, controlMessages);
            message.control = decoration.getControl();
            if (isAutoUpdate()) update();
        }

        private void createPrefix() {
            if (prefixProvider == null) {
                prefix = "";
                return;
            }
            prefix = prefixProvider.getPrefix(decoration.getControl());
            if (prefix == null) prefix = "";
        }

        String getPrefix() {
            if (prefix == null) createPrefix();
            return prefix;
        }

        public boolean isDisposed() {
            return decoration.getControl() == null;
        }

        boolean removeMessage(final Object key) {
            final Message message = findMessage(key, controlMessages);
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
                final ArrayList peers = createPeers(controlMessages);
                final int type = ((IMessage) peers.get(0)).getMessageType();
                final String description = createDetails(createPeers(peers), true);
                if (type == IMessageProvider.ERROR) decoration.setImage(standardError.getImage()); else if (type == IMessageProvider.WARNING) decoration.setImage(standardWarning.getImage()); else if (type == IMessageProvider.INFORMATION) decoration.setImage(standardInformation.getImage());
                decoration.setDescriptionText(description);
                decoration.show();
            }
        }

        void updatePosition() {
            final Control control = decoration.getControl();
            decoration.dispose();
            this.decoration = new ControlDecoration(control, decorationPosition, scrolledForm.getBody());
            update();
        }

        void updatePrefix() {
            prefix = null;
        }
    }

    static class DefaultPrefixProvider implements IMessagePrefixProvider {

        public String getPrefix(final Control c) {
            final Composite parent = c.getParent();
            final Control[] siblings = parent.getChildren();
            for (int i = 0; i < siblings.length; i++) {
                if (siblings[i] == c) {
                    for (int j = i - 1; j >= 0; j--) {
                        final Control label = siblings[j];
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

    static class Message implements IMessage {

        Control control;

        Object data;

        Object key;

        String message;

        int type;

        String prefix;

        Message(final Object key, final String message, final int type, final Object data) {
            this.key = key;
            this.message = message;
            this.type = type;
            this.data = data;
        }

        public Control getControl() {
            return control;
        }

        public Object getData() {
            return data;
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

        public String getPrefix() {
            return prefix;
        }
    }

    public static String createDetails(final IMessage[] messages) {
        if (messages == null || messages.length == 0) return null;
        final StringWriter sw = new StringWriter();
        final PrintWriter out = new PrintWriter(sw);
        for (int i = 0; i < messages.length; i++) {
            if (i > 0) out.println();
            out.print(getFullMessage(messages[i]));
        }
        out.flush();
        return sw.toString();
    }

    private static String getFullMessage(final IMessage message) {
        if (message.getPrefix() == null) return message.getMessage();
        return message.getPrefix() + message.getMessage();
    }

    /**
	 * Creates a new instance of the message manager that will work with the provided form.
	 * 
	 * @param scrolledForm
	 *            the form to control
	 */
    public MessageManager(final Form scrolledForm) {
        this.scrolledForm = scrolledForm;
    }

    public void addMessage(final Object key, final String messageText, final Object data, final int type) {
        addMessage(null, key, messageText, data, type, messages);
        if (isAutoUpdate()) updateForm();
    }

    public void addMessage(final Object key, final String messageText, final Object data, final int type, final Control control) {
        ControlDecorator dec = (ControlDecorator) decorators.get(control);
        if (dec == null) {
            dec = new ControlDecorator(control);
            decorators.put(control, dec);
        }
        dec.addMessage(key, messageText, data, type);
        if (isAutoUpdate()) updateForm();
    }

    private Message addMessage(final String prefix, final Object key, final String messageText, final Object data, final int type, final ArrayList list) {
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

    private String createDetails(final ArrayList messages, final boolean excludePrefix) {
        final StringWriter sw = new StringWriter();
        final PrintWriter out = new PrintWriter(sw);
        for (int i = 0; i < messages.size(); i++) {
            if (i > 0) out.println();
            final IMessage m = (IMessage) messages.get(i);
            out.print(excludePrefix ? m.getMessage() : getFullMessage(m));
        }
        out.flush();
        return sw.toString();
    }

    private ArrayList createPeers(final ArrayList messages) {
        final ArrayList peers = new ArrayList();
        int maxType = 0;
        for (int i = 0; i < messages.size(); i++) {
            final Message message = (Message) messages.get(i);
            if (message.type > maxType) {
                peers.clear();
                maxType = message.type;
            }
            if (message.type == maxType) peers.add(message);
        }
        return peers;
    }

    public String createSummary(final IMessage[] messages) {
        return createDetails(messages);
    }

    private Message findMessage(final Object key, final ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            final Message message = (Message) list.get(i);
            if (message.getKey().equals(key)) return message;
        }
        return null;
    }

    public int getDecorationPosition() {
        return decorationPosition;
    }

    /**
	 * @return Returns the number of error messages
	 */
    @SuppressWarnings("unchecked")
    public int getErrorMessageCount() {
        int errors = 0;
        for (final Enumeration<ControlDecorator> enm = decorators.elements(); enm.hasMoreElements(); ) {
            final ControlDecorator dec = enm.nextElement();
            final ArrayList<?> allMessages = dec.controlMessages;
            for (final Object object : allMessages) {
                if (object instanceof Message) {
                    final Message message = (Message) object;
                    if (message.getMessageType() == IMessageProvider.ERROR) {
                        errors++;
                    }
                }
            }
        }
        return errors;
    }

    public IMessagePrefixProvider getMessagePrefixProvider() {
        return prefixProvider;
    }

    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    private void pruneControlDecorators() {
        for (final Iterator iter = decorators.values().iterator(); iter.hasNext(); ) {
            final ControlDecorator dec = (ControlDecorator) iter.next();
            if (dec.isDisposed()) iter.remove();
        }
    }

    public void removeAllMessages() {
        boolean needsUpdate = false;
        for (final Enumeration enm = decorators.elements(); enm.hasMoreElements(); ) {
            final ControlDecorator control = (ControlDecorator) enm.nextElement();
            if (control.removeMessages()) needsUpdate = true;
        }
        if (!messages.isEmpty()) {
            messages.clear();
            needsUpdate = true;
        }
        if (needsUpdate && isAutoUpdate()) updateForm();
    }

    public void removeMessage(final Object key) {
        final Message message = findMessage(key, messages);
        if (message != null) {
            messages.remove(message);
            if (isAutoUpdate()) updateForm();
        }
    }

    public void removeMessage(final Object key, final Control control) {
        final ControlDecorator dec = (ControlDecorator) decorators.get(control);
        if (dec == null) return;
        if (dec.removeMessage(key)) if (isAutoUpdate()) updateForm();
    }

    public void removeMessages() {
        if (!messages.isEmpty()) {
            messages.clear();
            if (isAutoUpdate()) updateForm();
        }
    }

    public void removeMessages(final Control control) {
        final ControlDecorator dec = (ControlDecorator) decorators.get(control);
        if (dec != null) {
            if (dec.removeMessages()) {
                if (isAutoUpdate()) updateForm();
            }
        }
    }

    public void setAutoUpdate(final boolean autoUpdate) {
        final boolean needsUpdate = !this.autoUpdate && autoUpdate;
        this.autoUpdate = autoUpdate;
        if (needsUpdate) update();
    }

    public void setDecorationPosition(final int position) {
        this.decorationPosition = position;
        for (final Iterator iter = decorators.values().iterator(); iter.hasNext(); ) {
            final ControlDecorator dec = (ControlDecorator) iter.next();
            dec.updatePosition();
        }
    }

    public void setMessagePrefixProvider(final IMessagePrefixProvider provider) {
        this.prefixProvider = provider;
        for (final Iterator iter = decorators.values().iterator(); iter.hasNext(); ) {
            final ControlDecorator dec = (ControlDecorator) iter.next();
            dec.updatePrefix();
        }
    }

    public void update() {
        for (final Iterator iter = decorators.values().iterator(); iter.hasNext(); ) {
            final ControlDecorator dec = (ControlDecorator) iter.next();
            dec.update();
        }
        updateForm();
    }

    private void update(final ArrayList mergedList) {
        pruneControlDecorators();
        if (scrolledForm.getHead().getBounds().height == 0 || mergedList.isEmpty() || mergedList == null) {
            scrolledForm.setMessage(null, IMessageProvider.NONE);
            return;
        }
        final ArrayList peers = createPeers(mergedList);
        final int maxType = ((IMessage) peers.get(0)).getMessageType();
        String messageText;
        final IMessage[] array = (IMessage[]) peers.toArray(new IMessage[peers.size()]);
        if (peers.size() == 1 && ((Message) peers.get(0)).prefix == null) {
            final IMessage message = (IMessage) peers.get(0);
            messageText = message.getMessage();
            scrolledForm.setMessage(messageText, maxType, array);
        } else {
            if (peers.size() > 1) messageText = Messages.bind(MULTIPLE_MESSAGE_SUMMARY_KEYS[maxType], new String[] { peers.size() + "" }); else messageText = SINGLE_MESSAGE_SUMMARY_KEYS[maxType];
            scrolledForm.setMessage(messageText, maxType, array);
        }
    }

    private void updateForm() {
        final ArrayList mergedList = new ArrayList();
        mergedList.addAll(messages);
        for (final Enumeration enm = decorators.elements(); enm.hasMoreElements(); ) {
            final ControlDecorator dec = (ControlDecorator) enm.nextElement();
            dec.addAll(mergedList);
        }
        update(mergedList);
    }
}
