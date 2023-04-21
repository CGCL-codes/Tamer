package fr.wbr;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Christophe
 * Date: 27 ao�t 2006
 * Time: 15:58:42
 * To change this template use File | Settings | File Templates.
 */
public class SiteThreadWorldbirdnamesList extends SiteThread {

    static HashMap<String, Taxon> orderByName_ = null;

    static HashMap<String, Taxon> familyByName_ = null;

    static HashMap<String, Taxon> genusByName_ = null;

    static HashMap<String, Taxon> speciesByName_ = null;

    static HashMap<String, Taxon> taxonByName_ = null;

    static HashMap<String, String> pageNameByFamily_ = null;

    static final String headerUTF8_ = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    static final String headerIOS_ = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";

    static final String xmlName_ = "ioc-names-2.11.xml";

    static final String urlOdXml_ = "http://www.worldbirdnames.org/" + xmlName_;

    static final String wikiFranceSource = "version 2.11, 2012";

    static final String wikiCommonsSource = "ioc2.11";

    private SiteThreadWorldbirdnamesList() {
        super("Worldbirdnames List (IOC)", "IOC", new Options.WikiSite[] { Options.WikiSite.wikiFrance, Options.WikiSite.wikiCommons });
    }

    String getDescription() {
        return "Bird";
    }

    void fillWithManagedSites(HashMap<SiteThread, String> sites) {
        sites.put(SiteThreadTaxobox.getMainInstance(), "5");
        sites.put(SiteThreadTaxoboxTaxon.getMainInstance(), "0");
        sites.put(SiteThreadTaxonavigation.getMainInstance(), "5");
        sites.put(SiteThreadTaxonavigationAuthority.getMainInstance(), "0");
        sites.put(SiteThreadWorldbirdnamesListSubTaxonList.getMainInstance(), null);
        sites.put(SiteThreadCategory.getMainInstance(), null);
    }

    boolean shouldBeStarted() {
        assert (getRequest().getOptions().isWikiSiteActivated(Options.WikiSite.wikiFrance) || getRequest().getOptions().isWikiSiteActivated(Options.WikiSite.wikiCommons));
        if ((getSearchedType() != null) && (getSearchedType() != TYPE.BIRD)) {
            return false;
        }
        return true;
    }

