package org.mbari.vars.annotation.ui.dialogs;

import org.exolab.castor.jdo.Database;
import org.mbari.vars.annotation.model.VideoArchive;
import org.mbari.vars.annotation.model.dao.VideoArchiveDAO;
import org.mbari.vars.annotation.model.dao.VideoArchiveSetDAO;
import org.mbari.vars.annotation.ui.dispatchers.PredefinedDispatcher;
import org.mbari.vars.dao.DAOException;
import org.mbari.vars.dao.ObjectDAO;
import org.mbari.vars.util.AppFrameDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rachelorange
 * Date: Aug 12, 2010
 * Time: 9:50:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class VideoSourcePanelPreexisting extends JPanel implements IVideoSourcePanel {

    private JComboBox comboBox;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public VideoSourcePanelPreexisting() {
        super();
        initialize();
    }

    public boolean isValidVideoSource() {
        return true;
    }

    public void open() {
        try {
            String name = (String) getComboBox().getSelectedItem();
            VideoArchive videoArchive = VideoArchiveDAO.getInstance().findByVideoArchiveName(name);
            if (videoArchive == null) {
                AppFrameDispatcher.showWarningDialog("Unable to find the dive named " + name + " in the database");
            } else {
                PredefinedDispatcher.VIDEOARCHIVE.getDispatcher().setValueObject(videoArchive);
            }
        } catch (Exception e) {
        }
    }

    private void initialize() {
        setLayout(new java.awt.BorderLayout());
        add(getComboBox(), BorderLayout.CENTER);
    }

    private JComboBox getComboBox() {
        if (comboBox == null) {
            comboBox = new JComboBox();
            List<String> names = findAllVideoArchiveNames();
            for (String name : names) {
                comboBox.addItem(name);
            }
        }
        return comboBox;
    }

    private List<String> findAllVideoArchiveNames() {
        List<String> names = new ArrayList<String>();
        try {
            Database db = ObjectDAO.fetchDatabase();
            db.begin();
            Connection connection = db.getJdbcConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT videoArchiveName FROM VideoArchive";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                names.add(resultSet.getString(1));
            }
            db.commit();
            statement.close();
            db.close();
        } catch (Exception e) {
            log.warn("Failed to look up the list of VideoArchiveNames", e);
        }
        return names;
    }
}
