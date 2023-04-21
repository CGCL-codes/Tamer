package br.net.woodstock.rockframework.security.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import junit.framework.TestCase;
import br.net.woodstock.rockframework.security.Alias;
import br.net.woodstock.rockframework.security.cert.ExtendedKeyUsageType;
import br.net.woodstock.rockframework.security.cert.KeyUsageType;
import br.net.woodstock.rockframework.security.cert.PrivateKeyHolder;
import br.net.woodstock.rockframework.security.cert.impl.BouncyCastleCertificateBuilder;
import br.net.woodstock.rockframework.security.sign.PKCS7SignatureParameters;
import br.net.woodstock.rockframework.security.sign.Signature;
import br.net.woodstock.rockframework.security.sign.impl.PDFSigner;
import br.net.woodstock.rockframework.security.store.KeyStoreType;
import br.net.woodstock.rockframework.security.store.PasswordAlias;
import br.net.woodstock.rockframework.security.store.PrivateKeyEntry;
import br.net.woodstock.rockframework.security.store.Store;
import br.net.woodstock.rockframework.security.store.impl.JCAStore;
import br.net.woodstock.rockframework.security.timestamp.TimeStamp;
import br.net.woodstock.rockframework.security.timestamp.TimeStampClient;
import br.net.woodstock.rockframework.security.timestamp.TimeStampServer;
import br.net.woodstock.rockframework.security.timestamp.impl.BouncyCastleTimeStampServer;
import br.net.woodstock.rockframework.security.timestamp.impl.LocalTimeStampClient;
import br.net.woodstock.rockframework.utils.IOUtils;

public class TimeStampServerTest extends TestCase {

    private TimeStampServer getTimeStampServer() throws Exception {
        Store store = new JCAStore(KeyStoreType.PKCS12);
        store.read(new FileInputStream("/tmp/tsaserver.pfx"), "tsaserver");
        Alias alias = new PasswordAlias("tsaserver", "tsaserver");
        TimeStampServer server = new BouncyCastleTimeStampServer(store, alias);
        return server;
    }

    public void xtestCreateCert() throws Exception {
        BouncyCastleCertificateBuilder builder1 = new BouncyCastleCertificateBuilder("Lourival Sabino - TSA Server");
        builder1.withIssuer("Woodstock Tecnologia");
        builder1.withV3Extensions(true);
        builder1.withKeyUsage(KeyUsageType.DIGITAL_SIGNATURE, KeyUsageType.NON_REPUDIATION, KeyUsageType.KEY_AGREEMENT);
        builder1.withExtendedKeyUsage(ExtendedKeyUsageType.TIMESTAMPING);
        PrivateKeyHolder holder = builder1.build();
        Store store = new JCAStore(KeyStoreType.PKCS12);
        Alias alias = new PasswordAlias("tsaserver", "tsaserver");
        PrivateKeyEntry privateKeyEntry = new PrivateKeyEntry(alias, holder.getPrivateKey(), holder.getChain());
        store.add(privateKeyEntry);
        store.write(new FileOutputStream("/tmp/tsaserver.pfx"), "tsaserver");
    }

    public void xtest1() throws Exception {
        TimeStampServer server = this.getTimeStampServer();
        TimeStampClient client = new LocalTimeStampClient(server);
        FileInputStream inputStream = new FileInputStream("/home/lourival/Documentos/curriculum.pdf");
        byte[] input = IOUtils.toByteArray(inputStream);
        TimeStamp timeStamp = client.getTimeStamp(input);
        byte[] bytes = timeStamp.getEncoded();
        FileOutputStream outputStream = new FileOutputStream("/tmp/curriculum.pdf.p7s");
        outputStream.write(bytes);
        outputStream.close();
    }

    public void test2() throws Exception {
        JCAStore store = new JCAStore(KeyStoreType.JKS);
        store.read(new FileInputStream("/home/lourival/Downloads/LOURIVALSABINO.jks"), "storepasswd");
        FileInputStream fileInputStream = new FileInputStream("/home/lourival/Documentos/curriculum.pdf");
        TimeStampServer timeStampServer = this.getTimeStampServer();
        TimeStampClient timeStampClient = new LocalTimeStampClient(timeStampServer);
        PKCS7SignatureParameters signerInfo = new PKCS7SignatureParameters(new Alias[] { new PasswordAlias("lourival sabino", "lourival") }, store);
        signerInfo.setContactInfo("ConcactInfo");
        signerInfo.setLocation("Location");
        signerInfo.setName("Lourival Sabino");
        signerInfo.setReason("Reason");
        signerInfo.setTimeStampClient(timeStampClient);
        PDFSigner signer = new PDFSigner(signerInfo);
        byte[] signed = signer.sign(IOUtils.toByteArray(fileInputStream));
        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/teste-demo.pdf");
        fileOutputStream.write(signed);
        fileInputStream.close();
        fileOutputStream.close();
        Signature[] signatures = signer.getSignatures(signed);
        for (Signature signature : signatures) {
            TimeStamp timeStamp = signature.getTimeStamp();
            if (timeStamp != null) {
                FileOutputStream outputStream = new FileOutputStream("/tmp/teste-demo.pdf.p7m");
                outputStream.write(timeStamp.getEncoded());
                outputStream.close();
            }
            FileOutputStream outputStream = new FileOutputStream("/tmp/teste-demo.pdf.p7s");
            outputStream.write(signature.getEncoded());
            outputStream.close();
        }
    }
}
