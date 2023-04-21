package org.tastefuljava.pay;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.CollectionStore;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;

public class PaymentService {

    private static final Logger LOG = Logger.getLogger(PaymentService.class.getName());

    private static final Map<String, String> PAYPAL_LANGUAGES;

    private static final String PDT_PATH = "/cgi-bin/webscr";

    private static final Pattern CONTENT_TYPE_PATTERN = Pattern.compile("^[a-zA-Z_0-9+-]+/[a-zA-Z_0-9+-]+\\s*" + "(?:;\\s*charset=([a-zA-Z_0-9+-]+))$");

    private URL confURL;

    private String baseURL;

    private String business;

    private String certId;

    private PrivateKey ownKey;

    private X509Certificate ownCert;

    private X509Certificate paypalCert;

    private String identityToken;

    private int connectTimeout;

    private int readTimeout;

    public static PaymentService newInstance(URL confURL) throws IOException {
        return new PaymentService(confURL);
    }

    private PaymentService(URL confURL) throws IOException {
        try {
            this.confURL = confURL;
            Properties props = loadProps(confURL);
            baseURL = props.getProperty("base-url");
            business = props.getProperty("business");
            certId = props.getProperty("cert-id");
            identityToken = props.getProperty("identity-token");
            connectTimeout = Integer.parseInt(props.getProperty("connect-timeout"));
            readTimeout = Integer.parseInt(props.getProperty("read-timeout"));
            KeyStore ks = KeyStore.getInstance("JKS");
            URL url = getResource(props.getProperty("key-store"));
            InputStream in = url.openStream();
            try {
                ks.load(in, null);
            } finally {
                in.close();
            }
            String keyAlias = props.getProperty("key-alias");
            String keyPwd = props.getProperty("key-password");
            ownKey = (PrivateKey) ks.getKey(keyAlias, keyPwd.toCharArray());
            ownCert = (X509Certificate) ks.getCertificate(keyAlias);
            String ppCertAlias = props.getProperty("paypal-cert-alias");
            paypalCert = (X509Certificate) ks.getCertificate(ppCertAlias);
        } catch (CertificateException e) {
            LOG.log(Level.SEVERE, "Error loading key store", e);
            throw new IOException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            LOG.log(Level.SEVERE, "Error loading key store", e);
            throw new IOException(e.getMessage());
        } catch (UnrecoverableKeyException e) {
            LOG.log(Level.SEVERE, "Error loading key store", e);
            throw new IOException(e.getMessage());
        } catch (KeyStoreException e) {
            LOG.log(Level.SEVERE, "Error loading key store", e);
            throw new IOException(e.getMessage());
        }
    }

    public String getBaseURL() {
        return baseURL;
    }

    public PaymentButton createButton() {
        return new PaymentButton(this);
    }

