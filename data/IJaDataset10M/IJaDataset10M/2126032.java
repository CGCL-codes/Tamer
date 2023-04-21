package net.lwi.bitcoiner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import net.lwi.BitCoinRPC.BitCoinInfo;
import net.lwi.BitCoinRPC.BitCoinRPCClient;
import net.lwi.BitCoinRPC.BitCoinRPCException;
import net.lwi.BitCoinRPC.BitCoinReceived;
import net.lwi.BitCoinRPC.BitCoinTransaction;
import net.lwi.BitCoinRPC.BitCoinTransactionCategory;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

    private BitCoinRPCClient client;

    private SharedPreferences preferences;

    private Handler mHandler = new Handler();

    private Runnable mUpdateTask = new Runnable() {

        public void run() {
            updateOverview();
            startUpdateHandler(30000);
        }
    };

    private OnClickListener listenerHashrate = new OnClickListener() {

        public void onClick(View v) {
            try {
                if (client.getGenerating()) {
                    client.setGenerate(false);
                } else {
                    client.setGenerate(true);
                }
                startUpdateHandler(1000);
            } catch (BitCoinRPCException e) {
                showMessage(getString(R.string.ErrorCallFailed));
            }
        }
    };

    private void showMessage(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void amountToTextView(TextView textView, double value) {
        textView.setText(String.format("%.2f", value));
        if (value > 0) {
            ((TextView) textView).setTextColor(Color.GREEN);
        } else {
            ((TextView) textView).setTextColor(Color.RED);
        }
    }

    private void dateToTextView(TextView textView, Date value) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        textView.setText(format.format(value));
    }

    private boolean connected;

    private boolean connect() {
        boolean result = false;
        String host = preferences.getString("SettingsURL", "nohost");
        String user = preferences.getString("SettingsUser", "");
        String pass = preferences.getString("SettingsPassword", "");
        String lastTrustedCert = preferences.getString("LastTrustedCert", "");
        if (host.equals("nohost")) {
            showMessage(getString(R.string.ErrorServerSettingsMissing));
            return result;
        }
        try {
            client.connect(getApplicationContext(), host, user, pass, lastTrustedCert);
            result = true;
        } catch (BitCoinRPCException e) {
            try {
                if (e.getCause().getCause().getCause().getClass().getName().equals("java.security.cert.CertificateException")) {
                    String cert = e.getCause().getCause().getCause().getMessage();
                    showMessage(getString(R.string.ErrorServerCertificateUnknown));
                    Log.d("bitcoiner", "Old Trusted Cert: " + preferences.getString("LastTrustedCert", "-none-"));
                    Log.d("bitcoiner", "New Trusted Cert: " + cert);
                    Editor prefEditor = preferences.edit();
                    prefEditor.putString("LastTrustedCert", cert);
                    prefEditor.commit();
                    startUpdateHandler(100);
                }
            } catch (NullPointerException e2) {
            }
        } catch (Exception e) {
            showMessage(getString(R.string.ErrorServerNotReachable));
        }
        connected = result;
        return result;
    }

    private void updateOverview() {
        if (!connected) {
            connect();
        }
        if (connected) {
            try {
                setProgressBarIndeterminateVisibility(true);
                BitCoinInfo info = client.getInfo();
                ((TextView) findViewById(R.id.OverviewBalance)).setText(Double.toString(info.balance) + " BTC");
                if (info.generate) {
                    ((TextView) findViewById(R.id.OverviewHashrate)).setText(Double.toString(info.hashespersec / 1000.0) + " kHash/s");
                } else {
                    ((TextView) findViewById(R.id.OverviewHashrate)).setText(R.string.OverviewHashrateNotHashing);
                }
                LayoutInflater inflater = getLayoutInflater();
                ArrayList<BitCoinReceived> received = client.getAccounts();
                LinearLayout accounts = (LinearLayout) findViewById(R.id.OverviewAccounts);
                accounts.removeAllViews();
                boolean showEmpty = preferences.getBoolean("SettingsShowEmptyAccounts", false);
                for (BitCoinReceived account : received) {
                    double balance = client.getBalanceFromAccount(account.account);
                    if (showEmpty || balance > 0) {
                        View entry = inflater.inflate(R.layout.account, accounts, false);
                        ((TextView) entry.findViewById(R.id.AccountName)).setText(account.account);
                        ((TextView) entry.findViewById(R.id.AccountLabel)).setText(account.label);
                        amountToTextView((TextView) entry.findViewById(R.id.AccountBalance), balance);
                        accounts.addView(entry);
                    }
                }
                ArrayList<BitCoinTransaction> transactions = client.getTransactions("", 20);
                Collections.sort(transactions);
                Collections.reverse(transactions);
                LinearLayout recent = (LinearLayout) findViewById(R.id.OverviewRecentActivity);
                recent.removeAllViews();
                for (BitCoinTransaction transaction : transactions) {
                    View entry = inflater.inflate(R.layout.transaction, recent, false);
                    String from = "";
                    String to = "";
                    switch(transaction.details.get(0).category) {
                        case RECEIVE:
                            {
                                from = "?";
                                to = transaction.details.get(0).account;
                                break;
                            }
                        case SEND:
                            {
                                from = transaction.details.get(0).account;
                                to = transaction.to;
                                break;
                            }
                        case MOVE:
                            {
                                from = transaction.details.get(0).account;
                                to = transaction.to;
                                break;
                            }
                    }
                    ((TextView) entry.findViewById(R.id.TransactionFrom)).setText(from);
                    ((TextView) entry.findViewById(R.id.TransactionTo)).setText(to);
                    ((TextView) entry.findViewById(R.id.TransactionComment)).setText(transaction.comment);
                    dateToTextView((TextView) entry.findViewById(R.id.TransactionDate), transaction.time);
                    ((TextView) entry.findViewById(R.id.TransactionConfirmations)).setText(getString(R.string.OverviewTransactionConfirmations) + Integer.toString(transaction.confirmations));
                    amountToTextView((TextView) entry.findViewById(R.id.TransactionAmount), transaction.amount);
                    recent.addView(entry);
                }
                setProgressBarIndeterminateVisibility(false);
            } catch (BitCoinRPCException e) {
                connected = false;
                startUpdateHandler(1000);
            }
        } else {
            startUpdateHandler(1000);
        }
    }

    private void startUpdateHandler(int delay) {
        mHandler.removeCallbacks(mUpdateTask);
        mHandler.postDelayed(mUpdateTask, delay);
    }

    /** Called when the menu is first created. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    /** Called when the menu is selectd. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch(item.getItemId()) {
            case R.id.MainMenuSend:
                intent = new Intent(Main.this, SendForm.class);
                intent.putExtra("rpcURL", preferences.getString("SettingsURL", ""));
                intent.putExtra("rpcUser", preferences.getString("SettingsUser", ""));
                intent.putExtra("rpcPassword", preferences.getString("SettingsPassword", ""));
                startActivity(intent);
                return true;
            case R.id.MainMenuReceive:
                intent = new Intent(Main.this, RecvForm.class);
                intent.putExtra("rpcURL", preferences.getString("SettingsURL", ""));
                intent.putExtra("rpcUser", preferences.getString("SettingsUser", ""));
                intent.putExtra("rpcPassword", preferences.getString("SettingsPassword", ""));
                startActivity(intent);
                return true;
            case R.id.MainMenuSettings:
                startActivity(new Intent(Main.this, SetPreferences.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startUpdateHandler(500);
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mUpdateTask);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        client = new BitCoinRPCClient();
        setContentView(R.layout.overview);
        setProgressBarIndeterminateVisibility(true);
        TextView hashrate = (TextView) findViewById(R.id.OverviewHashrate);
        hashrate.setOnClickListener(listenerHashrate);
    }
}
