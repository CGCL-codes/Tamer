package br.unb.unbiquitous.ubiquitos.uos.ontologyEngine.exception;

/**
 *
 * @author anaozaki
 */
public class RedundancyException extends Exception {

    String cls;

    String subCls;

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getSubCls() {
        return subCls;
    }

    public void setSubCls(String subCls) {
        this.subCls = subCls;
    }

    public RedundancyException() {
        super();
    }

    public RedundancyException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedundancyException(String message, String subCls, String cls) {
        super(message);
        this.cls = cls;
        this.subCls = subCls;
    }

    public RedundancyException(Throwable cause) {
        super(cause);
    }
}
