package org.privale.utils.network.queuetest;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.Random;
import org.privale.utils.ChannelWriter;
import org.privale.utils.FileManager;
import org.privale.utils.network.ChannelProcessor;
import org.privale.utils.network.FactoryAttachment;
import org.privale.utils.network.LongFileSendTrans;
import org.privale.utils.network.LongSendTrans;
import org.privale.utils.network.NetSelector;
import org.privale.utils.network.ProcessorFactory;
import org.privale.utils.network.ProcessorPool;
import org.privale.utils.network.QueueHandler;
import org.privale.utils.network.QueueProcessor;
import org.privale.utils.network.SocketChannelHandler;
import org.privale.utils.network.SocketHandlerFactory;
import org.privale.utils.network.ThreadPool;

public class Client implements SocketHandlerFactory, ProcessorFactory {

    protected static long MaxLen = 100000;

    protected ProcessorPool PPool;

    protected ThreadPool TPool;

    protected NetSelector Sel;

    protected Random RandMan;

    protected FileManager FM;

    protected LinkedList<QueueHandler> Handlers;

    public Client() {
        PPool = new ProcessorPool(this);
        TPool = new ThreadPool();
        PPool.setMaxProcessors(10);
        TPool.setMaxThreads(10);
        Sel = new NetSelector();
        Handlers = new LinkedList<QueueHandler>();
        RandMan = new Random();
    }

    public File BuildFile() throws IOException {
        long len = (long) ((float) MaxLen * RandMan.nextFloat());
        len = len / (Long.SIZE / Byte.SIZE);
        File f = FM.createNewFile("tmp", "tmp");
        ChannelWriter cw = new ChannelWriter(f);
        for (int cnt = 0; cnt < len; cnt++) {
            cw.putLong(RandMan.nextLong());
        }
        cw.close();
        return f;
    }

    public void Init() throws IOException {
        FM = FileManager.getDir("client");
        Sel.Init(TPool);
        Sel.Go();
    }

    public SocketChannelHandler NewAcceptHandler(SelectionKey key) {
        return null;
    }

    public void ConnectTimedOut(Object connectref) {
    }

    public synchronized SocketChannelHandler NewConnectionHandler(SelectionKey key) {
        QueueHandler hand = new QueueHandler(key, Sel);
        DefaultTrans t = new DefaultTrans(hand);
        hand.setDefault(t);
        Handlers.add(hand);
        return hand;
    }

    public ChannelProcessor NewProcessor() {
        QueueProcessor p = new QueueProcessor(PPool);
        return p;
    }

    public void ConnectTo(String addr, int port) throws IOException {
        FactoryAttachment fat = new FactoryAttachment(Sel, this, PPool, null);
        Sel.ConnectTo(addr, port, fat);
    }

    public synchronized void Dispatch(long value) {
        for (int cnt = 0; cnt < Handlers.size(); cnt++) {
            QueueHandler hand = Handlers.get(cnt);
            long v = value++;
            ExpectTrans e = new ExpectTrans(hand, null, v);
            LongSendTrans s = new LongSendTrans(hand, e, v);
            hand.QueueTransaction(s);
        }
    }

    public void DispatchFiles() throws IOException {
        for (int cnt = 0; cnt < Handlers.size(); cnt++) {
            QueueHandler hand = Handlers.get(cnt);
            File outf = BuildFile();
            FileExpectTrans e = new FileExpectTrans(hand, null, outf, FM);
            LongFileSendTrans s = new LongFileSendTrans(hand, e, outf);
            hand.QueueTransaction(s);
        }
    }

    public void Stop() {
        Sel.Stop();
        TPool.Stop();
    }

    public static void main(String[] args) {
        Client c = new Client();
        try {
            c.Init();
            System.out.println("Connecting!");
            c.ConnectTo("127.0.0.1", 1234);
            c.ConnectTo("127.0.0.1", 1234);
            c.ConnectTo("127.0.0.1", 1234);
            c.ConnectTo("127.0.0.1", 1234);
            c.ConnectTo("127.0.0.1", 1234);
            c.ConnectTo("127.0.0.1", 1234);
            c.ConnectTo("127.0.0.1", 1234);
            c.ConnectTo("127.0.0.1", 1234);
            c.ConnectTo("127.0.0.1", 1234);
            System.out.println("Connected!");
            Thread.sleep(2000);
            System.out.println("SENDING!");
            c.DispatchFiles();
            System.out.println("Sleeping!");
            Thread.sleep(60000);
            c.Stop();
            System.out.println("STOPPED!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
