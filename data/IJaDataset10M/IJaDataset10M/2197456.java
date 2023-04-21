package ws.prova.api2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import ws.prova.RMessageQueue;
import ws.prova.agent2.ProvaReagent;
import ws.prova.agent2.ProvaReagentImpl;
import ws.prova.esb2.ProvaAgent;
import ws.prova.kernel2.ProvaKnowledgeBase;
import ws.prova.kernel2.ProvaList;
import ws.prova.kernel2.ProvaSolution;
import ws.prova.kernel2.messaging.ProvaMessenger;
import ws.prova.parser2.ProvaParsingException;

public class ProvaCommunicatorImpl implements ProvaCommunicator {

    public static final boolean ASYNC = true;

    public static final boolean SYNC = false;

    private ProvaReagent prova;

    private ProvaMessenger messenger;

    private RMessageQueue queue;

    private ProvaKnowledgeBase kb;

    /**
	 * @param agent   A logical name of the Prova agent (must be unique on each machine)
	 * @param port    An external port used for JADE communications. If <code>port</code> is
	 *                <code>null</code>, JADE communication is not used
	 * @param rules   There are three options to use here:
	 *                <br> if the <code>Object</code> is a <code>String</code>, it must be a filename of
	 *                the file to be consulted.
	 *                <br> otherwise, it must be either a <code>StringBuffer</code> or a <code>BufferedReader</code>,
	 *                from which the initial rulebase will be consulted.
	 * @param async   If it is <code>ProvaCommunicator.ASYNC</code>, the <code>Communicator</code> is started in a separate thread.
	 *                <br> If it is <code>ProvaCommunicator.SYNC</code>, it is started in the current thread.
	 */
    public ProvaCommunicatorImpl(String agent, String port, Object rules, boolean async) {
        String[] prot;
        if (port != null) {
            prot = new String[] { new String("jade") };
        } else {
            prot = null;
        }
        prova = new ProvaReagentImpl(agent, port, null, prot, rules, async, null, null);
        kb = prova.getKb();
        messenger = prova.getMessenger();
    }

    /**
	 * A preferred constructor for a Communicator.
	 *
	 * @param agent   A logical name of the Prova agent (must be unique on each machine)
	 * @param port    An external port used for JADE communications. If <code>port</code> is
	 *                <code>null</code>, JADE communication is not used
	 * @param rules   There are three options to use here:
	 *                <br> if the <code>Object</code> is a <code>String</code>, it must be a filename of
	 *                the file to be consulted.
	 *                <br> otherwise, it must be either a <code>StringBuffer</code> or a <code>BufferedReader</code>,
	 *                from which the initial rulebase will be consulted.
	 * @param async   If it is <code>ProvaCommunicator.ASYNC</code>, the <code>Communicator</code> is started in a separate thread.
	 *                <br> If it is <code>ProvaCommunicator.SYNC</code>, it is started in the current thread.
	 * @param globals map of arguments passed to the initialisation rulebase
	 */
    public ProvaCommunicatorImpl(String agent, String port, Object rules, boolean async, Map<String, Object> globals) {
        String[] prot;
        if (port != null) {
            prot = new String[] { new String("jade") };
        } else {
            prot = null;
        }
        prova = new ProvaReagentImpl(agent, port, null, prot, rules, async, null, globals);
        kb = prova.getKb();
        messenger = prova.getMessenger();
    }

    public ProvaCommunicatorImpl(String agent, String port, Object rules, boolean async, ProvaAgent esb, Map<String, Object> globals) {
        String[] prot;
        if (port != null) {
            prot = new String[] { new String("jade") };
        } else {
            prot = null;
        }
        prova = new ProvaReagentImpl(agent, port, null, prot, rules, async, esb, globals);
        kb = prova.getKb();
        messenger = prova.getMessenger();
    }

    public ProvaCommunicatorImpl(String agent, Object rules, boolean async) {
        prova = new ProvaReagentImpl(agent, null, null, null, rules, async, null, null);
        kb = prova.getKb();
        messenger = prova.getMessenger();
    }

    @Override
    public void stop() {
        prova.getMessenger().stop();
    }

    @Override
    public List<ProvaSolution[]> getInitializationSolutions() {
        return prova.getInitializationSolutions();
    }

    @Override
    public List<ProvaSolution[]> consultSync(String src, String key, Object[] objects) throws Exception {
        final Future<List<ProvaSolution[]>> list = prova.consultSync(src, key, objects);
        List<ProvaSolution[]> results = list.get();
        return results;
    }

    @Override
    public List<ProvaSolution[]> consultSync(BufferedReader in, String key, Object[] objects) {
        try {
            final Future<List<ProvaSolution[]>> list = prova.consultSync(in, key, objects);
            List<ProvaSolution[]> results = list.get();
            return results;
        } catch (Exception e) {
            if (e.getCause() instanceof ProvaParsingException) throw new RuntimeException(e.getCause()); else if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw new RuntimeException(e.getCause());
        }
    }

    /**
	 * A wrapper for an asynchronous query to the rulebase
	 *
	 * @param src   File name or URL with a rulebase to be consulted.
	 *                <br> Special constructs _N (e.g., _0) may be embedded in the code to represent Java
	 *                objects used in goals, facts, and rules.
	 * @param key     Unique key identifying the consulted code (useful in interactive environments)
	 * @param objects Java objects embedded into the <code>input</code> parameter.
	 * @throws Exception
	 */
    @Override
    public void consultAsync(String src, String key, Object[] objects) throws Exception {
        prova.consultAsync(src, key, objects);
    }

