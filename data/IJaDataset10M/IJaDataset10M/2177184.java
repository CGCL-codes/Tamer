package org.knowhowlab.osgi.monitoradmin.util;

/**
 * StatusVariable path filter with '*' wildcard support
 *
 * @author dmytro.pishchukhin
 */
public class StatusVariablePathFilter extends StatusVariablePath {

    private boolean monitorableWildcard = false;

    private boolean statusVariableWildcard = false;

    private String monitorableIdFilter;

    public StatusVariablePathFilter(String path) throws IllegalArgumentException {
        String[] ids = parseIds(path);
        this.path = path;
        monitorableIdFilter = ids[0];
        if (monitorableIdFilter.indexOf('*') != -1) {
            monitorableId = monitorableIdFilter.replace("*", "");
            monitorableWildcard = true;
        } else {
            monitorableId = ids[0];
        }
        if (ids[1].indexOf('*') != -1) {
            statusVariableId = ids[1].replace("*", "");
            statusVariableWildcard = true;
        } else {
            statusVariableId = ids[1];
        }
    }

    @Override
    protected boolean validateId(String id) {
        return Utils.validatePathFilterId(id);
    }

    /**
     * Check that given monitorable Id and StatusVarialbe Id match Filter
     * @param monitorableId monitorable Id
     * @param statusVariableId StatusVariable Id
     * @return result
     */
    public boolean match(String monitorableId, String statusVariableId) {
        return (monitorableWildcard ? monitorableId.startsWith(this.monitorableId) : monitorableId.equals(this.monitorableId)) && (statusVariableWildcard ? statusVariableId.startsWith(this.statusVariableId) : statusVariableId.endsWith(this.statusVariableId));
    }

    public String getMonitorableIdFilter() {
        return monitorableIdFilter;
    }
}
