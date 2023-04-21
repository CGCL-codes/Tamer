package edu.uiuc.android.scorch.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import edu.uiuc.android.scorch.GameThread;
import edu.uiuc.android.scorch.R;
import edu.uiuc.android.scorch.ScorchedPlayer;
import edu.uiuc.android.scorch.items.Item;
import edu.uiuc.android.scorch.items.OwnedItem;
import edu.uiuc.android.scorch.items.Inventory.ItemType;
import edu.uiuc.android.scorch.items.Inventory.WeaponType;
import java.util.ArrayList;

/**
 * Main activity-- this is where all the game play happens
 */
public class GameActivity extends Activity {

    private static final int MENU_PAUSE = Menu.FIRST;

    private static final int MENU_RESUME = Menu.FIRST + 1;

    private static final int MENU_SAVE = Menu.FIRST + 3;

    private static final int MENU_LOAD = Menu.FIRST + 4;

    private static final int MENU_NEXT_TURN = Menu.FIRST + 5;

    private static final int MENU_NEXT_ROUND = Menu.FIRST + 6;

    /**
     * Index of the debug message
     */
    public static final int MSG_DISPLAY_DEBUG = 0;

    /**
     * Index value of the HUD message
     */
    public static final int MSG_UPDATE_HUD = 1;

    /**
     * Index value of the Save message
     */
    public static final int MSG_DIALOG_SAVE = 3;

    /**
     * Index value of the Load message
     */
    public static final int MSG_DIALOG_LOAD = 4;

    /**
     * Index value of the game start progress message
     */
    public static final int MSG_DIALOG_GS = 5;

    /**
     * Index value of the Next Turn message
     */
    public static final int MSG_DIALOG_NEXT_TURN = 6;

    /**
     * Index value of the next round message
     */
    public static final int MSG_DIALOG_NEXT_ROUND = 7;

    /**
     * Index value of the items dialog
     */
    public static final int DIALOG_ITEMS = 1;

    /**
     * Low-level handler object for sending messages
     */
    public Handler mHandler;

    ProgressDialog mProgressDialog;

    GameSurface mGameSurface;

    GameThread mGameThread;

    private RelativeLayout mHUD;

    RelativeLayout mHUDLeft;

    RelativeLayout mHUDBottom;

    TextView mDebugInfo;

    TextView mPlayerLabel;

    private TextView mWindValue;

    private TextView mHealthLabel;

    private ProgressBar mHealthBar;

    private TextView mAngleValue;

    private Button mAnglePlus;

    private Button mAngleMinus;

    TextView mPowerValue;

    SeekBar mPowerBar;

    private ImageView mWeaponIcon;

    private TextView mWeaponLabel;

    private LinearLayout mPowerUpDisplay;

    private LinearLayout mItems;

    int mAngle;

    private static final int MIN_ANGLE = 0;

    private static final int MAX_ANGLE = 180;

    int mAngleResolution = 1;

