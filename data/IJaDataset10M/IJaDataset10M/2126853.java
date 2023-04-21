package com.dukesoftware.antsim.main;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import com.dukesoftware.antsim.simulate.AntSimulator;
import com.dukesoftware.antsim.simulate.Const;
import com.dukesoftware.utils.awt.CenterAloneLayout;
import com.dukesoftware.utils.simulate.Canvas;
import com.dukesoftware.utils.simulate.ISimulator;
import com.dukesoftware.utils.thread.LoopThread;
import com.dukesoftware.utils.thread.ShutdownHookWindowListener;

public class Main {

    public static void main(String[] args) {
        ISimulator simulator = new AntSimulator(Const.NUM_ANT, Const.NUM_BAG, Const.MAPX, Const.MAPY);
        Canvas canvas = new Canvas(Const.MAPX * Const.DOTSIZE, Const.MAPY * Const.DOTSIZE, simulator, Const.BACK_COLOR);
        LoopThread t = new LoopThread(canvas);
        t.start();
        JFrame frame = new JFrame(Const.FRAME_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new ShutdownHookWindowListener(t));
        frame.setResizable(false);
        Container root = frame.getContentPane();
        root.setLayout(new CenterAloneLayout());
        root.add(new JScrollPane(canvas));
        frame.pack();
        frame.setVisible(true);
    }
}
