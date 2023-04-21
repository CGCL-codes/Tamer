package org.tunesremote;

import java.util.List;
import org.tunesremote.daap.Response;
import org.tunesremote.daap.Session;
import org.tunesremote.daap.Speaker;
import org.tunesremote.daap.Status;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Main activity of TunesRemote. This controls the player and drives all the other activities.
 * <p>
 */
public class ControlActivity extends Activity {

    public static final String TAG = ControlActivity.class.toString();

    public static final int NOTIFICATION_CONTROL = 1;

    /**
	 * ID of the speakers dialog
	 */
    private static final int DIALOG_SPEAKERS = 1;

    public static final int VIBRATE_LEN = 150;

    public static final long CACHE_TIME = 10000;

    public static final String EULA = "eula";

    protected static BackendService backend;

    protected static Session session;

    protected static Status status;

    protected String showingAlbumId = null;

    protected RatingBar ratingBar;

    protected TextView trackName, trackArtist, trackAlbum, seekPosition, seekRemain;

    protected SeekBar seekBar;

    protected ImageView coverImage;

    protected ImageButton controlPrev, controlPause, controlNext, controlShuffle, controlRepeat;

    protected View volume;

    protected ProgressBar volumeBar;

    protected Toast volumeToast;

    protected FadeView fadeview;

    protected Toast shuffleToast;

    protected Toast repeatToast;

    protected boolean dragging = false, agreed = false, autoPause = false, stayConnected = false, fadeDetails = true, fadeUpNew = true, vibrate = true, cropImage = true, fullScreen = true, ignoreNextTick = false;

    protected Vibrator vibrator;

    protected SharedPreferences prefs;

    protected long cachedTime = -1;

    protected long cachedVolume = -1;

    /**
	 * List of available speakers
	 */
    protected List<Speaker> speakers;

    /**
	 * Instance of the speaker list adapter used in the speakers dialog
	 */
    protected SpeakersAdapter speakersAdapter;

