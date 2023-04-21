package com.inetmon.jn.addressBook.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import com.inetmon.jn.addressBook.jdbc.LoadDriverJDBC;

/**
 * UI design AddEntry page
 * @author    inetmon
 */
public class AddEntyPage extends WizardPage {

    /**
	 * @uml.property  name="mac"
	 */
    private String mac = "";

    /**
	 * @uml.property  name="ipv4"
	 */
    private String ipv4 = "000.000.000.000";

    /**
	 * @uml.property  name="ipv6"
	 */
    private String ipv6 = "";

    /**
	 * @uml.property  name="netbios"
	 */
    private String netbios = "";

    /**
	 * @uml.property  name="date"
	 */
    private String date = "";

    /**
	 * @uml.property  name="workgroup"
	 */
    private String workgroup = "";

    /**
	 * @uml.property  name="description1"
	 */
    private String description1 = "Manually added";

    /**
	 * @uml.property  name="subnet"
	 */
    private String subnet = "";

    /**
	 * @uml.property  name="iprange"
	 */
    private String iprange = "";

    /**
	 * @uml.property  name="os"
	 */
    private String os = "";

    LoadDriverJDBC jdbc;

    Group composite;

    /**
	 * NamePage constructor
	 */
    public AddEntyPage(LoadDriverJDBC jdbc) {
        super("Add New Entry Page", "Add New Entry", null);
        setDescription("Add New Entry Information");
        this.jdbc = jdbc;
        setPageComplete(false);
    }

