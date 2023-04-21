package hld.coins.status;

import hld.coins.interfaces.AbstractGameStatus;
import hld.coins.manager.GameViewManager;
import hld.coins.view.CourseView;
import android.app.Activity;

public class CourseStatus extends AbstractGameStatus {

    @Override
    public void EntryCurrentStatus(Activity activity) {
        new CourseView(activity);
    }

    @Override
    public void RemoveCurrentStatus(Activity activity) {
        GameViewManager.getInstance().removeAllView();
    }
}
