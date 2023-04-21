package harris.GiantBomb;

import java.io.IOException;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BombcastListAdapter extends ArrayAdapter<String> {

    ArrayList<News> news = new ArrayList<News>();

    @SuppressWarnings("unchecked")
    public BombcastListAdapter(Context context, int textViewResourceId, ArrayList<News> news) throws OptionalDataException, ClassNotFoundException, IOException {
        super(context, textViewResourceId, (List) news);
        this.news = news;
    }

    public View getView(int i, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.bombcastrow, null);
        }
        final TextView title = (TextView) v.findViewById(R.id.newstitle);
        final TextView desc = (TextView) v.findViewById(R.id.newsdesc);
        title.setText(news.get(i).getTitle());
        desc.setText(news.get(i).getContent());
        return v;
    }
}
