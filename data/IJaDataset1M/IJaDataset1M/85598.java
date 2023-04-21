package soccer.client.action;

/**
 *
 */
public class TurnOnSoundAction extends AbstractToggleAction {

    public TurnOnSoundAction() {
        super();
        putValue(NAME, "Sound On");
        setEnabled(true);
        setToggledOn(true);
    }

    /**
	 * The toggle was changed
	 */
    protected void toggleStateChanged() {
        getSoccerMaster().getSoundSystem().setSoundOn(isToggledOn());
    }
}
