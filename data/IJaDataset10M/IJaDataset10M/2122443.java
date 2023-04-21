package com.echodrama.upturner;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends Activity {

    private String gameCategory = Constants.LEVEL_ASPEN;

    private Timer timer;

    private int advCountDown = 5;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoundPollManager.initialize(this);
        setContentView(R.layout.main);
        buildMainView();
        renderMainView();
    }

    public void buildMainView() {
        Spinner spinner = (Spinner) findViewById(R.id.gameLevelSpinner);
        ArrayAdapter gameLevelClassAdapter = ArrayAdapter.createFromResource(this, R.array.game_level_class, android.R.layout.simple_spinner_item);
        gameLevelClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(gameLevelClassAdapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        MainActivity.this.gameCategory = Constants.LEVEL_ASPEN;
                        break;
                    case 1:
                        MainActivity.this.gameCategory = Constants.LEVEL_MANUTD;
                        break;
                    case 2:
                        MainActivity.this.gameCategory = Constants.LEVEL_INTER;
                        break;
                    case 3:
                        MainActivity.this.gameCategory = Constants.LEVEL_ANCIENT;
                        break;
                    case 4:
                        MainActivity.this.gameCategory = Constants.LEVEL_AV;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Button gameStartBtn = (Button) findViewById(R.id.gameStartBtn);
        gameStartBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startGame();
            }
        });
        Button gameSettingBtn = (Button) findViewById(R.id.gameSettingBtn);
        gameSettingBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent, R.layout.setting);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == R.layout.gameboard) {
            switch(resultCode) {
                case Constants.RESULT_GAME_RESTART:
                    startGame();
                    break;
                case Constants.RESULT_GAME_CLOSE:
                    setResult(Constants.RESULT_GAME_CLOSE);
                    break;
            }
        }
    }

    public void renderMainView() {
        Spinner spinner = (Spinner) findViewById(R.id.gameLevelSpinner);
        if (Constants.LEVEL_ASPEN.equalsIgnoreCase(gameCategory)) {
            spinner.setSelection(0, true);
        } else if (Constants.LEVEL_MANUTD.equalsIgnoreCase(gameCategory)) {
            spinner.setSelection(1, true);
        } else if (Constants.LEVEL_INTER.equalsIgnoreCase(gameCategory)) {
            spinner.setSelection(2, true);
        } else if (Constants.LEVEL_ANCIENT.equalsIgnoreCase(gameCategory)) {
            spinner.setSelection(3, true);
        } else if (Constants.LEVEL_AV.equalsIgnoreCase(gameCategory)) {
            spinner.setSelection(4, true);
        }
        final Button gameStartBtn = (Button) findViewById(R.id.gameStartBtn);
        final String buttonText = getResources().getString(R.string.game_start);
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                advCountDown = advCountDown - 1;
                if (advCountDown > 0) {
                    gameStartBtn.setText(buttonText + " [" + advCountDown + "]");
                    gameStartBtn.setEnabled(false);
                } else {
                    gameStartBtn.setText(buttonText);
                    gameStartBtn.setEnabled(true);
                    timer.cancel();
                }
                super.handleMessage(msg);
            }
        };
        final TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                Message message = new Message();
                handler.sendMessage(message);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1, 1000);
    }

    public void startGame() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM_NAME_GAME_CATEGORY, gameCategory);
        intent.putExtras(bundle);
        intent.setClass(MainActivity.this, GameBoardActivity.class);
        startActivityForResult(intent, R.layout.gameboard);
    }
}