    NonRegressionRequest[] getNonRegressionRequests() {
        return new NonRegressionRequest[] { new NonRegressionRequest("Rheiformes", "WFrance:{{Taxobox d�but | animal | ''Rheiformes'' | | | classification=COI }}\n" + "{{Taxobox | embranchement | Chordata }}\n" + "{{Taxobox | sous-embranchement | Vertebrata }}\n" + "{{Taxobox | classe | Aves }}\n" + "WFrance:{{Taxobox taxon | animal | ordre | Rheiformes | }}\n" + "Commons:Ordo|Rheiformes|\n" + "Commons:authority=}}\n" + "WFrance:Selon {{Bioref|COI|version 2.11, 2012}} :\n" + "* famille ''[[Rheidae]]''\n" + "** genre ''[[Rhea]]''\n" + "*** ''[[Rhea americana]]''\n" + "*** ''[[Rhea pennata]]''\n" + "\n" + "Commons:* {{Taxa|familiae|Rheidae|source=ioc2.11}}\n" + "Commons:* {{IOC|ratites|Rheiformes }}\n" + "WFrance:* {{COI|ratites|Rheiformes|consult� le=10 mars 2012 }}\n" + "Commons:[[Category:Aves]]", "http://www.worldbirdnames.org/n-ratites.html"), new NonRegressionRequest("Myrmia micrura", "WFrance:{{Taxobox d�but | animal | ''Myrmia micrura'' | | | classification=COI }}\n" + "{{Taxobox | embranchement | Chordata }}\n" + "{{Taxobox | sous-embranchement | Vertebrata }}\n" + "{{Taxobox | classe | Aves }}\n" + "{{Taxobox | ordre | Apodiformes }}\n" + "{{Taxobox | famille | Trochilidae }}\n" + "{{Taxobox | genre | Myrmia }}\n" + "WFrance:{{Taxobox taxon | animal | esp�ce | Myrmia micrura | }}\n" + "Commons:Genus|Myrmia|\n" + "Species|Myrmia micrura|\n" + "Commons:authority=}}\n" + "Commons:* {{Single|species|genus|Myrmia|source=ioc2.11}}\n" + "Commons:* {{IOC|swifts|Apodiformes|''Myrmia micrura'' }}\n" + "WFrance:* {{COI|swifts|Apodiformes|''Myrmia micrura''|consult� le=10 mars 2012 }}\n" + "Commons:[[Category:Trochilidae]]", "http://www.worldbirdnames.org/n-swifts.html"), new NonRegressionRequest("Hylopezus", "WFrance:{{Taxobox d�but | animal | ''Hylopezus'' | | | classification=COI }}\n" + "{{Taxobox | embranchement | Chordata }}\n" + "{{Taxobox | sous-embranchement | Vertebrata }}\n" + "{{Taxobox | classe | Aves }}\n" + "{{Taxobox | ordre | Passeriformes }}\n" + "{{Taxobox | famille | Grallariidae }}\n" + "WFrance:{{Taxobox taxon | animal | genre | Hylopezus | }}\n" + "Commons:Genus|Hylopezus|\n" + "Commons:authority=}}\n" + "WFrance:Selon {{Bioref|COI|version 2.11, 2012}} :\n" + "* ''[[Hylopezus perspicillatus]]''\n" + "* ''[[Hylopezus macularius]]''\n" + "* ''[[Hylopezus auricularis]]''\n" + "* ''[[Hylopezus dives]]''\n" + "* ''[[Hylopezus fulviventris]]''\n" + "* ''[[Hylopezus berlepschi]]''\n" + "* ''[[Hylopezus ochroleucus]]''\n" + "* ''[[Hylopezus nattereri]]''\n" + "\n" + "Commons:* {{Species|H|ylopezus|auricularis|berlepschi|dives|fulviventris|macularius|nattereri|ochroleucus|perspicillatus|source=ioc2.11}}\n" + "Commons:* {{IOC|antbirds|Passeriformes|''Hylopezus'' }}\n" + "WFrance:* {{COI|antbirds|Passeriformes|''Hylopezus''|consult� le=10 mars 2012 }}\n" + "Commons:[[Category:Grallariidae]]", "http://www.worldbirdnames.org/n-antbirds.html"), new NonRegressionRequest("Diomedeidae", "WFrance:{{Taxobox d�but | animal | ''Diomedeidae'' | | | classification=COI }}\n" + "{{Taxobox | embranchement | Chordata }}\n" + "{{Taxobox | sous-embranchement | Vertebrata }}\n" + "{{Taxobox | classe | Aves }}\n" + "{{Taxobox | ordre | Procellariiformes }}\n" + "WFrance:{{Taxobox taxon | animal | famille | Diomedeidae | }}\n" + "Commons:Familia|Diomedeidae|\n" + "Commons:authority=}}\n" + "WFrance:Selon {{Bioref|COI|version 2.11, 2012}} :\n" + "* genre ''[[Phoebastria]]''\n" + "* genre ''[[Diomedea]]''\n" + "* genre ''[[Phoebetria]]''\n" + "* genre ''[[Thalassarche]]''\n" + "\n" + "Commons:* {{Genera|Diomedea|Phoebastria|Phoebetria|Thalassarche|source=ioc2.11}}\n" + "Commons:* {{IOC|penguins|Procellariiformes|Diomedeidae }}\n" + "WFrance:* {{COI|penguins|Procellariiformes|Diomedeidae|consult� le=10 mars 2012 }}\n" + "Commons:[[Category:Procellariiformes]]", "http://www.worldbirdnames.org/n-penguins.html"), new NonRegressionRequest("Ammomanes", "WFrance:{{Taxobox d�but | animal | ''Ammomanes'' | | | classification=COI }}\n" + "{{Taxobox | embranchement | Chordata }}\n" + "{{Taxobox | sous-embranchement | Vertebrata }}\n" + "{{Taxobox | classe | Aves }}\n" + "{{Taxobox | ordre | Passeriformes }}\n" + "{{Taxobox | famille | Alaudidae }}\n" + "WFrance:{{Taxobox taxon | animal | genre | Ammomanes | }}\n" + "Commons:Genus|Ammomanes|\n" + "Commons:authority=}}\n" + "WFrance:Selon {{Bioref|COI|version 2.11, 2012}} :\n" + "* ''[[Ammomanes cinctura]]''\n" + "* ''[[Ammomanes phoenicura]]''\n" + "* ''[[Ammomanes deserti]]''\n" + "\n" + "Commons:* {{Species|A|mmomanes|cinctura|deserti|phoenicura|source=ioc2.11}}\n" + "Commons:* {{IOC|waxwings|Passeriformes|''Ammomanes'' }}\n" + "WFrance:* {{COI|waxwings|Passeriformes|''Ammomanes''|consult� le=10 mars 2012 }}\n" + "Commons:[[Category:Alaudidae]]", "http://www.worldbirdnames.org/n-waxwings.html"), new NonRegressionRequest("Christophe", null, null) };
    }

