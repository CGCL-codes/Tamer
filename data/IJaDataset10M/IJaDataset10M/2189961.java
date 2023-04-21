package org.apache.struts2.showcase.validation;

public class NonFieldValidatorsExampleAction extends AbstractValidationActionSupport {

    private static final long serialVersionUID = -524460368233581186L;

    private String someText;

    private String someTextRetype;

    private String someTextRetypeAgain;

    public String getSomeText() {
        return someText;
    }

    public void setSomeText(String someText) {
        this.someText = someText;
    }

    public String getSomeTextRetype() {
        return someTextRetype;
    }

    public void setSomeTextRetype(String someTextRetype) {
        this.someTextRetype = someTextRetype;
    }

    public String getSomeTextRetypeAgain() {
        return someTextRetypeAgain;
    }

    public void setSomeTextRetypeAgain(String someTextRetypeAgain) {
        this.someTextRetypeAgain = someTextRetypeAgain;
    }
}
