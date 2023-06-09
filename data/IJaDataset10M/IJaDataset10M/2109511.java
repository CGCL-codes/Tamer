package android.widget;

import static android.util.Log.d;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pool;
import android.util.Poolable;
import android.util.PoolableManager;
import android.util.Pools;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

/**
 * A Layout where the positions of the children can be described in relation to
 * each other or to the parent. For the sake of efficiency, the relations
 * between views are evaluated in one pass, so if view Y is dependent on the
 * position of view X, make sure the view X comes first in the layout.
 * 
 * <p>
 * Note that you cannot have a circular dependency between the size of the
 * RelativeLayout and the position of its children. For example, you cannot have
 * a RelativeLayout whose height is set to
 * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT WRAP_CONTENT} and a
 * child set to {@link #ALIGN_PARENT_BOTTOM}.
 * </p>
 * 
 * <p>
 * Also see {@link android.widget.RelativeLayout.LayoutParams
 * RelativeLayout.LayoutParams} for layout attributes
 * </p>
 * 
 * @attr ref android.R.styleable#RelativeLayout_gravity
 * @attr ref android.R.styleable#RelativeLayout_ignoreGravity
 */
public class RelativeLayout extends ViewGroup {

    private static final String LOG_TAG = "RelativeLayout";

    private static final boolean DEBUG_GRAPH = false;

    public static final int TRUE = -1;

    /**
     * Rule that aligns a child's right edge with another child's left edge.
     */
    public static final int LEFT_OF = 0;

    /**
     * Rule that aligns a child's left edge with another child's right edge.
     */
    public static final int RIGHT_OF = 1;

    /**
     * Rule that aligns a child's bottom edge with another child's top edge.
     */
    public static final int ABOVE = 2;

    /**
     * Rule that aligns a child's top edge with another child's bottom edge.
     */
    public static final int BELOW = 3;

    /**
     * Rule that aligns a child's baseline with another child's baseline.
     */
    public static final int ALIGN_BASELINE = 4;

    /**
     * Rule that aligns a child's left edge with another child's left edge.
     */
    public static final int ALIGN_LEFT = 5;

    /**
     * Rule that aligns a child's top edge with another child's top edge.
     */
    public static final int ALIGN_TOP = 6;

    /**
     * Rule that aligns a child's right edge with another child's right edge.
     */
    public static final int ALIGN_RIGHT = 7;

    /**
     * Rule that aligns a child's bottom edge with another child's bottom edge.
     */
    public static final int ALIGN_BOTTOM = 8;

    /**
     * Rule that aligns the child's left edge with its RelativeLayout parent's
     * left edge.
     */
    public static final int ALIGN_PARENT_LEFT = 9;

    /**
     * Rule that aligns the child's top edge with its RelativeLayout parent's
     * top edge.
     */
    public static final int ALIGN_PARENT_TOP = 10;

    /**
     * Rule that aligns the child's right edge with its RelativeLayout parent's
     * right edge.
     */
    public static final int ALIGN_PARENT_RIGHT = 11;

    /**
     * Rule that aligns the child's bottom edge with its RelativeLayout parent's
     * bottom edge.
     */
    public static final int ALIGN_PARENT_BOTTOM = 12;

    /**
     * Rule that centers the child with respect to the bounds of its
     * RelativeLayout parent.
     */
    public static final int CENTER_IN_PARENT = 13;

    /**
     * Rule that centers the child horizontally with respect to the bounds of
     * its RelativeLayout parent.
     */
    public static final int CENTER_HORIZONTAL = 14;

    /**
     * Rule that centers the child vertically with respect to the bounds of its
     * RelativeLayout parent.
     */
    public static final int CENTER_VERTICAL = 15;

    private static final int VERB_COUNT = 16;

    private View mBaselineView = null;

    private boolean mHasBaselineAlignedChild;

    private int mGravity = Gravity.LEFT | Gravity.TOP;

    private final Rect mContentBounds = new Rect();

    private final Rect mSelfBounds = new Rect();

    private int mIgnoreGravity;

    private boolean mDirtyHierarchy = true;

    private View[] mSortedHorizontalChildren = new View[0];

    private View[] mSortedVerticalChildren = new View[0];

    private final DependencyGraph mGraph = new DependencyGraph();

    public RelativeLayout(Context context) {
        super(context);
        initRelativeLayout(context, null);
    }

