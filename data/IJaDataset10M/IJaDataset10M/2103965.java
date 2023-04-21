package fortunata.fswapps.omemo;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;
import java.io.*;
import java.text.DateFormat;
import java.util.*;

/**
 * Given an ontology file this class generates JSPWiki pages with information for "mere mortals". Initially it is
 * intended for "Visualization Providers", i.e., "more mortals" with no knowledge about ontologies languages but
 * advanced skills in Web Graphic Design.
 * User: Mariano Rico
 * Date: 05-ene-2006
 */
public class InfoExtractor {

    OntModel m = null;

    String ns = null;

    Map prefix2NsMap = null;

    HashMap ns2prefixMap = null;

    String workDir;

    OntologyWikiPageName ontologyWikiPageName;

    String checksWikiPageName;

    final String indexPluginAdHocClassName = "fortunata.jspwikiplugins.IndexPluginNoInitials";

    static HashMap globalNs2PrefixMap = null;

    public static void resetGlobalMap() {
        globalNs2PrefixMap = null;
    }

    /**
     * @param fis     . Contains the data of the specification file.
     * @param namespace      . The name space (full) of this spec file.
     * @param ontLang . You can indicate that the file is written in "RDFS" or "OWL".
     * @throws Exception
     */
    public InfoExtractor(FileInputStream fis, String namespace, String ontLang) throws Exception {
        OntModelSpec ontoModelSpec = null;
        if (ontLang.equals("RDFS")) {
            ontoModelSpec = OntModelSpec.RDFS_MEM_RDFS_INF;
        } else {
            if (ontLang.equals("OWL")) {
                ontoModelSpec = OntModelSpec.OWL_MEM_RDFS_INF;
            } else {
                throw new Exception("The language " + ontLang + " is not supported");
            }
        }
        m = ModelFactory.createOntologyModel(ontoModelSpec, null);
        ns = (namespace.endsWith("/") || namespace.endsWith("#")) ? namespace : namespace + "/";
        m.read(fis, ns);
    }

    /**
     * Writes all the wiki pages to workDir
     * @param workDir
     * @param owpn
     * @param checksWikiPageName
     * @throws IOException
     */
    public void writePage(String workDir, OntologyWikiPageName owpn, String checksWikiPageName) throws IOException {
        this.workDir = workDir;
        this.ontologyWikiPageName = owpn;
        this.checksWikiPageName = checksWikiPageName;
        FileOutputStream fos = new FileOutputStream(workDir + File.separator + ontologyWikiPageName.getOntologyWikiPageName() + OntologyWikiPageName.getWikiFileExtension());
        PrintStream ps = new PrintStream(fos);
        this.prefix2NsMap = checkNsMap(m.getNsPrefixMap(), ns);
        this.ns2prefixMap = createNS2PrefixMap(prefix2NsMap);
        ps.println("[{TableOfContents }]");
        ps.println("Results obtained obeying Jena2 Onto model: " + specToString(m.getSpecification()) + "\\\\");
        ps.println("Generated on " + new Date());
        ps.println("!!Other versions");
        ps.println("[{" + indexPluginAdHocClassName + " include='" + owpn.getOntologyWikiPageNameWithoutVersion() + "*." + "'" + " exclude='" + ontologyWikiPageName + "' itemsPerLine=1}]");
        writeNamespace(ps);
        writeUsedNamespaces(ps);
        writeClasses(ps);
        writeProperties(ps);
        writeReferedClasses(ps);
        writeReferedProperties(ps);
        ps.flush();
        ps.close();
    }

