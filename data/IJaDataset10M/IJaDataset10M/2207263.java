package criticker.client.adaptor;

import java.util.ArrayList;
import criticker.client.R;
import criticker.lib.Const;
import criticker.lib.FilmData;
import criticker.lib.DataStore;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewReleaseAdaptor extends BaseAdapter {

    private ArrayList<Object> mElements = null;

    private Context mContext = null;

    public NewReleaseAdaptor(Context context, ArrayList<Object> elements) {
        mContext = context;
        mElements = elements;
    }

    public int getCount() {
        if (mElements == null) return 0;
        return mElements.size();
    }

    public Object getItem(int pos) {
        if (mElements == null || mElements.size() <= pos) return null;
        return mElements.get(pos);
    }

    public long getItemId(int pos) {
        return pos;
    }

    public View getView(int pos, View convertView, ViewGroup parent) {
        final int MAX_NAME_LEN = 15;
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.newreleasesgriditem, parent, false);
        } else {
            view = convertView;
        }
        String filmId = ((DataStore) mElements.get(pos)).GetDataString(Const.FILM_ID);
        String data = ((DataStore) mElements.get(pos)).GetDataString(Const.FILM_NAME);
        if (data.length() > MAX_NAME_LEN) {
            data = data.substring(0, MAX_NAME_LEN) + "...";
        }
        TextView title = (TextView) view.findViewById(R.id.newreleasesgridui_griditem_text);
        title.setText(data);
        ImageView image = (ImageView) view.findViewById(R.id.newreleasesgridui_griditem_image);
        Bitmap img = (Bitmap) FilmData.GetInstance().GetData(filmId, Const.FILM_IMAGE);
        if (img != null) {
            image.setImageBitmap(img);
        } else {
            image.setImageResource(R.drawable.nopic_film);
        }
        return view;
    }
}
