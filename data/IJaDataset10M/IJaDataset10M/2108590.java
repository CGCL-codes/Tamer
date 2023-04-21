package net.sf.dsig.verify;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import junit.framework.TestCase;
import org.apache.xml.security.utils.Base64;

public class OCSPHelperTest extends TestCase {

    private final String TEST_CERTIFICATE = "MIIFDjCCBHegAwIBAgIQMhkZ35XvUvCIFaOllXrVrDANBgkqhkiG9w0BAQUFADCBnDELMAkGA1UE" + "BhMCR1IxIzAhBgNVBAoTGkVGRyBFdXJvYmFuayBFcmdhc2lhcyBBLkUuMR8wHQYDVQQLExZWZXJp" + "U2lnbiBUcnVzdCBOZXR3b3JrMR8wHQYDVQQLExZGT1IgVEVTVCBQVVJQT1NFUyBPTkxZMSYwJAYD" + "VQQDEx1FRkcgRXVyb2JhbmsgRXJnYXNpYXMgVEVTVCBDQTAeFw0wOTA0MTEwMDAwMDBaFw0xMDA0" + "MTEyMzU5NTlaMIHwMRUwEwYDVQQLFAxBbGlhcyAtIGRzaWcxCzAJBgNVBAYTAkdSMR8wHQYDVQQL" + "FBZGT1IgVEVTVCBQVVJQT1NFUyBPTkxZMTEwLwYDVQQLFChUZXJtcyBvZiB1c2UgYXQgc2VjLmFk" + "YWNvbS5jb20vcnBhIChjKTAzMSMwIQYDVQQKFBpFRkcgRXVyb2JhbmsgRXJnYXNpYXMgQS5FLjEP" + "MA0GA1UEDBQGU2FtcGxlMRIwEAYDVQQEEwlTaWduYXR1cmUxEDAOBgNVBCoTB0RpZ2l0YWwxGjAY" + "BgNVBAMTEURpZ2l0YWwgU2lnbmF0dXJlMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA" + "ppx3we7zNqNzUNqmi9IiREM+/gEnps7ExiGMijeibVbiQcx93oT7vxuvFlGeWnu9KIdpO0Dbalak" + "Nx+Lg8HaqEmtwUvHuHJ56VWdh5+IvQKp7Z8b6608rlyaZ9s3/PnYp3bG+uFngQ1sAQp9I0a7m0J9" + "Kv4SR75svPKxNspGQeDdj0oXLEFEmX7k82bDiTd6hrPp2bwpn9qnSxWXih5dFmy6DBut3eiOIlSE" + "LrKQhPI2zMwHrX6R0lpwlWuUhZDmzNpmF5p8u2YetCR5zvP0rhuZ6QIOAM87z/uigacT+6T8pxfK" + "BbdAT3fJWi/MhLuxWQqN9TU5NpTNtrjWr4mthwIDAQABo4IBdTCCAXEwCQYDVR0TBAIwADALBgNV" + "HQ8EBAMCBeAwWwYDVR0gBFQwUjA3BgtghkgBhvhFAQcXAjAoMCYGCCsGAQUFBwIBFhpodHRwczov" + "L3NlYy5hZGFjb20uY29tL3JwYTANBgtghkgBhvhFAQcsAjAIBgYEAIswAQEwYgYDVR0fBFswWTBX" + "oFWgU4ZRaHR0cDovL2NybC10ZXN0LmFkYWNvbS5jb20vRUZHRXVyb2JhbmtFcmdhc2lhc0FFRk9S" + "VEVTVFBVUlBPU0VTT05MWS9MYXRlc3RDUkwuY3JsMBEGCWCGSAGG+EIBAQQEAwIHgDARBgpghkgB" + "hvhFAQYJBAMBAf8wHQYDVR0lBBYwFAYIKwYBBQUHAwIGCCsGAQUFBwMEMBgGCCsGAQUFBwEDBAww" + "CjAIBgYEAI5GAQEwNwYIKwYBBQUHAQEEKzApMCcGCCsGAQUFBzABhhtodHRwOi8vb2NzcC10ZXN0" + "LmFkYWNvbS5jb20wDQYJKoZIhvcNAQEFBQADgYEAkflhsm/1ljYzAIhJn1uYFOER1ZHIchON/sjh" + "V/UeATrycM2GJSj+/o6LlcQlmHSpwOPVCF5onTKDpNM++lvURQLbSgOTHw6kB9uVeW4oBI8ALk9H" + "m23BK53XkvhwtnQ/L1pHalcWoGp2dFk+PzKZzmmniWQ00gemA4xNO5HDNWA=";

