package cbr2.service;

import cbr2.FeatureVector;
import cbr2.util.CBRConfig;
import DE.FhG.IGD.util.*;
import DE.FhG.IGD.semoa.net.*;
import DE.FhG.IGD.semoa.server.*;
import DE.FhG.IGD.semoa.security.*;
import codec.asn1.ASN1Exception;
import java.io.*;
import java.util.*;
import java.net.MalformedURLException;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;
import java.awt.Image;
import java.awt.Dimension;
import javax.swing.*;
import DE.siemens.ct.pmap.authorization.TicketCheckException;
import DE.siemens.ct.pmap.authorization.asn1.*;
import DE.siemens.ct.pmap.authorization.TicketRequestException;
import DE.siemens.ct.pmap.authorization.service.TicketCheckInterface;
import DE.siemens.ct.pmap.authorization.service.TicketRequestInterface;

/**
 * Extends the original picutre central in a way that authorization
 * tokens can can be used.  
 */
public class BrokerPictureCentral extends PictureCentral implements PicsIndex, PicsFinder, BrokerService {

    private String localURL_;

    private JTextArea log_;

    private URL url_;

    private Object lock_;

    public BrokerPictureCentral() {
        super();
        lock_ = new Object();
        createLogWindow();
        setLocalURL();
    }

    public BrokerPictureCentral(PicsStore store) {
        super(store);
        lock_ = new Object();
        createLogWindow();
        setLocalURL();
    }

    /** 
     * This implementation of {@link
     * PictureCentral.find(FeatureVector,float,int) find} replaces the
     * original image source url with the broker's location before
     * handing over the picture entries to the agent.  
     */
    public PictureEntry[] find(FeatureVector v, float threshold, int max) throws IOException {
        PictureEntry pe[];
        int i;
        pe = super.find(v, threshold, max);
        for (i = 0; i < pe.length; i++) {
            pe[i].url = localURL_;
        }
        return pe;
    }

    /** 
     * This implementation of {@link
     * PictureCentral.find(FeatureVector,float,int) find} replaces the
     * original image source url with the broker's location before
     * handing over the picture entries to the agent.  
     */
    public PictureEntry[] find(Image im, float threshold, int max) throws IOException {
        PictureEntry pe[];
        int i;
        pe = super.find(im, threshold, max);
        for (i = 0; i < pe.length; i++) {
            pe[i].price = String.valueOf((float) (pe[i].thumbnail.length / 10) / 100f);
            pe[i].url = localURL_;
        }
        return pe;
    }

    /**
     * This implementation of {@link
     * PictureCentral.find(Image,float,int) find} adds the price to
     * the picture entries before adding them to the index store. 
     */
    public void put(Iterator entries) throws IOException {
        PictureEntry entry;
        LinkedList list;
        list = new LinkedList();
        while (entries.hasNext()) {
            entry = (PictureEntry) entries.next();
            entry.price = String.valueOf((float) (entry.thumbnail.length / 10) / 100f);
            list.add(entry);
        }
        super.put(list.iterator());
    }

    public ImageGrant getToken(String image) {
        return getToken(image, null);
    }

    public ImageGrant getToken(String image, Set subscriptions) {
        byte[] token;
        String dname;
        String cname;
        int startOffset;
        int endOffset;
        String url;
        String price;
        String msg;
        PicsIterator pi;
        PictureEntry entry;
        synchronized (lock_) {
            pi = store_.iterator();
            url = null;
            price = null;
            while (pi.hasNext()) {
                entry = (PictureEntry) pi.next();
                if (image.equals(entry.name)) {
                    url = entry.url;
                    price = entry.price;
                    break;
                }
            }
            try {
                pi.close();
            } catch (IOException ex) {
            }
            if (url == null || price == null) {
                System.out.println("Not all information for " + image + " available. This should not happen.\n" + "Price = " + price + ", URL = " + url);
                return null;
            }
        }
        token = null;
        msg = "Bildkauf von " + getUserCName() + " ist fehlgeschlagen. " + " Kein Ticket vom TGS erhalten.";
        if (subscriptions != null && subscriptions.size() > 0) {
            System.out.println("Agent has " + subscriptions.size() + " subscription(s)");
            token = retrieveTokenFromSubscription(image, url, subscriptions);
            msg = getUserCName() + " bezog Bild via Abo am " + new Date();
        }
        if (token == null) {
            System.out.println("Agent has to buy the image.");
            token = retrieveToken(image, url);
            msg = getUserCName() + " kaufte Bild \"" + image + "\" fuer " + price + " Euro am " + new Date();
        }
        if (token != null) {
            log(msg);
        }
        return new ImageGrant(url, token);
    }

