package com.xlg.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.xlg.R;
import com.xlg.adapter.ListViewAdapter;
import com.xlg.base.BaseActivity;
import com.xlg.common.network.RequestCallBack;
import com.xlg.common.network.RequestJson;
import com.xlg.common.network.RequestServer;
import com.xlg.common.utils.LayoutUtils;

/**
 * 搜索
 */
public class SearchActivity extends BaseActivity {

    /** 搜索框 */
    private EditText searchView;

    /** 结果listview */
    private ListView searchResultList;

    /** 删除图标 */
    private Drawable mIconSearchClear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        initView();
    }

    /**
	 * 初始化视图
	 */
    private void initView() {
        LayoutUtils.setTitle(this, R.layout.search_title, R.id.search_lable, "");
        LayoutUtils.setContent(this, R.layout.search_view);
        searchView = (EditText) findViewById(R.id.search_content);
        searchView.addTextChangedListener(textWatcher);
        searchView.setOnTouchListener(onTouchListener);
        searchResultList = (ListView) findViewById(R.id.search_result_list);
        findViewById(R.id.search_submit).setOnClickListener(onClickListener);
        mIconSearchClear = getResources().getDrawable(R.drawable.search_clear_icon);
    }

    /**
	 * 搜索框改变监听
	 */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            searchView.setCompoundDrawablesWithIntrinsicBounds(null, null, TextUtils.isEmpty(s) ? null : mIconSearchClear, null);
            searchResultList.setVisibility(View.VISIBLE);
            String key = String.valueOf(s);
            if (StringUtils.isBlank(key)) {
                searchResultList.setAdapter(new ArrayAdapter<String>(SearchActivity.this, R.layout.nodata));
                return;
            }
            String requestJson = RequestJson.getSearchTiXingJson(StringUtils.trim(key));
            RequestServer.call(SearchActivity.this, requestJson, callBack, false);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };

    /**
	 * 搜索自动提示回调  处理UI
	 */
    private RequestCallBack callBack = new RequestCallBack() {

        @Override
        public void callback(JSONObject resultJson) throws Throwable {
            List<Map<String, String>> list = buildDataList(resultJson);
            if (list == null || list.size() == 0) return;
            String[] fromData = new String[] { "itemTitle", "itemText" };
            int[] toID = new int[] { R.id.itemTitle, R.id.itemText };
            ListViewAdapter listAdapter = new ListViewAdapter(SearchActivity.this, list, R.layout.search_result, fromData, toID);
            searchResultList.setAdapter(listAdapter);
            searchResultList.setOnItemLongClickListener(onItemLongClickListener);
            searchResultList.setOnItemClickListener(onItemClickListener);
        }
    };

    /**
	 * 组装数据
	 */
    private List<Map<String, String>> buildDataList(JSONObject resultJsonObj) throws Throwable {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        JSONArray jsonArray = resultJsonObj.getJSONArray("list");
        int size = jsonArray.length();
        Map<String, String> itemMap = null;
        JSONObject jsonObj = null;
        for (int i = 0; i < size; i++) {
            jsonObj = jsonArray.getJSONObject(i);
            if (jsonObj == null) continue;
            itemMap = new HashMap<String, String>();
            itemMap.put("itemTitle", jsonObj.getString("content"));
            itemMap.put("itemText", jsonObj.getString("count") + "条结果");
            list.add(itemMap);
        }
        return list;
    }

    /**
	 * 自动提示列表长按监听
	 */
    private OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            showSearchResult(view);
            return false;
        }
    };

    /**
	 * 自动提示列表点击监听
	 */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showSearchResult(view);
        }
    };

    /**
	 * 显示搜索商品结果列表
	 */
    private void showSearchResult(View view) {
        TextView keyTextView = (TextView) view.findViewById(R.id.itemTitle);
        showSearchList(keyTextView.getText().toString());
    }

    /**
	 * 搜索框触摸监听
	 */
    private OnTouchListener onTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                int curX = (int) event.getX();
                if (mIconSearchClear != null && curX > v.getWidth() - mIconSearchClear.getIntrinsicWidth()) {
                    searchView.setText(StringUtils.EMPTY);
                    int cacheInputType = searchView.getInputType();
                    searchView.setInputType(InputType.TYPE_NULL);
                    searchView.onTouchEvent(event);
                    searchView.setInputType(cacheInputType);
                    return true;
                }
            }
            return false;
        }
    };

    /**
	 * 提交按钮点击监听
	 */
    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            String key = searchView.getText().toString();
            if (StringUtils.isBlank(key)) {
                Toast.makeText(SearchActivity.this, R.string.search_key_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            showSearchList(key);
        }
    };

    /**
	* 显示搜索结果
	*/
    private void showSearchList(String searchContent) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("searchContent", searchContent);
        parent().startSubActivity(intent);
    }
}
