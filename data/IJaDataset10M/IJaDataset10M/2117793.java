package com.googlecode.tcime.unofficial;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Contains all candidates in pages where users could move forward (next page)
 * or move backward (previous) page to select one of these candidates. 
 */
public class CandidatesContainer extends LinearLayout {

    private static final int ARROW_ALPHA_ENABLED = 0xff;

    private static final int ARROW_ALPHA_DISABLED = 0x40;

    private static final int FLING_DISTANCE_THRESHOLD = 40;

    private CandidateView candidateView;

    private ImageButton leftArrow;

    private ImageButton rightArrow;

    private String words;

    private boolean highlightDefault = false;

    private int currentPage = 0;

    private int pageCount = 0;

    private GestureDetector gestureDetector = new GestureDetector(new OnGestureListener());

    public CandidatesContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        candidateView = (CandidateView) findViewById(R.id.candidate_view);
        leftArrow = (ImageButton) findViewById(R.id.arrow_left);
        leftArrow.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showPage(currentPage - 1);
            }
        });
        rightArrow = (ImageButton) findViewById(R.id.arrow_right);
        rightArrow.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showPage(currentPage + 1);
            }
        });
    }

    public void setCandidateViewListener(CandidateView.CandidateViewListener listener) {
        candidateView.setCandidateViewListener(listener);
    }

    public void setCandidates(String words, boolean highlightDefault) {
        this.words = words;
        this.highlightDefault = highlightDefault;
        pageCount = getPageCount();
        showPage(0);
    }

    public boolean pickHighlighted() {
        return candidateView.pickHighlighted();
    }

    private void showPage(int page) {
        if (isPageEmpty(page)) {
            candidateView.setCandidates("");
            enableArrow(leftArrow, false);
            enableArrow(rightArrow, false);
        } else {
            final int start = page * CandidateView.MAX_CANDIDATE_COUNT;
            final int end = start + Math.min(words.length() - start, CandidateView.MAX_CANDIDATE_COUNT);
            candidateView.setCandidates(words.substring(start, end));
            if (highlightDefault) {
                candidateView.highlightDefault();
            }
            highlightDefault = true;
            enableArrow(leftArrow, (page > 0) ? true : false);
            enableArrow(rightArrow, (page < pageCount - 1) ? true : false);
        }
        currentPage = page;
    }

    /**
   * Change to previous candidate page.
   * 
   * @return true if change page successfully
   */
    public boolean pagePrev() {
        int page = currentPage - 1;
        boolean result = !isPageEmpty(page);
        if (result) {
            showPage(page);
        }
        return result;
    }

    /**
   * Change to next candidate page.
   * 
   * @return true if change page successfully
   */
    public boolean pageNext() {
        int page = currentPage + 1;
        boolean result = !isPageEmpty(page);
        if (result) {
            showPage(page);
        }
        return result;
    }

    /**
   * Checks if it's an empty page holding no candidates.
   */
    private boolean isPageEmpty(int page) {
        if (page < 0 || page >= pageCount) {
            return true;
        }
        return false;
    }

    private int getPageCount() {
        return (int) Math.ceil((double) words.length() / CandidateView.MAX_CANDIDATE_COUNT);
    }

    private void enableArrow(ImageButton arrow, boolean enabled) {
        arrow.setEnabled(enabled);
        arrow.setAlpha(enabled ? ARROW_ALPHA_ENABLED : ARROW_ALPHA_DISABLED);
    }

    /**
   * A workaround to avoid the focused CandidateView handling onTouchEvent first.
   * We let Container to handle first.
   * 
   * @param event MotionEvent
   * @return true if handled
   */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        event.offsetLocation(-leftArrow.getMeasuredWidth(), 0);
        candidateView.onTouchEventReal(event);
        return true;
    }

    /**
   * A SimpleOnGestureListener that listens to the gesture of the user.
   */
    public class OnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getX() - e2.getX()) < FLING_DISTANCE_THRESHOLD) {
                return false;
            }
            if (e1.getX() > e2.getX()) {
                pageNext();
            } else {
                pagePrev();
            }
            return true;
        }
    }

    public void highlightLeft() {
        if (candidateView.highlightLeft() && pagePrev()) {
            candidateView.changeHighlight(CandidateView.MAX_CANDIDATE_COUNT - 1);
        }
    }

    public void highlightRight() {
        if (candidateView.highlightRight()) pageNext();
    }
}
