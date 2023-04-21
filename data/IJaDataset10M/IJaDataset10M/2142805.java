package org.androidsoft.games.puzzle.kids;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import org.androidsoft.games.utils.credits.CreditsParams;
import org.androidsoft.games.utils.credits.CreditsView;

/**
 *
 * @author Pierre Levy
 */
public class CreditsActivity extends Activity {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View view = new CreditsView(this, getCreditsParams());
        setContentView(view);
    }

    private CreditsParams getCreditsParams() {
        CreditsParams p = new CreditsParams();
        p.setAppNameRes(R.string.credits_app_name);
        p.setAppVersionRes(R.string.credits_current_version);
        p.setBitmapBackgroundRes(R.drawable.background);
        p.setBitmapBackgroundLandscapeRes(R.drawable.background);
        p.setArrayCreditsRes(R.array.credits);
        p.setColorDefault(0xFF7BB026);
        p.setTextSizeDefault(24);
        p.setTypefaceDefault(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        p.setSpacingBeforeDefault(10);
        p.setSpacingAfterDefault(15);
        p.setColorCategory(0xFFFFFFFF);
        p.setTextSizeCategory(14);
        p.setTypefaceCategory(Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC));
        p.setSpacingBeforeCategory(10);
        p.setSpacingAfterCategory(10);
        return p;
    }
}
