package org.lhsi.lifepoisoncounter.widgets;

import org.lhsi.lifepoisoncounter.R;
import org.lhsi.lifepoisoncounter.LifeModel;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LifeLayout extends LinearLayout {

    private static final int MIN_LIFE = -999;

    private static final int MAX_LIFE = 999;

    private static final int START_LIFE = 20;

    private static final int SPINNER_SPEED = 300;

    private static final int COMMIT_DELAY_MS = 2000;

    private LifeView lifeHistory;

    private NumberPicker spinner;

    private Handler guiThread;

    private LifeModel life;

    private TextView nameView;

    public LifeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.life_layout, this, true);
        guiThread = new Handler();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.lifeLayout, 0, 0);
        nameView = (TextView) findViewById(R.id.player_name);
        int visibility = array.getBoolean(R.styleable.lifeLayout_nameShown, true) ? VISIBLE : GONE;
        nameView.setVisibility(visibility);
        lifeHistory = (LifeView) findViewById(R.id.life_widget);
        spinner = (NumberPicker) findViewById(R.id.spinner_widget);
        spinner.setRange(MIN_LIFE, MAX_LIFE);
        spinner.setSpeed(SPINNER_SPEED);
        spinner.setOnChangeListener(new LifeChangeListener());
        newGame();
    }

    public void newGame() {
        life = new LifeModel();
        lifeHistory.setModel(life);
        spinner.setCurrent(START_LIFE);
        lifeHistory.onHistoryChanged();
    }

    public void commitPendingChanges() {
        guiThread.removeCallbacks(commitLifeScore);
        life.commitValue();
    }

    public void setTheme(String theme) {
        lifeHistory.setTheme(theme);
    }

    public void setName(String name) {
        nameView.setText(name);
    }

    public LifeModel getModelForSaving() {
        commitPendingChanges();
        return life;
    }

    public void setModelFromSave(LifeModel in) {
        commitPendingChanges();
        life = in;
        lifeHistory.setModel(life);
        spinner.setCurrent(life.getValue());
        lifeHistory.onHistoryChanged();
    }

    private class LifeChangeListener implements NumberPicker.OnChangedListener {

        public void onChanged(NumberPicker spinner, int oldVal, int newVal) {
            if (oldVal == MIN_LIFE && newVal == MAX_LIFE) {
                spinner.setCurrent(MIN_LIFE);
            } else if (oldVal == MAX_LIFE && newVal == MIN_LIFE) {
                spinner.setCurrent(MAX_LIFE);
            } else {
                handleNewValue(spinner.getId(), newVal);
            }
        }
    }

    private void handleNewValue(int id, int newVal) {
        life.setValue(newVal);
        lifeHistory.onHistoryChanged();
        guiThread.removeCallbacks(commitLifeScore);
        guiThread.postDelayed(commitLifeScore, COMMIT_DELAY_MS);
    }

    private Runnable commitLifeScore = new Runnable() {

        public void run() {
            life.commitValue();
        }
    };
}
