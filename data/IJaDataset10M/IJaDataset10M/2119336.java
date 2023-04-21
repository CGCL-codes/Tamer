package howbuy.android.palmfund.activity.networth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import howbuy.android.palmfund.R;
import howbuy.android.palmfund.adapter.OpenFundAdapter;
import howbuy.android.palmfund.application.ApplicationParams;
import howbuy.android.palmfund.custom.CustomListView;
import howbuy.android.palmfund.dbservice.FundInfoDbService;
import howbuy.android.util.Cons;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * ����ʽ���
 * 
 * @author yescpu
 * 
 */
public class NetworthKfActivity extends Activity {

    public static final String TAG = "NetworthKfActivity";

    public static final String orderAscDes = "desc";

    public static String[][] orderOrderArray;

    public OpenFundAdapter openFundAdapter;

    public Cursor cursor;

    private CustomListView listView;

    private Context context;

    private FundInfoDbService fundInfoDbService;

    private int inTo;

    private ApplicationParams aParams;

    private Handler tHandler;

    private Handler handler;

    private void initView() {
        inTo = getIntent().getIntExtra(Cons.SFTabFundInfo, 0);
        listView = (CustomListView) findViewById(R.id.listview_kf);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.networth_1kf);
        context = this;
        initView();
        aParams = (ApplicationParams) getApplication();
        HandlerThread handlerThread = new HandlerThread("");
        handlerThread.start();
        tHandler = new Handler(handlerThread.getLooper());
        tHandler.post(new runAble());
        try {
            listView.setOnScrollListener(new OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView.setonRefreshListener(new CustomListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Thread(new Runnable() {

                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                        }
                        runOnUiThread(new Runnable() {

                            public void run() {
                                listView.onRefreshComplete();
                            }
                        });
                    }
                }).start();
            }
        });
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what) {
                    case Cons.SHOW_SUCCESS:
                        listView.setAdapter(openFundAdapter);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    class runAble implements Runnable {

        @Override
        public void run() {
            Message message = null;
            try {
                fundInfoDbService = FundInfoDbService.getInstance(context);
                String firstOrder = null;
                switch(inTo) {
                    case 0:
                        firstOrder = NetworthMainActivity.orderKeysKf[0];
                        cursor = fundInfoDbService.getfundOpen(firstOrder, orderAscDes);
                        break;
                    case 1:
                        firstOrder = NetworthMainActivity.orderKeysKfb[0];
                        cursor = fundInfoDbService.getfundOpenBranch("1", firstOrder, orderAscDes);
                        break;
                    case 2:
                        firstOrder = NetworthMainActivity.orderKeysKfb[0];
                        cursor = fundInfoDbService.getfundOpenBranch("17", firstOrder, orderAscDes);
                        break;
                    case 3:
                        firstOrder = NetworthMainActivity.orderKeysKfb[0];
                        cursor = fundInfoDbService.getfundOpenBranch("2", firstOrder, orderAscDes);
                        break;
                    case 4:
                        firstOrder = NetworthMainActivity.orderKeysKfb[0];
                        cursor = fundInfoDbService.getfundOpenBranch("4", firstOrder, orderAscDes);
                        break;
                    case 5:
                        firstOrder = NetworthMainActivity.orderKeysKfb[0];
                        cursor = fundInfoDbService.getfundOpenBranch("8", firstOrder, orderAscDes);
                        break;
                    case 6:
                        firstOrder = NetworthMainActivity.orderKeysKfb[0];
                        cursor = fundInfoDbService.getfundOpenBranch("16", firstOrder, orderAscDes);
                        break;
                    case 7:
                        firstOrder = NetworthMainActivity.orderKeysHb[0];
                        cursor = fundInfoDbService.getfundMoneys(firstOrder, orderAscDes);
                        break;
                    case 8:
                        firstOrder = NetworthMainActivity.orderKeysSm[0];
                        cursor = fundInfoDbService.getfundSimus(firstOrder, orderAscDes);
                        break;
                    case 9:
                        firstOrder = NetworthMainActivity.orderKeysFb[0];
                        cursor = fundInfoDbService.getfundCloses(firstOrder, orderAscDes);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                message = handler.obtainMessage(Cons.SHOW_ERROR, e.getMessage());
                e.printStackTrace();
            } finally {
                if (cursor != null && cursor.getCount() > 0) {
                    openFundAdapter = new OpenFundAdapter(context, cursor, inTo);
                    message = handler.obtainMessage(Cons.SHOW_SUCCESS);
                } else {
                    message = handler.obtainMessage(Cons.SHOW_NODATA, "û�����");
                }
                handler.sendMessage(message);
            }
        }
    }

    /**
	 * ������½���
	 * 
	 * @param c
	 */
    public void updateDateListView(Cursor c) {
        startManagingCursor(c);
        OpenFundAdapter openFundAdapter = new OpenFundAdapter(context, c, inTo);
        listView.setAdapter(openFundAdapter);
    }

    public OpenFundAdapter getOpenFundAdapter() {
        return openFundAdapter;
    }

    public void setOpenFundAdapter(OpenFundAdapter openFundAdapter) {
        this.openFundAdapter = openFundAdapter;
    }

    public CustomListView getListView() {
        return listView;
    }

    public void setListView(CustomListView listView) {
        this.listView = listView;
    }

    public FundInfoDbService getFundInfoDbService() {
        return fundInfoDbService;
    }

    public void setFundInfoDbService(FundInfoDbService fundInfoDbService) {
        this.fundInfoDbService = fundInfoDbService;
    }
}
