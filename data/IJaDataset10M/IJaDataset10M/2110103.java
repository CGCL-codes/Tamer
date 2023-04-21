package snooker.scoreboard.test;

import snooker.scoreboard.Match;
import snooker.scoreboard.activity.R;
import snooker.scoreboard.activity.SnookerScoreboardActivity;
import snooker.scoreboard.frame.Frame;
import snooker.scoreboard.persistence.SnookerSharedPreferencesFactory;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class SnookerScoreboardActivityInstTest extends ActivityInstrumentationTestCase2<SnookerScoreboardActivity> {

    private ImageView undoButton;

    private ImageView redButton;

    private ImageView yellowButton;

    private ImageView greenButton;

    private ImageView brownButton;

    private ImageView blueButton;

    private ImageView pinkButton;

    private ImageView blackButton;

    private ImageView whiteButton;

    private View p1Text;

    private View p2Text;

    private View faultCheckBox;

    private SnookerScoreboardActivity activity;

    private Instrumentation inst;

    public SnookerScoreboardActivityInstTest() {
        super("snooker.scoreboard.activity", SnookerScoreboardActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SnookerSharedPreferencesFactory.create(getActivity().getSharedPreferences("SnookerScoreboard", 0));
        Match.getCurrentMatch().newFrame(false, 15);
        final SnookerScoreboardActivity a = getActivity();
        undoButton = (ImageView) a.findViewById(R.id.undoButton);
        redButton = (ImageView) a.findViewById(R.id.redButton);
        blackButton = (ImageView) a.findViewById(R.id.blackButton);
        yellowButton = (ImageView) a.findViewById(R.id.yellowButton);
        greenButton = (ImageView) a.findViewById(R.id.greenButton);
        brownButton = (ImageView) a.findViewById(R.id.brownButton);
        blueButton = (ImageView) a.findViewById(R.id.blueButton);
        pinkButton = (ImageView) a.findViewById(R.id.pinkButton);
        blackButton = (ImageView) a.findViewById(R.id.blackButton);
        whiteButton = (ImageView) a.findViewById(R.id.whiteButton);
        faultCheckBox = (View) a.findViewById(R.id.foulButton);
        p1Text = (View) a.findViewById(R.id.player1Name);
        p2Text = (View) a.findViewById(R.id.player2Name);
        activity = getActivity();
        inst = getInstrumentation();
    }

    public void testUndo() {
        final int MAX_REDS = 15;
        Match.getCurrentMatch().newFrame(false, MAX_REDS);
        for (int i = 0; i < 3; i++) {
            clickButton(redButton);
            clickButton(blackButton);
            Log.i("TEST", Match.getCurrentMatch().getActFrame().getReds() + "");
            assertTrue(Match.getCurrentMatch().getActFrame().getReds() == MAX_REDS - i - 1);
        }
        for (int i = 0; i < 3; i++) {
            clickButton(undoButton);
            clickButton(undoButton);
            Log.i("TEST", Match.getCurrentMatch().getActFrame().getReds() + " reds");
            assertTrue(Match.getCurrentMatch().getActFrame().getReds() == MAX_REDS - 2 + i);
        }
    }

    private Frame actFrame;

    public void testScores() {
        final int MAX_REDS = 6;
        Match match = Match.getCurrentMatch();
        match.newFrame(false, MAX_REDS);
        actFrame = match.getActFrame();
        clickButton(redButton);
        asserts(5, 1, 0);
        clickButton(yellowButton);
        asserts(5, 3, 0);
        clickButton(redButton);
        asserts(4, 4, 0);
        clickButton(faultCheckBox);
        clickButton(greenButton);
        asserts(4, 4, 4);
        clickButton(redButton);
        clickButton(brownButton);
        clickButton(redButton);
        clickButton(blueButton);
        clickButton(redButton);
        clickButton(pinkButton);
        clickButton(redButton);
        clickButton(greenButton);
        asserts(0, 4, 26);
        clickButton(undoButton);
        clickButton(whiteButton);
        asserts(0, 8, 23);
        clickButton(yellowButton);
        clickButton(greenButton);
        clickButton(brownButton);
        clickButton(pinkButton);
        asserts(0, 17, 29);
        clickButton(p1Text);
        asserts(0, 17, 29);
        clickButton(blueButton);
        clickButton(pinkButton);
        clickButton(blackButton);
        asserts(0, 35, 29);
    }

    private void asserts(int actReds, int p1Score, int p2Score) {
        String msg = "Reds expected " + actReds + ", got " + actFrame.getReds();
        assertTrue(msg, actFrame.getReds() == actReds);
        msg = "P1 score expected " + p1Score + ", got " + actFrame.getPlayer1Score();
        assertTrue(msg, actFrame.getPlayer1Score() == p1Score);
        msg = "P2 score expected " + p2Score + ", got " + actFrame.getPlayer2Score();
        assertTrue(msg, actFrame.getPlayer2Score() == p2Score);
    }

    private void clickButton(View button) {
        activity.runOnUiThread(new ButtonClickRunnable(button));
        inst.waitForIdleSync();
    }

    class ButtonClickRunnable implements Runnable {

        View button;

        public ButtonClickRunnable(View button) {
            this.button = button;
        }

        public void run() {
            button.performClick();
        }
    }
}
