package com.guanri.android.insurance.activity;

import java.util.Date;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.guanri.android.insurance.R;
import com.guanri.android.insurance.activity.base.ApsaiActivity;
import com.guanri.android.insurance.bean.DownCommandBean;
import com.guanri.android.insurance.bean.SaleCheckBean;
import com.guanri.android.insurance.command.DownCommandParse;
import com.guanri.android.insurance.command.UpCommandHandler;
import com.guanri.android.insurance.command.UpCommandHandler.UpCommandHandlerListener;
import com.guanri.android.insurance.common.CommandConstant;
import com.guanri.android.insurance.common.DialogUtils;
import com.guanri.android.insurance.common.DialogUtils.OnAlertDlgSureBtn;
import com.guanri.android.insurance.common.SharedPreferencesUtils;
import com.guanri.android.insurance.service.SaleCheckService;
import com.guanri.android.lib.log.Logger;
import com.guanri.android.lib.utils.StringUtils;
import com.guanri.android.lib.utils.TimeUtils;
import com.guanri.android.lib.utils.Utils;

/**
 * 对账管理界面
 * @author Administrator
 *
 */
public class SaleCheckActivity extends ApsaiActivity {

    public static Logger logger = Logger.getLogger(SaleCheckActivity.class);

    public static final String CHECK_FLAS = "Check_id";

    public static final String MUST_CHECK_FLAS = "Must_Check";

    public static final int MAIN_ACTIVITY = 123456789;

    SaleCheckService saleCheckService = null;

    TextView sale_check_business_id = null;

    TextView sale_check_user_id = null;

    TextView sale_check_time = null;

    TextView sale_check_id = null;

    TextView sale_check_orders = null;

    TextView sale_check_order_price = null;

    TextView sale_check_back_orders = null;

    TextView sale_check_back_price = null;

    TextView sale_check_usless_orders = null;

    ImageButton user_sale_check_sure_btn = null;

    ImageButton user_sale_check_print_btn = null;

    ImageButton user_sale_check_cancel_btn = null;

    LinearLayout user_sale_check_sure_layout = null;

    LinearLayout user_sale_check_print_layout = null;

    LinearLayout user_sale_check_cancel_layout = null;

    int orderCheckDefine = 0;

    private SaleCheckBean saleCheckBean = null;

