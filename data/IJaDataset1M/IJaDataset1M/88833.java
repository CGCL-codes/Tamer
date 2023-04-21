package watij.dialogs;

public interface PrintDialog extends Dialog {

    void cancel() throws Exception;

    void print() throws Exception;

    void setNumberOfCopies(boolean colate) throws Exception;

    void setPageRange() throws Exception;

    void choosePrinterAtIndex(int index) throws Exception;
}
