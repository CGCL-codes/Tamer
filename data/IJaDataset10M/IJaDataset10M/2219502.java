package org.jomc.cli.commands;

import java.util.logging.Level;
import org.apache.commons.cli.CommandLine;
import org.jomc.tools.SourceFileProcessor;

@javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
public abstract class AbstractSourceFileProcessorCommand extends AbstractJomcToolCommand {

    /**
     * Creates a new {@code SourceFileProcessor} instance taking a command line.
     *
     * @param commandLine The command line to process.
     *
     * @return A new {@code SourceFileProcessor} instance as specified by the given command line or {@code null}, if
     * creating a new instance fails.
     *
     * @throws NullPointerException if {@code commandLine} is {@code null}.
     * @throws CommandExecutionException if creating a new instance fails.
     */
    protected SourceFileProcessor createSourceFileProcessor(final CommandLine commandLine) throws CommandExecutionException {
        if (commandLine == null) {
            throw new NullPointerException("commandLine");
        }
        final String className = commandLine.hasOption(this.getSourceFileProcessorOption().getOpt()) ? commandLine.getOptionValue(this.getSourceFileProcessorOption().getOpt()) : SourceFileProcessor.class.getName();
        return this.createJomcTool(className, SourceFileProcessor.class, commandLine);
    }

    /** {@inheritDoc} */
    protected final void executeCommand(final CommandLine commandLine) throws CommandExecutionException {
        if (commandLine == null) {
            throw new NullPointerException("commandLine");
        }
        if (commandLine.hasOption(this.getNoSourceProcessingOption().getOpt())) {
            this.log(Level.INFO, this.getDisabledMessage(this.getLocale()), null);
        } else {
            this.processSourceFiles(commandLine);
        }
    }

    /**
     * Processes source files.
     *
     * @param commandLine The command line to execute.
     *
     * @throws CommandExecutionException if processing source files fails.
     */
    protected abstract void processSourceFiles(final CommandLine commandLine) throws CommandExecutionException;

    /** Creates a new {@code AbstractSourceFileProcessorCommand} instance. */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    public AbstractSourceFileProcessorCommand() {
        super();
    }