    static String synchonizator = new String("synchonizator");

    void doRequest(String taxon) {
        synchronized (synchonizator) {
            doRequest2(taxon);
        }
    }

    void doRequest2(String taxonName) {
        if (orderByName_ == null) {
            doRequestTaxonByName();
        }
        if (httpClient_ != null) {
            httpClient_.disconnect();
        }
        SiteThread.Rank searchedRank = getSearchedRank();
        Taxon taxon;
        if (searchedRank == null) {
            taxon = taxonByName_.get(taxonName);
        } else if (searchedRank == SiteThread.Rank.Species) {
            taxon = speciesByName_.get(taxonName);
        } else if (searchedRank == SiteThread.Rank.Genus) {
            taxon = genusByName_.get(taxonName);
        } else if (searchedRank == SiteThread.Rank.Family) {
            taxon = familyByName_.get(taxonName);
        } else if (searchedRank == SiteThread.Rank.Ordo) {
            taxon = orderByName_.get(taxonName);
        } else {
            return;
        }
        if (taxon == null) {
            return;
        }
        Taxon parentFamily = null;
        if (taxon.getRank() == TaxonRank.Family) {
            parentFamily = taxon;
        } else if (taxon.getRank().isStrictlyUnder(TaxonRank.Family)) {
            parentFamily = taxon.findParent(TaxonRank.Family);
        } else {
            if (taxon.getSubTaxa() != null) {
                for (Taxon child : taxon.getSubTaxa()) {
                    parentFamily = child;
                    break;
                }
            }
        }
        Taxon parentOrder = null;
        if (taxon.getRank() == TaxonRank.Order) {
            parentOrder = taxon;
        } else if (taxon.getRank().isStrictlyUnder(TaxonRank.Order)) {
            parentOrder = taxon.findParent(TaxonRank.Order);
        } else {
        }
        displayTaxobox(taxon);
        displayTaxoboxTaxon(taxon);
        displayTaxonavigation(taxon);
        displayTaxonavigationAuthority();
        displaySubTaxaOrSingle(taxon);
        displayIOCLink(parentFamily, parentOrder, taxon);
        displayCategory(taxon);
    }

    private void displayTaxobox(Taxon foundTaxon) {
        if (!isNonRegression() && !getRequest().getOptions().isSiteThreadActivated(SiteThreadTaxobox.getMainInstance())) {
            return;
        }
        TaxonList taxonList = new TaxonList();
        Taxon currentTaxon = foundTaxon;
        while (currentTaxon != null) {
            taxonList.add(0, currentTaxon);
            currentTaxon = currentTaxon.getParent();
        }
        String taxobox = taxonList.taxonListToTaxobox("animal", foundTaxon, "COI");
        displayFinalResult(Options.WikiSite.wikiFrance, SiteThreadTaxobox.getMainInstance().getResultPriority(), taxobox, false, 5);
    }

    private void displayTaxoboxTaxon(Taxon foundTaxon) {
        if (!isNonRegression() && !getRequest().getOptions().isSiteThreadActivated(SiteThreadTaxoboxTaxon.getMainInstance())) {
            return;
        }
        String taxobox = TaxonList.taxonToTaxoboxTaxon("animal", foundTaxon);
        displayFinalResult(Options.WikiSite.wikiFrance, SiteThreadTaxoboxTaxon.getMainInstance().getResultPriority(), taxobox, false, 0);
    }

