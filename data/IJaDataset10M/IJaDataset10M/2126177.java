package org.webdocwf.util.smime.test;

import org.webdocwf.util.smime.smime.SignedSMIME;
import org.webdocwf.util.smime.exception.SMIMEException;
import javax.mail.Transport;
import java.io.FileInputStream;
import java.io.File;

/**
 * Tests signing process. Signed text/plain message with or
 * withouth attachments can be sent by this test. To get help for this
 * example type: "java org.webdocwf.util.smime.test.TestSigned" in command line.
 * It is assumed that smime200tests.jar is in your classpath.<BR>
 * <BR>
 * Parameters passed to example are:<BR>
 * &lt;mailHost&gt; &lt;mailAddress&gt; &lt;digestAlgorithm&gt; &lt;includingCert&gt;
 * &lt;includingSignAttrib&gt; &lt;pfxFileName&gt; &lt;externalSigning&gt;
 * [&lt;attachment&gt;]<BR>
 * <BR>
 * &lt;digestAlgorithm> could be: SHA1_WITH_RSA, MD2_WITH_RSA, MD5_WITH_RSA or SHA1_WITH_DSA.<BR>
 * &lt;includingCert> could be: true/false<BR>
 * &lt;includingSignAttrib> could be: true/false<BR>
 * &lt;externalSigning> could be: true/false<BR>
 * <BR>
 * Note that for this example passwords for .pfx or .p12 files are fixed to
 * "sea1". All .pfx files or .p12 files provided with this example have this
 * password. Also, email address "FROM" is fixed to: "sender@seateam.co.yu".
 * You should change this values in source code of TestSigned.java in order to use
 * them with other .pfx or .p12 files and corresponding "FROM" addresses and passwords.
 */
public class TestSigned {

    public static void main(String[] args) {
        String subject = "S/MIME signed message - Subject test: ���������";
        String content = "S/MIME signed message example\r\nContent test: ���������!";
        String from = "sender@seateam.co.yu";
        String password = "sea1";
        if (args.length < 7) {
            System.err.println(System.getProperty("line.separator") + "Usage of TestSigned: " + System.getProperty("line.separator") + "java TestSigned <mailHost> <mailAddress> <digestAlgorithm> " + "<includingCert> <includingSignAttrib> <pfxFileName> <externalSigning>" + "[<attachment>]" + System.getProperty("line.separator") + System.getProperty("line.separator") + "Examples:" + System.getProperty("line.separator") + "java TestSigned seateam.co.yu recipient@seateam.co.yu SHA1_WITH_RSA true " + "true sender512.pfx true" + System.getProperty("line.separator") + "java TestSigned seateam.co.yu recipient@seateam.co.yu SHA1_WITH_DSA true " + "true senderDSA512.pfx false .\\test\\Zip8Test.zip");
            System.exit(-1);
        }
        String smtpHost = args[0];
        String addressTO = args[1];
        String digestAlgorithm = args[2];
        boolean includingCert = true;
        if (args[3].equalsIgnoreCase("true")) includingCert = true; else includingCert = false;
        boolean includingSignAttrib = true;
        if (args[4].equalsIgnoreCase("true")) includingSignAttrib = true; else includingSignAttrib = false;
        String pfxfileName = args[5];
        boolean externalSigning = true;
        if (args[6].equalsIgnoreCase("true")) externalSigning = true; else externalSigning = false;
        String fileName = null;
        if (args.length > 7) fileName = args[7];
        String addressCC = "recipient@seateam.co.yu";
        String addressBCC = "recipient@seateam.co.yu";
        subject = digestAlgorithm + " " + args[3] + " " + args[4] + " " + pfxfileName + " " + args[6] + " " + subject;
        SignedSMIME ss = null;
        try {
            ss = new SignedSMIME(smtpHost, from, subject, content);
            if (fileName != null) {
                ss.addAttachment(fileName);
            }
            ss.setReply(from);
            ss.addRecipient(addressTO, "TO");
            ss.setCapabilities("SYMMETRIC", 5, 3, 1, 4, 2);
            ss.setCapabilities("ENCIPHER", 1, 0, 0, 0, 0);
            ss.setCapabilities("SIGNATURE", 1, 2, 3, 4, 0);
            ss.addSigner(pfxfileName, password, digestAlgorithm, includingCert, includingSignAttrib);
            System.out.println("Creating signed message with " + digestAlgorithm + " algorithm ... ");
            ss.signing(externalSigning);
            System.out.print("Sending signed message ... ");
            ss.send();
            System.out.println("done.");
        } catch (Exception e) {
            SMIMEException.setErrorFilePath("Log");
            if (e instanceof SMIMEException) {
                ((SMIMEException) e).displayErrors(null);
            } else System.out.println(e.getMessage());
        }
    }
}
