package com.yingyonghui.market.online;

import android.content.Context;

public class MarketService implements IMarketService {

    public static int THREAD_LIST = 0;

    public static int THREAD_OTHER;

    public static int THREAD_PAYMENT;

    public static int THREAD_THUMB;

    public static int THREAD_TRANSPORT = 1;

    private static int[] handlerIDs;

    private static MarketService instance;

    private MarketServiceAgent agent = null;

    protected Context mContext;

    private RequestEventHandler[] requestHandler;

    private CacheQueue requestQueueList;

    private CacheQueue requestQueueOther;

    private CacheQueue requestQueuePayment;

    private CacheQueue requestQueueThumb;

    private CacheQueue requestQueueTrans;

    static {
        THREAD_THUMB = 2;
        THREAD_PAYMENT = 3;
        THREAD_OTHER = 4;
        int[] arrayOfInt = new int[5];
        int i = THREAD_LIST;
        arrayOfInt[0] = i;
        int j = THREAD_TRANSPORT;
        arrayOfInt[1] = j;
        int k = THREAD_THUMB;
        arrayOfInt[2] = k;
        int m = THREAD_PAYMENT;
        arrayOfInt[3] = m;
        int n = THREAD_OTHER;
        arrayOfInt[4] = n;
        handlerIDs = arrayOfInt;
        instance = null;
    }

    private MarketService(Context paramContext) {
        CacheQueue localCacheQueue1 = new CacheQueue();
        this.requestQueueList = localCacheQueue1;
        CacheQueue localCacheQueue2 = new CacheQueue();
        this.requestQueueTrans = localCacheQueue2;
        CacheQueue localCacheQueue3 = new CacheQueue();
        this.requestQueueThumb = localCacheQueue3;
        CacheQueue localCacheQueue4 = new CacheQueue();
        this.requestQueuePayment = localCacheQueue4;
        CacheQueue localCacheQueue5 = new CacheQueue();
        this.requestQueueOther = localCacheQueue5;
        this.mContext = null;
        RequestEventHandler[] arrayOfRequestEventHandler = new RequestEventHandler[handlerIDs.length];
        this.requestHandler = arrayOfRequestEventHandler;
        MarketServiceAgent localMarketServiceAgent = MarketServiceAgent.getInstance(paramContext);
        this.agent = localMarketServiceAgent;
        this.mContext = paramContext;
        init();
    }

    public static MarketService getServiceInstance(Context paramContext) {
        if (instance == null) instance = new MarketService(paramContext);
        return instance;
    }

    public void buyByAlipay(Request paramRequest) {
        this.requestQueuePayment.pushRequest(paramRequest);
    }

    public void buyByBean(Request paramRequest) {
        this.requestQueuePayment.pushRequest(paramRequest);
    }

    public void buyByYeepayCard(Request paramRequest) {
        this.requestQueuePayment.pushRequest(paramRequest);
    }

    public void clearPendingRequest(int paramInt) {
        int i = THREAD_THUMB;
        if ((paramInt == i) && (!this.requestQueueThumb.isEmpty())) this.requestQueueThumb.clearPendingRequest();
    }

    public void deleteComment(Request paramRequest) {
        this.requestQueueOther.pushRequest(paramRequest);
    }

    public void destroy() {
        int i = 0;
        while (true) {
            int j = handlerIDs.length;
            if (i >= j) return;
            this.requestHandler[i].running = 0;
            this.requestHandler[i].interrupt();
            i += 1;
        }
    }

    public void finalize() {
        destroy();
        try {
            finalize();
            label8: return;
        } catch (Throwable localThrowable) {
            break label8;
        }
    }

    public void getAccountBalance(Request paramRequest) {
        this.requestQueuePayment.pushRequest(paramRequest);
    }

    public void getAppComments(Request paramRequest) {
        this.requestQueueList.pushRequest(paramRequest);
    }

    public void getAppContentStream(Request paramRequest) {
        this.requestQueueTrans.pushRequest(paramRequest);
    }

    public void getAppDetail(Request paramRequest) {
        this.requestQueueOther.pushRequest(paramRequest);
    }

    public void getAppIcon(Request paramRequest) {
        this.requestQueueThumb.pushRequest(paramRequest);
    }

    public void getAppList(Request paramRequest) {
        this.requestQueueList.pushRequest(paramRequest);
    }

