package com.peterhi;

import com.sun.grizzly.Context;
import com.sun.grizzly.Controller;
import com.sun.grizzly.DefaultProtocolChain;
import com.sun.grizzly.DefaultProtocolChainInstanceHandler;
import com.sun.grizzly.ProtocolChain;
import com.sun.grizzly.ProtocolFilter;
import com.sun.grizzly.TCPSelectorHandler;
import com.sun.grizzly.filter.ReadFilter;
import com.sun.grizzly.util.OutputWriter;
import com.sun.grizzly.util.WorkerThread;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;

/**
 *
 * @author YUN TAO
 */
public class TcpServer2 {

    public static void main(String[] args) throws Exception {
        Controller controller = new Controller();
        TCPSelectorHandler selector = new TCPSelectorHandler();
        selector.setPort(9080);
        final ProtocolFilter myFilter = new MyFilter();
        controller.setProtocolChainInstanceHandler(new DefaultProtocolChainInstanceHandler() {

            public ProtocolChain poll() {
                ProtocolChain protocolChain = protocolChains.poll();
                if (protocolChain == null) {
                    protocolChain = new DefaultProtocolChain();
                    protocolChain.addFilter(new ReadFilter());
                    protocolChain.addFilter(myFilter);
                }
                return protocolChain;
            }
        });
        controller.setSelectorHandler(selector);
        controller.start();
    }

    public static class MyFilter implements ProtocolFilter {

        public boolean execute(Context ctx) throws IOException {
            final WorkerThread workerThread = ((WorkerThread) Thread.currentThread());
            String message = "";
            ByteBuffer buffer = workerThread.getByteBuffer();
            buffer.flip();
            if (buffer.hasRemaining()) {
                byte[] data = new byte[buffer.remaining()];
                int position = buffer.position();
                buffer.get(data);
                buffer.position(position);
                message = new String(data);
            }
            SelectableChannel channel = ctx.getSelectionKey().channel();
            if (ctx.getProtocol() == Controller.Protocol.TCP) {
                OutputWriter.flushChannel(channel, buffer);
                System.out.println("server written");
            }
            System.out.println("New message being read, message is: " + message);
            buffer.clear();
            return false;
        }

        public boolean postExecute(Context ctx) throws IOException {
            return true;
        }
    }
}
