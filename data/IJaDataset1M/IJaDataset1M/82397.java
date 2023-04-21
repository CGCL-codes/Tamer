package com.inetmon.jn.graphicwebmonitor.extraViews;

import java.sql.ResultSet;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * UI design for search page
 * @author   inetmon
 */
public class AnalyserPage extends WizardPage {

    private String querySearch;

    LoadDriverJDBC jdbc = new LoadDriverJDBC();

    private String mac = "";

    /**
	 * NamePage constructor
	 */
    public AnalyserPage(String mac) {
        super("Machine Description", "Machine Description", null);
        setDescription("Machine Information is Detailed Below");
        this.mac = mac;
        setPageComplete(false);
    }

    /**
	 * Creates the page contents
	 * 
	 * @param parent
	 *            the parent composite
	 */
    public void createControl(Composite parent) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        parent.setLayout(gridLayout);
        Group group1 = new Group(parent, SWT.NONE);
        group1.setText("Warning");
        group1.setLayout(new GridLayout(2, false));
        group1.setVisible(false);
        GridData grid = new GridData();
        grid.horizontalSpan = 2;
        group1.setLayoutData(grid);
        Group group12 = new Group(parent, SWT.NONE);
        group12.setText("Node information");
        group12.setLayout(new GridLayout(2, false));
        group12.setVisible(false);
        Group group22 = new Group(parent, SWT.NONE);
        group22.setText("Group information");
        group22.setLayout(new GridLayout(2, false));
        GridData gridData = new GridData();
        gridData.verticalAlignment = 1;
        group22.setLayoutData(gridData);
        group22.setVisible(false);
        GridData data = new GridData();
        data.widthHint = 100;
        new Label(group12, SWT.LEFT).setText("MAC Address:");
        final Text MAC = new Text(group12, SWT.READ_ONLY);
        MAC.setLayoutData(data);
        MAC.setTextLimit(20);
        new Label(group12, SWT.LEFT).setText("IPV4 Address:");
        final Text IPV4 = new Text(group12, SWT.READ_ONLY);
        IPV4.setLayoutData(data);
        IPV4.setTextLimit(20);
        new Label(group12, SWT.LEFT).setText("IPV6 Address:");
        final Text IPV6 = new Text(group12, SWT.READ_ONLY);
        IPV6.setLayoutData(data);
        IPV6.setTextLimit(20);
        new Label(group12, SWT.LEFT).setText("NetBios Name:");
        final Text NetBios = new Text(group12, SWT.READ_ONLY);
        NetBios.setLayoutData(data);
        NetBios.setTextLimit(20);
        new Label(group12, SWT.LEFT).setText("OS:");
        final Text OS = new Text(group12, SWT.READ_ONLY);
        OS.setLayoutData(data);
        OS.setTextLimit(20);
        Label labeldes = new Label(group12, SWT.LEFT);
        labeldes.setText("Description:");
        labeldes.setLayoutData(data);
        final Text Description = new Text(group12, SWT.READ_ONLY);
        Description.setLayoutData(data);
        Description.setTextLimit(20);
        new Label(group12, SWT.LEFT).setText("Date:");
        final Text datetime = new Text(group12, SWT.READ_ONLY);
        datetime.setLayoutData(data);
        datetime.setTextLimit(10);
        new Label(group22, SWT.LEFT).setText("Workgroup:");
        final Text Workgroup = new Text(group22, SWT.READ_ONLY);
        Workgroup.setLayoutData(data);
        Workgroup.setTextLimit(20);
        new Label(group22, SWT.LEFT).setText("Subnet:");
        final Text Subnet = new Text(group22, SWT.READ_ONLY);
        Subnet.setLayoutData(data);
        Subnet.setTextLimit(20);
        querySearch = "SELECT * FROM informations WHERE MACAddress='" + mac + "'";
        ResultSet rs = jdbc.JDBCexecuteQuerySearch(querySearch);
        try {
            rs.last();
            int rowsnumber = rs.getRow();
            rs.beforeFirst();
            if (rowsnumber != 0) {
                while (rs.next()) {
                    MAC.setText(rs.getString("MACAddress"));
                    IPV4.setText(rs.getString("IPV4"));
                    IPV6.setText(rs.getString("IPV6"));
                    NetBios.setText(rs.getString("Netbios"));
                    Description.setText(rs.getString("Description"));
                    datetime.setText(rs.getString("DateCapture"));
                    OS.setText(rs.getString("OS"));
                    Workgroup.setText(rs.getString("Workgroup"));
                    Subnet.setText(rs.getString("Subnet"));
                    setPageComplete(true);
                    group12.setVisible(true);
                    group22.setVisible(true);
                }
                rs.close();
            } else {
                group1.setVisible(true);
                Label label = new Label(group1, SWT.CENTER);
                label.setSize(100, 25);
                label.setText("The MAC address doesn't exist in the database, please run the address book!");
            }
        } catch (Exception e1) {
            System.out.println("ERROR" + e1);
            e1.printStackTrace();
        }
        setControl(parent);
    }
}
