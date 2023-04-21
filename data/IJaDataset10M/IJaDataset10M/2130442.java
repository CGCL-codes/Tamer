package com.yingyonghui.market;

import android.os.Handler;
import android.os.Message;
import com.yingyonghui.market.online.Request;
import dalvik.annotation.EnclosingMethod;
import java.util.Observable;
import java.util.Observer;

@EnclosingMethod
class NewsContentActivity$5 implements Observer {

    public void update(Observable paramObservable, Object paramObject) {
        if (paramObject != null) {
            Message localMessage1 = Message.obtain(NewsContentActivity.access$14(this.this$0), 0, paramObject);
            NewsContentActivity.access$14(this.this$0).sendMessage(localMessage1);
        }
        while (true) {
            return;
            if (this.val$request.getStatus() == 196613) {
                Message localMessage2 = Message.obtain(NewsContentActivity.access$14(this.this$0), 4, paramObject);
                NewsContentActivity.access$14(this.this$0).sendMessage(localMessage2);
                continue;
            }
        }
    }
}