    private final String TEST_REVOKED_CERTIFICATE = "MIIFEjCCBHugAwIBAgIQfVnP2Pzvw5r5JCbs6otfDzANBgkqhkiG9w0BAQUFADCBnDELMAkGA1UE" + "BhMCR1IxIzAhBgNVBAoTGkVGRyBFdXJvYmFuayBFcmdhc2lhcyBBLkUuMR8wHQYDVQQLExZWZXJp" + "U2lnbiBUcnVzdCBOZXR3b3JrMR8wHQYDVQQLExZGT1IgVEVTVCBQVVJQT1NFUyBPTkxZMSYwJAYD" + "VQQDEx1FRkcgRXVyb2JhbmsgRXJnYXNpYXMgVEVTVCBDQTAeFw0wOTA0MTMwMDAwMDBaFw0xMDA0" + "MTMyMzU5NTlaMIH0MRUwEwYDVQQLFAxBbGlhcyAtIGRzaWcxCzAJBgNVBAYTAkdSMR8wHQYDVQQL" + "FBZGT1IgVEVTVCBQVVJQT1NFUyBPTkxZMTEwLwYDVQQLFChUZXJtcyBvZiB1c2UgYXQgc2VjLmFk" + "YWNvbS5jb20vcnBhIChjKTAzMSMwIQYDVQQKFBpFRkcgRXVyb2JhbmsgRXJnYXNpYXMgQS5FLjEP" + "MA0GA1UEDBQGU2FtcGxlMRQwEgYDVQQEEwtDZXJ0aWZpY2F0ZTEQMA4GA1UEKhMHUmV2b2tlZDEc" + "MBoGA1UEAxMTUmV2b2tlZCBDZXJ0aWZpY2F0ZTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoC" + "ggEBAM62BomPPjIaRU9AJhP00fCk+D9cZm81cnNMAkgiU+8rHMA883goM4BJXkLCWIGM/n/GNyAi" + "OlZMcOtbzAcvXHlVg/rveaws7T/gSQGcL26Zc3NUMFo4+RDwe/htcjl++BFYy1U6jYBvCUMVN7is" + "z0nqhKPWmvJUjbHJ+iopcEYTv27SwFiIEF+O/kjabDakKc3rPB++LmWVRjnO3RVbkf3nx7nLy6td" + "u+b28z6qDcArXEDv2PwG9FTkpJQ4DHVu9GcxAj+z9RLqMONzzYFHq1xvCOrsM/aVPn4l1V3METh4" + "bfVA1euFid90hA7b8YJzTPchB7lNR/NQccfoo2EXcRkCAwEAAaOCAXUwggFxMAkGA1UdEwQCMAAw" + "CwYDVR0PBAQDAgXgMFsGA1UdIARUMFIwNwYLYIZIAYb4RQEHFwIwKDAmBggrBgEFBQcCARYaaHR0" + "cHM6Ly9zZWMuYWRhY29tLmNvbS9ycGEwDQYLYIZIAYb4RQEHLAIwCAYGBACLMAEBMGIGA1UdHwRb" + "MFkwV6BVoFOGUWh0dHA6Ly9jcmwtdGVzdC5hZGFjb20uY29tL0VGR0V1cm9iYW5rRXJnYXNpYXNB" + "RUZPUlRFU1RQVVJQT1NFU09OTFkvTGF0ZXN0Q1JMLmNybDARBglghkgBhvhCAQEEBAMCB4AwEQYK" + "YIZIAYb4RQEGCQQDAQH/MB0GA1UdJQQWMBQGCCsGAQUFBwMCBggrBgEFBQcDBDAYBggrBgEFBQcB" + "AwQMMAowCAYGBACORgEBMDcGCCsGAQUFBwEBBCswKTAnBggrBgEFBQcwAYYbaHR0cDovL29jc3At" + "dGVzdC5hZGFjb20uY29tMA0GCSqGSIb3DQEBBQUAA4GBAGEJGS3jzKZhtoM495W4X7m7l2168qe6" + "1wwqXh8R6ff7eqtbkIWLkZSxY8X2wwxi9xXyjVqKUNVsS1vvq3QyQfW0GQffNcH0j+IYoxQ/0EKU" + "UTWbWLXB3qwG7GRz5fPx4SH0Pqn/fDJLk/qMeHncrw9Q0XrXa+gp7ScWsQjgsv0H";

    public static OCSPHelper getOcspHelper() throws Exception {
        X509Certificate caCertificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(OCSPHelperTest.class.getResourceAsStream("/ca.cer"));
        assertNotNull(caCertificate);
        OCSPHelper helper = new OCSPHelper();
        helper.setProxyHost(System.getProperty("http.proxyHost"));
        helper.setProxyPort(Integer.getInteger("http.proxyPort") != null ? Integer.getInteger("http.proxyPort").intValue() : -1);
        helper.setCaCertificate(caCertificate);
        return helper;
    }

