package com.thoughtworks.twu.gameoflife.renderer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import com.thoughtworks.twu.gameoflife.GameOfLife;
import com.thoughtworks.twu.gameoflife.GameOfLifeImpl;
import com.thoughtworks.twu.gameoflife.domain.GameListener;
import com.thoughtworks.twu.gameoflife.grid.GridType;
import com.thoughtworks.twu.gameoflife.rules.NormalRuleSet;
import com.thoughtworks.twu.gameoflife.rules.RuleType;

public class GameFrame extends JFrame implements GameListener {

    private static final int SCALE = 20;

    private static final long serialVersionUID = 1L;

    private final GameOfLife game;

    private JButton nextStep;

    private GameListener delegateListener = GameListener.NULL;

    private JComboBox currentRules, currentGrid;

    public GameFrame(GameOfLife game) {
        this.game = game;
        setUpFrame();
        setUpGrid();
        createNextStepButton();
        createGridDropDown();
        createRulesDropDown();
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("Game of Life");
    }

    private void setUpFrame() {
        this.setName("game.frame");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void setUpGrid() {
        GridBagLayout layout = new GridBagLayout();
        this.getContentPane().setLayout(layout);
        ImageRenderer imageRenderer = new ImageRenderer(game.getWidth(), game.getHeight(), SCALE);
        imageRenderer.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int column = e.getX() / SCALE;
                int row = e.getY() / SCALE;
                System.out.println("GameFrame: Toggling cell at " + row + "," + column);
                game.toggleCellAt(column, row);
            }
        });
        game.setListener(imageRenderer);
        this.delegateListener = imageRenderer;
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridwidth = 3;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        this.getContentPane().add(imageRenderer, constraints);
    }

    private void createRulesDropDown() {
        currentRules = new JComboBox(RuleType.values());
        currentRules.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                RuleType ruleType = RuleType.values()[currentRules.getSelectedIndex()];
                game.changeGameRules(ruleType);
                System.out.println("Rules changed to " + ruleType);
            }
        });
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 2;
        constraints.gridwidth = 1;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.1;
        constraints.weighty = 0;
        this.add(currentRules, constraints);
        currentRules.setSelectedIndex(0);
    }

    private void createGridDropDown() {
        currentGrid = new JComboBox(GridType.values());
        currentGrid.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                GridType gridType = GridType.values()[currentGrid.getSelectedIndex()];
                game.changeGameGrid(gridType);
                System.out.println("Grid changed to " + gridType);
            }
        });
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.1;
        constraints.weighty = 0;
        this.add(currentGrid, constraints);
        currentGrid.setSelectedIndex(0);
    }

    private void createNextStepButton() {
        nextStep = new JButton("Next Step");
        nextStep.setName("next.step");
        nextStep.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                game.nextGeneration();
            }
        });
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.gridwidth = 1;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.8;
        constraints.weighty = 0;
        this.getContentPane().add(nextStep, constraints);
    }

    public void cellsChanged(GameOfLife game) {
        delegateListener.cellsChanged(game);
    }

    /**
     * Replace the game in this method with your own game.
     */
    public static void main(String[] args) {
        int numberOfColumns = 20;
        int numberOfRows = 20;
        GameOfLife replaceMeWithYourGame = new GameOfLifeImpl(GridType.INFINITE_GRID, numberOfColumns, numberOfRows, new NormalRuleSet());
        new GameFrame(replaceMeWithYourGame);
    }
}
