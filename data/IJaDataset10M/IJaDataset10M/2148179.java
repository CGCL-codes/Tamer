package com.busfm.activity;

import com.busfm.R;
import com.busfm.listener.NetResponseListener;
import com.busfm.model.ChannelList;
import com.busfm.model.PlayList;
import com.busfm.model.ResultEntity;
import com.busfm.model.UserEntity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @Description This is the base activity, and it handle the common function.
 * 
 * @author DJ
 * @version 1.0
 * @date 2011/08/20
 */
public class BaseActivity extends Activity implements NetResponseListener {

    public static final int DIALOG_WAIT = 1;

    @Override
    public Dialog onCreateDialog(int id) {
        switch(id) {
            case DIALOG_WAIT:
                LayoutInflater factory = LayoutInflater.from(this);
                View view = factory.inflate(R.layout.dialog_indeterminate_progress, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.dialog_title_waiting).setCancelable(true).setView(view);
                builder.setCancelable(false);
                Dialog dialog = builder.create();
                return dialog;
            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    @Override
    public void clientDidLogin(NetResponseListener mClientListener, UserEntity userEnity) {
    }

    @Override
    public void clientDidRegister(NetResponseListener mClientListener, ResultEntity resultEntity) {
    }

    @Override
    public void clientDidResetPwd(NetResponseListener mClientListener, ResultEntity resultEntity) {
    }

    @Override
    public void clientDidChangePwd(NetResponseListener mClientListener, ResultEntity resultEntity) {
    }

    @Override
    public void clientDidCheckUserMail(NetResponseListener mClientListener, ResultEntity resultEntity) {
    }

    @Override
    public void clientDidCheckNickName(NetResponseListener mClientListener, ResultEntity resultEntity) {
    }

    @Override
    public void clientDidGetChannelList(NetResponseListener mClientListener, ChannelList channelList) {
    }

    @Override
    public void clientDidGetPlayListByChannel(NetResponseListener mClientListener, PlayList playList, int cid) {
    }

    @Override
    public void clientDidGetPlayListByUserId(NetResponseListener mClientListener, PlayList playList) {
    }

    @Override
    public void clientDidSongIsFaved(NetResponseListener mClientListener, ResultEntity resultEntity) {
    }

    @Override
    public void clientDidFaveThis(NetResponseListener mClientListener, ResultEntity resultEntity, String songId) {
    }

    @Override
    public void clientDidFailWithError(NetResponseListener mClientListener, int mOp, int scUnknown, String localizedMessage) {
    }

    @Override
    public void clientDidRequireAuthentication(NetResponseListener mClientListener) {
    }

    @Override
    public void clientNoNeedUpdate(NetResponseListener mClientListener) {
    }

    @Override
    public void clientNoEnoughCredit(NetResponseListener mClientListener) {
    }
}
