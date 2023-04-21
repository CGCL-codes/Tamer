package org.hswgt.teachingbox.usecases.realkorsel;

import java.awt.Rectangle;
import javax.vecmath.Point2d;
import org.hswgt.teachingbox.usecases.realkorsel.serialBT.KorselBT;
import org.hswgt.teachingbox.usecases.realkorsel.wiiIR.KorselIR;

public class LearnThread extends Thread {

    private KorselBT myKorselBT = null;

    private KorselIR myKorselIR = null;

    public LearnThread(KorselBT korselBT, KorselIR korselIR) {
        this.myKorselBT = korselBT;
        this.myKorselIR = korselIR;
        myKorselBT.isButtonPressed();
    }

    @Override
    public void run() {
        while (true) {
            RandomMotorSpeeds();
            try {
                Thread.sleep(300l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int xpos = (int) myKorselIR.getPosition().x;
            int ypos = (int) myKorselIR.getPosition().y;
            int bounds = 180;
            if (xpos < bounds || xpos > 1023 - bounds || ypos < bounds || ypos > 767 - bounds) {
                myKorselBT.setLeftMotorSpeed(-3);
                myKorselBT.setRightMotorSpeed(-3);
                try {
                    Thread.sleep(400l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myKorselBT.setLeftMotorSpeed(3);
                myKorselBT.setRightMotorSpeed(-3);
                try {
                    Thread.sleep(500l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myKorselBT.setLeftMotorSpeed(0);
                myKorselBT.setRightMotorSpeed(0);
            }
        }
    }

    void RandomMotorSpeeds() {
        int lspeed = (int) (Math.random() * 2 + 1);
        int rspeed = (int) (Math.random() * 2 + 1);
        if (lspeed == 1 && rspeed == 1) {
            myKorselBT.setLeftMotorSpeed(3);
            myKorselBT.setRightMotorSpeed(3);
            return;
        }
        if (lspeed == 1) {
            myKorselBT.setLeftMotorSpeed(3);
            myKorselBT.setRightMotorSpeed(0);
            return;
        } else {
            myKorselBT.setLeftMotorSpeed(0);
            myKorselBT.setRightMotorSpeed(3);
            return;
        }
    }
}
