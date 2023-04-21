package org.synote.player.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.layout.CardLayout;
import com.gwtext.client.widgets.layout.FitLayout;

public class AbstractWidget extends Composite {

    private AbstractFrame frame;

    private Panel outerPanel;

    private Panel innerPanel;

    public AbstractWidget(AbstractFrame parent, boolean initTopToolbar) {
        this.frame = parent;
        init(initTopToolbar);
    }

    public AbstractWidget(AbstractFrame parent) {
        this.frame = parent;
        init();
    }

    private void init(boolean initTopToolbar) {
        outerPanel = new Panel();
        outerPanel.setBorder(false);
        outerPanel.setLayout(new FitLayout());
        outerPanel.setBottomToolbar(new Toolbar());
        if (initTopToolbar) outerPanel.setTopToolbar(new Toolbar());
        innerPanel = new Panel();
        innerPanel.setBorder(false);
        innerPanel.setLayout(new CardLayout());
        outerPanel.add(innerPanel);
    }

    private void init() {
        outerPanel = new Panel();
        outerPanel.setBorder(false);
        outerPanel.setLayout(new FitLayout());
        outerPanel.setBottomToolbar(new Toolbar());
        innerPanel = new Panel();
        innerPanel.setBorder(false);
        innerPanel.setLayout(new CardLayout());
        outerPanel.add(innerPanel);
    }

    public AbstractFrame getFrame() {
        return frame;
    }

    public ProfileManager getProfile() {
        return frame.getProfile();
    }

    public PlayerModel getModel() {
        return frame.getModel();
    }

    public MultimediaController getController() {
        return frame.getController();
    }

    public String getName() {
        String name = getClass().getName();
        int index = name.lastIndexOf(".");
        if (index != -1) name = name.substring(index + 1, name.length());
        return name;
    }

    public Panel getOuterPanel() {
        return outerPanel;
    }

    public Panel getInnerPanel() {
        return innerPanel;
    }

    public void addWidget(Widget widget) {
        innerPanel.add(widget);
    }

    public void addInfoMessage(String message) {
        innerPanel.add(new HTML(message));
    }

    public void addErrorMessage(String message) {
        innerPanel.add(new HTML(message));
    }

    public int getActiveItem() {
        return innerPanel.getActiveItem();
    }

    public void setActiveItem(int index) {
        innerPanel.setActiveItem(index);
    }

    public void addBottomButton(ToolbarButton button) {
        outerPanel.getBottomToolbar().addButton(button);
    }

    public void addBottomSpace() {
        outerPanel.getBottomToolbar().addFill();
    }

    public void addBottomItem(ToolbarTextItem textItem) {
        outerPanel.getBottomToolbar().addItem(textItem);
    }

    public void addBottomSeparator() {
        outerPanel.getBottomToolbar().addSeparator();
    }

    public void addTopMenuButton(ToolbarMenuButton button) {
        if (outerPanel.getTopToolbar() != null) outerPanel.getTopToolbar().addButton(button);
    }

    public void addTopButton(ToolbarButton button) {
        if (outerPanel.getTopToolbar() != null) outerPanel.getTopToolbar().addButton(button);
    }

    public void addTopSeparator() {
        if (outerPanel.getTopToolbar() != null) outerPanel.getTopToolbar().addSeparator();
    }

    public void addTopItem(ToolbarTextItem textItem) {
        if (outerPanel.getTopToolbar() != null) outerPanel.getTopToolbar().addItem(textItem);
    }

    public void addTopSpace() {
        if (outerPanel.getTopToolbar() != null) outerPanel.getTopToolbar().addFill();
    }

    public void disableTopToolbar(boolean disabled) {
        if (outerPanel.getTopToolbar() != null) outerPanel.getTopToolbar().setDisabled(disabled);
    }

    public void setTopToolbarVisible(boolean isVisible) {
        if (outerPanel.getTopToolbar() != null) outerPanel.getTopToolbar().setVisible(isVisible);
    }
}