    private byte[] retrieveToken(String image, String url) {
        TicketRequestInterface trs;
        Environment env;
        String realm;
        String ipname;
        long endtime;
        boolean allowProxy;
        String[] readFileAccess;
        String[] writeFileAccess;
        String[] deleteFileAccess;
        int count;
        String delegee;
        boolean isSubscription;
        ByteArrayOutputStream bos;
        Map hosts;
        Iterator it;
        String key;
        URL val;
        Vicinity vicinity;
        byte[] token;
        long duration;
        String durString;
        KeyMaster keymaster;
        X509Certificate cert;
        env = Environment.getEnvironment();
        trs = (TicketRequestInterface) env.lookup(WhatIs.stringValue("TICKET_REQUEST"));
        if (trs == null) {
            System.out.println("Ticket request service not found!");
            return null;
        }
        realm = cbr2.util.CBRConfig.getProperty("cbr.tgs.realm");
        try {
            vicinity = (Vicinity) Environment.getEnvironment().lookup(WhatIs.stringValue("VICINITY"));
        } catch (Exception e) {
            System.err.println("Fatal error: Could not access vicinity at " + WhatIs.stringValue("VICINITY") + ":");
            return null;
        }
        hosts = vicinity.getContactTable();
        it = hosts.keySet().iterator();
        ipname = null;
        while (it.hasNext()) {
            key = (String) it.next();
            val = (URL) hosts.get(key);
            if (val.equals(url)) {
                ipname = key;
                break;
            }
        }
        if (ipname == null) {
            try {
                keymaster = (KeyMaster) env.lookup(WhatIs.stringValue("KEYMASTER"));
                cert = ((X509Certificate) keymaster.getCertificate(KeyMaster.CRYPT_KEY));
                ipname = cert.getSubjectDN().getName();
            } catch (CertificateException ex) {
                System.out.println("Own signing certificate not found");
                ipname = null;
            }
        }
        durString = CBRConfig.getProperty("cbr.subscription.duration");
        durString = durString.trim();
        try {
            duration = Long.parseLong(durString);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid ticket duration string '" + durString + "': " + ex.getMessage() + "\nUsing default 300000.");
            duration = 300000;
        }
        endtime = System.currentTimeMillis() + duration;
        allowProxy = false;
        readFileAccess = new String[] { image };
        deleteFileAccess = null;
        writeFileAccess = null;
        count = 1;
        delegee = null;
        isSubscription = false;
        bos = new ByteArrayOutputStream();
        try {
            trs.getProxyTicketDirect(realm, ipname, endtime, allowProxy, readFileAccess, writeFileAccess, deleteFileAccess, count, delegee, isSubscription, bos);
            token = bos.toByteArray();
        } catch (TicketRequestException ex1) {
            System.out.println("No ticket received: " + ex1.getMessage());
            return null;
        }
        return token;
    }

    private byte[] retrieveTokenFromSubscription(String image, String url, Set subscriptions) {
        Environment env;
        AccessRight access;
        Collection readFiles;
        TicketCheckInterface tcs;
        byte[] subToken;
        byte token[];
        Iterator it;
        boolean valid;
        env = Environment.getEnvironment();
        tcs = (TicketCheckInterface) env.lookup(WhatIs.stringValue("TICKET_CHECK"));
        if (tcs == null) {
            System.out.println("Ticket check service not found!");
            return null;
        }
        it = subscriptions.iterator();
        valid = false;
        while (it.hasNext()) {
            subToken = ((Subscription) it.next()).getToken();
            try {
                readFiles = new HashSet();
                readFiles.add(new KerberosString("abo"));
                access = new AccessRight(new KerberosStringSeq(readFiles), null, null);
                tcs.checkTicket(access, new ByteArrayInputStream(subToken), null);
                valid = true;
                break;
            } catch (ASN1Exception ex1) {
                System.out.println("ASN1Exception caught: " + ex1.getMessage());
            } catch (TicketCheckException ex2) {
                System.out.println("Ticket not valid!");
            } catch (Throwable t) {
                System.out.println("Unexpected throwable caught: " + t);
            }
        }
        if (valid) {
            System.out.println("Agent has a valid subscription.");
            token = retrieveToken(image, url);
        } else {
            token = null;
        }
        return token;
    }

