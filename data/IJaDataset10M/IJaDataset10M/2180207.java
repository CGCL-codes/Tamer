package com.hisham.parking.citations.web.touchnet;

import java.io.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.hisham.parking.citations.*;
import com.hisham.parking.citations.touchnet.*;
import com.hisham.parking.citations.web.*;

public class TnCpInfoForm extends CitationPaymentInfoForm implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8729031709110960227L;

    private TnCpTransactionInfo tnCpTransactionInfo;

    private TnCpTransactionResponse tnCpTransactionResponse;

    private String pmt_status = "";

    public TnCpInfoForm() {
        this.tnCpTransactionInfo = new TnCpTransactionInfo();
        this.tnCpTransactionResponse = new TnCpTransactionResponse();
    }

    public TnCpInfoForm(TnCpTransactionInfo tnCpTransactionInfo, TnCpTransactionResponse tnCpTransactionResponse) {
        this.tnCpTransactionInfo = tnCpTransactionInfo;
        this.tnCpTransactionResponse = tnCpTransactionResponse;
    }

    public TnCpTransactionInfo getTnCpTransactionInfo() {
        return this.tnCpTransactionInfo;
    }

    public void setTnCpTransactionInfo(TnCpTransactionInfo tnCpTransactionInfo) {
        this.tnCpTransactionInfo = tnCpTransactionInfo;
    }

    public TnCpTransactionResponse getTnCpTransactionResponse() {
        return this.tnCpTransactionResponse;
    }

    public double getPmt_amt() {
        return this.getTnCpTransactionResponse().getTnTransactionResponse().getCcTransactionAmount();
    }

    public String getPmt_status() {
        return pmt_status;
    }

    public void setTnCpTransactionResponse(TnCpTransactionResponse tnCpTransactionResponse) {
        this.tnCpTransactionResponse = tnCpTransactionResponse;
    }

    public void setPmt_amt(double pmt_amt) {
        this.getTnCpTransactionResponse().getTnTransactionResponse().setTnTransactionAmount(pmt_amt);
    }

    public void setPmt_status(String pmt_status) {
        boolean status = pmt_status.equalsIgnoreCase("success");
        this.getTnCpTransactionResponse().getCcTransactionResponse().setPreApprovedTransaction(status);
        this.getTnCpTransactionResponse().getCcTransactionResponse().setApprovedTransaction(status);
        this.pmt_status = pmt_status;
    }

    public CpTransactionInfo getCpTransactionInfo() {
        return this.getTnCpTransactionInfo();
    }

    public CpTransactionResponse getCpTransactionResponse() {
        return this.getTnCpTransactionResponse();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
        return super.validate(actionMapping, httpServletRequest);
    }

    public void setEXT_TRANS_ID(String extTransId) {
        this.getCitationInfoForm().setCitationNumbers(extTransId);
        this.setCitationNumbers(this.getCitationInfoForm().getCitationInfos().getCitationNumbers());
    }

    public void setSys_tracking_id(String trackingId) {
        this.setOrderNumber(trackingId);
        this.getCpTransactionInfo().getCcTransactionInfo().getCreditcard().setCcNumber(trackingId);
    }

    public void setCard_type(String cardType) {
        this.getCpTransactionInfo().getCcTransactionInfo().getCreditcard().setCcType(cardType);
    }
}