    private void displayTaxonavigation(Taxon taxon) {
        if (!isNonRegression() && !getRequest().getOptions().isSiteThreadActivated(SiteThreadTaxonavigation.getMainInstance())) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if ((taxon.parentTaxa_ != null) && (taxon.getRank() == TaxonRank.Species)) {
            sb.append(taxon.parentTaxa_.getRank().getLatinSingularCapital());
            sb.append("|");
            sb.append(taxon.parentTaxa_.getName());
            sb.append("|\n");
        }
        sb.append(taxon.getRank().getLatinSingularCapital());
        sb.append("|");
        sb.append(taxon.getName());
        sb.append("|");
        displayFinalResult(Options.WikiSite.wikiCommons, SiteThreadTaxonavigation.getMainInstance().getResultPriority(), sb.toString(), false, 5);
    }

    private void displayTaxonavigationAuthority() {
        if (!isNonRegression() && !getRequest().getOptions().isSiteThreadActivated(SiteThreadTaxonavigationAuthority.getMainInstance())) {
            return;
        }
        displayFinalResult(Options.WikiSite.wikiCommons, SiteThreadTaxonavigationAuthority.getMainInstance().getResultPriority(), "authority=}}", false, 0);
    }

    private void displaySubTaxaOrSingle(Taxon taxon) {
        if (!isNonRegression() && !getRequest().getOptions().isSiteThreadActivated(SiteThreadWorldbirdnamesListSubTaxonList.getMainInstance())) {
            return;
        }
        if ((taxon.parentTaxa_ != null) && (taxon.parentTaxa_.getSubTaxa().size() == 1)) {
            StringBuilder single = new StringBuilder("* {{Single|");
            single.append(taxon.rank_.getLatinSingularLowercase());
            single.append("|");
            single.append(taxon.parentTaxa_.rank_.getLatinSingularLowercase());
            single.append("|");
            single.append(taxon.parentTaxa_.getName());
            single.append("|source=");
            single.append(wikiCommonsSource);
            single.append("}}");
            displayFinalResult(Options.WikiSite.wikiCommons, SiteThreadWorldbirdnamesListSubTaxonList.getMainInstance().getResultPriority(), single.toString(), false);
        }
        if ((taxon.getSubTaxa() != null) && (!taxon.getSubTaxa().isEmpty())) {
            StringBuilder separatorSB = new StringBuilder();
            displayFinalResult(Options.WikiSite.wikiFrance, SiteThreadWorldbirdnamesListSubTaxonList.getMainInstance().getResultPriority(), taxon.getSubTaxa().taxonListToWikiFrance("COI", wikiFranceSource, getRequest().getOptions(), separatorSB), false);
            displayFinalResult(Options.WikiSite.wikiCommons, SiteThreadWorldbirdnamesListSubTaxonList.getMainInstance().getResultPriority(), taxon.getSubTaxa().simpleClone().sort().taxonListToWikiCommons(wikiCommonsSource, null, taxon.getRank()), false);
            getRequest().enableSubTaxaSeparator(separatorSB);
            getRequest().enableNotesEtReferences();
        }
    }

    private void displayIOCLink(Taxon parentFamily, Taxon parentOrder, Taxon taxon) {
        if ((parentFamily != null) && (parentOrder != null)) {
            String pageName = pageNameByFamily_.get(parentFamily.getName());
            if (pageName != null) {
                StringBuilder command = new StringBuilder();
                command.append("* {{IOC|");
                command.append(pageName);
                command.append("|");
                command.append(parentOrder.getName());
                if (taxon.getRank() != TaxonRank.Order) {
                    command.append("|");
                    if (taxon.getRank().isGenusOrSpecies()) {
                        command.append("''");
                        command.append(taxon.getName());
                        command.append("''");
                    } else {
                        command.append(taxon.getName());
                    }
                }
                command.append(" }}");
                String commandStr = command.toString();
                displayFinalURL("http://www.worldbirdnames.org/n-" + pageName + ".html");
                displayFinalResult(Options.WikiSite.wikiCommons, commandStr, true);
                displayFinalResult(Options.WikiSite.wikiFrance, commandStr.replace("IOC", "COI").replace(" }}", consultele() + " }}"), true);
            }
        }
    }