    public Subscription getSubscription() {
        TicketRequestInterface trs;
        Environment env;
        String realm;
        String ipname;
        long endtime;
        boolean allowProxy;
        String[] readFileAccess;
        String[] writeFileAccess;
        String[] deleteFileAccess;
        int count;
        String delegee;
        boolean isSubscription;
        ByteArrayOutputStream bos;
        Map hosts;
        Iterator it;
        String key;
        URL val;
        Vicinity vicinity;
        byte[] token;
        boolean isUser;
        KeyMaster keymaster;
        X509Certificate cert;
        long duration;
        String durString;
        env = Environment.getEnvironment();
        trs = (TicketRequestInterface) env.lookup(WhatIs.stringValue("TICKET_REQUEST"));
        if (trs == null) {
            System.out.println("Ticket request service not found!");
            return null;
        }
        realm = cbr2.util.CBRConfig.getProperty("cbr.tgs.realm");
        try {
            keymaster = (KeyMaster) env.lookup(WhatIs.stringValue("KEYMASTER"));
            cert = ((X509Certificate) keymaster.getCertificate(KeyMaster.CRYPT_KEY));
            ipname = cert.getSubjectDN().getName();
        } catch (CertificateException ex) {
            System.out.println("Own signing certificate not found");
            ipname = null;
        }
        durString = CBRConfig.getProperty("cbr.subscription.duration");
        durString = durString.trim();
        try {
            duration = Long.parseLong(durString);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid ticket duration string '" + durString + "': " + ex.getMessage() + "\nUsing default 600000.");
            duration = 600000;
        }
        endtime = System.currentTimeMillis() + duration;
        allowProxy = false;
        readFileAccess = new String[] { "abo" };
        deleteFileAccess = null;
        writeFileAccess = null;
        count = -1;
        isUser = true;
        delegee = null;
        isSubscription = false;
        bos = new ByteArrayOutputStream();
        try {
            trs.getServiceTicket(realm, ipname, endtime, allowProxy, readFileAccess, writeFileAccess, deleteFileAccess, count, isUser, delegee, isSubscription, bos);
            token = bos.toByteArray();
            bos.close();
            log(getUserCName() + " kaufte ein Abonnement fuer " + CBRConfig.getProperty("cbr.subscription.price") + " Euro am " + new Date());
            return new Subscription(token, new URL(localURL_), new Date(endtime));
        } catch (TicketRequestException ex1) {
            System.out.println("No subscription received: " + ex1.getMessage());
        } catch (MalformedURLException ex3) {
            System.out.println("Malformed URL: " + localURL_);
        } catch (IOException ex2) {
            System.out.println("IOException: " + ex2.getMessage());
        }
        return null;
    }

    private String getUserCName() {
        String dname;
        String cname;
        int endOffset;
        int startOffset;
        try {
            dname = Mobility.getContext().getCard().getCertificate().getSubjectDN().getName();
        } catch (CertificateException ex) {
            return "-UNKNOWN-";
        }
        startOffset = dname.indexOf("CN=");
        if (startOffset == -1) {
            cname = dname;
        } else {
            endOffset = dname.indexOf(",", startOffset);
            if (endOffset == -1) {
                cname = dname.substring(startOffset + 3);
            } else {
                cname = dname.substring(startOffset + 3, endOffset);
            }
        }
        return cname;
    }

    private void setLocalURL() {
        Environment env;
        RawInGate raw;
        String key;
        try {
            env = Environment.getEnvironment();
            key = WhatIs.stringValue("INGATE");
            raw = (RawInGate) env.lookup(key + "/raw");
            localURL_ = raw.localURL().toString();
        } catch (Exception e) {
            System.err.println("Fatal Error: Cannot get local URL from RAW. " + "Service will not function properly!");
            e.printStackTrace();
        }
    }

    private void createLogWindow() {
        JScrollPane scroll;
        JFrame frame;
        frame = new JFrame("Abgeschlossene Kaufvertraege");
        log_ = new JTextArea();
        log_.setPreferredSize(new Dimension(300, 300));
        scroll = new JScrollPane(log_, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.getContentPane().add(scroll);
        frame.pack();
        frame.setVisible(true);
    }

    private void log(String s) {
        log_.append("\n" + s);
        log_.setCaretPosition(log_.getDocument().getLength());
    }
}
