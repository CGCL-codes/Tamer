package net.sourceforge.ondex.xten.functions.scripting_functions;

import static net.sourceforge.ondex.xten.functions.StandardFunctions.getAccessionsOfType;
import static net.sourceforge.ondex.xten.functions.StandardFunctions.getOtherNode;
import static net.sourceforge.ondex.xten.functions.StandardFunctions.getOutgoingRelationsToConceptClass;
import static net.sourceforge.ondex.xten.functions.StandardFunctions.relationsToTargets;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;
import javax.xml.stream.XMLStreamException;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.CV;
import net.sourceforge.ondex.core.ConceptAccession;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.core.ONDEXIterator;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.ONDEXView;
import net.sourceforge.ondex.core.util.ONDEXViewFunctions;
import net.sourceforge.ondex.export.oxl.Export;
import net.sourceforge.ondex.tools.ZipEndings;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamWriter2;
import com.ctc.wstx.io.CharsetNames;

/**
 * General-purpose functions used in scripting
 * @author lysenkoa
 * net.sourceforge.ondex.xten.functions.scripting_functions.General
 */
public class General {

    public static void createDir(String parent, String name) {
        File f = new File(parent + File.separator + name);
        f.mkdir();
    }

    public static void saveGraph(ONDEXGraph graph, String filename) {
        new OXLExport(graph, new File(filename));
    }

    /**
	 * Cleans up go annotation by leaving only the most specific ones on each branch.
	 * @param graph - graph with go annotations to process
	 */
    public static String goAnnotationCleaner(final ONDEXGraph graph) {
        int removed = 0;
        Map<ConceptClass, Set<ONDEXConcept>> goCCs = new HashMap<ConceptClass, Set<ONDEXConcept>>();
        final Map<ConceptClass, Set<ONDEXConcept>> toRemove = new HashMap<ConceptClass, Set<ONDEXConcept>>();
        final Set<String> roots = new HashSet<String>(Arrays.asList(new String[] { "GO:0008150", "GO:0003674", "GO:0005575" }));
        ONDEXView<ONDEXConcept> other = graph.getConcepts();
        for (String gc : new String[] { "MolFunc", "BioProc", "CelComp" }) {
            ConceptClass cc = graph.getMetaData().getConceptClass(gc);
            other = ONDEXViewFunctions.andNot(other, graph.getConceptsOfConceptClass(cc));
            goCCs.put(cc, new HashSet<ONDEXConcept>());
            toRemove.put(cc, new HashSet<ONDEXConcept>());
        }
        int entCounter = 1;
        while (other.hasNext()) {
            ONDEXConcept c = other.next();
            entCounter++;
            ONDEXView<ONDEXRelation> rOfC = graph.getRelationsOfConcept(c);
            while (rOfC.hasNext()) {
                ONDEXConcept opp = getOtherNode(c, rOfC.next());
                Set<ONDEXConcept> holder = goCCs.get(opp.getOfType());
                if (holder != null) {
                    holder.add(opp);
                }
            }
            rOfC.close();
            Set<Thread> toWaitFor = new HashSet<Thread>();
            for (final Entry<ConceptClass, Set<ONDEXConcept>> ent : goCCs.entrySet()) {
                Thread t = new Thread() {

                    public void run() {
                        Set<ONDEXConcept> subset = new HashSet<ONDEXConcept>();
                        for (ONDEXConcept z : ent.getValue()) {
                            if (subset.contains(z)) continue;
                            if (accTest(z, roots)) {
                                subset.add(z);
                                continue;
                            }
                            Set<ONDEXConcept> parents = relationsToTargets(getOutgoingRelationsToConceptClass(graph, z, ent.getKey()));
                            while (parents.size() > 0) {
                                Set<ONDEXConcept> next = new HashSet<ONDEXConcept>();
                                for (ONDEXConcept p : parents) {
                                    if (!accTest(p, roots)) {
                                        if (ent.getValue().contains(p) && !p.equals(z)) subset.add(p);
                                        next.addAll(relationsToTargets(getOutgoingRelationsToConceptClass(graph, p, ent.getKey())));
                                    } else {
                                        subset.add(p);
                                    }
                                }
                                parents = next;
                            }
                        }
                        ent.getValue().clear();
                        toRemove.get(ent.getKey()).addAll(subset);
                    }
                };
                toWaitFor.add(t);
                t.start();
            }
            for (Thread t : toWaitFor) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            toWaitFor.clear();
            Set<ONDEXConcept> removeSet = new HashSet<ONDEXConcept>();
            for (Set<ONDEXConcept> set : toRemove.values()) {
                removeSet.addAll(set);
                set.clear();
            }
            ONDEXView<ONDEXRelation> rem = graph.getRelationsOfConcept(c);
            while (rem.hasNext()) {
                ONDEXRelation del = rem.next();
                if (removeSet.contains(getOtherNode(c, del))) {
                    graph.deleteRelation(del.getId());
                    removed++;
                }
            }
            rem.close();
        }
        other.close();
        return "Removed " + removed + " less specific annotations form the graph.";
    }

