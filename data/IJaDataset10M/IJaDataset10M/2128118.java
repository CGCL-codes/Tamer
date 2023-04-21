package wei.liu.myhealth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import wei.liu.myhealth.adapter.HealthsAdapter;
import wei.liu.myhealth.entity.ProfileHealth;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

/**
 * 我的信息 Create by 2012-4-20
 * 
 * @author liuw
 * @copyright Copyright(c) 2012-2014 Liuw
 */
public class MyProfileActivity extends AbsMyHealthActivity {

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_profile);
        ListView healths = (ListView) findViewById(R.id.healths);
        List<ProfileHealth> datas = new ArrayList<ProfileHealth>();
        for (int i = 0; i < 7; i++) {
            ProfileHealth ph = new ProfileHealth();
            ph.setTitle("四个汉字");
            if (i % 2 == 1) {
                ph.setNum("130/85 mmHg");
                ph.setState(0);
            } else {
                ph.setNum("9.0 mmol/L");
                ph.setState(1);
            }
            ph.setCreate(new Date());
            datas.add(ph);
        }
        healths.setAdapter(new HealthsAdapter(this, datas));
    }

    /**
	 * 更新列表
	 * Create by 2012-4-24
	 * @author Liuw
	 */
    private void updateListView() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        return true;
    }
}
