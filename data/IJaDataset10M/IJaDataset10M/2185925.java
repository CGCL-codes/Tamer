package rm.gwt.appspace.client.validator;

public class RequiredCondition extends AbstractCondition {

    private static final String errMsgPart = " :: is required!";

    private String errMsg = null;

    public RequiredCondition() {
        super();
    }

    public RequiredCondition(String fieldCaption) {
        super(fieldCaption);
    }

    public String validate(String validationText) {
        if (isEmpty(validationText)) return errMsg;
        return null;
    }

    @Override
    protected void initMessage() {
        errMsg = fieldCaption + errMsgPart;
    }
}
