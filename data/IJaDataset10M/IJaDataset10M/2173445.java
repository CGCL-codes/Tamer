package net.yama.android.views.adapter;

import net.yama.android.response.Photo;
import net.yama.android.util.DrawableManager;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter {

    Photo data;

    Context context;

    public GalleryAdapter(Photo data, Context ctx) {
        super();
        this.data = data;
        this.context = ctx;
    }

    public int getCount() {
        return data.getPhotoUrls().size();
    }

    public Object getItem(int position) {
        return data.getPhotoUrls().get(position);
    }

    public long getItemId(int position) {
        String photoUrl = data.getPhotoUrls().get(position);
        int start = photoUrl.lastIndexOf("_");
        int end = photoUrl.lastIndexOf(".");
        String idStr = photoUrl.substring((start + 1), end);
        return Long.valueOf(idStr);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i = new ImageView(context);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setPadding(2, 2, 2, 2);
        DrawableManager.instance.fetchDrawableOnThread(data.getPhotoUrls().get(position), i);
        return i;
    }
}
