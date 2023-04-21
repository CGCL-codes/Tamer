package org.doit.android.bobple.recommendation;

import org.doit.android.bobple.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class RecommendationSummaryActivity extends ListActivity {

    private TextView selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_summary);
        setListAdapter(new RecommnedArrayAdapter(this));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    }
}
