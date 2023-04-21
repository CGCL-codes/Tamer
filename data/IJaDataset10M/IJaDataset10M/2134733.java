package net.sourceforge.simplegamenet.framework.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import net.sourceforge.simplegamenet.specs.gui.GameSettingsPanel;
import net.sourceforge.simplegamenet.specs.model.GameFactory;
import net.sourceforge.simplegamenet.specs.to.GameSettings;
import net.sourceforge.simplegamenet.util.proportionlayout.ProportionConstraints;
import net.sourceforge.simplegamenet.util.proportionlayout.ProportionLayout;

public class GameSettingsChoicePanel extends JPanel {

    private ConnectionWizardDialog connectionWizardDialog;

    private GameFactory[] gameFactories;

    private GameSettings[] gameSettingsArray;

    private GameSettingsPanel[] gameSettingsPanels;

    private JPanel cardPanel = new JPanel();

    private CardLayout cardLayout = new CardLayout();

    private TitledBorder titledBorder;

    private int gameFactoriesIndex = 0;

    private ConnectionConfig connectionConfig;

    GameSettingsChoicePanel(ConnectionWizardDialog connectionWizardDialog, GameFactory[] gameFactories) {
        this.connectionWizardDialog = connectionWizardDialog;
        this.gameFactories = gameFactories;
        connectionConfig = connectionWizardDialog.getConnectionConfig();
        ProportionLayout layout = new ProportionLayout();
        layout.appendColumn(5);
        layout.appendColumn(0, 1.0);
        layout.appendColumn(5);
        layout.appendRow(0);
        layout.appendRow(0, 1.0);
        layout.appendRow(5);
        setLayout(layout);
        titledBorder = BorderFactory.createTitledBorder("Settings for " + gameFactories[gameFactoriesIndex].getName());
        setBorder(titledBorder);
        cardPanel.setLayout(cardLayout);
        gameSettingsArray = new GameSettings[gameFactories.length];
        gameSettingsPanels = new GameSettingsPanel[gameFactories.length];
        for (int i = 0; i < gameFactories.length; i++) {
            gameSettingsArray[i] = gameFactories[i].createDefaultGameSettings();
            gameSettingsPanels[i] = gameSettingsArray[i].createGameSettingsPanel();
            cardPanel.add(gameSettingsPanels[i], Integer.toString(i));
        }
        add(cardPanel, new ProportionConstraints(1, 1));
    }

    boolean areSettingsAcceptable() {
        if (gameSettingsPanels[gameFactoriesIndex].areSettingsAcceptable()) {
            connectionConfig.setGameSettings(gameSettingsArray[gameFactoriesIndex].createChangedGameSettings(gameSettingsPanels[gameFactoriesIndex]));
            return true;
        } else {
            return false;
        }
    }

    public void setGameFactoriesIndex(int gameFactoriesIndex) {
        this.gameFactoriesIndex = gameFactoriesIndex;
        cardLayout.show(cardPanel, Integer.toString(gameFactoriesIndex));
        titledBorder.setTitle("Settings for " + gameFactories[gameFactoriesIndex].getName());
    }
}