    /**
	 * Creates the page contents
	 * 
	 * @param parent
	 *            the parent composite
	 */
    public void createControl(Composite parent) {
        RowLayout row = new RowLayout();
        row.fill = true;
        row.justify = true;
        row.pack = true;
        row.marginBottom = 0;
        row.marginTop = 20;
        parent.setLayout(row);
        composite = new Group(parent, SWT.NONE);
        composite.setText("Node informations");
        composite.setLayout(new GridLayout(2, false));
        Group composite1 = new Group(parent, SWT.NONE);
        composite1.setText("Group informations");
        composite1.setLayout(new GridLayout(2, false));
        GridData data = new GridData();
        data.widthHint = 150;
        verifyMac();
        verifyIPV4();
        new Label(composite, SWT.LEFT).setText("IPV6 Address:  ");
        final Text IPV6 = new Text(composite, SWT.BORDER);
        IPV6.setLayoutData(data);
        IPV6.setTextLimit(20);
        new Label(composite, SWT.LEFT).setText("NetBios Name: ");
        final Text NetBios = new Text(composite, SWT.BORDER);
        NetBios.setLayoutData(data);
        NetBios.setTextLimit(20);
        new Label(composite, SWT.LEFT).setText("OS: ");
        final Text OS = new Text(composite, SWT.BORDER);
        OS.setLayoutData(data);
        OS.setTextLimit(20);
        new Label(composite1, SWT.LEFT).setText("Workgroup:");
        final Text Workgroup = new Text(composite1, SWT.BORDER);
        Workgroup.setLayoutData(data);
        Workgroup.setTextLimit(20);
        new Label(composite1, SWT.LEFT).setText("Subnet:");
        final Text Subnet = new Text(composite1, SWT.BORDER);
        Subnet.setLayoutData(data);
        Subnet.setTextLimit(20);
        IPV6.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                ipv6 = IPV6.getText();
            }
        });
        NetBios.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                netbios = NetBios.getText();
            }
        });
        OS.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                os = OS.getText();
            }
        });
        Workgroup.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                workgroup = Workgroup.getText();
            }
        });
        Subnet.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                subnet = Subnet.getText();
            }
        });
        setControl(composite);
    }

    /**
	 * @return
	 * @uml.property  name="date"
	 */
    public String getDate() {
        return date;
    }

    /**
	 * @return
	 * @uml.property  name="ipv4"
	 */
    public String getIpv4() {
        return ipv4;
    }

    /**
	 * @return
	 * @uml.property  name="ipv6"
	 */
    public String getIpv6() {
        return ipv6;
    }

    /**
	 * @return
	 * @uml.property  name="mac"
	 */
    public String getMac() {
        return mac;
    }

    /**
	 * @return
	 * @uml.property  name="netbios"
	 */
    public String getNetbios() {
        return netbios;
    }

    /**
	 * @return
	 * @uml.property  name="subnet"
	 */
    public String getSubnet() {
        return subnet;
    }

    /**
	 * @return
	 * @uml.property  name="workgroup"
	 */
    public String getWorkgroup() {
        return workgroup;
    }

    /**
	 * @return
	 * @uml.property  name="iprange"
	 */
    public String getIprange() {
        return iprange;
    }

    /**
	 * verify the mac entry
	 * 
	 */
    public void verifyMac() {
        String REGEX = "\\d{2}[:]\\d{2}[:]\\d{2}[:]\\d{2}[:]\\d{2}[:]\\d{2}";
        String template = "00:00:00:00:00:00";
        GridData data = new GridData();
        data.widthHint = 150;
        new Label(composite, SWT.LEFT).setText("MAC Address:  ");
        final Text MAC = new Text(composite, SWT.BORDER);
        MAC.setText(template);
        MAC.setLayoutData(data);
        MAC.setTextLimit(20);
        MAC.addListener(SWT.Verify, new Listener() {

            boolean ignore;

            public void handleEvent(Event e) {
                if (ignore) return;
                e.doit = false;
                StringBuffer buffer = new StringBuffer(e.text);
                char[] chars = new char[buffer.length()];
                buffer.getChars(0, chars.length, chars, 0);
                if (e.character == '\b') {
                    for (int i = e.start; i < e.end; i++) {
                        switch(i) {
                            case 0:
                            case 1:
                            case 3:
                            case 4:
                            case 6:
                            case 7:
                            case 9:
                            case 10:
                            case 12:
                            case 13:
                            case 15:
                            case 16:
                                {
                                    buffer.append('0');
                                    break;
                                }
                            case 2:
                            case 5:
                            case 8:
                            case 11:
                            case 14:
                                {
                                    buffer.append(':');
                                    break;
                                }
                            default:
                                return;
                        }
                    }
                    MAC.setSelection(e.start, e.start + buffer.length());
                    ignore = true;
                    MAC.insert(buffer.toString());
                    ignore = false;
                    MAC.setSelection(e.start, e.start);
                    return;
                }
                int start = e.start;
                if (start > 16) return;
                int index = 0;
                for (int i = 0; i < chars.length; i++) {
                    if (start + index == 2 || start + index == 5 || start + index == 8 || start + index == 11 || start + index == 14) {
                        if (chars[i] == ':') {
                            index++;
                            continue;
                        }
                        buffer.insert(index++, ':');
                    }
                    if ((!('a' <= chars[i] && chars[i] <= 'f')) && (!('0' <= chars[i] && chars[i] <= '9'))) {
                        return;
                    }
                    index++;
                }
                String newText = buffer.toString();
                int length = newText.length();
                MAC.setSelection(e.start, e.start + length);
                ignore = true;
                MAC.insert(newText);
                ignore = false;
            }
        });
        MAC.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                mac = MAC.getText();
                setPageComplete(mac.indexOf("#") == -1);
            }
        });
    }

    /**
	 * verify the IPV4 entry
	 * 
	 */
    public void verifyIPV4() {
        String REGEX = "\\d{2}[.]\\d{3}[.]\\d{3}[.]\\d{3}";
        String template = "000.000.000.000";
        GridData data = new GridData();
        data.widthHint = 150;
        new Label(composite, SWT.LEFT).setText("IPV4 Address: ");
        final Text IPV4 = new Text(composite, SWT.BORDER);
        IPV4.setText(template);
        IPV4.setLayoutData(data);
        IPV4.setTextLimit(20);
        IPV4.addListener(SWT.Verify, new Listener() {

            boolean ignore;

            public void handleEvent(Event e) {
                if (ignore) return;
                e.doit = false;
                StringBuffer buffer = new StringBuffer(e.text);
                char[] chars = new char[buffer.length()];
                buffer.getChars(0, chars.length, chars, 0);
                if (e.character == '\b') {
                    for (int i = e.start; i < e.end; i++) {
                        switch(i) {
                            case 0:
                            case 1:
                            case 2:
                            case 4:
                            case 5:
                            case 6:
                            case 8:
                            case 9:
                            case 10:
                            case 12:
                            case 13:
                            case 14:
                                {
                                    buffer.append('0');
                                    break;
                                }
                            case 3:
                            case 7:
                            case 11:
                                {
                                    buffer.append('.');
                                    break;
                                }
                            default:
                                return;
                        }
                    }
                    IPV4.setSelection(e.start, e.start + buffer.length());
                    ignore = true;
                    IPV4.insert(buffer.toString());
                    ignore = false;
                    IPV4.setSelection(e.start, e.start);
                    return;
                }
                int start = e.start;
                if (start > 14) return;
                int index = 0;
                for (int i = 0; i < chars.length; i++) {
                    if (start + index == 3 || start + index == 7 || start + index == 11) {
                        if (chars[i] == '.') {
                            index++;
                            continue;
                        }
                        buffer.insert(index++, '.');
                    }
                    if (chars[i] < '0' || '9' < chars[i]) return;
                    if (start + index == 0 && '2' < chars[i]) return;
                    if (start + index == 4 && '2' < chars[i]) return;
                    if (start + index == 8 && '2' < chars[i]) return;
                    if (start + index == 12 && '2' < chars[i]) return;
                    index++;
                }
                String newText = buffer.toString();
                int length = newText.length();
                IPV4.setSelection(e.start, e.start + length);
                ignore = true;
                IPV4.insert(newText);
                ignore = false;
            }
        });
        IPV4.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                ipv4 = IPV4.getText();
            }
        });
    }

    /**
	 * @param  ipv4
	 * @uml.property  name="ipv4"
	 */
    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    /**
	 * @return
	 * @uml.property  name="os"
	 */
    public String getOs() {
        return os;
    }

    /**
	 * @param  os
	 * @uml.property  name="os"
	 */
    public void setOs(String os) {
        this.os = os;
    }

    /**
	 * @return
	 * @uml.property  name="description1"
	 */
    public String getDescription1() {
        return description1;
    }

    /**
	 * @param  description1
	 * @uml.property  name="description1"
	 */
    public void setDescription1(String description1) {
        this.description1 = description1;
    }
}
