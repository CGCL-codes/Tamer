package com.androidchallenge.songoo.step;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import com.androidchallenge.songoo.*;
import com.androidchallenge.songoo.controls.*;
import com.androidchallenge.songoo.fx.ControlAnimation;
import com.androidchallenge.songoo.util.OnControlClickListener;
import com.androidchallenge.songoo.view.*;

/**
 * @author Martinien
 *
 */
public class MenuStep extends StepView implements IStepView {

    private ImageButton _play;

    private ImageButton _exit;

    private ImageButton _options;

    private ImageButton _tutorial;

    private Panel _backPanel;

    /**
	 * @param context
	 */
    public MenuStep(Context context) {
        super(context);
        InitStep();
    }

    /**
	 * @param context
	 * @param attrs
	 */
    public MenuStep(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitStep();
    }

    private void InitStep() {
        ((MenuActivity) _context).LoadBitmap(R.drawable.menu000);
        _play = new ImageButton(this, "playButton", R.drawable.playfr000, R.drawable.playfr001);
        _play.setPosition(540, 46);
        _play.setOnClickListener(new OnControlClickListener() {

            public void onClick(Control c) {
                ((MenuActivity) _context).PlaySound(1);
                StepNext();
            }
        });
        addControl(_play);
        _tutorial = new ImageButton(this, "tutorialButton", R.drawable.tutorialfr000, R.drawable.tutorialfr001);
        _tutorial.setPosition(495, 190);
        _tutorial.setOnClickListener(new OnControlClickListener() {

            public void onClick(Control c) {
                ((MenuActivity) _context).PlaySound(1);
                _backPanel.getControls().clear();
                if (_backPanel.getVisibility() == Control.VISIBLE) {
                    _backPanel.setVisibility(Control.INVISIBLE);
                } else {
                    Label lblTitle = new Label(_backPanel, "lblTitle");
                    lblTitle.setColor(Color.WHITE);
                    lblTitle.setSize(30);
                    lblTitle.setFont("JoeBeckerHeavyBold");
                    lblTitle.setText("TUTORIEL");
                    lblTitle.setPosition(170, 100);
                    Label lblContent = new Label(_backPanel, "lblContent");
                    lblContent.setColor(Color.WHITE);
                    lblContent.setSize(20);
                    lblContent.setFont("JoeBeckerHeavyNormal");
                    lblContent.setText("Le Songo'o est un jeu de grains Camerounais.");
                    lblContent.setPosition(30, 130);
                    _backPanel.addControl(lblTitle);
                    _backPanel.addControl(lblContent);
                    _backPanel.setVisibility(Control.VISIBLE);
                }
            }
        });
        addControl(_tutorial);
        _options = new ImageButton(this, "optionsButton", R.drawable.optionsfr000, R.drawable.optionsfr001);
        _options.setPosition(655, 190);
        _options.setOnClickListener(new OnControlClickListener() {

            public void onClick(Control c) {
                ((MenuActivity) _context).PlaySound(1);
            }
        });
        addControl(_options);
        _exit = new ImageButton(this, "exitButton", R.drawable.exitfr000, R.drawable.exitfr001);
        _exit.setPosition(530, 298);
        _exit.setOnClickListener(new OnControlClickListener() {

            public void onClick(Control c) {
                ((MenuActivity) _context).PlaySound(1);
                ((MenuActivity) c.getViewParent().getContext()).exitApplication();
            }
        });
        addControl(_exit);
        _backPanel = new Panel(this, "backPanel");
        _backPanel.setPosition(0, 0);
        _backPanel.setBackgroundPicture(R.drawable.backpanel000);
        _backPanel.setVisibility(Control.INVISIBLE);
        addControl(_backPanel);
    }

    public void Start() {
        super.Start();
        invalidate();
    }

    public void Stop() {
        super.Stop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(((MenuActivity) _context).GetBitmap(R.drawable.menu000), 0, 0, null);
        super.onDraw(canvas);
    }
}
