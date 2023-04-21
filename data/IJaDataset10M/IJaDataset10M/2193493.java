package org.owasp.orizon.demo;

import org.owasp.orizon.About;
import org.owasp.orizon.OrizonLog;
import org.owasp.orizon.java.Java2XML;

public class Java2XMLDemo {

    public static void main(final String[] args) {
        OrizonLog log = new OrizonLog(Java2XMLDemo.class);
        log.info(About.about());
        if (args.length != 1) {
            log.error("missing file name.");
            log.info("usage: java -cp .:path_to_log4j/log4j.jar:path_to_orizon_demodi/orizon_demo.jar file_to_test");
            System.exit(-1);
        }
        try {
            Java2XML j2x = new Java2XML(args[0]);
            j2x.translate();
            log.info(j2x.getOutputFilename() + "created.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
