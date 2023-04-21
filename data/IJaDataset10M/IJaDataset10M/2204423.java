package org.fao.fenix.services.test;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import junit.framework.TestCase;
import org.fao.fenix.services.utility.UrlFinder;

public class UtilityTest extends TestCase {

    private static String servicesUrl;

    private UrlFinder test = new UrlFinder();

    public void testParseSymbol() {
        String symbol = "dataset#243658";
        String type = "";
        String localId = "";
        for (int i = 0; i < symbol.length(); i++) if (symbol.charAt(i) == '#') {
            type = symbol.substring(0, i);
            localId = symbol.substring(i + 1, symbol.length());
        }
        System.out.println(type);
        System.out.println(localId);
        assertTrue(true);
    }

    public void testGetIp() {
        assertTrue(true);
    }

    public void testFindServicesUrl() {
        assertTrue(true);
    }

    public void testGetFenixServicesUrl() {
        String url = "http://localhost:8080/fenix-web/index.html";
        StringTokenizer tokenizer = new StringTokenizer(url, "/");
        List tokens = new ArrayList();
        while (tokenizer.hasMoreTokens()) tokens.add(tokenizer.nextToken());
        String services = "fenix-services/services/CommunicationModuleService";
        String fenixServicesUrl = (String) tokens.get(0) + "//" + (String) tokens.get(1) + "/" + services;
        System.out.println(fenixServicesUrl);
    }
}
