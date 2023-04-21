package info.reflectionsofmind.connexion.fortress.core.common.action;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.util.convert.CompositeEncoder;

public final class ActionEncoder extends CompositeEncoder<IAction> {

    public ActionEncoder(ClientGame game) {
        add(EndTurnAction.class, new EndTurnAction.Encoder(game));
        add(TilePlacementAction.class, new TilePlacementAction.Encoder(game));
        add(MeeplePlacementAction.class, new MeeplePlacementAction.Encoder(game));
    }
}