    private String specToString(OntModelSpec spec) {
        if (spec == OntModelSpec.DAML_MEM) {
            return "DAML_MEM";
        }
        if (spec == OntModelSpec.DAML_MEM_RDFS_INF) {
            return "DAML_MEM_RDFS_INF";
        }
        if (spec == OntModelSpec.DAML_MEM_RULE_INF) {
            return "DAML_MEM_RULE_INF";
        }
        if (spec == OntModelSpec.DAML_MEM_TRANS_INF) {
            return "DAML_MEM_TRANS_INF";
        }
        if (spec == OntModelSpec.RDFS_MEM) {
            return "DAML_MEM";
        }
        if (spec == OntModelSpec.RDFS_MEM_RDFS_INF) {
            return "RDFS_MEM_RDFS_INF";
        }
        if (spec == OntModelSpec.RDFS_MEM_TRANS_INF) {
            return "RDFS_MEM_TRANS_INF";
        }
        if (spec == OntModelSpec.OWL_DL_MEM) {
            return "OWL_DL_MEM";
        }
        if (spec == OntModelSpec.OWL_DL_MEM_RDFS_INF) {
            return "OWL_DL_MEM_RDFS_INF";
        }
        if (spec == OntModelSpec.OWL_DL_MEM_RULE_INF) {
            return "OWL_DL_MEM_RULE_INF";
        }
        if (spec == OntModelSpec.OWL_DL_MEM_TRANS_INF) {
            return "OWL_DL_MEM_TRANS_INF";
        }
        if (spec == OntModelSpec.OWL_LITE_MEM) {
            return "OWL_LITE_MEM";
        }
        if (spec == OntModelSpec.OWL_LITE_MEM_RDFS_INF) {
            return "OWL_LITE_MEM_RDFS_INF";
        }
        if (spec == OntModelSpec.OWL_LITE_MEM_RULES_INF) {
            return "OWL_LITE_MEM_RULES_INF";
        }
        if (spec == OntModelSpec.OWL_LITE_MEM_TRANS_INF) {
            return "OWL_LITE_MEM_TRANS_INF";
        }
        if (spec == OntModelSpec.OWL_MEM) {
            return "OWL_MEM";
        }
        if (spec == OntModelSpec.OWL_MEM_MICRO_RULE_INF) {
            return "OWL_MEM_MICRO_RULE_INF";
        }
        if (spec == OntModelSpec.OWL_MEM_MINI_RULE_INF) {
            return "OWL_MEM_MINI_RULE_INF";
        }
        if (spec == OntModelSpec.OWL_MEM_RDFS_INF) {
            return "OWL_MEM_RDFS_INF";
        }
        if (spec == OntModelSpec.OWL_MEM_RULE_INF) {
            return "OWL_MEM_RULE_INF";
        }
        if (spec == OntModelSpec.OWL_MEM_TRANS_INF) {
            return "OWL_MEM_TRANS_INF";
        }
        return "Unknown";
    }

    private void writeNamespace(PrintStream ps) {
        ps.println("!!Namespace defined");
        ps.println();
        ps.println(ns + " \\\\ ");
    }

    private void writeUsedNamespaces(PrintStream ps) {
        ps.println("!!Namespaces used");
        ps.println();
        Set set = prefix2NsMap.keySet();
        Iterator kiter = set.iterator();
        Object kobj = null;
        String sobj = null;
        if (kiter.hasNext()) {
            ps.println("%%sortable");
            ps.println("||Prefix||URL");
        }
        while (kiter.hasNext()) {
            kobj = kiter.next();
            sobj = (String) kobj;
            if (sobj.equals("")) {
                sobj = " ";
            }
            ps.println("|" + sobj + "|" + prefix2NsMap.get(kobj) + " \\\\ ");
        }
        ps.println("%%");
    }

    private void writeClasses(PrintStream ps) throws IOException {
        ExtendedIterator iter = m.listNamedClasses();
        ps.println("!!Classes defined");
        ps.println();
        OntClass oc = null;
        while (iter.hasNext()) {
            oc = (OntClass) iter.next();
            if (oc.getNameSpace().equals(ns)) {
                ps.println("* [" + oc.getLocalName() + "|" + ontologyWikiPageName + oc.getLocalName() + "]" + " \\\\ ");
                writeWikiPageForOntoClass(oc);
                writeWikiPageForGenericOntoClass(oc);
            }
        }
    }

