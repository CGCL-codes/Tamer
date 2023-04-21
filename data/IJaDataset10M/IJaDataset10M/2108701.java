package de.icehorsetools.iceoffice.state.testcondition;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.ugat.wiser.language.Lang;
import org.ugat.wiser.state.AUnState;
import de.icehorsetools.constants.TestconditionTypeCo;
import de.icehorsetools.dataAccess.objects.Test;
import de.icehorsetools.dataAccess.objects.Testcondition;
import de.icehorsetools.interfaces.IIcehorestoolsDataAccess;

/**
 * @author tkr
 * @version $Id: TestconditionForTestAddSt.java 324 2009-04-20 21:39:34Z kruegertom $
 */
public class TestconditionSubSt extends AUnState {

    private Object data = null;

    public void initialize() {
        this.setContainerComponentByPluginName("testconditionSub");
    }

    public Object getData() {
        if (this.data instanceof Testcondition) {
            Testcondition testcondition = (Testcondition) this.getContainerHandler().getData();
            return testcondition;
        } else {
            return null;
        }
    }

    public void setData(Object xData) {
        this.data = xData;
        if (this.data instanceof Testcondition) {
            Testcondition testcondition = (Testcondition) this.data;
            this.getContainerHandler().setData(testcondition);
        }
    }

    public void setElementData(Collection xExcludes, HashMap xFilter) {
        this.getContainerHandler().getComponentHandler("type").setElementData(TestconditionTypeCo.getElements());
    }

    public String getTitle() {
        return Lang.get(this.getClass(), "title");
    }
}
