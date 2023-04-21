package com.xenoage.zong.viewer;

import static com.xenoage.pdlib.PVector.pvec;
import static com.xenoage.util.error.Err.err;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.xenoage.pdlib.PVector;
import com.xenoage.util.FileUtils;
import com.xenoage.util.error.ErrorLevel;
import com.xenoage.util.logging.Log;
import com.xenoage.util.logging.LogLevel;
import com.xenoage.util.xml.XMLReader;
import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.instrument.InstrumentBase;
import com.xenoage.zong.core.instrument.InstrumentGroup;
import com.xenoage.zong.core.instrument.PitchedInstrument;
import com.xenoage.zong.core.instrument.Transpose;
import com.xenoage.zong.core.instrument.UnpitchedInstrument;

/**
 * This class loads all instruments from the xml-files
 * into the instruments folder in an ArrayList.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class InstrumentsPool {

    private final String instrumentspath = "data/instruments/";

    private Map<String, InstrumentGroup> groups = new HashMap<String, InstrumentGroup>();

    private ArrayList<Instrument> instruments = new ArrayList<Instrument>();

    public void loadPool() {
        String[] files = new File(instrumentspath).list(FileUtils.getXMLFilter());
        for (String file : files) {
            Log.message("Loading instrument file \"" + file + "\"...");
            Document doc = null;
            try {
                doc = XMLReader.readFile(instrumentspath + "/" + file);
            } catch (Exception e) {
                err().report(ErrorLevel.Fatal, "Instrument file could not be loaded! ", e, file);
            }
            Element root = XMLReader.root(doc);
            Element eGroupsList = XMLReader.element(root, "groups");
            List<Element> listGroups = XMLReader.elements(eGroupsList, "group");
            for (int i = 0; i < listGroups.size(); i++) {
                Element eGroup = listGroups.get(i);
                String gID = XMLReader.attribute(eGroup, "id");
                String gName = XMLReader.attribute(eGroup, "name");
                if (groups.containsKey(gID)) {
                    Log.warning("Group \"" + gID + "\" already defined. Not overwritten, but reused.");
                } else {
                    groups.put(gID, new InstrumentGroup(gID, gName));
                }
            }
            List<Element> eEntries = XMLReader.elements(root, "instrument");
            for (int i = 0; i < eEntries.size(); i++) {
                Element eInstrument = eEntries.get(i);
                String iID = XMLReader.attribute(eInstrument, "id");
                String iName = XMLReader.elementTextNotNull(eInstrument, "name");
                String iAbbr = XMLReader.elementText(eInstrument, "abbr");
                List<Element> eGroups = XMLReader.elements(XMLReader.element(eInstrument, "groups"), ("group"));
                PVector<InstrumentGroup> iGroups = pvec();
                for (int a = 0; a < eGroups.size(); a++) {
                    Element eGroup = eGroups.get(a);
                    InstrumentGroup group = groups.get(XMLReader.text(eGroup));
                    if (group == null) {
                        Log.log(LogLevel.Warning, "Unknown group \"" + XMLReader.text(eGroup) + "\" in instrument \"" + iID + "\". Ignored.");
                    } else {
                        iGroups = iGroups.plus(group);
                    }
                }
                InstrumentBase iBase = new InstrumentBase(iID, iName, iAbbr, iGroups, null, null);
                Instrument instrument;
                boolean unpitched = (XMLReader.element(eInstrument, "unpitched") != null);
                if (!unpitched) {
                    int iMidiProgram = Integer.parseInt(XMLReader.elementText(eInstrument, "midi-program"));
                    int iPolyphon = Integer.parseInt(XMLReader.elementText(eInstrument, "polyphonic"));
                    int iTranspose = Integer.parseInt(XMLReader.elementText(eInstrument, "transpose"));
                    instrument = new PitchedInstrument(iBase, iMidiProgram, new Transpose(iTranspose, 0, 0, false), null, null, iPolyphon);
                } else {
                    instrument = new UnpitchedInstrument(iBase);
                }
                instruments.add(instrument);
            }
        }
    }

    /**
	 * @return the instruments
	 */
    public ArrayList<Instrument> getInstruments() {
        return instruments;
    }

    public Instrument getInstrument(String id) {
        for (Instrument instrument : instruments) {
            if (instrument.base.id.equals(id)) return instrument;
        }
        return null;
    }
}
