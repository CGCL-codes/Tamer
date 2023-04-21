package net.rptools.chartool.ui.component;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import net.rptools.chartool.model.KnownSettingsFiles;
import net.rptools.chartool.model.property.PropertySettingsFile;
import net.rptools.lib.FileUtil;
import net.rptools.lib.io.PackedFile;

/**
 * This is a controller used to set up and handle a game menu.
 * 
 * @author Jay
 */
public class GameMenuController {

    /**
   * The frame that owns the menu.
   */
    private RPFrame frame;

    /**
   * The game menu which is modified when the list of games change.
   */
    private JMenu gameMenu;

    /**
   * The menu item that will update remote files if needed before reloading.
   */
    private JMenuItem updateMenuItem;

    /**
   * The base number of items in the game menu. All game setting menu items start at this location. 
   */
    private int gameMenuBaseCount;

    /**
   * The currently executing action is made available for the callable that can be executed before
   * any of the menu item actions are fully executed. The file name of the new game can be
   * retrieved by reading the {@link Action#ACTION_COMMAND_KEY}.
   */
    private Action currentAction;

    /**
   * An optional action which will be called when any of the items in this menu are called. The 
   * callable should return {@link Boolean#TRUE} if the game selection action should continue or
   * {@link Boolean#FALSE} if it should return before displaying the game selection dialog. 
   */
    private Callable<Boolean> gameSelectionCallable;

    /**
   * The known settings files are accessed through this interface.
   */
    private KnownSettingsFiles knownSettingsFiles;

