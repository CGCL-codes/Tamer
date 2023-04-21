package jolie.xml.xsd;

import java.util.List;
import jolie.lang.parse.ast.types.TypeDefinition;

/**
 *
 * @author Fabrizio Montesi
 */
public interface XsdToJolieConverter {

    public static class ConversionException extends Exception {

        public ConversionException(String message) {
            super(message);
        }
    }

    /**
	 * Converts a schema set into a list of JOLIE type definitions.
	 * @return a list of JOLIE type definitions obtained by reading the passed schema set.
	 * @throws ConversionException if an unsupported XSD element is encountered
	 * @see TypeDefinition
	 */
    public List<TypeDefinition> convert() throws ConversionException;
}
