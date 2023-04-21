package net.sourceforge.javo2.compiler.codegeneration;

import generated.ValueObject;
import java.util.Collection;
import net.sourceforge.javo2.compiler.configuration.Configuration;

/**
 * @author Nicolás Di Benedetto
 * {@link mailto:nikodb@users.sourceforge.net nikodb@users.sourceforge.net}.<br>
 * Created on 26/09/2007.<br>
 */
public interface ICodeGenerator {

    /**
	 * Generates code for given definitions. 
	 * @param definitions the value object definitions to generate.
	 * @param config the {@link Configuration} instance to set the environment.
	 * @throws CodeGenerationException in case of an exception during the code generation process.
	 */
    public void generateCode(Collection<ValueObject> definitions, Configuration config) throws CodeGenerationException;
}
