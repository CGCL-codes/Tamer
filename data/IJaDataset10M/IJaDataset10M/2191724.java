package de.tuberlin.julia.SFD_Numbers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import de.tlabs.ahmad.trigger_server.TriggerServer;
import experiment.ApplyDesign;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MultiBtnPressedListener implements OnClickListener {

    private static final Logger logger = LoggerFactory.getLogger();

    private ApplyDesign applyDesign = null;

    NumberView numberView;

    Thread feedbackThread;

    MediaPlayer mp;

    TimeZone tz = TimeZone.getTimeZone("Europe/Berlin");

    Locale locale = Locale.GERMANY;

    final Calendar c = Calendar.getInstance(tz, locale);

    SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss.SS");

    public MultiBtnPressedListener(NumberView numberView, ApplyDesign applyDesign, MediaPlayer mp) {
        this.numberView = numberView;
        this.applyDesign = applyDesign;
        this.mp = mp;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            TextView bt = (TextView) v;
            Object tag = bt.getTag();
            String digit = bt.getText().toString();
            if (!"C".equals(tag) && !"enter".equals(tag)) {
                logger.info("clicked");
                feedbackThread = new Thread(new HapticFeedbackProvider(bt, applyDesign.getFeedbackDelay()));
                feedbackThread.start();
                applyDesign.enterDigit(digit);
                applyDesign.compareDigits(feedbackThread);
            }
        }
    }

    private class HapticFeedbackProvider implements Runnable {

        private final TextView bt;

        private final int haptDelay;

        private Handler handler = new Handler();

        public HapticFeedbackProvider(TextView bt, int haptDelay) {
            this.bt = bt;
            this.haptDelay = haptDelay;
        }

        @Override
        public void run() {
            try {
                if (haptDelay != 0) Thread.sleep(haptDelay);
                TriggerServer.getOutputStream().print(24);
                TriggerServer.getOutputStream().flush();
                f.setTimeZone(c.getTimeZone());
                Date date = c.getTime();
                logger.info("multimodal feedback " + f.format(date));
                if (mp != null) {
                    mp.start();
                }
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        GradientDrawable pressedDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[] { Color.BLACK, Color.LTGRAY });
                        pressedDrawable.setCornerRadius(10);
                        bt.setBackgroundDrawable(pressedDrawable);
                    }
                });
                Object systemService = bt.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                if (systemService instanceof Vibrator) {
                    Vibrator vibrator = (Vibrator) systemService;
                    vibrator.vibrate(new long[] { 0, 50 }, -1);
                }
                Thread.sleep(100);
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        GradientDrawable origBackground = new GradientDrawable(Orientation.TOP_BOTTOM, new int[] { Color.LTGRAY, Color.DKGRAY });
                        origBackground.setCornerRadius(10);
                        bt.setBackgroundDrawable(origBackground);
                    }
                });
            } catch (InterruptedException e) {
                logger.error(e.toString());
            } catch (IllegalStateException e) {
                logger.error(e.toString());
                e.printStackTrace();
            }
        }
    }
}
