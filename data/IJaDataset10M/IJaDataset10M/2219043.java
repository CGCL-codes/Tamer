package org.brainypdm.dto.requests;

public class FilterHost extends BaseGetRequest {

    private static final long serialVersionUID = -5463721112751171288L;

    public static final int NO_ORDER = -1;

    public static final int ORDER_BY_HOST_NAME = 0;

    private boolean withService;

    private boolean withServiceData;

    private boolean onlyServiceInRRDDef;

    private int orderBy = FilterHost.NO_ORDER;

    private PagingParams paging;

    public boolean isWithService() {
        return withService;
    }

    public void setWithService(boolean withService) {
        this.withService = withService;
    }

    public boolean isWithServiceData() {
        return withServiceData;
    }

    public void setWithServiceData(boolean withServiceData) {
        this.withServiceData = withServiceData;
    }

    public PagingParams getPaging() {
        return paging;
    }

    public void setPaging(PagingParams paging) {
        this.paging = paging;
    }

    public String toString() {
        final String TAB = "    ";
        StringBuffer retValue = new StringBuffer();
        retValue.append("FilterHost [ ").append(super.toString()).append(TAB).append("withService = ").append(withService).append(TAB).append("withServiceData = ").append(withServiceData).append(TAB).append("paging = ").append(paging).append(TAB).append("orderBy = ").append(orderBy).append(TAB).append(TAB).append(" ]");
        return retValue.toString();
    }

    public boolean isOnlyServiceInRRDDef() {
        return onlyServiceInRRDDef;
    }

    public void setOnlyServiceInRRDDef(boolean onlyServiceInRRDDef) {
        this.onlyServiceInRRDDef = onlyServiceInRRDDef;
    }
}
