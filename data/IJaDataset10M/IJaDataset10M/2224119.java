package com.google.cdiscount.jayelco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import com.google.cdiscount.jayelco.scan.IntentIntegrator;
import com.google.cdiscount.jayelco.scan.IntentResult;

public class Cdiscount extends Activity {

    private OnClickListener btScanListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            IntentIntegrator.initiateScan(Cdiscount.this);
        }
    };

    private OnClickListener btAboutListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Cdiscount.this, About.class);
            startActivity(intent);
        }
    };

    private OnClickListener btFindListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Cdiscount.this, ProductSearch.class);
            startActivity(intent);
        }
    };

    private OnClickListener btAccountListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root);
        ImageButton btScan = (ImageButton) this.findViewById(R.id.BtScan);
        btScan.setOnClickListener(btScanListener);
        ImageButton btAbout = (ImageButton) this.findViewById(R.id.BtAbout);
        btAbout.setOnClickListener(btAboutListener);
        ImageButton btFindProduct = (ImageButton) this.findViewById(R.id.BtFindProduct);
        btFindProduct.setOnClickListener(btFindListener);
        ImageButton btAccount = (ImageButton) this.findViewById(R.id.BtAccount);
        btAccount.setOnClickListener(btAccountListener);
        TranslateAnimation trans1 = new TranslateAnimation(-320, 0, 0, 0);
        trans1.setStartOffset(320);
        trans1.setFillAfter(true);
        trans1.setDuration(1000);
        btScan.startAnimation(trans1);
        trans1 = new TranslateAnimation(320, 0, 0, 0);
        trans1.setStartOffset(320);
        trans1.setFillAfter(true);
        trans1.setDuration(1000);
        btAccount.startAnimation(trans1);
        trans1 = new TranslateAnimation(0, 0, 200, 0);
        trans1.setStartOffset(320);
        trans1.setFillAfter(true);
        trans1.setDuration(1000);
        btFindProduct.startAnimation(trans1);
        btAbout.startAnimation(trans1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("requestCode " + requestCode);
        System.out.println("resultCode" + resultCode);
        System.out.println("data " + data);
        switch(requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (scanResult != null) {
                        String out = scanResult.getContents();
                        System.out.println("out " + out);
                        if (out != null) {
                            Intent intent = new Intent(Cdiscount.this, ProductDetails.class);
                            intent.putExtra("bareCode", out);
                            startActivity(intent);
                        }
                    }
                }
        }
    }
}
