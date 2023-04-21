package com.ail.invoice;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.Functions;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.core.document.model.DocumentDefinition;
import com.ail.core.key.GenerateUniqueKeyService.GenerateUniqueKeyCommand;
import com.ail.invoice.GenerateInvoiceNumberService.GenerateInvoiceNumberCommand;
import com.ail.financial.Invoice;

/**
 * Service to generate an invoice document. This service delegates to the three document
 * generation phase services: Merge, Style and Render. The actual services used in the
 * generation phases depends on the {@link DocumentDefinition} type defined in the product associated
 * with the policy for which a document is being generated. By convention, this type is named "InvoiceDocument".
 */
@ServiceImplementation
public class AddInvoiceNumberService extends Service<AddInvoiceNumberService.AddInvoiceNumberArgument> {

    private static final long serialVersionUID = 3198893603833694389L;

    private String configurationNamespace = "com.ail.invoice.AddInvoiceNumberService";

    @ServiceArgument
    public interface AddInvoiceNumberArgument extends Argument {

        Invoice getInvoiceArgRet();

        void setInvoiceArgRet(Invoice invoiceArgRet);
    }

    @ServiceCommand(defaultServiceClass = AddInvoiceNumberService.class)
    public interface AddInvoiceNumberCommand extends Command, AddInvoiceNumberArgument {
    }

    /**
     * Return the product name from the arguments as the configuration namespace. 
     * The has the effect of selecting the product's configuration.
     * @return product name
     */
    @Override
    public String getConfigurationNamespace() {
        return configurationNamespace;
    }

    /**
     * The 'business logic' of the entry point.
     * @throws PreconditionException If one of the preconditions is not met
     * @throws SectionNotFoundException If one of the sections identified in the
     */
    @Override
    public void invoke() throws BaseException {
        if (args.getInvoiceArgRet() == null) {
            throw new PreconditionException("args.getInvoiceArgRet()==null");
        }
        if (args.getInvoiceArgRet().getProductTypeId() == null) {
            throw new PreconditionException("args.getInvoiceArgRet().getProductTypeId()==null");
        }
        if (args.getInvoiceArgRet().getInvoiceNumber() != null) {
            throw new PreconditionException("args.getInvoiceArgRet().getInvoiceNumber()!=null");
        }
        configurationNamespace = Functions.productNameToConfigurationNamespace(args.getInvoiceArgRet().getProductTypeId());
        GenerateUniqueKeyCommand gukc = core.newCommand(GenerateUniqueKeyCommand.class);
        gukc.setKeyIdArg("InvoiceNumber");
        gukc.invoke();
        GenerateInvoiceNumberCommand command = core.newCommand(GenerateInvoiceNumberCommand.class);
        command.setInvoiceArg(args.getInvoiceArgRet());
        command.setUniqueNumberArg(gukc.getKeyRet());
        command.invoke();
        String invoiceNumber = command.getInvoiceNumberRet();
        args.getInvoiceArgRet().setInvoiceNumber(invoiceNumber);
        core.logDebug("Invoice number: " + invoiceNumber + " generated");
        if (args.getInvoiceArgRet().getInvoiceNumber() == null || args.getInvoiceArgRet().getInvoiceNumber().length() == 0) {
            throw new PostconditionException("args.getInvoiceArgRet().getInvoiceNumber()==null || args.getInvoiceArgRet().getInvoiceNumber().length()==0");
        }
    }
}
