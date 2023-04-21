package uk.ac.ebi.intact.dataexchange.uniprotexport.factory;

import uk.ac.ebi.intact.util.uniprotExport.converters.encoreconverters.CCLineConverter;
import uk.ac.ebi.intact.util.uniprotExport.converters.encoreconverters.CCLineConverter1;
import uk.ac.ebi.intact.util.uniprotExport.converters.encoreconverters.CCLineConverter2;

/**
 * Factory for the CC line converters
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>08/03/11</pre>
 */
public class CCConverterFactory {

    public static CCLineConverter createCCConverter(int version) {
        switch(version) {
            case 1:
                return new CCLineConverter1();
            case 2:
                return new CCLineConverter2();
            default:
                return null;
        }
    }
}
