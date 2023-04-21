package com.banyingli.more;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * @ClassName: MoreActivity
 * @Description: TODO "更多"界面
 * @date 2011-8-10 下午05:18:52
 * 
 */
public class MoreActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
