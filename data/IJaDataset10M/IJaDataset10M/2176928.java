package aurora.plugin.bill99.pos;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import aurora.plugin.bill99.Configuration;

public class CerEncode {

    public boolean enCodeByCer(String val, String msg) {
        boolean flag = false;
        try {
            String verifyFile = getValue("pos_cp_verify_cer_file");
            String fStr = CerEncode.class.getClassLoader().getResource(verifyFile).getPath();
            InputStream inStream = new FileInputStream(fStr);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
            PublicKey pk = cert.getPublicKey();
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(pk);
            signature.update(val.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private static String getValue(String key) {
        String value = Configuration.getInstance().getValue(key);
        return value == null ? "" : value;
    }
}
