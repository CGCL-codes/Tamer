package org.springframework.android.reader;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.feed.SyndFeedHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;

/**
 * @author Roy Clarkson
 * @author Helena Edelson
 * @author Pierre-Yves Ricau
 */
public class RssSyndFeedActivity extends AbstractAsyncListActivity {

    protected static final String TAG = RssSyndFeedActivity.class.getSimpleName();

    private SyndFeed feed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        new DownloadRssFeedTask().execute();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (feed == null) {
            return;
        }
        SyndEntry entry = (SyndEntry) feed.getEntries().get(position);
        String link = entry.getLink();
        Log.i(TAG, link);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(link));
        this.startActivity(intent);
    }

    private void refreshRssFeed(SyndFeed feed) {
        this.feed = feed;
        if (feed == null) {
            return;
        }
        setTitle(feed.getTitle());
        SyndFeedListAdapter adapter = new SyndFeedListAdapter(this, feed);
        setListAdapter(adapter);
    }

    private class DownloadRssFeedTask extends AsyncTask<Void, Void, SyndFeed> {

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();
        }

        @Override
        protected SyndFeed doInBackground(Void... params) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                SyndFeedHttpMessageConverter converter = new SyndFeedHttpMessageConverter();
                List<MediaType> mediaTypes = new ArrayList<MediaType>();
                mediaTypes.add(MediaType.TEXT_XML);
                converter.setSupportedMediaTypes(mediaTypes);
                List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
                messageConverters.add(converter);
                restTemplate.setMessageConverters(messageConverters);
                final String url = getString(R.string.rss_feed_url);
                return restTemplate.getForObject(url, SyndFeed.class);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(SyndFeed feed) {
            dismissProgressDialog();
            refreshRssFeed(feed);
        }
    }
}
