package org.eyrene.jgames.ui;

import java.net.URL;
import java.util.*;
import javax.swing.ImageIcon;
import org.eyrene.jgames.GameSettings;
import org.eyrene.jgames.core.*;

/**
 * <p>Title: GameGUIBuilder.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: eyrene</p>
 * @author Francesca Vizzi
 * @version 1.0
 */
public class GameGUIBuilder implements GameUIBuilder {

    protected GameSettings gameSettings;

    protected GameRules gameRules;

    protected GameGUI gameGUI;

    public GameGUIBuilder(GameSettings gameSettings, GameRules gameRules) {
        if (gameSettings == null || gameRules == null) throw new NullPointerException("PRE-CONDIZIONE VIOLATA!");
        this.gameSettings = gameSettings;
        this.gameRules = gameRules;
        this.gameGUI = new GameGUI(gameSettings, gameRules);
    }

    public GameUIBuilder buildGameViews() {
        buildGameFieldView(gameSettings.getGameFieldSettings());
        for (int i = 0; i < gameSettings.getPlayersNumber(); i++) {
            buildPlayerView(i, gameSettings.getPlayersSettings()[i]);
            buildPlayerPiecesView(gameRules.getFirstGameStatus().getPlayersStatus()[i].allPieces(), gameSettings.getGameFieldSettings().getPiecesViewMap());
        }
        buildGameView();
        buildGameMasterView();
        return this;
    }

    public GameUIBuilder buildGameControllers() {
        buildGameFieldController();
        for (int i = 0; i < gameSettings.getPlayersNumber(); i++) {
            buildPlayerController(i);
        }
        buildGameController();
        buildGameMasterController();
        return this;
    }

    public GameUI getGameUI() {
        this.gameGUI.initMVC();
        return this.gameGUI;
    }

    private void buildGameView() {
        gameGUI.setGameView(new GameGUIView());
    }

    private void buildGameFieldView(GameSettings.GameFieldSettings gameFieldSettings) throws NullPointerException, IllegalArgumentException {
        if (gameFieldSettings == null) throw new NullPointerException("PRE-CONDIZIONE VIOLATA!");
        int[] dimensions = gameFieldSettings.getDimensions();
        Class cellViewClass = gameFieldSettings.getCellViewClass();
        if (dimensions == null || cellViewClass == null) throw new NullPointerException("PRE-CONDIZIONE VIOLATA!");
        for (int i = 0; i < dimensions.length; i++) if (dimensions[i] < 0) throw new IllegalArgumentException("PRE-CONDIZIONE VIOLATA!");
        if (!(CellView.class).isAssignableFrom(cellViewClass)) throw new IllegalArgumentException("PRE-CONDIZIONE VIOLATA!");
        GameFieldView gameFieldView = null;
        switch(dimensions.length) {
            case 2:
                if (!(Cell2DGUIView.class).isAssignableFrom(cellViewClass)) throw new IllegalArgumentException("PRE-CONDIZIONE VIOLATA!");
                Cell2DGUIView[][] c2Dv = new Cell2DGUIView[dimensions[0]][dimensions[1]];
                for (int i = 0; i < c2Dv.length; i++) {
                    for (int j = 0; j < c2Dv[0].length; j++) {
                        try {
                            c2Dv[i][j] = (Cell2DGUIView) cellViewClass.newInstance();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                gameFieldView = new GameField2DGUIView(c2Dv);
                ((GameField2DGUIView) gameFieldView).setCellsViewMap(gameFieldSettings.getCellsViewMap());
                ((GameField2DGUIView) gameFieldView).setPiecesViewMap(gameFieldSettings.getPiecesViewMap());
                break;
            case 3:
                throw new UnsupportedOperationException("3D view not yet implemented!");
            default:
                throw new IllegalArgumentException("PRE-CONDIZIONE VIOLATA!");
        }
        gameGUI.setGameFieldView(gameFieldView);
    }

    private void buildPlayerPiecesView(Set allPieces, Map piecesViewMap) {
        if (allPieces == null || piecesViewMap == null) throw new NullPointerException("PRE-CONDIZIONE VIOLATA!");
        Piece piece;
        PieceGUIView pieceView;
        Iterator iter = allPieces.iterator();
        while (iter.hasNext()) {
            piece = (Piece) iter.next();
            pieceView = PieceGUIView.getInstance(piece);
            Object obj = piecesViewMap.get(piece);
            if (obj == null) {
            } else if (obj instanceof String) {
                pieceView.setName(obj.toString());
                pieceView.paintIcon(false);
                pieceView.paintName(true);
            } else if (obj instanceof URL) {
                ImageIcon icon = new ImageIcon((URL) obj);
                pieceView.setPieceIcon(icon);
                pieceView.paintName(false);
                pieceView.paintIcon(true);
            }
        }
    }

    private void buildGameMasterView() {
        gameGUI.setGameMasterView(new GameMasterGUIView());
    }

    private void buildPlayerView(int player, GameSettings.PlayerSettings playerSettings) {
        gameGUI.setPlayerView(player, new PlayerGUIView(playerSettings));
    }

    private void buildGameController() {
        gameGUI.setGameController(new GameGUIController());
    }

    private void buildGameFieldController() {
        gameGUI.setGameFieldController(new GameFieldGUIController());
    }

    private void buildGameMasterController() {
        gameGUI.setGameMasterController(new GameMasterGUIController());
    }

    private void buildPlayerController(int player) {
        gameGUI.setPlayerController(player, new PlayerGUIController());
    }
}
