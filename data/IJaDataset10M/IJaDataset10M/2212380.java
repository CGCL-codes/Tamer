package simtools.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import simtools.data.DataException;
import simtools.data.DataSource;
import simtools.data.EndNotificationListener;
import simtools.data.async.TimeStampedDataSource;
import simtools.data.async.StreamingTSDataSource;

public class TimeStampedDataSourceInformation extends DataSourceInformation implements EndNotificationListener {

    protected static MenuResourceBundle resources = ResourceFinder.getMenu(TimeStampedDataSourceInformation.class);

    public static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

    protected JTextField tfTime1;

    protected JTextField tfTime2;

    protected JTextField tfPhysical;

    public void setPanel(DataSource ds) {
        dataSource = ds;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = STANDARD_INSETS;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 0.2;
        add(createStaticInformationPanel(), gbc);
        gbc.gridy++;
        gbc.weighty = 0.8;
        add(createTimeStampedDataSourceInformation(), gbc);
        dataSource.addEndNotificationListener(this);
    }

    protected JPanel createTimeStampedDataSourceInformation() {
        JPanel p = new JPanel();
        p.setBorder(new TitledBorder(resources.getString("dynamicInformation")));
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = STANDARD_INSETS;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tfTime1 = new JTextField();
        tfTime2 = new JTextField();
        tfPhysical = new JTextField();
        tfTime1.setEditable(false);
        tfTime2.setEditable(false);
        tfPhysical.setEditable(false);
        tfTime1.setColumns(FIELD_SIZE);
        tfTime2.setColumns(FIELD_SIZE);
        tfPhysical.setColumns(FIELD_SIZE);
        p.add(new JLabel(resources.getString("time1")), gbc);
        gbc.weightx = 1;
        gbc.gridwidth = 3;
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.WEST;
        tfTime1.setMinimumSize(tfTime1.getPreferredSize());
        p.add(tfTime1, gbc);
        if ((dataSource instanceof StreamingTSDataSource) && ((StreamingTSDataSource) dataSource).getTime2() != null) {
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.weightx = 0;
            gbc.anchor = GridBagConstraints.EAST;
            p.add(new JLabel(resources.getString("time2")), gbc);
            gbc.weightx = 1;
            gbc.gridwidth = 3;
            gbc.gridx++;
            gbc.anchor = GridBagConstraints.WEST;
            tfTime2.setMinimumSize(tfTime2.getPreferredSize());
            p.add(tfTime2, gbc);
        }
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p.add(new JLabel(resources.getString("physical")), gbc);
        gbc.weightx = 1;
        gbc.gridwidth = 3;
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.WEST;
        tfPhysical.setMinimumSize(tfPhysical.getPreferredSize());
        p.add(tfPhysical, gbc);
        updateTSvalues();
        return p;
    }

    public void notificationEnd(Object referer) {
        updatevalues();
    }

    protected void updateTSvalues() {
        try {
            if (dataSource instanceof TimeStampedDataSource) {
                Number time = (Number) ((TimeStampedDataSource) dataSource).getTime().getValue(((TimeStampedDataSource) dataSource).getTime().getLastIndex());
                tfTime1.setText(dateTimeFormatter.format(new Date(time.longValue())));
                Object value = ((TimeStampedDataSource) dataSource).getValue(((TimeStampedDataSource) dataSource).getLastIndex());
                tfPhysical.setText(value.toString());
            }
            if ((dataSource instanceof StreamingTSDataSource) && ((StreamingTSDataSource) dataSource).getTime2() != null) {
                Number time2 = (Number) ((StreamingTSDataSource) dataSource).getTime2().getValue(((StreamingTSDataSource) dataSource).getTime2().getLastIndex());
                tfTime2.setText(dateTimeFormatter.format(new Date(time2.longValue())));
            }
        } catch (DataException e) {
        }
    }

    protected void updatevalues() {
        updateTSvalues();
    }

    public void dispose() {
        dataSource.removeEndNotificationListener(this);
    }
}