    private void writeClassesAdv(PrintStream ps) {
        Iterator iter = m.listHierarchyRootClasses().filterDrop(new Filter() {

            public boolean accept(Object o) {
                return ((Resource) o).isAnon();
            }
        });
        ps.println("!!Classes Adv defined");
        ps.println();
        OntClass oc = null;
        while (iter.hasNext()) {
            oc = (OntClass) iter.next();
            if (oc.getNameSpace().equals(ns)) {
                ps.println("* " + oc.getLocalName() + " \\\\ ");
            }
        }
    }

    private void writeWikiPageForOntoClass(OntClass oc) throws IOException {
        FileOutputStream fos = null;
        String name = oc.getLocalName();
        try {
            fos = new FileOutputStream(workDir + File.separator + ontologyWikiPageName.getOntologyWikiPageName() + name + OntologyWikiPageName.getWikiFileExtension());
            PrintStream ps = new PrintStream(fos);
            ps.println("[{TableOfContents }]");
            ps.println("!!Other versions");
            ps.println("[{" + indexPluginAdHocClassName + " include='" + ontologyWikiPageName.getOntologyWikiPageNameWithoutVersion() + "*." + name + "'" + " exclude='" + ontologyWikiPageName.getOntologyWikiPageName() + name + "' itemsPerLine=1}]");
            ps.println("!!Properties used by this class");
            ps.println("||Name||Description||Type");
            ExtendedIterator iter = oc.listDeclaredProperties();
            OntProperty p;
            while (iter.hasNext()) {
                p = (OntProperty) iter.next();
                ps.println("|[" + p.getLocalName() + "|" + ontologyWikiPageName + p.getLocalName() + "]" + "|" + showPropDescription(p) + "|" + showPropRange(p) + "\\\\");
                writeWikiPageForOntoProperty(p);
            }
            ps.println("!!Is specialization of (inherits from / subclass of) classes");
            ps.println("!!Is specialized in (superclass of) classes");
            fos.close();
        } catch (IOException e) {
            throw new IOException("Error creating wiki page for " + name);
        }
    }

    private void writeWikiPageForGenericOntoClass(OntClass oc) throws IOException {
        FileOutputStream fos = null;
        String name = oc.getLocalName();
        String prefix = OntologyWikiPageName.getPrefix();
        try {
            fos = new FileOutputStream(workDir + File.separator + prefix + ".." + name + OntologyWikiPageName.getWikiFileExtension());
            PrintStream ps = new PrintStream(fos);
            ps.println("!!Generics for " + oc.getLocalName());
            ps.println("[{" + indexPluginAdHocClassName + " include='" + prefix + "*.*." + name + "'" + " exclude='" + prefix + ".." + name + "' itemsPerLine=1}]");
        } catch (IOException e) {
            throw new IOException("Error creating wiki page for " + name);
        }
    }

    private String showPropDescription(OntProperty op) {
        StringBuffer sb = new StringBuffer();
        if (op.getProfile().LABEL() != null && op.getLabel(null) != null) {
            String label = op.getLabel(null);
            if (label.length() > 0) {
                sb.append(label);
            }
            if (op.getProfile().COMMENT() != null && op.getComment(null) != null) {
                String comment = op.getComment(null);
                if (comment.length() > 0) {
                    sb.append("\\\\").append(comment);
                }
            }
        }
        if (sb.length() == 0) {
            sb.append(" ");
        }
        return sb.toString();
    }

    private String showPropRange(OntProperty op) {
        Profile profile = m.getProfile();
        StringBuffer sb = new StringBuffer();
        String prefix = OntologyWikiPageName.getPrefix();
        if (profile.RANGE() != null) {
            ExtendedIterator iter = op.listRange();
            OntResource ontRes;
            while (iter.hasNext()) {
                ontRes = (OntResource) iter.next();
                if (ontRes.getNameSpace() != null && ontRes.getNameSpace().equals(ns)) {
                    sb.append("[").append(ontRes.getLocalName()).append("|").append(ontologyWikiPageName).append(ontRes.getLocalName()).append("]");
                } else {
                    sb.append("[").append(ontRes.getLocalName()).append("|").append(prefix).append("..").append(ontRes.getLocalName()).append("]").append(" (" + ns2DefinedPrefix(ontRes.getNameSpace()) + ")");
                }
                if (iter.hasNext()) {
                    sb.append("\\\\");
                }
            }
        }
        return sb.toString();
    }

