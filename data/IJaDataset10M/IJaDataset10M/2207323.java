package wsmg.demo;

import java.net.*;
import org.exolab.jms.util.*;
import wsmg.*;

/**
 * <p>Title: </p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2004</p>

 * <p>Company: </p>

 * @author Yi Huang

 * @version 1.0

 */
public class TestWSE_Publisher {

    public static void main(String[] args) throws Exception {
        CommandLine cmdline = new CommandLine(args);
        String consumer = cmdline.value("consumer", "localhost:12345");
        String topic = cmdline.value("topic", null);
        String message = cmdline.value("message", "Hello World!");
        WseClientAPI client = new WseClientAPI();
        client.publish(consumer, topic, message);
    }
}
