package org.androidsoft.games.puzzle.kids;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 *
 * @author pierre
 */
public class PuzzleView extends GridView {

    private static final int MARGIN = 5;

    private Puzzle mPuzzle;

    private Context mContext;

    public PuzzleView(Context context) {
        super(context);
        mContext = context;
        setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mPuzzle.onPosition(position);
            }
        });
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mPuzzle.onPosition(position);
            }
        });
    }

    public PuzzleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mPuzzle.onPosition(position);
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        update();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        if (h < w) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }

    void update() {
        setAdapter(new ImageAdapter(mContext, getWidth(), getHeight(), MARGIN, mPuzzle));
    }

    void setPuzzle(Puzzle puzzle) {
        mPuzzle = puzzle;
    }
}