    public void getAppListByKeywords(Request paramRequest) {
        this.requestQueueList.pushRequest(paramRequest);
    }

    public void getAppScreenShorts(Request paramRequest) {
        this.requestQueueThumb.pushRequest(paramRequest);
    }

    public void getCategory(Request paramRequest) {
        this.requestQueueList.pushRequest(paramRequest);
    }

    public void getDownloadedAppList(Request paramRequest) {
        this.requestQueueList.pushRequest(paramRequest);
    }

    public void getInstallUpdateLog(Request paramRequest) {
        this.requestQueueOther.pushRequest(paramRequest);
    }

    public void getNewsAppInfo(Request paramRequest) {
        this.requestQueueOther.pushRequest(paramRequest);
    }

    public void getNewsContent(Request paramRequest) {
        this.requestQueueOther.pushRequest(paramRequest);
    }

    public void getNewsIcon(Request paramRequest) {
        this.requestQueueThumb.pushRequest(paramRequest);
    }

    public void getNewsImage(Request paramRequest) {
        this.requestQueueThumb.pushRequest(paramRequest);
    }

    public void getNewsList(Request paramRequest) {
        this.requestQueueList.pushRequest(paramRequest);
    }

    public void getNewsThumb(Request paramRequest) {
        this.requestQueueThumb.pushRequest(paramRequest);
    }

    public void getPageviewLog(Request paramRequest) {
        this.requestQueueOther.pushRequest(paramRequest);
    }

    public void getSearchLog(Request paramRequest) {
        this.requestQueueOther.pushRequest(paramRequest);
    }

    public MarketServiceAgent getServiceAgent() {
        return this.agent;
    }

    public void getTestUserAvailable(Request paramRequest) {
        this.requestQueueList.pushRequest(paramRequest);
    }

    public void getTopFourApp(Request paramRequest) {
        this.requestQueueThumb.pushRequest(paramRequest);
    }

    public void getTopKeywords(Request paramRequest) {
        this.requestQueueList.pushRequest(paramRequest);
    }

    public void getUpdateAvaliableNum(Request paramRequest) {
        this.requestQueueOther.pushRequest(paramRequest);
    }

    public void getWidgetDisabledLog(Request paramRequest) {
        this.requestQueueOther.pushRequest(paramRequest);
    }

    public void getWidgetEnabledLog(Request paramRequest) {
        this.requestQueueOther.pushRequest(paramRequest);
    }

    public void init() {
        if (this.requestHandler[0] != 0) return;
        int i = 0;
        while (true) {
            int j = handlerIDs.length;
            if (i >= j) break;
            RequestEventHandler[] arrayOfRequestEventHandler = this.requestHandler;
            int k = handlerIDs[i];
            RequestEventHandler localRequestEventHandler = new RequestEventHandler(this, k);
            arrayOfRequestEventHandler[i] = localRequestEventHandler;
            this.requestHandler[i].start();
            i += 1;
        }
    }

    public void isAppBuyed(Request paramRequest) {
        this.requestQueuePayment.pushRequest(paramRequest);
    }

    public void loginToServer(Request paramRequest) {
        this.requestQueueTrans.pushRequest(paramRequest);
    }

    public Object popRequest(int paramInt) throws InterruptedException {
        int i = THREAD_LIST;
        Object localObject;
        if (paramInt == i) localObject = this.requestQueueList.popRequest(0L);
        while (true) {
            return localObject;
            int j = THREAD_TRANSPORT;
            if (paramInt == j) {
                localObject = this.requestQueueTrans.popRequest(0L);
                continue;
            }
            int k = THREAD_THUMB;
            if (paramInt == k) {
                localObject = this.requestQueueThumb.popRequest(0L);
                continue;
            }
            int m = THREAD_PAYMENT;
            if (paramInt == m) {
                localObject = this.requestQueuePayment.popRequest(0L);
                continue;
            }
            localObject = this.requestQueueOther.popRequest(0L);
        }
    }

    public void reportError(Request paramRequest) {
        this.requestQueueOther.pushRequest(paramRequest);
    }

    public void sendComment(Request paramRequest) {
        this.requestQueueOther.pushRequest(paramRequest);
    }

    public void sendInstallLog(Request paramRequest) {
        this.requestQueueOther.pushRequest(paramRequest);
    }

    public void setAppContext(Context paramContext) {
        if (this.mContext != paramContext) this.mContext = paramContext;
    }
}
