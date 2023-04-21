package net.sf.jmimemagic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 * This class is the primary class for jMimeMagic
 * 
 * @author $Author: andyscukanec $
 * @version $Revision: 5941 $
 */
public class Magic {

    private Logger log;

    private MagicParser magicParser = null;

    /**
     * constructor
     */
    public Magic(String newMagicFile) throws MagicParseException, FileNotFoundException {
        log = Logger.getLogger("net.sf.jmimemagic");
        log.debug("Magic: instantiated");
        magicParser = new MagicParser(newMagicFile);
    }

    public Magic(InputStream stream) throws MagicParseException {
        log = Logger.getLogger("net.sf.jmimemagic");
        log.debug("Magic: instantiated");
        magicParser = new MagicParser(stream);
    }

    /**
     * return the parsed MagicMatch objects that were created from the magic.xml
     * definitions
     * 
     * @return the parsed MagicMatch objects
     */
    public Collection getMatchers() {
        log.debug("Magic: getMatchers()");
        return magicParser.getMatchers();
    }

    public MagicMatch getMagicMatch(byte[] data) throws MagicMatchNotFoundException, MagicException {
        log.debug("Magic: getMagicMatch(byte[])");
        Collection matchers = magicParser.getMatchers();
        log.debug("Magic: getMagicMatch(byte[]): have " + matchers.size() + " matchers");
        MagicMatcher matcher = null;
        MagicMatch match = null;
        Iterator i = matchers.iterator();
        while (i.hasNext()) {
            matcher = (MagicMatcher) i.next();
            log.debug("Magic: getMagicMatch(byte[]): trying to match: " + matcher.getMatch().getMimeType());
            try {
                if ((match = matcher.test(data)) != null) {
                    log.debug("Magic: getMagicMatch(byte[]): matched " + matcher.getMatch().getMimeType());
                    return match;
                }
            } catch (IOException e) {
                log.error("Magic: getMagicMatch(byte[]): " + e);
                throw new MagicException(e);
            } catch (UnsupportedTypeException e) {
                log.error("Magic: getMagicMatch(byte[]): " + e);
                throw new MagicException(e);
            }
        }
        throw new MagicMatchNotFoundException();
    }

    public MagicMatch getMagicMatch(File file) throws MagicMatchNotFoundException, MagicException {
        log.debug("Magic: getMagicMatch(File)");
        Collection matchers = magicParser.getMatchers();
        log.debug("Magic: getMagicMatch(File): have " + matchers.size() + " matches");
        MagicMatcher matcher = null;
        MagicMatch match = null;
        Iterator i = matchers.iterator();
        while (i.hasNext()) {
            matcher = (MagicMatcher) i.next();
            log.debug("Magic: getMagicMatch(File): trying to match: " + matcher.getMatch().getDescription());
            try {
                if ((match = matcher.test(file)) != null) {
                    log.debug("Magic: getMagicMatch(File): matched " + matcher.getMatch().getDescription());
                    return match;
                }
            } catch (UnsupportedTypeException e) {
                log.error("Magic: getMagicMatch(File): " + e);
                throw new MagicException(e);
            } catch (IOException e) {
                log.error("Magic: getMagicMatch(File): " + e);
                throw new MagicException(e);
            }
        }
        throw new MagicMatchNotFoundException();
    }

    public String toString() {
        Collection matchers = getMatchers();
        String result = "have " + matchers.size() + " matches";
        MagicMatcher matcher = null;
        Iterator i = matchers.iterator();
        while (i.hasNext()) {
            matcher = (MagicMatcher) i.next();
            result += "printing";
            result += matcher.toString();
        }
        return result;
    }
}
