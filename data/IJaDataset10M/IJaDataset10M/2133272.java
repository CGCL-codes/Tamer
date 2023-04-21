package org.light.portal.mobile.portlets;

import static org.light.portal.mobile.util.Configuration.JSON_SERVICE_INTERNALNEWS;
import java.util.ArrayList;
import org.json.JSONObject;
import org.light.portal.mobile.BaseActivity;
import org.light.portal.mobile.R;
import org.light.portal.mobile.Session;
import org.light.portal.mobile.model.Entity;
import org.light.portal.mobile.util.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

public class InternalNewsContentActivity extends BaseActivity {

    public static final String tag = "News";

    private ArrayList<Entity> contents = new ArrayList<Entity>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internal_news_content);
        bind();
    }

    @Override
    protected boolean needLoad() {
        return true;
    }

    @Override
    protected boolean isCachable() {
        return true;
    }

    @Override
    protected String getURL() {
        StringBuilder url = new StringBuilder();
        url.append(Configuration.getDomain()).append(JSON_SERVICE_INTERNALNEWS).append(Configuration.getSuffix());
        if (Session.currentEntity != null) {
            url.append("?newsId=").append(Session.currentEntity.getId());
        }
        ;
        return url.toString();
    }

    @Override
    protected void processData(String data) {
        try {
            JSONObject json = new JSONObject(data);
            JSONObject news = json.getJSONObject("news");
            final TextView name = (TextView) findViewById(R.id.internalNewsSubject);
            name.setText(news.getString("subject"));
            final TextView date = (TextView) findViewById(R.id.internalNewsDate);
            date.setText(news.getString("date"));
            final TextView content = (TextView) findViewById(R.id.internalNewsContent);
            Spanned html = Html.fromHtml(news.getString("content"));
            content.setText(html.toString());
        } catch (Exception e) {
            Log.e(tag, "error: " + e.getMessage());
        }
    }
}
