package se.kth.cid.test;

import se.kth.cid.util.Tracer;
import se.kth.cid.identity.*;
import se.kth.cid.identity.pathurn.*;

public class Resolve {

    public static void main(String[] argv) throws Exception {
        if (argv.length < 2) {
            Tracer.trace("Usage: Resolve tableurl urn [LogLevel]", Tracer.ERROR);
            System.exit(-1);
        }
        if (argv.length > 2) Tracer.setLogLevel(Tracer.parseLogLevel(argv[2])); else Tracer.setLogLevel(Tracer.NONE);
        URI table = URIClassifier.parseURI(argv[0]);
        PathURN parsedURN = new PathURN(argv[1]);
        printURI(parsedURN);
        ResolverTable rtable = new ResolverTable(table);
        TableResolver resolver = new TableResolver();
        rtable.fillResolver(resolver);
        ResolveResult[] res = resolver.resolve(parsedURN);
        for (int i = 0; i < res.length; i++) {
            printRes(res[i]);
        }
    }

    public static void printURI(URI parsedURI) {
        Tracer.debug("Scheme: " + parsedURI.getScheme());
        Tracer.debug("Fragment: " + parsedURI.getFragment());
        if (parsedURI instanceof URL) {
            Tracer.debug("Host: " + ((URL) parsedURI).getHost());
            Tracer.debug("Port: " + ((URL) parsedURI).getPort());
            Tracer.debug("Path: " + ((URL) parsedURI).getPath());
        } else if (parsedURI instanceof URN) {
            Tracer.debug("URN protocol: " + ((URN) parsedURI).getProtocol());
            if (parsedURI instanceof PathURN) Tracer.debug("Path: " + ((PathURN) parsedURI).getPath()); else Tracer.debug("Protocol specific: " + ((URN) parsedURI).getProtocolSpecific());
        } else if (parsedURI instanceof FileURL) Tracer.debug("File path: " + ((FileURL) parsedURI).getPath()); else Tracer.debug("URI scheme specific: " + parsedURI.getSchemeSpecific());
    }

    public static void printRes(ResolveResult r) {
        Tracer.debug("Result: " + r.uri);
        Tracer.debug("Matching path: " + r.matchingPath);
        Tracer.debug("baseURI: " + r.baseURI);
        Tracer.debug("type: " + r.type);
    }
}
