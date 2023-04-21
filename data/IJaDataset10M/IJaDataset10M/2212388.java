package org.owasp.oss.client;

import java.security.Security;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.owasp.oss.client.command.CertificateSignRequest;
import org.owasp.oss.client.command.GetCertificate;
import org.owasp.oss.client.command.VerifyChain;
import org.owasp.oss.client.command.CommandInterface;

public class OSSJClientMain {

    private static Map<String, CommandInterface> _commands;

    private static void init() {
        Security.addProvider(new BouncyCastleProvider());
        _commands = new HashMap<String, CommandInterface>();
        _commands.put("csr", new CertificateSignRequest());
        _commands.put("getcert", new GetCertificate());
        _commands.put("verifycert", new VerifyChain());
    }

    private static Map<String, String> buildParameterMap(String[] args) {
        Map<String, String> parameter = new HashMap<String, String>();
        for (int i = 0; i < args.length; ++i) {
            if (args[i] != null && args[i].startsWith("-")) {
                parameter.put(args[i].replaceFirst("-", ""), args[i + 1]);
                ++i;
            }
        }
        return parameter;
    }

    public static void printUsage() {
        System.out.println("Usage: java -jar OSSJClient-[version].jar [command]");
        System.out.println("Following commads supported:");
        Set<String> keys = _commands.keySet();
        Iterator<String> i = keys.iterator();
        while (i.hasNext()) {
            String key = i.next();
            System.out.println("\t" + key + " - " + _commands.get(key).getDescription());
        }
    }

    public static void main(String[] args) {
        System.out.println("Opensign Java Client Tool started");
        init();
        if (args.length < 1) {
            printUsage();
            return;
        }
        CommandInterface command = _commands.get(args[0]);
        if (command != null) {
            command.execute(buildParameterMap(args));
        } else {
            printUsage();
        }
    }
}
