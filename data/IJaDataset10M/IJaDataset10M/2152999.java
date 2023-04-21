package ces.research.oa.util;

import ces.workflow.wapi.Coflow;
import ces.workflow.wapi.WFFilter;
import ces.workflow.wapi.WFQuery;

public class WFFilterSon extends WFFilter {

    private String otherTable;

    public void setOtherTable(String ot) {
        this.otherTable = ot;
    }

    public WFFilterSon(WFQuery query, int fromLine, int toLine) {
        super(query, fromLine, toLine);
    }

    /**
     *
     * @return String[]
     */
    public String[] getUniteTableName() {
        if ("".equals(super.getProcessId()) && "".equals(super.getPackageId())) {
            return null;
        }
        try {
            String relevantDataTableName = Coflow.getDefineManager().getReventDatetablename(super.getPackageId(), null, super.getProcessId());
            if (relevantDataTableName == null) {
                return null;
            }
            if (this.otherTable != null) {
                return new String[] { relevantDataTableName + " s ", otherTable + " o" };
            }
            return new String[] { relevantDataTableName + " s " };
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
