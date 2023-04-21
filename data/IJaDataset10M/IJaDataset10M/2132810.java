package rails.game.correct;

import java.util.*;
import rails.common.DisplayBuffer;
import rails.common.LocalText;
import rails.game.*;

public class MapCorrectionManager extends CorrectionManager {

    public static enum ActionStep {

        SELECT_HEX, SELECT_TILE, SELECT_ORIENTATION, CONFIRM, RELAY_BASETOKENS, FINISHED, CANCELLED
    }

    private MapCorrectionAction activeTileAction = null;

    protected MapCorrectionManager(GameManager gm) {
        super(gm, CorrectionType.CORRECT_MAP);
    }

    @Override
    public List<CorrectionAction> createCorrections() {
        List<CorrectionAction> actions = super.createCorrections();
        if (isActive()) {
            if (activeTileAction == null) activeTileAction = new MapCorrectionAction();
            actions.add(activeTileAction);
        }
        return actions;
    }

    @Override
    public boolean executeCorrection(CorrectionAction action) {
        if (action instanceof MapCorrectionAction) return execute((MapCorrectionAction) action); else return super.executeCorrection(action);
    }

    private boolean execute(MapCorrectionAction action) {
        if (action.getStep() == ActionStep.FINISHED) {
            action.setNextStep(ActionStep.FINISHED);
        } else if (action.getNextStep() == ActionStep.CANCELLED) {
            activeTileAction = null;
            return true;
        }
        MapHex hex = action.getLocation();
        TileI chosenTile = action.getChosenTile();
        TileManager tmgr = gameManager.getTileManager();
        TileI preprintedTile = tmgr.getTile(hex.getPreprintedTileId());
        String errMsg = null;
        while (true) {
            if (chosenTile != null && rails.util.Util.hasValue(chosenTile.getExternalId()) && chosenTile != hex.getCurrentTile() && chosenTile.countFreeTiles() == 0) {
                errMsg = LocalText.getText("TileNotAvailable", chosenTile.getExternalId());
                action.selectHex(hex);
                break;
            }
            List<BaseToken> baseTokens = hex.getBaseTokens();
            if (chosenTile != null && baseTokens != null && !baseTokens.isEmpty()) {
                List<Station> stations = chosenTile.getStations();
                int nbSlots = 0;
                if (stations != null) {
                    for (Station station : stations) {
                        nbSlots += station.getBaseSlots();
                    }
                }
                if (baseTokens.size() > nbSlots) {
                    errMsg = LocalText.getText("CorrectMapNotEnoughSlots", chosenTile.getExternalId());
                    action.selectHex(hex);
                    break;
                }
                if (chosenTile.getNumStations() >= 2 && hex.getCurrentTile().getColourNumber() >= chosenTile.getColourNumber() || hex.getCurrentTile().relayBaseTokensOnUpgrade()) {
                    errMsg = LocalText.getText("CorrectMapRequiresRelays", chosenTile.getExternalId());
                    action.selectHex(hex);
                    break;
                }
            }
            break;
        }
        if (errMsg != null) {
            DisplayBuffer.add(LocalText.getText("CorrectMapCannotLayTile", chosenTile.getExternalId(), hex.getName(), errMsg));
            ;
        }
        ActionStep nextStep;
        if (action.getStep() != ActionStep.FINISHED) nextStep = action.getNextStep(); else nextStep = ActionStep.FINISHED;
        switch(nextStep) {
            case SELECT_TILE:
                List<TileI> possibleTiles = tmgr.getAllUpgrades(preprintedTile, hex);
                if (preprintedTile == hex.getCurrentTile()) possibleTiles.remove(hex.getCurrentTile());
                action.setTiles(possibleTiles);
                break;
            case SELECT_ORIENTATION:
                if (preprintedTile == chosenTile) {
                    action.selectOrientation(hex.getPreprintedTileRotation());
                    action.setNextStep(ActionStep.CONFIRM);
                    break;
                } else if (chosenTile.getFixedOrientation() != -1) {
                    action.selectOrientation(chosenTile.getFixedOrientation());
                    action.setNextStep(ActionStep.CONFIRM);
                    break;
                } else {
                    break;
                }
            case RELAY_BASETOKENS:
                if (chosenTile.getNumStations() >= 2 && hex.getCurrentTile().getColourNumber() >= chosenTile.getColourNumber() || hex.getCurrentTile().relayBaseTokensOnUpgrade()) {
                    List<BaseToken> tokens = new ArrayList<BaseToken>();
                    for (Stop oldStop : hex.getStops()) {
                        for (TokenI token : oldStop.getTokens()) {
                            if (token instanceof BaseToken) {
                                tokens.add((BaseToken) token);
                            }
                        }
                    }
                    action.setTokensToRelay(tokens);
                    action.setPossibleStations(chosenTile.getStations());
                    break;
                } else {
                    action.selectRelayBaseTokens(null);
                    return execute(action);
                }
            case FINISHED:
                gameManager.getMoveStack().start(false);
                int orientation = action.getOrientation();
                hex.upgrade(chosenTile, orientation, new HashMap<String, Integer>());
                String msg = LocalText.getText("CorrectMapLaysTileAt", chosenTile.getExternalId(), hex.getName(), hex.getOrientationName(orientation));
                ReportBuffer.add(msg);
                gameManager.addToNextPlayerMessages(msg, true);
                activeTileAction = null;
                break;
            case CANCELLED:
                activeTileAction = null;
        }
        if (action.getStep() != ActionStep.FINISHED) {
            action.moveToNextStep();
        }
        return true;
    }
}
