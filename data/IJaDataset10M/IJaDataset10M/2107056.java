package satmule.domain;

import java.net.*;
import java.io.*;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import satmule.persistence.FpHash;

/**
 * 
 * @author JMartinC
 *  
 * Standalone launcher of satmule server.
 * 
 */
public class TcpServer implements Runnable {

    static Log log = LogFactory.getLog(TcpServer.class);

    protected int portNumber;

    /***************************************************************************
	 * Creates a new instance of ChunkConsumer
	 */
    public TcpServer(int portNumber) {
        this.portNumber = portNumber;
    }

    public void run() {
        ServerSocket chunkConsumerService = null;
        Vector readers = new Vector();
        try {
            chunkConsumerService = new ServerSocket(portNumber);
            Socket socketService = null;
            Buffer buffer = Context.getTcpBuffer();
            TcpServant sirviente = new TcpServant(buffer);
            ChunkWriter cw1 = new ChunkWriter(Context.getChunkBuffer());
            ChunkWriter cw2 = new ChunkWriter(Context.getChunkBuffer());
            ChunkWriter cw3 = new ChunkWriter(Context.getChunkBuffer());
            Thread tcw1 = new Thread(cw1);
            Thread tcw2 = new Thread(cw2);
            Thread tcw3 = new Thread(cw3);
            tcw1.start();
            tcw2.start();
            tcw3.start();
            FileNameWriter fnw = new FileNameWriter(Context.getFileNameBuffer());
            Thread tfnw = new Thread(fnw);
            tfnw.start();
            Thread threadSirviente = new Thread(sirviente);
            threadSirviente.start();
            while (true) {
                log.info("waiting client connection");
                socketService = chunkConsumerService.accept();
                log.info("client connection received");
                TcpMessageReader reader = new TcpMessageReader(socketService, buffer);
                Thread threadReader = new Thread(reader);
                threadReader.setPriority(2);
                threadReader.start();
            }
        } catch (IOException e) {
            log.warn(e);
        }
    }

    public static void main(String args[]) {
        System.out.println("test ChunkConsumer");
        TcpServer ej = new TcpServer(8888);
        ej.run();
    }
}
