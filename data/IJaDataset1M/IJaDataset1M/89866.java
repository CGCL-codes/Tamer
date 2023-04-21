package org.fao.gast.gui.panels.manag.mefexport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import jeeves.utils.XmlRequest;
import org.dlib.gui.FlexLayout;
import org.dlib.gui.TPanel;
import org.fao.gast.app.App;
import org.fao.gast.app.Configuration;
import org.fao.gast.lib.Lib;
import org.fao.gast.localization.Messages;
import org.fao.geonet.constants.Geonet;
import org.jdom.Element;

public class SearchPanel extends TPanel implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = -9161193939072633779L;

    public SearchPanel() {
        super(Messages.getString("search"));
        FlexLayout fl = new FlexLayout(3, 4);
        fl.setColProp(1, FlexLayout.EXPAND);
        setLayout(fl);
        add("0,0", new JLabel(Messages.getString("freeText")));
        add("1,0,x", txtAny);
        add("0,1", new JLabel(Messages.getString("siteId")));
        add("1,1,x", cmbSiteId);
        add("2,1", btnRetrieve);
        add("0,2", new JLabel(Messages.getString("groupOwner")));
        add("1,2,x", cmbGroup);
        add("0,3", new JLabel(Messages.getString("templates")));
        add("1,3", chbTemp);
        btnRetrieve.addActionListener(this);
        cmbSiteId.addItem(new ComboInfo());
        cmbGroup.addItem(new ComboInfo());
    }

    public Element createRequest() {
        Element req = new Element("request").addContent(new Element("any").setText(txtAny.getText())).addContent(new Element("template").setText(chbTemp.isSelected() ? "y" : "n"));
        ComboInfo site = (ComboInfo) cmbSiteId.getSelectedItem();
        ComboInfo group = (ComboInfo) cmbGroup.getSelectedItem();
        if (site.id != null) req.addContent(new Element("siteId").setText(site.id));
        if (group.id != null) req.addContent(new Element("group").setText(group.id));
        return req;
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            Configuration cfg = App.config;
            XmlRequest req = new XmlRequest(cfg.getHost(), cfg.getPort());
            if (cfg.useAccount()) Lib.service.login(req);
            req.setAddress("/" + cfg.getServlet() + "/srv/en/" + Geonet.Service.XML_INFO);
            Element params = new Element("request").addContent(new Element("type").setText("groups")).addContent(new Element("type").setText("sources"));
            handleInfo(req.execute(params));
            if (cfg.useAccount()) Lib.service.logout(req);
        } catch (Exception e) {
            Lib.gui.showError(this, e);
        }
    }

    private void handleInfo(Element info) {
        cmbSiteId.removeAllItems();
        cmbGroup.removeAllItems();
        for (Object o : info.getChild("sources").getChildren()) {
            Element source = (Element) o;
            String uuid = source.getChildText("uuid");
            String name = source.getChildText("name");
            cmbSiteId.addItem(new ComboInfo(uuid, name));
        }
        for (Object o : info.getChild("groups").getChildren()) {
            Element group = (Element) o;
            String id = group.getAttributeValue("id");
            String name = null;
            for (Object o2 : group.getChild("label").getChildren()) {
                Element grpLabel = (Element) o2;
                if (grpLabel.getName().equals("en") || name == null) name = grpLabel.getText();
            }
            cmbGroup.addItem(new ComboInfo(id, name));
        }
        cmbSiteId.addItem(new ComboInfo());
        cmbGroup.addItem(new ComboInfo());
    }

    private JTextField txtAny = new JTextField(20);

    private JComboBox cmbSiteId = new JComboBox();

    private JComboBox cmbGroup = new JComboBox();

    private JCheckBox chbTemp = new JCheckBox();

    private JButton btnRetrieve = new JButton(Messages.getString("retrieve"));
}

class ComboInfo {

    public String id;

    public String name;

    public ComboInfo() {
    }

    public ComboInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
