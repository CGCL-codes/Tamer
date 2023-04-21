package MyButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

public class SoundPlayer {

    private Clip jumpSound, gameOverSound, hurt, playerchange, spellfire, squish;

    public SoundPlayer() {
        jumpSound = readSound("src\\Jump.wav");
        hurt = readSound("src\\Hurt.wav");
        playerchange = readSound("src\\plyrswitch.wav");
        spellfire = readSound("src\\spellfire.wav");
        squish = readSound("src\\Squish.wav");
    }

    public void playSound(int sound) {
        checkAllSounds();
        switch(sound) {
            case 0:
                jumpSound.setFramePosition(0);
                jumpSound.start();
                break;
            case 1:
                gameOverSound.setFramePosition(0);
                gameOverSound.start();
                break;
            case 2:
                hurt.setFramePosition(0);
                hurt.start();
                break;
            case 3:
                playerchange.setFramePosition(0);
                playerchange.start();
                break;
            case 4:
                if (!spellfire.isRunning()) {
                    spellfire.setFramePosition(0);
                    spellfire.start();
                }
                break;
            case 5:
                squish.setFramePosition(0);
                squish.start();
                break;
        }
    }

    public Clip readSound(String audio) {
        try {
            File soundFile = new File(audio);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return clip;
        } catch (Exception e) {
            return null;
        }
    }

    public void checkAllSounds() {
        checkRunningSounds(jumpSound);
        checkRunningSounds(hurt);
        checkRunningSounds(squish);
    }

    public void checkRunningSounds(Clip clip) {
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    public static void main(String[] args) {
        new SoundPlayer();
    }
}
