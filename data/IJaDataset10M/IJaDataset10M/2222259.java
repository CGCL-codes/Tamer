package com.samples.shapedrawable;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class ShapeDrawableActivity extends Activity {

    private static final int IDM_LINE = 101;

    private static final int IDM_OVAL = 102;

    private static final int IDM_RECT = 103;

    private static final int IDM_ROUNDRECT_FILL = 104;

    private static final int IDM_STAR = 105;

    private static final int IDM_ARC = 106;

    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mImage = (ImageView) findViewById(R.id.image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, IDM_LINE, Menu.NONE, "Line");
        menu.add(Menu.NONE, IDM_OVAL, Menu.NONE, "Oval");
        menu.add(Menu.NONE, IDM_RECT, Menu.NONE, "Rectangle");
        menu.add(Menu.NONE, IDM_ROUNDRECT_FILL, Menu.NONE, "Round Rect. Fill");
        menu.add(Menu.NONE, IDM_STAR, Menu.NONE, "Path");
        menu.add(Menu.NONE, IDM_ARC, Menu.NONE, "Arc");
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ShapeDrawable d = null;
        switch(item.getItemId()) {
            case IDM_LINE:
                d = new ShapeDrawable(new RectShape());
                d.setIntrinsicHeight(2);
                d.setIntrinsicWidth(150);
                d.getPaint().setColor(Color.MAGENTA);
                break;
            case IDM_OVAL:
                d = new ShapeDrawable(new OvalShape());
                d.setIntrinsicHeight(100);
                d.setIntrinsicWidth(150);
                d.getPaint().setColor(Color.RED);
                break;
            case IDM_RECT:
                d = new ShapeDrawable(new RectShape());
                d.setIntrinsicHeight(100);
                d.setIntrinsicWidth(150);
                d.getPaint().setColor(Color.BLUE);
                break;
            case IDM_ROUNDRECT_FILL:
                float[] outR = new float[] { 6, 6, 6, 6, 6, 6, 6, 6 };
                RectF rectF = new RectF(8, 8, 8, 8);
                float[] inR = new float[] { 6, 6, 6, 6, 6, 6, 6, 6 };
                d = new ShapeDrawable(new RoundRectShape(outR, rectF, inR));
                d.setIntrinsicHeight(100);
                d.setIntrinsicWidth(150);
                d.getPaint().setColor(Color.WHITE);
                break;
            case IDM_STAR:
                Path p = new Path();
                p.moveTo(50, 0);
                p.lineTo(25, 100);
                p.lineTo(100, 50);
                p.lineTo(0, 50);
                p.lineTo(75, 100);
                p.lineTo(50, 0);
                d = new ShapeDrawable(new PathShape(p, 100, 100));
                d.setIntrinsicHeight(100);
                d.setIntrinsicWidth(100);
                d.getPaint().setColor(Color.YELLOW);
                d.getPaint().setStyle(Paint.Style.STROKE);
                break;
            case IDM_ARC:
                d = new ShapeDrawable(new ArcShape(0, 255));
                d.setIntrinsicHeight(100);
                d.setIntrinsicWidth(100);
                d.getPaint().setColor(Color.YELLOW);
                break;
        }
        mImage.setBackgroundDrawable(d);
        return true;
    }
}
