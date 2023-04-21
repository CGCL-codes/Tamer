package gov.nist.scap.xccdf.document.x1_1;

import gov.nist.checklists.xccdf.x11.ValueType;

public class ValuePropertyExtensionResolver extends ItemPropertyExtensionResolver {

    private final ValueType extendingValue, extendedValue;

    public ValuePropertyExtensionResolver(ValueType extending, ValueType extended) {
        super(extending, extended);
        this.extendingValue = extending;
        this.extendedValue = extended;
    }

    @Override
    public void resolve() {
        super.resolve();
        PropertyExtensionResolver.getKeyedAttrResolver("selector").resolve(this.extendingValue.getChoicesList(), this.extendedValue.getChoicesList(), PropertyExtensionResolver.Action.PREPEND);
        PropertyExtensionResolver.getKeyedAttrResolver("uri").resolve(this.extendingValue.getSourceList(), this.extendedValue.getSourceList(), PropertyExtensionResolver.Action.PREPEND);
        PropertyExtensionResolver.getKeyedAttrResolver("selector").resolve(this.extendingValue.getValueList(), this.extendedValue.getValueList(), PropertyExtensionResolver.Action.APPEND);
        PropertyExtensionResolver.getXmlObjectResolver().resolve(this.extendingValue.getDefaultList(), this.extendedValue.getDefaultList(), PropertyExtensionResolver.Action.APPEND);
        PropertyExtensionResolver.getKeyedAttrResolver("selector").resolve(this.extendingValue.getMatchList(), this.extendedValue.getMatchList(), PropertyExtensionResolver.Action.APPEND);
        PropertyExtensionResolver.getKeyedAttrResolver("selector").resolve(this.extendingValue.getLowerBoundList(), this.extendedValue.getLowerBoundList(), PropertyExtensionResolver.Action.APPEND);
        PropertyExtensionResolver.getKeyedAttrResolver("selector").resolve(this.extendingValue.getUpperBoundList(), this.extendedValue.getUpperBoundList(), PropertyExtensionResolver.Action.APPEND);
        if (this.extendedValue.isSetType() && !this.extendingValue.isSetType()) {
            this.extendingValue.setType(this.extendedValue.getType());
        }
        if (this.extendedValue.isSetOperator() && !this.extendingValue.isSetOperator()) {
            this.extendingValue.setOperator(this.extendedValue.getOperator());
        }
        if (this.extendedValue.isSetInterfaceHint() && !this.extendingValue.isSetInterfaceHint()) {
            this.extendingValue.setInterfaceHint(this.extendedValue.getInterfaceHint());
        }
        if (this.extendedValue.isSetInteractive() && !this.extendingValue.isSetInteractive()) {
            this.extendingValue.setInteractive(this.extendedValue.getInteractive());
        }
        PropertyExtensionResolver.getHtmlTextWithSubTypeResolver().resolve(this.extendingValue.getWarningList(), this.extendedValue.getWarningList(), PropertyExtensionResolver.Action.OVERRIDE);
        PropertyExtensionResolver.getTextTypeResolver().resolve(this.extendingValue.getQuestionList(), this.extendedValue.getQuestionList(), PropertyExtensionResolver.Action.OVERRIDE);
    }
}