    private void writeReferedClasses(PrintStream ps) {
        ExtendedIterator iter = m.listNamedClasses();
        ps.println("!!Classes referred");
        ps.println();
        OntClass oc = null;
        if (iter.hasNext()) {
            ps.println("||Class name||Namespace");
        }
        while (iter.hasNext()) {
            oc = (OntClass) iter.next();
            if (!oc.getNameSpace().equals(ns)) {
                ps.println("|" + oc.getLocalName() + "|{{" + ns2DefinedPrefix(oc.getNameSpace()) + "}} \\\\ ");
            }
        }
    }

    /**
     * This will check if the indicated namespace is one of the ones declared in the model.
     * @param namespace
     * @return if it's declared returns the prefix, else returns the input namespace
     */
    private String ns2DefinedPrefix(String namespace) {
        Object prefix = ns2prefixMap.get(namespace);
        return (prefix != null) ? (String) prefix : namespace;
    }

    /**
     *
     * @param namespace
     * @return true is namespace is one of the ones defined in this ontolgy
     */
    private boolean isNsWithDefinedPrefix(String namespace) {
        return ns2DefinedPrefix(namespace).equals(namespace);
    }

    /**
     * Inverts the mapping. May be m has different prefixes for the same namespace.
     * In the ral cas eof WOT, the same namespace has two different prefixes (wot and ""). So,
     * we try to solve this case (""), but if we can't solve it, a warning message will appear
     * in the log.
     * @param mp2n
     * @return the inverted Map (as a new HashMap)
     */
    private HashMap createNS2PrefixMap(Map mp2n) {
        HashMap invM = new HashMap();
        Set set = mp2n.keySet();
        Iterator it = set.iterator();
        Object pre = null;
        Object ns = null;
        while (it.hasNext()) {
            pre = it.next();
            ns = mp2n.get(pre);
            if (pre.equals("")) {
                if (mp2n.containsValue(ns)) {
                } else {
                    System.out.println("WARNING: the same namespace has two different prefixes");
                }
            } else {
                invM.put(ns, pre);
            }
        }
        return invM;
    }

