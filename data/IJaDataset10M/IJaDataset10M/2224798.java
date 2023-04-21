package com.yingyonghui.market;

import android.view.View;
import android.view.View.OnClickListener;
import com.yingyonghui.market.online.IMarketService;
import com.yingyonghui.market.online.Request;
import dalvik.annotation.EnclosingMethod;

@EnclosingMethod
class CommentsActivity$3 implements View.OnClickListener {

    public void onClick(View paramView) {
        IMarketService localIMarketService = CommentsActivity.access$16(this.this$0);
        Request localRequest = CommentsActivity.access$17(this.this$0);
        localIMarketService.getAppComments(localRequest);
    }
}
