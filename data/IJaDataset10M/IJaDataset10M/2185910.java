package com.certesystems.swingforms.fields;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTabbedPane;
import org.jdesktop.swingx.JXCollapsiblePane;
import com.certesystems.swingforms.Context;
import com.certesystems.swingforms.entity.Entity;
import com.certesystems.swingforms.links.Link;
import com.certesystems.swingforms.links.LinkTabs;
import com.certesystems.swingforms.tools.IconFactory;

/**
 * 
 * @author mludeiro
 *
 */
public class FieldTabs extends Field {

    private List<Field> tabbedFields;

    @Override
    public Link createLink(Entity entity) {
        LinkTabs linkTabs = new LinkTabs();
        JXCollapsiblePane panel = new JXCollapsiblePane(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        panel.add(tabs, BorderLayout.CENTER);
        linkTabs.setContainer(panel);
        linkTabs.setTabPane(tabs);
        linkTabs.setField(this);
        List<Link> links = new ArrayList<Link>();
        for (Field field : tabbedFields) {
            Link link = field.createLink(entity);
            tabs.addTab(link.getField().getDescription(), IconFactory.getInstance().getIcon(link.getField().getIcon()), link.getContainer());
            links.add(link);
            Context.getContext().getDecorator().decorateTabbedContainer(link.getContainer());
        }
        linkTabs.setContainer(tabs);
        linkTabs.setLinks(links);
        return linkTabs;
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public String getCellValue(Object register) {
        return this.toString();
    }

    public void setTabbedFields(List<Field> tabbedFields) {
        this.tabbedFields = tabbedFields;
    }

    public List<Field> getTabbedFields() {
        return tabbedFields;
    }
}
