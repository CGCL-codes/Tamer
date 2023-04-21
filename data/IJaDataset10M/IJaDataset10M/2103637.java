package android.social;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.opensocial.client.OpenSocialBatch;
import org.opensocial.client.OpenSocialClient;
import org.opensocial.client.OpenSocialProvider;
import org.opensocial.client.OpenSocialRequest;
import org.opensocial.client.Token;
import org.opensocial.data.OpenSocialPerson;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class FriendFeedActivity extends Activity {

    private static final String ANDROID_SCHEME = "x-opensocial-friendfeed-app";

    public static Map<OpenSocialProvider, Token> SUPPORTED_PROVIDERS = new HashMap<OpenSocialProvider, Token>();

    public org.opensocial.android.OpenSocialActivity util;

    static {
        SUPPORTED_PROVIDERS.put(OpenSocialProvider.EISBAHN, new Token("d4fac4e5-c7ff-c3c1-b7e5-cee1d5c1c5f6", "567e816290f754646b36e11787bbf7d2"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createUI();
    }

    private void createUI() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        Button startButton = new Button(this);
        startButton.setText("Start");
        startButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setupClient();
            }
        });
        Button stopButton = new Button(this);
        stopButton.setText("Stop");
        stopButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("default", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(BackgroundService.STOP_FLAG, true);
                editor.commit();
            }
        });
        Button clearAuthButton = new Button(this);
        clearAuthButton.setText("Clear Auth");
        clearAuthButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                clearAuthentication();
            }
        });
        linearLayout.addView(startButton);
        linearLayout.addView(stopButton);
        linearLayout.addView(clearAuthButton);
        setContentView(linearLayout);
    }

    private void clearAuthentication() {
        util = new org.opensocial.android.OpenSocialActivity(this, SUPPORTED_PROVIDERS, ANDROID_SCHEME);
        util.clearSavedAuthentication();
    }

    private void startBackgroundService(List<OpenSocialPerson> friends, OpenSocialClient client) {
        Intent intent = new Intent(FriendFeedActivity.this, BackgroundService.class);
        intent.putExtra("waitSec", 5000);
        intent.putExtra("friends", new FriendsHolder(friends));
        startService(intent);
    }

    private void setupClient() {
        util = new org.opensocial.android.OpenSocialActivity(this, SUPPORTED_PROVIDERS, ANDROID_SCHEME);
        OpenSocialClient client = util.getOpenSocialClient();
        if (client != null) {
            client.setProperty(OpenSocialClient.Properties.DEBUG, "true");
            Log.d("FriendFeed", "Authorization succeed.");
            List<OpenSocialPerson> friends = fetchFriends(client, util.getProvider());
            if (friends != null) {
                Log.d("FriendFeed", "friends.size() = " + friends.size());
                startBackgroundService(friends, client);
            } else {
                Log.d("FriendFeed", "friends is null.");
            }
        }
    }

    private List<OpenSocialPerson> fetchFriends(OpenSocialClient client, OpenSocialProvider provider) {
        List<OpenSocialPerson> friends = null;
        try {
            if (provider.isOpenSocial) {
                friends = client.fetchFriends();
            } else {
                OpenSocialBatch batch = new OpenSocialBatch();
                batch.addRequest(new OpenSocialRequest("@me/@all", ""), "friends");
                friends = batch.send(client).getItemAsPersonCollection("friends");
            }
        } catch (Exception e) {
            Log.i("DisplayFriendActivity", "Couldn't fetch friends from the container: " + e.getMessage());
        }
        return friends;
    }
}
