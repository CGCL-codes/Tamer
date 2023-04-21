package org.hooliguns.ninja.telnet.teensyninja.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/**
 * A simple abstract class that provides test framework for TeensyNinja modded Ninja.
 * 
 * @author Manish Pandya (March 1 2009)
 * 
 */
public abstract class TestFrameworkForTeensyNinja {

    /**
   * max x pisition
   */
    protected static final int XMAX = 1800;

    /**
   * min x position
   */
    protected static final int XMIN = -1800;

    /**
   * max y position
   */
    protected static final int YMAX = 550;

    /**
   * min y position
   */
    protected static final int YMIN = -1200;

    /**
   * maximum velocity
   */
    protected static final int VMAX = 63;

    /**
   * minimum velocity
   */
    protected static final int VMIN = 0;

    /**
   * Should carry on? false will quit movement thread.
   */
    protected volatile boolean keepOn = true;

    /**
   * The terminal output stream that we print co-ordinates to.
   */
    protected OutputStream out = null;

    /**
   * The terminal input stream that we read response from.
   */
    protected InputStream in = null;

    /**
   * the shutdown hook
   */
    protected ShutdownHook shook = null;

    /**
   * The initialization method that sets up the comm port, adds shutdown hook and starts the random
   * movement thread. It uses gnu.io from RXTX.org but can readily converted to use javax.comm API.
   * 
   * @param portName
   *          the com port
   * @throws Exception
   *           when bad things happen
   */
    void connect(String portName, Runnable task) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                shook = new ShutdownHook(this);
                (new Thread(task)).start();
            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    /**
   * Method that checks if the Ninja unit is moving.
   * 
   * @param out
   *          the com port output stream where command for ninja is to be written
   * @param in
   *          the com port input stream where responce from ninja is to be read from
   * @return true if unit is moving, false otherwise
   */
    protected static boolean isInMotion(OutputStream out, InputStream in) {
        String status = new String(executeAndGetResponse("?", out, in));
        int pos = status.indexOf("IS_HALTED=");
        return '0' == status.charAt(pos + 10);
    }

    /**
   * A method that writes the randomly generated commands to the serial port and waits for about 6
   * seconds before returning.
   * 
   * @param command
   *          the command to be written
   * @param out
   *          the com port output stream where command for ninja is to be written
   * @param in
   *          the com port input stream where responce from ninja is to be read from
   */
    protected static byte[] executeAndGetResponse(String command, OutputStream out, InputStream in) {
        byte[] retval = new byte[0];
        try {
            if (out != null) {
                out.write(command.getBytes());
                out.write("\r".getBytes());
                out.flush();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                int availCount = in.available();
                if (availCount > 0) {
                    retval = new byte[availCount];
                    int readcount = 0;
                    while (readcount != availCount) {
                        readcount += in.read(retval, readcount, availCount - readcount);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retval;
    }

    /**
   * The shutdown hook that breaks the infinite loop of execution
   * 
   * @author Manish Pandya (March 1 2009)
   * 
   */
    protected class ShutdownHook extends Thread {

        /**
     * the test instance
     */
        TestFrameworkForTeensyNinja tftn;

        /**
     * A method that breaks the execution loop and centers the unit as last execution
     * 
     * @param rms
     *          the instance of the running test
     */
        protected ShutdownHook(TestFrameworkForTeensyNinja rms) {
            this.tftn = rms;
        }

        @Override
        public void run() {
            System.out.println("Shut down requested. Shutting down...");
            keepOn = false;
            String retstr = new String(executeAndGetResponse("c", tftn.out, tftn.in));
            System.out.println(retstr);
            while (isInMotion(tftn.out, tftn.in)) {
                try {
                    Thread.sleep(1200);
                    System.out.print('.');
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
