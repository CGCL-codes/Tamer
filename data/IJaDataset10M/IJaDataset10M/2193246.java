package opt.base.specific;

import opt.base.*;
import javax.swing.JSlider;

public class OptionsConsolidation extends OptionGroup {

    /**
	 * 
	 */
    public OptionsConsolidation() {
        super("Consolidation", "Consolidation");
        initialize();
    }

    private void initialize() {
        OptionGroup optSet = new OptionGroup("ConsolidateOpts", "Consolidation Options");
        optSet.addOption(new SingleBoolOption("MergeCharVarchar", "Merge Character into Varchar", "on=Changes char into varchar in fwd engineering", true));
        optSet.addOption(new SingleBoolOption("UseMetaNames", "Use Meta Names in consolidation", "on=Meta names will be used to match fields", true));
        optSet.addOption(new SingleBoolOption("UseFullNames", "Use Full Names in consolidation", "on=Full names will be used to match fields", true));
        optSet.addOption(new SingleBoolOption("UsePrimaryDescription", "Use Primary Descr. in consolidation", "on=Description will be used to match fields", true));
        optSet.addOption(new SingleSliderOption("ConfidenceThreshold", "Confidence Threshhold Percent", 80, 0, 100, JSlider.HORIZONTAL, 2, 10));
        optSet.addOption(new SingleSliderOption("StrengthThreshold", "Strength Threshold Percent", 80, 0, 100, JSlider.HORIZONTAL, 2, 10));
        addChildGroup(optSet);
    }
}
