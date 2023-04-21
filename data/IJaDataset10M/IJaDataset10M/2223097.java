package org.ufacekit.ui.swing.core.controls;

import java.awt.Button;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import org.ufacekit.core.ubean.notify.Notification;
import org.ufacekit.core.util.ConvertUtils;
import org.ufacekit.ui.core.UIFactory;
import org.ufacekit.ui.core.UIRunnable;
import org.ufacekit.ui.core.controls.UIButton;
import org.ufacekit.ui.core.controls.UIComposite;
import org.ufacekit.ui.core.controls.internal.InternalUIButton;
import org.ufacekit.ui.swing.core.internal.SwingControl;
import org.ufacekit.ui.swing.core.internal.SwingHelper;

/**
 * Swing implementation of {@link UIButton}
 *
 * @since 1.0
 */
public class SwingButton extends SwingControl<AbstractButton, AbstractButton> implements InternalUIButton {

    private UIRunnable<UIButton> selectionRunnable;

    /**
	 * Create a new instance of a {@link Button} and wrap it
	 * 
	 * @param factory
	 *            the factory the widget created with
	 * 
	 * @param parent
	 *            the parent composite the control is created on
	 * @param uiInfo
	 *            info to layout and define the control
	 * @since 1.0
	 */
    public SwingButton(UIFactory<?> factory, UIComposite parent, UIButton.ButtonUIInfo uiInfo) {
        this(factory, parent, createControl(SwingHelper.asContainer(parent), uiInfo), uiInfo);
    }

    /**
	 * Wrap an existing {@link Button}
	 * 
	 * @param factory
	 *            the factory the widget created with
	 * @param parent
	 *            the parent of the widget
	 * @param button
	 *            the button to wrap
	 * @param uiInfo
	 *            info to layout and define the control
	 * @since 1.0
	 */
    public SwingButton(UIFactory<?> factory, UIComposite parent, AbstractButton button, UIButton.ButtonUIInfo uiInfo) {
        super(factory, parent, button, button, uiInfo);
        getComponent().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (selectionRunnable != null) {
                    selectionRunnable.execute(SwingButton.this);
                }
            }
        });
    }

    /**
	 * Create Swing Button control.
	 * 
	 * @param parent
	 * @param uiInfo
	 * @return
	 */
    private static JButton createControl(Container parent, UIButton.ButtonUIInfo uiInfo) {
        return new JButton();
    }

    public void setActionRunnable(UIRunnable<UIButton> runnable) {
        this.selectionRunnable = runnable;
    }

    public UIRunnable<UIButton> getActionRunnable() {
        return selectionRunnable;
    }

    public UIComposite getParent() {
        return (UIComposite) super.getParent();
    }

    public String getText() {
        return getComponent().getText();
    }

    public void setText(String text) {
        String oldText = getText();
        if (!oldText.equals(text)) {
            getComponent().setText(text);
            notifyListeners(new Notification(this, Notification.SET, TEXT_PROPERTY, oldText, text));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V extends Object> V get(int featureId) {
        switch(featureId) {
            case TEXT_PROPERTY:
                return (V) getText();
            default:
                return (V) super.get(featureId);
        }
    }

    protected void addSupportedFeatures() {
        super.addSupportedFeatures();
        super.addSupportedFeature(TEXT_PROPERTY, String.class);
    }

    @Override
    public void set(int featureId, Object value) {
        switch(featureId) {
            case TEXT_PROPERTY:
                setText(ConvertUtils.getString(value));
                break;
            default:
                super.set(featureId, value);
                break;
        }
    }
}