    /**
     * Checks that they are no conflicts between namespaces. A wiki page with check's results is written.
     * @param ontologyMapPrefix2Ns mapping prefix -->namespace.
     * @return the entered map with the appropriated changes
     */
    private Map checkNsMap(Map ontologyMapPrefix2Ns, String namespace) throws IOException {
        HashMap ontologyMapNs2Prefix = createNS2PrefixMap(ontologyMapPrefix2Ns);
        boolean mustOverwriteChecksWikiFile = false;
        if (globalNs2PrefixMap == null) {
            globalNs2PrefixMap = new HashMap(ontologyMapNs2Prefix);
            mustOverwriteChecksWikiFile = true;
        }
        String fileName = workDir + File.separator + checksWikiPageName + OntologyWikiPageName.getWikiFileExtension();
        Date now = new Date();
        PrintStream ps;
        FileOutputStream fos;
        if (mustOverwriteChecksWikiFile) {
            fos = new FileOutputStream(fileName);
        } else {
            fos = new FileOutputStream(fileName, true);
        }
        ps = new PrintStream(fos);
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);
        ps.println("Generation date: " + df.format(now) + "\\\\");
        ps.println("!!Adding ontology with namespace " + namespace + "\\\\");
        check1(ontologyMapNs2Prefix, ps);
        check2(ontologyMapNs2Prefix, ps);
        ps.println("!global map");
        writeGlobalN2PMap(ps);
        ps.println();
        ps.close();
        fos.close();
        return ontologyMapPrefix2Ns;
    }

    /**
     * Ensure that each new namespace, if it's in the global namespace, has the same prefix
     *  An example:             in globalMap we have  http...200101 --> pp
     *  in one namespace in the new ontology we have  http...200101 --> pf
     * @param ontologyMapN2P
     */
    private void check1(HashMap ontologyMapN2P, PrintStream ps) {
        Set setNss = ontologyMapN2P.keySet();
        Iterator iterNs = setNss.iterator();
        Object keyNs = null;
        String sNs = null;
        String sPrefix = null;
        while (iterNs.hasNext()) {
            keyNs = iterNs.next();
            sNs = (String) keyNs;
            sPrefix = (String) ontologyMapN2P.get(keyNs);
            if (globalNs2PrefixMap.containsKey(sNs)) {
                String associatedPrefix = (String) globalNs2PrefixMap.get(sNs);
                if (!associatedPrefix.equals(sPrefix)) {
                    ps.println("WARNING!: namespace " + sNs + " had prefix " + associatedPrefix + " and now it is " + sPrefix + " \\\\");
                } else {
                    ps.println("OK: namespace " + sNs + " keeps prefix " + associatedPrefix + " \\\\");
                }
            } else {
                globalNs2PrefixMap.put(sNs, sPrefix);
                ps.println("Added new namespace " + sNs + " with prefix " + sPrefix + " \\\\");
            }
        }
    }

    /**
     * Ensure that each new prefix, if it's in the global prefixes, has the same namespace
     * For example:             in globalMap we have  http...200101 --> rdfs
     *  in one namespace in the new ontology we have  http...200108 --> rdfs
     * @param ontologyMapN2P
     */
    private void check2(HashMap ontologyMapN2P, PrintStream ps) {
        Set setOnt = ontologyMapN2P.keySet();
        Iterator kiter = setOnt.iterator();
        Object keyNs = null;
        String sNs = null;
        String sPrefix = null;
        while (kiter.hasNext()) {
            keyNs = kiter.next();
            sNs = (String) keyNs;
            sPrefix = (String) ontologyMapN2P.get(keyNs);
            if (globalNs2PrefixMap.containsValue(sPrefix)) {
                String associatedNamespace = (String) lookForKeyGivenValue(globalNs2PrefixMap, sPrefix);
                if (!associatedNamespace.equals(sNs)) {
                    ps.println("WARNING!: prefix " + sPrefix + " had namespace " + associatedNamespace + " and now it is " + sNs + " \\\\");
                } else {
                    ps.println("OK: prefix " + sPrefix + " had namespace " + associatedNamespace + " and now it is " + sNs + " \\\\");
                }
            } else {
                globalNs2PrefixMap.put(sNs, sPrefix);
                ps.println("Added new prefix " + sPrefix + " with namespace " + sNs + " \\\\");
            }
        }
    }

    private void writeGlobalN2PMap(PrintStream ps) {
        Set setNss = globalNs2PrefixMap.keySet();
        Object objNs;
        String sNS;
        Object objPrefix;
        int count = 0;
        Iterator iterNss = setNss.iterator();
        if (iterNss.hasNext()) {
            ps.println("%%sortable");
            ps.println("||#||namespace||Prefix");
            while (iterNss.hasNext()) {
                objNs = iterNss.next();
                count++;
                if (objNs != null) {
                    sNS = (String) objNs;
                    objPrefix = globalNs2PrefixMap.get(objNs);
                    ps.println("|" + count + "|" + sNS + "|" + objPrefix);
                } else {
                    ps.println("|" + count + "|null!! | nonsense");
                }
            }
            ps.println("%%");
        }
    }

    /**
     * Given a Map key->value, Given the value we want to know the key
     * @param hashMap
     * @param objValueTarget
     * @return the key with that objValueTarget, or null
     */
    private Object lookForKeyGivenValue(HashMap hashMap, Object objValueTarget) {
        Set set = hashMap.keySet();
        Iterator iterKeys = set.iterator();
        Object objValue;
        Object objKey = null;
        boolean found = false;
        while (iterKeys.hasNext()) {
            objKey = iterKeys.next();
            objValue = hashMap.get(objKey);
            if (objValue.equals(objValueTarget)) {
                found = true;
                break;
            }
        }
        return found ? objKey : null;
    }

    private void writeProperties(PrintStream ps) throws IOException {
        ExtendedIterator iter = m.listOntProperties();
        ps.println("!!Properties defined");
        ps.println();
        OntProperty op = null;
        while (iter.hasNext()) {
            op = (OntProperty) iter.next();
            if (op.getNameSpace().equals(ns)) {
                ps.println("* [" + op.getLocalName() + "|" + ontologyWikiPageName + op.getLocalName() + "]" + " \\\\ ");
                writeWikiPageForOntoProperty(op);
            }
        }
    }

    /**
     * This method assumes that classes' names begin with uppercase letter (for example Person) and that
     * properties' names begin with lower case (for example firstName). If this condition is not satisfied
     * properties will be considered classes and viceversa.
     * @param op
     * @throws IOException
     */
    private void writeWikiPageForOntoProperty(OntProperty op) throws IOException {
        FileOutputStream fos = null;
        String name = op.getLocalName();
        try {
            fos = new FileOutputStream(workDir + File.separator + ontologyWikiPageName + name + OntologyWikiPageName.getWikiFileExtension());
            PrintStream ps = new PrintStream(fos);
            ps.println("!!Other versions");
            ps.println("[{" + indexPluginAdHocClassName + " include='" + ontologyWikiPageName.getOntologyWikiPageNameWithoutVersion() + "*." + name + "'" + " exclude='" + ontologyWikiPageName.getOntologyWikiPageName() + name + "' itemsPerLine=1}]");
            ps.println("!!Info for property " + name);
            ps.println("||Name||Description||Type");
            ps.println("|" + op.getLocalName() + "|" + showPropDescription(op) + "|" + showPropRange(op) + "\\\\");
            ps.println("!!Classes using this property directly");
            ExtendedIterator iter = op.listDeclaringClasses(true);
            OntClass oc;
            String wikiText4Class;
            while (iter.hasNext()) {
                oc = (OntClass) iter.next();
                if (oc.getNameSpace() != null && oc.getNameSpace().equals(ns)) {
                    wikiText4Class = "[" + oc.getLocalName() + "|" + ontologyWikiPageName.getOntologyWikiPageName() + oc.getLocalName() + "]";
                } else {
                    wikiText4Class = oc.getLocalName();
                }
                ps.println("* " + wikiText4Class + " (" + ns2DefinedPrefix(oc.getNameSpace()) + ")");
            }
            fos.close();
        } catch (IOException e) {
            throw new IOException("Error creating wiki page for property " + name);
        }
    }

    private void writeReferedProperties(PrintStream ps) {
        ExtendedIterator iter = m.listOntProperties();
        ps.println("!!Properties referred");
        ps.println();
        OntProperty op = null;
        if (iter.hasNext()) {
            ps.println("||Property name||NameSpace");
        }
        while (iter.hasNext()) {
            op = (OntProperty) iter.next();
            if (!op.getNameSpace().equals(ns)) {
                ps.println("|" + op.getLocalName() + "|{{" + ns2DefinedPrefix(op.getNameSpace()) + "}} \\\\ ");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        OntologyWikiPageName[] owpn = { new OntologyWikiPageName("ical", "200212"), new OntologyWikiPageName("foaf", "20050403") };
        Object[][] objs = { { new FileInputStream(owpn[0].toFileName()), "http://www.w3.org/2002/12/cal/ical#", "OWL", owpn[0] }, { new FileInputStream(owpn[1].toFileName()), "http://xmlns.com/foaf/0.1", "RDFS", owpn[1] } };
        for (int i = 0; i < objs.length; i++) {
            InfoExtractor ie = new InfoExtractor((FileInputStream) objs[i][0], (String) objs[i][1], (String) objs[i][2]);
            ie.writePage("C:\\Documents and Settings\\Mariano Rico\\Mis documentos\\Mis experimentos\\Mis plugins para JSPWiki\\jspwikiRDF\\InfoExtractorSandBox", (OntologyWikiPageName) objs[i][3], "specResults");
        }
    }
}
