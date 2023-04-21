package tcg.scada.sim.iecsim.scripter;

import tcg.scada.sim.iecsim.datastore.DataStore;

public interface IScripter {

    public abstract void run_script(String fileName);

    public abstract DataStore getDataStore();

    public abstract IScriptAPI getScriptAPI();

    public void start();

    public void stop();
}
