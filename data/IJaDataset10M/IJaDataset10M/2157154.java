package imi.character.statemachine.corestates;

import imi.character.statemachine.StateInfo;

/**
 * Contains all of the configuration info to set up
 * the ActionState to perform a fully body animation and possibly
 * a facial animation as well.
 * @author Lou Hayt
 */
public class ActionInfo extends StateInfo {

    /** true to keep repeating the body animation */
    private boolean bRepeat = false;

    /** true to have an oscilating repeat, false to have it loop */
    private boolean bRepeatWillOscilate = false;

    /** Constructor with body animation only **/
    public ActionInfo(String bodyAnimationName) {
        super(bodyAnimationName);
    }

    /** Constructor with body animation and facial animation **/
    public ActionInfo(String bodyAnimationName, String facialAnimationName, float facialAnimationTransitionTime, float facialAnimationHoldtime) {
        super(bodyAnimationName, facialAnimationName, facialAnimationTransitionTime, facialAnimationHoldtime);
    }

    /** Apply this configuration on that state **/
    public void apply(ActionState state) {
        super.apply(state);
        state.setRepeat(bRepeat);
        state.setRepeatWillOscilate(bRepeatWillOscilate);
        if (state instanceof CycleActionState) ((CycleActionState) state).setSimpleAction(true);
    }

    /** true to keep repeating the body animation */
    public boolean isRepeat() {
        return bRepeat;
    }

    /** true to keep repeating the body animation */
    public void setRepeat(boolean bRepeat) {
        this.bRepeat = bRepeat;
    }

    /** true to have an oscilating repeat, false to have it loop */
    public boolean isRepeatWillOscilate() {
        return bRepeatWillOscilate;
    }

    /** true to have an oscilating repeat, false to have it loop */
    public void setRepeatWillOscilate(boolean bRepeatWillOscilate) {
        this.bRepeatWillOscilate = bRepeatWillOscilate;
    }
}
