package edu.upmc.opi.caBIG.common;

import org.apache.log4j.Logger;
import org.doomdark.uuid.UUIDGenerator;
import org.doomdark.uuid.UUID;

/**
 * Static Utility class for generating JUG identifiers.
 * 
 * @author mitchellkj@upmc.edu
 * @version $Id: CaBIG_UUIdGenerator.java,v 1.1 2005/12/06 18:51:54 mitchellkj
 * Exp $
 * @since 1.4.2_04
 */
public class CaBIG_UUIdGenerator {

    private static final UUIDGenerator generator = UUIDGenerator.getInstance();

    public static void main(String[] args) {
        String className = CaBIG_UUIdGenerator.class.getName();
        Logger logger = Logger.getLogger(className);
        int numberToGenerate = 10;
        if (args.length > 0) {
            String numberToGenerateAsString = args[0];
            if (numberToGenerateAsString != null && numberToGenerateAsString.length() > 0 && numberToGenerateAsString.matches("\\d+")) {
                numberToGenerate = new Integer(numberToGenerateAsString);
            }
        }
        for (int idx = 0; idx < numberToGenerate; idx++) {
            logger.info(getUUID());
        }
    }

    public static String getUUID() {
        UUID uuid = generator.generateRandomBasedUUID();
        return uuid.toString();
    }
}
