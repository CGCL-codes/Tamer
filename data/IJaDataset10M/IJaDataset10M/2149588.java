package axs.jdbc.dataSpecification.supportedJavaTypes;

import axs.jdbc.dataSourceConfiguration.TypeException;

/**
 * @author 		Daniele Pes
 */
public interface ParserFromStringToObject {

    public Object fromString(String value) throws TypeException;

    public Object fromString(String value, String dateFormat) throws TypeException;
}
