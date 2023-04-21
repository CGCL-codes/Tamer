package projectManagement.Project;

import java.math.BigDecimal;
import java.util.List;
import net.sf.mpxj.mspdi.schema.TimephasedDataType;

public class AssignmentBaselineData {

    protected List<TimephasedDataType> timephasedData;

    protected String number;

    protected String start;

    protected String finish;

    protected String work;

    protected String cost;

    protected BigDecimal bcws;

    protected BigDecimal bcwp;
}
