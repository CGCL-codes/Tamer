package com.ad_oss.merkat;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.ad_oss.merkat.IconDownloader.IconHolder;

public class MarketsAdapter extends BaseAdapter {

    private ArrayList<MarketData> mMarkets;

    private final LayoutInflater mInflater;

    private final int mIconSize;

    private final Context mContext;

    MarketsAdapter(Context context, LayoutInflater inflater) {
        mInflater = inflater;
        mContext = context;
        mIconSize = mContext.getResources().getDimensionPixelSize(android.R.dimen.app_icon_size);
    }

    public void updateList(JSONArray markets) throws JSONException {
        if (markets == null) mMarkets = new ArrayList<MarketData>(); else {
            mMarkets = new ArrayList<MarketData>(markets.length());
            Log.i(Main.TAG, "loaded " + markets.length());
            for (int i = 0; i < markets.length(); ++i) {
                mMarkets.add(new MarketData(markets.getJSONObject(i)));
            }
            notifyDataSetChanged();
        }
    }

    public int getCount() {
        return mMarkets.size();
    }

    public Object getItem(int index) {
        return mMarkets.get(index);
    }

    public long getItemId(int id) {
        return id;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View layout;
        ViewHolder holder;
        if (convertView != null) {
            layout = convertView;
            holder = (ViewHolder) layout.getTag();
        } else {
            layout = mInflater.inflate(R.layout.item_market, null);
            holder = new ViewHolder(mContext, layout, mIconSize);
            layout.setTag(holder);
        }
        final MarketData market = mMarkets.get(position);
        holder.setMarket(market);
        layout.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = MarketInfo.getIntent(mContext, market);
                mContext.startActivity(intent);
            }
        });
        TextView vt = (TextView) layout.findViewById(R.id.textName);
        vt.setText(market.name);
        TextView installed = (TextView) layout.findViewById(R.id.textInstalled);
        installed.setVisibility(market.isInstalled() ? View.VISIBLE : View.GONE);
        RatingBar rb = (RatingBar) layout.findViewById(R.id.ratingBar1);
        rb.setRating((float) market.rating);
        return layout;
    }

    private static class ViewHolder implements IconHolder {

        public ViewHolder(Context context, View layout, int iconSize) {
            mContext = context;
            mIconView = (ImageView) layout.findViewById(R.id.imageView1);
            mIconSize = iconSize;
        }

        public void setMarket(MarketData market) {
            Uri uri = Uri.parse(market.getIconURL(mIconSize));
            setIconUri(uri);
        }

        private void setIconUri(Uri uri) {
            if (uri.equals(mIconUri)) {
                if (downloader == null || downloader.isCancelled()) {
                    downloader = new IconDownloader(mContext, this, mIconSize);
                    mIconUri = uri;
                    downloader.execute(uri);
                }
            } else {
                if (downloader != null) downloader.cancel(false);
                downloader = new IconDownloader(mContext, this, mIconSize);
                mIconUri = uri;
                downloader.execute(uri);
            }
        }

        private final Context mContext;

        private final ImageView mIconView;

        private final int mIconSize;

        private IconDownloader downloader;

        private Uri mIconUri;

        public void postDownloadIcon(IconDownloader me, Bitmap bitmap) {
            if (bitmap == null) {
                mIconView.setImageResource(R.drawable.icon);
                mIconView.setTag(null);
            } else {
                mIconView.setImageBitmap(bitmap);
                mIconView.setTag(mIconUri);
            }
            downloader = null;
        }

        public void preDownloadIcon(IconDownloader me) {
            if (mIconView.getTag() == null || !mIconView.getTag().equals(mIconUri)) mIconView.setImageResource(R.drawable.icon);
        }
    }
}
