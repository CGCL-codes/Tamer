package org.openxml4j.opc.internal.signature;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.X509Certificate;
import org.openxml4j.exceptions.InvalidFormatException;
import org.openxml4j.exceptions.OpenXML4JException;
import org.openxml4j.exceptions.OpenXML4JRuntimeException;
import org.openxml4j.opc.Package;
import org.openxml4j.opc.PackagePart;
import org.openxml4j.opc.PackagePartName;
import org.openxml4j.opc.internal.ContentType;

/**
 * Digital certificate part.
 * 
 * @author Julien Chable
 * @version 0.1
 */
public final class DigitalCertificatePart extends PackagePart {

    private X509Certificate certificate;

    private static final ContentType contentType;

    private static final String relationshipType = "http://schemas.openxmlformats.org/package/2006/relationships/digital-signature/certificate";

    private static final String extension = ".cer";

    static {
        try {
            contentType = new ContentType("application/vnd.openxmlformats-package.digital-signature-certificate");
        } catch (InvalidFormatException e) {
            throw new OpenXML4JRuntimeException("Can't create the digital certificate part content type ! This exception should never be raise, please contact the project development team.");
        }
    }

    DigitalCertificatePart(Package parentPackage, PackagePartName partName) throws InvalidFormatException {
        super(parentPackage, partName, contentType);
    }

    X509Certificate getCertificate() {
        if (this.certificate == null) {
        }
        return this.certificate;
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }

    @Override
    protected InputStream getInputStreamImpl() throws IOException {
        return null;
    }

    @Override
    protected OutputStream getOutputStreamImpl() {
        return null;
    }

    @Override
    public boolean load(InputStream ios) throws InvalidFormatException {
        return false;
    }

    @Override
    public boolean save(OutputStream zos) throws OpenXML4JException {
        return false;
    }
}
