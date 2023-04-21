package au.edu.uq.itee.eresearch.dimer.core.util;

import java.util.Random;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.lang.CharUtils;

public class HandleUtils {

    private static Random random = new Random();

    private static String genNum() {
        return Integer.toString(random.nextInt(10));
    }

    private static String genChar() {
        return CharUtils.toString((char) ('a' + random.nextInt(26)));
    }

    public static String generateString(Session session, String path) throws PathNotFoundException, RepositoryException {
        String handle = genChar() + genChar() + genChar() + genNum() + genNum();
        int count = 0;
        while (session.nodeExists(path + "/" + handle) && count < 100) {
            handle = genChar() + genChar() + genChar();
            for (int i = 0; i < Math.floor(count / 10.0); i++) {
                handle += genChar();
            }
            handle += genNum() + genNum();
            count++;
        }
        return handle;
    }
}
