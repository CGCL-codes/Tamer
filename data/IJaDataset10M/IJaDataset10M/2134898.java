package jp.nextep.android.vtextviewer.component;

import jp.nextep.android.vtextviewer.util.MathDataUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

public class DisplayConfig {

    /** 欄外ヘッダー */
    public static final int ARTICLE_TOP_SPACE = 18;

    public static final int INT_FONT_SIZE_LARGE = 30;

    public static final int INT_FONT_SIZE_MEDIUM = 24;

    public static final int INT_FONT_SIZE_SMALL = 18;

    public static final String FONT_SIZE_LARGE = "30";

    public static final String FONT_SIZE_MEDIUM = "24";

    public static final String FONT_SIZE_SMALL = "18";

    public static final String[] MODEL_NAME = { "SC-01C", "L-06C" };

    public static final String[] NORMAL_FONT_SIZE_ENTRIES = { "大", "小" };

    public static final String[] NORMAL_FONT_SIZE_VALUES = { FONT_SIZE_MEDIUM, FONT_SIZE_SMALL };

    public static final String[] TABLET_FONT_SIZE_ENTRIES = { "大", "中", "小" };

    public static final String[] TABLET_FONT_SIZE_VALUES = { FONT_SIZE_LARGE, FONT_SIZE_MEDIUM, FONT_SIZE_SMALL };

    public static final String[] NORMAL_BLOCK_NUM_ENTRIES = { "1", "2", "3" };

    public static final String[] NORMAL_BLOCK_NUM_VALUES = { "1", "2", "3" };

    public static final String[] TABLET_BLOCK_NUM_ENTRIES = { "1", "2", "3", "4", "5" };

    public static final String[] TABLET_BLOCK_NUM_VALUES = { "1", "2", "3", "4", "5" };

    public static final int CHAR_COLOR_BLACK = Color.BLACK;

    public static final int CHAR_COLOR_WHITE = Color.WHITE;

    public static final int BACK_COLOR_BLACK = Color.BLACK;

    public static final int BACK_COLOR_WHITE = Color.WHITE;

    private int mLineNum;

    private int mRowNum;

    private int mFontSize;

    private int mBlockNum;

    private float mDispWidth;

    private float mDispHeight;

    public DisplayConfig(int fontSize, int blockNum, float dispWidth, float dispHeight) {
        this.mFontSize = fontSize;
        this.mBlockNum = blockNum;
        this.mDispWidth = dispWidth;
        this.mDispHeight = dispHeight;
    }

    public void setSmartphone() {
        setConfig(1);
    }

    public void setTablet() {
        setConfig(2);
    }

    /**
	 *
	 * @param dispType
	 */
    private void setConfig(int dispType) {
        Paint paint = new Paint();
        paint.setTextSize(mFontSize);
        float fontSpace = paint.getFontSpacing();
        mLineNum = MathDataUtil.calcLineNum(mDispHeight / mBlockNum, ARTICLE_TOP_SPACE, fontSpace);
        mRowNum = MathDataUtil.calcRowNum(mDispWidth, fontSpace * 1.5f);
    }

    /**
	 *
	 * @return
	 */
    public static boolean checkModel() {
        boolean result = false;
        String currentModel = Build.MODEL;
        for (String mn : MODEL_NAME) {
            if (currentModel.equals(mn)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
	 *
	 * @return
	 */
    public static String getDefaultFontSize() {
        return getConfig(1);
    }

    public static String getDefaultBlockNum() {
        return getConfig(2);
    }

    private static String getConfig(int i) {
        String str = null;
        if (checkModel()) {
            if (i == 1) {
                str = TABLET_FONT_SIZE_VALUES[0];
            } else {
                str = TABLET_BLOCK_NUM_VALUES[0];
            }
        } else {
            if (i == 1) {
                str = NORMAL_FONT_SIZE_VALUES[0];
            } else {
                str = NORMAL_BLOCK_NUM_VALUES[0];
            }
        }
        return str;
    }

    public int getLineNum() {
        return mLineNum;
    }

    public int getRowNum() {
        return mRowNum;
    }

    public int getFontSize() {
        return mFontSize;
    }

    public int getmBlockNum() {
        return mBlockNum;
    }
}