    /**
   * Action to select a game file from the file system
   */
    private final Action selectGameAction = new AbstractAction() {

        {
            putValue(Action.NAME, "Select Game...");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_G);
            putValue(Action.SHORT_DESCRIPTION, "Select a new game. The current character will be closed.");
            putValue(GameMenuController.class.getName(), this);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                frame.startBusy();
                if (gameSelectionCallable != null) {
                    try {
                        currentAction = this;
                        if (!gameSelectionCallable.call().booleanValue()) return;
                    } catch (Exception e1) {
                        LOGGER.log(Level.WARNING, "Exception executing the game selection callable.");
                    }
                }
                GameSelectionDialog dialog = new GameSelectionDialog(frame, knownSettingsFiles);
                dialog.setModal(true);
                Utilities.setWindowPosition(frame.getBounds(), dialog);
                dialog.setVisible(true);
                updateGameMenu();
            } finally {
                frame.endBusy();
            }
        }
    };

    /**
   * Action that causes the settings files to be reloaded.
   */
    private final Action reloadGameAction = new AbstractAction() {

        {
            putValue(Action.NAME, "Reload Current Game");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_G);
            putValue(Action.SHORT_DESCRIPTION, "Reload the current game settings.");
        }

        public void actionPerformed(ActionEvent evt) {
            frame.startBusy();
            try {
                if (gameSelectionCallable != null) {
                    try {
                        currentAction = this;
                        if (!gameSelectionCallable.call().booleanValue()) {
                            updateGameMenu();
                            return;
                        }
                    } catch (Exception e1) {
                        LOGGER.log(Level.WARNING, "Exception executing the game selection callable.");
                    }
                }
                knownSettingsFiles.loadSettingsFile(knownSettingsFiles.getDefaultSettingsFile(), knownSettingsFiles.getLoadedSourceFiles(), null);
            } catch (RuntimeException e) {
                LOGGER.log(Level.WARNING, "Exception loading the settings file: " + knownSettingsFiles.getDefaultSettingsFile().getAbsolutePath(), e);
                JOptionPane.showMessageDialog(frame, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            } finally {
                frame.endBusy();
            }
            updateGameMenu();
        }
    };

    /**
   * Action that causes the settings files to be reloaded.
   */
    private final Action updateGameAction = new AbstractAction() {

        {
            putValue(Action.NAME, "Update Current Game");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_G);
            putValue(Action.SHORT_DESCRIPTION, "Update the current game settings from the server and reload them.");
        }

        public void actionPerformed(ActionEvent evt) {
            frame.startBusy();
            try {
                if (gameSelectionCallable != null) {
                    try {
                        currentAction = this;
                        if (!gameSelectionCallable.call().booleanValue()) {
                            updateGameMenu();
                            return;
                        }
                    } catch (Exception e1) {
                        LOGGER.log(Level.WARNING, "Exception executing the game selection callable.");
                    }
                }
                updateFile(knownSettingsFiles.getDefaultSettingsFile());
                for (File file : knownSettingsFiles.getLoadedSourceFiles()) {
                    updateFile(file);
                }
                knownSettingsFiles.loadSettingsFile(knownSettingsFiles.getDefaultSettingsFile(), knownSettingsFiles.getLoadedSourceFiles(), null);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(frame, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            } finally {
                frame.endBusy();
            }
            updateGameMenu();
        }
    };

    /**
   * Logger instance for this class.
   */
    private static final Logger LOGGER = Logger.getLogger(GameMenuController.class.getName());

    /**
   * Create a new game menu controller.
   * 
   * @param aFrame The menu is placed on this frame.
   * @param theKnownSettingsFiles The object used to access the list of valid settings files.
   */
    public GameMenuController(RPFrame aFrame, KnownSettingsFiles theKnownSettingsFiles) {
        frame = aFrame;
        knownSettingsFiles = theKnownSettingsFiles;
    }

    /**
   * Create the actual menu to be placed into the frame
   * 
   * @param callable An optional method to call before the game selection dialog is displayed.
   * @return The new menu.
   */
    public JMenu createMenu(Callable<Boolean> callable) {
        gameSelectionCallable = callable;
        gameMenu = new JMenu("Game");
        gameMenu.add(new JMenuItem(selectGameAction));
        gameMenu.add(new JMenuItem(reloadGameAction));
        updateMenuItem = new JMenuItem(updateGameAction);
        gameMenu.add(updateMenuItem);
        gameMenu.addSeparator();
        gameMenuBaseCount = gameMenu.getMenuComponentCount();
        updateGameMenu();
        return gameMenu;
    }

    /**
   * Update the game menu to show all of the available games.
   */
    public void updateGameMenu() {
        Boolean unchanged = Utilities.checkGameFile(knownSettingsFiles.getDefaultSettingsFile());
        Iterator j = knownSettingsFiles.getLoadedSourceFiles().iterator();
        while ((unchanged == null || unchanged.booleanValue()) && j.hasNext()) unchanged = Utilities.checkGameFile((File) j.next());
        updateMenuItem.setEnabled(unchanged != null && !unchanged.booleanValue());
        int count = gameMenu.getMenuComponentCount();
        for (int i = count - 1; i >= gameMenuBaseCount; i--) gameMenu.remove(i);
        count = 1;
        ButtonGroup bg = new ButtonGroup();
        for (File gameFile : knownSettingsFiles.getKnownSettingsFiles()) {
            String name = null;
            String source = null;
            PackedFile pFile = new PackedFile(gameFile);
            try {
                name = (String) pFile.getProperty(PropertySettingsFile.GAME_NAME_PROP_NAME);
                source = (String) pFile.getProperty(PropertySettingsFile.SOURCE_PROP_NAME);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "An invalid settings file has been removed from the list of known games: " + gameFile, e);
                JOptionPane.showMessageDialog(gameMenu, "An invalid settings file has been removed " + "from the list of known games:\n   " + gameFile, "Error!", JOptionPane.ERROR_MESSAGE);
                knownSettingsFiles.removeKnownSettingsFile(gameFile);
                continue;
            } finally {
                pFile.close();
            }
            if (source != null) continue;
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(createGameSettingsAction(name, gameFile, count));
            gameMenu.add(item);
            bg.add(item);
            count += 1;
        }
    }

    /**
   * Create an action for a game setting so that it has the proper name, accelerator, mnemonic, icon, and tool tip
   * 
   * @param name Name of the game
   * @param file Actual path of the game settings file
   * @param order The order of placement for the state. The accelerator and mnemonic use this order
   * @return The updated action.
   */
    private Action createGameSettingsAction(String name, File file, int order) {
        Action action = new AbstractAction() {

            public void actionPerformed(ActionEvent event) {
                frame.startBusy();
                File file = new File((String) getValue(Action.ACTION_COMMAND_KEY));
                try {
                    if (file.equals(knownSettingsFiles.getDefaultSettingsFile())) return;
                    if (gameSelectionCallable != null) {
                        try {
                            currentAction = this;
                            if (!gameSelectionCallable.call().booleanValue()) {
                                updateGameMenu();
                                return;
                            }
                        } catch (Exception e1) {
                            LOGGER.log(Level.WARNING, "Exception executing the game selection callable.");
                        }
                    }
                    knownSettingsFiles.loadSettingsFile(file, null, null);
                } catch (RuntimeException e) {
                    LOGGER.log(Level.WARNING, "Exception loading the settings file: " + file.getAbsolutePath(), e);
                    JOptionPane.showMessageDialog(frame, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                    knownSettingsFiles.removeKnownSettingsFile(file);
                } finally {
                    frame.endBusy();
                }
            }
        };
        if (order < 10) {
            action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt " + order));
            action.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_0 + order);
            action.putValue(Action.NAME, "" + order + " " + name + " ");
        } else {
            action.putValue(Action.ACCELERATOR_KEY, null);
            action.putValue(Action.MNEMONIC_KEY, null);
            action.putValue(Action.NAME, name + " ");
        }
        action.putValue(Action.SHORT_DESCRIPTION, "Edit characters from the " + name + " game.");
        action.putValue(Action.ACTION_COMMAND_KEY, file.toString());
        if (file.equals(knownSettingsFiles.getDefaultSettingsFile())) action.putValue(Action.SELECTED_KEY, Boolean.TRUE);
        return action;
    }

    /** @return Getter for currentAction */
    public Action getCurrentAction() {
        return currentAction;
    }

    /**
   * Update a file from the server.
   * 
   * @param file The file that is being replaced.
   */
    private void updateFile(File file) {
        File temp = null;
        try {
            Boolean update = Utilities.checkGameFile(file);
            if (update == null || update.booleanValue()) return;
            String game = file.getName();
            game = game.substring(0, game.lastIndexOf('.'));
            temp = File.createTempFile(game, ".rpgame");
            FileUtil.copyFile(file, temp);
            Utilities.checkGameFile(game, true);
            temp.delete();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unable to copy current version of the file.", e);
            JOptionPane.showMessageDialog(frame, "Unable to copy current version of the file.", "Error!", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException e) {
            LOGGER.log(Level.WARNING, "Unable to update the existing game file from the server.", e);
            JOptionPane.showMessageDialog(frame, "Unable to update the existing game file from the server.", "Error!", JOptionPane.ERROR_MESSAGE);
            try {
                FileUtil.copyFile(temp, file);
            } catch (IOException e1) {
                LOGGER.log(Level.WARNING, "Unable to restore old version of the file from:\n   " + temp.getPath(), e);
                JOptionPane.showMessageDialog(frame, "Unable to restore old version of the file from:\n   " + temp.getPath() + "\n   This file may be loaded manually. The current game file is removed from the list.", "Error!", JOptionPane.ERROR_MESSAGE);
                knownSettingsFiles.removeKnownSettingsFile(file);
            }
        }
    }
}
