package edu.cmu.ece.agora.kernel.router.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DataConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfGen {

    private static final String HOST_FORMAT = "node-%d.authdht.sherlock.isi.deterlab.net";

    private static final int PORT = 6666;

    private static final int NODE_START = 1;

    private static final int NODE_END = 100;

    private static final int NODE_BOOT = 1;

    private static final String KP_ALGO = "RSA";

    private static final int KP_ALGO_STR = 1024;

    /**
   * @param args
   * @throws ConfigurationException 
   * @throws ConfigurationException 
   * @throws IOException 
   * @throws FileNotFoundException 
   * @throws NoSuchAlgorithmException 
   */
    public static void main(String[] args) throws ConfigurationException, FileNotFoundException, IOException, NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(KP_ALGO);
        kpg.initialize(KP_ALGO_STR);
        String domainName = "agora.ece.cmu.edu";
        String domainKPFile = String.format("domain.kp");
        {
            KeyPair domainKP = kpg.generateKeyPair();
            ObjectOutputStream kpos = new ObjectOutputStream(new FileOutputStream(domainKPFile));
            kpos.writeObject(domainKP);
            kpos.close();
        }
        for (int i = NODE_START; i <= NODE_END; i++) {
            PropertiesConfiguration pconf = new PropertiesConfiguration();
            DataConfiguration dconf = new DataConfiguration(pconf);
            dconf.setProperty("domain.name", domainName);
            dconf.setProperty("domain.kp", "conf/" + domainKPFile);
            KeyPair kp = kpg.generateKeyPair();
            String kpfile = String.format("%d.kp", i);
            ObjectOutputStream kpos = new ObjectOutputStream(new FileOutputStream(kpfile));
            kpos.writeObject(kp);
            kpos.close();
            dconf.setProperty("local.kp", "conf/" + kpfile);
            dconf.setProperty("local.host", String.format(HOST_FORMAT, i));
            dconf.setProperty("local.port", PORT);
            if (i != NODE_BOOT) {
                int bsNode = (int) (Math.random() * (i - NODE_START)) + NODE_START;
                dconf.setProperty("remote.host", String.format(HOST_FORMAT, bsNode));
                dconf.setProperty("remote.port", PORT);
            }
            dconf.setProperty("behavior", "exit");
            pconf.save(String.format("%d.conf", i));
        }
    }
}