    private void displayCategory(Taxon taxon) {
        if (isNonRegression() || getRequest().getOptions().isSiteThreadActivated(SiteThreadCategory.getMainInstance())) {
            if (taxon.parentTaxa_ != null) {
                String category = null;
                if ((taxon.parentTaxa_.getSubTaxa().size() == 1) && (taxon.parentTaxa_.parentTaxa_ != null)) {
                    category = taxon.parentTaxa_.parentTaxa_.getName();
                } else if (taxon.getRank() == TaxonRank.Species) {
                    int pos = taxon.getName().indexOf(" ");
                    if (pos > 0) {
                        category = taxon.getName().substring(0, pos).trim() + "|" + taxon.getName().substring(pos + 1).trim();
                    }
                } else {
                    category = taxon.parentTaxa_.getName();
                }
                if (category != null) {
                    displayFinalResult(Options.WikiSite.wikiCommons, SiteThreadCategory.getMainInstance().getResultPriority(), "[[Category:" + category + "]]", false);
                }
            }
        }
    }

    HTTPClient httpClient_ = null;

    HTTPClient getHTTPClient(String url) throws Exception {
        if (httpClient_ == null) {
            httpClient_ = new HTTPClient(url, this);
            httpClient_.connect("GET", null);
        } else {
            httpClient_.connect("GET", null, url, null);
        }
        return httpClient_;
    }