    boolean isMustCheck = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sale_check_order);
        this.setTitle(this.getString(R.string.apsai_sale_check_text));
        orderCheckDefine = 0;
        user_sale_check_sure_btn = (ImageButton) this.findViewById(R.id.user_sale_check_sure_btn);
        user_sale_check_print_btn = (ImageButton) this.findViewById(R.id.user_sale_check_print_btn);
        user_sale_check_cancel_btn = (ImageButton) this.findViewById(R.id.user_sale_check_cancel_btn);
        user_sale_check_sure_layout = (LinearLayout) this.findViewById(R.id.user_sale_check_sure_layout);
        user_sale_check_print_layout = (LinearLayout) this.findViewById(R.id.user_sale_check_print_layout);
        user_sale_check_cancel_layout = (LinearLayout) this.findViewById(R.id.ser_sale_check_cancel_layout);
        user_sale_check_sure_btn.setOnClickListener(this);
        user_sale_check_print_btn.setOnClickListener(this);
        user_sale_check_cancel_btn.setOnClickListener(this);
        saleCheckService = new SaleCheckService(this);
        String checkID = this.getIntent().getStringExtra(SaleCheckActivity.CHECK_FLAS);
        isMustCheck = this.getIntent().getBooleanExtra(SaleCheckActivity.MUST_CHECK_FLAS, false);
        if (checkID != null && !"".equals(checkID)) {
            saleCheckBean = saleCheckService.queryCheckedOrder(checkID);
            user_sale_check_sure_layout.setVisibility(View.INVISIBLE);
            user_sale_check_cancel_layout.setVisibility(View.INVISIBLE);
            orderCheckDefine = 2;
        } else {
            saleCheckBean = saleCheckService.queryNoCheckedOrder();
            if (saleCheckBean != null && saleCheckBean.getOperator_id() != null && !"".equals(saleCheckBean.getOperator_id())) {
                orderCheckDefine = 1;
            } else {
                DialogUtils.showMessageAlertDlg(this, R.string.apsai_sale_check_text, R.string.apsai_sale_check_non_need_text, new OnAlertDlgSureBtn() {

                    public void OnSureBtn() {
                        finish();
                    }
                });
            }
        }
        if (saleCheckBean != null && saleCheckBean.getOperator_id() != null && !"".equals(saleCheckBean.getOperator_id())) {
            sale_check_business_id = (TextView) this.findViewById(R.id.sale_check_business_id);
            sale_check_user_id = (TextView) this.findViewById(R.id.sale_check_user_id);
            sale_check_time = (TextView) this.findViewById(R.id.sale_check_time);
            sale_check_id = (TextView) this.findViewById(R.id.sale_check_id);
            sale_check_orders = (TextView) this.findViewById(R.id.sale_check_orders);
            sale_check_order_price = (TextView) this.findViewById(R.id.sale_check_order_price);
            sale_check_back_orders = (TextView) this.findViewById(R.id.sale_check_back_orders);
            sale_check_back_price = (TextView) this.findViewById(R.id.sale_check_back_price);
            sale_check_usless_orders = (TextView) this.findViewById(R.id.sale_check_usless_orders);
            sale_check_business_id.setText(CommandConstant.COMFIG_POS_ID);
            sale_check_user_id.setText(saleCheckBean.getOperator_id());
            sale_check_time.setText(saleCheckBean.getCheck_time());
            sale_check_id.setText(String.valueOf(saleCheckBean.getCheck_id()));
            sale_check_orders.setText(String.valueOf(saleCheckBean.getOrder_count()) + SaleCheckService.orderUnit);
            sale_check_order_price.setText(String.valueOf(Utils.fengToYuan(saleCheckBean.getOrder_sum())) + SaleCheckService.moneyUnit);
            sale_check_back_orders.setText(String.valueOf(saleCheckBean.getOrder_back_count()) + SaleCheckService.orderUnit);
            sale_check_back_price.setText(String.valueOf(Utils.fengToYuan(saleCheckBean.getOrder_back_sum())) + SaleCheckService.moneyUnit);
            sale_check_usless_orders.setText(String.valueOf(saleCheckBean.getOrder_useless_count()) + SaleCheckService.orderUnit);
        } else {
            user_sale_check_sure_layout.setVisibility(View.INVISIBLE);
            user_sale_check_print_layout.setVisibility(View.INVISIBLE);
            user_sale_check_cancel_layout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        if (saleCheckService != null) {
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.user_sale_check_sure_btn:
                logger.debug("++++++++++++++++++++++" + orderCheckDefine);
                if (orderCheckDefine == 0) {
                    DialogUtils.showErrorAlertDlg(this, this.getString(R.string.apsai_sale_check_text), this.getString(R.string.apsai_sale_check_non_need_text));
                } else if (orderCheckDefine > 1) {
                    DialogUtils.showErrorAlertDlg(this, this.getString(R.string.apsai_sale_check_text), this.getString(R.string.apsai_sale_check_end_text));
                } else {
                    btDialog = new ProgressDialog(this);
                    btDialog.setTitle(StringUtils.getStringFromValue(R.string.apsai_sale_check_text));
                    btDialog.setMessage(StringUtils.getStringFromValue(R.string.apsai_sale_check_loading));
                    btDialog.show();
                    saleCheckService.doAllSaleCheck(saleCheckBean, allCheckHandler);
                }
                break;
            case R.id.user_sale_check_print_btn:
                if (orderCheckDefine < 2) {
                    DialogUtils.showErrorAlertDlg(this, this.getString(R.string.apsai_sale_check_text), this.getString(R.string.apsai_sale_check_cannt_print_text));
                } else if (orderCheckDefine == 3) {
                    DialogUtils.showChoiceAlertDlg(this, this.getString(R.string.apsai_sale_check_text), this.getString(R.string.apsai_common_is_reprint_text), new OnAlertDlgSureBtn() {

                        public void OnSureBtn() {
                            saleCheckService.printCheckResult(saleCheckBean);
                        }
                    });
                } else {
                    saleCheckService.printCheckResult(saleCheckBean);
                    orderCheckDefine = 3;
                }
                break;
            case R.id.user_sale_check_cancel_btn:
                exitMethod();
                break;
            default:
                break;
        }
    }

    private ProgressDialog btDialog = null;

    UpCommandHandler allCheckHandler = new UpCommandHandler(new UpCommandHandlerListener(this) {

        public void handlerOthorMsg(int what, Object object) {
            DownCommandParse downCommandParse = (DownCommandParse) object;
            DownCommandBean downCommandBean = downCommandParse.getDownCommandBean();
            logger.debug("AnswerCode11:" + downCommandBean.getAnswerCode());
            logger.debug("CommandCode11:" + downCommandBean.getCommandCode());
            logger.debug("AnswerMsg11:" + downCommandBean.getAnswerMsg());
            if (downCommandBean.getAnswerCode().equals("0")) {
                saleCheckService.logInfo(StringUtils.getStringFromValue(R.string.apsai_sale_check_text));
                DialogUtils.showErrorAlertDlg(SaleCheckActivity.this, getString(R.string.apsai_sale_check_text), getString(R.string.apsai_sale_check_succ_text));
                saleCheckService.saveSaleCheck(saleCheckBean);
                orderCheckDefine = 2;
                SharedPreferencesUtils.setConfigString(SharedPreferencesUtils.COMFIG_INFO, SharedPreferencesUtils.CHECK_ID, String.valueOf(saleCheckBean.getCheck_id() + 1));
            } else if (downCommandBean.getAnswerCode().equals("1")) {
                saleCheckService.logInfo(StringUtils.getStringFromValue(R.string.apsai_sale_check_text) + ":" + getString(R.string.apsai_sale_check_no_faire_text));
                DialogUtils.showMessageAlertDlg(SaleCheckActivity.this, getString(R.string.apsai_sale_check_text), getString(R.string.apsai_sale_check_no_faire_text), new OnAlertDlgSureBtn() {

                    public void OnSureBtn() {
                        if (btDialog != null) {
                            btDialog.setMessage(getString(R.string.apsai_sale_check_no_faire_text));
                            btDialog.show();
                        }
                        new Handler().post(new Runnable() {

                            public void run() {
                                String reMsg = saleCheckService.doSaleRecordCheck(saleCheckBean);
                                if (reMsg != null) {
                                    saleCheckService.logInfo(StringUtils.getStringFromValue(R.string.apsai_sale_record_check_text) + ":" + reMsg);
                                    DialogUtils.showErrorAlertDlg(SaleCheckActivity.this, getString(R.string.apsai_sale_record_check_text), reMsg);
                                    orderCheckDefine = 4;
                                } else {
                                    SharedPreferencesUtils.setConfigString(SharedPreferencesUtils.COMFIG_INFO, SharedPreferencesUtils.CHECK_ID, String.valueOf(saleCheckBean.getCheck_id() + 1));
                                    saleCheckService.logInfo(StringUtils.getStringFromValue(R.string.apsai_sale_record_check_text) + ":" + getString(R.string.apsai_sale_check_succ_text));
                                    saleCheckService.saveSaleCheck(saleCheckBean);
                                    orderCheckDefine = 2;
                                    DialogUtils.showErrorAlertDlg(SaleCheckActivity.this, getString(R.string.apsai_sale_record_check_text), StringUtils.getStringFromValue(R.string.apsai_sale_record_check_text) + StringUtils.getStringFromValue(R.string.apsai_sale_check_succ_text));
                                }
                                btDialog.dismiss();
                            }
                        });
                    }
                });
            } else {
                saleCheckService.logInfo(downCommandBean.getAnswerMsg() + downCommandBean.getMark() == null ? "" : downCommandBean.getMark());
                DialogUtils.showErrorAlertDlg(SaleCheckActivity.this, getString(R.string.apsai_sale_check_text), downCommandBean.getAnswerMsg() + downCommandBean.getMark() == null ? "" : downCommandBean.getMark());
            }
            if (btDialog != null && btDialog.isShowing()) {
                btDialog.dismiss();
            }
        }

        public void handlerInfo() {
            if (btDialog != null && btDialog.isShowing()) {
                btDialog.dismiss();
            }
            saleCheckService.logInfo(StringUtils.getStringFromValue(R.string.apsai_sale_check_text) + ":" + StringUtils.getStringFromValue(R.string.apsai_common_cmd_error));
        }
    });

    /**
	 * 退出系统
	 */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking() && !event.isCanceled()) {
            exitMethod();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void exitMethod() {
        if (orderCheckDefine < 2 && isMustCheck) {
            DialogUtils.showChoiceAlertDlg(this, this.getString(R.string.apsai_sale_check_text), this.getString(R.string.apsai_sale_check_must_check_text), new OnAlertDlgSureBtn() {

                public void OnSureBtn() {
                    setResult(RESULT_OK, null);
                    finish();
                }
            });
        } else {
            finish();
        }
    }
}
