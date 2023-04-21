package com.yingyonghui.market;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;

class FilteredCategoryListActivity$1
  implements AbsListView.OnScrollListener
{
  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
    switch (paramInt)
    {
    default:
    case 0:
    case 1:
    case 2:
    }
    while (true)
    {
      return;
      FilteredCategoryListActivity.access$0(this.this$0, 0);
      int i = paramAbsListView.getFirstVisiblePosition();
      int j = paramAbsListView.getChildCount();
      int k = 0;
      label52: View localView;
      if (k < j)
      {
        localView = paramAbsListView.getChildAt(k);
        if (!FilteredCategoryListActivity.access$1(this.this$0))
          break label155;
      }
      label155: for (int m = i + k - 1; ; m = i + k)
      {
        FilteredCategoryListActivity.ViewHolder localViewHolder = (FilteredCategoryListActivity.ViewHolder)localView.getTag();
        if (localViewHolder != null)
        {
          ImageView localImageView = localViewHolder.thumbnail;
          FilteredCategoryListActivity localFilteredCategoryListActivity = this.this$0;
          int n = (int)FilteredCategoryListActivity.access$2(this.this$0).getTopAppId(m);
          Drawable localDrawable = localFilteredCategoryListActivity.getThumbnail(m, n);
          localImageView.setImageDrawable(localDrawable);
        }
        k += 1;
        break label52;
        break;
      }
      if (FilteredCategoryListActivity.access$3(this.this$0))
        continue;
      FilteredCategoryListActivity.access$4(this.this$0);
      FilteredCategoryListActivity.access$0(this.this$0, 1);
    }
  }
}

/* Location:           D:\android_tools\dex2jar-0.0.7.4-SNAPSHOT\classes.dex.dex2jar.jar
 * Qualified Name:     com.yingyonghui.market.FilteredCategoryListActivity.1
 * JD-Core Version:    0.6.0
 */