    private static boolean accTest(ONDEXConcept c, Set<String> accs) {
        boolean result = false;
        ONDEXIterator<ConceptAccession> it = c.getConceptAccessions();
        while (it.hasNext()) {
            ConceptAccession ca = it.next();
            if (accs.contains(ca.getAccession())) {
                result = true;
                break;
            }
        }
        it.close();
        return result;
    }

    public static void exportToSungear(ONDEXGraph graph, String acc_type_to_use, String fileName, String... atts) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            List<AttributeName> ats = new ArrayList<AttributeName>();
            CV cv = graph.getMetaData().getCV(acc_type_to_use);
            boolean first = true;
            for (String s : atts) {
                ats.add(graph.getMetaData().getAttributeName(s));
                if (first) {
                    first = false;
                    bw.write(s);
                    bw.flush();
                } else {
                    bw.write(" | " + s);
                    bw.flush();
                }
            }
            bw.write(" | gene\n");
            bw.flush();
            ONDEXView<ONDEXConcept> cs = graph.getConcepts();
            while (cs.hasNext()) {
                ONDEXConcept c = cs.next();
                ONDEXView<ONDEXRelation> rs = graph.getRelationsOfConcept(c);
                Set<AttributeName> found = new HashSet<AttributeName>();
                while (rs.hasNext()) {
                    ONDEXRelation r = rs.next();
                    for (AttributeName at : ats) {
                        if (r.getRelationGDS(at) != null) {
                            found.add(at);
                        }
                    }
                }
                rs.close();
                if (found.size() > 0) {
                    String acc = "???";
                    List<String> accs = getAccessionsOfType(c, cv);
                    if (accs.size() > 0) {
                        if (acc_type_to_use.equals("TAIR")) {
                            for (String s : accs) {
                                if (!s.contains(".")) {
                                    acc = s;
                                    break;
                                }
                            }
                            if (acc.equals("???")) {
                                acc = accs.get(0).substring(0, accs.get(0).indexOf("."));
                            }
                        } else {
                            acc = accs.get(0);
                        }
                    }
                    first = true;
                    for (AttributeName an : ats) {
                        if (first) {
                            first = false;
                            bw.write(found.contains(an) ? "1" : "0");
                            bw.flush();
                        } else {
                            bw.write(" | " + (found.contains(an) ? "1" : "0"));
                            bw.flush();
                        }
                    }
                    bw.write(" | " + acc + "\n");
                    bw.flush();
                }
            }
            cs.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class OXLExport extends Export {

        public OXLExport(ONDEXGraph aog, File file) {
            XMLOutputFactory2 xmlOutput = (XMLOutputFactory2) XMLOutputFactory2.newInstance();
            xmlOutput.configureForSpeed();
            xmlOutput.setProperty(XMLOutputFactory2.IS_REPAIRING_NAMESPACES, false);
            int detectedEnding = ZipEndings.getPostfix(file);
            try {
                OutputStream outStream = null;
                switch(detectedEnding) {
                    case ZipEndings.GZ:
                        outStream = new GZIPOutputStream(new FileOutputStream(file));
                        System.out.println("Detected GZIP file");
                        break;
                    case ZipEndings.ZIP:
                        System.err.println("ZIP file not supported");
                        break;
                    case ZipEndings.XML:
                        outStream = new FileOutputStream(file);
                        System.out.println("Detected Uncompressed file");
                        break;
                    default:
                        File f = new File(file.getAbsolutePath() + ".xml.gz");
                        outStream = new GZIPOutputStream(new FileOutputStream(f));
                }
                if (outStream != null) {
                    XMLStreamWriter2 xmlWriteStream = (XMLStreamWriter2) xmlOutput.createXMLStreamWriter(outStream, CharsetNames.CS_UTF8);
                    buildDocument(xmlWriteStream, aog);
                    xmlWriteStream.flush();
                    xmlWriteStream.close();
                    outStream.flush();
                    outStream.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
