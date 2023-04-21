package org.one.stone.soup.xapp.factories;

import java.awt.Point;
import java.awt.event.ActionListener;
import org.one.stone.soup.xapp.XApplication;
import org.one.stone.soup.xapp.XappApplicationFrame;
import org.one.stone.soup.xapp.XappComponentStore;
import org.one.stone.soup.xapp.components.XappComponent;
import org.one.stone.soup.xapp.components.XappConsole;
import org.one.stone.soup.xapp.components.XappField;
import org.one.stone.soup.xapp.components.XappImage;
import org.one.stone.soup.xapp.components.XappPopupMenu;
import org.one.stone.soup.xapp.components.compound.XappSplashPage;
import org.one.stone.soup.xapp.components.form.XForm;
import org.one.stone.soup.xapp.components.tree.XTree;
import org.one.stone.soup.xapp.containers.XappContainer;
import org.one.stone.soup.xapp.containers.XappFrame;
import org.one.stone.soup.xapp.containers.XappMenuBar;
import org.one.stone.soup.xapp.containers.XappPopup;
import org.one.stone.soup.xapp.controller.xml.XmlSchemaViewController;
import org.one.stone.soup.xapp.filebrowser.XappFileBrowser;
import org.one.stone.soup.xapp.resource.manager.XuiResourceManager;
import org.one.stone.soup.xml.XmlElement;

public interface XappComponentFactory {

    public abstract XappDragAndDropFacotry getDragAndDropFactory();

    public abstract XappApplicationFrame buildApplicationFrame();

    public abstract XappApplicationFrame buildSubApplicationFrame();

    public void closeApplication(XApplication app);

    public abstract XappFrame buildXappFrame(String title);

    public abstract XappComponent buildAnchor(XappContainer container, XmlElement xData, String parentName, XappComponentStore componentStore);

    public abstract XappComponent buildXappComponent(XappContainer container, Object data, String parentName, XappComponentStore componentStore);

    public abstract XappComponent buildHeading(XappContainer container, XmlElement xData, String parentName, XappComponentStore componentStore);

    public abstract XappComponent buildInput(XappContainer container, XmlElement xData, String parentName, XappComponentStore componentStore);

    public abstract XappComponent buildXappComponent(XappContainer container, XmlElement xData);

    public abstract XappComponent buildSeparator(XappContainer container, XmlElement xData);

    public abstract XappComponent buildLabel(XappContainer container, XmlElement xData, String parentName, XappComponentStore componentStore);

    public abstract XappComponent buildProgress(XappContainer container, XmlElement xData, String parentName, XappComponentStore componentStore);

    public abstract XappComponent buildSelect(XappContainer container, XmlElement xData, String parentName, XappComponentStore componentStore);

    public abstract XappComponent buildSlider(XappContainer container, XmlElement xData, String parentName, XappComponentStore componentStore);

    public XForm buildForm(XmlElement xActionsForm);

    public XForm buildForm(XmlElement xActionsForm, XappComponentStore store);

    public XappFrame buildFrame();

    public abstract XappComponent buildSpreadsheet(XmlElement xData, XappComponentStore componentStore);

    public abstract XappComponent buildTextArea(XappComponent parent, XappContainer container, XmlElement xData, String parentName, XappComponentStore componentStore);

    public abstract XappImage getImage(String imageName);

    public abstract void setResourceManager(XuiResourceManager manager);

    public abstract boolean setBackground(XappComponent component, XmlElement table);

    public abstract boolean setColor(XappComponent component, XmlElement table);

    public abstract void setTip(XappComponent component, XmlElement xData);

    public XappMenuBar buildXappMenuBar(XmlElement xMenuBar, XappComponentStore store);

    public XappPopup buildXappPopup(XmlElement xPopup);

    public abstract void buildMenu(XmlElement xMenu, ActionListener listener, XappField owner, XappComponentStore componentStore);

    public abstract void buildMenuItem(XmlElement xItem, ActionListener listener, XappField owner, boolean isPopup, XappComponentStore componentStore);

    public abstract XappComponent buildContainer(XmlElement xui, XappComponentStore componentStore);

    public abstract void buildContainers(XmlElement xui, XappComponentStore componentStore);

    public abstract XappFileBrowser buildFileBrowser(String title, String fileTypeName, String fileExtension);

    public abstract XappConsole buildConsole();

    public abstract XappSplashPage buildSplashPage(XmlElement table);

    public abstract XTree buildXTree(XmlElement modelRoot, XmlSchemaViewController viewController);

    public abstract Point getComponentPosition(XappComponent component);

    public abstract XappComponent getComponentAtPosition(Point position);
}
