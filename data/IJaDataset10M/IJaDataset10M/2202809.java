package com.xiaolei.android.BizTracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import android.content.Context;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.xiaolei.android.common.Utility;
import com.xiaolei.android.entity.CostValue;
import com.xiaolei.android.listener.OnLoadCompletedListener;
import com.xiaolei.android.service.DataService;

/**
 * @author xiaolei
 * 
 */
public class MonthlyTransactionListAdapter extends BaseAdapter {

    private Context context;

    private Date date;

    private Date[] items;

    private LayoutInflater inflater;

    private String defaultCurrencyCode = "";

    private OnLoadCompletedListener loadCompletedListener;

    private ListView listView;

    private MonthlyTransactionListAdapter self;

    private View parentView;

    private Hashtable<Integer, CostValue> dataSource;

    public MonthlyTransactionListAdapter(Context context, Date date) {
        this.context = context;
        this.date = date;
        items = new Date[0];
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.date);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date startDay = Utility.getStartDayOfMonth(date);
        Date d = (Date) startDay.clone();
        ArrayList<Date> dates = new ArrayList<Date>();
        dataSource = new Hashtable<Integer, CostValue>();
        for (int i = 0; i < days; i++) {
            d = new Date(startDay.getYear(), startDay.getMonth(), i + 1);
            dates.add(d);
            CostValue value = new CostValue();
            value.ExpenseMoney = 0d;
            value.IncomeMoney = 0d;
            dataSource.put(i, value);
        }
        items = dates.toArray(items);
        inflater = LayoutInflater.from(context);
    }

    public MonthlyTransactionListAdapter(Context context, View parentView, ListView listView, Date date, OnLoadCompletedListener loadCompletedListener) {
        this(context, date);
        this.self = this;
        this.parentView = parentView;
        this.loadCompletedListener = loadCompletedListener;
        this.listView = listView;
    }

    public void loadDataAsync() {
        if (parentView != null) {
            ViewFlipper viewSwitcher = (ViewFlipper) parentView.findViewById(R.id.viewFlipperMonthlyTransactionList);
            if (viewSwitcher != null) {
                viewSwitcher.setDisplayedChild(0);
            }
        }
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... args) {
                defaultCurrencyCode = DataService.GetInstance(context).getDefaultCurrencyCode();
                defaultCurrencyCode = defaultCurrencyCode != null ? defaultCurrencyCode : "";
                for (int i = 0; i < items.length; i++) {
                    Date dayOfMonth = items[i];
                    double pay = DataService.GetInstance(context).getTotalPay(dayOfMonth, Utility.getEndTimeOfDate(dayOfMonth));
                    double earn = DataService.GetInstance(context).getTotalEarn(dayOfMonth, Utility.getEndTimeOfDate(dayOfMonth));
                    CostValue value = new CostValue();
                    value.ExpenseMoney = pay;
                    value.IncomeMoney = earn;
                    dataSource.put(i, value);
                }
                return true;
            }

            protected void onPostExecute(Boolean result) {
                if (parentView != null) {
                    ViewFlipper viewSwitcher = (ViewFlipper) parentView.findViewById(R.id.viewFlipperMonthlyTransactionList);
                    if (viewSwitcher != null) {
                        viewSwitcher.setDisplayedChild(1);
                    }
                }
                if (result == true) {
                    if (listView != null) {
                        listView.setAdapter(self);
                    }
                }
                if (loadCompletedListener != null) {
                    loadCompletedListener.onLoadCompleted(result);
                }
            }
        };
        task.execute();
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int arg0) {
        return items[arg0];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.day_of_month_item_template, parent, false);
        }
        TextView tvStuffName = (TextView) convertView.findViewById(R.id.textViewItemTemplateStuffName);
        TextView tvPay = (TextView) convertView.findViewById(R.id.textViewTotalPay);
        TextView tvEarn = (TextView) convertView.findViewById(R.id.textViewTotalEarn);
        Date date = items[position];
        String formattedDateString = DateUtils.formatDateTime(context, date.getTime(), DateUtils.FORMAT_NO_YEAR);
        tvStuffName.setText(formattedDateString);
        double pay = dataSource.get(position).ExpenseMoney;
        double earn = dataSource.get(position).IncomeMoney;
        if (pay < 0) {
            tvPay.setText(Utility.formatCurrency(pay, defaultCurrencyCode));
        } else {
            tvPay.setText("*");
        }
        if (earn > 0) {
            tvEarn.setText(Utility.formatCurrency(earn, defaultCurrencyCode));
        } else {
            tvEarn.setText("*");
        }
        return convertView;
    }
}
