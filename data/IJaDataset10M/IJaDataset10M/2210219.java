package ossobook2010.plugins.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ossobook2010.controller.IGuiController;
import ossobook2010.exceptions.NotConnectedException;
import ossobook2010.helpers.metainfo.UniqueArrayList;
import ossobook2010.plugins.IPlugin;
import ossobook2010.plugins.PluginDatamanager;
import ossobook2010.querys.IQueryManager;
import ossobook2010.plugins.PluginInformation;

/**
 * Example plugin for the OssoBook client.
 * 
 * <p>
 * Queries the current version from the database on initialization.
 * </p>
 * 
 * @author fnuecke
 */
public class Main implements IPlugin, PluginDatamanager {

    private IGuiController queryInterface;

    @Override
    public void initialize(IGuiController queryInterface) {
        this.queryInterface = queryInterface;
        queryInterface.addDatamanager(this);
    }

    @Override
    public PluginInformation getPluginInformation() {
        return new PluginInformation("asdtest", "test", "values", 1, PluginInformation.PluginType.PASSIVE);
    }

    @Override
    public UniqueArrayList<String> getData(String name) {
        UniqueArrayList<String> asd = new UniqueArrayList<String>();
        asd.add("dollertest1");
        asd.add("dollertest2");
        asd.add("dollertest3");
        asd.add("dollertest4");
        asd.add("dollertest5");
        return asd;
    }

    @Override
    public void deactivate() {
        queryInterface.removeDatamanager(this);
    }
}
