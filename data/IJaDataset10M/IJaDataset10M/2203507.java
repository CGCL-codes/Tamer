package opt.base.specific;

import opt.base.*;

public class OptionsStaging extends OptionGroup {

    /**
	 * 
	 */
    public OptionsStaging() {
        super("StagingArea", "Staging Area");
        initialize();
    }

    private void initialize() {
        OptionGroup stageOpts = new OptionGroup("StageOptions", "Staging Area Options");
        stageOpts.addOption(new SingleBoolOption("STGIsPrefixed", "Is Prefixed", "On=Apply Table name as a prefix,Off=Apply Table Name as a suffix", true));
        stageOpts.addOption(new SingleBoolOption("STGIncludeSchema", "Include Schema Name in Stage Name", "On=Use Schema name in Stage Table Name", false));
        stageOpts.addOption(new SingleStringOption("STGTableName", "Table Prefix/Suffix", "STG", "STG"));
        stageOpts.addOption(new SingleBoolOption("STGFieldTableNameIsMuted", "Table Prefix/Suffix is Muted", "On=Remove the table prefix/suffix from common fields.", false));
        stageOpts.addOption(new SingleBoolOption("STGGenSequenceObjects", "Generate Sequence Objects", "On = yes, Off = no (ORACLE ONLY)", true));
        addChildGroup(stageOpts);
        super.addCommonFieldOptions("StagingFields", "Staging Fields", "STG");
        super.addCommonIDXOptions("S", "StagingIndexes", "Staging Indexes", "STG");
    }
}
