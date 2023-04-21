package com.ail.invoice;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceInterface;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.financial.Invoice;

@ServiceInterface
public interface GenerateInvoiceNumberService {

    @ServiceArgument
    public interface GenerateInvoiceNumberArgument extends Argument {

        /**
         * Getter for the invoiceArg property. The invoice to generate a number for
         * @return Value of invoiceArg, or null if it is unset
         */
        Invoice getInvoiceArg();

        /**
         * Setter for the invoiceArg property. 
         * @see #getInvoiceArg
         * @param invoiceArg new value for property.
         */
        void setInvoiceArg(Invoice policyArg);

        /**
         * Getter for the uniqueNumberArg property. A unique number that may be used by the number generation service. This number
         * is guaranteed to be unique for each invocation of the service.
         * @return Value of uniqueNumberArg, or null if it is unset
         */
        int getUniqueNumberArg();

        /**
         * Setter for the uniqueNumberArg property. 
         * @see #getUniqueNumberArg
         * @param uniqueNumberArg new value for property.
         */
        void setUniqueNumberArg(int uniqueNumberArg);

        /**
         * Getter for the invoiceNumberRet property. The generated policy number.
         * @return Value of invoiceNumberRet, or null if it is unset
         */
        String getInvoiceNumberRet();

        /**
         * Setter for the invoiceNumberRet property. 
         * @see #getPolicyNumberRet
         * @param invoiceNumberRet new value for property.
         */
        void setInvoiceNumberRet(String invoiceNumberRet);
    }

    @ServiceCommand
    public interface GenerateInvoiceNumberCommand extends Command, GenerateInvoiceNumberArgument {
    }
}
