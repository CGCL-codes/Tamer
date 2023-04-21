package model;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.struts.util.LabelValueBean;

public final class Def {

    public static final int LETTER = 0;

    public static final int LEGAL = 1;

    public static final int TABLOID = 2;

    public static final int A2 = 3;

    public static Collection<LabelValueBean> getPaperSizes() {
        ArrayList<LabelValueBean> paperSizes = new ArrayList<LabelValueBean>();
        paperSizes.add(new LabelValueBean("Tabloid (11x17)", "2"));
        paperSizes.add(new LabelValueBean("Legal (8.5x14)", "1"));
        paperSizes.add(new LabelValueBean("A2 (22x17)", "3"));
        paperSizes.add(new LabelValueBean("Letter (8.5x11)", "0"));
        return paperSizes;
    }

    public static final int UP = 2;

    public static final int DOWN = 3;

    public static final int IN = 0;

    public static final int CM = 1;

    public static final int SOLID = 0;

    public static final int DASHED = 1;

    public static final boolean USE_TEST_VALUES = true;

    public static final boolean LIVE = false;

    public static final int LEFT = 0;

    public static final int TOP_LEFT = 2;

    public static final int TOP = 3;

    public static final int TOP_RIGHT = 4;

    public static final int RIGHT = 1;

    public static final int BOTTOM_RIGHT = 5;

    public static final int BOTTOM = 6;

    public static final int BOTTOM_LEFT = 7;
}
