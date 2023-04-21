package com.yawnefpark.scorekeeper.state;

import java.util.Observer;

/**
 * @author sperreault
 * 
 */
public interface State extends Observer {

    public void infieldSingle();

    public void dubble();

    public void cleanDouble();

    public void wildPitch();

    public void triple();

    public void homeRun();

    public void strikeOut();

    public void baseOnBalls();

    public void cleanSingle();

    public void doubleOffTheWall();

    public void out();

    public void doublePlay();

    public void failedDoublePlay();

    public void forgoneDoublePlay();

    public void triplePlay();

    public void error();

    public void sacrificeFly();

    public void sacrificeFlyForgone();

    public void sacrificeFlyCaughtTagging();

    public void strikeOutLooking();

    /**
	 * @return the outs
	 */
    public int getOuts();

    /**
	 * @return the onFirst
	 */
    public boolean isOnFirst();

    /**
	 * @return the onSecond
	 */
    public boolean isOnSecond();

    /**
	 * @return the onThird
	 */
    public boolean isOnThird();

    public String getCurrentBaseRunnerStatus();
}
