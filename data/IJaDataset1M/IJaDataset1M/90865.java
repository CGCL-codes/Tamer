package org.openscience.nmrshiftdb.webservices;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.converters.DOMConverter;
import org.apache.jetspeed.services.rundata.DefaultJetspeedRunData;
import org.apache.turbine.om.NumberKey;
import org.apache.turbine.om.security.User;
import org.apache.turbine.services.security.TurbineSecurity;
import org.apache.turbine.util.TurbineConfig;
import org.apache.turbine.util.db.Criteria;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.geometry.BondTools;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.CMLReader;
import org.openscience.cdk.io.MDLWriter;
import org.openscience.cdk.io.cml.ChemFileCDO;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.nmrshiftdb.PredictionTool;
import org.openscience.nmrshiftdb.om.DBConditionType;
import org.openscience.nmrshiftdb.om.DBLiterature;
import org.openscience.nmrshiftdb.om.DBMolecule;
import org.openscience.nmrshiftdb.om.DBMoleculePeer;
import org.openscience.nmrshiftdb.om.DBSpectrumPeer;
import org.openscience.nmrshiftdb.om.DBSpectrumType;
import org.openscience.nmrshiftdb.om.DBSpectrumTypePeer;
import org.openscience.nmrshiftdb.om.NmrshiftdbUser;
import org.openscience.nmrshiftdb.om.NmrshiftdbUserPeer;
import org.openscience.nmrshiftdb.portlets.SubmitPortlet;
import org.openscience.nmrshiftdb.util.GeneralUtils;
import org.openscience.nmrshiftdb.util.NmrshiftdbConstants;
import org.openscience.nmrshiftdb.util.StringAndInt;
import org.openscience.nmrshiftdb.util.SubmittingData;
import org.openscience.nmrshiftdb.util.ValueTriple;
import org.openscience.nmrshiftdb.util.ValuesForVelocityBean;
import org.openscience.nmrshiftdb.util.exception.NmrshiftdbException;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.base.CMLElements;
import org.xmlcml.cml.base.CMLUtil;
import org.xmlcml.cml.element.CMLBond;
import org.xmlcml.cml.element.CMLBuilder;
import org.xmlcml.cml.element.CMLCml;
import org.xmlcml.cml.element.CMLMetadata;
import org.xmlcml.cml.element.CMLMetadataList;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.element.CMLPeak;
import org.xmlcml.cml.element.CMLPeakList;
import org.xmlcml.cml.element.CMLScalar;
import org.xmlcml.cml.element.CMLSpectrum;
import com.workingdogs.village.Record;

public class NMRShiftDBServiceBindingImpl implements NMRShiftDB {

    private static final String webinfdir = "/usr/local/jakarta-tomcat-3.3.2/webapps/jetspeed/WEB-INF/";

    public org.w3c.dom.Document doPrediction(org.w3c.dom.Document request) throws RemoteException {
        org.w3c.dom.Document document = null;
        try {
            Document doc = DOMConverter.convert(request);
            Node elmol = doc.getChild(0).getChild(0);
            Node eltype = doc.getChild(0).getChild(1);
            IChemFile cfile = new ChemFile();
            CMLReader cmlreader = new CMLReader(new ByteArrayInputStream(elmol.toXML().getBytes()));
            cfile = (IChemFile) cmlreader.read(cfile);
            IMolecule mol = cfile.getChemSequence(0).getChemModel(0).getMoleculeSet().getMolecule(0);
            TurbineConfig tc = new TurbineConfig("", webinfdir + "conf/TurbineResources.properties");
            File nmrpropsFile = new File(webinfdir + "conf/NMRShiftDB.properties");
            Properties nmrprops = new Properties();
            nmrprops.load(new FileInputStream(nmrpropsFile));
            GeneralUtils.nmrprops = nmrprops;
            GeneralUtils.logToSql("Prediction WebService - " + elmol.toXML(), null);
            tc.init();
            String spectype = eltype.getChild(0).getValue();
            DBSpectrumType dbspectype = null;
            if (!spectype.equals("All")) dbspectype = DBSpectrumTypePeer.getByName(spectype);
            CMLSpectrum spectrum = new CMLSpectrum();
            CMLPeakList peakList = new CMLPeakList();
            spectrum.addPeakList(peakList);
            for (int i = 0; i < mol.getAtomCount(); i++) {
                if (spectype.equals("All") || dbspectype.getDBIsotope(1).getElementSymbol().equals(mol.getAtom(i).getSymbol())) {
                    try {
                        double[] result = PredictionTool.generalPredict(mol, mol.getAtom(i), false, true, -1, -1, new StringBuffer(), false, false, null, new HashMap(), -1, true, new StringBuffer(), 6, true, true);
                        CMLPeak peak = new CMLPeak();
                        peak.setXMin(result[0]);
                        peak.setXValue(result[1]);
                        peak.setXMax(result[1]);
                        peak.setXUnits("units:ppm");
                        peak.setId("p" + i);
                        String[] atomrefs = new String[] { mol.getAtom(i).getID() };
                        peak.setAtomRefs(atomrefs);
                        peakList.addPeak(peak);
                    } catch (Exception ex) {
                    }
                }
            }
            DocumentBuilder builder;
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(new ByteArrayInputStream(spectrum.toXML().getBytes()));
        } catch (Exception ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }
        return document;
    }

