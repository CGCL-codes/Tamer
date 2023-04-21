package com.ivis.xprocess.web.client.widgets;

import javax.swing.JLabel;
import com.ivis.xprocess.web.client.pages.OrganizationPage;
import com.ivis.xprocess.web.client.ui.ClientManager;

public class OrganizationHyperlinkLine extends XelementHyperlinkLine {

    private static final long serialVersionUID = 1L;

    public OrganizationHyperlinkLine(String elementName, String uuid, final ClientManager clientManager) {
        super(elementName, uuid, clientManager);
    }

    @Override
    public void createLinks() {
        elementNameLabel = new JLabel();
        elementNameLabel.setText(elementName);
        add(elementNameLabel);
        openLink = new HyperlinkLabel(underlineText("Open"), new IHyperlinkListener() {

            @Override
            public void linkClicked() {
                clientManager.setPage(new OrganizationPage(clientManager, elementUuid), false);
            }
        });
        editLink = new HyperlinkLabel(underlineText("Edit"), new IHyperlinkListener() {

            @Override
            public void linkClicked() {
            }
        });
        deleteLink = new HyperlinkLabel(underlineText("Delete"), new IHyperlinkListener() {

            @Override
            public void linkClicked() {
            }
        });
        add(openLink);
        add(editLink);
        add(deleteLink);
    }
}