    /**
	 * A wrapper for an asynchronous query to the rulebase
	 *
	 * @param in   BufferedReader wrapping access to the rulebase to be consulted.
	 *                <br> Special constructs _N (e.g., _0) may be embedded in the code to represent Java
	 *                objects used in goals, facts, and rules.
	 * @param key     Unique key identifying the consulted code (useful in interactive environments)
	 * @param objects Java objects embedded into the <code>input</code> parameter.
	 * @throws Exception
	 */
    @Override
    public void consultAsync(BufferedReader in, String key, Object[] objects) throws Exception {
        prova.consultAsync(in, key, objects);
    }

    @Override
    public void setPrintWriter(PrintWriter printWriter) {
        prova.setPrintWriter(printWriter);
    }

    /**
	 * Send a message to an asynchronous Prova agent
	 * (inluding a local <code>Communicator</code> created in asynchronous mode)
	 *
	 * @param xid          Conversation-id for the message. Specifying <code>null</code> starts a new
	 *                     conversation. Otherwise, it is assumed that the message is a follow-up to the conversation
	 *                     with the specified conversation-id. Use %0 to pass an object here.
	 * @param protocol     Currently, "self", "jade", or "jms". For sending messages
	 *                     to the Prova engine in this <code>Communicator</code>, specify "self".
	 * @param obj_receiver The logical name of the target agent. Specifying "0" here results
	 *                     in sending this message to the Prova agent in this <code>Communicator</code> (it is)
	 *                     equivalent to specifying the <code>protocol</code> as "self".
	 * @param perf         Performative, i.e., speech act communicated by the message.
	 *                     Broadly speaking, it is the "type" of the message. Examples include
	 *                     standard FIPA performatives such as <code>query-ref</code>, <code>ask-if</code>, or <code>inform</code>.
	 * @param term         The message contents as a <code>String</code>. It is assumed to be
	 *                     a comma-separated list with various tags and parameters to the message. Java objects
	 *                     may be embedded by using placeholders in the format <code>%N</code> with <code>N</code> corresponding to an index
	 *                     into the array with objects in <code>objs</code>.
	 * @param objs         Array with Java objects to be embedded in the locations specified by
	 *                     placeholders in the message contents <code>term</code>.
	 * @return Conversation-id of the sent message. This id may be useful when a new
	 *         conversation is initiated. In this case, possible follow-up messages must be sent
	 *         with the conversation-id returned here.
	 */
    public Object sendMsg(String xid, String protocol, Object obj_receiver, String perf, String term, Object[] objs) {
        StringBuilder sb = new StringBuilder();
        sb.append(":-sendMsg(");
        sb.append(xid);
        sb.append(',');
        sb.append(protocol);
        sb.append(',');
        sb.append(obj_receiver);
        sb.append(',');
        sb.append(perf);
        sb.append(',');
        sb.append(term);
        sb.append(").");
        BufferedReader in = new BufferedReader(new StringReader(sb.toString()));
        try {
            consultSync(in, "", objs);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return "";
    }

    /**
	 * The main Prova Reloaded runner.
	 * <br/>
	 * Example:
	 * prova3[.bat|.sh] agent password rules/prova-examples/test001_args.prova anticoagulant
	 *
	 * @param args String[]
	 */
    public static void main(String[] args) {
        if (args.length < 3) usage();
        String agent = args[0];
        String password = args[1];
        String rules = args[2];
        String[] prot = null;
        int timeout = 0;
        int i = 3;
        while (i < args.length) {
            if (args[i].equals("-p")) {
                if (i + 1 == args.length) usage();
                prot = args[i + 1].split(",");
                i += 2;
            } else if (args[i].equals("-t")) {
                if (i + 1 == args.length) usage();
                timeout = Integer.parseInt(args[i + 1]);
                i += 2;
            } else break;
        }
        Map<String, Object> globals = new HashMap<String, Object>();
        for (int offset = i; i < args.length; i++) {
            globals.put("$" + (i - offset), args[i]);
        }
        try {
            ProvaCommunicator comm = new ProvaCommunicatorImpl(agent, null, rules, ProvaCommunicatorImpl.SYNC, globals);
            try {
                synchronized (comm) {
                    if (timeout == 0) {
                        while (!comm.getReagent().canShutdown()) comm.wait(1000);
                    } else if (timeout == -1) comm.wait(); else comm.wait(timeout * 1000);
                }
            } catch (Exception e) {
            }
            comm.shutdown();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException) {
                System.err.println("Could not read from " + rules);
            } else System.err.println(e.getCause());
        }
    }

    /**
	 * 
	 */
    private static void usage() {
        System.out.println("Usage: prova <agent> <password> <rules file> [-t <timeout in seconds>] [-p <prot1,..,protN>] [arguments]");
        System.exit(1);
    }

    @Override
    public void shutdown() {
        prova.shutdown();
    }

    @Override
    public ProvaReagent getReagent() {
        return prova;
    }

    @Override
    public void addMsg(ProvaList terms) {
        messenger.addMsg(terms);
    }

    @Override
    public void unconsultSync(String src) {
        prova.unconsultSync(src);
    }
}
