package com.scoreloop.android.coreui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.scoreloop.client.android.core.controller.GamesController;
import com.scoreloop.client.android.core.controller.RequestCancelledException;
import com.scoreloop.client.android.core.controller.RequestController;
import com.scoreloop.client.android.core.controller.RequestControllerObserver;
import com.scoreloop.client.android.core.controller.UserController;
import com.scoreloop.client.android.core.model.Game;
import com.scoreloop.client.android.core.model.Session;
import com.scoreloop.client.android.core.model.User;
import com.geekadoo.R;

public class UserActivity extends BaseActivity {

    private class GamesControllerObserver implements RequestControllerObserver {

        @Override
        public void requestControllerDidFail(final RequestController requestController, final Exception exception) {
            if (!(exception instanceof RequestCancelledException)) {
                showDialogSafe(BaseActivity.DIALOG_ERROR_NETWORK);
                adapter.clear();
            }
        }

        @Override
        public void requestControllerDidReceiveResponse(final RequestController requestController) {
            updateStatusBar();
            adapter.clear();
            final List<Game> games = gamesController.getGames();
            if (!games.isEmpty()) {
                for (final Game game : games) {
                    adapter.add(new ListItem(game));
                }
                ((LinearLayout) findViewById(R.id.recent_games_layout)).setVisibility(View.VISIBLE);
                final List<Game> recentGamesCandidates = new ArrayList<Game>(gamesController.getGames());
                recentGames = new ArrayList<Game>();
                Game recentGame;
                final Random random = new Random();
                final int j = Math.min(recentGamesCandidates.size(), NR_OF_RECENT_GAMES);
                for (int i = 0; i < j; i++) {
                    recentGame = recentGamesCandidates.get(random.nextInt(recentGamesCandidates.size()));
                    recentGamesCandidates.remove(recentGame);
                    recentGames.add(recentGame);
                    recentGameImage[i].setImageDrawable(getDrawable(recentGame.getImageUrl()));
                }
            } else {
                adapter.add(new ListItem(getResources().getString(R.string.sl_no_results_found)));
            }
        }
    }

    private class ListItemAdapter extends GenericListItemAdapter {

        public ListItemAdapter(final Context context, final int resource, final List<ListItem> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            convertView = init(position, convertView);
            if (listItem.isSpecialItem()) {
                text0.setText("");
                text1.setText(listItem.getLabel());
                text2.setVisibility(View.GONE);
                image.setVisibility(View.GONE);
            } else {
                text0.setVisibility(View.GONE);
                text1.setText(listItem.getGame().getName());
                text1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                text2.setText(listItem.getGame().getPublisherName());
                text2.setVisibility(View.VISIBLE);
                image.setVisibility(View.VISIBLE);
                image.setImageDrawable(getDrawable(listItem.getGame().getImageUrl()));
            }
            return convertView;
        }
    }

    private class ManageBuddyObserver extends UserGenericObserver {

        @Override
        public void requestControllerDidFail(final RequestController requestController, final Exception exception) {
            if (!(exception instanceof RequestCancelledException)) {
                setProgressIndicator(false);
                showDialogSafe(BaseActivity.DIALOG_ERROR_NETWORK);
                finish();
            }
        }

        @Override
        public void requestControllerDidReceiveResponse(final RequestController requestController) {
            setProgressIndicator(false);
            finish();
        }
    }

    private class MyBuddiesObserver extends UserGenericObserver {

        @Override
        public void requestControllerDidFail(final RequestController requestController, final Exception exception) {
            if (!(exception instanceof RequestCancelledException)) {
                showDialogSafe(BaseActivity.DIALOG_ERROR_NETWORK);
                buddyButton.setText(getString(R.string.sl_empty));
            }
        }

        @Override
        public void requestControllerDidReceiveResponse(final RequestController requestController) {
            updateStatusBar();
            buddyButton.setEnabled(true);
            if (Session.getCurrentSession().getUser().getBuddyUsers().contains(user)) {
                buddyButtonIsRemove = true;
                buddyButton.setText(getString(R.string.sl_remove_friend));
            } else {
                buddyButton.setText(getString(R.string.sl_add_friend));
            }
        }
    }

    private class UserBuddiesObserver extends UserGenericObserver {

        @Override
        public void requestControllerDidFail(final RequestController requestController, final Exception exception) {
            if (!(exception instanceof RequestCancelledException)) {
                showDialogSafe(BaseActivity.DIALOG_ERROR_NETWORK);
                friendsNumberText.setText(String.format(getString(R.string.sl_friends_number_format), getString(R.string.sl_empty)));
            }
        }

