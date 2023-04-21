package com.hisham.parking.citations.web.skipjack;

import java.io.*;
import com.hisham.parking.citations.*;
import com.hisham.parking.citations.skipjack.*;
import com.hisham.parking.citations.web.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;

public class SjCpInfoForm extends CitationPaymentInfoForm implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8384898298717687895L;

    private SjCpTransactionInfo sjCpTransactionInfo;

    private SjCpTransactionResponse sjCpTransactionResponse;

    public SjCpInfoForm() {
        this.sjCpTransactionInfo = new SjCpTransactionInfo();
        this.sjCpTransactionResponse = new SjCpTransactionResponse();
    }

    public SjCpInfoForm(SjCpTransactionInfo sjCpTransactionInfo, SjCpTransactionResponse sjCpTransactionResponse) {
        this.sjCpTransactionInfo = sjCpTransactionInfo;
        this.sjCpTransactionResponse = sjCpTransactionResponse;
    }

    public SjCpTransactionInfo getSjCpTransactionInfo() {
        return this.sjCpTransactionInfo;
    }

    public void setSjCpTransactionInfo(SjCpTransactionInfo sjCpTransactionInfo) {
        this.sjCpTransactionInfo = sjCpTransactionInfo;
    }

    public SjCpTransactionResponse getSjCpTransactionResponse() {
        return this.sjCpTransactionResponse;
    }

    public void setSjCpTransactionResponse(SjCpTransactionResponse sjCpTransactionResponse) {
        this.sjCpTransactionResponse = sjCpTransactionResponse;
    }

    public CpTransactionInfo getCpTransactionInfo() {
        return this.getSjCpTransactionInfo();
    }

    public CpTransactionResponse getCpTransactionResponse() {
        return this.getSjCpTransactionResponse();
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
}
