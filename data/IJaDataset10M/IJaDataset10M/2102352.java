package com.technoetic.dof.examples.socket;

import java.net.InetSocketAddress;
import com.technoetic.dof.examples.ejb.StockQuoteService;
import com.technoetic.dof.transport.DistributedObjectProxyFactory;
import com.technoetic.dof.transport.security.UUIDAuthenticationTokenSource;
import com.technoetic.dof.transport.socket.SocketTransport;

public class SocketClient {

    private static final int PORT = 8888;

    public static void main(String[] args) throws Exception {
        SocketTransport transport = new SocketTransport(new InetSocketAddress(PORT));
        StockQuoteService quoteService = DistributedObjectProxyFactory.create(new Class<?>[] { StockQuoteService.class }, SocketServer.STOCK_QUOTE_SERVICE_ID, transport, new UUIDAuthenticationTokenSource());
        System.out.println("Quote: " + quoteService.getQuote("IBM"));
    }
}
