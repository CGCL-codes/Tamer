package xutools.universe.inventory.items;

import java.util.Iterator;
import java.util.LinkedList;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2Impl;
import xutools.config.ObjectTypeLibrary;
import xutools.config.race.Race;
import xutools.universe.core.Sector;
import xutools.universe.inventory.helpers.Merchandise;

/**
 * A pier where the player can buy ships (but no space stations or regular
 * merchandise items).
 * 
 * @author Tobias Weigel
 * @date 16.11.2008
 * 
 */
public class Pier extends Station {

    public Pier(Sector sector, Race race) {
        super(sector, ObjectTypeLibrary.PIER, race);
    }

    private LinkedList<Merchandise> shipsOnOffer = new LinkedList<Merchandise>();

    @Override
    public void saveToXML(TransformerHandler handler) throws SAXException {
        Attributes2Impl atts = new Attributes2Impl();
        atts.addAttribute("", "", "race", "CDATA", "" + race.getIdentifier().getName());
        atts.addAttribute("", "", "subtype", "CDATA", "" + getSubtype());
        handler.startElement("", "", "pier", atts);
        coords.saveToXML(handler);
        saveShipsOnOfferToXML(handler);
        saveDockedShipsToXML(handler);
        handler.endElement("", "", "pier");
    }

    private void saveShipsOnOfferToXML(TransformerHandler handler) throws SAXException {
        for (Merchandise m : shipsOnOffer) {
            m.saveToXML(handler);
        }
    }

    private int getSubtype() {
        switch(race.getIdentifier()) {
            case ARGON:
                return 179;
            case BORON:
                return 182;
            case TELADI:
                return 183;
            case PARANID:
                return 181;
            case SPLIT:
                return 180;
            default:
                throw new IllegalStateException("Invalid race for pier: " + race);
        }
    }

    public void addShipOnOffer(Merchandise m) {
        shipsOnOffer.add(m);
    }

    /**
     * Returns an iterator over the ships on offer.
     * 
     * @return Iterator<Merchandise>
     */
    public Iterator<Merchandise> shipsOnOfferIterator() {
        return shipsOnOffer.iterator();
    }
}
