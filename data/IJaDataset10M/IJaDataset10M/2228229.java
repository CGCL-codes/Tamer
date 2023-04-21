package com.controltier.ctl.cli.ctlgen;

import com.controltier.ctl.cli.CtlGenMain;
import com.controltier.ctl.cli.ActionFactory;
import com.controltier.ctl.common.CmdHandler;
import com.controltier.ctl.common.CmdModule;
import org.apache.commons.cli.CommandLine;
import java.io.IOException;
import java.util.Properties;

/**
 * ModuleMaker performs the actions for the "module" component of ad-gen.
 *
 * @author Greg Schueler <a href="mailto:greg@controltier.com">greg@controltier.com</a>
 * @version $Revision: 1080 $
 */
public class ModuleMaker extends BaseMaker implements ActionFactory {

    /**
     * Default command type
     */
    private static final String DEFAULT_CMD_TYPE = CmdHandler.SHELL_TYPE;

    public ModuleMaker(final CtlGenMain main, final CommandLine cli) {
        super(main, cli);
    }

    /**
     * "add" action for modules.  Creates a new module with the given
     * name.
     */
    public void actionadd() throws IOException {
        final String actionName = "add";
        if (!cli.hasOption('m')) {
            main.help("Action '" + actionName + "': missing option --module");
            main.exit(2);
        }
        final String argName = cli.getOptionValue('m');
        if (existsModule(argName)) {
            main.error("Module already exists: " + argName);
            return;
        }
        final String[] commands = new String[] {};
        final CmdModule module = CmdModule.createCommandModule(argName, main.getModuleBasedir().getAbsolutePath(), main.getTemplateBasedir().getAbsolutePath(), getModuleLookup());
        module.appendCommandList(commands);
    }

    /**
     * "remove" action for modules.  Removes the named module completely.
     *
     * @throws IOException
     */
    public void actionremove() throws IOException {
        final String actionName = "remove";
        if (!cli.hasOption('m')) {
            main.help("Action '" + actionName + "': missing option --module");
            main.exit(2);
        }
        final String argName = cli.getOptionValue('m');
        if (!existsModule(argName)) {
            main.error("Module does not exist: " + argName);
            return;
        }
        CmdModule module = getModule(argName);
        CmdModule.deleteModule(module, main.getFramework());
    }

    /**
     * "generate" action for modules.  Generates all command handlers,
     * overwriting if the "--overwrite" option is present.
     *
     * @throws IOException
     */
    public void actiongenerate() throws IOException {
        final String actionName = "generate";
        if (!cli.hasOption('m')) {
            main.help("Action '" + actionName + "': missing option --module");
            main.exit(2);
        }
        final String argName = cli.getOptionValue('m');
        if (!existsModule(argName)) {
            main.error("Module does not exist: " + argName);
            return;
        }
        CmdModule module = getModule(argName);
        module.generateHandlers(isOverwrite(), this);
    }

    /**
     * "attrib" action for modules.  Sets the value of a property based on the
     * given attribute name.
     *
     * @throws IOException
     */
    public void actionattrib() throws IOException {
        final String actionName = "attrib";
        if (!cli.hasOption('m')) {
            main.help("Action '" + actionName + "': missing option --module");
            main.exit(2);
        }
        if (!cli.hasOption('k')) {
            main.help("Action '" + actionName + "': missing option --key");
            main.exit(2);
        }
        if (!cli.hasOption('v')) {
            main.help("Action '" + actionName + "': missing option --value");
            main.exit(2);
        }
        final String argName = cli.getOptionValue('m');
        if (!existsModule(argName)) {
            main.error("Module does not exist: " + argName);
            return;
        }
        String key = cli.getOptionValue('k');
        String value = cli.getOptionValue('v');
        CmdModule module = getModule(argName);
        module.setAttribute(key, value);
    }

    /**
     * "add-command" action for modules.  A new command is created using the name of the "--command" option value.  The
     * command type defaults to "ant", but can be specified with the "--command-type" option.
     *
     * @throws IOException
     */
    public void actionadd_command() throws IOException {
        final String actionName = "add-command";
        if (!cli.hasOption('m')) {
            main.help("Action '" + actionName + "': missing option --module");
            main.exit(2);
        }
        final String argName = cli.getOptionValue('m');
        if (!existsModule(argName)) {
            main.error("Module does not exist: " + argName);
            return;
        }
        CmdModule module = getModule(argName);
        if (!cli.hasOption('c')) {
            main.help("Action '" + actionName + "': missing option --command");
            main.exit(2);
        }
        String cmdname = cli.getOptionValue('c');
        String cmdtype = DEFAULT_CMD_TYPE;
        if (cli.hasOption('T')) {
            cmdtype = cli.getOptionValue('T');
        }
        if (module.existsCmdHandler(cmdname)) {
            main.help("Action '" + actionName + "': command named '" + cmdname + "' already exists");
            main.exit(2);
        }
        Properties props = new Properties();
        CmdHandler.createCmdHandler(module, cmdname, cmdtype, props, this);
        module.appendCommandList(new String[] { cmdname });
    }

