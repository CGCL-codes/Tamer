package wheel.components;

import org.mvel.MVEL;
import org.xmlpull.v1.XmlSerializer;
import wheel.WheelException;
import java.io.IOException;
import java.util.*;

/**
 * All components that want to handle form input should extend from this class.
 * Defines a set of operations like validation and binding. <br/>
 * <b>Binding fields to FormElements:</b><br/>
 * Binding is done with MVEL-expressions.
 * The only rule is that the target of the binding must be available in the complex component using javabean get/set-methods.
 * <br/>
 * Example:
 * <pre>
 * public class MyComponent extends StandaloneComponent {
 *  private Calendar cal;
 *
 *  public Calendar getCal() {
 *      return cal;
 *  }
 *
 *  public void setCal(Calendar cal) {
 *      this.cal = cal;
 *  }
 * }
 * </pre>
 * In the above example, the containing complex component has a field declaration like this:
 * <pre>private List<Integer> numbers;</pre>
 * Note that when using Lists and Maps, they need to be initialized, ie. they can't be empty.
 *
 * <b>Limitations:</b><br/>
 * Array types are not supported at the moment. Use Collections-api instead.
 *
 * @author Henri Frilund
 */
public abstract class FormElement extends RenderableComponent {

    private CharSequence binding;

    private List<ValidationRule> validationRules;

    protected String[] value;

    private List<ValidationError> validationErrors;

    protected String genericFieldErrorMessage = "fieldError";

    private String formElementName;

    private boolean complex;

    protected FormElement(Component parent, String elementName, String componentId) {
        super(parent, componentId);
        this.formElementName = elementName;
    }

    /**
     * Adds a validation rule to this form element. Validation rules are MVEL-expressions that are beefed up with a set of
     * predefined variables and a set of validation methods as a default context.
     * Example: <br/>
     * A validation expression "isInt(value)" is equivalent to Validations.getInstance().isInt(formElement.getValue);<br/>
     * <b>Predefined variables:</b><br/>
     * <ol>
     *  <li>value - the submitted form value for this field.</li>
     *  <li>element - a reference to <i>this</i>, ie. gives access to the form element itself.</li>
     *  <li>form - a reference to the Form-component that contains the form element.</li>
     * </ol>
     * <br/>
     * Additional variables can be injected using StandaloneComponent.config().useValidationVariable() method.
     * @param elExpression A valid MVEL-expression
     * @param message The message to put in the ValidationError object if the validation fails. Can be an el-expression or a localized message.
     * @return
     * @see Validations Validations for currently avaiable validation methods.
     */
    public FormElement validationRule(String elExpression, String message) {
        if (validationRules == null) validationRules = new LinkedList<ValidationRule>();
        validationRules.add(new ValidationRule(elExpression, message));
        if (elExpression.contains("element") || elExpression.contains("form")) complex = true;
        return this;
    }

    public FormElement validationRule(String elExpression) {
        if (validationRules == null) validationRules = new LinkedList<ValidationRule>();
        validationRules.add(new ValidationRule(elExpression, ""));
        if (elExpression.contains("element") || elExpression.contains("form")) complex = true;
        return this;
    }

    /**
     * Returns the binding for this form element.
     * @return
     */
    public CharSequence getBinding() {
        if (binding == null) return "";
        return binding;
    }

    public FormElement setBinding(CharSequence binding) {
        this.binding = binding;
        return this;
    }

    /**
     * Returns true if no value has been set due to a form submit to this element yet.
     * @return
     */
    public boolean isEmpty() {
        return value == null || value.length == 0 || value[0] == null || value[0].length() == 0;
    }

    /**
     * Causes this element to validate itself against given input value.
     */
    public void validate() {
        if (validationRules == null || validationRules.isEmpty()) return;
        Map vars = new HashMap();
        vars.put("value", value[0]);
        vars.put("element", this);
        vars.put("form", _getForm(true));
        Map otherVariables = _getTopLevelComponent(true).validationVariables;
        if (otherVariables != null) vars.putAll(otherVariables);
        for (ValidationRule validationRule : validationRules) {
            Boolean valid = null;
            try {
                valid = (Boolean) MVEL.eval(validationRule.getElExpression(), Validations.getInstance(), vars);
            } catch (Throwable e) {
                throw new WheelException("Validation expression " + validationRule.getElExpression() + " failed.", e, this);
            }
            if (!valid.booleanValue()) {
                if (validationErrors == null) validationErrors = new LinkedList<ValidationError>();
                validationErrors.add(new ValidationError(eval(validationRule.getMessage()), this));
            }
        }
        if (!isValid()) addInternalRenderHint("error");
    }

    /**
     * Sets the value retrieved from a form submit for this element. Calling this method also triggers a call to validate();
     * @param value
     */
    public void _setSubmitValue(String[] value) {
        this.value = value;
        validate();
        if (isValid() && !isEmpty()) {
            if (binding instanceof ElExpression) {
                try {
                    ((ElExpression) binding).store(_getTopLevelComponent(true), this, value[0]);
                } catch (Exception e) {
                    if (validationErrors == null) validationErrors = new LinkedList<ValidationError>();
                    validationErrors.add(new ValidationError(eval(message(genericFieldErrorMessage)), this));
                    addInternalRenderHint("error");
                }
            }
        }
    }

