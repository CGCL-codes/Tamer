package org.jtools.protocol.samples.helloworld.server;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.security.auth.Subject;
import org.jtools.protocol.server.Job;
import org.jtools.protocol.server.impl.ProtocolServerImpl;
import org.jtools.protocol.server.service.ProtocolServiceHandshake;
import org.jtools.protocol.server.service.ProtocolService;
import org.jtools.server.Server;
import org.jtools.server.auth.AlternativeAuthenticator;
import org.jtools.server.auth.Authenticator;
import org.jtools.server.auth.CertificateAuthenticator;
import org.jtools.server.auth.DigestAuthenticator;
import org.jtools.server.auth.DigestPrincipal;
import org.jtools.server.auth.SimpleUserMap;
import org.jtools.server.config.ConfigurationHelper;
import org.jtools.server.config.ListenerConfiguration;
import org.jtools.server.impl.ServerImpl;
import org.jtools.util.auth.digest.Qop;
import org.jtools.util.ssl.SSLContextUtil;

public class HelloWorldServer {

    private static final String realmname = "user@mydomain.com";

    private static final String digest = "MD5";

    private static <O> Authenticator<O> auth() throws IOException {
        SimpleUserMap realm = new SimpleUserMap(realmname, true);
        Subject user = new Subject();
        user.getPrincipals().add(DigestPrincipal.newInstance(realm, digest, "testuser", "testpassword"));
        realm.add(user);
        user = new Subject();
        user.getPrincipals().add(DigestPrincipal.newInstance(realm, digest, "admin", "adminpassword"));
        realm.add(user);
        DigestAuthenticator<O> da = new DigestAuthenticator<O>(realm, null, false, null, Qop.auth);
        CertificateAuthenticator<O> ca = new CertificateAuthenticator<O>(realm);
        return new AlternativeAuthenticator<O>(ca, da);
    }

    public static void main(String[] args) {
        try {
            URL keystorefile = HelloWorldServer.class.getClassLoader().getResource("serverkeystore");
            SSLContext ssl = SSLContextUtil.newTLSInstance(new File(keystorefile.toURI()).toString(), "serverpass");
            Authenticator<?> auth = auth();
            Server server = new ServerImpl();
            ProtocolService service = new ProtocolService(new ProtocolServerImpl());
            server.addService(service);
            ConfigurationHelper h = server.getListenerConfigurationHelper();
            h.setNeedClientAuth(false);
            h.setSslContext(ssl);
            h.setBindAddress(InetAddress.getByName("localhost"));
            h.setUseWellKnownPorts(true);
            h.setAuthenticator(auth);
            h.setHandshake(new ProtocolServiceHandshake());
            server.startServices();
            h.setDefaultService(service);
            server.startListener(h.newInstance("plainssl"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
