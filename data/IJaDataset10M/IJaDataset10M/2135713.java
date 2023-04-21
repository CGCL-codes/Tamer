package org.riverock.portlet.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;
import org.apache.log4j.Logger;

public class TestCDataMarshal {

    public String text = "";

    private static Logger cat = Logger.getLogger("org.riverock.portlet.test.TestCDataMarshal");

    public TestCDataMarshal() {
    }

    public static void writeToFile(Object obj, String fileName, String encoding, String root, boolean isValidate, String namespace[][]) throws Exception {
        FileOutputStream fos = new FileOutputStream(fileName);
        Marshaller marsh = new Marshaller(new OutputStreamWriter(fos, encoding));
        if (root != null && root.trim().length() > 0) marsh.setRootElement(root);
        marsh.setValidation(isValidate);
        marsh.setMarshalAsDocument(true);
        marsh.setEncoding(encoding);
        if (namespace != null) {
            for (int i = 0; i < namespace.length; i++) {
                marsh.setNamespaceMapping(namespace[i][0], namespace[i][1]);
            }
        }
        marsh.marshal(obj);
        fos.flush();
        fos.close();
        fos = null;
    }

    public static void main(String args[]) throws Exception {
        long mills = System.currentTimeMillis();
        org.riverock.generic.startup.StartupApplication.init();
        String tempFile = "c:\\temp\\cdata-test.xml";
        TestCDataMarshal cdata = new TestCDataMarshal();
        cdata.text = "&lt;![CDATA[&lt;a href=&quot;http://baikal.askmore.info&quot;>Prebaikalsky National Park&lt;/a>]]>";
        writeToFile(cdata, tempFile, "utf-8", null, false, null);
        InputSource inCurrSrc = new InputSource(new FileInputStream(tempFile));
        TestCDataMarshal cdataResult = (TestCDataMarshal) Unmarshaller.unmarshal(TestCDataMarshal.class, inCurrSrc);
    }
}
