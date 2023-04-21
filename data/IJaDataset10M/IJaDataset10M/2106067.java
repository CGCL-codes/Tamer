package com.hundsun.wowsecrit;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

public class WoWSecritToolActivity extends Activity {

    Handler handler = new Handler();

    TextView textValue;

    String[][] woWSecritValue = { { "4 5", "2 0 5", "1 9", "3 3 6", "7 0 1", "6 9 ", "1 9 1", "8 5", "9 8 4", "3 0", "5 4", "5 9" }, { "3 8 4", "6", " 4 5 0", "3 2 8", "2 0 2", "4 2", "3 9", "2 5 5", "5 8 8", "6 7 6", "1 3 1", "9 2" }, { "9 2 7", "1 8", "4 8", "9 0 6", "4", "5 4 8", "4 8", "4 8 5", "7 4 2", "6 9", "9 4 6", "5 0 2" }, { "3 4 3", "9", "6 3 7", "3 3", "2 4 0", "7 7 6", "9 8", "8 4", "1 9 8", "8", "2 0", "2 7" }, { "1 0 7", "5 3 6", "4 8", "7 6 9", "2 4 1", "4 5 1", "6 7 8", "5 6", "4 5", "6 5 5", "5 8 5", "1 6 6" }, { "4 1 6", "3 6", "2 1", "6 4 1", "8 9 6", "5 8 8", "8 7 9", "4 0 6", "5 5", "2", "9 3 7", "3 2" }, { "1 7", "8 6", "1 8 9", "1 0", "6 0", "6 8 2", "9 4 4", "2 3 6", "4 5 7", "2", "5 9", "6 0 9" } };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = this.getLayoutInflater().inflate(R.layout.main, null);
        setContentView(contentView);
        textValue = (TextView) findViewById(R.id.wowsecrit);
        initView(contentView);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                makeWheelViewTextRightShow();
            }
        }, 150);
    }

    private String[] x_list;

    private String y_list[][];

    WheelView subWV;

    WheelView baseWV;

    View contentView;

    private void initView(View view) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        view.setMinimumWidth(screenWidth - 20);
        baseWV = (WheelView) view.findViewById(R.id.bankuaibase);
        x_list = getArrayById(R.array.region_list);
        baseWV.setVisibleItems(9);
        baseWV.setAdapter(new ArrayWheelAdapter<String>(x_list));
        y_list = new String[][] { getArrayById(R.array.region_detail_list_1), getArrayById(R.array.region_detail_list_2), getArrayById(R.array.region_detail_list_3), getArrayById(R.array.region_detail_list_4), getArrayById(R.array.region_detail_list_5), getArrayById(R.array.region_detail_list_6), getArrayById(R.array.region_detail_list_7) };
        subWV = (WheelView) view.findViewById(R.id.bankuaisub);
        subWV.setVisibleItems(9);
        baseWV.addChangingListener(new OnWheelChangedListener() {

            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                subWV.setAdapter(new ArrayWheelAdapter<String>(y_list[newValue]));
                subWV.setCurrentItem(y_list[newValue].length / 2);
                showTextValue(x_list[baseWV.getCurrentItem()], y_list[baseWV.getCurrentItem()][subWV.getCurrentItem()]);
            }
        });
        subWV.addChangingListener(new OnWheelChangedListener() {

            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                showTextValue(x_list[baseWV.getCurrentItem()], y_list[baseWV.getCurrentItem()][subWV.getCurrentItem()]);
            }
        });
        baseWV.setCurrentItem(2);
    }

    private void showTextValue(String x, String y) {
        int xValue = 0;
        int yValue = 0;
        switch(x.toCharArray()[0]) {
            case 'A':
                xValue = 0;
                break;
            case 'B':
                xValue = 1;
                break;
            case 'C':
                xValue = 2;
                break;
            case 'D':
                xValue = 3;
                break;
            case 'E':
                xValue = 4;
                break;
            case 'F':
                xValue = 5;
                break;
            case 'G':
                xValue = 6;
                break;
        }
        yValue = Integer.valueOf(y) - 1;
        textValue.setText(woWSecritValue[xValue][yValue]);
    }

    private String[] getArrayById(int id) {
        ArrayList<String> list = new ArrayList<String>();
        for (String s : getResources().getStringArray(id)) {
            list.add(s);
        }
        String[] str = new String[list.size()];
        return list.toArray(str);
    }

    /** ������һ�£���ʼ����ʾ���������ص� */
    private void makeWheelViewTextRightShow() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (baseWV != null) baseWV.justify();
                if (contentView != null) {
                    contentView.setVisibility(View.VISIBLE);
                }
            }
        }, 100);
    }
}
