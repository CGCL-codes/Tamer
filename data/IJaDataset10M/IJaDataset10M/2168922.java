package com.db4o.odbgen.plugins.java.maingenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import com.db4o.internal.odbgen.TemplatesUtils;
import com.db4o.odbgen.OdbGen;
import com.db4o.odbgen.OdbgenException;
import com.db4o.odbgen.TargetLanguage;
import com.db4o.odbgen.plugins.MainGeneratorPlugin;
import com.db4o.internal.odbgen.plugins.OptionList;
import com.db4o.odbgen.plugins.Plugin;
import com.db4o.odbgen.plugins.java.maingenerator.typedxmlschema.CodeStorage;
import com.db4o.odbgen.plugins.java.maingenerator.typedxmlschema.TypedXmlSchema;
import com.db4o.odbgen.plugins.xmlschema.XmlSchema;

public class MainGenerator implements Plugin, MainGeneratorPlugin {

    private TypedXmlSchema _typedXmlSchema;

    private OdbGen _odbGen;

    private List<Plugin> _pluginStack;

    private XmlSchema _xmlSchema;

    private XmlSchemaUtils _db4oUtils;

    private enum Option implements OptionList {

        /**
		 * The root package of each generated source file.
		 * Each package found within XmlSchema will be
		 * prefixed with the root package.
		 * This is optional.
		 */
        ROOTPACKAGE("rootPackage", ""), /**
		 * The comma separated list of imports that will be added to
		 * all generate source files.
		 * This is optional.
		 */
        IMPORTS("imports", ""), /**
		 * The directory where to output ObjectSchema.
		 * This directory must exist prior to running the generator.
		 * This is mandatory.
		 */
        OUTPUTDIRECTORY("outputDirectory", null), /**
		 * The path to the jar containing the standard templates.
		 * This is mandatory. 
		 */
        STANDARDTEMPLATESJAR("standardTemplatesJar", null);

        private String _defaultValue;

        private String _name;

        Option(String name, String defaultValue) {
            this._name = name;
            this._defaultValue = defaultValue;
        }

        /**
		 * See {@link OptionList} for details.
		 */
        @Override
        public String getName() {
            return this._name;
        }

        /**
		 * See {@link OptionList} for details.
		 */
        @Override
        public String getDefaultValue() {
            return this._defaultValue;
        }
    }

    /**
	 * Returns a {@link XmlSchemaUtils} object.
	 * @return
	 */
    public XmlSchemaUtils getSchemaUtils() {
        return this._db4oUtils;
    }

    /**
	 * See {@link Plugin} for details.
	 */
    @Override
    public boolean supportsTargetLanguage(TargetLanguage targetLanguage) {
        if (targetLanguage == TargetLanguage.JAVA) return true; else return false;
    }

    /**
	 * See {@link Plugin} for details.
	 */
    @Override
    public void initialize(MainGeneratorPlugin mainGenerator) {
    }

    /**
	 * See {@link Plugin} for details.
	 */
    @Override
    public List<Class<? extends Plugin>> getPluginDependencies() {
        return new ArrayList<Class<? extends Plugin>>();
    }

    /**
	 * See {@link MainGeneratorPlugin} for details.
	 */
    @Override
    public void initializeMainGenerator(OdbGen odbGen, XmlSchema xmlSchema, List<Plugin> pluginStack) throws OdbgenException {
        this._odbGen = odbGen;
        this._pluginStack = pluginStack;
        this._xmlSchema = xmlSchema;
        if (this.getOption(MainGenerator.Option.STANDARDTEMPLATESJAR) == null) {
            throw new OdbgenException("The option '-%s' is not specified.", MainGenerator.Option.STANDARDTEMPLATESJAR.getName());
        }
        if (!new File(this.getOption(MainGenerator.Option.STANDARDTEMPLATESJAR)).exists()) {
            throw new OdbgenException("Standard template file '%s' not found.", MainGenerator.Option.STANDARDTEMPLATESJAR.getName());
        }
        String outputDirPath = this.getOption(MainGenerator.Option.OUTPUTDIRECTORY);
        if (outputDirPath == null) {
            throw new OdbgenException("The option '-%s' is not specified.", MainGenerator.Option.OUTPUTDIRECTORY.getName());
        }
        if (!new File(outputDirPath).exists()) {
            throw new OdbgenException("The directory '%s' cannot be found.", new File(outputDirPath).getAbsolutePath());
        }
        this.generateTypdeXmlSchema();
        this._db4oUtils = new XmlSchemaUtils(this);
    }

    /**
	 * Use this to get the name of the jar that holds the standard templated.
	 * Use this along with {@link TemplatesUtils}.
	 * @return
	 */
    public String getStandardTemplatesJarName() {
        return this.getOption(MainGenerator.Option.STANDARDTEMPLATESJAR);
    }

    /**
	 * See {@link MainGeneratorPlugin} for details.
	 * @throws OdbgenException 
	 */
    @Override
    public void start() throws OdbgenException {
        for (int i = 0; i < this._pluginStack.size(); i++) {
            Plugin plugin = this._pluginStack.get(i);
            if (plugin != this) {
                plugin.start();
            }
        }
        CodeStorage codeStorage = new CodeStorage(this._typedXmlSchema, new File(this.getOption(MainGenerator.Option.OUTPUTDIRECTORY)), this.getStandardTemplatesJarName());
        codeStorage.writeStorage();
    }

    /**
	 * See {@link MainGeneratorPlugin} for details.
	 */
    @Override
    public Properties getOptions() {
        return this._odbGen.getOptions();
    }

    /**
	 * Returns the specified option if found, otherwise returns the default value.
	 */
    private String getOption(Option option) {
        return this.getOptions().getProperty(option.getName(), option.getDefaultValue());
    }

    /**
	 * Use this to query the XmlSchema.
	 */
    public TypedXmlSchema getTypedXmlSchema() {
        return _typedXmlSchema;
    }

    /**
	 * Generates the TypedXmlSchema given OdbGen's XmlSchema.
	 */
    private void generateTypdeXmlSchema() throws OdbgenException {
        String rootPackage = this.getOption(MainGenerator.Option.ROOTPACKAGE);
        this._typedXmlSchema = new TypedXmlSchema(this._xmlSchema, rootPackage, this.getOption(MainGenerator.Option.IMPORTS), this.getStandardTemplatesJarName());
    }

    /**
	 * Returns the root package as received through '-rootpackage' option.
	 */
    public String getRootPackage() {
        return this.getOption(Option.ROOTPACKAGE);
    }
}
