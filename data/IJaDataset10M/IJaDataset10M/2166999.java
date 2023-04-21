package de.tudresden.inf.rn.mobilis.android.xhunt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import de.tudresden.inf.rn.mobilis.android.xhunt.Const;
import de.tudresden.inf.rn.mobilis.android.xhunt.R;
import de.tudresden.inf.rn.mobilis.android.xhunt.clientstub.MobilisServiceDiscoveryBean;
import de.tudresden.inf.rn.mobilis.android.xhunt.clientstub.MobilisServiceInfo;
import de.tudresden.inf.rn.mobilis.android.xhunt.clientstub.XMPPBean;
import de.tudresden.inf.rn.mobilis.android.xhunt.model.GameState;
import de.tudresden.inf.rn.mobilis.android.xhunt.proxy.MXAProxy;
import de.tudresden.inf.rn.mobilis.android.xhunt.service.ServiceConnector;
import de.tudresden.inf.rn.mobilis.android.xhunt.ui.DialogRemoteLoading;

/**
 * The Class MainActivity is the entrypoint of the game.
 */
public class MainActivity extends Activity {

    /** Identifier for the Log outputs *. */
    public static final String TAG = "MainActivity";

    /** The ServiceConnector to connect to XHuntService. */
    private ServiceConnector mServiceConnector;

    /** The MXAProxy. */
    private MXAProxy mMxaProxy;

    /** Dialog that displays if client is waiting for server acks. */
    private DialogRemoteLoading mRemoteLoadingDialog;

    /** Is used, if Mobilis-Server supports XHunt-Service. */
    private static final int CODE_SERVICES_AVAILABLE = 1;

    /** Is used, if Mobilis-Server doesn't supports XHunt-Service. */
    private static final int CODE_SERVICE_XHUNT_UNAVAILABLE = -1;

    /** Is used, if contacting the Mobilis-Server fails. */
    private static final int CODE_SERVER_RESPONSE_ERROR = -2;

