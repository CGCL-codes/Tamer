package info.reflectionsofmind.connexion.platform.game.server;

public interface IServerGameFactory {

    IServerGame createServerGame(IServerGameParameters parameters);
}
