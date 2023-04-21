package ucalgary.ebe.ci.AgilePlanner;

import ucalgary.ebe.ci.gestures.DefaultGesturesAdapter;
import ucalgary.ebe.ci.gestures.ICIGestureIndentifier;
import ucalgary.ebe.ci.gestures.events.ICIGestureEvent;

/**
 * @author hkolenda
 * 
 */
public abstract class AgilePlannerGesturesAdapter extends DefaultGesturesAdapter {

    public void gestureReleased(ICIGestureEvent e) {
        super.gestureReleased(e);
        ICIGestureIndentifier gesture = e.getGesture();
        if (gesture.equals(AgilePlannerGestures.STORYCARD_FLIP)) {
            StoryCard_Flip(e);
            return;
        }
        if (gesture.equals(AgilePlannerGestures.BACKLOG_ORGANIZE_ALL)) {
            Backlog_Organize_All(e);
            return;
        }
        if (gesture.equals(AgilePlannerGestures.ITERATION_CREATE_NEW)) {
            Iteration_Create_New(e);
            return;
        }
        if (gesture.equals(AgilePlannerGestures.ITERATION_ORGANIZE_ALL)) {
            Iteration_Organize_All(e);
            return;
        }
        if (gesture.equals(AgilePlannerGestures.STORYCARD_COLLAPSE_ALL)) {
            StoryCard_Collapse_All(e);
            return;
        }
        if (gesture.equals(AgilePlannerGestures.STORYCARD_CREATE_NEW)) {
            StoryCard_Create_New(e);
            return;
        }
        if (gesture.equals(AgilePlannerGestures.STORYCARD_EXPAND_ALL)) {
            StoryCard_Expand_All(e);
            return;
        }
        if (gesture.equals(AgilePlannerGestures.STORYCARD_ISROTATING)) {
            StoryCard_IsRotating(e);
            return;
        }
        if (gesture.equals(AgilePlannerGestures.STORYCARD_ORGANIZE_ALL)) {
            StoryCard_Organize_All(e);
            return;
        }
        if (gesture.equals(AgilePlannerGestures.STORYCARD_ROTATION_DONE)) {
            StoryCard_Rotation_Done(e);
            return;
        }
        if (gesture.equals(AgilePlannerGestures.STORYCARD_SET_COMPLETED)) {
            StoryCard_Set_Completed(e);
            return;
        }
        if (gesture.equals(AgilePlannerGestures.STORYCARD_BESTCASE_SETTING)) {
            StoryCard_BestCase_Setting(e);
            return;
        }
        if (gesture.equals(AgilePlannerGestures.STORYCARD_BESTCASE_SET)) {
            StoryCard_BestCase_Set(e);
            return;
        }
    }

    public void StoryCard_Flip(ICIGestureEvent e) {
    }

    public void Backlog_Organize_All(ICIGestureEvent e) {
    }

    public void Iteration_Create_New(ICIGestureEvent e) {
    }

    public void Iteration_Organize_All(ICIGestureEvent e) {
    }

    public void StoryCard_Collapse_All(ICIGestureEvent e) {
    }

    public void StoryCard_Create_New(ICIGestureEvent e) {
    }

    public void StoryCard_Expand_All(ICIGestureEvent e) {
    }

    public void StoryCard_Organize_All(ICIGestureEvent e) {
    }

    public void StoryCard_Set_Completed(ICIGestureEvent e) {
    }

    public void StoryCard_Rotation_Done(ICIGestureEvent e) {
    }

    public void StoryCard_IsRotating(ICIGestureEvent e) {
    }

    public void StoryCard_BestCase_Setting(ICIGestureEvent e) {
    }

    public void StoryCard_BestCase_Set(ICIGestureEvent e) {
    }
}
