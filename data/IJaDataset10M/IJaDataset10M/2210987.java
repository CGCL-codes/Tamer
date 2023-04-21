package org.otfeed.protocol.connector;

import org.junit.Test;
import org.otfeed.*;
import org.otfeed.command.ListExchangesCommand;
import org.otfeed.command.ListSymbolEnum;
import org.otfeed.command.ListSymbolsCommand;
import org.otfeed.event.ICompletionDelegate;
import org.otfeed.event.IConnectionStateListener;
import org.otfeed.event.IDataDelegate;
import org.otfeed.event.OTError;
import org.otfeed.event.OTExchange;
import org.otfeed.event.OTHost;
import org.otfeed.event.OTSymbol;
import static org.junit.Assert.*;

/**
 * Unit test for {@link org.otfeed.OTConnectionFactory}
 */
public class ConnectionFactoryTest {

    private String username;

    private String password;

    private String host;

    private int port;

    public void setup() throws Exception {
        username = System.getProperty("otfeed.username");
        password = System.getProperty("otfeed.password");
        host = System.getProperty("otfeed.host");
        port = Integer.parseInt(System.getProperty("otfeed.port"));
        assertTrue(username != null);
        assertTrue(password != null);
        assertTrue(host != null);
    }

    private final IConnection connect() throws Exception {
        setup();
        OTConnectionFactory factory = new OTConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        factory.getHosts().add(new OTHost(host, port));
        IConnection client = factory.connect(null, new IConnectionStateListener() {

            public void onConnecting(OTHost host) {
                System.out.println("Connecting to: " + host);
            }

            public void onConnected() {
                System.out.println("Connected!");
            }

            public void onLogin() {
                System.out.println("Logged in!");
            }

            public void onRedirect(OTHost host) {
                System.out.println("Redirect: " + host);
            }

            public void onError(OTError err) {
                System.out.println("Error: " + err);
            }
        });
        return client;
    }

    @Test
    public void testDummy() {
    }

    public void testConnectionFailure() throws Exception {
        setup();
        OTConnectionFactory factory = new OTConnectionFactory();
        factory.setUsername("aa");
        factory.setPassword("aa");
        factory.getHosts().add(new OTHost("aaa.bbb.ccc", 10010));
        IConnection client = factory.connect(null, new IConnectionStateListener() {

            public void onConnecting(OTHost host) {
                System.out.println("Connecting to: " + host);
            }

            public void onConnected() {
                System.out.println("Connected!");
            }

            public void onLogin() {
                System.out.println("Logged in!");
            }

            public void onRedirect(OTHost host) {
                System.out.println("Redirect: " + host);
            }

            public void onError(OTError err) {
                System.out.println("Error: " + err);
            }
        });
        client.waitForCompletion();
    }

    public void testLoginFailure() throws Exception {
        setup();
        OTConnectionFactory factory = new OTConnectionFactory();
        factory.setUsername("aa");
        factory.setPassword("aa");
        factory.getHosts().add(new OTHost(host, port));
        IConnection client = factory.connect(null, new IConnectionStateListener() {

            public void onConnecting(OTHost host) {
                System.out.println("Connecting to: " + host);
            }

            public void onConnected() {
                System.out.println("Connected!");
            }

            public void onLogin() {
                System.out.println("Logged in!");
            }

            public void onRedirect(OTHost host) {
                System.out.println("Redirect: " + host);
            }

            public void onError(OTError err) {
                System.out.println("Error: " + err);
            }
        });
        client.waitForCompletion();
    }

    public void testLoginSuccess() throws Exception {
        IConnection client = connect();
        Thread.sleep(15000);
        client.shutdown();
        client.waitForCompletion();
    }

    public void testCancel() throws Exception {
        IConnection connection = connect();
        ListSymbolsCommand command = new ListSymbolsCommand();
        command.setExchangeCode("A");
        command.setTypes(ListSymbolEnum.ALL);
        command.setDataDelegate(new IDataDelegate<OTSymbol>() {

            public void onData(OTSymbol symbol) {
                System.out.println(symbol);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
            }
        });
        command.setCompletionDelegate(new ICompletionDelegate() {

            public void onDataEnd(OTError error) {
                if (error != null) {
                    System.out.println("Err: " + error);
                } else {
                    System.out.println("no more data");
                }
            }
        });
        IRequest request = connection.prepareRequest(command);
        request.submit();
        Thread.sleep(10000);
        request.cancel();
        request.waitForCompletion();
        System.out.println("request finished");
        Thread.sleep(10000);
        connection.shutdown();
        connection.waitForCompletion();
        System.out.println("connection closed");
    }

    public void testListExchanges() throws Exception {
        IConnection connection = connect();
        ListExchangesCommand command = new ListExchangesCommand();
        command.setDataDelegate(new IDataDelegate<OTExchange>() {

            public void onData(OTExchange exchange) {
                System.out.println(exchange);
            }
        });
        command.setCompletionDelegate(new ICompletionDelegate() {

            public void onDataEnd(OTError error) {
                if (error != null) {
                    System.out.println("Err: " + error);
                } else {
                    System.out.println("no more data");
                }
            }
        });
        IRequest request = connection.prepareRequest(command);
        request.submit();
        request.waitForCompletion();
        System.out.println("request finished");
        connection.shutdown();
        connection.waitForCompletion();
        System.out.println("connection closed");
    }
}
