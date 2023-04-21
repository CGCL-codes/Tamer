package net.sf.opendf.execution.transport;

public abstract class AbstractTransport implements Transport {

    @Override
    public abstract void createConnection() throws TransportException;

    @Override
    public boolean createConnection(int retries, long retryDelay) {
        int n = 0;
        while (n <= retries) {
            try {
                createConnection();
                return true;
            } catch (Exception e) {
            }
            try {
                wait(retryDelay);
            } catch (InterruptedException e) {
            }
        }
        return false;
    }
}