    public void testHelper() throws Exception {
        X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(Base64.decode(TEST_CERTIFICATE)));
        assertNotNull(certificate);
        X509Certificate revokedCertificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(Base64.decode(TEST_REVOKED_CERTIFICATE)));
        assertNotNull(revokedCertificate);
        OCSPHelper helper = getOcspHelper();
        String authorityInfoAccessUriAsString = OCSPHelper.getOCSPAccessLocationUri(certificate);
        assertNotNull(authorityInfoAccessUriAsString);
        assertEquals("http://ocsp-test.adacom.com", authorityInfoAccessUriAsString);
        assertTrue(helper.isValid(certificate));
        assertFalse(helper.isValid(revokedCertificate));
    }

    private String certYpesAsString = "MIIEFzCCAv+gAwIBAgIQa36XyrLNzPvLRco3/1B+VTANBgkqhkiG9w0BAQUFADCC" + "ASUxCzAJBgNVBAYTAkdSMTgwNgYDVQQKEy9IZWxsZW5pYyBQdWJsaWMgQWRtaW5p" + "c3RyYXRpb24gUm9vdCBDQSAtIEhQQVJDQTEkMCIGA1UECxMbWVBFUyAtIE1pbmlz" + "dHJ5IG9mIEludGVyaW9yMUYwRAYDVQQLEz1HZW5lcmFsIFNlY3JldGFyaWF0IG9m" + "IFB1YmxpYyBBZG1pbmlzdHJhdGlvbiBhbmQgZS1Hb3Zlcm5tZW50MUUwQwYDVQQL" + "EzxHZW5pa2kgR3JhbW1hdGVpYSBEaW1vc2lhcyBEaW9pa2lzaXMga2FpIElsZWsu" + "IERpYWt5dmVybmlzaXMxJzAlBgNVBAMTHllQRVMgLSBNaW5pc3RyeSBvZiBJbnRl" + "cmlvciBDQTAeFw0wOTA1MTgwMDAwMDBaFw0xMDA1MTgyMzU5NTlaMGIxDTALBgNV" + "BAoMBFlQRVMxEjAQBgNVBAsMCUVybWlzU2lnbjEUMBIGA1UEAwwLZXJtaXMgc2ln" + "bjExJzAlBgkqhkiG9w0BCQEWGHBraS1zdXBwb3J0QGVybWlzLmdvdi5ncjCBnzAN" + "BgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAwDWb0BMdxOPAIxO+yaV5rnjVjDV5ecQ/" + "9u2kRrZ+YD1fj1Eyj0hlS82BIkuJZH1R8GHOW5ejy72zcmngcWS1df3IA/B35JYO" + "a2WMm5BFVOlc/U1JWKZxzz6lAsNoWZX0/26HTWhgPXi5vYSEjMW8e8XJQGhubK0f" + "UEmX57uVTQMCAwEAAaOBhzCBhDAJBgNVHRMEAjAAMAsGA1UdDwQEAwIFoDARBglg" + "hkgBhvhCAQEEBAMCB4AwRAYDVR0fBD0wOzA5oDegNYYzaHR0cDovL2NybC5lcm1p" + "cy5nb3YuZ3IvWVBFU0VybWlzU2lnbi9MYXRlc3RDUkwuY3JsMBEGCmCGSAGG+EUB" + "BgkEAwEB/zANBgkqhkiG9w0BAQUFAAOCAQEAC6VNLpBL8/s+XqSfjYijTt6i9zJ4" + "UtCF2wPK9X+QU2kKJFqgVT2OBQoyS0piy3vjEYr0hViOZvWUeVyk5t3f03VGwCM2" + "Mh3Bf555giyyVNTTANbk9Gs13QsEGjsBF291GenSsq23kO0kPNbDvhkoAWuRgHPH" + "ZMoP7ag4I9LTMhbkxCsnI7VicH270nzGhysuhiZpNowJXNb6epJxHfZKnn/asKcg" + "+RXxCmD9q+qtSNPz63xqRHh+DLZxLx+88TMxmKUcGFm/9zFBagEsFFq4t4yml/gj" + "Y6c3WyXiq+B0ZkDJh7gVqWkSHYjtHHabsoL1E18/+fJE43eALzeBdVIa7g==";

    public void testYpesHelper() throws Exception {
        X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(Base64.decode(certYpesAsString)));
        X509Certificate wrongCertificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(Base64.decode(TEST_CERTIFICATE)));
        System.out.println(certificate);
        X509Certificate caCertificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(getClass().getResourceAsStream("/YPES_Ministry_Interior_CA.cer"));
        OCSPHelper helper = getOcspHelper();
        helper.setCaCertificate(caCertificate);
        helper.setDefaultOcspAccessLocation("http://ocsp.ermis.gov.gr");
        try {
            helper.isValid(wrongCertificate);
        } catch (ConfigurationException ignored) {
        }
        helper.isValid(certificate);
    }
}
