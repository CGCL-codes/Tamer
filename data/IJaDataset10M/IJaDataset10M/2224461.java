package snooker.scoreboard.activity;

import java.util.List;
import snooker.scoreboard.Match;
import snooker.scoreboard.StatisticRecord;
import snooker.scoreboard.Statistics;
import snooker.scoreboard.event.FinishActivityTouchListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class FrameStatisticsActivity extends Activity {

    private static int direction = FinishActivityTouchListener.DIRECTION_TO_RIGHT;

    private static int frameId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.statistics);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            frameId = getIntent().getExtras().getInt("frameid");
            boolean changeDirection = extras.getBoolean("changedirection");
            if (changeDirection) {
                direction = FinishActivityTouchListener.DIRECTION_TO_UP;
            }
        }
        findViewById(R.id.statisticsScroll).setOnTouchListener(new FinishActivityTouchListener(this, direction));
        TableLayout tl = (TableLayout) findViewById(R.id.statisticsTable);
        List<StatisticRecord> lsr = Statistics.getFrameStatistics(Match.getCurrentMatch(), frameId);
        for (StatisticRecord sr : lsr) {
            TableRow newRow = new TableRow(this);
            TextView nameText = new TextView(this);
            TextView value1Text = new TextView(this);
            TextView value2Text = new TextView(this);
            nameText.setText(sr.getName());
            value1Text.setText(sr.getValue1());
            value2Text.setText(sr.getValue2());
            newRow.addView(nameText);
            newRow.addView(value1Text);
            newRow.addView(value2Text);
            tl.addView(newRow);
        }
    }
}
