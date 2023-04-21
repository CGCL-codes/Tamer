package com.miracleas.bitcoin_spinner_lib;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import android.os.Handler;
import com.bccapi.core.StreamReader;
import com.miracleas.bitcoin_spinner_lib.Ticker.BtcToUsdCallbackHandler;

/**
 * This class lets you obtain USD -> BTC exchange rates from MtGox.
 */
public class MultiTicker {

    private static final int MTGOX_RETRIES = 3;

    private static final long MTGOX_CACHE_INTERVAL = Consts.MINUTE_IN_NANOSECONDS * 5;

    private static final HostnameVerifier HOST_NAME_VERIFIER;

    private static final SSLSocketFactory SSL_SOCKET_FACTORY;

    private static Double _cachedValue;

    private static long _cacheTime;

    private static String _cachedCurrency;

    private static boolean _blockerOn;

    static {
        HOST_NAME_VERIFIER = new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return hostname.equals("mtgox.com");
            }
        };
        TrustManager[] trustOneCert = new TrustManager[] { new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
                throw new CertificateException();
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
                return;
            }
        } };
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustOneCert, null);
            SSL_SOCKET_FACTORY = sc.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    public interface TickerCallbackHandler {

        public void handleTickerCallback(String currency, Double value);
    }

    private static class TickerRequester implements Runnable {

        String _currency;

        Handler _handler;

        TickerCallbackHandler _callBackhandler;

        public TickerRequester(String currency, Handler handler, TickerCallbackHandler callbackHandler) {
            _currency = currency;
            _handler = handler;
            _callBackhandler = callbackHandler;
        }

        @Override
        public void run() {
            if (_blockerOn) {
                return;
            }
            try {
                _blockerOn = true;
                for (int i = 0; i < MTGOX_RETRIES; i++) {
                    Double mtgox = getLastMtGoxTrade(_currency);
                    if (mtgox != null) {
                        doCallback(mtgox);
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        doCallback(null);
                        return;
                    }
                }
                doCallback(null);
            } finally {
                _blockerOn = false;
            }
        }

        private static final String LAST = "\"last\"";

        private static final String VALUE = "\"value\"";

        private static Double getLastMtGoxTrade(String currency) {
            try {
                URL url = new URL("https://mtgox.com/api/1/BTC" + currency + "/public/ticker");
                HttpsURLConnection connection;
                connection = (HttpsURLConnection) url.openConnection();
                connection.setHostnameVerifier(HOST_NAME_VERIFIER);
                connection.setSSLSocketFactory(SSL_SOCKET_FACTORY);
                connection.setReadTimeout(60000);
                connection.setRequestMethod("GET");
                connection.connect();
                int status = connection.getResponseCode();
                if (status != 200) {
                    return null;
                }
                String ticker = StreamReader.readFully(connection.getInputStream());
                int index = ticker.indexOf(LAST);
                if (index == -1) {
                    return null;
                }
                index += LAST.length();
                index = ticker.indexOf(VALUE, index);
                if (index == -1) {
                    return null;
                }
                index += VALUE.length();
                index = ticker.indexOf('"', index);
                if (index == -1) {
                    return null;
                }
                index += 1;
                int startIndex = index;
                index = ticker.indexOf('"', index);
                if (index == -1) {
                    return null;
                }
                int endIndex = index - 1;
                if (endIndex <= startIndex) {
                    return null;
                }
                String value = ticker.substring(startIndex, endIndex);
                return Double.parseDouble(value);
            } catch (Exception e) {
                return null;
            }
        }

        private synchronized void doCallback(final Double value) {
            _cachedValue = value;
            _handler.post(new Runnable() {

                @Override
                public void run() {
                    _callBackhandler.handleTickerCallback(_currency, value);
                }
            });
        }
    }

    /**
	 * Request a number of Bitcoins measured in satoshis to be converted into USD at the current MtGox buy rate.
	 * 
	 * @param satoshis
	 *            The number of Bitcoins in satoshis
	 * @param callback
	 *            The {@link BtcToUsdCallbackHandler} to make a call back to once the conversion is done.
	 */
    public static void requestTicker(String currency, TickerCallbackHandler callback) {
        if (!currency.equals(_cachedCurrency) || _cachedValue == null || _cacheTime + MTGOX_CACHE_INTERVAL < System.nanoTime()) {
            _cachedCurrency = currency;
            _cacheTime = System.nanoTime();
            final Handler handler = new Handler();
            Thread t = new Thread(new TickerRequester(currency, handler, callback));
            t.start();
        } else {
            new TickerRequester(currency, new Handler(), callback).doCallback(_cachedValue);
        }
    }
}
