package com.yingyonghui.market;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.yingyonghui.market.model.AssetDetail;
import com.yingyonghui.market.model.Comment;
import com.yingyonghui.market.util.GlobalUtil;
import dalvik.annotation.EnclosingMethod;
import java.util.ArrayList;

@EnclosingMethod
class CommentsActivity$4 extends Handler
{
  public void handleMessage(Message paramMessage)
  {
    switch (paramMessage.what)
    {
    default:
    case 0:
    case 1:
    case 100:
    }
    while (true)
    {
      return;
      Object[] arrayOfObject = (Object[])paramMessage.obj;
      Integer localInteger = (Integer)arrayOfObject[0];
      paramMessage = (AssetInfoActivity)this.this$0.getParent();
      int i;
      label74: ArrayList localArrayList;
      if (localInteger == null)
      {
        i = 0;
        paramMessage.setCommentCount(i);
        localArrayList = (ArrayList)arrayOfObject[1];
        if (CommentsActivity.access$11(this.this$0) != null)
          break label637;
        CommentsActivity localCommentsActivity1 = this.this$0;
        CommentsActivity localCommentsActivity2 = this.this$0;
        CommentsActivity localCommentsActivity3 = this.this$0;
        CommentsActivity.ReviewAdapter localReviewAdapter1 = new CommentsActivity.ReviewAdapter(localCommentsActivity2, localCommentsActivity3, localArrayList);
        CommentsActivity.access$8(localCommentsActivity1, localReviewAdapter1);
        ListView localListView1 = CommentsActivity.access$6(this.this$0);
        CommentsActivity.ReviewAdapter localReviewAdapter2 = CommentsActivity.access$11(this.this$0);
        localListView1.setAdapter(localReviewAdapter2);
        if ((localArrayList != null) && (localArrayList.size() != 0))
          break label424;
        if (CommentsActivity.access$1(this.this$0).installed != 0)
          this.this$0.findViewById(2131427436).setVisibility(0);
        label202: CommentsActivity.access$18(this.this$0).setVisibility(8);
        CommentsActivity.access$19(this.this$0).setVisibility(8);
        CommentsActivity.access$20(this.this$0).setVisibility(8);
        if ((localArrayList != null) && (localArrayList.size() != 0))
          break label441;
        if (CommentsActivity.access$1(this.this$0).installed != 0)
        {
          CommentsActivity.access$19(this.this$0).setVisibility(0);
          CommentsActivity.access$20(this.this$0).setVisibility(0);
          CommentsActivity.access$21(this.this$0);
        }
      }
      label424: label441: label637: 
      do
        while (true)
        {
          if (localArrayList != null)
          {
            CommentsActivity localCommentsActivity4 = this.this$0;
            int j = CommentsActivity.access$23(localCommentsActivity4);
            int k = localArrayList.size();
            int m = j + k;
            CommentsActivity.access$9(localCommentsActivity4, m);
          }
          if ((localArrayList == null) || (localArrayList.size() == 0) || (localArrayList.size() < 5))
          {
            CommentsActivity.access$5(this.this$0, 1);
            ListView localListView2 = CommentsActivity.access$6(this.this$0);
            View localView1 = CommentsActivity.access$7(this.this$0);
            localListView2.removeFooterView(localView1);
          }
          CommentsActivity.access$24(this.this$0).setVisibility(8);
          CommentsActivity.access$6(this.this$0).setVisibility(0);
          break;
          i = localInteger.intValue();
          break label74;
          this.this$0.findViewById(2131427436).setVisibility(8);
          break label202;
          Comment localComment1 = (Comment)localArrayList.get(0);
          if ((!localComment1.isDelete) && (localComment1.isMine))
          {
            String str1 = localComment1.type;
            String str2 = Comment.TYPE_COMMENT;
            if (str1.equals(str2))
            {
              CommentsActivity.access$18(this.this$0).setVisibility(0);
              CommentsActivity.access$20(this.this$0).setVisibility(0);
              CommentsActivity.access$22(this.this$0, localComment1);
              continue;
            }
          }
          if (CommentsActivity.access$1(this.this$0).installed == 0)
            continue;
          if (localComment1.isMine)
          {
            String str3 = localComment1.type;
            String str4 = Comment.TYPE_COMMENT;
            if (str3.equals(str4))
            {
              if ((!localComment1.isMine) || (!localComment1.isDelete))
                continue;
              String str5 = localComment1.type;
              String str6 = Comment.TYPE_COMMENT;
              if (!str5.equals(str6))
                continue;
            }
          }
          CommentsActivity.access$19(this.this$0).setVisibility(0);
          CommentsActivity.access$20(this.this$0).setVisibility(0);
          CommentsActivity.access$21(this.this$0);
        }
      while (localArrayList == null);
      int n = localArrayList.size();
      int i1 = 0;
      while (true)
      {
        if (i1 >= n)
        {
          CommentsActivity.access$11(this.this$0).notifyDataSetChanged();
          break;
        }
        CommentsActivity.ReviewAdapter localReviewAdapter3 = CommentsActivity.access$11(this.this$0);
        Comment localComment2 = (Comment)localArrayList.get(i1);
        localReviewAdapter3.add(localComment2);
        i1 += 1;
      }
      try
      {
        this.this$0.removeDialog(200);
        try
        {
          label719: this.this$0.showDialog(100);
        }
        catch (Exception localException1)
        {
        }
        continue;
        this.this$0.removeDialog(200);
        if (((Boolean)paramMessage.obj).booleanValue())
        {
          GlobalUtil.longToast(this.this$0, 2131296515);
          if (CommentsActivity.access$4(this.this$0))
          {
            CommentsActivity.access$5(this.this$0, 0);
            ListView localListView3 = CommentsActivity.access$6(this.this$0);
            View localView2 = CommentsActivity.access$7(this.this$0);
            localListView3.addFooterView(localView2, null, 0);
          }
          CommentsActivity.access$8(this.this$0, null);
          CommentsActivity.access$9(this.this$0, 0);
          CommentsActivity.access$3(this.this$0);
          continue;
        }
        GlobalUtil.longToast(this.this$0, 2131296516);
      }
      catch (Exception localException2)
      {
        break label719;
      }
    }
  }
}

/* Location:           D:\android_tools\dex2jar-0.0.7.4-SNAPSHOT\classes.dex.dex2jar.jar
 * Qualified Name:     com.yingyonghui.market.CommentsActivity.4
 * JD-Core Version:    0.6.0
 */