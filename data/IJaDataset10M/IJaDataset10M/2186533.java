package com.cosina.game.fly.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class InfoBar extends View {

    public InfoBar(Context context) {
        super(context);
        setBackgroundColor(Color.argb(255, 206, 255, 255));
        bestScore = context.getSharedPreferences(PS_KEY, Context.MODE_PRIVATE).getLong(PS_KEY_NODE_BEST, 0);
    }

    private static Paint paintForText = new Paint();

    {
    }

    private long startTime = 0;

    private long currentScore = 0;

    private long bestScore = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText(LABEL_SCORE, 5, 20, paintForText);
        canvas.drawText(currentScore + "", paintForText.measureText("score") + 10, 20, paintForText);
        canvas.drawText(LABEL_BEST, 150, 20, paintForText);
        canvas.drawText(bestScore + "", paintForText.measureText("Best") + 155, 20, paintForText);
    }

    public void start() {
        startTime = System.currentTimeMillis();
        this.postInvalidate();
        this.postDelayed(updateScore, 10);
    }

    private void calculateScore() {
        currentScore = (System.currentTimeMillis() - startTime) / 20;
    }

    private final Runnable updateScore = new Runnable() {

        @Override
        public void run() {
            currentScore = (System.currentTimeMillis() - startTime) / 20;
            InfoBar.this.postInvalidate();
            InfoBar.this.postDelayed(this, 10);
        }
    };

    private static final String PS_KEY = "Fly-Info";

    private static final String PS_KEY_NODE_BEST = "Best";

    private static final String LABEL_SCORE = "Score";

    private static final String LABEL_BEST = "Best";

    public void end() {
        this.removeCallbacks(updateScore);
        calculateScore();
        InfoBar.this.postInvalidate();
        if (currentScore > bestScore) {
            this.getContext().getSharedPreferences(PS_KEY, Context.MODE_PRIVATE).edit().putLong(PS_KEY_NODE_BEST, currentScore).commit();
        }
    }
}
