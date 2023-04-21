package agorum.blender.yadra.master;

import java.net.Socket;
import agorum.blender.yadra.common.ComUtils;
import agorum.blender.yadra.common.Config;
import agorum.blender.yadra.common.CryptInputStreamWrapper;
import agorum.blender.yadra.common.CryptOutputStreamWrapper;

/**
 * 
 * @author oliver.schulze
 */
public class MasterHandler extends Thread {

    private Config config = null;

    private Socket socket = null;

    /** Creates a new instance of Main */
    public MasterHandler(Config config, Socket socket) {
        this.config = config;
        this.socket = socket;
    }

    /**
	 * start the masterServer
	 */
    public void run() {
        try {
            CryptInputStreamWrapper cis = new CryptInputStreamWrapper(socket.getInputStream(), config);
            CryptOutputStreamWrapper cos = new CryptOutputStreamWrapper(socket.getOutputStream(), config);
            ComUtils comUtils = new ComUtils(config);
            comUtils.receiveCommand(socket, cis, cos);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
            }
        }
    }
}
