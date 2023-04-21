package net.java.nioserver.sample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import net.java.nioserver.BasicService;
import net.java.nioserver.aio.AioService;
import net.java.nioserver.utils.NonBlockingByteBufferPool;

/**
 * @author Leonid Shlyapnikov
 */
public class AioSampleMain {

    public static void main(String argv[]) throws IOException {
        AioService.Builder builder = new AioService.Builder();
        final NonBlockingByteBufferPool byteBufferPool = new NonBlockingByteBufferPool(4, 20, true);
        builder.address(new InetSocketAddress(1975)).executor(Executors.newFixedThreadPool(4)).byteBufferPool(byteBufferPool).opRead(new EchoOpRead(byteBufferPool)).opAccept(new ConnectedOpAccept());
        BasicService poller = builder.build();
        poller.start();
    }
}
