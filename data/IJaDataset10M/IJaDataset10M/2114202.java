package equilibrium.commons.report.databuilder.optionsstrategy;

import java.util.List;
import equilibrium.commons.report.ReportContext;
import equilibrium.commons.report.config.model.ElementBean;
import equilibrium.commons.report.config.model.OptionsStrategyType;
import equilibrium.commons.report.databuilder.optionsstrategy.mock.OptionSelectorReportContextMock;
import junit.framework.TestCase;

public abstract class AbstractOptionsSelectorTest extends TestCase {

    protected OptionsSelector selector;

    protected abstract void setUp() throws Exception;

    public abstract void testSelectOptions();

    public void testSelectOptionsWhenNothingSelected() {
        ElementBean firstOption = ElementBean.createOptionElement("not satisfied condition");
        firstOption.addElement(firstOption);
        ElementBean optionsElement = ElementBean.createOptionsElement(OptionsStrategyType.FIRST);
        optionsElement.addElement(firstOption);
        ReportContext context = new OptionSelectorReportContextMock();
        List<ElementBean> selectedOptions = selector.selectOptions(optionsElement, context);
        assertEquals(0, selectedOptions.size());
    }
}
