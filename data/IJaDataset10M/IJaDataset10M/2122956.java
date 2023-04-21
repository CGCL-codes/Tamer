package sk.MichalKatrik.Android.RadioFMUnofficial;

import java.text.DateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import sk.MichalKatrik.Android.RadioFMUnofficial.R;

public class RadioFMUnofficialActivity extends Activity {

    private Context context = null;

    private TextView tvNowPlayingTime = null;

    private TextView tvNowPlayingTitle = null;

    private TextView tvNowPlayingArtist = null;

    private TextView tvNowPlayingAlbum = null;

    private TextView tvProgress = null;

    public MyData myData = null;

    private ProgressBar bar = null;

    private TableRow trAlbum = null;

    private TableLayout tlNowPlaying = null;

    private TextView tvNowOnAirTime = null;

    private TextView tvNowOnAirTitle = null;

    private TextView tvNowOnAirDescription = null;

    private TableRow trDescription = null;

    private TableLayout tlNowOnAir = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = getApplicationContext();
        bar = (ProgressBar) findViewById(R.id.progressBar1);
        bar.setVisibility(View.INVISIBLE);
        bar.setMax(2);
        tvProgress = (TextView) this.findViewById(R.id.tvProgress);
        tvNowPlayingTime = (TextView) this.findViewById(R.id.tvNowPlayingTime);
        tvNowPlayingTitle = (TextView) this.findViewById(R.id.tvNowPlayingTitle);
        tvNowPlayingArtist = (TextView) this.findViewById(R.id.tvNowPlayingArtist);
        tvNowPlayingAlbum = (TextView) this.findViewById(R.id.tvNowPlayingAlbum);
        trAlbum = (TableRow) this.findViewById(R.id.trAlbum);
        tlNowPlaying = (TableLayout) this.findViewById(R.id.tlNowPlaying);
        tvNowOnAirTime = (TextView) this.findViewById(R.id.tvNowOnAirTime);
        tvNowOnAirTitle = (TextView) this.findViewById(R.id.tvNowOnAirTitle);
        tvNowOnAirDescription = (TextView) this.findViewById(R.id.tvNowOnAirDescription);
        trDescription = (TableRow) this.findViewById(R.id.trDescription);
        tlNowOnAir = (TableLayout) this.findViewById(R.id.tlNowOnAir);
        myData = (MyData) getLastNonConfigurationInstance();
        if (myData == null) {
            myData = new MyData();
        }
        if (myData.task == null) {
            myData.task = new LoadPlaylistTask(this);
            if (!(myData.playlistLoaded && myData.onAirLoaded)) {
                startRefreshTask();
            }
        } else {
            myData.task.attachActivity(this);
            bar.setVisibility(View.VISIBLE);
            updateProgress(myData.task.getProgress());
            if (myData.task.getProgress() >= bar.getMax()) {
                progressCompleted();
            }
        }
        updatePlaylistViews(true);
        updateOnAirViews(true);
    }

    public void setVisibility() {
        if (tvNowPlayingTitle.getText().toString().trim() == "") {
            tlNowPlaying.setVisibility(View.INVISIBLE);
        } else {
            tlNowPlaying.setVisibility(View.VISIBLE);
        }
        if (tvNowPlayingAlbum.getText().toString().trim() != "") {
            trAlbum.setVisibility(View.VISIBLE);
        } else {
            trAlbum.setVisibility(View.INVISIBLE);
        }
        if (tvNowOnAirTitle.getText().toString().trim() == "") {
            tlNowOnAir.setVisibility(View.INVISIBLE);
        } else {
            tlNowOnAir.setVisibility(View.VISIBLE);
        }
        if (tvNowOnAirDescription.getText().toString().trim() == "") {
            trDescription.setVisibility(View.INVISIBLE);
        } else {
            trDescription.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setVisibility();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(this.context.getResources().getResourceName(tvProgress.getId()), tvProgress.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvProgress.setText(savedInstanceState.getString(this.context.getResources().getResourceName(tvProgress.getId())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.mnuRefresh:
                startRefreshTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isConnectionAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return ((netInfo != null) && (netInfo.isConnectedOrConnecting()));
    }

    public void updateProgress(int progress) {
        bar.setProgress(progress);
    }

    public Object onRetainNonConfigurationInstance() {
        if ((myData != null) && (myData.task != null)) {
            myData.task.detachActivity();
        }
        return myData;
    }

    public void updatePlaylistViews(Boolean refreshOnly) {
        if ((myData.plItem != null) && (myData.playlistLoaded)) {
            tlNowPlaying.setVisibility(View.VISIBLE);
            tvNowPlayingTime.setText(getText(R.string.nowPlaying) + " (" + myData.plItem.getTimeFrom() + ")");
            tvNowPlayingTitle.setText(myData.plItem.getTitle());
            tvNowPlayingArtist.setText(myData.plItem.getArtist());
            String album = myData.plItem.getAlbum();
            tvNowPlayingAlbum.setText(album);
            setVisibility();
        } else if (!refreshOnly) {
            tvNowPlayingTime.setText(R.string.updateFailed);
        }
    }

    public void updateOnAirViews(Boolean refreshOnly) {
        if ((myData.oaItem != null) && (myData.onAirLoaded)) {
            tlNowOnAir.setVisibility(View.VISIBLE);
            String time = getText(R.string.nowOnAir) + " (" + myData.oaItem.getTimeFrom();
            if (myData.oaItem.getTimeTo().trim() != "") {
                time += " - " + myData.oaItem.getTimeTo();
            }
            time += ")";
            tvNowOnAirTime.setText(time);
            tvNowOnAirTitle.setText(myData.oaItem.getTitle());
            tvNowOnAirDescription.setText(myData.oaItem.getDescription());
            setVisibility();
        } else if (!refreshOnly) {
            tvNowOnAirTime.setText(R.string.updateFailed);
        }
    }

    public void progressCompleted() {
        bar.setProgress(bar.getMax());
        bar.setVisibility(View.GONE);
        if (myData.task != null) {
            RadioFMWeb radioFMWeb = myData.task.getRadioFMWeb();
            if ((radioFMWeb != null) && (myData != null) && (myData.playlistLoaded && myData.onAirLoaded)) {
                Date date = new Date();
                Context lContext = getApplicationContext();
                DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(lContext);
                DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(lContext);
                String currentDateTimeString = getText(R.string.statusLastUpdate) + ": " + dateFormat.format(date) + " " + timeFormat.format(date);
                tvProgress.setText(currentDateTimeString);
            } else if (!myData.playlistLoaded && !myData.onAirLoaded) {
                tvProgress.setText(R.string.updateFailed);
            } else {
                tvProgress.setText(R.string.updateIncomplete);
            }
        }
    }

    private void startRefreshTask() {
        if (isConnectionAvailable()) {
            bar.setVisibility(View.VISIBLE);
            tvProgress.setText(R.string.statusLoading);
            if (myData.task != null) {
                myData.task = new LoadPlaylistTask(this);
            }
            myData.task.execute();
        } else {
            Toast.makeText(context, R.string.errNoConnection, Toast.LENGTH_LONG).show();
        }
    }
}
