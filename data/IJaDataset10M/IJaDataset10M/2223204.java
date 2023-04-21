package com.guanri.android.insurance.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.guanri.android.lib.utils.StringUtils;

/**
 * 保险销售查询界面
 * @author Administrator
 *
 */
public class InsuUselessQueryAcvitity extends ApsaiActivity {

    private static Logger logger = Logger.getLogger(SaleCheckedRecordActivity.class);

    private ListView queryedRecordList = null;

    private DBOperator dbOperator = null;

    private SaleCheckListAdapter recordAdapter = null;

    private String[] from = null;

    private int[] to = null;

    private List<Map<String, String>> drinkandurinesMapList = null;

    private ApsaiPageNumPannelView apsaiPageNumPannel = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(StringUtils.getStringFromValue(R.string.apsai_sale_useless_query));
        setContentView(R.layout.user_sale_check_manager);
        dbOperator = DBOperator.getInstance();
        MainApplication.getInstance().setScreenW2H(this);
        queryedRecordList = (ListView) this.findViewById(R.id.sale_checked_list);
        initArrayList();
        apsaiPageNumPannel = (ApsaiPageNumPannelView) this.findViewById(R.id.sale_checked_page_num_pannel);
        apsaiPageNumPannel.setApsaiPageNumOperator(apsaiPageNumOperator);
    }

    /**
	 * 根据数据构造表格
	 */
    public void initArrayList() {
        from = new String[] { "Paper_no", "number", "Operator_id", "Operate_time" };
        to = new int[] { R.id.apsai_sale_query_list_billno, R.id.apsai_sale_query_list_number, R.id.apsai_sale_query_list_operate_no, R.id.apsai_sale_query_list_operate_time };
        queryedRecordList.setHorizontalScrollBarEnabled(true);
        queryedRecordList.setDividerHeight(0);
        LayoutInflater mInflater = LayoutInflater.from(this);
        LinearLayout header = (LinearLayout) mInflater.inflate(R.layout.insu_useless_query_list_header, null);
        queryedRecordList.addHeaderView(header);
    }

    private ApsaiPageNumOperator apsaiPageNumOperator = new ApsaiPageNumOperator() {

        /**
		 * 查询总记录数
		 */
        public int queryAllRows() {
            return dbOperator.queryRowNum(DBBean.TB_SALE_ORDER_USELESS, null);
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
            param.put("ORDERBY", "Operate_time DESC");
            drinkandurinesMapList = dbOperator.queryMapList(DBBean.TB_SALE_ORDER_USELESS, from, param);
            recordAdapter = new SaleCheckListAdapter(InsuUselessQueryAcvitity.this, drinkandurinesMapList, R.layout.insu_useless_query_list_item, from, to);
            queryedRecordList.setAdapter(recordAdapter);
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
}
