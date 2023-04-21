package org.linubr.kernel.logging;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import org.linubr.kernel.util.FacadeCurrentTime;
import org.linubr.kernel.util.enumtype.KindOfExceptionET;

/**
 * This class implements a system of resources for logging. The
 * log messages will be added in file <code>linubr.log</code>
 * and shown in console. This class contains too a simple method
 * to debug messages. In the design pattern Command, this class
 * represents a ConcreteCommand. 
 * 
 * @author Guilherme Pontes <lgapontes@gmail.com>
 * @version last updated 18/04/2009
 */
public class SimpleLogger implements ILoggerCommand, Serializable {

    /**
	 * Attribute used in cases where the system is running in
	 * two or more J2EE servers.
	 */
    private static final long serialVersionUID = 689973103716381265L;

    /**
	 * Name of the log file.
	 */
    private String fileName;

    /**
	 * Constructor of the class.
	 */
    public SimpleLogger() {
        this.fileName = "linubr.log";
    }

    /**
	 * Method private to create a default log message.
	 * 
	 * @param message
	 *        Message that will show in log file and console.
	 * @param kind
	 *        Type of message that will show. The type of messages
	 *        are defined in class <code>KindOfExceptionET</code>.
	 * @return default message.
	 */
    private String createDefaultMessage(String message, KindOfExceptionET kind) {
        return "[!] " + kind.getDescription() + " :: " + FacadeCurrentTime.getCurrentTime() + " :: " + message + " [!]\n";
    }

    /**
	 * Method to show messages in console. For default, these messages
	 * will of the type <code>SEVERITY_INFOMATION</code>.
	 * 
	 * @param message
	 *        Message that will be displayed.
	 */
    @Override
    public void debug(String message) {
        System.err.print(this.createDefaultMessage(message, KindOfExceptionET.SEVERITY_INFOMATION));
    }

    /**
	 * Method that append the attribute <code>message</code> of the
	 * type <code>kind</code> in log file and in console of the
	 * system. If failures occur, a <code>printStackTrace()</code> of
	 * the exception will be displayed in console, only.
	 * 
	 * @param message
	 *        Message that will be displayed.
	 * @param kind
	 *        Type of the message.
	 */
    @Override
    public void append(String message, KindOfExceptionET kind) {
        try {
            OutputStream outputStream = new FileOutputStream(fileName, true);
            OutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(bufferedOutputStream, "UTF8");
            String msg = this.createDefaultMessage(message, kind);
            outputStreamWriter.write(msg);
            kind.getPrintStream().print(msg);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
