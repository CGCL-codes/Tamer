package org.tripcom.ScalabilityClient;

import jargs.gnu.CmdLineParser;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.tripcom.api.ws.client.TSClient;
import org.tripcom.integration.entry.SpaceURI;
import org.tripcom.integration.entry.Template;

public class Main {

    private static Ring<SpaceURI> spaces;

    private static TSClient tsclient;

    private static Integer repetitions;

    private static Template template = new Template("SELECT * WHERE { ?s ?p ?o .}");

    /**
	 * 
	 * @param args
	 *            <rootSpace host> <kernel to test>
	 */
    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getRootLogger();
        logger.setLevel(Level.WARN);
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option r = parser.addStringOption('r', "rootSpace");
        CmdLineParser.Option k = parser.addStringOption('k', "kernel");
        CmdLineParser.Option s = parser.addIntegerOption('s', "numberOfSubSpaces");
        CmdLineParser.Option t = parser.addIntegerOption('t', "triplesPerSpace");
        CmdLineParser.Option l = parser.addIntegerOption('l', "repetitions");
        CmdLineParser.Option c = parser.addIntegerOption('c', "threads");
        parser.parse(args);
        String rootSpace = (String) parser.getOptionValue(r);
        String kernel = (String) parser.getOptionValue(k);
        Integer numberOfSubSpaces = (Integer) parser.getOptionValue(s);
        Integer triplesPerSpace = (Integer) parser.getOptionValue(t);
        repetitions = (Integer) parser.getOptionValue(l);
        Integer threads = (Integer) parser.getOptionValue(c);
        System.err.println("Root space:      \t" + rootSpace);
        System.err.println("kernel:          \t" + kernel);
        System.err.println("numberOfSubSpace:\t" + numberOfSubSpaces);
        System.err.println("triplesPerSpace: \t" + triplesPerSpace);
        System.err.println("threads:         \t" + threads);
        System.err.println("repetitions:     \t" + repetitions);
        System.err.println("Create root space: " + "tsc://" + rootSpace + ":8080/root");
        SpaceURI rURI = new SpaceURI("tsc://" + rootSpace + ":8080/root");
        TSClient rtsclient = new TSClient("http", InetAddress.getByName(rootSpace), 8080);
        rURI = rtsclient.create(rURI);
        tsclient = new TSClient("http", InetAddress.getByName(kernel), 8080);
        spaces = new Ring<SpaceURI>();
        ValueFactory myFactory = new ValueFactoryImpl();
        System.err.println("Create " + numberOfSubSpaces + " on kernel " + kernel);
        for (int i = 0; i < numberOfSubSpaces; i++) {
            SpaceURI subSpace = tsclient.create(new SpaceURI(rURI.getURI().toASCIIString() + "/" + i));
            spaces.add(subSpace);
            Set<Statement> stmts = new HashSet<Statement>();
            for (int j = 0; j < triplesPerSpace; j++) {
                String book = i + "-" + j;
                URI mySubject = myFactory.createURI("http://example.com/book/book" + book);
                URI myPredicate = myFactory.createURI("http://purl.org/dc/elements/1.1/title");
                LiteralImpl myObject = (LiteralImpl) myFactory.createLiteral("Book " + book);
                stmts.add(myFactory.createStatement(mySubject, myPredicate, myObject));
            }
            tsclient.outSynchrone(stmts, subSpace);
        }
        List<Thread> tt = new ArrayList<Thread>();
        for (int i = 0; i < threads; i++) {
            Thread thread = new Thread() {

                public void run() {
                    try {
                        SpaceURI space = spaces.next();
                        for (int j = 0; j < repetitions; j++) {
                            long startTime = System.currentTimeMillis();
                            Set<Statement> st = tsclient.rd_nonrecursive(template, space, 60000);
                            if (st.size() >= 1) {
                                System.out.println(System.currentTimeMillis() - startTime);
                            } else {
                                System.out.println("");
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            tt.add(thread);
            thread.start();
        }
        for (Thread thread : tt) {
            thread.join();
        }
    }
}
