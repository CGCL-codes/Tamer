package com.ail.insurance.quotation;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceInterface;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.insurance.policy.Policy;

@ServiceInterface
public interface FetchQuoteService {

    @ServiceArgument
    public interface FetchDocumentArgument extends Argument {

        /**
         * The number of the quotation for which a document should be returned
         * @return quotation number.
         */
        String getQuotationNumberArg();

        /**
         * @see #getInvoiceArg()
         * @param policyArg
         */
        void setQuotationNumberArg(String quotationNumberArg);

        /**
         * Fetch the quotation which may have been modified by the process of document generation
         * @return Modified quotation
         */
        Policy getQuotationRet();

        /**
         * @see #getQuotationRet()
         * @param policyRet
         */
        void setQuotationRet(Policy policyRet);

        /**
         * The generated document.
         * @return document
         */
        byte[] getDocumentRet();

        /**
         * @see #getDocumentRet()
         * @param documentRet
         */
        void setDocumentRet(byte[] documentRet);
    }

    @ServiceCommand
    public interface FetchDocumentCommand extends Command, FetchDocumentArgument {
    }
}
