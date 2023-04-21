package de.mpg.mpiz.koeln.anna.step.conrad.train.local;

import java.util.List;
import de.kerner.commons.CommandStringBuilder;
import de.mpg.mpiz.koeln.anna.step.conrad.common.ConradConstants;
import de.mpg.mpiz.koeln.anna.step.conrad.train.common.AbstractTrain;

public class TrainLocal extends AbstractTrain {

    @Override
    public List<String> getCmdList() {
        return new CommandStringBuilder(ConradConstants.getConradCmdString()).addFlagCommand("train").addFlagCommand("models/singleSpecies.xml").addFlagCommand(workingDir.getAbsolutePath()).addFlagCommand(TRAINING_FILE.getAbsolutePath()).getCommandList();
    }
}
