package com.founder.android.sample;

import java.io.File;
import org.apache.http.HttpResponse;
import com.founder.android.http.HttpClientHelper;
import com.founder.android.http.ResponseListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class TestSampleActivity extends Activity {

    private EditText urlText;

    private EditText resultText;

    private ViewGroup imagePanel;

    private int type = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        urlText = (EditText) findViewById(R.id.editText1);
        resultText = (EditText) findViewById(R.id.result);
        imagePanel = (ViewGroup) findViewById(R.id.imagesPanel);
        Button request = (Button) findViewById(R.id.btnRequest);
        request.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                String url = "http://" + urlText.getText();
                appendResult("\nRequest Url:\n" + url + "\n...");
                if (type == 0) {
                    HttpClientHelper.asyncGetTextRequest(TestSampleActivity.this, url, textResponseListener);
                } else if (type == 1) {
                    HttpClientHelper.asyncGetImageRequest(TestSampleActivity.this, url, imageResponseListener);
                } else {
                    String localPath = TestSampleActivity.this.getFilesDir().getAbsolutePath();
                    Log.d("TestSampleActivity", "localPath=" + localPath);
                    File file = new File(localPath + File.separator + "images" + File.separator + "smoke.gif");
                    HttpClientHelper.asyncDownFileRequest(TestSampleActivity.this, url, file, downLoadResponseListener);
                }
            }
        });
        Button btnText = (Button) findViewById(R.id.btnText);
        btnText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                type = 0;
                urlText.setText("www.google.com/ig/api?hl=zh-cn&weather=Beijing");
            }
        });
        Button btnImage = (Button) findViewById(R.id.btnImage);
        btnImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                type = 1;
                urlText.setText("www.google.com/ig/images/weather/mostly_sunny.gif");
            }
        });
        Button btnDown = (Button) findViewById(R.id.btnDownLoad);
        btnDown.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                type = 2;
                urlText.setText("www.google.com/ig/images/weather/smoke.gif");
            }
        });
        Button btnClean = (Button) findViewById(R.id.btnClean);
        btnClean.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                type = 0;
                urlText.setText("");
                resultText.setText("");
                imagePanel.removeAllViews();
            }
        });
        Button weather = (Button) findViewById(R.id.btnWeather);
        weather.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Context _this = TestSampleActivity.this;
                Intent intent = new Intent(_this, WeatherActivity.class);
                _this.startActivity(intent);
            }
        });
    }

    private ResponseListener downLoadResponseListener = new ResponseListener() {

        @Override
        public void onResponseFailed(HttpResponse response, Throwable error) {
            appendResult("\nDownLoad Request Failed!\n" + error);
        }

        @Override
        public void onResponseSuccess(HttpResponse response, Object result) {
            File file = (File) result;
            appendResult("\nDownLoad Request success!\n" + file.getAbsolutePath());
            ImageView imageView = new ImageView(TestSampleActivity.this);
            imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            imagePanel.addView(imageView);
        }
    };

    private ResponseListener imageResponseListener = new ResponseListener() {

        @Override
        public void onResponseFailed(HttpResponse response, Throwable error) {
            appendResult("\nBitmap Request Failed!\n" + error);
        }

        @Override
        public void onResponseSuccess(HttpResponse response, Object result) {
            appendResult("\nBitmap Request success!\n");
            ImageView imageView = new ImageView(TestSampleActivity.this);
            imageView.setImageBitmap((Bitmap) result);
            imagePanel.addView(imageView);
        }
    };

    private ResponseListener textResponseListener = new ResponseListener() {

        @Override
        public void onResponseFailed(HttpResponse response, Throwable error) {
            appendResult("\nText Request Failed!\n" + error);
        }

        @Override
        public void onResponseSuccess(HttpResponse response, Object result) {
            appendResult("\nText Request success!\n" + result);
        }
    };

    private void appendResult(String str) {
        resultText.append(str);
        resultText.setSelection(resultText.length());
    }
}
