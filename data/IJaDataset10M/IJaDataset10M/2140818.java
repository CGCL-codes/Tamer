package org.androidsoft.coloring.ui.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import org.androidsoft.coloring.R;
import org.androidsoft.utils.credits.CreditsParams;
import org.androidsoft.utils.credits.CreditsView;
import org.androidsoft.utils.ui.BasicActivity;

/**
 * Credits activity
 * @author Pierre Levy
 */
public class CreditsActivity extends BasicActivity {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        View view = new CreditsView(this, getCreditsParams());
        setContentView(view);
    }

    /**
     * {@inheritDoc } 
     */
    @Override
    public int getMenuResource() {
        return R.menu.menu_close;
    }

    /**
     * {@inheritDoc } 
     */
    @Override
    public int getMenuCloseId() {
        return R.id.menu_close;
    }

    private CreditsParams getCreditsParams() {
        CreditsParams p = new CreditsParams();
        p.setAppNameRes(R.string.credits_app_name);
        p.setAppVersionRes(R.string.credits_current_version);
        p.setBitmapBackgroundRes(R.drawable.background);
        p.setBitmapBackgroundLandscapeRes(R.drawable.background_land);
        p.setArrayCreditsRes(R.array.credits);
        p.setColorDefault(0xCCCEA757);
        p.setTextSizeDefault(32);
        p.setTypefaceDefault(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        p.setSpacingBeforeDefault(10);
        p.setSpacingAfterDefault(18);
        p.setColorCategory(0xCCFFFFFF);
        p.setTextSizeCategory(20);
        p.setTypefaceCategory(Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC));
        p.setSpacingBeforeCategory(12);
        p.setSpacingAfterCategory(12);
        return p;
    }
}
