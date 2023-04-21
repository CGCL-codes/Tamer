package org.jaffa.modules.printing.components.printerdefinitionlookup;

import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.modules.printing.components.printerdefinitionlookup.dto.PrinterDefinitionLookupInDto;
import org.jaffa.modules.printing.components.printerdefinitionlookup.dto.PrinterDefinitionLookupOutDto;

/** Interface for PrinterDefinitionLookup components.
 */
public interface IPrinterDefinitionLookup {

    /** Searches for PrinterDefinition objects.
     * @param input The criteria based on which the search will be performed.
     * @throws ApplicationExceptions This will be thrown if the criteria contains invalid data.
     * @throws FrameworkException Indicates some system error
     * @return The search results.
     */
    public PrinterDefinitionLookupOutDto find(PrinterDefinitionLookupInDto input) throws FrameworkException, ApplicationExceptions;

    /**
     * This should be invoked, when done with the component.
     */
    public void destroy();
}