        @Override
        public void requestControllerDidReceiveResponse(final RequestController requestController) {
            updateStatusBar();
            friendsNumberText.setText(String.format(getString(R.string.sl_friends_number_format), user.getBuddyUsers().size()));
        }
    }

    private class UserDetailObserver extends UserGenericObserver {

        @Override
        public void requestControllerDidFail(final RequestController requestController, final Exception exception) {
            if (!(exception instanceof RequestCancelledException)) {
                showDialogSafe(BaseActivity.DIALOG_ERROR_NETWORK);
                setProgressIndicator(false);
                updateUI();
            }
        }

        @Override
        public void requestControllerDidReceiveResponse(final RequestController requestController) {
            setProgressIndicator(false);
            updateStatusBar();
            updateUI();
        }

        private void updateUI() {
            final String empty = getString(R.string.sl_empty);
            final String lastActive = (isEmpty(user.getLastActiveAt()) ? empty : DEFAULT_DATE_FORMAT.format(user.getLastActiveAt()));
            lastActiveText.setText(String.format(getString(R.string.sl_last_active_format), lastActive));
        }
    }

    private static final int NR_OF_RECENT_GAMES = 3;

    private ListItemAdapter adapter;

    private Button buddyButton;

    private boolean buddyButtonIsRemove;

    private LinearLayout detailsLayout;

    private TextView friendsNumberText;

    private GamesController gamesController;

    private TextView lastActiveText;

    private ListView listView;

    private final ImageView recentGameImage[] = new ImageView[NR_OF_RECENT_GAMES];

    private List<Game> recentGames;

    private User user;

    private void switchToTab(final int tab) {
        if (tab == 0) {
            detailsLayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            detailsLayout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sl_user);
        updateStatusBar();
        updateHeading(getString(R.string.sl_people), false);
        user = ScoreloopManager.getUser();
        final TextView loginText = (TextView) findViewById(R.id.name_text);
        loginText.setText(user.getLogin());
        loginText.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        detailsLayout = (LinearLayout) findViewById(R.id.details_layout);
        friendsNumberText = (TextView) findViewById(R.id.friends_number_text);
        friendsNumberText.setText((String.format(getString(R.string.sl_friends_number_format), getString(R.string.sl_loading))));
        lastActiveText = (TextView) findViewById(R.id.last_active_text);
        lastActiveText.setText((String.format(getString(R.string.sl_last_active_format), getString(R.string.sl_loading))));
        recentGameImage[0] = (ImageView) findViewById(R.id.image_view_0);
        recentGameImage[1] = (ImageView) findViewById(R.id.image_view_1);
        recentGameImage[2] = (ImageView) findViewById(R.id.image_view_2);
        buddyButton = (Button) findViewById(R.id.buddy_button);
        buddyButton.setEnabled(false);
        buddyButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                setProgressIndicator(true);
                final UserController userController = new UserController(new ManageBuddyObserver());
                userController.setUser(user);
                if (buddyButtonIsRemove) {
                    userController.removeAsBuddy();
                } else {
                    userController.addAsBuddy();
                }
            }
        });
        listView = (ListView) findViewById(R.id.list_view);
        final SegmentedView segmentedView = (SegmentedView) findViewById(R.id.segments);
        segmentedView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View view) {
                switchToTab(segmentedView.getSelectedSegment());
            }
        });
        adapter = new ListItemAdapter(UserActivity.this, R.layout.sl_user, new ArrayList<ListItem>());
        adapter.add(new ListItem(getResources().getString(R.string.sl_loading)));
        listView.setAdapter(adapter);
        gamesController = new GamesController(new GamesControllerObserver());
        gamesController.loadRangeForUser(user);
        final UserController myBuddiesController = new UserController(new MyBuddiesObserver());
        myBuddiesController.loadBuddies();
        final UserController userBuddiesController = new UserController(new UserBuddiesObserver());
        userBuddiesController.setUser(user);
        userBuddiesController.loadBuddies();
        final UserController userDetailController = new UserController(new UserDetailObserver());
        userDetailController.setUser(user);
        userDetailController.loadUserDetail();
        setProgressIndicator(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setNotify(new Runnable() {

            public void run() {
                adapter.notifyDataSetChanged();
                int i = 0;
                for (final Game recentGame : recentGames) {
                    recentGameImage[i].setImageDrawable(getDrawable(recentGame.getImageUrl()));
                    i++;
                }
            }
        });
    }
}