    private OnTouchListener mHUDOnTouchListener;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("GameActivity", "onCreate");
        this.setProgressBarVisibility(true);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game);
        String action = getIntent().getAction();
        Log.d("GameActivity", "Constructor - action = " + action);
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case MSG_DISPLAY_DEBUG:
                        mDebugInfo.setText((String) msg.obj);
                        break;
                    case MSG_UPDATE_HUD:
                        if (msg.obj != null) {
                            setWind(mGameThread.getGameState().getWindValue());
                            ScorchedPlayer currentPlayer = (ScorchedPlayer) msg.obj;
                            Log.d("GameActivity", "handleMessage - " + msg.what + " - HUD now viewing " + currentPlayer.getName());
                            mPlayerLabel.setTextColor(currentPlayer.getColor());
                            mPlayerLabel.setText(currentPlayer.getName());
                            setHealth(currentPlayer.getHealth());
                            if (currentPlayer.isHuman()) {
                                mAngle = currentPlayer.getAngle();
                                incrementAngle(0);
                                mPowerBar.setMax(currentPlayer.getHealth() * 10);
                                mPowerBar.setProgress(1);
                                mPowerBar.setProgress(0);
                                mPowerBar.setProgress(currentPlayer.getPower());
                                setWeapon(currentPlayer.getWeapon());
                                setPowerups(new ItemType[0]);
                                mHUDLeft.setVisibility(View.VISIBLE);
                                mHUDBottom.setVisibility(View.VISIBLE);
                                mGameSurface.setTouchEnabled(true);
                            } else {
                                mGameSurface.setTouchEnabled(false);
                                mHUDLeft.setVisibility(View.INVISIBLE);
                                mHUDBottom.setVisibility(View.INVISIBLE);
                            }
                            showHUD();
                        } else if (msg.arg1 == 0) hideHUD(); else if (msg.arg1 == 1) showHUD();
                        break;
                    case MSG_DIALOG_SAVE:
                        mProgressDialog = ProgressDialog.show(GameActivity.this, "Please wait...", "Saving Game State...", true);
                        new Thread() {

                            @Override
                            public void run() {
                                mGameThread.doPause();
                                mGameThread.saveGameState("autosave");
                                mGameThread.doResume();
                                mProgressDialog.dismiss();
                            }
                        }.start();
                        break;
                    case MSG_DIALOG_LOAD:
                        mProgressDialog = ProgressDialog.show(GameActivity.this, "Please wait...", "Loading Game State...", true);
                        new Thread() {

                            @Override
                            public void run() {
                                mGameThread.doPause();
                                mGameThread.loadGameState("autosave");
                                mGameThread.doResume();
                                Message.obtain(mHandler, GameActivity.MSG_UPDATE_HUD, 1, 0).sendToTarget();
                                mProgressDialog.dismiss();
                            }
                        }.start();
                        break;
                    case MSG_DIALOG_GS:
                        mProgressDialog = ProgressDialog.show(GameActivity.this, "Please wait...", "Found Game State...", true);
                        new Thread() {

                            @Override
                            public void run() {
                                mGameThread.doPause();
                                mGameThread.useGameStateInExtras();
                                Message.obtain(mHandler, GameActivity.MSG_DIALOG_NEXT_ROUND, mGameThread.getGameState().getCurrentRound() + 1, mGameThread.getGameState().getNumRounds()).sendToTarget();
                                mProgressDialog.dismiss();
                            }
                        }.start();
                        break;
                    case MSG_DIALOG_NEXT_TURN:
                        mProgressDialog = ProgressDialog.show(GameActivity.this, "Please wait...", "Starting next turn...", true);
                        new Thread() {

                            @Override
                            public void run() {
                                mGameThread.doPause();
                                mGameThread.nextTurn();
                                mGameThread.doResume();
                                Message.obtain(mHandler, GameActivity.MSG_UPDATE_HUD, 1, 0).sendToTarget();
                                mProgressDialog.dismiss();
                            }
                        }.start();
                        break;
                    case MSG_DIALOG_NEXT_ROUND:
                        mProgressDialog = ProgressDialog.show(GameActivity.this, "Please wait...", "Starting round " + msg.arg1 + " of " + msg.arg2 + "...", true);
                        new Thread() {

                            @Override
                            public void run() {
                                mGameThread.doPause();
                                mGameThread.nextRound();
                                mGameThread.doResume();
                                Message.obtain(mHandler, GameActivity.MSG_UPDATE_HUD, 1, 0).sendToTarget();
                                mProgressDialog.dismiss();
                            }
                        }.start();
                        break;
                }
            }
        };
        mGameSurface = (GameSurface) findViewById(R.id.GameSurface);
        mGameThread = mGameSurface.getThread();
        mHUD = (RelativeLayout) findViewById(R.id.HUD);
        mHUDLeft = (RelativeLayout) findViewById(R.id.HUDLeft);
        mHUDBottom = (RelativeLayout) findViewById(R.id.HUDBottom);
        mDebugInfo = (TextView) findViewById(R.id.DebugInfo);
        mPlayerLabel = (TextView) findViewById(R.id.PlayerLabel);
        mWindValue = (TextView) findViewById(R.id.WindValue);
        mHealthLabel = (TextView) findViewById(R.id.HealthLabel);
        mHealthBar = (ProgressBar) findViewById(R.id.HealthBar);
        mAngleValue = (TextView) findViewById(R.id.AngleValue);
        mAnglePlus = (Button) findViewById(R.id.AnglePlus);
        mAngleMinus = (Button) findViewById(R.id.AngleMinus);
        mPowerValue = (TextView) findViewById(R.id.PowerValue);
        mPowerBar = (SeekBar) findViewById(R.id.PowerBar);
        mWeaponIcon = (ImageView) findViewById(R.id.WeaponIcon);
        mWeaponLabel = (TextView) findViewById(R.id.WeaponLabel);
        mPowerUpDisplay = (LinearLayout) findViewById(R.id.PowerUpDisplay);
        mItems = (LinearLayout) findViewById(R.id.HUDItems);
        mHUDOnTouchListener = new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    switch(view.getId()) {
                        case R.id.AnglePlus:
                            incrementAngle(mAngleResolution);
                            mGameThread.getGameState().getCurrentPlayer().setAngle(mAngle);
                            break;
                        case R.id.AngleMinus:
                            decrementAngle(mAngleResolution);
                            mGameThread.getGameState().getCurrentPlayer().setAngle(mAngle);
                            break;
                        case R.id.HUDItems:
                            showDialog(DIALOG_ITEMS);
                            break;
                    }
                }
                try {
                    Thread.sleep(25L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }
        };
        mItems.setOnTouchListener(mHUDOnTouchListener);
        mAnglePlus.setOnTouchListener(mHUDOnTouchListener);
        mAnglePlus.setFocusable(false);
        mAngleMinus.setOnTouchListener(mHUDOnTouchListener);
        mAngleMinus.setFocusable(false);
        mPowerBar.setMax(ScorchedPlayer.MAX_POWER);
        mPowerBar.setProgress(0);
        mPowerBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                Log.d("GameActivity", "mPowerBar - onProgressChanged - progress = " + progress);
                mPowerValue.setText(Integer.toString(progress));
                if (fromTouch) mGameThread.getGameState().getCurrentPlayer().setPower(progress);
                try {
                    Thread.sleep(25L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onStopTrackingTouch(SeekBar arg0) {
            }
        });
        setHealth(0);
        setWind(0);
        setWeapon(WeaponType.BABY_MISSILE);
        if (savedInstanceState != null) {
            Log.d("GameActivity", "onCreate - found saved instance state");
        } else {
            Log.d("GameActivity", "onCreate - no saved instance state");
        }
    }

    /**
     * Increment the angle which is displayed on the HUD
     * 
     * @param delta The amount to change the angle by
     */
    public void incrementAngle(int delta) {
        mAngle += delta;
        if (mAngle > MAX_ANGLE) mAngle = MIN_ANGLE;
        mAngleValue.setText(Integer.toString(mAngle));
    }

    /**
     * Decrement the displayed angle
     * 
     * @param delta The decrement value
     */
    public void decrementAngle(int delta) {
        mAngle -= delta;
        if (mAngle < MIN_ANGLE) mAngle = MAX_ANGLE;
        mAngleValue.setText(Integer.toString(mAngle));
    }

    /**
     * Set the displayed health for the current player
     * 
     * @param percent The new health value
     */
    public void setHealth(int percent) {
        mHealthBar.setProgress(percent);
        mHealthLabel.setText("Health: " + Integer.toString(percent) + "%");
        mHealthLabel.setTextColor(Color.rgb((int) (255 - 190.0 * percent / 100), 65, (int) (65 + 190.0 * percent / 100)));
    }

    /**
     * Set the displayed wind value
     * 
     * @param value The new wind value
     */
    public void setWind(int value) {
        int magnitude = Math.abs(value);
        mWindValue.setText((value < 0 ? "<- " : "") + (Integer.toString(magnitude)) + (value > 0 ? " ->" : ""));
        mWindValue.setTextColor(Color.rgb((int) (196 - 128.0 * magnitude / 100), (int) (196 - 128.0 * magnitude / 100), 255));
    }

    /**
     * Set the displayed weapon type
     * 
     * @param weapon The new weapon type
     */
    public void setWeapon(WeaponType weapon) {
        mWeaponIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), weapon.getIcon()));
        mWeaponLabel.setText(weapon.getName());
    }

    /**
     * Use the given weapon
     * 
     * @param weapon The weapon to "use"
     */
    public void useWeapon(WeaponType weapon) {
        mGameThread.getGameState().getCurrentPlayer().setWeapon(weapon);
        setWeapon(weapon);
    }

    /**
     * Set the list of powerups currently available
     * 
     * @param items The powerup items
     */
    public void setPowerups(ItemType[] items) {
        mPowerUpDisplay.removeAllViews();
        for (ItemType i : items) {
            ImageView icon = (ImageView) getLayoutInflater().inflate(R.layout.icon, null);
            MarginLayoutParams layout = new ViewGroup.MarginLayoutParams(32, 32);
            layout.setMargins(1, 1, 1, 1);
            icon.setLayoutParams(new LinearLayout.LayoutParams(layout));
            icon.setImageResource(i.getIcon());
            mPowerUpDisplay.addView(icon);
        }
    }

    /**
     * Use the given powerup item
     * 
     * @param item The powerup to use
     */
    public void usePowerup(ItemType item) {
        setPowerups(new ItemType[] { item });
    }

    /**
     * Hide the HUD from display
     */
    public void hideHUD() {
        mHUD.setVisibility(View.INVISIBLE);
    }

    /**
     * Show the HUD
     */
    public void showHUD() {
        mHUD.setVisibility(View.VISIBLE);
    }

    /**
     * Invoked during init to give the Activity a chance to set up its Menu.
     * 
     * @param menu the Menu to which entries may be added
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_SAVE, 0, R.string.game_menu_save);
        menu.add(0, MENU_LOAD, 0, R.string.game_menu_load);
        menu.add(0, MENU_NEXT_TURN, 0, "Next Turn");
        menu.add(0, MENU_NEXT_ROUND, 0, "Next Round");
        return true;
    }

    /**
     * Invoked when the user selects an item from the Menu.
     * 
     * @param item the Menu entry which was selected
     * @return true if the Menu item was legit (and we consumed it), false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_PAUSE:
                mGameThread.doPause();
                return true;
            case MENU_RESUME:
                mGameThread.doResume();
                return true;
            case MENU_SAVE:
                Message.obtain(mHandler, GameActivity.MSG_DIALOG_SAVE, 0, 0).sendToTarget();
                return true;
            case MENU_LOAD:
                Message.obtain(mHandler, GameActivity.MSG_DIALOG_LOAD, 0, 0).sendToTarget();
                return true;
            case MENU_NEXT_TURN:
                Message.obtain(mHandler, GameActivity.MSG_DIALOG_NEXT_TURN, 0, 0).sendToTarget();
                return true;
            case MENU_NEXT_ROUND:
                Message.obtain(mHandler, GameActivity.MSG_DIALOG_NEXT_ROUND, 0, 0).sendToTarget();
                return true;
        }
        return false;
    }

    /**
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        Log.d("GameActivity", "onCreateDialog - id = " + id);
        switch(id) {
            case DIALOG_ITEMS:
                ScorchedPlayer currentPlayer = mGameThread.getGameState().getCurrentPlayer();
                ArrayList<OwnedItem> items = currentPlayer.getInStockInventory();
                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                ItemAdapter itemAdapter = new ItemAdapter(this, items);
                builder.setAdapter(itemAdapter, new OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Item item = mGameThread.getGameState().getCurrentPlayer().getInStockInventory().get(which).getItem();
                        if (item instanceof WeaponType) useWeapon((WeaponType) item); else usePowerup((ItemType) item);
                        Log.d("GameActivity", "selectItemDialog - item = " + item.getName());
                        removeDialog(DIALOG_ITEMS);
                    }
                });
                builder.setTitle("Select a Weapon or Item");
                builder.setInverseBackgroundForced(false);
                return builder.create();
        }
        return super.onCreateDialog(id);
    }

    /**
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                Log.d("GameActivity", "onActivityResult");
                break;
        }
    }

    /**
     * @see android.app.Activity#onPause()
     */
    @Override
    public void onPause() {
        Log.d("GameActivity", "onPause - GameThread is " + mGameThread.getState());
        mGameThread.doPause();
        super.onPause();
    }

    /**
     * @see android.app.Activity#onResume()
     */
    @Override
    public void onResume() {
        Log.d("GameActivity", "onResume - GameThread is " + mGameThread.getState());
        mGameThread.doResume();
        super.onResume();
    }

    /**
     * @see android.app.Activity#onPostResume()
     */
    @Override
    public void onPostResume() {
        Log.d("GameActivity", "onPostResume - GameThread is " + mGameThread.getState());
        super.onPostResume();
    }

    /**
     * @see android.app.Activity#onRestart()
     */
    @Override
    public void onRestart() {
        Log.d("GameActivity", "onRestart - GameThread is " + mGameThread.getState());
        super.onRestart();
    }

    /**
     * @see android.app.Activity#onStop()
     */
    @Override
    public void onStop() {
        Log.d("GameActivity", "onStop - GameThread is " + mGameThread.getState());
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("scorched_game_file", MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putBoolean("savedGame", true);
        editor.commit();
        mGameThread.saveGameState("autosave");
    }

    /**
     * @see android.app.Activity#onDestroy()
     */
    @Override
    public void onDestroy() {
        Log.d("GameActivity", "onDestroy - GameThread is " + mGameThread.getState());
        super.onDestroy();
    }

    /**
     * @see android.app.Activity#onWindowFocusChanged(boolean)
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("GameActivity", "onSaveInstanceState");
    }

    /**
     * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("GameActivity", "onRestoreInstanceState");
    }

    /**
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("GameActivity", "onKeyDown - keyCode = " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            if (mGameThread.getGameState().getCurrentPlayer().isHuman()) mGameThread.fireWeapon();
            return true;
        } else return super.onKeyDown(keyCode, event);
    }

    /**
     * Set the game thread which is associated with the activity
     * 
     * @param gamethread The new game thread
     */
    public void setGameThread(GameThread gamethread) {
        mGameThread = gamethread;
    }
}