    void doRequestTaxonByName() {
        String url = null;
        String response = readFile();
        if (response == null) {
            try {
                url = urlOdXml_;
                HTTPClient c = getHTTPClient(url);
                int errorCode = c.getResponseCode();
                if (200 != errorCode) {
                    displayFinalError("returned errorCode " + errorCode + " on " + url);
                    return;
                }
                StringBuilder responseBuilder = new StringBuilder(5 * 1014 * 1024);
                c.displayResponse(null, responseBuilder, HTTPClient.utf8);
                response = responseBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                displayFinalError(e.getMessage());
                return;
            }
            displayTempStatus("Received page");
        }
        String message = response;
        int pos = message.indexOf(headerUTF8_);
        if (pos >= 0) {
            message = headerIOS_ + message.substring(pos + headerUTF8_.length());
        }
        Document dom = WikiUtils.parseXml(message, null);
        if (dom == null) return;
        Element divElement = dom.getDocumentElement();
        orderByName_ = new HashMap<String, Taxon>(40);
        familyByName_ = new HashMap<String, Taxon>(230);
        genusByName_ = new HashMap<String, Taxon>(2240);
        speciesByName_ = new HashMap<String, Taxon>(11000);
        taxonByName_ = new HashMap<String, Taxon>(13000);
        pageNameByFamily_ = new HashMap<String, String>(230);
        try {
            TaxonList orders = analyseXML(divElement);
            System.out.println("XML Parsing summary:");
            System.out.println("NB Order=" + orderByName_.size());
            System.out.println("NB Family=" + familyByName_.size());
            System.out.println("NB Genus=" + genusByName_.size());
            System.out.println("NB Species=" + speciesByName_.size());
            System.out.println("NB Taxon=" + taxonByName_.size());
            doChecks();
            if (url != null) {
                saveFile(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFile(String message) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(xmlName_));
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private String readFile() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(xmlName_));
            StringBuilder sb = new StringBuilder(5000 * 1024);
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    static Taxon avesTaxon_ = null;

    Taxon getAvesTaxon() {
        if (avesTaxon_ == null) {
            Taxon chordataTaxon = new Taxon("Chordata", "", TaxonRank.Phylum);
            Taxon vertebrataTaxon = new Taxon("Vertebrata", "", TaxonRank.Subphylum);
            vertebrataTaxon.setParent(chordataTaxon);
            avesTaxon_ = new Taxon("Aves", "", TaxonRank.Class);
            avesTaxon_.setParent(vertebrataTaxon);
        }
        return avesTaxon_;
    }

    void dumpFrance(TaxonList orders, boolean withGenera) {
        fillMap();
        for (Taxon order : orders) {
            int count = order.getSubTaxaCount(TaxonRank.Species);
            if (withGenera) {
                System.out.println("\n== ordre [[" + order.getName() + "]] <small>(" + count + " esp�ce" + (count > 1 ? "s" : "") + ")</small> ==");
            } else {
                System.out.println("* ordre [[" + order.getName() + "]] <small>(" + count + " esp�ce" + (count > 1 ? "s" : "") + ")</small>");
            }
            for (Taxon family : order.getSubTaxa()) {
                if (withGenera) {
                    if (family.getName().equalsIgnoreCase("Acanthisittidae")) {
                        System.out.println("=== partie 1 ===");
                    } else if (family.getName().equalsIgnoreCase("Cotingidae")) {
                        System.out.println("\n=== partie 2 ===");
                    } else if (family.getName().equalsIgnoreCase("Laniidae")) {
                        System.out.println("\n=== partie 3 ===");
                    } else if (family.getName().equalsIgnoreCase("Donacobiidae")) {
                        System.out.println("\n=== partie 4 ===");
                    } else if (family.getName().equalsIgnoreCase("Muscicapidae")) {
                        System.out.println("\n=== partie 5 ===");
                    } else if (family.getName().equalsIgnoreCase("Emberizidae")) {
                        System.out.println("\n=== partie 6 ===");
                    }
                }
                count = family.getSubTaxaCount(TaxonRank.Species);
                if (withGenera) {
                    System.out.println("* famille [[" + family.getName() + "]] <small>(" + count + " esp�ce" + (count > 1 ? "s" : "") + ")</small>");
                } else {
                    System.out.println("** famille [[" + family.getName() + "]] <small>(" + count + " esp�ce" + (count > 1 ? "s" : "") + ")</small>");
                }
                if (withGenera) {
                    for (Taxon genus : family.getSubTaxa()) {
                        count = genus.getSubTaxaCount(TaxonRank.Species);
                        String genusName = genus.getName();
                        String page = S_map.get(genusName);
                        if (page != null) {
                            genusName = page + "|" + genusName;
                        }
                        System.out.println("** genre ''[[" + genusName + "]]'' <small>(" + count + " esp�ce" + (count > 1 ? "s" : "") + ")</small>");
                    }
                }
            }
        }
        System.out.flush();
    }

    void dumpCommons(TaxonList orders, boolean withGenera) {
        fillMap();
        for (Taxon order : orders) {
            int count = order.getSubTaxaCount(TaxonRank.Species);
            if (withGenera) {
                System.out.println("\n== order [[" + order.getName() + "|]] <small>(" + count + " species)</small> ==");
            } else {
                System.out.println("* order [[:Category:" + order.getName() + "|]] <small>(" + count + " species)</small>");
            }
            for (Taxon family : order.getSubTaxa()) {
                if ((family.getName() == "Incertae sedis") && !withGenera) continue;
                count = family.getSubTaxaCount(TaxonRank.Species);
                System.out.println((withGenera ? "" : "*") + "* family [[:Category:" + family.getName() + "|]] <small>(" + count + " species)</small>");
                if (withGenera) {
                    for (Taxon genus : family.getSubTaxa()) {
                        count = genus.getSubTaxaCount(TaxonRank.Species);
                        String genusName = genus.getName();
                        String page = S_map.get(genusName);
                        if (page != null) {
                            genusName = page + "|" + genusName;
                        }
                        System.out.println((withGenera ? "" : "*") + "** genus ''[[:Category:" + genusName + "|]]'' <small>(" + count + " species)</small>");
                    }
                }
            }
        }
        System.out.flush();
    }

    TaxonList analyseXML(Element divElement) {
        NodeList nodeList = divElement.getChildNodes();
        if (nodeList == null) return null;
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            if (!(node instanceof Element)) continue;
            Element listElement = (Element) node;
            String nodeName = listElement.getNodeName();
            if (!nodeName.equalsIgnoreCase("list")) continue;
            return analyseList(listElement);
        }
        return null;
    }

    TaxonList analyseList(Element divElement) {
        NodeList nodeList = divElement.getChildNodes();
        if (nodeList == null) return null;
        TaxonList orders = new TaxonList();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node subNode = nodeList.item(i);
            if (!(subNode instanceof Element)) continue;
            Element subElement = (Element) subNode;
            String subElementName = subElement.getNodeName();
            if (!subElementName.equalsIgnoreCase("order")) continue;
            String orderName = getNodeValue(subElement, "latin_name");
            Taxon order = new Taxon(StringUtils.capitalizeAndLowercase(orderName), "", TaxonRank.Order);
            order.setParent(getAvesTaxon());
            orders.add(order);
            getAvesTaxon().addSubTaxon(order);
            orderByName_.put(order.getName(), order);
            taxonByName_.put(order.getName(), order);
            analyseOrder(order, subElement);
        }
        return orders;
    }

