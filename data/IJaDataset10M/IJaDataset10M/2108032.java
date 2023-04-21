package com.myapp.tools.media.renamer.view.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import com.myapp.tools.media.renamer.controller.Msg;
import com.myapp.tools.media.renamer.model.IRenamable;
import com.myapp.tools.media.renamer.model.IRenameProcessListener;
import com.myapp.tools.media.renamer.model.IRenamer;

/**
 * shows the rename progress in a progress bar
 * 
 * @author andre
 * 
 */
class RenameProcessDisplay implements IRenameProcessListener {

    private JProgressBar progressBar;

    private JLabel statusLabel;

    private volatile int totalCount;

    private volatile int totalRenamed;

    private IRenamable renamedAtTheMoment = null;

    private List<IRenamable> successFullyRenamed;

    private JFrame frame = new JFrame(Msg.msg("RenameProcessReport.title"));

    /**
     * creates a new RenameProcessDisplay
     */
    RenameProcessDisplay() {
        successFullyRenamed = new ArrayList<IRenamable>();
    }

    @Override
    public void processStarting(IRenamer renamer) {
        successFullyRenamed.clear();
        totalCount = renamer.getSize();
        totalRenamed = 0;
        renamedAtTheMoment = null;
        progressBar = new JProgressBar(SwingConstants.HORIZONTAL, totalRenamed, totalCount);
        statusLabel = new JLabel();
        JPanel cp = new JPanel(new BorderLayout());
        cp.add(progressBar, BorderLayout.CENTER);
        cp.add(statusLabel, BorderLayout.SOUTH);
        frame.setContentPane(cp);
        frame.setPreferredSize(new Dimension(640, 75));
        frame.pack();
        frame.setLocation(Utils.getCenteredPosition(frame));
        frame.setVisible(true);
    }

    @Override
    public void processFileSuccess() {
        renamedAtTheMoment = null;
        totalRenamed++;
        progressBar.getModel().setValue(totalRenamed);
    }

    @Override
    public void processFinished() {
        frame.setVisible(false);
    }

    @Override
    public void processFileStart(IRenamable file) {
        renamedAtTheMoment = file;
        String processFigure = processFigure();
        String statusLabelText = Msg.msg("RenameProcessReport.label.status").replace("#oldName#", renamedAtTheMoment.getOldName()).replace("#newName#", renamedAtTheMoment.getNewName()).replace("#statusStr#", processFigure);
        statusLabel.setText(statusLabelText);
        frame.setTitle(Msg.msg("RenameProcessReport.title.status").replace("#statusStr#", processFigure));
        final AtomicBoolean painting = new AtomicBoolean(true);
        final Graphics g = frame.getGraphics();
        new Thread(new Runnable() {

            public void run() {
                frame.paintComponents(g);
                painting.set(false);
            }
        }).start();
        while (painting.get()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void processFailed(Throwable t, IRenamable f) {
    }

    /**
     * calculates a short string showing the count of renamed and the total
     * count of files to rename.
     * 
     * @return a short process figure
     */
    private String processFigure() {
        return Msg.msg("RenameProcessReport.statusString").replace("#renamed#", Integer.toString(totalRenamed + 1)).replace("#total#", Integer.toString(totalCount));
    }
}
