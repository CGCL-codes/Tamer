package de.tudresden.inf.rn.mobilis.commons.views;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.tudresden.inf.rn.mobilis.media.R;

public class PairListAdapter<O, T> extends BaseAdapter {

    public static class Pair<O, T> {

        public O one;

        public T two;

        public Pair(O one, T two) {
            this.one = one;
            this.two = two;
        }
    }

    private static class ViewHolder {

        public TextView text1;

        public TextView text2;
    }

    private List<Pair<O, T>> backingList = new ArrayList<Pair<O, T>>();

    private Context context;

    public PairListAdapter(Context c) {
        this.context = c;
    }

    public void addPair(O one, T two) {
        this.backingList.add(new Pair<O, T>(one, two));
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return this.backingList.size();
    }

    public Pair<O, T> getItem(int position) {
        return this.backingList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View target, ViewGroup parent) {
        ViewHolder holder;
        if (target == null) {
            target = View.inflate(this.context, R.layout.list_hashmap, null);
            holder = new ViewHolder();
            holder.text1 = (TextView) ((ViewGroup) target).findViewById(android.R.id.text1);
            holder.text2 = (TextView) ((ViewGroup) target).findViewById(android.R.id.text2);
            target.setTag(holder);
        } else holder = (ViewHolder) target.getTag();
        holder.text1.setText(this.getItem(position).one.toString());
        holder.text2.setText(this.getItem(position).two.toString());
        return target;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
