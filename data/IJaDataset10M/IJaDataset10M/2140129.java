package net.sourceforge.quexec.proc;

import net.sourceforge.quexec.packet.chars.stream.CharPacketInputStream;
import net.sourceforge.quexec.packet.chars.stream.CharPacketOutputStream;
import net.sourceforge.quexec.packet.chars.stream.JmsReceiveCharPacketStream;
import net.sourceforge.quexec.packet.chars.stream.JmsSendCharPacketStream;
import net.sourceforge.quexec.util.SpringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Connect to a remotely running native process via JMS queues for process input
 * and output.
 * 
 * @author schickin
 *
 */
public class RemoteProcessStub implements ProcessCharPacketStreams {

    private static final Log log = LogFactory.getLog(RemoteProcessStub.class);

    private JmsSendCharPacketStream processInputStream;

    private JmsReceiveCharPacketStream processOutputStream;

    /**
	 * Set the input stream for the process input to be sent via a JMS queue.
	 */
    public void setProcessInputStream(JmsSendCharPacketStream inputStream) {
        this.processInputStream = inputStream;
    }

    public CharPacketOutputStream getProcessInput() {
        return this.processInputStream;
    }

    /**
	 * Set the output stream that is connected to a JMS queue
	 * from which the process output shall be read.
	 */
    public void setProcessOutputStream(JmsReceiveCharPacketStream outputStream) {
        this.processOutputStream = outputStream;
    }

    public CharPacketInputStream getProcessOutput() {
        return this.processOutputStream;
    }

    /**
	 * Change the JMS queue name of the queue which transmits the input for the
	 * native process.
	 * 
	 * Must be called before the process intput stream is actually used.
	 * 
	 * @param destName the name of the JMS queue
	 */
    public void adjustProcessInputDestinationName(String destName) {
        log.debug("adjusting input destination to '" + destName + "'");
        this.processInputStream.setDestinationName(destName);
    }

    /**
	 * Change the JMS queue name of the queue which transmits the output of the
	 * native process.
	 * 
	 * Must be called before the process output stream is actually used.
	 * 
	 * @param destName the name of the JMS queue
	 */
    public void adjustProcessOutputDestinationName(String destName) {
        log.debug("adjusting output destination to '" + destName + "'");
        this.processOutputStream.setDestinationName(destName);
    }

    private static ApplicationContext remoteStubContext = new ClassPathXmlApplicationContext(new String[] { SpringUtils.contextDefaultLocationFor(RemoteProcessStub.class), "/spring/jmsBase-context.xml", "/spring/beanPostProcess-context.xml" });

    /**
	 * Create an instance of type {@linkplain RemoteProcessStub} for a given
	 * remote process handle.
	 * 
	 * @param handle the handle of the remote process to which the stub shall
	 * connect
	 * @return the newly created process stub instance
	 */
    public static ProcessCharPacketStreams newInstance(AsyncProcessHandle handle) {
        RemoteProcessStub result = (RemoteProcessStub) remoteStubContext.getBean("quexec:remoteProcessStub");
        result.adjustProcessInputDestinationName(handle.getProcessInputQueueName());
        result.adjustProcessOutputDestinationName(handle.getProcessOutputQueueName());
        return result;
    }
}
