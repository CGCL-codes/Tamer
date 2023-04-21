package net.adrianromero.tpv.payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import net.adrianromero.tpv.forms.AppLocal;
import net.adrianromero.tpv.forms.AppConfig;
import net.adrianromero.tpv.forms.AppProperties;

/**
 * PaymentGatewayMemberCard implements a payment gateway designed for
 * customized member cards.
 * 
 * @author Hans
 */
public class PaymentGatewayMemberCard implements PaymentGateway {

    private static final String ENC = "UTF-8";

    private static String m_gatewayAddress;

    private static boolean m_usePost;

    private static String m_sCommerceID;

    private static String m_sCommercePassword;

    private static boolean m_bTestMode;

    private String m_hostname;

    private String m_sMagCardReader;

    private String m_transType;

    /** Creates a new instance of PaymentGatewayMemberCard
     * @param app 
     */
    public PaymentGatewayMemberCard(AppProperties app) {
        m_sMagCardReader = app.getProperty("payment.membercardreader");
        m_gatewayAddress = app.getProperty("gatewayURL");
        m_usePost = AppConfig.getInstance().usePostFlag();
        m_sCommerceID = app.getProperty("payment.memberid");
        m_sCommercePassword = app.getProperty("payment.memberpassword");
        m_bTestMode = Boolean.valueOf(app.getProperty("payment.testmode")).booleanValue();
        m_hostname = app.getHost();
    }

    /**
     * Returns the payment panel to be used to communicate with the gateway.
     * Depending of the notifier information different request types can be used.
     *  
     * @param notifier notifier.isTopUp indicates a TOPUP rewquest. If false,
     * a normal payment request, ENQUIRY, PURCHASE or REFUND will be performed.
     * 
     * @return the 
     */
    @Override
    public PaymentPanel getInfoMagcardFactory(JPaymentNotifier notifier) {
        return new PaymentPanelMemberCard(m_sMagCardReader, notifier);
    }

