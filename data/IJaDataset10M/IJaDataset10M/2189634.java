package com.android.plagas;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.android.plagas.dao.Maleza;
import com.android.plagas.dao.MalezaDao;
import com.android.plagas.dao._dao;

public class infoMalezaActivity extends Activity {

    private Maleza male;

    private _dao dao;

    private MalezaDao maleDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plagainfo);
        dao = _dao.getInstance(this);
        maleDao = dao.getSession().getMalezaDao();
        Bundle bundle = getIntent().getExtras();
        long maleId = bundle.getLong("malezaId");
        male = maleDao.load(maleId);
        completaCampos();
    }

    private void completaCampos() {
        ImageView ivFoto = (ImageView) findViewById(R.id.ivPic);
        Drawable image = getResources().getDrawable(getDrawableFromName());
        ivFoto.setImageDrawable(image);
    }

    public void onOkClick(View Button) {
        finish();
    }

    private int getDrawableFromName() {
        int resID = getResources().getIdentifier("com.android.plagas:drawable/male_" + male.getId(), null, null);
        if (resID == 0) return R.drawable.francella;
        return resID;
    }
}
