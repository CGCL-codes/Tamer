package cn.edu.nju.software.xyz.pim.rss;

import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx 2008-4-9 下午05:40:59
 * 
 */
public class RSSArticlesView extends ListActivity {

    private static final int ACTIVITY_CREATE = 1;

    private static final int REFRESH_M_ID = 0;

    private static final int RETURN_M_ID = 1;

    private long feedId;

    private List<Article> articleList;

    private List<String> articleTitle;

    @SuppressWarnings("unchecked")
    private ArrayAdapter articleAdapter;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.rss_article_list);
        Bundle extras = getIntent().getExtras();
        if (extras != null) feedId = extras.getLong("FEEDID");
        articleTitle = new ArrayList<String>();
        articleAdapter = new ArrayAdapter(this, R.layout.rss_article_row, R.id.rss_row_text, articleTitle);
        setListAdapter(articleAdapter);
        fillData();
    }

    @SuppressWarnings({ "static-access", "unchecked" })
    private void fillData() {
        final ProgressDialog pb = ProgressDialog.show(this, "Getting Articles", "", true, false);
        final Handler handler = new Handler();
        final Runnable dissMiss = new Runnable() {

            public void run() {
                articleAdapter = new ArrayAdapter(RSSArticlesView.this, R.layout.rss_article_row, R.id.rss_row_text, articleTitle);
                setListAdapter(articleAdapter);
                pb.dismiss();
            }
        };
        Thread getArticlesThread = new Thread(new Runnable() {

            @Override
            public void run() {
                articleList = NewsDroidDB.getInstance(RSSArticlesView.this).getArticles(feedId);
                int count = articleList.size();
                articleTitle.clear();
                for (int index = 0; index < count; ++index) {
                    articleTitle.add(articleList.get(index).Title);
                }
                handler.post(dissMiss);
            }
        });
        getArticlesThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, REFRESH_M_ID, R.string.refresh, R.drawable.refresh);
        menu.add(0, RETURN_M_ID, R.string.back, R.drawable.back);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, Item item) {
        switch(item.getId()) {
            case REFRESH_M_ID:
                refreshArticles();
                break;
            case RETURN_M_ID:
                finish();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void refreshArticles() {
        final ProgressDialog pb = ProgressDialog.show(this, "Refreshing", "", true, false);
        final Handler handler = new Handler();
        final Runnable dissMiss = new Runnable() {

            @Override
            public void run() {
                pb.dismiss();
                fillData();
            }
        };
        Thread getArticlesThread = new Thread(new Runnable() {

            @Override
            public void run() {
                RSSHandler.getInstance().updateArticles(RSSArticlesView.this, NewsDroidDB.getInstance(RSSArticlesView.this).getFeed(feedId));
                handler.post(dissMiss);
            }
        });
        getArticlesThread.start();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        openFeed(position);
    }

    private void openFeed(int position) {
        Intent openIntent = new Intent(this, RSSDescriptionView.class);
        openIntent.putExtra("ARTICLEID", articleList.get(position).ArticleId);
        startSubActivity(openIntent, ACTIVITY_CREATE);
    }
}
