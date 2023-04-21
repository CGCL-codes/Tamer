package fhi.bg.server;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;
import fhi.bg.server.actions.*;
import fhi.bg.fachklassen.BeerGame;
import fhi.bg.fachklassen.Spieler;

public class ActionDispatcher {

    private static ArrayList<Action> actions;

    static {
        actions = new ArrayList<Action>();
        actions.add(new S01_SpielerLoginAction());
        actions.add(new S02_SpieleruebersichtAnzeigenAction());
        actions.add(new S03_BestellungSendenAction());
        actions.add(new S04_BestaetigungSendenAction());
        actions.add(new S05_SpielerAuswertungDurchfuehrenAction());
        actions.add(new SL01_SpielleiterLoginAction());
        actions.add(new SL02_SpielErstellenAction());
        actions.add(new SL03_SpielleiteruebersichtAnzeigenAction());
        actions.add(new SL04_EreigniskarteEingebenAction());
        actions.add(new SL05_FinanzenAnzeigenAction());
        actions.add(new SL06_UnterstuetzungstoolsBeantragenAction());
        actions.add(new SL07_RundenstatusAnzeigenAction());
        actions.add(new SL08_AuswertungDurchfuehrenAction());
        actions.add(new SL09_SpielerlisteAnzeigenAction());
        actions.add(new SL10_SpielVerlassenAction());
        actions.add(new SL11_WorkshopBestaetigungSendenAction());
        actions.add(new SL12_SummaryErzeugenAction());
    }

    public static ArrayList<Action> getActions() {
        return actions;
    }

    private URL dtdLocation;

    private Spieler spieler;

    private boolean istSpielleiter;

    public ActionDispatcher() {
    }

    public void setDtdLocation(URL l) {
        this.dtdLocation = l;
    }

    /**
	 * Unterstuetzt Multithreading
	 * 
	 * @param rawXML
	 * @return
	 */
    public Document buildDocument(String rawXML) {
        Reader reader = new StringReader(rawXML);
        SAXBuilder builder = new SAXBuilder(true);
        builder.setEntityResolver(new LocalBrewerResolver(dtdLocation));
        Document document = null;
        try {
            document = builder.build(reader);
        } catch (IOException e) {
            System.err.println(e);
        } catch (JDOMException e) {
            System.err.println(e);
        }
        return document;
    }

    public synchronized String processAction(Document document) {
        int referenceNumber;
        Element[] result = new Element[] { new Fehler("Fehler", "NOP").getElement() };
        ;
        Element root = document.getRootElement();
        String auth = root.getAttributeValue("auth");
        Element actionEnvelope = root.getChild("action");
        try {
            referenceNumber = Integer.parseInt(actionEnvelope.getAttributeValue("reference"));
        } catch (NumberFormatException e) {
            referenceNumber = 0;
        }
        if (!verifyAuthentification(auth) && actionEnvelope.getChild("spielleiterLogin") == null && actionEnvelope.getChild("spielerLogin") == null) {
            result = new Element[] { new Fehler("Fehler", "Nicht angemeldet").getElement() };
            return createResponse(result, referenceNumber);
        }
        for (Action a : actions) {
            Element action;
            if ((action = actionEnvelope.getChild(a.getActionXMLName())) != null) {
                if (a.istSpielleiterAction() && !this.istSpielleiter) {
                    result = new Element[] { new Fehler("Fehler", "Keine Berechtigung").getElement() };
                    return createResponse(result, referenceNumber);
                }
                a.setSpieler(spieler);
                result = a.execute(action);
                if (!a.istReadonly()) {
                    BeerGame.get().databaseUpdateAndCommit();
                }
                break;
            }
        }
        return createResponse(result, referenceNumber);
    }

    private boolean verifyAuthentification(String auth) {
        if (BeerGame.get().getAktuellesSpiel() != null) {
            if (BeerGame.get().getAktuellesSpiel().findSpielerByToken(auth) != null) {
                this.istSpielleiter = false;
                spieler = BeerGame.get().getAktuellesSpiel().findSpielerByToken(auth);
                return true;
            }
        }
        if (auth.equals(BeerGame.get().getSpielleiterPasswort())) {
            this.istSpielleiter = true;
            spieler = null;
            return true;
        }
        return false;
    }

    public String createResponse(Element[] result, int referenceNumber) {
        Element root = new Element("response");
        Element action = new Element("action");
        action.setAttribute("reference", new Integer(referenceNumber).toString());
        for (Element e : result) {
            action.addContent(e);
        }
        root.addContent(action);
        Document response = new Document(root);
        response.setDocType(new DocType("response", "-//FH Ingolstadt//Beer Game Response DTD 1.0//DE", "http://fhi-beer-game.sourceforge.net/dtd/1.0/drinker.dtd"));
        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        XMLOutputter xml = new XMLOutputter(format);
        return xml.outputString(response);
    }
}
