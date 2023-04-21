package pl.edu.mimuw.xqtav.proc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.embl.ebi.escience.baclava.DataThing;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scuflworkers.ProcessorTaskWorker;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import pl.edu.mimuw.xqtav.XQMultiDocument;
import pl.edu.mimuw.xqtav.exec.TLSData;
import pl.edu.mimuw.xqtav.exec.xqq.XQQData;
import pl.edu.mimuw.xqtav.exec.xqtavengine_1.ProcessorRunner_1;
import pl.edu.mimuw.xqtav.exec.xqtavengine_1.XQTavEngine_1;
import pl.edu.mimuw.xqtav.itype.JDomConvert;
import pl.edu.mimuw.xqtav.itype.TypeConvert;
import pl.edu.mimuw.xqtav.proc.exec.ProcConsoleErrorHandler;
import pl.edu.mimuw.xqtav.proc.exec.ProcConsoleEventLogger;
import pl.edu.mimuw.xqtav.proc.exec.ProcExecProgressMonitor;
import pl.edu.mimuw.xqtav.proc.exec.ProcInputHandler;
import pl.edu.mimuw.xqtav.proc.exec.ProcOutputHandler;
import uk.ac.soton.itinnovation.taverna.enactor.entities.ProcessorTask;

/**
 *
 * @author marchant
 */
public class TavInvocationTask implements ProcessorTaskWorker {

    protected TavProcessor myProc = null;

    /** Creates a new instance of TavInvocationTask */
    public TavInvocationTask(Processor p) {
        myProc = (TavProcessor) p;
    }

    public Map execute(Map inputMap, ProcessorTask parentTask) {
        System.out.println("XQTav: Preparing engine to start");
        XQTavEngine_1 ste = new XQTavEngine_1();
        TLSData.setRunningProcessor(myProc);
        ste.preconfigure();
        ste.setErrorHandler(new ProcConsoleErrorHandler());
        ste.setEventLogger(new ProcConsoleEventLogger());
        ste.setInputProvider(new ProcInputHandler(inputMap));
        ProcOutputHandler poh = new ProcOutputHandler();
        ste.setOutputProvider(poh);
        ste.setProcessorRunner(new ProcessorRunner_1());
        ste.setProgressMonitor(new ProcExecProgressMonitor());
        String contents = null;
        XQMultiDocument xmd = myProc.getDocument();
        if (xmd == null) {
            throw new TavProcException("No XQuery defined, cannot run!");
        }
        byte[] bc = xmd.getDocument("/");
        if (bc == null) {
            throw new TavProcException("No part / defined in XQuery");
        }
        contents = xmd.mergeIncludesInMain();
        XQQData.current.setDoc(myProc.getName(), xmd);
        ste.loadXQueryFromString(contents);
        ste.setXQUri(":other:");
        System.out.println("XQTav: Launching engine");
        ste.executeXQ();
        System.out.println("XQTav: Execution completed.");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ste.fetchResult(baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        SAXBuilder builder = new SAXBuilder();
        Document outputDoc = null;
        try {
            outputDoc = builder.build(bais);
        } catch (Exception e) {
            throw new TavProcException("Failed to build output doc");
        }
        Element root = outputDoc.getRootElement();
        if (root != null) {
            if (root.getName().equals("sequence")) {
                List l = root.getChildren();
                for (Iterator it = l.iterator(); it.hasNext(); ) {
                    Element el = (Element) it.next();
                    if (el.getName().equals("element")) {
                        List ell = el.getChildren();
                        if (ell.size() != 1) {
                            throw new TavProcException("Unexpected count of children: " + ell.size());
                        }
                        el = (Element) ell.get(0);
                        if (el.getName().equals("p")) {
                            String outputName = el.getAttributeValue("name");
                            if (outputName == null) {
                                System.err.println("No output name in element");
                                continue;
                            }
                            List elc = el.getChildren();
                            if (elc.size() > 1) {
                                DataThing dt = TypeConvert.IType2BDM(JDomConvert.JDom2IType(el));
                                poh.consumeOutput(outputName, dt);
                            } else {
                                if (elc.size() == 0) {
                                    poh.consumeOutput(outputName, TypeConvert.makeBDM(new LinkedList()));
                                } else {
                                    DataThing dt = TypeConvert.IType2BDM(JDomConvert.JDom2IType((Element) elc.get(0)));
                                    poh.consumeOutput(outputName, dt);
                                }
                            }
                            continue;
                        }
                        System.err.println("Unexpected element: " + el.getName());
                    }
                    if (el.getName().equals("atomic-value")) {
                        System.err.println("Unknown atomic value in output");
                        continue;
                    }
                    System.err.println("Unknown element name in output document: " + el.getName());
                }
            } else {
                throw new TavProcException("Output document root node should be named 'sequence' but is named '" + root.getName() + "'");
            }
        }
        poh.close();
        return poh.getOutputHashMap();
    }
}
