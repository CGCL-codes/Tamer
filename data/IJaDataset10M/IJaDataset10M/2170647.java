package com.wubotao;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class CoverFlowApp extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoverFlow coverFlow;
        coverFlow = new CoverFlow(this);
        coverFlow.setAdapter(new ImageAdapter(this));
        ImageAdapter coverImageAdapter = new ImageAdapter(this);
        coverFlow.setAdapter(coverImageAdapter);
        coverFlow.setSpacing(-25);
        coverFlow.setSelection(4, true);
        coverFlow.setAnimationDuration(1000);
        setContentView(coverFlow);
    }

    public class ImageAdapter extends BaseAdapter {

        int mGalleryItemBackground;

        private Context mContext;

        private Integer[] mImageIds = { R.drawable.card_big_0, R.drawable.card_big_1, R.drawable.card_big_2, R.drawable.card_big_3, R.drawable.card_big_4 };

        private ImageView[] mImages;

        public ImageAdapter(Context c) {
            mContext = c;
            mImages = new ImageView[mImageIds.length];
        }

        public boolean createReflectedImages() {
            final int reflectionGap = 4;
            int index = 0;
            for (int imageId : mImageIds) {
                Bitmap originalImage = BitmapFactory.decodeResource(getResources(), imageId);
                int width = originalImage.getWidth();
                int height = originalImage.getHeight();
                Matrix matrix = new Matrix();
                matrix.preScale(1, -1);
                Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false);
                Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmapWithReflection);
                canvas.drawBitmap(originalImage, 0, 0, null);
                Paint deafaultPaint = new Paint();
                canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
                canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
                Paint paint = new Paint();
                LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
                paint.setShader(shader);
                paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
                canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
                ImageView imageView = new ImageView(mContext);
                imageView.setImageBitmap(bitmapWithReflection);
                imageView.setLayoutParams(new CoverFlow.LayoutParams(120, 180));
                imageView.setScaleType(ScaleType.MATRIX);
                mImages[index++] = imageView;
            }
            return true;
        }

        public int getCount() {
            return mImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);
            i.setImageResource(mImageIds[position]);
            i.setLayoutParams(new CoverFlow.LayoutParams(130, 130));
            i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
            drawable.setAntiAlias(true);
            return i;
        }

        /**
		 * Returns the size (0.0f to 1.0f) of the views depending on the
		 * 'offset' to the center.
		 */
        public float getScale(boolean focused, int offset) {
            return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
        }
    }
}