    public ServiceConnection connection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, final IBinder service) {
            new Thread(new Runnable() {

                public void run() {
                    try {
                        backend = ((BackendService.BackendBinder) service).getService();
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ControlActivity.this);
                        backend.setPrefs(settings);
                        if (!agreed) return;
                        Log.w(TAG, "onServiceConnected");
                        session = backend.getSession();
                        if (session == null) {
                            ControlActivity.this.startActivityForResult(new Intent(ControlActivity.this, LibraryActivity.class), 1);
                        } else {
                            status = session.singletonStatus(statusUpdate);
                            status.updateHandler(statusUpdate);
                            statusUpdate.sendEmptyMessage(Status.UPDATE_TRACK);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onServiceConnected:" + e.getMessage());
                    }
                }
            }).start();
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.w(TAG, "onServiceDisconnected");
            status.updateHandler(null);
            backend = null;
            status = null;
        }
    };

    protected Handler statusUpdate = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case Status.UPDATE_TRACK:
                    trackName.setText(status.getTrackName());
                    trackArtist.setText(status.getTrackArtist());
                    trackAlbum.setText(status.getTrackAlbum());
                    ratingBar.setVisibility(RatingBar.INVISIBLE);
                    if (fadeUpNew) fadeview.keepAwake();
                case Status.UPDATE_COVER:
                    boolean forced = (msg.what == Status.UPDATE_COVER);
                    boolean shouldUpdate = (status.albumId != showingAlbumId) && !status.coverEmpty;
                    if (forced) shouldUpdate = true;
                    Log.d(TAG, String.format("Artwork for albumid=%s, value=%s, what=%d", status.albumId, status.coverCache, msg.what));
                    if (shouldUpdate) {
                        if (status.coverEmpty) {
                            coverImage.setImageDrawable(new ColorDrawable(Color.BLACK));
                        } else if (status.coverCache != null) {
                            coverImage.setImageDrawable(new BitmapDrawable(status.coverCache));
                        }
                        showingAlbumId = status.albumId;
                    }
                case Status.UPDATE_STATE:
                    controlPause.setImageResource((status.getPlayStatus() == Status.STATE_PLAYING) ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
                    seekBar.setMax(status.getProgressTotal());
                case Status.UPDATE_PROGRESS:
                    if (ignoreNextTick) {
                        ignoreNextTick = false;
                        return;
                    }
                    seekPosition.setText(Response.convertTime(status.getProgress() * 1000));
                    seekRemain.setText("-" + Response.convertTime(status.getRemaining() * 1000));
                    if (!dragging) {
                        seekBar.setProgress(status.getProgress());
                    }
                    break;
                case Status.UPDATE_RATING:
                    long rating = status.getRating();
                    if (rating >= 0) {
                        ratingBar.setRating(((float) status.getRating() / 100) * 5);
                        ratingBar.setVisibility(RatingBar.VISIBLE);
                        if (fadeUpNew) fadeview.keepAwake();
                    }
                    break;
            }
            checkShuffle();
            checkRepeat();
        }
    };

    public android.telephony.PhoneStateListener psListener = new android.telephony.PhoneStateListener() {

        private boolean wasPlaying = false;

        @Override
        public void onCallStateChanged(int state, java.lang.String incomingNumber) {
            if (ControlActivity.this.autoPause) {
                switch(state) {
                    case android.telephony.TelephonyManager.CALL_STATE_IDLE:
                        if (wasPlaying && session != null && ControlActivity.status.getPlayStatus() == Status.STATE_PAUSED) {
                            session.controlPlay();
                            wasPlaying = false;
                        }
                        break;
                    case android.telephony.TelephonyManager.CALL_STATE_OFFHOOK:
                        if (session != null && ControlActivity.status.getPlayStatus() == Status.STATE_PLAYING) {
                            session.controlPause();
                            wasPlaying = true;
                        }
                        break;
                    case android.telephony.TelephonyManager.CALL_STATE_RINGING:
                        if (session != null && ControlActivity.status.getPlayStatus() == Status.STATE_PLAYING) {
                            session.controlPause();
                            wasPlaying = true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    protected void StartNowPlaying() {
        if (status == null) return;
        new Thread(new Runnable() {

            public void run() {
                Intent intent = new Intent(ControlActivity.this, NowPlayingActivity.class);
                intent.putExtra(Intent.EXTRA_TITLE, status.getAlbumId());
                ControlActivity.this.startActivity(intent);
            }
        }).start();
    }

    protected Handler doubleTapHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            StartNowPlaying();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        this.stayConnected = this.prefs.getBoolean(this.getString(R.string.pref_background), this.stayConnected);
        this.fadeDetails = this.prefs.getBoolean(this.getString(R.string.pref_fade), this.fadeDetails);
        this.fadeUpNew = this.prefs.getBoolean(this.getString(R.string.pref_fadeupnew), this.fadeUpNew);
        this.vibrate = this.prefs.getBoolean(this.getString(R.string.pref_vibrate), this.vibrate);
        this.autoPause = this.prefs.getBoolean(this.getString(R.string.pref_autopause), this.autoPause);
        this.fadeview.allowFade = this.fadeDetails;
        this.fadeview.keepAwake();
        Intent service = new Intent(this, BackendService.class);
        if (this.stayConnected) {
            this.startService(service);
        } else {
            this.stopService(service);
        }
        this.bindService(service, connection, Context.BIND_AUTO_CREATE);
        android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) ControlActivity.this.getSystemService(android.content.Context.TELEPHONY_SERVICE);
        tm.listen(psListener, android.telephony.PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w(TAG, "Stopping TunesRemote...");
        try {
            if (!this.stayConnected && session != null) {
                session.purgeAllStatus();
            }
            this.unbindService(connection);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "Destroying TunesRemote...");
        try {
            if (session != null) {
                session.purgeAllStatus();
                session.logout();
                session = null;
            }
            backend = null;
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
        Log.w(TAG, "Destroyed TunesRemote!");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Editor edit = prefs.edit();
            edit.putBoolean(EULA, true);
            edit.commit();
            this.agreed = true;
        } else {
            this.finish();
        }
    }

    /**
	 * OnSeekBarChangeListener that controls the volume for a certain speaker
	 * @author Daniel Thommes
	 */
    public class VolumeSeekBarListener implements OnSeekBarChangeListener {

        private final Speaker speaker;

        public VolumeSeekBarListener(Speaker speaker) {
            this.speaker = speaker;
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int newVolume, boolean fromUser) {
            if (fromUser) {
                int maxVolume = 0;
                int secondMaxVolume = 0;
                for (Speaker speaker : speakers) {
                    if (speaker.getAbsoluteVolume() > maxVolume) {
                        secondMaxVolume = maxVolume;
                        maxVolume = speaker.getAbsoluteVolume();
                    } else if (speaker.getAbsoluteVolume() > secondMaxVolume) {
                        secondMaxVolume = speaker.getAbsoluteVolume();
                    }
                }
                checkCachedVolume();
                int formerVolume = speaker.getAbsoluteVolume();
                status.setSpeakerVolume(speaker.getId(), newVolume, formerVolume, maxVolume, secondMaxVolume, cachedVolume);
                speaker.setAbsoluteVolume(newVolume);
            }
        }
    }

    /**
	 * List Adapter for displaying the list of available speakers.
	 * @author Daniel Thommes
	 */
    public class SpeakersAdapter extends BaseAdapter {

        private final LayoutInflater inflater;

        public SpeakersAdapter(Context context) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return speakers.size();
        }

        public Object getItem(int position) {
            return speakers.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        /**
		 * Toggles activation of a given speaker and refreshes the view
		 * @param active Flag indicating, whether the speaker shall be activated
		 * @param speaker the speaker to be activated or deactivated
		 */
        public void setSpeakerActive(boolean active, final Speaker speaker) {
            if (speaker == null) {
                return;
            }
            if (status == null) {
                return;
            }
            speaker.setActive(active);
            status.setSpeakers(speakers);
            speakers = status.getSpeakers();
            notifyDataSetChanged();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                View row;
                if (null == convertView) {
                    row = inflater.inflate(R.layout.item_speaker, null);
                } else {
                    row = convertView;
                }
                TextView nameTextview = (TextView) row.findViewById(R.id.speakerNameTextView);
                TextView speakerTypeTextView = (TextView) row.findViewById(R.id.speakerTypeTextView);
                final CheckBox activeCheckBox = (CheckBox) row.findViewById(R.id.speakerActiveCheckBox);
                SeekBar volumeBar = (SeekBar) row.findViewById(R.id.speakerVolumeBar);
                final Speaker speaker = speakers.get(position);
                nameTextview.setText(speaker.getName());
                speakerTypeTextView.setText(speaker.isLocalSpeaker() ? R.string.speakers_dialog_computer_speaker : R.string.speakers_dialog_airport_express);
                activeCheckBox.setChecked(speaker.isActive());
                activeCheckBox.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        setSpeakerActive(activeCheckBox.isChecked(), speaker);
                    }
                });
                nameTextview.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        activeCheckBox.toggle();
                        setSpeakerActive(activeCheckBox.isChecked(), speaker);
                    }
                });
                speakerTypeTextView.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        activeCheckBox.toggle();
                        setSpeakerActive(activeCheckBox.isChecked(), speaker);
                    }
                });
                if (speaker.isActive()) {
                    volumeBar.setEnabled(true);
                    volumeBar.setProgress(speaker.getAbsoluteVolume());
                    volumeBar.setOnSeekBarChangeListener(new VolumeSeekBarListener(speaker));
                } else {
                    volumeBar.setEnabled(false);
                    volumeBar.setProgress(0);
                }
                return row;
            } catch (RuntimeException e) {
                Log.e(TAG, "Error when rendering speaker item: ", e);
                throw e;
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_SPEAKERS) {
            return new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_lock_silent_mode_off).setTitle(R.string.control_menu_speakers).setAdapter(speakersAdapter, null).setPositiveButton("OK", null).create();
        }
        return null;
    }

    @Override
    protected void onResume() {
        this.fullScreen = this.prefs.getBoolean(this.getString(R.string.pref_fullscreen), true);
        if (this.fullScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        this.cropImage = this.prefs.getBoolean(this.getString(R.string.pref_cropimage), true);
        if (this.cropImage) {
            this.coverImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            this.coverImage.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.agreed = prefs.getBoolean(EULA, false);
        if (this.prefs.getBoolean(this.getString(R.string.pref_fullscreen), true)) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        if (!this.agreed) {
            this.startActivityForResult(new Intent(this, WizardActivity.class), 1);
        }
        setContentView(R.layout.act_control);
        this.vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.volume = inflater.inflate(R.layout.toa_volume, null, false);
        this.volumeBar = (ProgressBar) this.volume.findViewById(R.id.volume);
        this.volumeToast = new Toast(this);
        this.volumeToast.setDuration(Toast.LENGTH_SHORT);
        this.volumeToast.setGravity(Gravity.CENTER, 0, 0);
        this.volumeToast.setView(this.volume);
        this.shuffleToast = Toast.makeText(this, R.string.control_menu_shuffle_off, Toast.LENGTH_SHORT);
        this.shuffleToast.setGravity(Gravity.CENTER, 0, 0);
        this.repeatToast = Toast.makeText(this, R.string.control_menu_repeat_none, Toast.LENGTH_SHORT);
        this.repeatToast.setGravity(Gravity.CENTER, 0, 0);
        this.trackName = (TextView) findViewById(R.id.info_title);
        this.trackArtist = (TextView) findViewById(R.id.info_artist);
        this.trackAlbum = (TextView) findViewById(R.id.info_album);
        this.ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        this.coverImage = (ImageView) findViewById(R.id.cover);
        this.cropImage = this.prefs.getBoolean(this.getString(R.string.pref_cropimage), true);
        if (this.cropImage) {
            this.coverImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            this.coverImage.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        this.seekBar = (SeekBar) findViewById(R.id.seek);
        this.seekPosition = (TextView) findViewById(R.id.seek_position);
        this.seekRemain = (TextView) findViewById(R.id.seek_remain);
        this.controlPrev = (ImageButton) findViewById(R.id.control_prev);
        this.controlPause = (ImageButton) findViewById(R.id.control_pause);
        this.controlNext = (ImageButton) findViewById(R.id.control_next);
        this.controlShuffle = (ImageButton) findViewById(R.id.control_shuffle);
        this.controlRepeat = (ImageButton) findViewById(R.id.control_repeat);
        this.seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                dragging = true;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                dragging = false;
                session.controlProgress(seekBar.getProgress());
                ignoreNextTick = true;
                if (vibrate) vibrator.vibrate(VIBRATE_LEN);
            }
        });
        this.controlPrev.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (session == null) {
                    return;
                }
                session.controlPrev();
                if (vibrate) vibrator.vibrate(VIBRATE_LEN);
            }
        });
        this.controlNext.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (session == null) {
                    return;
                }
                session.controlNext();
                if (vibrate) vibrator.vibrate(VIBRATE_LEN);
            }
        });
        this.controlPause.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (session == null) {
                    return;
                }
                if (session != null && ControlActivity.status.getPlayStatus() == Status.STATE_PLAYING) {
                    session.controlPause();
                } else {
                    session.controlPlay();
                }
                if (vibrate) vibrator.vibrate(VIBRATE_LEN);
            }
        });
        this.controlRepeat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                checkSetRepeat();
            }
        });
        checkRepeat();
        this.controlShuffle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                checkSetShuffle();
            }
        });
        checkShuffle();
        this.fadeview = (FadeView) findViewById(R.id.fadeview);
        this.fadeview.startFade();
        this.fadeview.doubleTapHandler = this.doubleTapHandler;
        this.ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser && rating <= 5) {
                    session.controlRating((long) ((rating / 5) * 100), status.getTrackId());
                }
            }
        });
        speakersAdapter = new SpeakersAdapter(this);
    }

    /**
	 * Use to check the repeat status and button state, without altering it unlike 
	 * {@link checkSetRepeat()}
	 */
    protected void checkRepeat() {
        if (session == null || status == null) return;
        switch(status.getRepeat()) {
            case Status.REPEAT_OFF:
                controlRepeat.setImageResource(R.drawable.btn_repeat_off);
                break;
            case Status.REPEAT_SINGLE:
                controlRepeat.setImageResource(R.drawable.btn_repeat_once);
                break;
            case Status.REPEAT_ALL:
                controlRepeat.setImageResource(R.drawable.btn_repeat_on_all);
                break;
        }
    }

    protected void checkSetRepeat() {
        if (session == null || status == null) return;
        switch(status.getRepeat()) {
            case Status.REPEAT_OFF:
                session.controlRepeat(Status.REPEAT_SINGLE);
                repeatToast.setText(R.string.control_menu_repeat_one);
                controlRepeat.setImageResource(R.drawable.btn_repeat_once);
                break;
            case Status.REPEAT_SINGLE:
                session.controlRepeat(Status.REPEAT_ALL);
                repeatToast.setText(R.string.control_menu_repeat_all);
                controlRepeat.setImageResource(R.drawable.btn_repeat_on_all);
                break;
            case Status.REPEAT_ALL:
                session.controlRepeat(Status.REPEAT_OFF);
                repeatToast.setText(R.string.control_menu_repeat_none);
                controlRepeat.setImageResource(R.drawable.btn_repeat_off);
                break;
        }
        repeatToast.show();
    }

    protected void checkShuffle() {
        if (session == null || status == null) return;
        switch(status.getShuffle()) {
            case Status.SHUFFLE_OFF:
                controlShuffle.setImageResource(R.drawable.btn_shuffle_off);
                break;
            case Status.SHUFFLE_ON:
                controlShuffle.setImageResource(R.drawable.btn_shuffle_on);
                break;
        }
    }

    protected void checkSetShuffle() {
        if (session == null || status == null) return;
        switch(status.getShuffle()) {
            case Status.SHUFFLE_OFF:
                session.controlShuffle(Status.SHUFFLE_ON);
                shuffleToast.setText(R.string.control_menu_shuffle_on);
                controlShuffle.setImageResource(R.drawable.btn_shuffle_on);
                break;
            case Status.SHUFFLE_ON:
                session.controlShuffle(Status.SHUFFLE_OFF);
                shuffleToast.setText(R.string.control_menu_shuffle_off);
                controlShuffle.setImageResource(R.drawable.btn_shuffle_off);
                break;
        }
        shuffleToast.show();
    }

    protected void incrementVolume(final long increment) {
        new Thread(new Runnable() {

            public void run() {
                checkCachedVolume();
                cachedVolume += increment;
                session.controlVolume(ControlActivity.this.cachedVolume);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        ControlActivity.this.volumeBar.setProgress((int) ControlActivity.this.cachedVolume);
                        ControlActivity.this.volume.invalidate();
                        ControlActivity.this.volumeToast.show();
                    }
                });
            }
        }).start();
    }

    /**
	 * Updates the cachedVolume if necessary
	 */
    protected void checkCachedVolume() {
        if (System.currentTimeMillis() - cachedTime > CACHE_TIME) {
            this.cachedVolume = status.getVolume();
            this.cachedTime = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int increment = 5;
        try {
            increment = Integer.parseInt(backend.getPrefs().getString(getResources().getString(R.string.pref_volumeincrement), "5"));
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                this.incrementVolume(+increment);
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                this.incrementVolume(-increment);
                return true;
            }
            this.fadeview.keepAwake();
        } catch (Exception e) {
            Log.e(TAG, "Volume Increment Exception:" + e.getMessage());
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keycode, KeyEvent event) {
        switch(keycode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_UP:
                break;
            default:
                return super.onKeyUp(keycode, event);
        }
        return true;
    }

    /**
	 * Used to start a notification with the currently playing item. Not currently used.
	 * @return A notification which shows the current song
	 */
    public Notification initNotification() {
        if (session == null || status == null) return null;
        String song = status.getTrackName();
        Notification note = new Notification(R.drawable.ic_notify, song, System.currentTimeMillis());
        PendingIntent clicker = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this, ControlActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
        note.setLatestEventInfo(getApplicationContext(), song, status.getTrackArtist(), clicker);
        note.flags = Notification.FLAG_ONGOING_EVENT;
        return note;
    }

    /**
	 * Used to recind the notification created in {@link initNotification()}. Not currently used.
	 */
    public void killNotification() {
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(NOTIFICATION_CONTROL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.act_control, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.control_menu_search:
                onSearchRequested();
                return true;
            case R.id.control_menu_artists:
                Intent artistIntent = new Intent(ControlActivity.this, BrowseActivity.class);
                artistIntent.putExtra("windowType", BaseBrowseActivity.RESULT_SWITCH_TO_ARTISTS);
                startActivity(artistIntent);
                return true;
            case R.id.control_menu_playlists:
                Intent playlistIntent = new Intent(ControlActivity.this, BrowseActivity.class);
                playlistIntent.putExtra("windowType", BaseBrowseActivity.RESULT_SWITCH_TO_PLAYLISTS);
                startActivity(playlistIntent);
                return true;
            case R.id.control_menu_speakers:
                showDialog(DIALOG_SPEAKERS);
                return true;
            case R.id.control_menu_add_library:
                startActivity(new Intent(ControlActivity.this, LibraryActivity.class));
                return true;
            case R.id.control_menu_pick_library:
                startActivity(new Intent(ControlActivity.this, ServerActivity.class));
                return true;
            case R.id.control_menu_now_playing:
                StartNowPlaying();
                return true;
            case R.id.control_menu_settings:
                startActivity(new Intent(this, PrefsActivity.class));
                return true;
            case R.id.control_menu_about:
                startActivity(new Intent(this, WizardActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (session == null || status == null) return true;
        MenuItem speakersMenuItem = menu.findItem(R.id.control_menu_speakers);
        new Thread(new SpeakersRunnable(speakersMenuItem));
        return true;
    }

    public class SpeakersRunnable implements Runnable {

        MenuItem mSpeakersItem;

        public SpeakersRunnable(MenuItem speakersItem) {
            super();
            mSpeakersItem = speakersItem;
        }

        @Override
        public void run() {
            speakers = status.getSpeakers();
            if (mSpeakersItem != null) mSpeakersItem.setVisible(speakers.size() > 1);
        }
    }
}
