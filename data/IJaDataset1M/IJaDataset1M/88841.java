package eu.maydu.gwt.validation.client.actions;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import eu.maydu.gwt.validation.client.ValidationAction;
import eu.maydu.gwt.validation.client.ValidationResult;
import eu.maydu.gwt.validation.client.ValidationResult.ValidationError;

/**
 * Action that can be used as a global action added directly
 * to the <code>ValidationProcessor</code>'s <code>addGlobalAction</code>
 * method instance used for validation. It will provide a 
 * summary of all the errors that occured during a validation run.
 * 
 * @author Anatol Gregory Mayen
 *
 */
public class DisclosureTextAction extends ValidationAction<Object> {

    private DisclosurePanel disclosure;

    private boolean isOpen = false;

    private boolean autoHide;

    private VerticalPanel errorPanel = new VerticalPanel();

    private String delimiter;

    private String style;

    private LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();

    public DisclosureTextAction(DisclosurePanel panel, String style) {
        this(panel, "redText", ": ", true);
    }

    public DisclosureTextAction(DisclosurePanel panel, String style, String delimiter) {
        this(panel, style, delimiter, true);
    }

    public DisclosureTextAction(DisclosurePanel panel, String style, String delimiter, boolean autoHide) {
        super();
        this.disclosure = panel;
        this.autoHide = autoHide;
        this.delimiter = delimiter;
        this.style = style;
        disclosure.addEventHandler(new DisclosureHandler() {

            public void onClose(DisclosureEvent event) {
                isOpen = false;
            }

            public void onOpen(DisclosureEvent event) {
                isOpen = true;
            }
        });
        if (disclosure.getContent() != null) disclosure.remove(disclosure.getContent());
        disclosure.add(errorPanel);
    }

    @Override
    public void invoke(ValidationResult result, Object notUsed) {
        if (result == null) return;
        reset();
        for (ValidationError error : result.getErrors()) {
            String prefix = "";
            if (error.propertyName != null && !error.propertyName.trim().equals("")) {
                if (!localeInfo.isRTL()) prefix = error.propertyName + delimiter; else prefix = delimiter + error.propertyName;
            }
            String text;
            if (!localeInfo.isRTL()) text = prefix + error.error; else text = error.error + prefix;
            Label l = new Label(text);
            l.setStyleName(style);
            errorPanel.add(l);
        }
        if (!isOpen) {
            disclosure.setOpen(true);
        }
    }

    @Override
    public void reset() {
        errorPanel.clear();
        if (autoHide && isOpen) {
            disclosure.setOpen(false);
        }
    }
}
