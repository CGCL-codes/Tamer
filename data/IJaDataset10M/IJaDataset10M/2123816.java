package com.gempukku.gempballs.view;

import com.gempukku.animator.composite.ContainerAnimated;
import com.gempukku.animator.composite.SwitchAnimated;
import com.gempukku.animator.composite.switcheffect.FadeOutFadeInSwitchEffect;
import com.gempukku.animator.decorator.AnimatedBoundsAdapter;
import com.gempukku.animator.decorator.MirroredAnimated;
import com.gempukku.animator.function.alpha.LinearAlpha;
import com.gempukku.animator.widget.GradientBackgroundAnimated;
import com.gempukku.gempballs.GempBallsManager;
import com.gempukku.gempballs.logic.GempBallsLogic;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameView extends ContainerAnimated {

    private SwitchAnimated _switchAnimated;

    private boolean _inMenuScreen = true;

    public GameView() {
        addAnimated(new GradientBackgroundAnimated());
        _switchAnimated = new SwitchAnimated(null);
        addAnimated(new MirroredAnimated(new AnimatedBoundsAdapter(_switchAnimated, new Rectangle(800, 450)), MirroredAnimated.MirrorMode.Fixed, 150));
    }

    public void goToMenuScreen() {
        MenuScreen menuScreen = new MenuScreen();
        menuScreen.addBeginnerButtonListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (_inMenuScreen) {
                    _inMenuScreen = false;
                    setupNewGame(GempBallsLogic.Level.BEGINNER);
                }
            }
        });
        menuScreen.addAdvancedButtonListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (_inMenuScreen) {
                    _inMenuScreen = false;
                    setupNewGame(GempBallsLogic.Level.ADVANCED);
                }
            }
        });
        menuScreen.addExpertButtonListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (_inMenuScreen) {
                    _inMenuScreen = false;
                    setupNewGame(GempBallsLogic.Level.EXPERT);
                }
            }
        });
        menuScreen.addExitButtonListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        _switchAnimated.transform(menuScreen, new LinearAlpha(), new FadeOutFadeInSwitchEffect(), 1000);
    }

    private void setupNewGame(GempBallsLogic.Level level) {
        GempBallsLogic gempBallsLogic = new GempBallsLogic(level);
        gempBallsLogic.prepareStartupPosition();
        GempBallsView gempBallsView = new GempBallsView(gempBallsLogic.getEdgeSize(), new Rectangle(0, 50, 600, 400));
        gempBallsLogic.addGempBallsModelListener(gempBallsView);
        GempBallsManager gempBallsManager = new GempBallsManager(gempBallsLogic, gempBallsView);
        GameScreen gameScreen = new GameScreen(gempBallsView);
        _switchAnimated.transform(new AnimatedBoundsAdapter(gameScreen, new Rectangle(800, 600)), new LinearAlpha(), new FadeOutFadeInSwitchEffect(), 1000);
    }
}
