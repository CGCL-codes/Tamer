package JSci.org.w3c.dom.mathml;

import org.w3c.dom.mathml.MathMLContentElement;

public interface MathMLCaseElement extends MathMLContentElement {

    public MathMLContentElement getCaseCondition();

    public void setCaseCondition(MathMLContentElement caseCondition);

    public MathMLContentElement getCaseValue();

    public void setCaseValue(MathMLContentElement caseValue);
}

;
