package csel.controller.commands;

import csel.model.GameCommand;
import csel.model.Model;
import csel.model.skills.NonRangedActiveSkill;
import csel.view.GameViewport;

public class BMUseOnSelfSkillCommand implements GameCommand {

    private Model model;

    private GameViewport gameViewport;

    public BMUseOnSelfSkillCommand(Model model, GameViewport gameViewport) {
        this.model = model;
        this.gameViewport = gameViewport;
    }

    public void execute() {
        model.getPlayerCharacter().useSkillOnSelf((NonRangedActiveSkill) gameViewport.getSelectedSkill());
    }
}