    /**
     * Gets the {@code <ClasspathOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Classpath Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <ClasspathOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getClasspathOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "ClasspathOption");
        assert _d != null : "'ClasspathOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <CountryOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Country Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <CountryOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getCountryOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "CountryOption");
        assert _d != null : "'CountryOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <DefaultTemplateProfileOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Default Template Profile Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <DefaultTemplateProfileOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getDefaultTemplateProfileOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "DefaultTemplateProfileOption");
        assert _d != null : "'DefaultTemplateProfileOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <DocumentsOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Documents Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <DocumentsOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getDocumentsOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "DocumentsOption");
        assert _d != null : "'DocumentsOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <ImplementationOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Implementation Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <ImplementationOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getImplementationOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "ImplementationOption");
        assert _d != null : "'ImplementationOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <IndentationStringOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Indentation String Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <IndentationStringOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getIndentationStringOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "IndentationStringOption");
        assert _d != null : "'IndentationStringOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <InputEncodingOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Input Encoding Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <InputEncodingOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getInputEncodingOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "InputEncodingOption");
        assert _d != null : "'InputEncodingOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <LanguageOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Language Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <LanguageOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getLanguageOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "LanguageOption");
        assert _d != null : "'LanguageOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <LineSeparatorOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Line Separator Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <LineSeparatorOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getLineSeparatorOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "LineSeparatorOption");
        assert _d != null : "'LineSeparatorOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <Locale>} dependency.
     * <p>
     *   This method returns the {@code <default>} object of the {@code <java.util.Locale>} specification at specification level 1.1.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <Locale>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private java.util.Locale getLocale() {
        final java.util.Locale _d = (java.util.Locale) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "Locale");
        assert _d != null : "'Locale' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <LocaleVariantOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Locale Variant Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <LocaleVariantOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getLocaleVariantOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "LocaleVariantOption");
        assert _d != null : "'LocaleVariantOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <ModelContextFactoryOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI ModelContextFactory Class Name Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <ModelContextFactoryOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getModelContextFactoryOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "ModelContextFactoryOption");
        assert _d != null : "'ModelContextFactoryOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <ModelOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Model Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <ModelOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getModelOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "ModelOption");
        assert _d != null : "'ModelOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <ModletLocationOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Modlet Location Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <ModletLocationOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getModletLocationOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "ModletLocationOption");
        assert _d != null : "'ModletLocationOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <ModletSchemaSystemIdOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Modlet Schema System Id Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <ModletSchemaSystemIdOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getModletSchemaSystemIdOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "ModletSchemaSystemIdOption");
        assert _d != null : "'ModletSchemaSystemIdOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <ModuleLocationOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Module Location Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <ModuleLocationOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getModuleLocationOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "ModuleLocationOption");
        assert _d != null : "'ModuleLocationOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <ModuleNameOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Module Name Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <ModuleNameOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getModuleNameOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "ModuleNameOption");
        assert _d != null : "'ModuleNameOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <NoClasspathResolutionOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI No Classpath Resolution Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <NoClasspathResolutionOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getNoClasspathResolutionOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "NoClasspathResolutionOption");
        assert _d != null : "'NoClasspathResolutionOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <NoModelProcessingOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI No Model Processing Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <NoModelProcessingOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getNoModelProcessingOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "NoModelProcessingOption");
        assert _d != null : "'NoModelProcessingOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <NoModelResourceValidation>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI No Model Resource Validation Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <NoModelResourceValidation>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getNoModelResourceValidation() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "NoModelResourceValidation");
        assert _d != null : "'NoModelResourceValidation' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <NoModletResourceValidation>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI No Modlet Resource Validation Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <NoModletResourceValidation>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getNoModletResourceValidation() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "NoModletResourceValidation");
        assert _d != null : "'NoModletResourceValidation' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <NoSourceProcessingOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI No Source File Processing Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <NoSourceProcessingOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getNoSourceProcessingOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "NoSourceProcessingOption");
        assert _d != null : "'NoSourceProcessingOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <OutputEncodingOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Output Encoding Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <OutputEncodingOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getOutputEncodingOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "OutputEncodingOption");
        assert _d != null : "'OutputEncodingOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <PlatformProviderLocationOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Platform Provider Location Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <PlatformProviderLocationOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getPlatformProviderLocationOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "PlatformProviderLocationOption");
        assert _d != null : "'PlatformProviderLocationOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <ProviderLocationOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Provider Location Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <ProviderLocationOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getProviderLocationOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "ProviderLocationOption");
        assert _d != null : "'ProviderLocationOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <SourceDirectoryOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Source Directory Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <p><strong>Properties:</strong>
     *   <table border="1" width="100%" cellpadding="3" cellspacing="0">
     *     <tr class="TableSubHeadingColor">
     *       <th align="left" scope="col" nowrap><b>Name</b></th>
     *       <th align="left" scope="col" nowrap><b>Type</b></th>
     *       <th align="left" scope="col" nowrap><b>Documentation</b></th>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>{@code <required>}</td>
     *       <td align="left" valign="top" nowrap>{@code boolean}</td>
     *       <td align="left" valign="top"></td>
     *     </tr>
     *   </table>
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <SourceDirectoryOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getSourceDirectoryOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "SourceDirectoryOption");
        assert _d != null : "'SourceDirectoryOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <SourceFileProcessorOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI SourceFileProcessor Class Name Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <SourceFileProcessorOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getSourceFileProcessorOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "SourceFileProcessorOption");
        assert _d != null : "'SourceFileProcessorOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <SpecificationOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Specification Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <SpecificationOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getSpecificationOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "SpecificationOption");
        assert _d != null : "'SpecificationOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <TemplateEncodingOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Template Encoding Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <TemplateEncodingOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getTemplateEncodingOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "TemplateEncodingOption");
        assert _d != null : "'TemplateEncodingOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <TemplateLocationOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Template Location Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <TemplateLocationOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getTemplateLocationOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "TemplateLocationOption");
        assert _d != null : "'TemplateLocationOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <TemplateProfileOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Template Profile Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <TemplateProfileOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getTemplateProfileOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "TemplateProfileOption");
        assert _d != null : "'TemplateProfileOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the {@code <TransformerLocationOption>} dependency.
     * <p>
     *   This method returns the {@code <JOMC CLI Transformer Location Option>} object of the {@code <JOMC CLI Command Option>} specification at specification level 1.2.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <TransformerLocationOption>} dependency.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private org.apache.commons.cli.Option getTransformerLocationOption() {
        final org.apache.commons.cli.Option _d = (org.apache.commons.cli.Option) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "TransformerLocationOption");
        assert _d != null : "'TransformerLocationOption' dependency not found.";
        return _d;
    }

    /**
     * Gets the value of the {@code <abbreviatedCommandName>} property.
     * <p><dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @return Abbreviated name of the command.
     * @throws org.jomc.ObjectManagementException if getting the property instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private java.lang.String getAbbreviatedCommandName() {
        final java.lang.String _p = (java.lang.String) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getProperty(this, "abbreviatedCommandName");
        assert _p != null : "'abbreviatedCommandName' property not found.";
        return _p;
    }

    /**
     * Gets the value of the {@code <applicationModlet>} property.
     * <p><dl>
     *   <dt><b>Final:</b></dt><dd>Yes</dd>
     * </dl></p>
     * @return Name of the 'shaded' application modlet.
     * @throws org.jomc.ObjectManagementException if getting the property instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private java.lang.String getApplicationModlet() {
        final java.lang.String _p = (java.lang.String) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getProperty(this, "applicationModlet");
        assert _p != null : "'applicationModlet' property not found.";
        return _p;
    }

    /**
     * Gets the value of the {@code <commandName>} property.
     * <p><dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @return Name of the command.
     * @throws org.jomc.ObjectManagementException if getting the property instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private java.lang.String getCommandName() {
        final java.lang.String _p = (java.lang.String) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getProperty(this, "commandName");
        assert _p != null : "'commandName' property not found.";
        return _p;
    }

    /**
     * Gets the value of the {@code <modletExcludes>} property.
     * <p><dl>
     *   <dt><b>Final:</b></dt><dd>Yes</dd>
     * </dl></p>
     * @return List of modlet names to exclude from any {@code META-INF/jomc-modlet.xml} files separated by {@code :}.
     * @throws org.jomc.ObjectManagementException if getting the property instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private java.lang.String getModletExcludes() {
        final java.lang.String _p = (java.lang.String) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getProperty(this, "modletExcludes");
        assert _p != null : "'modletExcludes' property not found.";
        return _p;
    }

    /**
     * Gets the value of the {@code <providerExcludes>} property.
     * <p><dl>
     *   <dt><b>Final:</b></dt><dd>Yes</dd>
     * </dl></p>
     * @return List of providers to exclude from any {@code META-INF/services} files separated by {@code :}.
     * @throws org.jomc.ObjectManagementException if getting the property instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private java.lang.String getProviderExcludes() {
        final java.lang.String _p = (java.lang.String) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getProperty(this, "providerExcludes");
        assert _p != null : "'providerExcludes' property not found.";
        return _p;
    }

    /**
     * Gets the value of the {@code <schemaExcludes>} property.
     * <p><dl>
     *   <dt><b>Final:</b></dt><dd>Yes</dd>
     * </dl></p>
     * @return List of schema context-ids to exclude from any {@code META-INF/jomc-modlet.xml} files separated by {@code :}.
     * @throws org.jomc.ObjectManagementException if getting the property instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private java.lang.String getSchemaExcludes() {
        final java.lang.String _p = (java.lang.String) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getProperty(this, "schemaExcludes");
        assert _p != null : "'schemaExcludes' property not found.";
        return _p;
    }

    /**
     * Gets the value of the {@code <serviceExcludes>} property.
     * <p><dl>
     *   <dt><b>Final:</b></dt><dd>Yes</dd>
     * </dl></p>
     * @return List of service classes to exclude from any {@code META-INF/jomc-modlet.xml} files separated by {@code :}.
     * @throws org.jomc.ObjectManagementException if getting the property instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private java.lang.String getServiceExcludes() {
        final java.lang.String _p = (java.lang.String) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getProperty(this, "serviceExcludes");
        assert _p != null : "'serviceExcludes' property not found.";
        return _p;
    }

    /**
     * Gets the text of the {@code <applicationTitle>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @return The text of the {@code <applicationTitle>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getApplicationTitle(final java.util.Locale locale) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "applicationTitle", locale);
        assert _m != null : "'applicationTitle' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <cannotProcessMessage>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param itemInfo Format argument.
     * @param detailMessage Format argument.
     * @return The text of the {@code <cannotProcessMessage>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getCannotProcessMessage(final java.util.Locale locale, final java.lang.String itemInfo, final java.lang.String detailMessage) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "cannotProcessMessage", locale, itemInfo, detailMessage);
        assert _m != null : "'cannotProcessMessage' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <classpathElementInfo>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param classpathElement Format argument.
     * @return The text of the {@code <classpathElementInfo>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getClasspathElementInfo(final java.util.Locale locale, final java.lang.String classpathElement) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "classpathElementInfo", locale, classpathElement);
        assert _m != null : "'classpathElementInfo' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <classpathElementNotFoundWarning>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param fileName Format argument.
     * @return The text of the {@code <classpathElementNotFoundWarning>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getClasspathElementNotFoundWarning(final java.util.Locale locale, final java.lang.String fileName) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "classpathElementNotFoundWarning", locale, fileName);
        assert _m != null : "'classpathElementNotFoundWarning' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <commandFailureMessage>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param toolName Format argument.
     * @return The text of the {@code <commandFailureMessage>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getCommandFailureMessage(final java.util.Locale locale, final java.lang.String toolName) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "commandFailureMessage", locale, toolName);
        assert _m != null : "'commandFailureMessage' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <commandInfoMessage>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param toolName Format argument.
     * @return The text of the {@code <commandInfoMessage>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getCommandInfoMessage(final java.util.Locale locale, final java.lang.String toolName) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "commandInfoMessage", locale, toolName);
        assert _m != null : "'commandInfoMessage' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <commandSuccessMessage>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param toolName Format argument.
     * @return The text of the {@code <commandSuccessMessage>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getCommandSuccessMessage(final java.util.Locale locale, final java.lang.String toolName) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "commandSuccessMessage", locale, toolName);
        assert _m != null : "'commandSuccessMessage' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <defaultLogLevelInfo>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param defaultLogLevel Format argument.
     * @return The text of the {@code <defaultLogLevelInfo>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getDefaultLogLevelInfo(final java.util.Locale locale, final java.lang.String defaultLogLevel) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "defaultLogLevelInfo", locale, defaultLogLevel);
        assert _m != null : "'defaultLogLevelInfo' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <disabledMessage>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @return The text of the {@code <disabledMessage>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getDisabledMessage(final java.util.Locale locale) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "disabledMessage", locale);
        assert _m != null : "'disabledMessage' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <documentFileInfo>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param documentFile Format argument.
     * @return The text of the {@code <documentFileInfo>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getDocumentFileInfo(final java.util.Locale locale, final java.lang.String documentFile) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "documentFileInfo", locale, documentFile);
        assert _m != null : "'documentFileInfo' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <documentFileNotFoundWarning>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param fileName Format argument.
     * @return The text of the {@code <documentFileNotFoundWarning>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getDocumentFileNotFoundWarning(final java.util.Locale locale, final java.lang.String fileName) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "documentFileNotFoundWarning", locale, fileName);
        assert _m != null : "'documentFileNotFoundWarning' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <excludedModletInfo>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param resourceName Format argument.
     * @param modletIdentifier Format argument.
     * @return The text of the {@code <excludedModletInfo>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getExcludedModletInfo(final java.util.Locale locale, final java.lang.String resourceName, final java.lang.String modletIdentifier) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "excludedModletInfo", locale, resourceName, modletIdentifier);
        assert _m != null : "'excludedModletInfo' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <excludedProviderInfo>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param resourceName Format argument.
     * @param providerName Format argument.
     * @return The text of the {@code <excludedProviderInfo>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getExcludedProviderInfo(final java.util.Locale locale, final java.lang.String resourceName, final java.lang.String providerName) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "excludedProviderInfo", locale, resourceName, providerName);
        assert _m != null : "'excludedProviderInfo' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <excludedSchemaInfo>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param resourceName Format argument.
     * @param contextId Format argument.
     * @return The text of the {@code <excludedSchemaInfo>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getExcludedSchemaInfo(final java.util.Locale locale, final java.lang.String resourceName, final java.lang.String contextId) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "excludedSchemaInfo", locale, resourceName, contextId);
        assert _m != null : "'excludedSchemaInfo' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <excludedServiceInfo>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param resourceName Format argument.
     * @param serviceName Format argument.
     * @return The text of the {@code <excludedServiceInfo>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getExcludedServiceInfo(final java.util.Locale locale, final java.lang.String resourceName, final java.lang.String serviceName) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "excludedServiceInfo", locale, resourceName, serviceName);
        assert _m != null : "'excludedServiceInfo' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <failedCreatingObjectMessage>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param objectInfo Format argument.
     * @return The text of the {@code <failedCreatingObjectMessage>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getFailedCreatingObjectMessage(final java.util.Locale locale, final java.lang.String objectInfo) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "failedCreatingObjectMessage", locale, objectInfo);
        assert _m != null : "'failedCreatingObjectMessage' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <implementationNotFoundWarning>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>Yes</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param implementationIdentifier Format argument.
     * @return The text of the {@code <implementationNotFoundWarning>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getImplementationNotFoundWarning(final java.util.Locale locale, final java.lang.String implementationIdentifier) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "implementationNotFoundWarning", locale, implementationIdentifier);
        assert _m != null : "'implementationNotFoundWarning' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <invalidModelMessage>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param modelIdentifier Format argument.
     * @return The text of the {@code <invalidModelMessage>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getInvalidModelMessage(final java.util.Locale locale, final java.lang.String modelIdentifier) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "invalidModelMessage", locale, modelIdentifier);
        assert _m != null : "'invalidModelMessage' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <longDescriptionMessage>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @return The text of the {@code <longDescriptionMessage>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getLongDescriptionMessage(final java.util.Locale locale) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "longDescriptionMessage", locale);
        assert _m != null : "'longDescriptionMessage' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <moduleNotFoundWarning>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>Yes</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param moduleName Format argument.
     * @return The text of the {@code <moduleNotFoundWarning>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getModuleNotFoundWarning(final java.util.Locale locale, final java.lang.String moduleName) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "moduleNotFoundWarning", locale, moduleName);
        assert _m != null : "'moduleNotFoundWarning' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <readingMessage>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param locationInfo Format argument.
     * @return The text of the {@code <readingMessage>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getReadingMessage(final java.util.Locale locale, final java.lang.String locationInfo) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "readingMessage", locale, locationInfo);
        assert _m != null : "'readingMessage' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <separator>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @return The text of the {@code <separator>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getSeparator(final java.util.Locale locale) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "separator", locale);
        assert _m != null : "'separator' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <shortDescriptionMessage>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @return The text of the {@code <shortDescriptionMessage>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getShortDescriptionMessage(final java.util.Locale locale) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "shortDescriptionMessage", locale);
        assert _m != null : "'shortDescriptionMessage' message not found.";
        return _m;
    }

    /**
     * Gets the text of the {@code <specificationNotFoundWarning>} message.
     * <p><dl>
     *   <dt><b>Languages:</b></dt>
     *     <dd>English (default)</dd>
     *     <dd>Deutsch</dd>
     *   <dt><b>Final:</b></dt><dd>Yes</dd>
     * </dl></p>
     * @param locale The locale of the message to return.
     * @param specificationIdentifier Format argument.
     * @return The text of the {@code <specificationNotFoundWarning>} message for {@code locale}.
     * @throws org.jomc.ObjectManagementException if getting the message instance fails.
     */
    @SuppressWarnings("unused")
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    private String getSpecificationNotFoundWarning(final java.util.Locale locale, final java.lang.String specificationIdentifier) {
        final String _m = org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getMessage(this, "specificationNotFoundWarning", locale, specificationIdentifier);
        assert _m != null : "'specificationNotFoundWarning' message not found.";
        return _m;
    }

