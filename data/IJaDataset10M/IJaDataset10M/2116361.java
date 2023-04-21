package net.sf.dropboxmq.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.NamingException;
import net.sf.dropboxmq.Configuration;
import net.sf.dropboxmq.destinations.DestinationImpl;
import net.sf.dropboxmq.destinations.QueueImpl;
import net.sf.dropboxmq.destinations.TopicImpl;
import net.sf.dropboxmq.dropboxsupport.DirectoryStructure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created: 15 Jun 2006
 * @author <a href="mailto:dwayne@schultz.net">Dwayne Schultz</a>
 * @version $Revision: 211 $, $Date: 2010-11-14 16:21:24 -0500 (Sun, 14 Nov 2010) $
 */
public class DropboxCreator {

    private static final Log log = LogFactory.getLog(DropboxCreator.class);

    private DropboxCreator() {
    }

    public static void main(final String[] args) throws NamingException, JMSException {
        final List destinations = new ArrayList();
        final boolean help = decodeArgs(args, destinations);
        if (help) {
            help();
        } else {
            create(destinations);
        }
    }

    private static void help() {
        stdout("create-dropbox -? | -h | --help | /? | /h | /help");
        stdout("    Display this help text.");
        stdout("create-dropbox { -q | --queue | /q | /queue } queue-name1");
        stdout("               { -q | --queue | /q | /queue } queue-name2");
        stdout("               { -t | --topic | /t | /topic } topic-name1");
        stdout("               { -t | --topic | /t | /topic } topic-name2");
        stdout("    Create the one or more queues and/or topics listed.");
    }

    private static boolean decodeArgs(final String[] args, final List destinations) {
        boolean help = false;
        for (int i = 0; i < args.length; i++) {
            final String arg = args[i];
            if (arg.equalsIgnoreCase("-q") || arg.equalsIgnoreCase("--queue") || arg.equalsIgnoreCase("/q") || arg.equalsIgnoreCase("/queue")) {
                i++;
                destinations.add(new QueueImpl(args[i]));
            } else if (arg.equalsIgnoreCase("-t") || arg.equalsIgnoreCase("--topic") || arg.equalsIgnoreCase("/t") || arg.equalsIgnoreCase("/topic")) {
                i++;
                destinations.add(new TopicImpl(args[i]));
            } else if (arg.equalsIgnoreCase("-?") || arg.equalsIgnoreCase("-h") || arg.equalsIgnoreCase("--help") || arg.equalsIgnoreCase("/?") || arg.equalsIgnoreCase("/h") || arg.equalsIgnoreCase("/help")) {
                help = true;
            } else {
                help = true;
                stderr("Unknown command line option " + arg);
            }
        }
        if (!help && destinations.isEmpty()) {
            help = true;
            stderr("No queues or topics defined");
        }
        return help;
    }

    private static void create(final List destinations) throws NamingException, JMSException {
        final Properties properties = new Properties(System.getProperties());
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, Configuration.INITIAL_CONTEXT_FACTORY);
        properties.setProperty(Configuration.ROOT_PROPERTY, System.getProperty(Configuration.ROOT_PROPERTY));
        final Configuration configuration = new Configuration(properties);
        for (Iterator iterator = destinations.iterator(); iterator.hasNext(); ) {
            final DestinationImpl destination = (DestinationImpl) iterator.next();
            final String type = destination instanceof Queue ? "queue" : "topic";
            stdout("Creating " + type + " " + destination.getName() + " in " + configuration.getRootDir());
            DirectoryStructure.createDropbox(destination, configuration);
        }
    }

    private static void stdout(final String line) {
        System.out.println(line);
    }

    private static void stderr(final String line) {
        System.err.println(line);
    }
}
