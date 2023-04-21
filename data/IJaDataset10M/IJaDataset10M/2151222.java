package eu.fbk.hlt.edits.rules.extract;

import java.util.ArrayList;
import java.util.List;
import eu.fbk.hlt.common.CommandHandler;
import eu.fbk.hlt.common.ConfigurationLoader;
import eu.fbk.hlt.common.EDITSException;
import eu.fbk.hlt.common.ModuleLoader;
import eu.fbk.hlt.common.conffile.Configuration;
import eu.fbk.hlt.common.conffile.Type;
import eu.fbk.hlt.common.module.Command;
import eu.fbk.hlt.common.module.ModuleInfo;
import eu.fbk.hlt.common.module.OptionInfo;
import eu.fbk.hlt.common.module.SubModule;
import eu.fbk.hlt.edits.EDITS;
import eu.fbk.hlt.edits.etaf.EntailmentPair;
import eu.fbk.hlt.edits.etaf.RelationRule;
import eu.fbk.hlt.edits.processor.FileInterlocutor;
import eu.fbk.hlt.edits.processor.Interlocutor;

/**
 * @author Milen Kouylekov
 */
public class RulesExtractorHandler extends CommandHandler {

    public static final String EXTRACT_COMMAND = "extract";

    public static final String SAVE_OPTION = "rules-output";

    @Override
    public void execute(String command) throws EDITSException {
        ConfigurationLoader loader = new ConfigurationLoader();
        Configuration conf = loader.readModule(EDITS.RULES_EXTRACTOR);
        RulesExtractor engine = (RulesExtractor) ModuleLoader.initialize(conf.getClassName());
        engine.configure(conf);
        List<Interlocutor<EntailmentPair, RelationRule>> processors = FileInterlocutor.makeRules(EDITS.system().script().input(), EDITS.system().script().option(SAVE_OPTION), overwrite(), useMemory());
        for (Interlocutor<EntailmentPair, RelationRule> iz : processors) engine.extract(iz);
    }

    @Override
    public ModuleInfo info() {
        ModuleInfo def = super.info();
        for (OptionInfo i : def.options()) {
            i.context().add(EXTRACT_COMMAND);
        }
        OptionInfo o = new OptionInfo();
        o.setName(SAVE_OPTION);
        o.setAbbreviation("ro");
        o.setType(Type.OUTPUT);
        o.setMultiple(false);
        o.setRequired(false);
        o.setId(false);
        o.setDescription(EDITS.system().description(SAVE_OPTION));
        o.context().add(EXTRACT_COMMAND);
        def.options().add(o);
        SubModule module = new SubModule();
        module.setType(EDITS.RULES_EXTRACTOR);
        module.setMultiple(false);
        module.setRequired(true);
        module.context().add(EXTRACT_COMMAND);
        def.subModules().add(module);
        return def;
    }

    @Override
    public List<Command> supportedCommands() {
        List<Command> ops = new ArrayList<Command>();
        Command o = null;
        o = new Command();
        o.setName(EXTRACT_COMMAND);
        o.setDescription(EDITS.system().description(EXTRACT_COMMAND));
        o.setAbbreviation("x");
        ops.add(o);
        return ops;
    }
}
