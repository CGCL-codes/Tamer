package com.guanri.android.insurance.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.guanri.android.insurance.R;
import com.guanri.android.insurance.activity.base.ApsaiActivity;
import com.guanri.android.insurance.activity.base.ApsaiPageNumPannelView;
import com.guanri.android.insurance.activity.base.ApsaiPageNumPannelView.ApsaiPageNumOperator;
import com.guanri.android.insurance.db.DBBean;
import com.guanri.android.insurance.db.DBOperator;
import com.guanri.android.lib.context.MainApplication;
import com.guanri.android.lib.log.Logger;
import com.guanri.android.lib.utils.Utils;

/**
 * 对账列表界面
 * @author Administrator
 *
 */
public class SaleCheckedRecordActivity extends ApsaiActivity {

    private static Logger logger = Logger.getLogger(SaleCheckedRecordActivity.class);

    private ListView checkedRecordList = null;

    private DBOperator dbOperator = null;

    private SaleCheckListAdapter recordAdapter = null;

    private String[] from = null;

    private int[] to = null;

    private List<Map<String, String>> drinkandurinesMapList = null;

    private ApsaiPageNumPannelView apsaiPageNumPannel = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sale_check_manager);
        this.setTitle(this.getString(R.string.apsai_sale_check_record));
        dbOperator = DBOperator.getInstance();
        MainApplication.getInstance().setScreenW2H(this);
        checkedRecordList = (ListView) this.findViewById(R.id.sale_checked_list);
        checkedRecordList.setOnItemClickListener(checkedRecordListListener);
        initArrayList();
        apsaiPageNumPannel = (ApsaiPageNumPannelView) this.findViewById(R.id.sale_checked_page_num_pannel);
        apsaiPageNumPannel.setApsaiPageNumOperator(apsaiPageNumOperator);
    }

    /**
	 * 根据数据构造表格
	 */
    public void initArrayList() {
        from = new String[] { "Check_id", "Order_count", "Order_sum", "Order_back_count", "Order_back_sum", "Order_useless_count", "Check_time" };
        to = new int[] { R.id.apsai_sale_check_list_batch, R.id.apsai_sale_check_list_orders, R.id.apsai_sale_check_list_order_price, R.id.apsai_sale_check_list_back_orders, R.id.apsai_sale_check_list_back_price, R.id.apsai_sale_check_list_usless_orders, R.id.apsai_sale_check_list_order_time };
        checkedRecordList.setHorizontalScrollBarEnabled(true);
        checkedRecordList.setDividerHeight(0);
        LayoutInflater mInflater = LayoutInflater.from(this);
        LinearLayout header = (LinearLayout) mInflater.inflate(R.layout.user_sale_check_list_header, null);
        checkedRecordList.addHeaderView(header);
    }

    private ApsaiPageNumOperator apsaiPageNumOperator = new ApsaiPageNumOperator() {

        /**
		 * 查询总记录数
		 */
        public int queryAllRows() {
            return dbOperator.queryRowNum(DBBean.TB_SALE_CHECK, null);
        }

        /**
		 * 设置表格数据 
		 * 	pageInfo[0]//第几页
		 *	pageInfo[1]//每页行数
		 *	pageInfo[2]//总页数
		 *	pageInfo[3]//总行数
		 */
        @Override
        public void initListAdapter(int[] pageInfo) {
            Map<String, String> param = new HashMap<String, String>(1);
            param.put("LIMIT", (pageInfo[0] - 1) * pageInfo[1] + "," + String.valueOf(pageInfo[1]));
            drinkandurinesMapList = dbOperator.queryMapList(DBBean.TB_SALE_CHECK, from, param);
            for (Map<String, String> item : drinkandurinesMapList) {
                try {
                    item.put("Order_sum", String.valueOf(Utils.fengToYuan(Integer.parseInt(item.get("Order_sum")))));
                } catch (Exception e) {
                }
                try {
                    item.put("Order_back_sum", String.valueOf(Utils.fengToYuan(Integer.parseInt(item.get("Order_back_sum")))));
                } catch (Exception e) {
                }
            }
            recordAdapter = new SaleCheckListAdapter(SaleCheckedRecordActivity.this, drinkandurinesMapList, R.layout.user_sale_check_list_item, from, to);
            checkedRecordList.setAdapter(recordAdapter);
        }
    };

    /**
	 * 列表数据适配器
	 * @author Administrator
	 *
	 */
    public class SaleCheckListAdapter extends SimpleAdapter {

        private int[] colors = new int[] { 0x30FF0000, 0x300000FF };

        public SaleCheckListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            int colorPos = position % colors.length;
            if (colorPos == 1) {
                view.setBackgroundColor(Color.argb(250, 255, 255, 255));
            } else {
                view.setBackgroundColor(Color.argb(250, 224, 243, 250));
            }
            return view;
        }
    }

    /**
	 * 行单击事件
	 */
    private OnItemClickListener checkedRecordListListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Map<String, String> item = (Map<String, String>) arg0.getItemAtPosition(arg2);
            if (item != null) {
                Intent intent = new Intent(SaleCheckedRecordActivity.this, SaleCheckActivity.class);
                intent.putExtra("Check_id", item.get("Check_id"));
                startActivity(intent);
            }
        }
    };

    @Override
    public void onClick(View v) {
    }
}
