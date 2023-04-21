package com.vnpgame.undersea.play.field;

import java.util.List;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.vnpgame.chickenmerrychristmas.R;
import com.vnpgame.undersea.lostgame.activity.LostGameScreen;
import com.vnpgame.undersea.music.Music;
import com.vnpgame.undersea.nextlevel.activity.NextLevelScreen;
import com.vnpgame.undersea.play.activity.Play3Screen;
import com.vnpgame.undersea.play.views.BoardView;

public class FieldPlay implements OnClickListener {

    private Play3Screen play3Screen;

    private BoardView boardView;

    private Button btnMenu;

    private TextView tvLevel;

    private TextView tvFailed;

    private TextView tvScore;

    private Board board = new Board(3);

    private MenuFiled menuFiled = new MenuFiled();

    private void updateMenu() {
        tvLevel.setText("Level : " + menuFiled.getLevel());
        tvFailed.setText("Failure : " + menuFiled.getFail());
        tvScore.setText("" + menuFiled.getScore());
    }

    public FieldPlay(Play3Screen play3Screen) {
        this.play3Screen = play3Screen;
        btnMenu = (Button) play3Screen.findViewById(R.id.button1);
        tvLevel = (TextView) play3Screen.findViewById(R.id.textView1);
        tvFailed = (TextView) play3Screen.findViewById(R.id.TextView02);
        tvScore = (TextView) play3Screen.findViewById(R.id.TextView01);
        boardView = (BoardView) play3Screen.findViewById(R.id.boardView1);
        btnMenu.setOnClickListener(this);
        configOnClick();
        new LoadBoard().execute("");
    }

    private void configOnClick() {
        for (int i = 0; i < FiledBoard.SIZE; i++) {
            for (int j = 0; j < FiledRow.SIZE; j++) {
                OnClickBoard board = new OnClickBoard(i, j);
                boardView.setOnClick(i, j, board);
            }
        }
    }

    private boolean isRun = false;

    private class OnClickBoard implements OnClickListener {

        int row = -1;

        int column = -1;

        public OnClickBoard(int row, int cloumn) {
            this.row = row;
            this.column = cloumn;
        }

        public void onClick(View v) {
            if (isRun) {
                return;
            }
            if (board.get(row, column) != R.drawable.tranfer) {
                Music.playEffect();
                new UpdateBoard(row, column).execute("");
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class UpdateBoard extends AsyncTask<String, String, String> {

        Handler handler = new Handler();

        int row, column;

        public UpdateBoard(int row, int column) {
            this.row = row;
            this.column = column;
        }

        List<Point> list;

        protected String doInBackground(String... params) {
            isRun = true;
            list = board.check(row, column);
            if (list.size() >= 2) {
                board.remove(list);
                menuFiled.setScore(menuFiled.getScore() + list.size() * list.size() * list.size() * 5 * menuFiled.getLevel());
            } else {
                return null;
            }
            handler.post(new Runnable() {

                public void run() {
                    boardView.updateBoard(board);
                    updateMenu();
                }
            });
            do {
                sleep();
                handler.post(new Runnable() {

                    public void run() {
                        boardView.updateBoard(board);
                    }
                });
            } while ((board.update()));
            sleep();
            if (!board.check()) {
                menuFiled.setFail(menuFiled.getFail() - board.count());
                if (menuFiled.getFail() < 0) {
                    menuFiled.setFail(0);
                }
                handler.post(new Runnable() {

                    public void run() {
                        updateMenu();
                    }
                });
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (menuFiled.getFail() > 0 && !board.check()) {
                Music.playNextLevel();
                Intent intent = new Intent(play3Screen, NextLevelScreen.class);
                play3Screen.startActivityForResult(intent, 0);
            } else if (!board.check() && menuFiled.getFail() <= 0) {
                Music.playLostGame();
                Intent intent = new Intent(play3Screen, LostGameScreen.class);
                intent.putExtra("arg0", menuFiled.getLevel());
                intent.putExtra("arg1", menuFiled.getScore());
                play3Screen.startActivityForResult(intent, 1);
            }
            isRun = false;
        }
    }

    public void onClick(View v) {
        play3Screen.finish();
    }

    private class LoadBoard extends AsyncTask<String, String, String> {

        Handler handler = new Handler();

        private ProgressDialog dialog;

        protected String doInBackground(String... params) {
            handler.post(new Runnable() {

                public void run() {
                    dialog = ProgressDialog.show(play3Screen, "Load board", "Loadding ...");
                    board.createNewBoard(menuFiled.getLevel());
                }
            });
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            boardView.updateBoard(board);
            updateMenu();
            dialog.dismiss();
        }
    }

    public void nextLevel() {
        menuFiled.setLevel(menuFiled.getLevel() + 1);
        new LoadBoard().execute("");
    }
}
