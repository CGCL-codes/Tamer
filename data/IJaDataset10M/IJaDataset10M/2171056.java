package org.monet.kernel.library;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.monet.kernel.exceptions.DataException;

public class LibraryPDF {

    public static ByteArrayOutputStream create(String sSourceFilename) {
        ByteArrayOutputStream oOutput = new ByteArrayOutputStream();
        FopFactory oFopFactory = FopFactory.newInstance();
        TransformerFactory oFactory = TransformerFactory.newInstance();
        Fop oFop;
        Transformer oTransformer;
        Source oSource;
        Result oResult;
        try {
            oFop = oFopFactory.newFop(MimeConstants.MIME_PDF, oOutput);
            oTransformer = oFactory.newTransformer();
            oSource = new StreamSource(sSourceFilename);
            oResult = new SAXResult(oFop.getDefaultHandler());
            oTransformer.transform(oSource, oResult);
        } catch (Exception oException) {
            throw new DataException("create pdf", sSourceFilename, oException);
        }
        return oOutput;
    }
}
