package cn.add.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import cn.add.R;
import cn.add.cache.CacheManage;
import cn.add.model.Battery;
import cn.add.util.DrawUtil;

public class BatteryImage extends Component {

    private static final String TAG = "defoe";

    private Battery mBattery;

    private final int[] images = new int[] { R.drawable.stat_sys_battery_10, R.drawable.stat_sys_battery_20, R.drawable.stat_sys_battery_40, R.drawable.battery_60, R.drawable.stat_sys_battery_80, R.drawable.stat_sys_battery_100 };

    private int mCurrentIndex = -1;

    private static final int Five = 5;

    private static final int Four = 4;

    private static final int Three = 3;

    private static final int Two = 2;

    private static final int One = 1;

    private static final int Zero = 0;

    public BatteryImage(Context ctx, Battery battery) {
        super(ctx);
        mBattery = battery;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawImage(canvas);
    }

    private void drawImage(Canvas mCanvas) {
        Bitmap mBitmap;
        if (mBattery != null) {
            int position = levelToPosition(mBattery.getMLevel());
            if (mCurrentIndex != position) {
                mCurrentIndex = position;
                mBitmap = CacheManage.getBitmap(images[position], images[position]);
            } else {
                mBitmap = CacheManage.getBitmap(images[mCurrentIndex], images[mCurrentIndex]);
                if (mBitmap == null) {
                }
            }
            DrawUtil.drawBitmap(mCanvas, mBitmap, 140, 380, null);
        }
    }

    private int levelToPosition(int level) {
        int position = -1;
        if (level > 80) {
            position = Five;
        } else if (level > 60) {
            position = Four;
        } else if (level > 40) {
            position = Three;
        } else if (level > 20) {
            position = Two;
        } else if (level > 10) {
            position = One;
        } else {
            position = Zero;
        }
        return position;
    }
}
