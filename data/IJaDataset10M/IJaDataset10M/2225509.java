package net.cattaka.rdbassistant.driver.dummy;

import net.cattaka.rdbassistant.config.RdbaConnectionInfo;
import net.cattaka.rdbassistant.config.RdbaJdbcBundle;
import net.cattaka.rdbassistant.core.RdbaException;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.gui.connectioninfo.RdbaConnectionInfoEditor;
import net.cattaka.util.ExceptionHandler;

public class DummyRdbaConnectionInfo implements RdbaConnectionInfo {

    private static final long serialVersionUID = 1L;

    private String label = "";

    public String getRdbmsName() {
        return "Dummy";
    }

    public String toUrl() {
        return "dummy";
    }

    public RdbaConnection createConnection(RdbaJdbcBundle rdbaJdbcBundle) throws RdbaException {
        DummyRdbaConnection rdbaConnection = new DummyRdbaConnection(label);
        return rdbaConnection;
    }

    public String[] getDisplayStrings() {
        return new String[] { "Dummy", label, "", "" };
    }

    public String getTooltipText() {
        return label;
    }

    public RdbaConnectionInfoEditor createEditor() {
        return new DummyRdbaConnectionInfoEditor();
    }

    public String[] toStringArray() {
        String[] result = { getClass().getCanonicalName(), "1", label };
        return result;
    }

    public boolean restoreStringArray(String[] stringArray, int configRevision) {
        boolean result = false;
        if (stringArray[1].equals("1") && stringArray.length == 3) {
            label = stringArray[2];
            result = true;
        }
        return result;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public DummyRdbaConnectionInfo createClone() {
        try {
            return (DummyRdbaConnectionInfo) this.clone();
        } catch (CloneNotSupportedException e) {
            ExceptionHandler.error(e);
            return new DummyRdbaConnectionInfo();
        }
    }
}
