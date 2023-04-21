package com.panopset.sf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.InputSource;

/**
 * build.xml ant file needs to know the current version of flywheel in order to
 * call Pom2classpathProps.
 *
 * @author Karl Dinwiddie
 *
 */
public final class FlywheelVersion {

    /**
     * Flywheel version xpath.
     */
    private static final String XPATH_FLYWHEEL_VERSION = "/project/dependencies/dependency[1]/version/text()";

    /**
     * @param args
     *            0 = pom file name. If not provided, "pom.xml".
     * @throws Exception
     *             Exception.
     */
    public static void main(final String... args) throws Exception {
        String fn = "pom.xml";
        if (args != null && args.length > 0 && args[0] != null && args[0].length() > 0) {
            fn = args[0];
        }
        FileReader fr = new FileReader(new File(fn));
        BufferedReader br = new BufferedReader(fr);
        String s = br.readLine();
        StringWriter rtn = new StringWriter();
        while (s != null) {
            rtn.append(s);
            s = br.readLine();
        }
        br.close();
        String inp = rtn.toString();
        StringWriter sw = new StringWriter();
        sw.append("<project>");
        String zonk = inp.substring(inp.indexOf("<project"));
        int i = zonk.indexOf(">");
        zonk = zonk.substring(i + 1);
        sw.append(zonk);
        System.out.println(XPathFactory.newInstance().newXPath().evaluate(XPATH_FLYWHEEL_VERSION, new InputSource(new StringReader(sw.toString()))).replace("" + (char) LF, "").replace("" + (char) CR, "").trim());
    }

    /**
     * CR.
     */
    private static final int CR = 13;

    /**
     * LF.
     */
    private static final int LF = 10;

    /**
     * Constructor.
     */
    private FlywheelVersion() {
    }
}
