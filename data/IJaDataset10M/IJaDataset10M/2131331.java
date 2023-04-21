package net.sf.dvstar.android.diamon.journal;

import net.sf.dvstar.android.diamon.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;

public class CarbsWeeklyReport extends JournalCommonActivity implements IJournalListItems {

    public CarbsWeeklyReport(Resources resources) {
        NAME = resources.getString(R.string.jrn_name_common);
        DESC = resources.getString(R.string.jrn_desc_common);
    }

    public String getName() {
        return NAME;
    }

    public String getDesc() {
        return DESC;
    }

    public Bitmap getIcon() {
        return ICON;
    }

    public Intent execute(Context context) {
        return null;
    }
}
