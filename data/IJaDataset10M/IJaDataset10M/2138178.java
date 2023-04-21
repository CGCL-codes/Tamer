package net.java.nioserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.java.nioserver.aio.AioService;
import net.java.nioserver.sample.ConnectedOpAccept;
import net.java.nioserver.sample.EchoOpRead;
import net.java.nioserver.sio.SioService;
import net.java.nioserver.utils.NonBlockingByteBufferPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Leonid Shlyapnikov
 */
public class BasicServiceTest {

    private static final Logger log = LoggerFactory.getLogger(BasicServiceTest.class);

    private ExecutorService executor;

    private static final long ONE_SECOND = 1000L;

    private static final long THREE_SECONDS = 3000L;

    private static final int PORT = 1975;

    private final List<Exception> exceptionList = Collections.synchronizedList(new ArrayList<Exception>());

    private boolean testFinished = false;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        testFinished = false;
        exceptionList.clear();
        executor = Executors.newFixedThreadPool(1);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() throws InterruptedException {
        Assert.assertTrue(testFinished);
        if (exceptionList.size() > 0) {
            Assert.fail("Errors: " + exceptionList);
        }
        executor.shutdown();
        executor.shutdownNow();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    @DataProvider(name = "3servers")
    public Object[][] getAioPollers() {
        AioService.Builder aioBuilder = new AioService.Builder();
        final NonBlockingByteBufferPool blockingByteBufferPool = new NonBlockingByteBufferPool(2, 20, true);
        aioBuilder.executor(Executors.newFixedThreadPool(2)).byteBufferPool(blockingByteBufferPool).address(new InetSocketAddress(PORT)).opRead(new EchoOpRead(blockingByteBufferPool));
        BasicService multiThreadedPoller = aioBuilder.build();
        BasicService singleThreadedPoller = aioBuilder.executor(null).build();
        SioService.Builder sioBuilder = new SioService.Builder();
        final NonBlockingByteBufferPool byteBufferPool = new NonBlockingByteBufferPool(2, 20, true);
        sioBuilder.executor(Executors.newFixedThreadPool(2)).byteBufferPool(byteBufferPool).address(new InetSocketAddress(PORT)).opRead(new EchoOpRead(byteBufferPool));
        BasicService sioServer = sioBuilder.build();
        return new BasicService[][] { { multiThreadedPoller }, { singleThreadedPoller }, { sioServer } };
    }

    @DataProvider(name = "onlySioServer")
    public Object[][] getOnlySioServer() {
        SioService.Builder sioBuilder = new SioService.Builder();
        final NonBlockingByteBufferPool blockingByteBufferPool = new NonBlockingByteBufferPool(2, 20, true);
        sioBuilder.executor(Executors.newFixedThreadPool(2)).byteBufferPool(blockingByteBufferPool).address(new InetSocketAddress(PORT)).opRead(new EchoOpRead(blockingByteBufferPool));
        BasicService sioServer = sioBuilder.build();
        return new BasicService[][] { { sioServer } };
    }

    @Test(groups = "functional", dataProvider = "3servers")
    public void testShouldStartAndStop(final BasicService server) throws InterruptedException {
        log.info("poller: " + server);
        startServerWaitItIsUpAndAssert(server);
        stopServerWaitItIsDownAndAssert(server);
        testFinished = true;
    }

    private void stopServerWaitItIsDownAndAssert(BasicService server) throws InterruptedException {
        server.stop(THREE_SECONDS);
        for (int indx = 0; indx < 15 && server.isRuning(); indx++) {
            log.info("wating for shutdown, indx: " + indx);
            Thread.sleep(ONE_SECOND);
        }
        Assert.assertFalse(server.isRuning());
    }

    private void startServerWaitItIsUpAndAssert(final BasicService server) throws InterruptedException {
        Assert.assertFalse(server.isRuning());
        executor.execute(new Runnable() {

            public void run() {
                try {
                    server.start();
                } catch (Exception e) {
                    exceptionList.add(e);
                }
            }
        });
        for (int indx = 0; indx < 15 && !server.isRuning(); indx++) {
            log.info("wating for startup, indx: " + indx);
            Thread.sleep(ONE_SECOND);
        }
        Assert.assertTrue(server.isRuning());
    }

    @Test(groups = "functional", dataProvider = "3servers", dependsOnMethods = "testShouldStartAndStop")
    public void testShouldSendAndReceiveOneEchoedMessages(final BasicService server) throws InterruptedException, IOException {
        log.info("\nserver: " + server + "\n");
        startServerWaitItIsUpAndAssert(server);
        Socket socket = new Socket("localhost", PORT);
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        final String sentMessage = "sentMessage\n";
        output.write(sentMessage);
        output.flush();
        final String readMessage = input.readLine() + '\n';
        socket.close();
        log.info("readMessage: " + readMessage);
        Assert.assertEquals(readMessage, sentMessage);
        stopServerWaitItIsDownAndAssert(server);
        testFinished = true;
    }

    @Test(groups = "functional")
    public void testSioServerShouldProcessOpAccept() throws Exception {
        final String opAcceptResponse = "-->CONNECTED\n";
        SioService.Builder sioBuilder = new SioService.Builder();
        final NonBlockingByteBufferPool blockingByteBufferPool = new NonBlockingByteBufferPool(2, 20, true);
        sioBuilder.executor(Executors.newFixedThreadPool(2)).byteBufferPool(blockingByteBufferPool).address(new InetSocketAddress(PORT)).opRead(new EchoOpRead(blockingByteBufferPool)).opAccept(new ConnectedOpAccept(opAcceptResponse));
        BasicService server = sioBuilder.build();
        startServerWaitItIsUpAndAssert(server);
        Socket socket = new Socket("localhost", PORT);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        final String readMessage = input.readLine() + '\n';
        socket.close();
        log.info("readMessage: " + readMessage);
        Assert.assertEquals(readMessage, opAcceptResponse);
        stopServerWaitItIsDownAndAssert(server);
        testFinished = true;
    }
}
