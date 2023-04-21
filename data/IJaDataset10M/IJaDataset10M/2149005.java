package org.xymos.ifilter4j.sample;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xymos.ifilter4j.DefaultIFilterEventHandler;
import org.xymos.ifilter4j.IFilter;
import org.xymos.ifilter4j.IFilterFactory;
import org.xymos.ifilter4j.contrib.IFilterHelper;

/**
 *
 * @author mschlegel
 */
public class ExtractorThread implements Runnable {

    public void run() {
        try {
            String clsid = IFilterHelper.getIFilterGUIDForExtension(".doc");
            System.out.println("Found clsid: " + clsid);
            IFilter filter = IFilterFactory.createWrapper(clsid);
            DefaultIFilterEventHandler eventHandler = new DefaultIFilterEventHandler(490);
            filter.processDocument(new File("src/sample/resources/IFilterTestDocument.doc"), eventHandler);
            filter.close();
            System.out.println("Extracted Text (" + eventHandler.getTextLength() + "): ");
            System.out.println(eventHandler.getText());
        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
