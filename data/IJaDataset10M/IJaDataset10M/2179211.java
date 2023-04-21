package org.starobjects.jpa.tools;

import java.io.IOException;
import java.sql.SQLException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.nakedobjects.metamodel.config.ConfigurationBuilderFileSystem;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.starobjects.jpa.tools.internal.util.CmdLineUtil;

public class SchemaManager extends AbstractJpaTool {

    private static Logger LOG = Logger.getLogger(SchemaManager.class);

    private static class Parameters {

        public String configDir;

        public boolean export;

        public boolean create;

        public boolean drop;

        public Parameters(String configDir, boolean export, boolean create, boolean drop) {
            this.configDir = configDir;
            this.export = export;
            this.create = create;
            this.drop = drop;
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        SchemaManager schemaManager = new SchemaManager();
        Parameters parameters = schemaManager.parseAndSetupLogging(args);
        if (parameters == null) {
            return;
        }
        schemaManager.execute(parameters);
    }

    private Parameters parseAndSetupLogging(String[] args) throws IOException {
        Options options = new Options();
        CmdLineUtil.addOption(options, "g", "config", true, "Config directory (containing nakedobjects.properties & logging.properties)");
        CmdLineUtil.addOption(options, "c", "create", false, "Create only");
        CmdLineUtil.addOption(options, "d", "drop", false, "Drop only");
        CmdLineUtil.addOption(options, "x", "export", false, "Export to database");
        String programName = getClass().getSimpleName();
        CommandLine cmdLine = CmdLineUtil.parse(programName, options, args);
        if (cmdLine == null) {
            return null;
        }
        String configDirName = getConfigDirAndConfigureLogging(cmdLine, options);
        if (configDirName == null) {
            return null;
        }
        boolean create = cmdLine.hasOption("c");
        boolean drop = cmdLine.hasOption("d");
        boolean export = cmdLine.hasOption("x");
        LOG.info("config dir (-g): " + configDirName);
        LOG.info("export (-x): " + yesOrNo(export));
        LOG.info("create (-c): " + yesOrNo(create));
        LOG.info("drop   (-d): " + yesOrNo(drop));
        return new Parameters(configDirName, export, create, drop);
    }

    public void execute(Parameters parameters) {
        ConfigurationBuilderFileSystem configurationLoader = new ConfigurationBuilderFileSystem(parameters.configDir);
        bootstrapNakedObjects(configurationLoader);
        SpecificationLoader specificationLoader = NakedObjectsContext.getSpecificationLoader();
        AnnotationConfiguration hibConfiguration = createHibernateMetaModel(specificationLoader);
        SchemaExport schemaExport = new SchemaExport(hibConfiguration);
        schemaExport.execute(true, parameters.export, parameters.drop, parameters.create);
    }
}
