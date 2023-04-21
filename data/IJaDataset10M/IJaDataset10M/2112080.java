package org.expasy.jpl.core.mol.polymer.modif.unimod;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.parsers.SAXParser;
import org.expasy.jpl.commons.base.builder.BuilderException;
import org.expasy.jpl.commons.base.io.Parser;
import org.expasy.jpl.commons.collection.Pool;
import org.expasy.jpl.commons.collection.PoolImpl;
import org.expasy.jpl.core.mol.chem.ChemicalFacade;
import org.expasy.jpl.core.mol.chem.api.Molecule;
import org.expasy.jpl.core.mol.modif.Modification;
import org.expasy.jpl.core.mol.modif.ModificationFactory;
import org.expasy.jpl.core.mol.polymer.pept.matcher.AAMotifMatcher;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Parse and store {@code Modification}s in the pool handled by {@code
 * ModifManager} from the file 'unimod.xml'.
 * 
 * @author nikitin
 * 
 * @version 1.0
 * 
 */
public class UnimodParser implements Parser<BufferedReader> {

    private Log log = LogFactory.getLog(UnimodParser.class);

    /** the handler that process the parsing */
    private final UnimodParserHandler contentHandler;

    /** the sax parser that parse xml files */
    private final XMLReader parser;

    /** some modifications can undergo loss of material while fragmentation */
    @SuppressWarnings("unused")
    private boolean isLossEnabled = false;

    /** a pool of matchers */
    private Pool<AAMotifMatcher> matcherPool;

    private UnimodParser() {
        parser = new SAXParser();
        contentHandler = new UnimodParserHandler();
        parser.setContentHandler(contentHandler);
        matcherPool = PoolImpl.newInstance();
    }

    /**
	 * This static factory method makes a new instance of parser.
	 */
    public static UnimodParser newInstance() {
        return new UnimodParser();
    }

    public void parse(BufferedReader br) throws ParseException {
        try {
            parser.parse(new InputSource(br));
        } catch (IOException e) {
            throw new ParseException(e.getMessage() + ": Error reading URI", -1);
        } catch (SAXException e) {
            throw new ParseException(e.getMessage() + ": Error in parsing", -1);
        }
    }

    /**
	 * Build parsed modifications and store by the {@code JPLModifManager}.
	 * 
	 * @param manager the manager that take care of building of modifs.
	 * 
	 * @throws BuilderException if building error.
	 */
    public void buildModifs(UnimodManager manager) {
        Map<String, StringBuilder> modifs = contentHandler.getModifs();
        Map<Modification, String> redondantModifs = null;
        if (log.isDebugEnabled()) {
            redondantModifs = new HashMap<Modification, String>();
        }
        for (String modifName : modifs.keySet()) {
            StringBuilder formula = modifs.get(modifName);
            try {
                Molecule mol = ChemicalFacade.getMolecule(formula.toString());
                Modification modif = ModificationFactory.withLabel(modifName, mol);
                manager.registerModif(modif);
                if (log.isDebugEnabled()) {
                    if (redondantModifs.containsKey(modif)) {
                        log.debug(modifName + " has the same modif as " + redondantModifs.get(modif) + " (" + modif + ")");
                    } else {
                        redondantModifs.put(modif, modifName);
                    }
                }
            } catch (ParseException e) {
                throw new IllegalStateException("cannot register modif " + formula, e);
            }
        }
    }

    /**
	 * Build parsed rules and store by the {@code JPLModifManager}.
	 * 
	 * @param manager the manager that handle specificities.
	 * 
	 * @throws BuilderException if building error.
	 */
    public void buildSpecificities(UnimodManager manager) throws BuilderException {
        Map<String, Set<String>> specificities = contentHandler.getSpecificities();
        if (log.isDebugEnabled()) {
            log.debug(specificities);
        }
        for (String modifName : specificities.keySet()) {
            Set<String> modifSpecs = specificities.get(modifName);
            for (String modifSpec : modifSpecs) {
                String[] siteNPosNClass = modifSpec.split("[@:]");
                AAMotifMatcher matcher = makeMatcher(siteNPosNClass[0], siteNPosNClass[1]);
                if (!matcherPool.isRegistered(matcher)) {
                    matcherPool.register(matcher);
                }
                manager.registerSiteRuleForModif(manager.getModif(modifName), UnimodSpecificity.newInstance(matcher, siteNPosNClass[2]));
            }
        }
    }

    private AAMotifMatcher makeMatcher(String site, String pos) {
        if (site.length() != 1) {
            site = "A-Z";
        }
        AAMotifMatcher matcher = AAMotifMatcher.newInstance("[" + site + "]");
        if (pos.matches("[^N]+N-term")) {
            matcher.matchNTermOnly();
        } else if (pos.matches("[^C]+C-term")) {
            matcher.matchNTermOnly();
        }
        if (pos.matches("Protein.*")) {
            matcher.matchProteinTermOnly();
        }
        return matcher;
    }
}