    public String encrypt(PaymentButton button) {
        try {
            String params = buttonParams(button);
            return encrypt(params.getBytes("UTF-8"));
        } catch (OperatorCreationException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } catch (GeneralSecurityException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public Map<String, String> getPaymentInfo(String txid) throws IOException {
        URL uploadUrl = new URL(baseURL + PDT_PATH);
        HttpURLConnection con = (HttpURLConnection) uploadUrl.openConnection();
        try {
            StringBuilder buf = new StringBuilder();
            buf.append("cmd=_notify-synch&tx=");
            buf.append(txid);
            buf.append("&at=");
            buf.append(identityToken);
            byte[] content = buf.toString().getBytes("ASCII");
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setFixedLengthStreamingMode(content.length);
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(readTimeout);
            OutputStream out = con.getOutputStream();
            try {
                out.write(content);
            } finally {
                out.close();
            }
            int code = con.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                String msg = con.getResponseMessage();
                throw new IOException("HTTP Error " + code + ": " + msg);
            }
            if (LOG.isLoggable(Level.FINE)) {
                dumpHeaderFields(con.getHeaderFields());
            }
            String enc = "UTF-8";
            String type = con.getHeaderField("Content-Type");
            if (type != null) {
                Matcher matcher = CONTENT_TYPE_PATTERN.matcher(type);
                if (matcher.matches()) {
                    enc = matcher.group(1);
                }
            }
            InputStream stream = con.getInputStream();
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(stream, enc));
                String s = in.readLine();
                if (!"SUCCESS".equals(s)) {
                    throw new IOException("Transaction failed " + txid);
                }
                Map<String, String> attrs = new HashMap<String, String>();
                while (true) {
                    s = in.readLine();
                    if (s == null) {
                        break;
                    }
                    int pos = s.indexOf('=');
                    String name = s.substring(0, pos);
                    String val = URLDecoder.decode(s.substring(pos + 1), enc);
                    LOG.log(Level.FINE, "    {0}={1}", new Object[] { name, val });
                    attrs.put(name, val);
                }
                return attrs;
            } finally {
                stream.close();
            }
        } finally {
            con.disconnect();
        }
    }

    public void processNotification(Map<String, String[]> attrs) throws IOException {
        StringBuilder buf = new StringBuilder("cmd=_notify-validate");
        for (Map.Entry<String, String[]> entry : attrs.entrySet()) {
            String name = entry.getKey();
            for (String value : entry.getValue()) {
                buf.append('&');
                buf.append(name);
                buf.append('=');
                buf.append(URLEncoder.encode(value, "UTF-8"));
            }
        }
        byte[] content = buf.toString().getBytes("UTF-8");
        URL uploadUrl = new URL(baseURL + PDT_PATH);
        HttpURLConnection con = (HttpURLConnection) uploadUrl.openConnection();
        try {
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setFixedLengthStreamingMode(content.length);
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(readTimeout);
            OutputStream out = con.getOutputStream();
            try {
                out.write(content);
            } finally {
                out.close();
            }
            int code = con.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                String msg = con.getResponseMessage();
                throw new IOException("HTTP Error " + code + ": " + msg);
            }
            String res;
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            try {
                res = in.readLine();
            } finally {
                in.close();
            }
            if (!res.equals("VERIFIED")) {
                throw new IOException("Payment verification failed");
            }
        } finally {
            con.disconnect();
        }
    }

    void renderButton(PaymentButton button, Writer writer) {
        try {
            String encrypted = encrypt(button);
            PrintWriter out = new PrintWriter(writer);
            try {
                out.println("<form action='" + baseURL + "/cgi-bin/webscr' method='post'>");
                out.println("<input type='hidden' name='cmd'" + " value='_s-xclick'>");
                out.println("<input type='submit' name='submit'" + " value='" + button.getLabel() + "'>");
                out.println("<input type='hidden' name='encrypted'" + " value='" + encrypted + "'>");
                out.println("</form>");
            } finally {
                out.close();
            }
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, "Error rendering button", e);
            throw e;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error rendering button", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private String encrypt(byte[] data) throws IOException, GeneralSecurityException, OperatorCreationException {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA1withRSA");
            builder.setProvider("BC");
            ContentSigner sha1Signer = builder.build(ownKey);
            CMSSignedDataGenerator sg = new CMSSignedDataGenerator();
            JcaDigestCalculatorProviderBuilder dcpbuilder = new JcaDigestCalculatorProviderBuilder();
            dcpbuilder.setProvider("BC");
            JcaSignerInfoGeneratorBuilder sigbuilder = new JcaSignerInfoGeneratorBuilder(dcpbuilder.build());
            sg.addSignerInfoGenerator(sigbuilder.build(sha1Signer, ownCert));
            ArrayList<X509CertificateHolder> certs = new ArrayList<X509CertificateHolder>();
            certs.add(new JcaX509CertificateHolder(ownCert));
            Store certStore = new CollectionStore(certs);
            sg.addCertificates(certStore);
            CMSProcessableByteArray cmsba = new CMSProcessableByteArray(data);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            cmsba.write(baos);
            CMSSignedData signedData = sg.generate(cmsba, true);
            byte[] signed = signedData.getEncoded();
            CMSEnvelopedDataGenerator eg = new CMSEnvelopedDataGenerator();
            JceKeyTransRecipientInfoGenerator trig = new JceKeyTransRecipientInfoGenerator(paypalCert);
            trig.setProvider("BC");
            eg.addRecipientInfoGenerator(trig);
            CMSEnvelopedData envData = eg.generate(new CMSProcessableByteArray(signed), new JceCMSContentEncryptorBuilder(CMSAlgorithm.DES_EDE3_CBC).setProvider("BC").build());
            byte[] bytes = envData.getEncoded();
            String encoded = new String(Base64.encode(bytes), "ASCII");
            return "-----BEGIN PKCS7-----" + encoded + "-----END PKCS7-----";
        } catch (CMSException e) {
            LOG.log(Level.SEVERE, "Error encrypting button data", e);
            throw new GeneralSecurityException(e.getMessage());
        }
    }

    private static Properties loadProps(URL url) throws IOException {
        Properties props = new Properties();
        InputStream in = url.openStream();
        try {
            props.load(in);
        } finally {
            in.close();
        }
        return props;
    }

    private URL getResource(String fileName) throws IOException {
        return new URL(confURL, fileName);
    }

    private static String getPackagePath() {
        Class thisClass = PaymentService.class;
        return thisClass.getPackage().getName().replace('.', '/');
    }

    private String buttonParams(PaymentButton button) {
        StringBuilder buf = new StringBuilder();
        appendParam("charset", "UTF-8", buf);
        appendParam("business", business, buf);
        appendParam("cert_id", certId, buf);
        appendParam("cmd", "_xclick", buf);
        appendParam("no_note", "1", buf);
        appendParam("custom", button.getCustomerId(), buf);
        appendParam("item_number", button.getItemCode(), buf);
        appendParam("item_name", button.getItemLabel(), buf);
        appendParam("amount", Util.formatDecimal(button.getPrice()), buf);
        appendParam("currency_code", button.getCurrency(), buf);
        appendParam("lc", paypalLanguage(button.getLanguage()), buf);
        appendParam("notify_url", button.getNotifyUrl(), buf);
        appendParam("return", button.getReturnUrl(), buf);
        appendParam("cancel_return", button.getCancelUrl(), buf);
        appendParam("address_override", "1", buf);
        appendParam("email", button.getEmail(), buf);
        appendParam("first_name", button.getFirstName(), buf);
        appendParam("last_name", button.getLastName(), buf);
        appendParam("address1", button.getAddress1(), buf);
        appendParam("address2", button.getAddress2(), buf);
        appendParam("zip", button.getZip(), buf);
        appendParam("city", button.getCity(), buf);
        if ("US".equals(button.getCountry())) {
            appendParam("state", button.getState(), buf);
        }
        appendParam("country", button.getCountry(), buf);
        return buf.toString();
    }

    private static void appendParam(String name, String value, StringBuilder buf) {
        buf.append(name);
        buf.append('=');
        buf.append(value);
        buf.append('\n');
    }

    private static String paypalLanguage(String language) {
        String result = language == null ? null : PAYPAL_LANGUAGES.get(language);
        return result == null ? "US" : result;
    }

    private void dumpHeaderFields(Map<String, List<String>> fields) {
        LOG.fine("Header fields:");
        String names[] = fields.keySet().toArray(new String[fields.size()]);
        for (String name : names) {
            LOG.log(Level.FINE, "    {0}: {1}", new Object[] { name, fields.get(name) });
        }
    }

    public static void initialize() {
        LOG.info("Registering bouncy castle");
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void terminate() {
        LOG.info("Unregistering bouncy castle");
        Security.removeProvider("BC");
    }

    static {
        PAYPAL_LANGUAGES = new HashMap<String, String>();
        PAYPAL_LANGUAGES.put("en", "US");
        PAYPAL_LANGUAGES.put("fr", "FR");
        PAYPAL_LANGUAGES.put("it", "IT");
        PAYPAL_LANGUAGES.put("de", "DE");
    }
}
