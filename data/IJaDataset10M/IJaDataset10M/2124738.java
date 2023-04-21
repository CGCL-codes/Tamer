package monkey.generator;

import monkey.generator.java.JavaGenerator;
import java.util.Hashtable;

/**
 * Singleton class responsible for creating language depended generators.<br>
 *
 * @see Generator
 */
public class GeneratorFactory {

    private Hashtable generatosClasses;

    private static GeneratorFactory instance;

    private GeneratorFactory() {
        generatosClasses = new Hashtable();
        registerGeneratorClass(JavaGenerator.LANGUAGE, JavaGenerator.class);
    }

    /**
     * @param language the language id
     * @return a new instance of a concrete language generator
     * @throws UnknownLanguageException
     */
    public Generator createGenerator(String language) throws UnknownLanguageException {
        Class generatorClass = (Class) generatosClasses.get(language);
        if (generatorClass != null) {
            try {
                return (Generator) generatorClass.newInstance();
            } catch (Throwable throwable) {
            }
        }
        throw new UnknownLanguageException(language);
    }

    /**
     * @return an instance of GeneratorFactory
     */
    public static GeneratorFactory getInstance() {
        if (instance == null) {
            instance = new GeneratorFactory();
        }
        return instance;
    }

    /**
     * Registers a language depended generator for a specified language.
     *
     * @param language
     * @param generatorClass
     */
    public void registerGeneratorClass(String language, Class generatorClass) {
        generatosClasses.put(language, generatorClass);
    }
}