    /**
     * "remove-field" action, which removes a named field from the CmdModule.
     *
     * @throws IOException
     */
    public void actionremove_field() throws IOException {
        final String actionName = "remove-field";
        if (!cli.hasOption('m')) {
            main.help("Action '" + actionName + "': missing option --module");
            main.exit(2);
        }
        final String argName = cli.getOptionValue('m');
        if (!existsModule(argName)) {
            main.error("Module does not exist: " + argName);
            return;
        }
        CmdModule module = getModule(argName);
        if (!cli.hasOption('f')) {
            main.help("Action '" + actionName + "': missing option --fieldname");
            main.exit(2);
        }
        String fieldname = cli.getOptionValue('f');
        try {
            module.removeField(fieldname);
            module.storeTypeProperties();
        } catch (IllegalArgumentException e) {
            main.exit(2, e.getMessage());
        }
    }

    /**
     * "add-setting" action, which adds a setting field to the type module. Requires the --datatype option.
     * The --value option is optional.
     *
     * @throws IOException
     */
    public void actionadd_setting() throws IOException {
        final String actionName = "add-setting";
        if (!cli.hasOption('m')) {
            main.help("Action '" + actionName + "': missing option --module");
            main.exit(2);
        }
        final String argName = cli.getOptionValue('m');
        if (!existsModule(argName)) {
            main.error("Module does not exist: " + argName);
            return;
        }
        CmdModule module = getModule(argName);
        if (!cli.hasOption('f')) {
            main.help("Action '" + actionName + "': missing option --fieldname");
            main.exit(2);
        }
        String setname = cli.getOptionValue('f');
        if (!cli.hasOption('y')) {
            main.help("Action '" + actionName + "': missing option --datatype");
            main.exit(2);
        }
        String settype = cli.getOptionValue('y');
        String setdef = null;
        if (cli.hasOption('v')) {
            setdef = cli.getOptionValue('v');
        }
        try {
            module.addSettingField(setname, settype, setdef);
            module.storeTypeProperties();
        } catch (IllegalArgumentException e) {
            main.exit(2, e.getMessage());
        }
    }

    /**
     * "add-deployment" action, which adds a deployment field to the type module.
     * Requires the --class option. The --defaultUri option
     * is optional.
     *
     * @throws IOException
     */
    public void actionadd_deployment() throws IOException {
        final String actionName = "add-deployment";
        if (!cli.hasOption('m')) {
            main.help("Action '" + actionName + "': missing option --module");
            main.exit(2);
        }
        final String argName = cli.getOptionValue('m');
        if (!existsModule(argName)) {
            main.error("Module does not exist: " + argName);
            return;
        }
        CmdModule module = getModule(argName);
        if (!cli.hasOption('f')) {
            main.help("Action '" + actionName + "': missing option --fieldname");
            main.exit(2);
        }
        String fieldname = cli.getOptionValue('f');
        if (!cli.hasOption('l')) {
            main.help("Action '" + actionName + "': missing option --class");
            main.exit(2);
        }
        String depclass = cli.getOptionValue('l');
        String defuri = null;
        if (cli.hasOption('u')) {
            defuri = cli.getOptionValue('u');
        }
        try {
            module.addDeploymentField(fieldname, depclass, defuri);
            module.storeTypeProperties();
        } catch (IllegalArgumentException e) {
            main.exit(2, e.getMessage());
        }
    }

    /**
     * "add-package" action, which adds a package field to the type module. Requires the --class option. The
     * --defaultUri option is optional.
     *
     * @throws IOException
     */
    public void actionadd_package() throws IOException {
        final String actionName = "add-package";
        if (!cli.hasOption('m')) {
            main.help("Action '" + actionName + "': missing option --module");
            main.exit(2);
        }
        final String argName = cli.getOptionValue('m');
        if (!existsModule(argName)) {
            main.error("Module does not exist: " + argName);
            return;
        }
        CmdModule module = getModule(argName);
        if (!cli.hasOption('f')) {
            main.help("Action '" + actionName + "': missing option --fieldname");
            main.exit(2);
        }
        String fieldname = cli.getOptionValue('f');
        if (!cli.hasOption('l')) {
            main.help("Action '" + actionName + "': missing option --class");
            main.exit(2);
        }
        String depclass = cli.getOptionValue('l');
        String defuri = null;
        if (cli.hasOption('u')) {
            defuri = cli.getOptionValue('u');
        }
        try {
            module.addPackageField(fieldname, depclass, defuri);
            module.storeTypeProperties();
        } catch (IllegalArgumentException e) {
            main.exit(2, e.getMessage());
        }
    }

    /**
     * "add-document" action, which adds a document field to the type module.  (documents are formerly known as
     * "transforms".)  Requires the "--path" option and the "--template" option.
     *
     * @throws IOException
     */
    public void actionadd_document() throws IOException {
        final String actionName = "add-document";
        if (!cli.hasOption('m')) {
            main.help("Action '" + actionName + "': missing option --module");
            main.exit(2);
        }
        final String argName = cli.getOptionValue('m');
        if (!existsModule(argName)) {
            main.error("Module does not exist: " + argName);
            return;
        }
        CmdModule module = getModule(argName);
        if (!cli.hasOption('f')) {
            main.help("Action '" + actionName + "': missing option --fieldname");
            main.exit(2);
        }
        String fieldname = cli.getOptionValue('f');
        if (!cli.hasOption('P')) {
            main.help("Action '" + actionName + "': missing option --path");
            main.exit(2);
        }
        String docpath = cli.getOptionValue('P');
        if (!cli.hasOption('e')) {
            main.help("Action '" + actionName + "': missing option --template");
            main.exit(2);
        }
        String templ = cli.getOptionValue('e');
        try {
            module.addDocumentField(fieldname, docpath, templ);
            module.storeTypeProperties();
        } catch (IllegalArgumentException e) {
            main.exit(2, e.getMessage());
        }
    }
}
