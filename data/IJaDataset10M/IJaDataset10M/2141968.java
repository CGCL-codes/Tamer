package com.samples.gridviewimage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private static final Integer[] mImages = { R.drawable.photo1, R.drawable.photo2, R.drawable.photo3, R.drawable.photo4, R.drawable.photo5, R.drawable.photo6, R.drawable.photo7, R.drawable.photo8, R.drawable.photo1, R.drawable.photo2, R.drawable.photo3, R.drawable.photo4, R.drawable.photo5, R.drawable.photo6, R.drawable.photo7, R.drawable.photo8, R.drawable.photo1, R.drawable.photo2, R.drawable.photo3, R.drawable.photo4, R.drawable.photo5, R.drawable.photo6, R.drawable.photo7, R.drawable.photo8 };

    public ImageAdapter(Context context) {
        mContext = context;
    }

    public int getCount() {
        return mImages.length;
    }

    public Object getItem(int position) {
        return mImages[position];
    }

    public long getItemId(int position) {
        return mImages[position];
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view;
        if (convertView == null) {
            view = new ImageView(mContext);
            view.setLayoutParams(new GridView.LayoutParams(85, 85));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setPadding(2, 2, 2, 2);
        } else {
            view = (ImageView) convertView;
        }
        view.setImageResource(mImages[position]);
        return view;
    }
}
