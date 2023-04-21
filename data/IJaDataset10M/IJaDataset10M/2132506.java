package uoft.smp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

/**
 * @author 10107896
 * 
 */
public class Menu extends Activity {

    private ImageView rocket1, rocket2, planet1;

    private TranslateAnimation moveUpDown;

    private Animation resizePlanet;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.menu);
        rocket1 = (ImageView) findViewById(R.id.imgViewRocket1);
        rocket2 = (ImageView) findViewById(R.id.imgViewRocket2);
        planet1 = (ImageView) findViewById(R.id.imgViewPlanet1);
        resizePlanet = AnimationUtils.loadAnimation(this, R.anim.resizing_planet);
        moveUpDown = new TranslateAnimation(0, 0, 500, -500);
        moveUpDown.setDuration(1000);
        moveUpDown.setFillAfter(true);
        moveUpDown.setRepeatCount(TranslateAnimation.INFINITE);
        moveUpDown.setRepeatMode(TranslateAnimation.REVERSE);
        rocket1.startAnimation(moveUpDown);
        rocket2.startAnimation(moveUpDown);
        planet1.startAnimation(resizePlanet);
        final Button play = (Button) findViewById(R.id.btnPlay);
        play.setFocusable(true);
        play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(Menu.this, Game.class);
                startActivity(intent);
            }
        });
        final Button settings = (Button) findViewById(R.id.btnSettings);
        settings.setFocusable(true);
        settings.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(Menu.this, Settings.class);
                startActivity(intent);
            }
        });
        final Button instructions = (Button) findViewById(R.id.btnInstructions);
        instructions.setFocusable(true);
        instructions.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(Menu.this, Instructions.class);
                startActivity(intent);
            }
        });
    }

    public void onStart() {
        super.onStart();
        rocket1 = (ImageView) findViewById(R.id.imgViewRocket1);
        rocket2 = (ImageView) findViewById(R.id.imgViewRocket2);
        planet1 = (ImageView) findViewById(R.id.imgViewPlanet1);
        resizePlanet = AnimationUtils.loadAnimation(this, R.anim.resizing_planet);
        moveUpDown = new TranslateAnimation(0, 0, 500, -500);
        moveUpDown.setDuration(4000);
        moveUpDown.setFillAfter(true);
        moveUpDown.setRepeatCount(100);
        moveUpDown.setRepeatMode(TranslateAnimation.REVERSE);
        rocket1.startAnimation(moveUpDown);
        rocket2.startAnimation(moveUpDown);
        planet1.startAnimation(resizePlanet);
    }
}
