package jhomenet.gui.tab;

import java.util.*;
import javax.swing.table.*;
import org.apache.log4j.Logger;
import jhomenet.persistence.*;
import jhomenet.persistence.dao.DAOFactory;
import jhomenet.hw.*;
import jhomenet.hw.data.AbstractHardwareData;
import jhomenet.hw.sensor.Sensor;

/**
 * A custom hardware data table model that automatically updates
 * when the hardware's data updates.
 * <br>
 * Id: $Id$
 * 
 * @author David Irwin
 */
public class AbstractCustomDataTableModel {

    /***
     * Serial version ID information - used for the serialization process.
     */
    private static final long serialVersionUID = 00001;

    /**
     * The logger
     */
    protected static Logger logger = Logger.getLogger(AbstractCustomDataTableModel.class);

    /**
     * Reference to the actual hardware object.
     */
    private Hardware hardware;

    /**
     * The actual table model.
     */
    private DefaultTableModel tableModel = new DefaultTableModel();

    /**
     * Default constructor.
     */
    public AbstractCustomDataTableModel(Hardware hardware) {
        super();
        this.hardware = hardware;
        tableModel.setColumnIdentifiers(new Object[] { "Hardware ID", "Data", "Timestamp", "Channel" });
    }

    /**
     * Build the table model.
     */
    public void buildDataTable() {
        if (hardware instanceof Sensor) {
            Sensor sensor = (Sensor) hardware;
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            List<AbstractHardwareData> dataList = DAOFactory.HIBERNATE.getHardwareDataDAO().getAllData(sensor.getHardwareId());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
            for (AbstractHardwareData data : dataList) {
                tableModel.addRow(new Object[] { data.getHardwareId(), data.getDataString(), data.getTimestamp(), data.getChannel() });
            }
        }
    }

    /**
     * Add data the current data table model.
     * 
     * @param data
     */
    public void addData(AbstractHardwareData data) {
        logger.debug("Adding data to custom table model...");
        Vector dataVector = new Vector();
        dataVector.add(data.getHardwareId());
        dataVector.add(data.getDataString());
        dataVector.add(data.getTimestamp());
        dataVector.add(data.getChannel());
        tableModel.addRow(dataVector);
    }

    public AbstractTableModel getTableModel() {
        return tableModel;
    }
}
