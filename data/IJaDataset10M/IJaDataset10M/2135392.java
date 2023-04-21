package snooker.scoreboard.ball;

import snooker.scoreboard.activity.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class RedBall {

    private Bitmap originalBitmap;

    public RedBall(Resources r) {
        originalBitmap = BitmapFactory.decodeResource(r, R.drawable.red_ball);
    }

    public Bitmap getBitmap(int reds) {
        Bitmap myBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        if (reds > 0) {
            Canvas canvas = new Canvas(myBitmap);
            Paint paint = new Paint(Color.BLACK);
            canvas.drawBitmap(originalBitmap, 0, 0, paint);
            int left = 8;
            if (reds < 10) {
                left = 4;
            }
            canvas.drawText(Integer.toString(reds), originalBitmap.getWidth() / 2 - left, originalBitmap.getHeight() / 2 + 3, paint);
        }
        return myBitmap;
    }
}