    void analyseOrder(Taxon order, Element divElement) {
        NodeList nodeList = divElement.getChildNodes();
        if (nodeList == null) return;
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node subNode = nodeList.item(i);
            if (!(subNode instanceof Element)) continue;
            Element subElement = (Element) subNode;
            String subElementName = subElement.getNodeName();
            if (!subElementName.equalsIgnoreCase("family")) continue;
            String familyName = getNodeValue(subElement, "latin_name");
            String url = getNodeValue(subElement, "file").replace("<![CDATA[", "").replace("]]>", "").trim();
            if (url.startsWith("n-")) {
                url = url.substring(2);
            }
            if (url.endsWith(".html")) {
                url = url.substring(0, url.length() - 5);
            }
            if (familyName.equalsIgnoreCase("Incertae Sedis")) {
                familyName = order.getName() + " incertae sedis";
                for (int j = 1; j < 100; ++j) {
                    String freeFamilyName = (j == 1 ? familyName : familyName + j);
                    if (familyByName_.get(freeFamilyName) == null) {
                        familyName = freeFamilyName;
                        break;
                    }
                }
            } else {
                familyName = StringUtils.capitalizeAndLowercase(familyName);
            }
            Taxon family = new Taxon(familyName, "", TaxonRank.Family);
            order.addSubTaxon(family);
            familyByName_.put(family.getName(), family);
            taxonByName_.put(family.getName(), family);
            pageNameByFamily_.put(family.getName(), url);
            analyseFamily(family, subElement);
        }
    }

    void analyseFamily(Taxon family, Element divElement) {
        NodeList nodeList = divElement.getChildNodes();
        if (nodeList == null) return;
        TaxonList genera = new TaxonList();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node subNode = nodeList.item(i);
            if (!(subNode instanceof Element)) continue;
            Element subElement = (Element) subNode;
            String subElementName = subElement.getNodeName();
            if (!subElementName.equalsIgnoreCase("genus")) continue;
            String genusName = getNodeValue(subElement, "latin_name");
            Taxon genus = new Taxon(StringUtils.capitalizeAndLowercase(genusName), "", TaxonRank.Genus);
            family.addSubTaxon(genus);
            genusByName_.put(genus.getName(), genus);
            taxonByName_.put(genus.getName(), genus);
            analyseGenus(genus, subElement);
        }
    }

    void analyseGenus(Taxon genus, Element divElement) {
        NodeList nodeList = divElement.getChildNodes();
        if (nodeList == null) return;
        TaxonList speciesList = new TaxonList();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node subNode = nodeList.item(i);
            if (!(subNode instanceof Element)) continue;
            Element subElement = (Element) subNode;
            String subElementName = subElement.getNodeName();
            if (!subElementName.equalsIgnoreCase("species")) continue;
            String speciesName = getNodeValue(subElement, "latin_name");
            Taxon species = new Taxon(genus.getName() + " " + speciesName.toLowerCase(), "", TaxonRank.Species);
            genus.addSubTaxon(species);
            speciesByName_.put(species.getName(), species);
            taxonByName_.put(species.getName(), species);
        }
    }

    String getNodeValue(Element element, String nodeName) {
        NodeList nodeList = element.getChildNodes();
        if (nodeList == null) return null;
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node subNode = nodeList.item(i);
            if (!(subNode instanceof Element)) continue;
            Element subElement = (Element) subNode;
            String subElementName = subElement.getNodeName();
            if (subElementName.equalsIgnoreCase(nodeName)) return subElement.getTextContent();
        }
        return null;
    }

    void doChecks() {
        if ((familyByName_ != null) && (pageNameByFamily_ != null)) {
            for (Map.Entry<String, Taxon> pair : familyByName_.entrySet()) {
                if (pageNameByFamily_.get(pair.getKey()) == null) {
                    System.out.println("Warning: family " + pair.getKey() + " does not have a page");
                }
            }
        }
    }

    private static final HashMap<String, String> S_map = new HashMap<String, String>();

    void fillMap() {
        if (!S_map.isEmpty()) return;
        S_map.put("Penelope", "Penelope (genre)");
        S_map.put("Perdix", "Perdix (genre)");
        S_map.put("Gallus", "Gallus (genre)");
        S_map.put("Chen", "Chen (genre)");
        S_map.put("Cygnus", "Cygnus (oiseau)");
        S_map.put("Aix", "Aix (oiseau)");
        S_map.put("Oxyura", "Oxyura (oiseau)");
        S_map.put("Gavia", "Gavia (genre)");
        S_map.put("Jabiru", "Jabiru (genre)");
        S_map.put("Bostrychia", "Bostrychia (oiseau)");
        S_map.put("Nycticorax", "Nycticorax (genre)");
        S_map.put("Morus", "Morus (oiseau)");
        S_map.put("Sula", "Sula (oiseau)");
        S_map.put("Vultur", "Vultur (genre)");
        S_map.put("Sagittarius", "Sagittarius (genre)");
        S_map.put("Pandion", "Pandion (genre)");
        S_map.put("Pernis", "Pernis (genre)");
        S_map.put("Circus", "Circus (genre)");
        S_map.put("Aquila", "Aquila (genre)");
        S_map.put("Caracara", "Caracara (genre)");
        S_map.put("Falco", "Falco (genre)");
        S_map.put("Otis", "Otis (oiseau)");
        S_map.put("Limosa", "Limosa (genre)");
        S_map.put("Bartramia", "Bartramia (oiseau)");
        S_map.put("Arenaria", "Arenaria (oiseau)");
        S_map.put("Rissa", "Rissa (oiseau)");
        S_map.put("Alle", "Alle (oiseau)");
        S_map.put("Columba", "Columba (genre)");
        S_map.put("Nestor", "Nestor (genre)");
        S_map.put("Eos", "Eos (genre)");
        S_map.put("Lorius", "Lorius (genre)");
        S_map.put("Vini", "Vini (genre)");
        S_map.put("Ara", "Ara (genre)");
        S_map.put("Tapera", "Tapera (genre)");
        S_map.put("Asio", "Asio (genre)");
        S_map.put("Veles", "Veles (oiseau)");
        S_map.put("Colibri", "Colibri (genre)");
        S_map.put("Elvira", "Elvira (oiseau)");
        S_map.put("Ensifera", "Ensifera (genre)");
        S_map.put("Lesbia", "Lesbia (genre)");
        S_map.put("Sappho", "Sappho (genre)");
        S_map.put("Electron", "Electron (oiseau)");
        S_map.put("Picumnus", "Picumnus (oiseau)");
        S_map.put("Corydon", "Corydon (genre)");
        S_map.put("Pitta", "Pitta (oiseau)");
        S_map.put("Batara", "Batara (genre)");
        S_map.put("Colonia", "Colonia (genre)");
        S_map.put("Legatus", "Legatus (genre)");
        S_map.put("Attila", "Attila (genre)");
        S_map.put("Tijuca", "Tijuca (genre)");
        S_map.put("Bias", "Bias (genre)");
        S_map.put("Batis", "Batis (oiseau)");
        S_map.put("Vanga", "Vanga (genre)");
        S_map.put("Pityriasis", "Pityriasis (genre)");
        S_map.put("Pitohui", "Pitohui (genre)");
        S_map.put("Arses", "Arses (genre)");
        S_map.put("Pica", "Pica (genre)");
        S_map.put("Eremophila", "Eremophila (oiseau)");
        S_map.put("Bleda", "Bleda (genre)");
        S_map.put("Iole", "Iole (genre)");
        S_map.put("Malia", "Malia (genre)");
        S_map.put("Alcippe", "Alcippe (genre)");
        S_map.put("Phyllanthus", "Phyllanthus (oiseau)");
        S_map.put("Rukia", "Rukia (genre)");
        S_map.put("Kakamega", "Kakamega (genre)");
        S_map.put("Mino", "Mino (genre)");
        S_map.put("Pastor", "Pastor (genre)");
        S_map.put("Oenanthe", "Oenanthe (oiseau)");
        S_map.put("Nigrita", "Nigrita (genre)");
        S_map.put("Vestiaria", "Vestiaria (genre)");
        S_map.put("Dives", "Dives (genre)");
        S_map.put("Tangara", "Tangara (genre)");
        S_map.put("Catamenia", "Catamenia (genre)");
        S_map.put("Glaucidium", "Glaucidium (oiseau)");
        S_map.put("Sylvia", "Sylvia (genre)");
        S_map.put("Regulus", "Regulus (oiseau)");
        S_map.put("Jynx", "Jynx (genre)");
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private static SiteThread mainInstance_ = new SiteThreadWorldbirdnamesList();

    public static SiteThread getMainInstance() {
        return mainInstance_;
    }
}
