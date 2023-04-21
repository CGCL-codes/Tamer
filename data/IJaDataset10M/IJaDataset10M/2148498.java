package name.vampidroid;

import name.vampidroid.DatabaseHelper.CardType;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.util.Log;

public class VampiDroidSearch extends VampiDroidBase {

    private String mSearchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) mSearchQuery = savedInstanceState.getString("searchquery"); else mSearchQuery = getSearchQuery();
        super.onCreate(savedInstanceState);
        setTitle("Search Results for: " + mSearchQuery);
    }

    private String getSearchQuery() {
        String query = formatQueryString(getIntent().getStringExtra(SearchManager.QUERY)).trim().toLowerCase();
        VampidroidSuggestionProvider.getBridge(this.getApplicationContext()).saveRecentQuery(query, null);
        return query;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("searchquery", mSearchQuery);
    }

    @Override
    protected String getLibraryQuery() {
        return DatabaseHelper.ALL_FROM_LIBRARY_QUERY + " and (lower(CardText) like '%?%') ".replace("?", mSearchQuery);
    }

    @Override
    protected String getCryptQuery() {
        return DatabaseHelper.ALL_FROM_CRYPT_QUERY + " and (lower(CardText) like '%?%') ".replace("?", mSearchQuery);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("vampidroid", "VampiDroidSearch.onNewintent");
        super.onNewIntent(intent);
        setIntent(intent);
        mSearchQuery = getSearchQuery();
        setTitle("Search Results for: " + mSearchQuery);
        mVampidroidFragment.getCryptListFragment().setQuery(getCryptQuery());
        mVampidroidFragment.getLibraryListFragment().setQuery(getLibraryQuery());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("vampidroid", "vampidroidsearch.onstart");
        CardListCursorAdapter adapter = new CardListCursorAdapter(CardType.CRYPT, this, R.layout.cryptlistitem, null, DatabaseHelper.STRING_ARRAY_CRYPT_LIST_COLUMNS, new int[] { R.id.txtCardName, R.id.txtCardExtraInformation, R.id.txtCardCost, R.id.txtCardInitialText, R.id.txtCardGroup });
        mVampidroidFragment.getCryptListFragment().setListAdapter(adapter);
        mVampidroidFragment.getCryptListFragment().setQuery(getCryptQuery());
        mVampidroidFragment.getCryptListFragment().setOrderBy(DatabaseHelper.ORDER_BY_NAME);
        adapter = new CardListCursorAdapter(CardType.LIBRARY, this, R.layout.librarylistitem, null, new String[] { "Name", "Type", "Clan", "Discipline" }, new int[] { R.id.txtCardName, R.id.txtCardType, R.id.txtCardClan, R.id.txtCardDiscipline });
        mVampidroidFragment.getLibraryListFragment().setListAdapter(adapter);
        mVampidroidFragment.getLibraryListFragment().setQuery(getLibraryQuery());
        mVampidroidFragment.getLibraryListFragment().setOrderBy(DatabaseHelper.ORDER_BY_NAME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_search:
                onSearchRequested();
                return true;
            case android.R.id.home:
                Intent intent = new Intent(this, VampiDroid.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }
}
