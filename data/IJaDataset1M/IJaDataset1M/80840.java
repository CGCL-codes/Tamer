package org.srfc.driftscore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import org.srfc.driftscore.Scorer.Score;

public class Displayer extends Thread {

    public static final byte DIS_TYPE_SECSCORE = 0x01;

    public static final byte DIS_TYPE_SPLITSPEED = 0x11;

    private static final int DISPLAY_TIME = 1500;

    private boolean running = true;

    private Executor executor;

    private LinkedList<byte[]> displayList = new LinkedList<byte[]>();

    private LinkedList<Object[]> onDisplayList = new LinkedList<Object[]>();

    public Displayer(Executor e) {
        setDaemon(true);
        this.executor = e;
    }

    @Override
    public void run() {
        try {
            while (running) {
                Thread.sleep(200);
                checkDisplayList();
                checkDisplayTimeOut();
            }
        } catch (Exception e) {
            e.printStackTrace();
            executor.quit();
        }
    }

    public void quit() {
        this.running = false;
    }

    public void notice(byte plid, byte disType) {
        synchronized (displayList) {
            displayList.add(new byte[] { plid, disType });
        }
    }

    private void display(byte plid, byte disType) throws IOException {
        switch(disType) {
            case DIS_TYPE_SECSCORE:
                displaySecScore(plid);
                break;
            case DIS_TYPE_SPLITSPEED:
                displaySplitSpeed(plid);
                break;
            default:
                return;
        }
        onDisplayList.add(new Object[] { new Byte(plid), new Long(System.currentTimeMillis()), new Byte(disType) });
    }

    private void displaySecScore(byte plid) throws IOException {
        Scorer scorer = executor.getSocrer();
        Score s = scorer.get(plid);
        if (s == null) return;
        ArrayList<float[]> secScore = s.getSecScore();
        if (secScore == null || secScore.isEmpty()) return;
        float[] sec = secScore.get(secScore.size() - 1);
        if (sec == null) return;
        int t = (int) sec[1];
        executor.showCenterMsg(DIS_TYPE_SECSCORE, String.valueOf(t), plid);
    }

    private void displaySplitSpeed(byte plid) throws IOException {
        Scorer scorer = executor.getSocrer();
        Score s = scorer.get(plid);
        if (s == null) return;
        int speed = s.getSplitSpeed();
        executor.showCenterMsg(DIS_TYPE_SPLITSPEED, String.valueOf(speed), plid);
    }

    private void clear(byte plid, byte disType) throws IOException {
        switch(disType) {
            case DIS_TYPE_SECSCORE:
                clearSecScore(plid);
                break;
            case DIS_TYPE_SPLITSPEED:
                clearSplitSpeed(plid);
                break;
            default:
                break;
        }
    }

    private void clearSecScore(byte plid) throws IOException {
        executor.clearCenterMsg(DIS_TYPE_SECSCORE, plid);
    }

    private void clearSplitSpeed(byte plid) throws IOException {
        executor.clearCenterMsg(DIS_TYPE_SPLITSPEED, plid);
    }

    private void checkDisplayList() throws IOException {
        ArrayList<byte[]> list = null;
        synchronized (displayList) {
            if (displayList.isEmpty()) {
                return;
            }
            list = new ArrayList<byte[]>(displayList);
            displayList.clear();
        }
        if (list == null) return;
        for (byte[] todis : list) {
            if (todis == null) continue;
            display(todis[0], todis[1]);
        }
    }

    private void checkDisplayTimeOut() throws IOException {
        if (onDisplayList.isEmpty()) return;
        long curtime = System.currentTimeMillis();
        for (Iterator<Object[]> it = onDisplayList.iterator(); it.hasNext(); ) {
            Object[] obj = it.next();
            byte plid = (Byte) obj[0];
            long time = (Long) obj[1];
            byte disType = (Byte) obj[2];
            if (curtime - time > DISPLAY_TIME) {
                clear(plid, disType);
                it.remove();
            }
        }
    }
}
