package fr.ign.cogit.geoxygene.datatools.castor.conf;

/** Usage interne. */
public class Database {

    private String _name;

    private String _engine;

    private Driver _driver;

    public Database() {
        super();
    }

    public Driver getDriver() {
        return this._driver;
    }

    public String getEngine() {
        return this._engine;
    }

    public String getName() {
        return this._name;
    }

    public void setDriver(Driver driver) {
        this._driver = driver;
    }

    public void setEngine(String engine) {
        this._engine = engine;
    }

    public void setName(String name) {
        this._name = name;
    }
}
