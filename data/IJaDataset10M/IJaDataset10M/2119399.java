package de.javagimmicks.games.jotris;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import de.javagimmicks.games.jotris.controller.AutoPauseWindowListener;
import de.javagimmicks.games.jotris.controller.StandardControlKeyListener;
import de.javagimmicks.games.jotris.controller.StartGameAction;
import de.javagimmicks.games.jotris.controller.StopGameAction;
import de.javagimmicks.games.jotris.model.JoTrisModel;
import de.javagimmicks.games.jotris.model.TileFactory;
import de.javagimmicks.games.jotris.model.impl.DefaultJoTrisModel;
import de.javagimmicks.games.jotris.model.impl.DefaultTileFactory;
import de.javagimmicks.games.jotris.model.impl.TileUtil;
import de.javagimmicks.games.jotris.view.JoTrisGrid;
import de.javagimmicks.games.jotris.view.JoTrisPreviewGrid;
import de.javagimmicks.games.jotris.view.JoTrisRowsField;
import de.javagimmicks.games.jotris.view.JoTrisScoreField;

class Run {

    public static void main(String[] args) {
        TileFactory tileFactory = DefaultTileFactory.getDefaultInstance();
        final JoTrisModel model = new DefaultJoTrisModel(20, 8, tileFactory);
        JoTrisGrid grid = new JoTrisGrid(model, 22);
        grid.setBorder(BorderFactory.createLoweredBevelBorder());
        JoTrisPreviewGrid previewGrid = new JoTrisPreviewGrid(model, TileUtil.getCommonMinimalFormat(tileFactory.getPrototypes()), 22);
        JoTrisRowsField rowsBox = new JoTrisRowsField(model);
        JoTrisScoreField scoreBox = new JoTrisScoreField(model);
        JPanel panelRowsCompleted = new JPanel(new GridLayout(1, 2, 5, 5));
        panelRowsCompleted.add(new JLabel("Rows:"));
        panelRowsCompleted.add(rowsBox);
        JPanel panelScore = new JPanel(new GridLayout(1, 2, 5, 5));
        panelScore.add(new JLabel("Score:"));
        panelScore.add(scoreBox);
        JPanel panelStatistics = new JPanel(new GridLayout(1, 2, 2, 2));
        panelStatistics.add(panelRowsCompleted);
        panelStatistics.add(panelScore);
        JPanel panelPreview = new JPanel(new GridLayout(1, 2, 5, 5));
        panelPreview.add(new JLabel("Preview:"));
        panelPreview.add(previewGrid);
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelTop.add(panelStatistics);
        panelTop.add(panelPreview);
        JButton buttonStart = new JButton(new StartGameAction(model, "Start"));
        JButton buttonStop = new JButton(new StopGameAction(model, "Stop"));
        buttonStart.setFocusable(false);
        buttonStop.setFocusable(false);
        JPanel panelStartStop = new JPanel(new GridLayout(1, 2, 2, 2));
        panelStartStop.add(buttonStart);
        panelStartStop.add(buttonStop);
        setBorder(panelRowsCompleted);
        setBorder(panelScore);
        setBorder(panelPreview);
        JPanel panelWindowContent = new JPanel(new BorderLayout(5, 5));
        panelWindowContent.add(panelTop, BorderLayout.NORTH);
        panelWindowContent.add(grid, BorderLayout.CENTER);
        panelWindowContent.add(panelStartStop, BorderLayout.SOUTH);
        JFrame window = new JFrame("JoTris");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().add(panelWindowContent);
        window.pack();
        window.setVisible(true);
        window.addWindowListener(new AutoPauseWindowListener(model));
        window.addKeyListener(new StandardControlKeyListener(model));
    }

    static void setBorder(JPanel panel) {
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
    }
}
