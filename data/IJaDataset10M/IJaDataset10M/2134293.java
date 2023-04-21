package ch.hslu.ta.prg2.f11.group6.yatzy.model;

/**
 *
 * @author Remo Blättler <remo.blaettler@stud.hslu.ch>
 */
public class ComputerPlayer extends Player {

    public ComputerPlayer(int id, String name) {
        super(id, name);
    }

    public void play() {
        AutoPlay autoPlay = new AutoPlay(this);
        Thread thread = new Thread(autoPlay);
        thread.start();
    }
}
