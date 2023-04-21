package net.sf.validation4gwt.demo.client;

import net.sf.validation4gwt.client.AbstractAsynchronousValidator;
import net.sf.validation4gwt.client.AbstractSynchronousValidator;
import net.sf.validation4gwt.client.AllValidCompositeValidator;
import net.sf.validation4gwt.client.ValidationListener;
import net.sf.validation4gwt.client.event.SuccessfulValidationEvent;
import net.sf.validation4gwt.client.event.ValidationEvent;
import net.sf.validation4gwt.client.trigger.OnChangeValidationTrigger;
import net.sf.validation4gwt.client.trigger.OnKeyUpValidationTrigger;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DemoValidateableForm extends Composite {

    private final TextBox textBox1 = new TextBox();

    private final TextBox textBox2 = new TextBox();

    private final ListBox listBox = new ListBox();

    private final TextBox serverSideValidatedTextBox = new TextBox();

    private final Button submitButton = new Button("Submit");

    private final AllValidCompositeValidator allValidCompositeValidator = new AllValidCompositeValidator();

    private final FormValidationListener formValidationListener = new FormValidationListener();

    private final TextBox12BothFieldsAreNotEmptyValidator textBox12Validator = new TextBox12BothFieldsAreNotEmptyValidator();

    private final ListBoxValidValueSelectedValidator listBoxValidator = new ListBoxValidValueSelectedValidator();

    private final ServerSideValidator serverSideValidator = new ServerSideValidator();

    public DemoValidateableForm() {
        Panel panel = new VerticalPanel();
        panel.add(textBox1);
        panel.add(textBox2);
        panel.add(listBox);
        panel.add(serverSideValidatedTextBox);
        listBox.addItem("valid");
        listBox.addItem("invalid");
        panel.add(submitButton);
        initWidget(panel);
        textBox1.addKeyboardListener(new OnKeyUpValidationTrigger(textBox12Validator));
        textBox2.addKeyboardListener(new OnKeyUpValidationTrigger(textBox12Validator));
        serverSideValidatedTextBox.addKeyboardListener(new OnKeyUpValidationTrigger(serverSideValidator));
        listBox.addChangeListener(new OnChangeValidationTrigger(listBoxValidator));
        allValidCompositeValidator.addValidationListeners(formValidationListener);
        allValidCompositeValidator.addValidators(serverSideValidator, textBox12Validator, listBoxValidator);
    }

    private class TextBox12BothFieldsAreNotEmptyValidator extends AbstractSynchronousValidator {

        @Override
        protected boolean isValid() {
            return (textBox1.getText().length() != 0 && textBox2.getText().length() != 0);
        }
    }

    private class ListBoxValidValueSelectedValidator extends AbstractSynchronousValidator {

        @Override
        protected boolean isValid() {
            return listBox.getValue(listBox.getSelectedIndex()).equals("valid");
        }
    }

    private class FormValidationListener implements ValidationListener {

        public void handle(ValidationEvent validationEvent) {
            if (validationEvent instanceof SuccessfulValidationEvent) {
                submitButton.setEnabled(true);
            } else {
                submitButton.setEnabled(false);
            }
        }
    }

    private class ServerSideValidator extends AbstractAsynchronousValidator {

        @Override
        public void doAsynchronousValidation() {
            DemoValidationServiceAsync demoValidationServiceAsync = GWT.create(DemoValidationService.class);
            ServiceDefTarget endpoint = (ServiceDefTarget) demoValidationServiceAsync;
            String moduleRelativeURL = GWT.getModuleBaseURL() + "validate";
            endpoint.setServiceEntryPoint(moduleRelativeURL);
            demoValidationServiceAsync.validateLengthGreaterThanSix(serverSideValidatedTextBox.getText(), new ValidationAsyncCallback());
        }
    }
}
