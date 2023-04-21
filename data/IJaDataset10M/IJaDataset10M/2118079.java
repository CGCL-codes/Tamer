package info.reflectionsofmind.connexion.fortress.core.common.action;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;
import info.reflectionsofmind.connexion.util.convert.CompositeDecoder;

public final class ActionDecoder extends CompositeDecoder<AbstractAction> {

    public ActionDecoder(ServerGame game) {
        add(new EndTurnAction.Decoder(game));
        add(new MeeplePlacementAction.Decoder(game));
        add(new TilePlacementAction.Decoder(game));
    }
}
