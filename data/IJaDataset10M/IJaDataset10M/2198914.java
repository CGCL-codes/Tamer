package com.google.zxing.client.android.book;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.zxing.client.android.AndroidHttpClient;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.R;

/**
 * Uses Google Book Search to find a word or phrase in the requested book.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class SearchBookContentsActivity extends Activity {

    private static final String TAG = SearchBookContentsActivity.class.getSimpleName();

    private static final String USER_AGENT = "ZXing (Android)";

    private static final Pattern TAG_PATTERN = Pattern.compile("\\<.*?\\>");

    private static final Pattern LT_ENTITY_PATTERN = Pattern.compile("&lt;");

    private static final Pattern GT_ENTITY_PATTERN = Pattern.compile("&gt;");

    private static final Pattern QUOTE_ENTITY_PATTERN = Pattern.compile("&#39;");

    private static final Pattern QUOT_ENTITY_PATTERN = Pattern.compile("&quot;");

    private NetworkThread networkThread;

    private String isbn;

    private EditText queryTextView;

    private Button queryButton;

    private ListView resultListView;

    private TextView headerView;

    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            switch(message.what) {
                case R.id.search_book_contents_succeeded:
                    handleSearchResults((JSONObject) message.obj);
                    resetForNewQuery();
                    break;
                case R.id.search_book_contents_failed:
                    resetForNewQuery();
                    headerView.setText(R.string.msg_sbc_failed);
                    break;
            }
        }
    };

    private final Button.OnClickListener buttonListener = new Button.OnClickListener() {

        public void onClick(View view) {
            launchSearch();
        }
    };

    private final View.OnKeyListener keyListener = new View.OnKeyListener() {

        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                launchSearch();
                return true;
            }
            return false;
        }
    };

    String getISBN() {
        return isbn;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().removeExpiredCookie();
        Intent intent = getIntent();
        if (intent == null || (!intent.getAction().equals(Intents.SearchBookContents.ACTION))) {
            finish();
            return;
        }
        isbn = intent.getStringExtra(Intents.SearchBookContents.ISBN);
        if (isbn.startsWith("http://google.com/books?id=")) {
            setTitle(getString(R.string.sbc_name));
        } else {
            setTitle(getString(R.string.sbc_name) + ": ISBN " + isbn);
        }
        setContentView(R.layout.search_book_contents);
        queryTextView = (EditText) findViewById(R.id.query_text_view);
        String initialQuery = intent.getStringExtra(Intents.SearchBookContents.QUERY);
        if (initialQuery != null && initialQuery.length() > 0) {
            queryTextView.setText(initialQuery);
        }
        queryTextView.setOnKeyListener(keyListener);
        queryButton = (Button) findViewById(R.id.query_button);
        queryButton.setOnClickListener(buttonListener);
        resultListView = (ListView) findViewById(R.id.result_list_view);
        LayoutInflater factory = LayoutInflater.from(this);
        headerView = (TextView) factory.inflate(R.layout.search_book_contents_header, resultListView, false);
        resultListView.addHeaderView(headerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryTextView.selectAll();
    }

    private void resetForNewQuery() {
        networkThread = null;
        queryTextView.setEnabled(true);
        queryTextView.selectAll();
        queryButton.setEnabled(true);
    }

    private void launchSearch() {
        if (networkThread == null) {
            String query = queryTextView.getText().toString();
            if (query != null && query.length() > 0) {
                networkThread = new NetworkThread(isbn, query, handler);
                networkThread.start();
                headerView.setText(R.string.msg_sbc_searching_book);
                resultListView.setAdapter(null);
                queryTextView.setEnabled(false);
                queryButton.setEnabled(false);
            }
        }
    }

    private void handleSearchResults(JSONObject json) {
        try {
            int count = json.getInt("number_of_results");
            headerView.setText("Found " + (count == 1 ? "1 result" : count + " results"));
            if (count > 0) {
                JSONArray results = json.getJSONArray("search_results");
                SearchBookContentsResult.setQuery(queryTextView.getText().toString());
                List<SearchBookContentsResult> items = new ArrayList<SearchBookContentsResult>(count);
                for (int x = 0; x < count; x++) {
                    items.add(parseResult(results.getJSONObject(x)));
                }
                resultListView.setOnItemClickListener(new BrowseBookListener(this, items));
                resultListView.setAdapter(new SearchBookContentsAdapter(this, items));
            } else {
                String searchable = json.optString("searchable");
                if ("false".equals(searchable)) {
                    headerView.setText(R.string.msg_sbc_book_not_searchable);
                }
                resultListView.setAdapter(null);
            }
        } catch (JSONException e) {
            Log.w(TAG, "Bad JSON from book search", e);
            resultListView.setAdapter(null);
            headerView.setText(R.string.msg_sbc_failed);
        }
    }

    private SearchBookContentsResult parseResult(JSONObject json) {
        try {
            String pageId = json.getString("page_id");
            String pageNumber = json.getString("page_number");
            if (pageNumber.length() > 0) {
                pageNumber = getString(R.string.msg_sbc_page) + ' ' + pageNumber;
            } else {
                pageNumber = getString(R.string.msg_sbc_unknown_page);
            }
            String snippet = json.optString("snippet_text");
            boolean valid = true;
            if (snippet.length() > 0) {
                snippet = TAG_PATTERN.matcher(snippet).replaceAll("");
                snippet = LT_ENTITY_PATTERN.matcher(snippet).replaceAll("<");
                snippet = GT_ENTITY_PATTERN.matcher(snippet).replaceAll(">");
                snippet = QUOTE_ENTITY_PATTERN.matcher(snippet).replaceAll("'");
                snippet = QUOT_ENTITY_PATTERN.matcher(snippet).replaceAll("\"");
            } else {
                snippet = '(' + getString(R.string.msg_sbc_snippet_unavailable) + ')';
                valid = false;
            }
            return new SearchBookContentsResult(pageId, pageNumber, snippet, valid);
        } catch (JSONException e) {
            return new SearchBookContentsResult(getString(R.string.msg_sbc_no_page_returned), "", "", false);
        }
    }

    private static final class NetworkThread extends Thread {

        private final String isbn;

        private final String query;

        private final Handler handler;

        NetworkThread(String isbn, String query, Handler handler) {
            this.isbn = isbn;
            this.query = query;
            this.handler = handler;
        }

        @Override
        public void run() {
            AndroidHttpClient client = null;
            try {
                URI uri;
                if (isbn.startsWith("http://google.com/books?id=")) {
                    int equals = isbn.indexOf('=');
                    String volumeId = isbn.substring(equals + 1);
                    uri = new URI("http", null, "www.google.com", -1, "/books", "id=" + volumeId + "&jscmd=SearchWithinVolume2&q=" + query, null);
                } else {
                    uri = new URI("http", null, "www.google.com", -1, "/books", "vid=isbn" + isbn + "&jscmd=SearchWithinVolume2&q=" + query, null);
                }
                HttpUriRequest get = new HttpGet(uri);
                get.setHeader("cookie", getCookie(uri.toString()));
                client = AndroidHttpClient.newInstance(USER_AGENT);
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    ByteArrayOutputStream jsonHolder = new ByteArrayOutputStream();
                    entity.writeTo(jsonHolder);
                    jsonHolder.flush();
                    JSONObject json = new JSONObject(jsonHolder.toString(getEncoding(entity)));
                    jsonHolder.close();
                    Message message = Message.obtain(handler, R.id.search_book_contents_succeeded);
                    message.obj = json;
                    message.sendToTarget();
                } else {
                    Log.w(TAG, "HTTP returned " + response.getStatusLine().getStatusCode() + " for " + uri);
                    Message message = Message.obtain(handler, R.id.search_book_contents_failed);
                    message.sendToTarget();
                }
            } catch (Exception e) {
                Log.w(TAG, "Error accessing book search", e);
                Message message = Message.obtain(handler, R.id.search_book_contents_failed);
                message.sendToTarget();
            } finally {
                if (client != null) {
                    client.close();
                }
            }
        }

        private static String getCookie(String url) {
            String cookie = CookieManager.getInstance().getCookie(url);
            if (cookie == null || cookie.length() == 0) {
                Log.d(TAG, "Book Search cookie was missing or expired");
                HttpUriRequest head = new HttpHead(url);
                AndroidHttpClient client = AndroidHttpClient.newInstance(USER_AGENT);
                try {
                    HttpResponse response = client.execute(head);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        Header[] cookies = response.getHeaders("set-cookie");
                        for (Header theCookie : cookies) {
                            CookieManager.getInstance().setCookie(url, theCookie.getValue());
                        }
                        CookieSyncManager.getInstance().sync();
                        cookie = CookieManager.getInstance().getCookie(url);
                    }
                } catch (IOException e) {
                    Log.w(TAG, "Error setting book search cookie", e);
                }
                client.close();
            }
            return cookie;
        }

        private static String getEncoding(HttpEntity entity) {
            return "windows-1252";
        }
    }
}