    public org.w3c.dom.Document getSpectrumTypes(org.w3c.dom.Document dummy) throws RemoteException {
        System.err.println("lkj");
        StringBuffer sb = new StringBuffer();
        org.w3c.dom.Document document = null;
        try {
            TurbineConfig tc = new TurbineConfig("", webinfdir + "conf/TurbineResources.properties");
            tc.init();
            Vector v = DBSpectrumTypePeer.doSelect(new Criteria());
            for (int i = 0; i < v.size(); i++) {
                sb.append(((DBSpectrumType) v.get(i)).getName() + " ");
            }
            DocumentBuilder builder;
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(new ByteArrayInputStream(new String("<xml>" + sb.toString() + "</xml>").getBytes()));
        } catch (Exception ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }
        return document;
    }

    public org.w3c.dom.Document doSubmit(org.w3c.dom.Document request) throws RemoteException {
        StringBuffer sb = new StringBuffer();
        org.w3c.dom.Document document;
        try {
            TurbineConfig tc = new TurbineConfig("", webinfdir + "conf/TurbineResources.properties");
            File nmrpropsFile = new File(webinfdir + "conf/NMRShiftDB.properties");
            Properties nmrprops = new Properties();
            nmrprops.load(new FileInputStream(nmrpropsFile));
            GeneralUtils.nmrprops = nmrprops;
            tc.init();
            System.err.println(request);
            Document doc = DOMConverter.convert(request);
            System.err.println(doc);
            Node elcml = doc.getChild(0).getChild(0);
            Node eluser = doc.getChild(0).getChild(1);
            Node elpassword = doc.getChild(0).getChild(2);
            CMLBuilder builder = new CMLBuilder(false);
            Element element = (Element) builder.parseString(elcml.toXML());
            namespaceThemAll(element.getChildElements());
            element.setNamespaceURI(CMLUtil.CML_NS);
            CMLCml cmlcml = (CMLCml) builder.parseString(element.toXML());
            if (cmlcml.getCMLChildCount("molecule") != 1) {
                throw new NmrshiftdbException("There must be exactly one molcule in cml");
            }
            CMLMolecule cmlmol = (CMLMolecule) cmlcml.getChildCMLElement("molecule", 0);
            Elements spectra = cmlcml.getChildCMLElements("spectrum");
            if (spectra.size() == 0) {
                throw new NmrshiftdbException("There must be at least one spectrum in cml");
            }
            NmrshiftdbUser user;
            User turbineuser = TurbineSecurity.getAuthenticatedUser(eluser.getValue(), elpassword.getValue());
            user = NmrshiftdbUserPeer.getByName(turbineuser.getUserName());
            for (int i = 0; i < spectra.size(); i++) {
                CMLSpectrum spectrum = (CMLSpectrum) spectra.get(i);
                CMLMolecule molecule = (CMLMolecule) cmlcml.getChildCMLElements("molecule").get(0);
                SubmittingData subData = new SubmittingData(webinfdir + "conf/normalizer.xml");
                if (spectrum.getMetadataListElements().size() > 0 && spectrum.getMetadataListElements().get(0).getMetadataDescendantsByName("nmr:nmrshiftdbid").size() > 0) {
                    if (spectrum.getMetadataListElements().get(0).getMetadataDescendantsByName("nmr:nmrshiftdbid").size() > 0) {
                        List l = DBSpectrumPeer.executeQuery("select SPECTRUM_ID from SPECTRUM where NMRSHIFTDB_NUMBER='" + spectrum.getMetadataListElements().get(0).getMetadataDescendantsByName("nmr:nmrshiftdbid").get(0).getContent() + "'");
                        subData.setEditSpectrum(new NumberKey(((Record) l.get(0)).getValue(1).asInt()));
                    }
                }
                subData.setMeasured();
                StringBuffer names = new StringBuffer();
                for (int k = 0; k < molecule.getNameElements().size(); k++) {
                    if (molecule.getNameElements().get(k).getAttribute("convention") != null && molecule.getNameElements().get(k).getAttribute("convention").getValue().equals("CAS")) {
                        subData.setCasNumber(molecule.getNameElements().get(k).getXMLContent());
                    } else {
                        names.append(molecule.getNameElements().get(k).getXMLContent() + "\r\n");
                    }
                }
                subData.setChemName(names.toString());
                DBSpectrumType type = DBSpectrumTypePeer.getByName(spectrum.getMetadataListElements().get(0).getMetadataDescendantsByName("nmr:OBSERVENUCLEUS").get(0).getContent());
                subData.setChoosenSpectrumType(type);
                CMLReader cmlreader = new CMLReader(new ByteArrayInputStream(cmlmol.toXML().getBytes()));
                IMolecule cdkmol = ((ChemFileCDO) cmlreader.read(new ChemFile())).getChemSequence(0).getChemModel(0).getMoleculeSet().getMolecule(0);
                boolean[] doublebondconfigurationsprov = new boolean[cdkmol.getBondCount()];
                for (int k = 0; k < cdkmol.getBondCount(); k++) {
                    CMLBond bond = cmlmol.getBonds().get(k);
                    if (bond.getFirstChildElement("doublebondconfiguration") != null) {
                        doublebondconfigurationsprov[k] = true;
                    }
                }
                Vector toremove = new Vector();
                for (int k = 0; k < cdkmol.getBondCount(); k++) {
                    IAtom atom1 = cdkmol.getBond(k).getAtom(0);
                    if (!doublebondconfigurationsprov[k] && !BondTools.isStereo(cdkmol, atom1)) {
                        toremove.add(atom1);
                    }
                    IAtom atom2 = cdkmol.getBond(k).getAtom(1);
                    if (!doublebondconfigurationsprov[k] && !BondTools.isStereo(cdkmol, atom2)) {
                        toremove.add(atom2);
                    }
                }
                for (int k = 0; k < toremove.size(); k++) {
                    List conn = cdkmol.getConnectedAtomsList((IAtom) toremove.get(k));
                    for (int l = 0; l < conn.size(); l++) {
                        if (((IAtom) conn.get(l)).getSymbol().equals("H")) cdkmol.removeAtomAndConnectedElectronContainers((IAtom) conn.get(l));
                    }
                }
                StringWriter writer = new StringWriter();
                MDLWriter mdlwriter = new MDLWriter(writer);
                mdlwriter.write(cdkmol);
                subData.setMol(writer.toString(), false, false, true);
                boolean[] doublebondconfigurations = new boolean[subData.getMolWithH().getBondCount()];
                for (int k = 0; k < cmlmol.getBondCount(); k++) {
                    if (doublebondconfigurationsprov.length > k && doublebondconfigurationsprov[k] == true) {
                        doublebondconfigurations[k] = true;
                    }
                }
                subData.setIsDoubleBondSpecified(doublebondconfigurations);
                for (int k = 0; k < cmlmol.getAtomCount(); k++) {
                    subData.getMolWithH().getAtom(k).setID(cmlmol.getAtom(k).getId());
                    if (k < subData.getMolWithHCount().getAtomCount()) subData.getMolWithHCount().getAtom(k).setID(cmlmol.getAtom(k).getId());
                }
                CMLElements<CMLPeak> peaks = spectrum.getPeakListElements().get(0).getPeakElements();
                for (int k = 0; k < peaks.size(); k++) {
                    CMLPeak peak = peaks.get(k);
                    ValueTriple vt = new ValueTriple();
                    vt.value1 = (float) peak.getXValue();
                    if (!Double.isNaN(peak.getYValue())) {
                        vt.value2 = (float) peak.getYValue();
                    }
                    vt.multiplicityString = peak.getPeakMultiplicity();
                    String[] atomrefs = peak.getAtomRefs();
                    if (atomrefs != null) {
                        for (int l = 0; l < atomrefs.length; l++) {
                            IAtom atom = AtomContainerManipulator.getAtomById(subData.getMolWithH(), atomrefs[l]);
                            if (subData.getChoosenSpectrumType().getName().equals("1H") && !atom.getSymbol().equals("H")) {
                                Iterator it = subData.getMolWithH().getConnectedAtomsList(atom).iterator();
                                while (it.hasNext()) {
                                    IAtom connatom = (IAtom) it.next();
                                    if (connatom.getSymbol().equals("H") && !connatom.getFlag(4711)) {
                                        vt.atoms.add(connatom);
                                        connatom.setFlag(4711, true);
                                        break;
                                    }
                                }
                            } else {
                                vt.atoms.add(atom);
                            }
                        }
                        subData.getSignalstable().add(vt);
                    }
                }
                Iterator it = subData.getMolWithH().atoms();
                while (it.hasNext()) {
                    ((IAtom) it.next()).setFlag(4711, false);
                }
                for (int l = 0; l < subData.getConditions().size(); l++) {
                    Elements els = spectrum.getConditionListElements().get(0).getChildCMLElements("scalar");
                    if (((DBConditionType) subData.getConditions().get(l)).getConditionName().equals("Solvent")) {
                        for (int k = 0; k < els.size(); k++) {
                            if (els.get(k).getAttribute("dictRef") != null && els.get(k).getAttribute("dictRef").getValue().equals(NmrshiftdbConstants.solvent)) {
                                ((DBConditionType) subData.getConditions().get(l)).setCurrentValue(((CMLScalar) els.get(k)).getValue());
                                break;
                            }
                        }
                    }
                    if (((DBConditionType) subData.getConditions().get(l)).getConditionName().equals("Temperature [K]")) {
                        for (int k = 0; k < els.size(); k++) {
                            if (els.get(k).getAttribute("dictRef") != null && els.get(k).getAttribute("dictRef").getValue().equals(NmrshiftdbConstants.temperature)) {
                                ((DBConditionType) subData.getConditions().get(l)).setCurrentValue(((CMLScalar) els.get(k)).getValue());
                                break;
                            }
                        }
                    }
                    if (((DBConditionType) subData.getConditions().get(l)).getConditionName().equals("Field Strength [MHz]")) {
                        for (int k = 0; k < els.size(); k++) {
                            if (els.get(k).getAttribute("dictRef") != null && els.get(k).getAttribute("dictRef").getValue().equals(NmrshiftdbConstants.frequency)) {
                                ((DBConditionType) subData.getConditions().get(l)).setCurrentValue(((CMLScalar) els.get(k)).getValue());
                                break;
                            }
                        }
                    }
                    if (((DBConditionType) subData.getConditions().get(l)).getConditionName().equals("Assignment Method")) {
                        ((DBConditionType) subData.getConditions().get(l)).setCurrentValue(spectrum.getMetadataListElements().get(0).getMetadataDescendantsByName(NmrshiftdbConstants.assignment).get(0).getContent());
                    }
                }
                Elements els = spectrum.getChildElements("entry", "http://bibtexml.sf.net/");
                for (int k = 0; k < els.size(); k++) {
                    DBLiterature lit = null;
                    for (int m = 0; m < els.get(k).getChildElements().size(); m++) {
                        Element el = els.get(k).getChildElements().get(m);
                        if (el.getLocalName().equals("article")) {
                            lit = new DBLiterature(NmrshiftdbConstants.JOURNAL_ARTICLE);
                            lit.setAuthor(el.getChildElements("author", "http://bibtexml.sf.net/").get(0).getValue());
                            lit.setTitle(el.getChildElements("title", "http://bibtexml.sf.net/").get(0).getValue());
                            lit.journaltitle = el.getChildElements("journal", "http://bibtexml.sf.net/").get(0).getValue();
                            lit.year = Integer.parseInt(el.getChildElements("year", "http://bibtexml.sf.net/").get(0).getValue());
                            lit.volume = Integer.parseInt(el.getChildElements("volume", "http://bibtexml.sf.net/").get(0).getValue());
                            String pages = el.getChildElements("pages", "http://bibtexml.sf.net/").get(0).getValue();
                            StringBuffer sb1 = new StringBuffer();
                            StringBuffer sb2 = new StringBuffer();
                            boolean first = true;
                            for (int o = 0; o < pages.length(); o++) {
                                if (first && Character.isDigit(pages.charAt(o))) {
                                    sb1.append(pages.charAt(o));
                                } else if (first && !Character.isDigit(pages.charAt(o))) {
                                    first = false;
                                } else {
                                    sb2.append(pages.charAt(o));
                                }
                            }
                            lit.from = Integer.parseInt(sb1.toString());
                            lit.to = Integer.parseInt(sb2.toString());
                        }
                    }
                    if (lit != null) {
                        subData.getLiterature().add(lit);
                    }
                }
                subData.save(user.getUserId(), "false", "bioclipse", new Date(), null, Integer.parseInt(GeneralUtils.getNmrshiftdbProperty("hosecode.spheres")), false);
                sb.append(subData.nmrshiftdbnr + " ");
                SubmitPortlet.doReview(subData, new Date(), user, null);
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dombuilder = factory.newDocumentBuilder();
            document = dombuilder.newDocument();
            org.w3c.dom.Element root = (org.w3c.dom.Element) document.createElement("answer");
            document.appendChild(root);
            root.appendChild(document.createTextNode(sb.toString().trim()));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RemoteException(ex.getMessage(), ex);
        }
        return document;
    }

    private void namespaceThemAll(Elements elements) {
        for (int i = 0; i < elements.size(); i++) {
            Element elem = elements.get(i);
            if (!elem.getLocalName().equals("entry") && !elem.getLocalName().equals("doublebondconfiguration")) {
                elem.setNamespaceURI(CMLUtil.CML_NS);
                if (elem.getChildCount() != 0) {
                    namespaceThemAll(elem.getChildElements());
                }
            }
        }
    }

    public org.w3c.dom.Document doElucidate(org.w3c.dom.Document request) throws RemoteException {
        org.w3c.dom.Document document;
        try {
            TurbineConfig tc = new TurbineConfig("", webinfdir + "conf/TurbineResources.properties");
            File nmrpropsFile = new File(webinfdir + "conf/NMRShiftDB.properties");
            Properties nmrprops = new Properties();
            nmrprops.load(new FileInputStream(nmrpropsFile));
            GeneralUtils.nmrprops = nmrprops;
            tc.init();
            System.err.println(request);
            Document doc = DOMConverter.convert(request);
            System.err.println(doc);
            Node spectrum = doc.getChild(0).getChild(0);
            Node complete = doc.getChild(0).getChild(1);
            CMLBuilder builder = new CMLBuilder(false);
            Element element = (Element) builder.parseString(spectrum.toXML());
            namespaceThemAll(element.getChildElements());
            element.setNamespaceURI(CMLUtil.CML_NS);
            CMLSpectrum cmlspectrum = (CMLSpectrum) builder.parseString(element.toXML());
            CMLPeakList peaklist = cmlspectrum.getPeakListElements().get(0);
            StringBuffer searchSpectrumBuffer = new StringBuffer();
            for (int i = 0; i < peaklist.getPeakElements().size(); i++) {
                searchSpectrumBuffer.append((float) peaklist.getPeakElements().get(i).getXValue() + ";" + (Double.isNaN(peaklist.getPeakElements().get(i).getYValue()) ? "0" : (float) peaklist.getPeakElements().get(i).getYValue()) + (peaklist.getPeakElements().get(i).getPeakMultiplicity() != null ? peaklist.getPeakElements().get(i).getPeakMultiplicity() : "") + ";0|");
            }
            boolean sub = complete.getValue().equals("sub");
            Vector<ValuesForVelocityBean> v = new Vector<ValuesForVelocityBean>();
            if (sub) {
                v.add(new ValuesForVelocityBean(NmrshiftdbConstants.SUBSPECTRUM, "--", searchSpectrumBuffer.toString()));
            } else {
                v.add(new ValuesForVelocityBean(NmrshiftdbConstants.TOTALSPECTRUM, "--", searchSpectrumBuffer.toString()));
            }
            GeneralUtils.logToSql("Search - SELECT DISTINCT FINGERPRINTS.MOLECULE_ID, spectrumsimilarity(SPECTRUM_FINGERPRINTS.SIMPLE_SPECFILE,'" + searchSpectrumBuffer.toString() + "','total') FROM SPECTRUM_FINGERPRINTS, FINGERPRINTS WHERE SPECTRUM_FINGERPRINTS.MOLECULE_ID = FINGERPRINTS.MOLECULE_ID AND (SPECTRUM_FINGERPRINTS.NAME='13C' OR SPECTRUM_FINGERPRINTS.NAME='1H' OR SPECTRUM_FINGERPRINTS.NAME='15N' OR SPECTRUM_FINGERPRINTS.NAME='31P' OR SPECTRUM_FINGERPRINTS.NAME='19F' OR SPECTRUM_FINGERPRINTS.NAME='11B' OR SPECTRUM_FINGERPRINTS.NAME='29Si' OR SPECTRUM_FINGERPRINTS.NAME='17O' OR SPECTRUM_FINGERPRINTS.NAME='73Ge' OR SPECTRUM_FINGERPRINTS.NAME='195Pt' OR SPECTRUM_FINGERPRINTS.NAME='33S' OR false) and spectrumsimilarity(SPECTRUM_FINGERPRINTS.SIMPLE_SPECFILE, '" + searchSpectrumBuffer.toString() + "', 'total')  order by spectrumsimilarity(SPECTRUM_FINGERPRINTS.SIMPLE_SPECFILE,'" + searchSpectrumBuffer.toString() + "', 'total') desc;", null);
            Vector similarities = new Vector();
            Vector results = GeneralUtils.executeSearch(v, new int[1], new StringAndInt(), similarities, true, true, new DefaultJetspeedRunData(), null, false, false, false, DBSpectrumTypePeer.doSelect(new Criteria()), false);
            CMLCml cmlcml = new CMLCml();
            DecimalFormat format = new DecimalFormat("0.00");
            FieldPosition f = new FieldPosition(0);
            StringBuffer s = new StringBuffer();
            for (int i = 0; i < results.size(); i++) {
                if (i > 50) break;
                DBMolecule mol = DBMoleculePeer.retrieveByPK((NumberKey) results.get(i));
                CMLCml parent = (CMLCml) mol.getCML(1);
                CMLElement cmlmol = parent.getChildCMLElements().get(0);
                parent.removeChildren();
                CMLMetadataList mdl = new CMLMetadataList();
                CMLMetadata metadata = new CMLMetadata();
                metadata.setName("qname:similarity");
                metadata.setContent(format.format(((Double) similarities.get(i)).doubleValue(), s, f) + " %");
                CMLMetadata metadata2 = new CMLMetadata();
                metadata2.setName("qname:name");
                metadata2.setContent(mol.getChemicalNamesAsOneStringWithFallback());
                mdl.addMetadata(metadata);
                mdl.addMetadata(metadata2);
                cmlmol.appendChild(mdl);
                cmlcml.appendChild(cmlmol);
            }
            DocumentBuilder docbuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = docbuilder.parse(new ByteArrayInputStream(cmlcml.toXML().getBytes()));
            return document;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RemoteException(ex.getMessage(), ex);
        }
    }
}
