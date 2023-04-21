package es.eucm.eadventure.engine.core.control.functionaldata.functionalactions;

import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.Description;
import es.eucm.eadventure.engine.core.control.ActionManager;
import es.eucm.eadventure.engine.core.control.DebugLog;
import es.eucm.eadventure.engine.core.control.Game;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalDescriptions;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalElement;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalPlayer;

/**
 * The action to examine an object
 * 
 * @author Eugenio Marchiori
 * 
 */
public class FunctionalExamine extends FunctionalAction {

    /**
     * The element to be examined
     */
    FunctionalElement element;

    /**
     * Default constructor, using the original action and the element to
     * examine.
     * 
     * @param action
     * @param element
     */
    public FunctionalExamine(Action action, FunctionalElement element) {
        super(action);
        type = ActionManager.ACTION_EXAMINE;
        this.element = element;
        originalAction = element.getFirstValidAction(Action.EXAMINE);
        if (element.isInInventory() || originalAction == null) {
            this.needsGoTo = false;
        } else {
            this.needsGoTo = originalAction.isNeedsGoTo();
            this.keepDistance = originalAction.getKeepDistance();
        }
    }

    @Override
    public void drawAditionalElements() {
    }

    @Override
    public void setAnotherElement(FunctionalElement element) {
    }

    @Override
    public void start(FunctionalPlayer functionalPlayer) {
        Description description = new FunctionalDescriptions(element.getElement().getDescriptions()).getDescription();
        this.functionalPlayer = functionalPlayer;
        if (!element.examine()) {
            if (functionalPlayer.isAlwaysSynthesizer()) functionalPlayer.speakWithFreeTTS(description.getDetailedDescription(), functionalPlayer.getPlayerVoice()); else if (description.getDetailedDescriptionSoundPath() != null && !description.getDetailedDescriptionSoundPath().equals("")) functionalPlayer.speak(description.getDetailedDescription(), description.getDetailedDescriptionSoundPath()); else functionalPlayer.speak(description.getDetailedDescription(), Game.getInstance().getGameDescriptor().isKeepShowing());
        }
        finished = true;
        DebugLog.player("Started Examine: " + element.getElement().getId());
    }

    @Override
    public void stop() {
        finished = true;
    }

    @Override
    public void update(long elapsedTime) {
    }
}
