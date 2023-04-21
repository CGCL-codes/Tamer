package edu.ucla.cs.typecast.rmi.server;

import java.util.Date;
import java.util.Random;

/**
 *
 * @date Aug 8, 2007
 */
public class GUIDGenerator {

    private static Random random = new Random(new Date().getTime());

    public static String createGUID(Object object) {
        int node = object.hashCode();
        return Integer.toHexString(node);
    }
}
