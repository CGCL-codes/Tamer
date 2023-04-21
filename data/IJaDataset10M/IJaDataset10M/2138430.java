package br.ufmg.ubicomp.droidguide.context;

import java.util.ArrayList;
import java.util.List;
import br.ufmg.ubicomp.droidguide.R;
import br.ufmg.ubicomp.droidguide.application.Web;
import br.ufmg.ubicomp.droidguide.eventservice.NotificationList;
import br.ufmg.ubicomp.droidguide.eventservice.ServiceList;
import br.ufmg.ubicomp.droidguide.utils.AndroidUtils;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ContextList extends ListActivity {

    List<String> infoItems = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListInfoServices();
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, infoItems));
        getListView().setItemsCanFocus(true);
        getListView().setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View view, int arg2, long id) {
                String info = getItem((int) id);
                showInfoView(info);
            }
        });
        getListView().setTextFilterEnabled(true);
    }

    private void initListInfoServices() {
        String[] meuArray = getResources().getStringArray(R.array.context_menu);
        for (String s : meuArray) {
            infoItems.add(s);
        }
    }

    private String getItem(int id) {
        return infoItems.get(id);
    }

    private void showInfoView(String info) {
        if (info.equals("Hunger")) {
            AndroidUtils.startActivity(this, Hunger.class);
        } else if (info.equals("Mood")) {
            AndroidUtils.startActivity(this, Mood.class);
        } else if (info.equals("Provision")) {
            AndroidUtils.startActivity(this, Provision.class);
        } else if (info.equals("Time Available")) {
            AndroidUtils.startActivity(this, TimeAvailable.class);
        } else if (info.equals("Sleep")) {
            AndroidUtils.startActivity(this, Sleep.class);
        }
    }
}
