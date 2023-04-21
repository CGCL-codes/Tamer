package org.opennms.netmgt.provision.detector;

import java.nio.charset.Charset;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.opennms.netmgt.provision.support.codec.MultilineOrientedCodecFactory;

public class SmtpDetector extends AsyncMultilineDetector {

    /**
     * @param defaultPort
     * @param defaultTimeout
     * @param defaultRetries
     */
    protected SmtpDetector() {
        super(25, 1000, 2);
        setServiceName("SMTP");
    }

    public void onInit() {
        setProtocolCodecFilter(new ProtocolCodecFilter(new MultilineOrientedCodecFactory(Charset.forName("UTF-8"), "-")));
        expectBanner(startsWith("220"));
        send(request("HELO LOCALHOST"), startsWith("250"));
        send(request("QUIT"), startsWith("221"));
    }
}
