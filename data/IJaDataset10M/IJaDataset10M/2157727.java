package ti.plato.components.logger.feedback;

import ti.plato.components.logger.constants.LoggerConstants;
import ti.plato.components.logger.filters.FormulaFilteringManagement;
import ti.plato.shared.types.OnGetProgressInfoResult;

public class DiagnosticMonitorMain {

    private static DiagnosticMonitorMain diagnosticMonitorMain = new DiagnosticMonitorMain();

    public static DiagnosticMonitorMain getDefault() {
        return diagnosticMonitorMain;
    }

    public int getHandle() {
        return InterCommunication.getDefault().getExternalProcessor().getHandle(LoggerConstants.viewerTypeDM);
    }

    public void setCurrentFilterIndex(int handle, int index) {
        InterCommunication.getDefault().getExternalProcessor().setFilter(handle, index);
    }

    public static class OnGetRangeResult {

        public int rangeMin = -1;

        public int rangeMax = -1;

        public boolean eraseAll = false;
    }

    public OnGetRangeResult onGetRange(int handle) {
        OnGetRangeResult onGetRangeResult = new OnGetRangeResult();
        int[] range = InterCommunication.getDefault().getExternalProcessor().getRange(handle);
        onGetRangeResult.rangeMin = range[0];
        onGetRangeResult.rangeMax = range[1];
        return onGetRangeResult;
    }

    public String getName(int handle, int index) {
        return InterCommunication.getDefault().getExternalProcessor().getName(handle, index);
    }

    public long getTime(int handle, int index) {
        return InterCommunication.getDefault().getExternalProcessor().getTime(handle, index);
    }

    public Integer getTimeShiftCount(String[] arrayField) {
        return FormulaFilteringManagement.formula_helperGetTimeShiftCount(arrayField);
    }

    public double[][] getTimeShiftDelay(String[] arrayField, int timeShiftCount) {
        double[][] result = FormulaFilteringManagement.formula_helperGetTimeShiftDelay(arrayField);
        if (result == null) return null;
        if ((result[0] == null) && (result[1] == null)) {
            result = new double[2][];
            result[0] = new double[] { 1.0 };
            result[1] = null;
        } else if (result[1] != null && result[1].length == 0) {
            result = new double[2][];
            result[0] = new double[] { 1.0 };
            result[1] = null;
        }
        if (result[0] != null) return result;
        double[] extendedArray = new double[timeShiftCount];
        int timeShiftIndex;
        double lastDelta = 0.0;
        double delayLast = 0.0;
        for (timeShiftIndex = 0; timeShiftIndex < timeShiftCount; timeShiftIndex++) {
            double delayNow;
            if (timeShiftIndex < result[1].length) {
                delayNow = result[1][timeShiftIndex];
            } else {
                delayNow = delayLast + lastDelta;
            }
            extendedArray[timeShiftIndex] = delayNow;
            if (timeShiftIndex == 0) lastDelta = 1.0; else lastDelta = delayNow - delayLast;
            delayLast = delayNow;
        }
        result[1] = extendedArray;
        return result;
    }

    public void addMessageNameSpecification(int handle, String messageName) {
        InterCommunication.getDefault().getExternalProcessor().addMessageNameSpecification(handle, messageName);
    }

    public void removeMessageNameSpecification(int handle, String messageName) {
        InterCommunication.getDefault().getExternalProcessor().removeMessageNameSpecification(handle, messageName);
    }

    public long getNr(int handle, int index) {
        return InterCommunication.getDefault().getExternalProcessor().getNr(handle, index);
    }

    public int decompressIndex(int handle, int index) {
        return InterCommunication.getDefault().getExternalProcessor().decompressIndex(handle, index);
    }

    public int compressIndex(int handle, int index) {
        return InterCommunication.getDefault().getExternalProcessor().compressIndex(handle, index);
    }

    public int findCompressedIndexByTimestamp(int handle, long timestamp) {
        return InterCommunication.getDefault().getExternalProcessor().findCompressedIndexByTimestamp(handle, timestamp);
    }

    public boolean diagnosticMonitorEnabled(int handle, int index) {
        String trackName = InterCommunication.getDefault().getExternalProcessor().getName(handle, index);
        if (trackName == null) return false;
        String[][] fieldList = InterCommunication.getDefault().getExternalProcessor().getFieldList(handle, index, true);
        if (fieldList == null) return false;
        return true;
    }

    public void removeHandle(int handle) {
        InterCommunication.getDefault().getExternalProcessor().removeHandle(handle);
    }

    public void synchronize(int handle, boolean status) {
        InterCommunication.getDefault().getExternalProcessor().synchronize(handle, status);
    }

    public OnGetProgressInfoResult onGetProgressInfo(int handle) {
        return InterCommunication.getDefault().getExternalProcessor().onGetProgressInfo(handle);
    }
}
