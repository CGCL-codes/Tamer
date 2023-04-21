package com.edu.koTA07.takingOrder;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.edu.koTA07.adapter.MenuPesananListAdapter;
import com.edu.koTA07.bean.PesananDetailBean;
import com.edu.koTA07.utils.PesananDetailUtils;

public class ListPesanan extends Activity {

    private ListView pesananDetailList;

    ArrayList<PesananDetailBean> currentData;

    PesananDetailBean pesananDetailBean;

    MenuPesananListAdapter pesananDetailListAdapter;

    PesananDetailUtils util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_pesanan);
        util = new PesananDetailUtils(this);
        init();
    }

    public void init() {
        pesananDetailList = (ListView) findViewById(android.R.id.list);
        currentData = new ArrayList<PesananDetailBean>();
        currentData = util.loadData();
        pesananDetailListAdapter = new MenuPesananListAdapter(this, currentData);
        pesananDetailList.setAdapter(pesananDetailListAdapter);
    }
}
