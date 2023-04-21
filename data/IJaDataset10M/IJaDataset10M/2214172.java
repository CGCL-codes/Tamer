package wei.liu.myhealth.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wei.liu.myhealth.AbsMyHealthActivity;
import wei.liu.myhealth.MyDataActivity;
import wei.liu.myhealth.MyhealthActivity;
import wei.liu.myhealth.R;
import wei.liu.myhealth.entity.Norm;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Create by 2012-4-29
 * @author Liuw
 * @copyright Copyright(c) 2012-  Liuw
 */
public class AddData extends AbsMyHealthActivity {

    private EditText min;

    private EditText max;

    private Spinner selected;

    private DatePicker created;

    private Map<String, Norm> normMap;

    private ArrayAdapter<String> arr;

    private boolean hasMax = false;

    private int selectId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_make);
        min = (EditText) findViewById(R.id.min);
        min.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) min.setHint(""); else {
                    String strMin = min.getText() == null ? "" : min.getText().toString();
                    if (strMin.equals("")) min.setHint("最小值/记录值");
                }
            }
        });
        max = (EditText) findViewById(R.id.max);
        max.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) max.setHint(""); else {
                    String strMax = max.getText() == null ? "" : max.getText().toString();
                    if (strMax.equals("")) max.setHint("最大值");
                }
            }
        });
        selected = (Spinner) findViewById(R.id.normList);
        initNormMap();
        selected.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adp, View view, int index, long position) {
                Norm sNorm = normMap.get(adp.getItemAtPosition(index).toString());
                hasMax = sNorm.getHasMax() == 1 ? true : false;
                selectId = sNorm.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String max = ((EditText) findViewById(R.id.max)).getText().toString();
                max = max == null ? "" : max;
                if (hasMax && max.equals("")) Toast.makeText(AddData.this, "最大值必须填", Toast.LENGTH_SHORT).show(); else {
                    float min = Float.parseFloat(((EditText) findViewById(R.id.min)).getText().toString());
                    created = (DatePicker) findViewById(R.id.created);
                    int year = created.getYear();
                    int month = created.getMonth();
                    int day = created.getDayOfMonth();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    Cursor cursor = dataAdapter.fetchDatas("SELECT MAX(id) AS new_id FROM myindex");
                    int new_id = cursor.getInt(0) + 1;
                    cursor.close();
                    Map<String, String> datas = new HashMap<String, String>();
                    datas.put("id", String.valueOf(new_id));
                    datas.put("nid", String.valueOf(selectId));
                    datas.put("minNum", String.valueOf(min));
                    datas.put("maxNum", max.equals("") ? "0" : max);
                    datas.put("created", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()) + new SimpleDateFormat(" HH:mm:SS").format(new Date()));
                    if (dataAdapter.insert("myindex", null, datas) > -1) {
                        Toast.makeText(AddData.this, "数据已保存", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(AddData.this, MyhealthActivity.class);
                        intent.putExtra("tabId", getResources().getString(R.string.tab_data));
                        startActivity(intent);
                        AddData.this.finish();
                    } else {
                        Toast.makeText(AddData.this, "数据保存失败了", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AddData.this, MyhealthActivity.class);
                intent.putExtra("tabId", getResources().getString(R.string.tab_data));
                startActivity(intent);
                AddData.this.finish();
            }
        });
    }

    /**
	 * 初始化项目列表
	 * Create by 2012-4-30
	 * @author Liuw
	 */
    private void initNormMap() {
        String sql = "SELECT id, name, minNum, maxNum, unit, hasMax FROM norm ORDER BY id ASC";
        Cursor cursor = dataAdapter.fetchDatas(sql);
        normMap = new HashMap<String, Norm>();
        List<String> items = new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            Norm norm = new Norm(cursor.getInt(0), cursor.getString(1), cursor.getFloat(2), cursor.getFloat(3), cursor.getString(4), cursor.getInt(5));
            normMap.put(norm.getName(), norm);
            items.add(norm.getName());
            cursor.moveToNext();
        }
        arr = new ArrayAdapter<String>(AddData.this, android.R.layout.simple_spinner_item, items);
        arr.setDropDownViewResource(R.layout.add_data_spinner);
        selected.setAdapter(arr);
    }
}