    /**
     * Adds a label to this form element. Will create a &lt;label for="componentId"&gt;label text&lt;/label&gt; tag before the form element.
     * @param label The text to render inside the label-tag.
     * @return
     */
    public FormElement label(String label) {
        RenderableComponent l = (RenderableComponent) getParent().create().label(label).attribute("for", getComponentId());
        addRenderBefore(l);
        return this;
    }

    /**
     * Returns all validation errors for this form element.
     * @return
     */
    public List<ValidationError> getErrors() {
        if (validationErrors == null) return Collections.EMPTY_LIST;
        return validationErrors;
    }

    /**
     * Returns true if this form element is valid.
     * @return
     */
    public boolean isValid() {
        if (validationErrors == null || (validationErrors != null && validationErrors.isEmpty())) return true;
        return false;
    }

    /**
     * Returns the value recieved in form submit.
     * @return
     */
    public String[] _getSubmitValues() {
        return value;
    }

    /**
     * Returns the value recieved in form submit.
     * @return
     */
    public String _getSubmitValue() {
        return value[0];
    }

    @Override
    public synchronized void _clear() {
        if (_getRenderBeforeMe() != null) _getRenderBeforeMe().clear();
        if (_getRenderAfterMe() != null) _getRenderAfterMe().clear();
        if (_getActions() != null) _getActions().clear();
        if (_getRenderHints() != null) _getRenderHints().clear();
    }

    /**
     * A shorthand method to mark this form element as required.
     * @return
     */
    public FormElement required() {
        validationRule("required(value)", message(genericFieldErrorMessage));
        return this;
    }

    /**
     * A shorthand method to mark this form element as an integer.
     * @return
     */
    public FormElement isInt() {
        validationRule("isInt(value)", message(genericFieldErrorMessage));
        return this;
    }

    protected String defaultDomEvent() {
        return "onchange";
    }

    public FormElement setFormElementName(String name) {
        this.formElementName = name;
        return this;
    }

    public String getFormElementName() {
        return formElementName;
    }

    @Override
    public void _render(XmlSerializer serializer) throws IOException {
        attribute("name", getFormElementName().toString());
        super._render(serializer);
    }

    public FormElement fieldBinding(String binding) {
        this.binding = el(binding);
        return this;
    }

    public FormElement value(String value) {
        throw new WheelException("This form element cannot be bound to a literal value.", this);
    }

    public FormElement initialFieldValue(String value) {
        if (!(getBinding() instanceof ElExpression)) throw new WheelException("Initial field value can only be set for a bound field element.", this);
        _getTopLevelComponent(false).config().initialFieldValue(getBinding().toString(), value);
        return this;
    }

    public FormElement initialFieldValue(String value, Object emptyValue) {
        if (!(getBinding() instanceof ElExpression)) throw new WheelException("Initial field value can only be set for a bound field element.", this);
        _getTopLevelComponent(false).config().initialFieldValue(getBinding().toString(), value, emptyValue);
        return this;
    }

    public FormElement addValidationError(String message) {
        if (validationErrors == null) validationErrors = new LinkedList<ValidationError>();
        validationErrors.add(new ValidationError(message, this));
        return this;
    }

    boolean isComplex() {
        return complex;
    }

    void serialize(StringBuilder sb) {
        sb.append(formElementName).append("$$");
        sb.append(getComponentName()).append("$$");
        if (getBinding() instanceof ElExpression) sb.append("el:").append(getBinding()); else sb.append(getBinding());
        if (validationRules != null) {
            sb.append("$$");
            Iterator<ValidationRule> rulesIterator = validationRules.iterator();
            while (rulesIterator.hasNext()) {
                ValidationRule rule = rulesIterator.next();
                sb.append(rule.getElExpression()).append("$$");
                sb.append(rule.getMessage().length() == 0 ? " " : rule.getMessage()).append("$$");
            }
        }
    }

    FormElement deserialize(Form form, String serializedString) {
        String[] split = serializedString.split("$$");
        String name = split[0];
        String binding = split[1];
        FormElement element = null;
        if (split[2].equals("Submit")) {
            element = create().submit(name);
        } else if (split[2].equals("Hidden")) {
            element = create().hidden(name);
        } else if (split[2].equals("Checkbox")) {
            element = create().hidden(name);
        } else if (split[2].equals("TextInput")) {
            element = create().textInput(name);
        }
        if (binding.startsWith("el:")) element.fieldBinding(binding); else element.value(binding);
        if (split.length > 3) {
            for (int i = 3; i < split.length; i += 2) {
                String rule = split[i];
                String message = split[i + 1];
                element.validationRule(rule, message);
            }
        }
        return element;
    }
}
