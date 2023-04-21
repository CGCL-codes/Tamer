package com.gameloft.amru.clashofwar;

/**
 *
 * @author amrullah
 */
public interface IScreen {

    void paintScreen();

    void keyPressed(int gameAction, int keyCode);

    void stop();
}
