package org.torweg.pulse.component.shop.payment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.mail.internet.InternetAddress;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.torweg.pulse.component.shop.payment.WirecardPaymentModuleConfiguration.BusinessCaseConfiguration;
import org.torweg.pulse.component.shop.payment.WirecardPaymentModuleConfiguration.ConnectionConfiguration;
import org.torweg.pulse.component.shop.payment.WirecardPaymentModuleConfiguration.ModuleConfiguration;
import org.torweg.pulse.configuration.EmailAddressConfiguration;
import org.torweg.pulse.configuration.EmailConfiguration;
import org.torweg.pulse.email.Email;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;
import org.torweg.pulse.service.PulseException;
import de.wirecard.eft.ADDRESS;
import de.wirecard.eft.Amount;
import de.wirecard.eft.CORPTRUSTCENTERDATA;
import de.wirecard.eft.DETAIL;
import de.wirecard.eft.EXTERNALACCOUNT;
import de.wirecard.eft.FunctionAuthorizationRequest;
import de.wirecard.eft.FunctionDebitRequest;
import de.wirecard.eft.ProcessingStatus;
import de.wirecard.eft.RequestJob;
import de.wirecard.eft.TransactionAuthorizationRequest;
import de.wirecard.eft.TransactionDebitRequest;
import de.wirecard.eft.WIRECARDBXML;
import de.wirecard.eft.WREQUEST;

/**
 * @author Christian Schatt
 * @version $Revision$
 */
public final class WirecardEFTPaymentModule implements IPaymentModule {

    /**
	 * The serialVersionUID.
	 */
    private static final long serialVersionUID = 918166698615487304L;

    /**
	 * The <code>Logger</code>.
	 */
    private static final Logger LOGGER = Logger.getLogger(WirecardEFTPaymentModule.class);

    /**
	 * The <code>ModuleConfiguration</code>.
	 */
    private ModuleConfiguration configuration;

    /**
	 * The <code>WirecardEFTPaymentRequest</code>.
	 */
    private WirecardEFTPaymentRequest paymentRequest;

    /**
	 * The <code>PaymentStatus</code>.
	 */
    private PaymentStatus paymentStatus;

    /**
	 * The GuWID ("Globally unique Wirecard ID") received from Wirecard.
	 */
    private String guWID = null;

    /**
	 * The no-argument constructor.
	 */
    private WirecardEFTPaymentModule() {
        super();
    }

    /**
	 * Returns a new instance of the <code>WirecardEFTPaymentModule</code> for
	 * the given <code>WirecardPaymentModuleConfiguration</code> and the given
	 * <code>WirecardEFTPaymentRequest</code>.
	 * 
	 * @param conf
	 *            the <code>WirecardPaymentModuleConfiguration</code>
	 * @param request
	 *            the <code>WirecardEFTPaymentRequest</code>
	 * @return the <code>WirecardEFTPaymentModule</code>
	 */
    public static WirecardEFTPaymentModule newInstance(final WirecardPaymentModuleConfiguration conf, final WirecardEFTPaymentRequest request) {
        WirecardEFTPaymentModule module = new WirecardEFTPaymentModule();
        module.setConfiguration(conf.getModuleConfigurations().get(request.getLocale()));
        module.setPaymentRequest(request);
        module.setPaymentStatus(PaymentStatus.INITIALIZED);
        return module;
    }

    /**
	 * Sets the <code>ModuleConfiguration</code>.
	 * 
	 * @param conf
	 *            the <code>ModuleConfiguration</code> to set
	 * @return the <code>WirecardEFTPaymentModule</code> itself
	 */
    private WirecardEFTPaymentModule setConfiguration(final ModuleConfiguration conf) {
        if (conf == null) {
            throw new IllegalArgumentException(getClass() + ".configuration must not be null.");
        }
        this.configuration = conf;
        return this;
    }

