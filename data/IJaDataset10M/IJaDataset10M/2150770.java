package br.ufmg.ubicomp.droidguide.application;

import java.util.ArrayList;
import java.util.List;
import br.ufmg.ubicomp.droidguide.utils.AndroidUtils;
import android.app.ListActivity;
import android.graphics.Color;
import android.view.View;

public abstract class DroidGuideScreenListActivity extends ListActivity {

    private List<Integer> itemColors = new ArrayList<Integer>();

    private List<String> itemKeys = new ArrayList<String>();

    private List<String> itemNames = new ArrayList<String>();

    private List<Boolean> itemsSubscribed = new ArrayList<Boolean>();

    private static final int COLOR_OFF = Color.BLACK;

    private static final int COLOR_ON = Color.RED;

    private void setBackgroundColorOfChild(long id, int color) {
        View child = getListView().getChildAt((int) id);
        child.setBackgroundColor(color);
        itemColors.set((int) id, color);
    }

    protected void handleMarkedItem(long id) {
        String item = itemNames.get((int) id);
        System.out.println(getListView().getChildCount());
        if (itemColors.get((int) id) == COLOR_ON) {
            setBackgroundColorOfChild(id, COLOR_OFF);
            onItemSelected(item);
        } else {
            setBackgroundColorOfChild(id, COLOR_ON);
            onItemUnselected(item);
        }
    }

    protected void showAlert(String msg) {
        AndroidUtils.showAlert(this, msg);
    }

    protected void notifyError(Exception e, String op) {
        AndroidUtils.notifyError(e, this, op);
    }

    protected abstract void onItemSelected(String item);

    protected abstract void onItemUnselected(String item);
}