    /**
     * Transmit is the operation executing a gateway request based on
     * PaymentInfoMembercard.
     * We use a method different from execute to avoid extending the 
     * PaymentInfoMagcard interface.
     * This could eventually be solved in a somewhat cleaner way.
     * 
     * <p>To cusomize gateway operation look at the transmitted string and the
     * returned params.
     * 
     * <p>POST or GET requests can be configured in config.      * 
     * 
     * @param payinfo carries the information for the payment request.
     */
    @Override
    public void transmit(PaymentInfoMembercard payinfo) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("commerce_login=");
            sb.append(URLEncoder.encode(m_sCommerceID, ENC));
            sb.append("&commerce_password=");
            sb.append(URLEncoder.encode(m_sCommercePassword, ENC));
            sb.append("&test_request=");
            sb.append(m_bTestMode);
            sb.append("&hostname=");
            sb.append(URLEncoder.encode(m_hostname, ENC));
            sb.append("&card_string=");
            sb.append(URLEncoder.encode(payinfo.getCardNumber(), ENC));
            if (payinfo.is_topup()) {
                m_transType = "TOPUP";
            } else if (payinfo.printReport()) {
                m_transType = "REPORT01";
            } else if (payinfo.cancelTransaction()) {
                m_transType = "CANCEL";
            } else if (payinfo.getHolderName() == null) {
                m_transType = "ENQUIRY";
            } else {
                if (payinfo.getTotal() > 0.0) {
                    m_transType = "PURCHASE";
                } else {
                    m_transType = "REFUND";
                }
            }
            sb.append("&trans_type=");
            sb.append(m_transType);
            sb.append("&amount=");
            NumberFormat formatter = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));
            String amount = formatter.format(payinfo.getTotal());
            sb.append(URLEncoder.encode(amount, ENC));
            sb.append("&transaction_id=");
            sb.append(URLEncoder.encode(payinfo.getTransactionID(), ENC));
            URLConnection connection;
            if (m_usePost) {
                connection = openPostConnection(sb);
            } else {
                connection = openGetConnection(sb);
            }
            String http_response = connection.getHeaderField("Status");
            if ("201 Created".equals(http_response)) {
                String params[] = readResponse(connection);
                payinfo.paymentOK("OK");
                payinfo.setClosingBalance(params[0]);
            } else if ("202 Accepted".equals(http_response)) {
                String params[] = readResponse(connection);
                payinfo.setCardNumber(params[1]);
                payinfo.setOpeningBalance(params[2]);
                payinfo.enquiryOK(params[0]);
                payinfo.paymentOK("OK");
            } else if ("205 Reset Content".equals(http_response)) {
                String params[] = readResponse(connection);
                payinfo.setCardNumber(params[1]);
                payinfo.enquiryOK(params[0]);
                payinfo.setJsonString(params[2]);
            } else if ("206 Partial Content".equals(http_response)) {
                String params[] = readResponse(connection);
                payinfo.setCardNumber(params[1]);
                payinfo.setOpeningBalance(params[2]);
                payinfo.enquiryOK(params[0]);
                payinfo.paymentError(AppLocal.getInstance().getIntString("message.paymentrequired"));
            } else if ("200 OK".equals(http_response)) {
                String params[] = readResponse(connection);
                payinfo.setClosingBalance(params[2]);
                payinfo.setOpeningBalance(params[1]);
                payinfo.setCardNumber(params[3]);
                payinfo.enquiryOK(params[0]);
                payinfo.paymentOK("OK");
            } else {
                if ("500 Internal Server Error".equals(http_response)) {
                    payinfo.paymentError(AppLocal.getInstance().getIntString("message.internalservererror"));
                } else if ("402 Payment Required".equals(http_response)) {
                    payinfo.paymentError(AppLocal.getInstance().getIntString("message.paymentrequired"));
                } else if ("404 Not Found".equals(http_response)) {
                    payinfo.paymentError(AppLocal.getInstance().getIntString("message.notfound"));
                } else if ("412 Precondition Failed".equals(http_response)) {
                    payinfo.paymentError(AppLocal.getInstance().getIntString("message.preconditionfailed"));
                } else if ("401 Unauthorized".equals(http_response)) {
                    payinfo.paymentError(AppLocal.getInstance().getIntString("message.unauthorized"));
                } else if ("400 Bad Request".equals(http_response)) {
                    payinfo.paymentError(AppLocal.getInstance().getIntString("message.badrequest"));
                } else if ((http_response) == null) {
                    payinfo.paymentError(AppLocal.getInstance().getIntString("message.gatewaynoresponse"));
                } else {
                    payinfo.paymentError(AppLocal.getInstance().getIntString("message.paymenterror") + "\n" + http_response);
                }
                String params[] = readResponse(connection);
                if (params[0] != null) {
                    payinfo.paymentError(AppLocal.getInstance().getIntString(params[0]));
                }
            }
        } catch (UnsupportedEncodingException eUE) {
            payinfo.paymentError(AppLocal.getInstance().getIntString("message.paymentexceptionservice") + "\n" + eUE.getMessage());
        } catch (MalformedURLException eMURL) {
            payinfo.paymentError(AppLocal.getInstance().getIntString("message.badgatewayaddress") + "\n");
        } catch (IOException e) {
        }
    }

    private URLConnection openPostConnection(StringBuffer sb) throws IOException, IOException, MalformedURLException {
        URL url = new URL(m_gatewayAddress);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(sb.toString());
        wr.flush();
        wr.close();
        return connection;
    }

    private URLConnection openGetConnection(StringBuffer sb) throws IOException, IOException, MalformedURLException {
        URL url = new URL(m_gatewayAddress + "?" + sb.toString());
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection;
    }

    private void readHeaderFields(URLConnection conn) {
        try {
            for (int i = 0; ; i++) {
                String headerName = conn.getHeaderFieldKey(i);
                String headerValue = conn.getHeaderField(i);
                if (headerName == null && headerValue == null) {
                    break;
                }
                if (headerName == null) {
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void execute(PaymentInfoMagcard payinfo) {
        throw new UnsupportedOperationException("Execute should not be used in this context; use transmit instead.");
    }

    private String[] readResponse(URLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        line = in.readLine();
        in.close();
        return line.split("\\|");
    }
}
