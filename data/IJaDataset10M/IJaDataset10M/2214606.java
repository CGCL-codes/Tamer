package edu.psu.citeseerx.exec.com.tests;

import edu.psu.citeseerx.exec.com.ByteStreamConnectionPool;
import edu.psu.citeseerx.exec.com.ConnectionPool;
import edu.psu.citeseerx.exec.com.ConnectionPoolMonitor;
import edu.psu.citeseerx.exec.com.ObjectTransferConnection;
import edu.psu.citeseerx.exec.com.XStreamConnectionPool;
import java.net.*;
import java.util.concurrent.*;

/**
 * Container for corecom unit tests.
 * 
 * @author Isaac Councill
 *
 */
public class TestClient implements ConnectionPoolMonitor {

    ConnectionPool clientPool;

    static int counter = 0;

    static synchronized int getCount() {
        return counter++;
    }

    public void notifyFinalized(ConnectionPool pool) {
        System.out.println("ConnectionPool shut down cleanly.");
    }

    public void notifyShutdown(ConnectionPool pool) {
    }

    /**
     * Sets up a connection pool based on test configuration.
     */
    public TestClient() {
        try {
            InetAddress serverHost = InetAddress.getByName(Configuration.serverHost);
            switch(Configuration.TYPE) {
                case Configuration.BYTE_STREAM:
                    clientPool = new ByteStreamConnectionPool(serverHost, Configuration.serverPort, Configuration.expirationTime, Configuration.compress, Configuration.compressedBlockSize);
                    break;
                case Configuration.XSTREAM:
                    clientPool = new XStreamConnectionPool(serverHost, Configuration.serverPort, Configuration.expirationTime, Configuration.compress, Configuration.compressedBlockSize);
                    break;
            }
            clientPool.registerMonitor(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a connection from the pool, writes a request and receives
     * a reponse, checks response for validity, then returns the connection.
     * @param attempts number of attempts to make, allowing retry in case of errors.
     * method will return after the first successful completion.
     */
    public long simpleRequest(int attempts) {
        int ID = getCount();
        long now = System.currentTimeMillis();
        for (int i = 0; i < attempts; i++) {
            CommandContainer req = new CommandContainer();
            req.request.put(CommandContainer.requestKey, CommandContainer.requestVal);
            ObjectTransferConnection connection;
            try {
                connection = clientPool.leaseConnection(Configuration.leaseTime);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(ID + " failed to lease connection");
                break;
            }
            now = System.currentTimeMillis();
            try {
                connection.writeObject(req);
                CommandContainer res = (CommandContainer) connection.readObject();
                String resVal = (String) res.response.get(CommandContainer.responseKey);
                if (resVal.equals(CommandContainer.responseVal)) {
                } else {
                    System.out.println(ID + " invalid response: " + resVal);
                }
                return System.currentTimeMillis() - now;
            } catch (SocketTimeoutException e) {
                System.out.println(ID + " req timed out");
                clientPool.invalidate(connection);
            } catch (Exception e) {
                System.out.println(ID + " request failed");
                e.printStackTrace();
            } finally {
                clientPool.returnConnection(connection);
            }
        }
        System.out.println(ID + " req failed after " + attempts + " tries");
        return System.currentTimeMillis() - now;
    }

    public void loadedTestImpl(int numReqs, long delay) {
        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Configuration.clientPoolSize, Configuration.clientPoolSize, Long.MAX_VALUE, TimeUnit.NANOSECONDS, workQueue);
        threadPool.prestartAllCoreThreads();
        for (int i = 0; i < numReqs; i++) {
            threadPool.submit(new Runnable() {

                public void run() {
                    long latency = simpleRequest(3);
                    System.out.println(latency);
                }
            });
            try {
                Thread.currentThread().sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(3600, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientPool.shutdown();
    }

    public void shutdown() {
        System.out.println("Shutting down client.");
        clientPool.shutdown();
    }

    public static void simpleTest() {
        TestClient testClient = new TestClient();
        testClient.simpleRequest(1);
        testClient.shutdown();
    }

    public static void incrementalTest(int iterations) {
        TestClient testClient = new TestClient();
        for (int i = 0; i < 10; i++) {
            testClient.simpleRequest(1);
        }
        testClient.shutdown();
    }

    public void loadedTest() {
        int numReqs = 10000;
        loadedTestImpl(numReqs, 50);
    }

    public static void main(String args[]) {
        TestClient client = new TestClient();
        client.loadedTest();
    }
}
