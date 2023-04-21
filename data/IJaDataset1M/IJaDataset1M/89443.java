package common.persistence.io;

public interface IGameSettingsIO {

    public IPersistenceGameSettings readGameSettings() throws PersistenceIOException;

    public void writeGameSettings(IPersistenceGameSettings _objSettings) throws PersistenceIOException;
}