    public RelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRelativeLayout(context, attrs);
    }

    private void initRelativeLayout(Context c, AttributeSet attrs) {
        if (attrs != null && attrs.getAttributeCount() > 0) {
            parseRelativeLayoutAttributes(attrs);
        }
    }

    /**
     * Defines which View is ignored when the gravity is applied. This setting
     * has no effect if the gravity is <code>Gravity.LEFT | Gravity.TOP</code>.
     * 
     * @param viewId
     *            The id of the View to be ignored by gravity, or 0 if no View
     *            should be ignored.
     * 
     * @see #setGravity(int)
     * 
     * @attr ref android.R.styleable#RelativeLayout_ignoreGravity
     */
    public void setIgnoreGravity(int viewId) {
        mIgnoreGravity = viewId;
    }

    /**
     * Describes how the child views are positioned. Defaults to
     * <code>Gravity.LEFT | Gravity.TOP</code>.
     * 
     * @param gravity
     *            See {@link android.view.Gravity}
     * 
     * @see #setHorizontalGravity(int)
     * @see #setVerticalGravity(int)
     * 
     * @attr ref android.R.styleable#RelativeLayout_gravity
     */
    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == 0) {
                gravity |= Gravity.LEFT;
            }
            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == 0) {
                gravity |= Gravity.TOP;
            }
            mGravity = gravity;
            requestLayout();
        }
    }

    public void setHorizontalGravity(int horizontalGravity) {
        final int gravity = horizontalGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        if ((mGravity & Gravity.HORIZONTAL_GRAVITY_MASK) != gravity) {
            mGravity = (mGravity & ~Gravity.HORIZONTAL_GRAVITY_MASK) | gravity;
            requestLayout();
        }
    }

    public void setVerticalGravity(int verticalGravity) {
        final int gravity = verticalGravity & Gravity.VERTICAL_GRAVITY_MASK;
        if ((mGravity & Gravity.VERTICAL_GRAVITY_MASK) != gravity) {
            mGravity = (mGravity & ~Gravity.VERTICAL_GRAVITY_MASK) | gravity;
            requestLayout();
        }
    }

    @Override
    public int getBaseline() {
        return mBaselineView != null ? mBaselineView.getBaseline() : super.getBaseline();
    }

    @Override
    public void requestLayout() {
        mDirtyHierarchy = true;
        super.requestLayout();
    }

    private void sortChildren() {
        int count = getChildCount();
        if (mSortedVerticalChildren.length != count) mSortedVerticalChildren = new View[count];
        if (mSortedHorizontalChildren.length != count) mSortedHorizontalChildren = new View[count];
        final DependencyGraph graph = mGraph;
        graph.clear();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            graph.add(child);
        }
        if (DEBUG_GRAPH) {
            d(LOG_TAG, "=== Sorted vertical children");
            graph.log(getResources(), ABOVE, BELOW, ALIGN_BASELINE, ALIGN_TOP, ALIGN_BOTTOM);
            d(LOG_TAG, "=== Sorted horizontal children");
            graph.log(getResources(), LEFT_OF, RIGHT_OF, ALIGN_LEFT, ALIGN_RIGHT);
        }
        graph.getSortedViews(mSortedVerticalChildren, ABOVE, BELOW, ALIGN_BASELINE, ALIGN_TOP, ALIGN_BOTTOM);
        graph.getSortedViews(mSortedHorizontalChildren, LEFT_OF, RIGHT_OF, ALIGN_LEFT, ALIGN_RIGHT);
        if (DEBUG_GRAPH) {
            d(LOG_TAG, "=== Ordered list of vertical children");
            for (View view : mSortedVerticalChildren) {
                DependencyGraph.printViewId(getResources(), view);
            }
            d(LOG_TAG, "=== Ordered list of horizontal children");
            for (View view : mSortedHorizontalChildren) {
                DependencyGraph.printViewId(getResources(), view);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mDirtyHierarchy) {
            mDirtyHierarchy = false;
            sortChildren();
        }
        int myWidth = -1;
        int myHeight = -1;
        int width = 0;
        int height = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode != MeasureSpec.UNSPECIFIED) {
            myWidth = widthSize;
        }
        if (heightMode != MeasureSpec.UNSPECIFIED) {
            myHeight = heightSize;
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            width = myWidth;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = myHeight;
        }
        mHasBaselineAlignedChild = false;
        View ignore = null;
        int gravity = mGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        final boolean horizontalGravity = gravity != Gravity.LEFT && gravity != 0;
        gravity = mGravity & Gravity.VERTICAL_GRAVITY_MASK;
        final boolean verticalGravity = gravity != Gravity.TOP && gravity != 0;
        int left = Integer.MAX_VALUE;
        int top = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;
        int bottom = Integer.MIN_VALUE;
        boolean offsetHorizontalAxis = false;
        boolean offsetVerticalAxis = false;
        if ((horizontalGravity || verticalGravity) && mIgnoreGravity != View.NO_ID) {
            ignore = findViewById(mIgnoreGravity);
        }
        final boolean isWrapContentWidth = widthMode != MeasureSpec.EXACTLY;
        final boolean isWrapContentHeight = heightMode != MeasureSpec.EXACTLY;
        View[] views = mSortedHorizontalChildren;
        int count = views.length;
        for (int i = 0; i < count; i++) {
            View child = views[i];
            if (child.getVisibility() != GONE) {
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                applyHorizontalSizeRules(params, myWidth);
                measureChildHorizontal(child, params, myWidth, myHeight);
                if (positionChildHorizontal(child, params, myWidth, isWrapContentWidth)) {
                    offsetHorizontalAxis = true;
                }
            }
        }
        views = mSortedVerticalChildren;
        count = views.length;
        for (int i = 0; i < count; i++) {
            View child = views[i];
            if (child.getVisibility() != GONE) {
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                applyVerticalSizeRules(params, myHeight);
                measureChild(child, params, myWidth, myHeight);
                if (positionChildVertical(child, params, myHeight, isWrapContentHeight)) {
                    offsetVerticalAxis = true;
                }
                if (isWrapContentWidth) {
                    width = Math.max(width, params.mRight);
                }
                if (isWrapContentHeight) {
                    height = Math.max(height, params.mBottom);
                }
                if (child != ignore || verticalGravity) {
                    left = Math.min(left, params.mLeft - params.leftMargin);
                    top = Math.min(top, params.mTop - params.topMargin);
                }
                if (child != ignore || horizontalGravity) {
                    right = Math.max(right, params.mRight + params.rightMargin);
                    bottom = Math.max(bottom, params.mBottom + params.bottomMargin);
                }
            }
        }
        if (mHasBaselineAlignedChild) {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
                    LayoutParams params = (LayoutParams) child.getLayoutParams();
                    alignBaseline(child, params);
                    if (child != ignore || verticalGravity) {
                        left = Math.min(left, params.mLeft - params.leftMargin);
                        top = Math.min(top, params.mTop - params.topMargin);
                    }
                    if (child != ignore || horizontalGravity) {
                        right = Math.max(right, params.mRight + params.rightMargin);
                        bottom = Math.max(bottom, params.mBottom + params.bottomMargin);
                    }
                }
            }
        }
        if (isWrapContentWidth) {
            width += paddingRight;
            if (layoutParams.width >= 0) {
                width = Math.max(width, layoutParams.width);
            }
            width = Math.max(width, getSuggestedMinimumWidth());
            width = resolveSize(width, widthMeasureSpec);
            if (offsetHorizontalAxis) {
                for (int i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    if (child.getVisibility() != GONE) {
                        LayoutParams params = (LayoutParams) child.getLayoutParams();
                        final int[] rules = params.getRules();
                        if (rules[CENTER_IN_PARENT] != 0 || rules[CENTER_HORIZONTAL] != 0) {
                            centerHorizontal(child, params, width);
                        }
                    }
                }
            }
        }
        if (isWrapContentHeight) {
            height += paddingBottom;
            if (layoutParams.height >= 0) {
                height = Math.max(height, layoutParams.height);
            }
            height = Math.max(height, getSuggestedMinimumHeight());
            height = resolveSize(height, heightMeasureSpec);
            if (offsetVerticalAxis) {
                for (int i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    if (child.getVisibility() != GONE) {
                        LayoutParams params = (LayoutParams) child.getLayoutParams();
                        final int[] rules = params.getRules();
                        if (rules[CENTER_IN_PARENT] != 0 || rules[CENTER_VERTICAL] != 0) {
                            centerVertical(child, params, height);
                        }
                    }
                }
            }
        }
        if (horizontalGravity || verticalGravity) {
            final Rect selfBounds = mSelfBounds;
            selfBounds.set(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
            final Rect contentBounds = mContentBounds;
            Gravity.apply(mGravity, right - left, bottom - top, selfBounds, contentBounds);
            final int horizontalOffset = contentBounds.left - left;
            final int verticalOffset = contentBounds.top - top;
            if (horizontalOffset != 0 || verticalOffset != 0) {
                for (int i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    if (child.getVisibility() != GONE && child != ignore) {
                        LayoutParams params = (LayoutParams) child.getLayoutParams();
                        if (horizontalGravity) {
                            params.mLeft += horizontalOffset;
                            params.mRight += horizontalOffset;
                        }
                        if (verticalGravity) {
                            params.mTop += verticalOffset;
                            params.mBottom += verticalOffset;
                        }
                    }
                }
            }
        }
        setMeasuredDimension(width, height);
    }

    private void alignBaseline(View child, LayoutParams params) {
        int[] rules = params.getRules();
        int anchorBaseline = getRelatedViewBaseline(rules, ALIGN_BASELINE);
        if (anchorBaseline != -1) {
            LayoutParams anchorParams = getRelatedViewParams(rules, ALIGN_BASELINE);
            if (anchorParams != null) {
                int offset = anchorParams.mTop + anchorBaseline;
                int baseline = child.getBaseline();
                if (baseline != -1) {
                    offset -= baseline;
                }
                int height = params.mBottom - params.mTop;
                params.mTop = offset;
                params.mBottom = params.mTop + height;
            }
        }
        if (mBaselineView == null) {
            mBaselineView = child;
        } else {
            LayoutParams lp = (LayoutParams) mBaselineView.getLayoutParams();
            if (params.mTop < lp.mTop || (params.mTop == lp.mTop && params.mLeft < lp.mLeft)) {
                mBaselineView = child;
            }
        }
    }

    /**
     * Measure a child. The child should have left, top, right and bottom
     * information stored in its LayoutParams. If any of these values is -1 it
     * means that the view can extend up to the corresponding edge.
     * 
     * @param child
     *            Child to measure
     * @param params
     *            LayoutParams associated with child
     * @param myWidth
     *            Width of the the RelativeLayout
     * @param myHeight
     *            Height of the RelativeLayout
     */
    private void measureChild(View child, LayoutParams params, int myWidth, int myHeight) {
        int childWidthMeasureSpec = getChildMeasureSpec(params.mLeft, params.mRight, params.width, params.leftMargin, params.rightMargin, paddingLeft, paddingRight, myWidth);
        int childHeightMeasureSpec = getChildMeasureSpec(params.mTop, params.mBottom, params.height, params.topMargin, params.bottomMargin, paddingTop, paddingBottom, myHeight);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    private void measureChildHorizontal(View child, LayoutParams params, int myWidth, int myHeight) {
        int childWidthMeasureSpec = getChildMeasureSpec(params.mLeft, params.mRight, params.width, params.leftMargin, params.rightMargin, paddingLeft, paddingRight, myWidth);
        int childHeightMeasureSpec;
        if (params.width == LayoutParams.FILL_PARENT) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.AT_MOST);
        }
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    /**
     * Get a measure spec that accounts for all of the constraints on this view.
     * This includes size contstraints imposed by the RelativeLayout as well as
     * the View's desired dimension.
     * 
     * @param childStart
     *            The left or top field of the child's layout params
     * @param childEnd
     *            The right or bottom field of the child's layout params
     * @param childSize
     *            The child's desired size (the width or height field of the
     *            child's layout params)
     * @param startMargin
     *            The left or top margin
     * @param endMargin
     *            The right or bottom margin
     * @param startPadding
     *            mPaddingLeft or mPaddingTop
     * @param endPadding
     *            mPaddingRight or mPaddingBottom
     * @param mySize
     *            The width or height of this view (the RelativeLayout)
     * @return MeasureSpec for the child
     */
    private int getChildMeasureSpec(int childStart, int childEnd, int childSize, int startMargin, int endMargin, int startPadding, int endPadding, int mySize) {
        int childSpecMode = 0;
        int childSpecSize = 0;
        int tempStart = childStart;
        int tempEnd = childEnd;
        if (tempStart < 0) {
            tempStart = startPadding + startMargin;
        }
        if (tempEnd < 0) {
            tempEnd = mySize - endPadding - endMargin;
        }
        int maxAvailable = tempEnd - tempStart;
        if (childStart >= 0 && childEnd >= 0) {
            childSpecMode = MeasureSpec.EXACTLY;
            childSpecSize = maxAvailable;
        } else {
            if (childSize >= 0) {
                childSpecMode = MeasureSpec.EXACTLY;
                if (maxAvailable >= 0) {
                    childSpecSize = Math.min(maxAvailable, childSize);
                } else {
                    childSpecSize = childSize;
                }
            } else if (childSize == LayoutParams.FILL_PARENT) {
                childSpecMode = MeasureSpec.EXACTLY;
                childSpecSize = maxAvailable;
            } else if (childSize == LayoutParams.WRAP_CONTENT) {
                if (maxAvailable >= 0) {
                    childSpecMode = MeasureSpec.AT_MOST;
                    childSpecSize = maxAvailable;
                } else {
                    childSpecMode = MeasureSpec.UNSPECIFIED;
                    childSpecSize = 0;
                }
            }
        }
        return MeasureSpec.makeMeasureSpec(childSpecSize, childSpecMode);
    }

    private boolean positionChildHorizontal(View child, LayoutParams params, int myWidth, boolean wrapContent) {
        int[] rules = params.getRules();
        if (params.mLeft < 0 && params.mRight >= 0) {
            params.mLeft = params.mRight - child.getMeasuredWidth();
        } else if (params.mLeft >= 0 && params.mRight < 0) {
            params.mRight = params.mLeft + child.getMeasuredWidth();
        } else if (params.mLeft < 0 && params.mRight < 0) {
            if (rules[CENTER_IN_PARENT] != 0 || rules[CENTER_HORIZONTAL] != 0) {
                if (!wrapContent) {
                    centerHorizontal(child, params, myWidth);
                } else {
                    params.mLeft = paddingLeft + params.leftMargin;
                    params.mRight = params.mLeft + child.getMeasuredWidth();
                }
                return true;
            } else {
                params.mLeft = paddingLeft + params.leftMargin;
                params.mRight = params.mLeft + child.getMeasuredWidth();
            }
        }
        return false;
    }

    private boolean positionChildVertical(View child, LayoutParams params, int myHeight, boolean wrapContent) {
        int[] rules = params.getRules();
        if (params.mTop < 0 && params.mBottom >= 0) {
            params.mTop = params.mBottom - child.getMeasuredHeight();
        } else if (params.mTop >= 0 && params.mBottom < 0) {
            params.mBottom = params.mTop + child.getMeasuredHeight();
        } else if (params.mTop < 0 && params.mBottom < 0) {
            if (rules[CENTER_IN_PARENT] != 0 || rules[CENTER_VERTICAL] != 0) {
                if (!wrapContent) {
                    centerVertical(child, params, myHeight);
                } else {
                    params.mTop = paddingTop + params.topMargin;
                    params.mBottom = params.mTop + child.getMeasuredHeight();
                }
                return true;
            } else {
                params.mTop = paddingTop + params.topMargin;
                params.mBottom = params.mTop + child.getMeasuredHeight();
            }
        }
        return false;
    }

    private void applyHorizontalSizeRules(LayoutParams childParams, int myWidth) {
        int[] rules = childParams.getRules();
        RelativeLayout.LayoutParams anchorParams;
        childParams.mLeft = -1;
        childParams.mRight = -1;
        anchorParams = getRelatedViewParams(rules, LEFT_OF);
        if (anchorParams != null) {
            childParams.mRight = anchorParams.mLeft - (anchorParams.leftMargin + childParams.rightMargin);
        } else if (childParams.alignWithParent && rules[LEFT_OF] != 0) {
            if (myWidth >= 0) {
                childParams.mRight = myWidth - paddingRight - childParams.rightMargin;
            } else {
            }
        }
        anchorParams = getRelatedViewParams(rules, RIGHT_OF);
        if (anchorParams != null) {
            childParams.mLeft = anchorParams.mRight + (anchorParams.rightMargin + childParams.leftMargin);
        } else if (childParams.alignWithParent && rules[RIGHT_OF] != 0) {
            childParams.mLeft = paddingLeft + childParams.leftMargin;
        }
        anchorParams = getRelatedViewParams(rules, ALIGN_LEFT);
        if (anchorParams != null) {
            childParams.mLeft = anchorParams.mLeft + childParams.leftMargin;
        } else if (childParams.alignWithParent && rules[ALIGN_LEFT] != 0) {
            childParams.mLeft = paddingLeft + childParams.leftMargin;
        }
        anchorParams = getRelatedViewParams(rules, ALIGN_RIGHT);
        if (anchorParams != null) {
            childParams.mRight = anchorParams.mRight - childParams.rightMargin;
        } else if (childParams.alignWithParent && rules[ALIGN_RIGHT] != 0) {
            if (myWidth >= 0) {
                childParams.mRight = myWidth - paddingRight - childParams.rightMargin;
            } else {
            }
        }
        if (0 != rules[ALIGN_PARENT_LEFT]) {
            childParams.mLeft = paddingLeft + childParams.leftMargin;
        }
        if (0 != rules[ALIGN_PARENT_RIGHT]) {
            if (myWidth >= 0) {
                childParams.mRight = myWidth - paddingRight - childParams.rightMargin;
            } else {
            }
        }
    }

    private void applyVerticalSizeRules(LayoutParams childParams, int myHeight) {
        int[] rules = childParams.getRules();
        RelativeLayout.LayoutParams anchorParams;
        childParams.mTop = -1;
        childParams.mBottom = -1;
        anchorParams = getRelatedViewParams(rules, ABOVE);
        if (anchorParams != null) {
            childParams.mBottom = anchorParams.mTop - (anchorParams.topMargin + childParams.bottomMargin);
        } else if (childParams.alignWithParent && rules[ABOVE] != 0) {
            if (myHeight >= 0) {
                childParams.mBottom = myHeight - paddingBottom - childParams.bottomMargin;
            } else {
            }
        }
        anchorParams = getRelatedViewParams(rules, BELOW);
        if (anchorParams != null) {
            childParams.mTop = anchorParams.mBottom + (anchorParams.bottomMargin + childParams.topMargin);
        } else if (childParams.alignWithParent && rules[BELOW] != 0) {
            childParams.mTop = paddingTop + childParams.topMargin;
        }
        anchorParams = getRelatedViewParams(rules, ALIGN_TOP);
        if (anchorParams != null) {
            childParams.mTop = anchorParams.mTop + childParams.topMargin;
        } else if (childParams.alignWithParent && rules[ALIGN_TOP] != 0) {
            childParams.mTop = paddingTop + childParams.topMargin;
        }
        anchorParams = getRelatedViewParams(rules, ALIGN_BOTTOM);
        if (anchorParams != null) {
            childParams.mBottom = anchorParams.mBottom - childParams.bottomMargin;
        } else if (childParams.alignWithParent && rules[ALIGN_BOTTOM] != 0) {
            if (myHeight >= 0) {
                childParams.mBottom = myHeight - paddingBottom - childParams.bottomMargin;
            } else {
            }
        }
        if (0 != rules[ALIGN_PARENT_TOP]) {
            childParams.mTop = paddingTop + childParams.topMargin;
        }
        if (0 != rules[ALIGN_PARENT_BOTTOM]) {
            if (myHeight >= 0) {
                childParams.mBottom = myHeight - paddingBottom - childParams.bottomMargin;
            } else {
            }
        }
        if (rules[ALIGN_BASELINE] != 0) {
            mHasBaselineAlignedChild = true;
        }
    }

    private View getRelatedView(int[] rules, int relation) {
        int id = rules[relation];
        if (id != 0) {
            DependencyGraph.Node node = mGraph.mKeyNodes.get(id);
            if (node == null) return null;
            View v = node.view;
            while (v.getVisibility() == View.GONE) {
                rules = ((LayoutParams) v.getLayoutParams()).getRules();
                node = mGraph.mKeyNodes.get((rules[relation]));
                if (node == null) return null;
                v = node.view;
            }
            return v;
        }
        return null;
    }

    private LayoutParams getRelatedViewParams(int[] rules, int relation) {
        View v = getRelatedView(rules, relation);
        if (v != null) {
            ViewGroup.LayoutParams params = v.getLayoutParams();
            if (params instanceof LayoutParams) {
                return (LayoutParams) v.getLayoutParams();
            }
        }
        return null;
    }

    private int getRelatedViewBaseline(int[] rules, int relation) {
        View v = getRelatedView(rules, relation);
        if (v != null) {
            return v.getBaseline();
        }
        return -1;
    }

    private void centerHorizontal(View child, LayoutParams params, int myWidth) {
        int childWidth = child.getMeasuredWidth();
        int left = (myWidth - childWidth) / 2;
        params.mLeft = left;
        params.mRight = left + childWidth;
    }

    private void centerVertical(View child, LayoutParams params, int myHeight) {
        int childHeight = child.getMeasuredHeight();
        int top = (myHeight - childHeight) / 2;
        params.mTop = top;
        params.mBottom = top + childHeight;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                RelativeLayout.LayoutParams st = (RelativeLayout.LayoutParams) child.getLayoutParams();
                child.layout(st.mLeft, st.mTop, st.mRight, st.mBottom);
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new RelativeLayout.LayoutParams(getContext(), attrs);
    }

    /**
     * Returns a set of layout parameters with a width of
     * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}, a height of
     * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT} and no spanning.
     */
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof RelativeLayout.LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    private void parseRelativeLayoutAttributes(AttributeSet attrs) {
        setIgnoreRequestLayout(true);
        setGravity(Gravity.parseGravity(attrs.getAttributeValue(null, "gravity"), 0));
        setIgnoreGravity(attrs.getAttributeResourceValue(null, "ignoreGravity", 0));
        setIgnoreRequestLayout(false);
    }

    /**
     * Per-child layout information associated with RelativeLayout.
     * 
     * @attr ref android.R.styleable#
     *       RelativeLayout_Layout_layout_alignWithParentIfMissing
     * @attr ref android.R.styleable#RelativeLayout_Layout_layout_toLeftOf
     * @attr ref android.R.styleable#RelativeLayout_Layout_layout_toRightOf
     * @attr ref android.R.styleable#RelativeLayout_Layout_layout_above
     * @attr ref android.R.styleable#RelativeLayout_Layout_layout_below
     * @attr ref android.R.styleable#RelativeLayout_Layout_layout_alignBaseline
     * @attr ref android.R.styleable#RelativeLayout_Layout_layout_alignLeft
     * @attr ref android.R.styleable#RelativeLayout_Layout_layout_alignTop
     * @attr ref android.R.styleable#RelativeLayout_Layout_layout_alignRight
     * @attr ref android.R.styleable#RelativeLayout_Layout_layout_alignBottom
     * @attr ref
     *       android.R.styleable#RelativeLayout_Layout_layout_alignParentLeft
     * @attr ref android.R.styleable#RelativeLayout_Layout_layout_alignParentTop
     * @attr ref
     *       android.R.styleable#RelativeLayout_Layout_layout_alignParentRight
     * @attr ref
     *       android.R.styleable#RelativeLayout_Layout_layout_alignParentBottom
     * @attr ref android.R.styleable#RelativeLayout_Layout_layout_centerInParent
     * @attr ref
     *       android.R.styleable#RelativeLayout_Layout_layout_centerHorizontal
     * @attr ref android.R.styleable#RelativeLayout_Layout_layout_centerVertical
     */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        private int[] mRules = new int[VERB_COUNT];

        private int mLeft, mTop, mRight, mBottom;

        /**
         * When true, uses the parent as the anchor if the anchor doesn't exist
         * or if the anchor's visibility is GONE.
         */
        public boolean alignWithParent;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            alignWithParent = attrs.getAttributeBooleanValue(null, "layout_alignWithParentIfMissing", false);
            mRules[LEFT_OF] = attrs.getAttributeResourceValue(null, "layout_toLeftOf", 0);
            mRules[RIGHT_OF] = attrs.getAttributeResourceValue(null, "layout_toRightOf", 0);
            mRules[ABOVE] = attrs.getAttributeResourceValue(null, "layout_above", 0);
            mRules[BELOW] = attrs.getAttributeResourceValue(null, "layout_below", 0);
            mRules[ALIGN_BASELINE] = attrs.getAttributeResourceValue(null, "layout_alignBaseline", 0);
            mRules[ALIGN_LEFT] = attrs.getAttributeResourceValue(null, "layout_alignLeft", 0);
            mRules[ALIGN_TOP] = attrs.getAttributeResourceValue(null, "layout_alignTop", 0);
            mRules[ALIGN_RIGHT] = attrs.getAttributeResourceValue(null, "layout_alignRight", 0);
            mRules[ALIGN_BOTTOM] = attrs.getAttributeResourceValue(null, "layout_alignBottom", 0);
            mRules[ALIGN_PARENT_LEFT] = attrs.getAttributeBooleanValue(null, "layout_alignParentLeft", false) ? TRUE : 0;
            mRules[ALIGN_PARENT_TOP] = attrs.getAttributeBooleanValue(null, "layout_alignParentTop", false) ? TRUE : 0;
            mRules[ALIGN_PARENT_RIGHT] = attrs.getAttributeBooleanValue(null, "layout_alignParentRight", false) ? TRUE : 0;
            mRules[ALIGN_PARENT_BOTTOM] = attrs.getAttributeBooleanValue(null, "layout_alignParentBottom", false) ? TRUE : 0;
            mRules[CENTER_IN_PARENT] = attrs.getAttributeBooleanValue(null, "layout_centerInParent", false) ? TRUE : 0;
            mRules[CENTER_HORIZONTAL] = attrs.getAttributeBooleanValue(null, "layout_centerHorizontal", false) ? TRUE : 0;
            mRules[CENTER_VERTICAL] = attrs.getAttributeBooleanValue(null, "layout_centerVertical", false) ? TRUE : 0;
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        /**
         * Adds a layout rule to be interpreted by the RelativeLayout. This
         * method should only be used for constraints that don't refer to
         * another sibling (e.g., CENTER_IN_PARENT) or take a boolean value (
         * {@link RelativeLayout#TRUE} for true or - for false). To specify a
         * verb that takes a subject, use {@link #addRule(int, int)} instead.
         * 
         * @param verb
         *            One of the verbs defined by
         *            {@link android.widget.RelativeLayout RelativeLayout}, such
         *            as ALIGN_WITH_PARENT_LEFT.
         * @see #addRule(int, int)
         */
        public void addRule(int verb) {
            mRules[verb] = TRUE;
        }

        /**
         * Adds a layout rule to be interpreted by the RelativeLayout. Use this
         * for verbs that take a target, such as a sibling (ALIGN_RIGHT) or a
         * boolean value (VISIBLE).
         * 
         * @param verb
         *            One of the verbs defined by
         *            {@link android.widget.RelativeLayout RelativeLayout}, such
         *            as ALIGN_WITH_PARENT_LEFT.
         * @param anchor
         *            The id of another view to use as an anchor, or a boolean
         *            value(represented as {@link RelativeLayout#TRUE}) for true
         *            or 0 for false). For verbs that don't refer to another
         *            sibling (for example, ALIGN_WITH_PARENT_BOTTOM) just use
         *            -1.
         * @see #addRule(int)
         */
        public void addRule(int verb, int anchor) {
            mRules[verb] = anchor;
        }

        /**
         * Retrieves a complete list of all supported rules, where the index is
         * the rule verb, and the element value is the value specified, or
         * "false" if it was never set.
         * 
         * @return the supported rules
         * @see #addRule(int, int)
         */
        public int[] getRules() {
            return mRules;
        }
    }

    private static class DependencyGraph {

        /**
         * List of all views in the graph.
         */
        private ArrayList<Node> mNodes = new ArrayList<Node>();

        /**
         * List of nodes in the graph. Each node is identified by its view id
         * (see View#getId()).
         */
        private SparseArray<Node> mKeyNodes = new SparseArray<Node>();

        /**
         * Temporary data structure used to build the list of roots for this
         * graph.
         */
        private LinkedList<Node> mRoots = new LinkedList<Node>();

        /**
         * Clears the graph.
         */
        void clear() {
            final ArrayList<Node> nodes = mNodes;
            final int count = nodes.size();
            for (int i = 0; i < count; i++) {
                nodes.get(i).release();
            }
            nodes.clear();
            mKeyNodes.clear();
            mRoots.clear();
        }

        /**
         * Adds a view to the graph.
         * 
         * @param view
         *            The view to be added as a node to the graph.
         */
        void add(View view) {
            final int id = view.getId();
            final Node node = Node.acquire(view);
            if (id != View.NO_ID) {
                mKeyNodes.put(id, node);
            }
            mNodes.add(node);
        }

        /**
         * Builds a sorted list of views. The sorting order depends on the
         * dependencies between the view. For instance, if view C needs view A
         * to be processed first and view A needs view B to be processed first,
         * the dependency graph is: B -> A -> C. The sorted array will contain
         * views B, A and C in this order.
         * 
         * @param sorted
         *            The sorted list of views. The length of this array must be
         *            equal to getChildCount().
         * @param rules
         *            The list of rules to take into account.
         */
        void getSortedViews(View[] sorted, int... rules) {
            final LinkedList<Node> roots = findRoots(rules);
            int index = 0;
            while (roots.size() > 0) {
                final Node node = roots.removeFirst();
                final View view = node.view;
                final int key = view.getId();
                sorted[index++] = view;
                final HashSet<Node> dependents = node.dependents;
                for (Node dependent : dependents) {
                    final SparseArray<Node> dependencies = dependent.dependencies;
                    dependencies.remove(key);
                    if (dependencies.size() == 0) {
                        roots.add(dependent);
                    }
                }
            }
            if (index < sorted.length) {
                throw new IllegalStateException("Circular dependencies cannot exist" + " in RelativeLayout");
            }
        }

        /**
         * Finds the roots of the graph. A root is a node with no dependency and
         * with [0..n] dependents.
         * 
         * @param rulesFilter
         *            The list of rules to consider when building the
         *            dependencies
         * 
         * @return A list of node, each being a root of the graph
         */
        private LinkedList<Node> findRoots(int[] rulesFilter) {
            final SparseArray<Node> keyNodes = mKeyNodes;
            final ArrayList<Node> nodes = mNodes;
            final int count = nodes.size();
            for (int i = 0; i < count; i++) {
                final Node node = nodes.get(i);
                node.dependents.clear();
                node.dependencies.clear();
            }
            for (int i = 0; i < count; i++) {
                final Node node = nodes.get(i);
                final LayoutParams layoutParams = (LayoutParams) node.view.getLayoutParams();
                final int[] rules = layoutParams.mRules;
                final int rulesCount = rulesFilter.length;
                for (int j = 0; j < rulesCount; j++) {
                    final int rule = rules[rulesFilter[j]];
                    if (rule > 0) {
                        final Node dependency = keyNodes.get(rule);
                        if (dependency == null || dependency == node) {
                            continue;
                        }
                        dependency.dependents.add(node);
                        node.dependencies.put(rule, dependency);
                    }
                }
            }
            final LinkedList<Node> roots = mRoots;
            roots.clear();
            for (int i = 0; i < count; i++) {
                final Node node = nodes.get(i);
                if (node.dependencies.size() == 0) roots.add(node);
            }
            return roots;
        }

        /**
         * Prints the dependency graph for the specified rules.
         * 
         * @param resources
         *            The context's resources to print the ids.
         * @param rules
         *            The list of rules to take into account.
         */
        void log(Resources resources, int... rules) {
            final LinkedList<Node> roots = findRoots(rules);
            for (Node node : roots) {
                printNode(resources, node);
            }
        }

        static void printViewId(Resources resources, View view) {
            if (view.getId() != View.NO_ID) {
                d(LOG_TAG, resources.getResourceEntryName(view.getId()));
            } else {
                d(LOG_TAG, "NO_ID");
            }
        }

        private static void appendViewId(Resources resources, Node node, StringBuilder buffer) {
            if (node.view.getId() != View.NO_ID) {
                buffer.append(resources.getResourceEntryName(node.view.getId()));
            } else {
                buffer.append("NO_ID");
            }
        }

        private static void printNode(Resources resources, Node node) {
            if (node.dependents.size() == 0) {
                printViewId(resources, node.view);
            } else {
                for (Node dependent : node.dependents) {
                    StringBuilder buffer = new StringBuilder();
                    appendViewId(resources, node, buffer);
                    printdependents(resources, dependent, buffer);
                }
            }
        }

        private static void printdependents(Resources resources, Node node, StringBuilder buffer) {
            buffer.append(" -> ");
            appendViewId(resources, node, buffer);
            if (node.dependents.size() == 0) {
                d(LOG_TAG, buffer.toString());
            } else {
                for (Node dependent : node.dependents) {
                    StringBuilder subBuffer = new StringBuilder(buffer);
                    printdependents(resources, dependent, subBuffer);
                }
            }
        }

        /**
         * A node in the dependency graph. A node is a view, its list of
         * dependencies and its list of dependents.
         * 
         * A node with no dependent is considered a root of the graph.
         */
        static class Node implements Poolable<Node> {

            /**
             * The view representing this node in the layout.
             */
            View view;

            /**
             * The list of dependents for this node; a dependent is a node that
             * needs this node to be processed first.
             */
            final HashSet<Node> dependents = new HashSet<Node>();

            /**
             * The list of dependencies for this node.
             */
            final SparseArray<Node> dependencies = new SparseArray<Node>();

            private static final int POOL_LIMIT = 100;

            private static final Pool<Node> sPool = Pools.synchronizedPool(Pools.finitePool(new PoolableManager<Node>() {

                public Node newInstance() {
                    return new Node();
                }

                public void onAcquired(Node element) {
                }

                public void onReleased(Node element) {
                }
            }, POOL_LIMIT));

            private Node mNext;

            public void setNextPoolable(Node element) {
                mNext = element;
            }

            public Node getNextPoolable() {
                return mNext;
            }

            static Node acquire(View view) {
                final Node node = sPool.acquire();
                node.view = view;
                return node;
            }

            void release() {
                view = null;
                dependents.clear();
                dependencies.clear();
                sPool.release(this);
            }
        }
    }
}
