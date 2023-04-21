package owltools.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import de.tudresden.inf.lat.jcel.owlapi.main.JcelReasoner;
import owltools.graph.OWLGraphWrapper;
import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;

public class OWLServer extends AbstractHandler {

    private static Logger LOG = Logger.getLogger(OWLServer.class);

    OWLGraphWrapper graph;

    Map<String, OWLReasoner> reasonerMap = new HashMap<String, OWLReasoner>();

    public OWLServer(OWLGraphWrapper g) {
        super();
        graph = g;
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = request.getPathInfo();
        baseRequest.setHandled(true);
        OWLHandler handler = new OWLHandler(this, graph, request, response);
        String[] toks = path.split("/");
        String m;
        if (toks.length == 0) {
            m = "top";
        } else {
            m = toks[1];
        }
        if (m.contains(".")) {
            String[] mpa = m.split("\\.", 2);
            m = mpa[0];
            handler.setFormat(mpa[1]);
        }
        handler.setCommandName(m);
        Class[] mArgs = new Class[0];
        try {
            Method method = handler.getClass().getMethod(m + "Command", mArgs);
            Object[] oArgs = new Object[0];
            method.invoke(handler, oArgs);
            handler.printCachedObjects();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            System.err.println("M=" + m + " // mArgs=" + mArgs);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }
    }

    public synchronized OWLReasoner getReasoner(String reasonerName) {
        if (reasonerMap.containsKey(reasonerName)) return reasonerMap.get(reasonerName);
        OWLOntology ont = graph.getSourceOntology();
        OWLReasonerFactory reasonerFactory = null;
        OWLReasoner reasoner = null;
        LOG.info("Creating reasoner:" + reasonerName);
        if (reasonerName == null || reasonerName.equals("factpp")) reasonerFactory = new FaCTPlusPlusReasonerFactory(); else if (reasonerName.equals("pellet")) reasonerFactory = new PelletReasonerFactory(); else if (reasonerName.equals("hermit")) {
            reasonerFactory = new org.semanticweb.HermiT.Reasoner.ReasonerFactory();
        } else if (reasonerName.equals("elk")) {
            reasonerFactory = new ElkReasonerFactory();
            reasoner = reasonerFactory.createNonBufferingReasoner(ont);
            reasoner.precomputeInferences(InferenceType.values());
        } else if (reasonerName.equals("jcel")) {
            reasoner = new JcelReasoner(ont);
            reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
            return reasoner;
        } else if (reasonerName.equals("structural")) {
            reasonerFactory = new StructuralReasonerFactory();
        } else {
            System.out.println("no such reasoner: " + reasonerName);
        }
        if (reasoner == null) reasoner = reasonerFactory.createReasoner(ont);
        reasonerMap.put(reasonerName, reasoner);
        return reasoner;
    }
}