    /**
	 * Sets the <code>WirecardEFTPaymentRequest</code>. Throws an
	 * <code>IllegalArgumentException</code> if the given
	 * <code>WirecardEFTPaymentRequest</code> 'request' is <code>null</code>.
	 * 
	 * @param request
	 *            the <code>WirecardEFTPaymentRequest</code> to set
	 * @return the <code>WirecardCCPaymentModule</code> itself
	 */
    private WirecardEFTPaymentModule setPaymentRequest(final WirecardEFTPaymentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException(getClass() + ".paymentRequest must not be null.");
        }
        this.paymentRequest = request;
        return this;
    }

    /**
	 * Returns the <code>PaymentStatus</code>.
	 * 
	 * @return the <code>PaymentStatus</code>
	 */
    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    /**
	 * Sets the <code>PaymentStatus</code>. Throws an
	 * <code>IllegalArgumentException</code> if the given
	 * <code>PaymentStatus</code> 'status' is <code>null</code>.
	 * 
	 * @param status
	 *            the <code>PaymentStatus</code> to set
	 * @return the <code>WirecardEFTPaymentModule</code> itself
	 */
    private WirecardEFTPaymentModule setPaymentStatus(final PaymentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException(getClass() + ".paymentStatus must not be null.");
        }
        this.paymentStatus = status;
        return this;
    }

    /**
	 * Sets the GuWID. Throws an <code>IllegalArgumentException</code> if the
	 * given <code>String</code> 'id' is whitespace or empty.
	 * 
	 * @param id
	 *            the GuWID to set
	 * @return the <code>WirecardEFTPaymentModule</code> itself
	 */
    private WirecardEFTPaymentModule setGuWID(final String id) {
        if ((id != null) && id.trim().equals("")) {
            throw new IllegalArgumentException(getClass() + ".guWID must not be whitespace or empty.");
        }
        this.guWID = id;
        return this;
    }

    /**
	 * Performs the authorization of the requested payment.
	 * 
	 * @return an <code>WirecardEFTPaymentResponse</code>
	 */
    public WirecardEFTPaymentResponse authorize() {
        try {
            if (!this.paymentStatus.equals(PaymentStatus.INITIALIZED)) {
                throw new IllegalStateException("Cannot authorize the payment, because the payment module's status is: '" + this.paymentStatus.toString() + "'.");
            }
            String transactionId = createTransactionId();
            String errorMessage = null;
            HttpsURLConnection con = null;
            OutputStream out = null;
            InputStream in = null;
            try {
                con = prepareConnection();
                con.connect();
                out = con.getOutputStream();
                JAXBContext context = JAXBContext.newInstance("de.wirecard.eft");
                Marshaller marshaller = context.createMarshaller();
                marshaller.marshal(getAuthorizationBusinessXML(transactionId), out);
                in = con.getInputStream();
                Unmarshaller unmarshaller = context.createUnmarshaller();
                WIRECARDBXML response = (WIRECARDBXML) unmarshaller.unmarshal(in);
                ProcessingStatus status = response.getWRESPONSE().getWJOB().getFNCFTAUTHORIZATION().get(0).getFTTRANSACTION().get(0).getPROCESSINGSTATUS();
                setGuWID(status.getGuWID());
                if (status.getFunctionResult().equals("NOK")) {
                    StringBuilder error = new StringBuilder();
                    error.append("FunctionResult: ").append(status.getFunctionResult()).append(", ");
                    error.append("TimeStamp: ").append(status.getTimeStamp()).append(", ");
                    error.append("StatusCode: ").append(status.getStatusCode()).append(", ");
                    error.append("ReasonCode: ").append(status.getReasonCode());
                    for (DETAIL detail : status.getDETAIL()) {
                        error.append(", Detail: (");
                        error.append("ReturnCode: " + detail.getReturnCode());
                        error.append("Message: " + detail.getMessage());
                        error.append("Advice: " + detail.getAdvice());
                        error.append(")");
                    }
                    setPaymentStatus(PaymentStatus.AUTHORIZATION_REJECTED);
                    errorMessage = error.toString();
                    LOGGER.warn("The authorization was not successful due to the following error: " + errorMessage);
                } else {
                    setPaymentStatus(PaymentStatus.AUTHORIZED);
                }
            } catch (IOException e) {
                setPaymentStatus(PaymentStatus.INTERRUPTED);
            } catch (JAXBException e) {
                throw new PulseException(e.getLocalizedMessage(), e);
            } finally {
                closeAndDisconnect(in, out, con);
            }
            WirecardEFTPaymentResponse response = new WirecardEFTPaymentResponse();
            response.setPaymentRequest(this.paymentRequest);
            response.setPaymentStatus(this.paymentStatus).setErrorMessage(errorMessage);
            response.setTransactionId(transactionId).setGuWID(this.guWID);
            return response;
        } catch (Exception e) {
            sendErrorReport(e);
            throw new PulseException(e.getLocalizedMessage(), e);
        }
    }

    /**
	 * Performs the capture of the requested payment.
	 * 
	 * @return a <code>WirecardEFTPaymentResponse</code>
	 */
    public WirecardEFTPaymentResponse capture() {
        try {
            if (!this.paymentStatus.equals(PaymentStatus.AUTHORIZED)) {
                throw new IllegalStateException("Cannot capture the payment, because the payment module's status is: '" + this.paymentStatus.toString() + "'.");
            }
            String transactionId = createTransactionId();
            String errorMessage = null;
            HttpsURLConnection con = null;
            OutputStream out = null;
            InputStream in = null;
            try {
                con = prepareConnection();
                con.connect();
                out = con.getOutputStream();
                JAXBContext context = JAXBContext.newInstance("de.wirecard.eft");
                Marshaller marshaller = context.createMarshaller();
                marshaller.marshal(getCaptureAuthorizationBusinessXML(transactionId), out);
                in = con.getInputStream();
                Unmarshaller unmarshaller = context.createUnmarshaller();
                WIRECARDBXML response = (WIRECARDBXML) unmarshaller.unmarshal(in);
                ProcessingStatus status = response.getWRESPONSE().getWJOB().getFNCFTDEBIT().get(0).getFTTRANSACTION().get(0).getPROCESSINGSTATUS();
                setGuWID(status.getGuWID());
                if (status.getFunctionResult().equals("NOK")) {
                    StringBuilder error = new StringBuilder();
                    error.append("FunctionResult: ").append(status.getFunctionResult()).append(", ");
                    error.append("TimeStamp: ").append(status.getTimeStamp()).append(", ");
                    error.append("StatusCode: ").append(status.getStatusCode()).append(", ");
                    error.append("ReasonCode: ").append(status.getReasonCode());
                    for (DETAIL detail : status.getDETAIL()) {
                        error.append(", Detail: (");
                        error.append("ReturnCode: " + detail.getReturnCode());
                        error.append("Message: " + detail.getMessage());
                        error.append("Advice: " + detail.getAdvice());
                        error.append(")");
                    }
                    setPaymentStatus(PaymentStatus.CAPTURE_REJECTED);
                    errorMessage = error.toString();
                    LOGGER.warn("The capture was not successful due to the following error: " + errorMessage);
                } else {
                    setPaymentStatus(PaymentStatus.CAPTURED);
                }
            } catch (IOException e) {
                setPaymentStatus(PaymentStatus.INTERRUPTED);
            } catch (JAXBException e) {
                throw new PulseException(e.getLocalizedMessage(), e);
            } finally {
                closeAndDisconnect(in, out, con);
            }
            WirecardEFTPaymentResponse response = new WirecardEFTPaymentResponse();
            response.setPaymentRequest(this.paymentRequest);
            response.setPaymentStatus(this.paymentStatus).setErrorMessage(errorMessage);
            response.setTransactionId(transactionId).setGuWID(this.guWID);
            return response;
        } catch (Exception e) {
            sendErrorReport(e);
            throw new PulseException(e.getLocalizedMessage(), e);
        }
    }

    /**
	 * Returns a generated transaction id.
	 * 
	 * @return the transaction id
	 */
    private String createTransactionId() {
        StringBuilder result = new StringBuilder(Long.toHexString(System.currentTimeMillis()));
        while (result.length() < 16) {
            result.insert(0, '0');
        }
        if (this.paymentRequest.getUserId() == null) {
            result.insert(0, Long.toHexString((long) (Math.random() * Long.MAX_VALUE)));
        } else {
            result.insert(0, Long.toHexString(this.paymentRequest.getUserId().longValue()));
        }
        while (result.length() < 32) {
            result.insert(0, '0');
        }
        return result.toString();
    }

    /**
	 * Prepares the <code>HttpsURLConnection</code>.
	 * 
	 * @return the <code>HttpsURLConnection</code>
	 * @throws IOException
	 *             if an <code>IOException</code> occurs.
	 */
    private HttpsURLConnection prepareConnection() throws IOException {
        ConnectionConfiguration conConf = this.configuration.getConnectionConfiguration();
        BusinessCaseConfiguration caseConf = conConf.getBusinessCaseConfiguration();
        HttpsURLConnection connection = (HttpsURLConnection) new URL(conConf.getUrlString()).openConnection();
        connection.setConnectTimeout(conConf.getTimeout());
        connection.setDoOutput(true);
        connection.setRequestMethod(conConf.getRequestMethod());
        connection.setRequestProperty("Authorization", "Basic " + new String(Base64.encodeBase64((caseConf.getUserName() + ":" + caseConf.getPassword()).getBytes())));
        connection.setRequestProperty("Content-Type", conConf.getContentType());
        connection.setRequestProperty("Content-Encoding", conConf.getContentEncoding());
        return connection;
    }

    /**
	 * Creates the <code>WIRECARDBXML</code> for the authorization of the
	 * request.
	 * 
	 * @param trans
	 *            the transaction id
	 * @return the <code>WIRECARDBXML</code>
	 */
    private WIRECARDBXML getAuthorizationBusinessXML(final String trans) {
        TransactionAuthorizationRequest transaction = new de.wirecard.eft.TransactionAuthorizationRequest();
        transaction.setMode(this.configuration.getTransactionMode().toString().toLowerCase());
        transaction.setTransactionID(trans);
        List<Object> transProps = transaction.getRest();
        CORPTRUSTCENTERDATA ctcData = new CORPTRUSTCENTERDATA();
        ADDRESS address = new ADDRESS();
        address.setFirstName(this.paymentRequest.getFirstName());
        address.setLastName(this.paymentRequest.getLastName());
        address.setAddress1(this.paymentRequest.getStreet());
        address.setCity(this.paymentRequest.getCity());
        address.setZipCode(this.paymentRequest.getPostalCode());
        address.setCountry(this.paymentRequest.getCountryCode());
        address.setEmail(this.paymentRequest.getEmailAddress());
        ctcData.setADDRESS(address);
        transProps.add(ctcData);
        EXTERNALACCOUNT extAcc = new de.wirecard.eft.EXTERNALACCOUNT();
        extAcc.setFirstName(this.paymentRequest.getAccountHolderFirstName());
        extAcc.setLastName(this.paymentRequest.getAccountHolderLastName());
        extAcc.setCompanyName(this.paymentRequest.getAccountHolderCompany());
        extAcc.setAccountNumber(this.paymentRequest.getAccountNumber());
        extAcc.setBankCode(this.paymentRequest.getBankCode());
        extAcc.setCountry(this.paymentRequest.getBankCountryCode());
        transProps.add(extAcc);
        transProps.add(new JAXBElement<String>(new QName("Currency"), String.class, this.paymentRequest.getCurrency().getCurrencyCode()));
        Amount amount = new Amount();
        amount.setValue(BigInteger.valueOf(this.paymentRequest.getAmount()));
        transProps.add(amount);
        FunctionAuthorizationRequest function = new FunctionAuthorizationRequest();
        function.setFunctionID("");
        function.getFTTRANSACTION().add(transaction);
        RequestJob job = new RequestJob();
        job.setJobID("");
        job.setBusinessCaseSignature(this.configuration.getConnectionConfiguration().getBusinessCaseConfiguration().getSignature());
        job.getFNCFTAUTHORIZATION().add(function);
        WREQUEST eftRequest = new WREQUEST();
        eftRequest.setWJOB(job);
        WIRECARDBXML result = new WIRECARDBXML();
        result.setWREQUEST(eftRequest);
        return result;
    }

    /**
	 * Creates the <code>WIRECARDBXML</code> for the capture of the previously
	 * authorized request.
	 * 
	 * @param trans
	 *            the transaction id
	 * @return the <code>WIRECARDBXML</code>
	 */
    private WIRECARDBXML getCaptureAuthorizationBusinessXML(final String trans) {
        TransactionDebitRequest transaction = new TransactionDebitRequest();
        transaction.setMode(this.configuration.getTransactionMode().toString().toLowerCase());
        transaction.setTransactionID(trans);
        List<Object> transProps = transaction.getRest();
        CORPTRUSTCENTERDATA ctcData = new CORPTRUSTCENTERDATA();
        ADDRESS address = new ADDRESS();
        address.setFirstName(this.paymentRequest.getFirstName());
        address.setLastName(this.paymentRequest.getLastName());
        address.setAddress1(this.paymentRequest.getStreet());
        address.setCity(this.paymentRequest.getCity());
        address.setZipCode(this.paymentRequest.getPostalCode());
        address.setCountry(this.paymentRequest.getCountryCode());
        address.setEmail(this.paymentRequest.getEmailAddress());
        ctcData.setADDRESS(address);
        transProps.add(ctcData);
        EXTERNALACCOUNT extAcc = new de.wirecard.eft.EXTERNALACCOUNT();
        extAcc.setFirstName(this.paymentRequest.getAccountHolderFirstName());
        extAcc.setLastName(this.paymentRequest.getAccountHolderLastName());
        extAcc.setCompanyName(this.paymentRequest.getAccountHolderCompany());
        extAcc.setAccountNumber(this.paymentRequest.getAccountNumber());
        extAcc.setBankCode(this.paymentRequest.getBankCode());
        extAcc.setCountry(this.paymentRequest.getBankCountryCode());
        transProps.add(extAcc);
        transProps.add(new JAXBElement<String>(new QName("Currency"), String.class, this.paymentRequest.getCurrency().getCurrencyCode()));
        Amount amount = new Amount();
        amount.setValue(BigInteger.valueOf(this.paymentRequest.getAmount()));
        transProps.add(amount);
        FunctionDebitRequest function = new FunctionDebitRequest();
        function.setFunctionID("");
        function.setFTTRANSACTION(transaction);
        RequestJob job = new RequestJob();
        job.setJobID("");
        job.setBusinessCaseSignature(this.configuration.getConnectionConfiguration().getBusinessCaseConfiguration().getSignature());
        job.getFNCFTDEBIT().add(function);
        WREQUEST eftRequest = new WREQUEST();
        eftRequest.setWJOB(job);
        WIRECARDBXML result = new WIRECARDBXML();
        result.setWREQUEST(eftRequest);
        return result;
    }

    /**
	 * Closes the given <code>InputStream</code> and <code>OutputStream</code>
	 * and disconnects the given <code>HttpsURLConnection</code>.
	 * 
	 * @param in
	 *            the <code>InputStream</code>
	 * @param out
	 *            the <code>OutputStream</code>
	 * @param con
	 *            the <code>HttpsURLConnection</code>
	 */
    private void closeAndDisconnect(final InputStream in, final OutputStream out, final HttpsURLConnection con) {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
        if (con != null) {
            con.disconnect();
        }
    }

    /**
	 * Sends an error report email as configured in the
	 * <code>ModuleConfiguration</code>.
	 * 
	 * @param exc
	 *            the <code>Exception</code> which is the basis of the error
	 *            report
	 */
    private void sendErrorReport(final Exception exc) {
        Date date = new Date();
        EmailConfiguration config = this.configuration.getErrorReportConfiguration();
        ArrayList<InternetAddress> recipients = new ArrayList<InternetAddress>();
        for (EmailAddressConfiguration recipientConf : config.getRecipients()) {
            recipients.add(recipientConf.getInternetAddress());
        }
        ArrayList<InternetAddress> senders = new ArrayList<InternetAddress>();
        for (EmailAddressConfiguration senderConf : config.getSenders()) {
            senders.add(senderConf.getInternetAddress());
        }
        String subject = config.getSubject();
        String separator = System.getProperty("line.separator");
        StringWriter writer = new StringWriter();
        exc.printStackTrace(new PrintWriter(writer));
        StringBuilder builder = new StringBuilder("Exception thrown by: ").append(getClass().getCanonicalName()).append(separator);
        builder.append("Date: ").append(DateFormat.getInstance().format(date)).append(separator).append(separator);
        builder.append(writer.toString());
        String text = builder.toString();
        for (InternetAddress recipient : recipients) {
            Email email = new Email();
            email.setRecipient(recipient);
            email.setFrom(senders);
            email.setSubject(subject);
            email.setTextContent(text);
            Lifecycle.getMailQueue().add(email);
        }
    }
}
