package com.pyrphoros.erddb.gui.windows.imports;

import com.pyrphoros.erddb.Designer;
import com.pyrphoros.erddb.gui.windows.dbconnect.*;
import com.pyrphoros.erddb.db.DriverShim;
import com.pyrphoros.erddb.loader.ModelLoader;
import com.pyrphoros.erddb.gui.util.ParentFinder;
import com.pyrphoros.erddb.loader.ProjectLoader;
import com.pyrphoros.erddb.model.data.DataModel;
import java.awt.Component;
import java.awt.event.*;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 */
public class OKAction implements ActionListener {

    private static Logger logger = Logger.getLogger(OKAction.class);

    private ConnectDialog parent;

    public OKAction(ConnectDialog parent) {
        this.parent = parent;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            String url = parent.getURL();
            Properties props = new Properties();
            if (!parent.getAnonymousLogin()) {
                props.setProperty("user", parent.getUsername());
                props.setProperty("password", parent.getPassword());
            }
            DataModel m = ModelLoader.getModelFromDB(url, props, parent.getSchemaName());
            ProjectLoader.loadProject(m);
            ParentFinder.getParentWindow((Component) e.getSource()).dispose();
        } catch (Exception ex) {
            logger.error(ex);
            JOptionPane.showMessageDialog(parent, ex.getMessage(), "Connection Failed!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
