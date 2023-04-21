package org.makagiga.commons.validator;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Serializable;
import javax.accessibility.AccessibleContext;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.makagiga.commons.MLogger;
import org.makagiga.commons.TK;
import org.makagiga.commons.UI;
import org.makagiga.commons.WTFError;
import org.makagiga.commons.annotation.InvokedFromConstructor;
import org.makagiga.commons.autocompletion.AutoCompletion;
import org.makagiga.commons.swing.MDialog;
import org.makagiga.commons.swing.MScrollPane;

/**
 * @since 3.4, 3.8 (extends InputVerifier)
 */
public abstract class Validator<V> extends InputVerifier implements Serializable {

    public enum MessageType {

        ERROR, INFO, WARNING
    }

    ;

    private boolean enabled = true;

    private JComponent component;

    private MessageType messageType = MessageType.ERROR;

    private static final StaticHandler staticHandler = new StaticHandler();

    public Validator() {
    }

    public Validator(final JComponent component) {
        setComponent(component);
    }

    public JComponent getComponent() {
        return component;
    }

    @InvokedFromConstructor
    public void setComponent(final JComponent component) {
        if (this.component != null) {
            this.component.removeFocusListener(staticHandler);
            uninstall();
        }
        this.component = component;
        if (this.component != null) {
            this.component.addFocusListener(staticHandler);
            install();
        }
    }

    /**
	 * @since 3.6
	 */
    public boolean isEnabled() {
        return enabled;
    }

    /**
	 * @since 3.6
	 */
    public void setEnabled(final boolean value) {
        enabled = value;
    }

    public final boolean validate() {
        if (!enabled) return true;
        MDialog dialog = getDialog();
        if (dialog == null) return true;
        boolean updateMessage = false;
        boolean valid;
        ValidatorMessage message = dialog.getValidatorMessage();
        try {
            setMessageType(MessageType.ERROR);
            valid = isValid();
            if (!valid && (message != null)) {
                updateMessage = true;
                message.validatedBy = this;
                message.setIcon(null);
                message.setText(null);
                message.setToolTipText(null);
            }
        } catch (Exception exception) {
            String text = exception.getMessage();
            if (message != null) {
                updateMessage = true;
                message.validatedBy = this;
                message.setToolTipText(null);
                if (component != null) {
                    AccessibleContext accessibleContext = component.getAccessibleContext();
                    if (accessibleContext != null) message.setToolTipText(accessibleContext.getAccessibleName());
                }
                switch(messageType) {
                    case ERROR:
                        message.setErrorMessage(text);
                        break;
                    case INFO:
                        message.setInfoMessage(text);
                        break;
                    case WARNING:
                        message.setWarningMessage(text);
                        break;
                    default:
                        throw new WTFError(messageType);
                }
            }
            valid = false;
        }
        if (message != null) {
            Container messageParent = message.getParent();
            if (valid) {
                if (message.isVisible() && ((message.validatedBy == this) || TK.isEmpty(message.getText()))) {
                    if (!(messageParent instanceof JLayeredPane) && (messageParent != null)) {
                        int h = message.getPreferredSize().height;
                        dialog.setHeight(dialog.getHeight() - h);
                    }
                    message.setVisible(false);
                }
            } else {
                if (updateMessage && !TK.isEmpty(message.getText())) {
                    message.setVisible(true);
                    JTextComponent textComponent = AutoCompletion.getCurrentTextComponent();
                    if (textComponent == component) AutoCompletion.hidePopupWindow();
                    if (messageParent instanceof JLayeredPane) {
                        if (component != null) {
                            JScrollPane scrollPane = MScrollPane.getScrollPane(component);
                            Rectangle r = SwingUtilities.convertRectangle((scrollPane != null) ? scrollPane.getParent() : component.getParent(), (scrollPane != null) ? scrollPane.getBounds() : component.getBounds(), dialog.getContentPane());
                            int y = r.y + component.getHeight() - (int) (ValidatorMessage.TAIL_SIZE / 1.5f);
                            message.setLocation(r.x + 5, y);
                            message.setSize(message.getPreferredSize());
                        } else {
                            message.setLocation(0, 0);
                            message.setSize(message.getPreferredSize());
                        }
                    } else if (messageParent != null) {
                        int h = message.getPreferredSize().height;
                        dialog.setHeight(dialog.getHeight() + h);
                    }
                }
            }
        }
        if (dialog.getOKButton() != null) dialog.getOKButton().setEnabled(valid || (messageType != MessageType.ERROR));
        return valid;
    }

    @Override
    public boolean verify(final JComponent input) {
        return validate();
    }

    protected MDialog getDialog() {
        if (component == null) {
            MLogger.debug("validator", "Null component");
            return null;
        }
        Window window = UI.windowFor(component);
        return (window instanceof MDialog) ? (MDialog) window : null;
    }

    protected abstract V getValue();

    protected abstract void install();

    protected abstract void uninstall();

    protected abstract boolean isValid() throws Exception;

    protected final boolean revalidate() {
        MDialog dialog = getDialog();
        if (dialog == null) return true;
        ValidatorSupport validatorSupport = dialog.getValidatorSupport();
        if (validatorSupport != null) return validatorSupport.validate(dialog);
        return true;
    }

    protected final void setMessageType(final MessageType value) {
        messageType = value;
    }

    private static final class StaticHandler implements FocusListener {

        @Override
        public void focusGained(final FocusEvent e) {
            JComponent component = (JComponent) e.getSource();
            Window window = UI.windowFor(component);
            if (window instanceof MDialog) {
                MDialog dialog = (MDialog) window;
                dialog.getValidatorSupport().validate(dialog);
            }
        }

        @Override
        public void focusLost(final FocusEvent e) {
            JComponent component = (JComponent) e.getSource();
            Window window = UI.windowFor(component);
            if (window instanceof MDialog) {
                MDialog dialog = (MDialog) window;
                ValidatorMessage message = dialog.getValidatorMessage();
                if (message != null) message.setVisible(false);
            }
        }
    }
}
