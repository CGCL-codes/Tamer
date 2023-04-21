package org.zoolib.net;

import org.zoolib.ZDebug;
import org.zoolib.stream.ZStreamIO;
import org.zoolib.thread.ZMutex;
import org.zoolib.thread.ZCondition;
import java.io.InputStream;
import java.io.OutputStream;

public class ZClient {

    public interface Handler {

        public ZStreamIO establishConnection();

        public boolean reader(InputStream iInputStream);

        public boolean writer(OutputStream iOutputStream);

        public void notify_Starting();

        public void notify_Established();

        public void notify_Failed();

        public void notify_Dead();
    }

    public ZClient(Handler iHandler) {
        ZDebug.sAssert(null != iHandler);
        fHandler = iHandler;
    }

    public final void start() {
        fMutex.acquire();
        ZDebug.sAssert(null == fStreamIO);
        ZDebug.sAssert(!fReaderThreadRunning);
        ZDebug.sAssert(!fReaderOkay);
        ZDebug.sAssert(!fWriterStarted);
        ZDebug.sAssert(!fWriterExited);
        ZDebug.sAssert(!fForceExit);
        fReaderThreadRunning = true;
        Runnable theRunnable = new Runnable() {

            public void run() {
                pRunReader();
            }
        };
        (new Thread(theRunnable)).start();
        fMutex.release();
    }

    public final void stop() {
        fMutex.acquire();
        fForceExit = true;
        fCondition.broadcast();
        if (null != fStreamIO) {
            try {
                fStreamIO.getInputStream().close();
            } catch (Throwable t) {
            }
            try {
                fStreamIO.getOutputStream().close();
            } catch (Throwable t) {
            }
        }
        while (fReaderThreadRunning) fCondition.wait(fMutex);
        fForceExit = false;
        fMutex.release();
    }

    private final void pRunReader() {
        fMutex.acquire();
        fHandler.notify_Starting();
        while (!fForceExit) {
            ZDebug.sAssert(null == fStreamIO);
            ZDebug.sAssert(!fWriterStarted);
            ZDebug.sAssert(!fWriterExited);
            fStreamIO = fHandler.establishConnection();
            if (null == fStreamIO) {
                break;
            }
            fReaderOkay = true;
            fHandler.notify_Established();
            Runnable theRunnable = new Runnable() {

                public void run() {
                    pRunWriter();
                }
            };
            (new Thread(theRunnable)).start();
            while (!fWriterStarted) fCondition.wait(fMutex);
            while (!fForceExit) {
                fMutex.release();
                try {
                    if (!fHandler.reader(fStreamIO.getInputStream())) break;
                } catch (Exception ex) {
                    break;
                } finally {
                    fMutex.acquire();
                }
            }
            fReaderOkay = false;
            fCondition.broadcast();
            fHandler.notify_Failed();
            while (!fWriterExited) fCondition.wait(fMutex);
            fWriterStarted = false;
            fWriterExited = false;
            fStreamIO = null;
        }
        fHandler.notify_Dead();
        fReaderThreadRunning = false;
        fCondition.broadcast();
        fMutex.release();
    }

    private final void pRunWriter() {
        fMutex.acquire();
        fWriterStarted = true;
        fCondition.broadcast();
        while (!fForceExit && fReaderOkay) {
            fMutex.release();
            try {
                if (!fHandler.writer(fStreamIO.getOutputStream())) break;
            } catch (Exception ex) {
                break;
            } finally {
                fMutex.acquire();
            }
        }
        fWriterExited = true;
        fCondition.broadcast();
        fMutex.release();
    }

    private ZStreamIO fStreamIO;

    private final Handler fHandler;

    private final ZMutex fMutex = new ZMutex();

    private final ZCondition fCondition = new ZCondition();

    private boolean fReaderThreadRunning = false;

    private boolean fReaderOkay = false;

    private boolean fWriterStarted = false;

    private boolean fWriterExited = false;

    private boolean fForceExit = false;
}
