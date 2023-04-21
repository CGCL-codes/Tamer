package org.jaffa.modules.printing.components.printerdefinitionfinder;

import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.modules.printing.components.printerdefinitionfinder.dto.PrinterDefinitionFinderInDto;
import org.jaffa.modules.printing.components.printerdefinitionfinder.dto.PrinterDefinitionFinderOutDto;

/** Interface for PrinterDefinitionFinder components.
 */
public interface IPrinterDefinitionFinder {

    /** Searches for PrinterDefinition objects.
     * @param input The criteria based on which the search will be performed.
     * @throws ApplicationExceptions This will be thrown if the criteria contains invalid data.
     * @throws FrameworkException Indicates some system error
     * @return The search results.
     */
    public PrinterDefinitionFinderOutDto find(PrinterDefinitionFinderInDto input) throws FrameworkException, ApplicationExceptions;

    /**
     * This should be invoked, when done with the component.
     */
    public void destroy();
}
