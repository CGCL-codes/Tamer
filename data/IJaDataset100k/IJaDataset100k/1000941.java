package com.william.lifetraxer;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainPage extends ActivityGroup {

    private ImageButton friendsPage, tracksPage, gamesPage, optionsPage;

    private LinearLayout mainPageContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        initializeView();
        beInFriendsPage();
    }

    private void initializeView() {
        friendsPage = (ImageButton) findViewById(R.id.main_page_friends_page);
        tracksPage = (ImageButton) findViewById(R.id.main_page_tracks_page);
        gamesPage = (ImageButton) findViewById(R.id.main_page_games_page);
        optionsPage = (ImageButton) findViewById(R.id.main_page_options_page);
        mainPageContent = (LinearLayout) findViewById(R.id.main_page_content);
        friendsPage.setOnClickListener(new ImageButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                beInFriendsPage();
            }
        });
        tracksPage.setOnClickListener(new ImageButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                beInTracksPage();
            }
        });
        gamesPage.setOnClickListener(new ImageButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                beInGamesPage();
            }
        });
        optionsPage.setOnClickListener(new ImageButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                beInOptionsPage();
            }
        });
    }

    private void beInFriendsPage() {
        mainPageContent.removeAllViews();
        mainPageContent.addView(getLocalActivityManager().startActivity("FriendList", new Intent(MainPage.this, MainPageFriendList.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
    }

    private void beInTracksPage() {
        mainPageContent.removeAllViews();
        mainPageContent.addView(getLocalActivityManager().startActivity("Track", new Intent(MainPage.this, MainPageTrack.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
    }

    private void beInGamesPage() {
        mainPageContent.removeAllViews();
        mainPageContent.addView(getLocalActivityManager().startActivity("Game", new Intent(MainPage.this, MainPageGame.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
    }

    private void beInOptionsPage() {
        mainPageContent.removeAllViews();
        mainPageContent.addView(getLocalActivityManager().startActivity("Options", new Intent(MainPage.this, MainPageOptions.class)).getDecorView());
    }
}
