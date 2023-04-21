package client.interfaces;

import java.io.IOException;

public interface PrinterInterface {

    public void displayError(String msg);

    public void log(String msg) throws IOException;

    public void close() throws IOException;
}