    /** Handler to handle MobilisServiceDiscoveryBeans. */
    private Handler mServiceDiscoveryResultHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_SERVICES_AVAILABLE) {
                Intent i = new Intent(MainActivity.this, OpenGamesActivity.class);
                startActivity(i);
            } else if (msg.what == CODE_SERVICE_XHUNT_UNAVAILABLE) {
                Toast.makeText(MainActivity.this, "Server doesn't support XHunt service", Toast.LENGTH_SHORT).show();
            } else if (msg.what == CODE_SERVER_RESPONSE_ERROR) {
                Toast.makeText(MainActivity.this, "Server sends Error result. Please check your settings", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /** The handler which is called, if XMPP connection was established successfully. */
    private Handler mXmppConnectedHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (mRemoteLoadingDialog != null) {
                mRemoteLoadingDialog.cancel();
            }
            Toast.makeText(MainActivity.this, "XMPP connected", Toast.LENGTH_SHORT).show();
            mMxaProxy.getIQProxy().registerCallbacks();
            startGame();
        }
    };

    /** The handler which is called if the XHuntService was bound. */
    private Handler mXHuntServiceBoundHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(MainActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
            mServiceConnector.getXHuntService().setGameState(new GameStateMain());
            mMxaProxy = mServiceConnector.getXHuntService().getMXAProxy();
            mMxaProxy.setStaticMode(true);
            mServiceConnector.getXHuntService().getGPSProxy().setLocation(51033880, 13783272);
        }
    };

    /**
	 * Bind XHuntService using the mXHuntServiceBoundHandler and start local XHuntService.
	 */
    private void bindXHuntService() {
        mServiceConnector = new ServiceConnector(this);
        mServiceConnector.startXHuntService();
        mServiceConnector.doBindXHuntService(mXHuntServiceBoundHandler);
    }

    /**
	 * Establish a connection to the XMPP-Server. 
	 * If connection is already established, game will be started.
	 */
    private void connectToXMPP() {
        if (mMxaProxy != null && mMxaProxy.isConnected()) {
            startGame();
        } else {
            mRemoteLoadingDialog.setLoadingText("Start up MXA.\n\n     Please wait...");
            mRemoteLoadingDialog.run();
            mMxaProxy.registerXMPPConnectHandler(mXmppConnectedHandler);
            try {
                mMxaProxy.connect();
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(MainActivity.this, "ERROR: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void finish() {
        if (mServiceConnector != null && mServiceConnector.getXHuntService() != null) {
            mMxaProxy.getIQProxy().unregisterCallbacks();
            mServiceConnector.getXHuntService().stopSelf();
            mServiceConnector.doUnbindXHuntService();
        }
        super.finish();
    }

    /**
	 * Initialize all UI elements from resources.
	 */
    private void initComponents() {
        mRemoteLoadingDialog = new DialogRemoteLoading(this, Const.CONNECTION_TIMEOUT_DELAY + (10 * 1000));
        Button btn_Play = (Button) findViewById(R.id.main_btn_play);
        btn_Play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mMxaProxy != null) {
                    mMxaProxy.getIQProxy().updateServerJid();
                    if (!mMxaProxy.isConnected()) connectToXMPP(); else {
                        startGame();
                    }
                }
            }
        });
        Button btn_Settings = (Button) findViewById(R.id.main_btn_settings);
        btn_Settings.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });
        Button btn_Instructions = (Button) findViewById(R.id.main_btn_instructions);
        btn_Instructions.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameInstructionActivity.class);
                startActivity(i);
            }
        });
        Button btn_Exit = (Button) findViewById(R.id.main_btn_exit);
        btn_Exit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        bindXHuntService();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        mRemoteLoadingDialog = new DialogRemoteLoading(this, Const.CONNECTION_TIMEOUT_DELAY + (10 * 1000));
        if (mServiceConnector.getXHuntService() != null) mServiceConnector.getXHuntService().setGameState(new GameStateMain());
        super.onResume();
    }

    /**
     * Start the game. This will set the nickname of the player and send an empty
     * MobilisServiceDiscoveryBean to the Mobilis-Server.
     */
    private void startGame() {
        mMxaProxy.setNickname(mServiceConnector.getXHuntService().getSharedPrefHelper().getValue(getResources().getString(R.string.bundle_key_settings_username)));
        mMxaProxy.getIQProxy().sendServiceDiscoveryIQ(null);
    }

    /**
     * The Class GameStateMain is an inner class which represents the current state of the game.
     */
    private class GameStateMain extends GameState {

        @Override
        public void processPacket(XMPPBean inBean) {
            if (inBean.getType() == XMPPBean.TYPE_ERROR) {
                Log.e(TAG, "IQ Type ERROR: " + inBean.toXML());
            }
            if (inBean instanceof MobilisServiceDiscoveryBean) {
                MobilisServiceDiscoveryBean bean = (MobilisServiceDiscoveryBean) inBean;
                if (bean != null && bean.getType() != XMPPBean.TYPE_ERROR) {
                    if (bean.getDiscoveredServices() != null && bean.getDiscoveredServices().size() > 0) {
                        boolean serverSupportsXHunt = false;
                        for (MobilisServiceInfo info : bean.getDiscoveredServices()) {
                            if (info.getServiceNamespace().toLowerCase().contains("xhunt")) {
                                serverSupportsXHunt = true;
                                break;
                            }
                        }
                        if (serverSupportsXHunt) {
                            mServiceDiscoveryResultHandler.sendEmptyMessage(CODE_SERVICES_AVAILABLE);
                        } else {
                            mServiceDiscoveryResultHandler.sendEmptyMessage(CODE_SERVICE_XHUNT_UNAVAILABLE);
                        }
                    }
                } else if (bean.getType() == XMPPBean.TYPE_ERROR) {
                    mServiceDiscoveryResultHandler.sendEmptyMessage(CODE_SERVER_RESPONSE_ERROR);
                }
            } else if (inBean.getType() == XMPPBean.TYPE_GET || inBean.getType() == XMPPBean.TYPE_SET) {
                inBean.errorType = "wait";
                inBean.errorCondition = "unexpected-request";
                inBean.errorText = "This request is not supportet at this game state(main)";
                mMxaProxy.getIQProxy().sendXMPPBeanError(inBean);
            }
        }
    }
}