    /**
     * Gets the options of the command.
     * <p><strong>Options:</strong>
     *   <table border="1" width="100%" cellpadding="3" cellspacing="0">
     *     <tr class="TableSubHeadingColor">
     *       <th align="left" scope="col" nowrap><b>Specification</b></th>
     *       <th align="left" scope="col" nowrap><b>Implementation</b></th>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Classpath Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Country Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Default Template Profile Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Documents Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Implementation Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Indentation String Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Input Encoding Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Language Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Line Separator Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Locale Variant Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI ModelContextFactory Class Name Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Model Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Modlet Location Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Modlet Schema System Id Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Module Location Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Module Name Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI No Classpath Resolution Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI No Model Processing Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI No Model Resource Validation Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI No Modlet Resource Validation Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI No Source File Processing Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Output Encoding Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Platform Provider Location Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Provider Location Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Source Directory Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI SourceFileProcessor Class Name Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Specification Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Template Encoding Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Template Location Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Template Profile Option</td>
     *     </tr>
     *     <tr class="TableRow">
     *       <td align="left" valign="top" nowrap>JOMC CLI Command Option {@code (org.apache.commons.cli.Option)} @ 1.2</td>
     *       <td align="left" valign="top" nowrap>JOMC CLI Transformer Location Option</td>
     *     </tr>
     *   </table>
     * </p>
     * @return The options of the command.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    @Override
    public org.apache.commons.cli.Options getOptions() {
        final org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
        options.addOption(this.getClasspathOption());
        options.addOption(this.getCountryOption());
        options.addOption(this.getDefaultTemplateProfileOption());
        options.addOption(this.getDocumentsOption());
        options.addOption(this.getImplementationOption());
        options.addOption(this.getIndentationStringOption());
        options.addOption(this.getInputEncodingOption());
        options.addOption(this.getLanguageOption());
        options.addOption(this.getLineSeparatorOption());
        options.addOption(this.getLocaleVariantOption());
        options.addOption(this.getModelContextFactoryOption());
        options.addOption(this.getModelOption());
        options.addOption(this.getModletLocationOption());
        options.addOption(this.getModletSchemaSystemIdOption());
        options.addOption(this.getModuleLocationOption());
        options.addOption(this.getModuleNameOption());
        options.addOption(this.getNoClasspathResolutionOption());
        options.addOption(this.getNoModelProcessingOption());
        options.addOption(this.getNoModelResourceValidation());
        options.addOption(this.getNoModletResourceValidation());
        options.addOption(this.getNoSourceProcessingOption());
        options.addOption(this.getOutputEncodingOption());
        options.addOption(this.getPlatformProviderLocationOption());
        options.addOption(this.getProviderLocationOption());
        options.addOption(this.getSourceDirectoryOption());
        options.addOption(this.getSourceFileProcessorOption());
        options.addOption(this.getSpecificationOption());
        options.addOption(this.getTemplateEncodingOption());
        options.addOption(this.getTemplateLocationOption());
        options.addOption(this.getTemplateProfileOption());
        options.addOption(this.getTransformerLocationOption());
        return options;
    }
}
