package com.genia.toolbox.portlet.editor.gui.view.settings;

import java.awt.CardLayout;
import com.genia.toolbox.basics.editor.gui.panel.AbstractSettings;
import com.genia.toolbox.basics.editor.gui.view.settings.panel.EmptySettingsPanel;
import com.genia.toolbox.portlet.editor.gui.PortletEditorGUI;
import com.genia.toolbox.portlet.editor.gui.view.settings.panel.DocumentSettingsPanel;
import com.genia.toolbox.portlet.editor.gui.view.settings.panel.portlet.ContainerPortletSettingsPanel;
import com.genia.toolbox.portlet.editor.gui.view.settings.panel.portlet.DispatcherPortletSettingsPanel;
import com.genia.toolbox.portlet.editor.gui.view.settings.panel.portlet.HolePortletSettingsPanel;
import com.genia.toolbox.portlet.editor.gui.view.settings.panel.portlet.LinkedPortletSettingsPanel;
import com.genia.toolbox.portlet.editor.gui.view.settings.panel.portlet.SimplePortletSettingsPanel;
import com.genia.toolbox.portlet.editor.gui.view.settings.panel.portlet.TemplatePortletSettingsPanel;
import com.genia.toolbox.portlet.editor.model.document.impl.DocumentModel;
import com.genia.toolbox.portlet.editor.model.portlet.PortletType;
import com.genia.toolbox.portlet.editor.model.portlet.impl.PortletModel;

/**
 * Main portlet settings panel.
 */
@SuppressWarnings("serial")
public class MainSettingsPanel extends AbstractSettings<PortletEditorGUI, DocumentModel, PortletModel> {

    /**
   * The displayed settings name.
   */
    private String displayedSettings = null;

    /**
   * The empty panel name.
   */
    private static final String PANEL_EMPTY = "Empty";

    /**
   * The default panel name.
   */
    private static final String PANEL_DEFAULT = "Default";

    /**
   * The simple panel name.
   */
    private static final String PANEL_SIMPLE = "Simple";

    /**
   * The linked panel name.
   */
    private static final String PANEL_LINKED = "Linked";

    /**
   * The dispatcher panel name.
   */
    private static final String PANEL_DISPATCHER = "Dispatcher";

    /**
   * The container panel name.
   */
    private static final String PANEL_CONTAINER = "Container";

    /**
   * The hole panel name.
   */
    private static final String PANEL_HOLE = "Hole";

    /**
   * The template panel name.
   */
    private static final String PANEL_TEMPLATE = "Template";

    /**
   * The empty settings panel.
   */
    private EmptySettingsPanel emptySettingsPanel = null;

    /**
   * The hole settings panel.
   */
    private HolePortletSettingsPanel holeSettingsPanel = null;

    /**
   * The document settings panel.
   */
    private DocumentSettingsPanel documentSettingsPanel = null;

    /**
   * The dispatcher settings panel.
   */
    private DispatcherPortletSettingsPanel dispatcherSettingsPanel = null;

    /**
   * The linked settings panel.
   */
    private LinkedPortletSettingsPanel linkedSettingsPanel = null;

    /**
   * The simple settings panel.
   */
    private SimplePortletSettingsPanel simpleSettingsPanel = null;

    /**
   * The container settings panel.
   */
    private ContainerPortletSettingsPanel containerSettingsPanel = null;

    /**
   * The template settings panel.
   */
    private TemplatePortletSettingsPanel templateSettingsPanel = null;

    /**
   * The settings layout.
   */
    private CardLayout settingsLayout = null;

    /**
   * Constructor.
   * 
   * @param portletEditorGUI
   *          the application portletEditorGUI.
   */
    public MainSettingsPanel(PortletEditorGUI portletEditorGUI) {
        super(portletEditorGUI);
        this.settingsLayout = new CardLayout();
        this.setLayout(this.settingsLayout);
        this.emptySettingsPanel = new EmptySettingsPanel();
        this.add(this.emptySettingsPanel, PANEL_EMPTY);
        this.documentSettingsPanel = new DocumentSettingsPanel(this.getGui());
        this.add(this.documentSettingsPanel, PANEL_DEFAULT);
        this.dispatcherSettingsPanel = new DispatcherPortletSettingsPanel(this.getGui());
        this.dispatcherSettingsPanel.initialise();
        this.add(this.dispatcherSettingsPanel, PANEL_DISPATCHER);
        this.linkedSettingsPanel = new LinkedPortletSettingsPanel(this.getGui());
        this.linkedSettingsPanel.initialise();
        this.add(this.linkedSettingsPanel, PANEL_LINKED);
        this.simpleSettingsPanel = new SimplePortletSettingsPanel(this.getGui());
        this.simpleSettingsPanel.initialise();
        this.add(this.simpleSettingsPanel, PANEL_SIMPLE);
        this.containerSettingsPanel = new ContainerPortletSettingsPanel(this.getGui());
        this.containerSettingsPanel.initialise();
        this.add(this.containerSettingsPanel, PANEL_CONTAINER);
        this.holeSettingsPanel = new HolePortletSettingsPanel(this.getGui());
        this.holeSettingsPanel.initialise();
        this.add(this.holeSettingsPanel, PANEL_HOLE);
        this.templateSettingsPanel = new TemplatePortletSettingsPanel(this.getGui());
        this.templateSettingsPanel.initialise();
        this.add(this.templateSettingsPanel, PANEL_TEMPLATE);
        this.settingsLayout.show(this, PANEL_EMPTY);
        this.displayedSettings = null;
    }

    /**
   * Set the displayed document.
   * 
   * @param document
   *          the document.
   */
    @Override
    public void setDocument(DocumentModel document) {
        if (this.element != null) {
            this.getGui().notifyElementSettingsChanged(this.getElement());
        }
        if (this.document != null) {
            this.getGui().notifyDocumentSettingsChanged(this.getDocument());
        }
        this.element = null;
        this.document = document;
        if (document != null) {
            this.documentSettingsPanel.setDocument(document);
            this.settingsLayout.show(this, PANEL_DEFAULT);
            this.displayedSettings = PANEL_DEFAULT;
        } else {
            this.settingsLayout.show(this, PANEL_EMPTY);
            this.displayedSettings = PANEL_EMPTY;
        }
    }

    /**
   * Set the displayed portlet.
   * 
   * @param portlet
   *          the portlet.
   */
    @Override
    public void setElement(PortletModel portlet) {
        if (this.element != null) {
            this.getGui().notifyElementSettingsChanged(this.getElement());
        }
        if (this.document != null) {
            this.getGui().notifyDocumentSettingsChanged(this.getDocument());
        }
        this.element = portlet;
        this.document = null;
        if (this.element != null) {
            if (PortletType.Dispatcher.equals(this.element.getType())) {
                this.dispatcherSettingsPanel.setPortlet(this.element);
                this.settingsLayout.show(this, PANEL_DISPATCHER);
                this.displayedSettings = PANEL_DISPATCHER;
            } else if (PortletType.Linked.equals(this.element.getType())) {
                this.linkedSettingsPanel.setPortlet(this.element);
                this.settingsLayout.show(this, PANEL_LINKED);
                this.displayedSettings = PANEL_LINKED;
            } else if (PortletType.Container.equals(this.element.getType())) {
                this.containerSettingsPanel.setPortlet(this.element);
                this.settingsLayout.show(this, PANEL_CONTAINER);
                this.displayedSettings = PANEL_CONTAINER;
            } else if (PortletType.Simple.equals(this.element.getType())) {
                this.simpleSettingsPanel.setPortlet(this.element);
                this.settingsLayout.show(this, PANEL_SIMPLE);
                this.displayedSettings = PANEL_SIMPLE;
            } else if (PortletType.Hole.equals(this.element.getType())) {
                this.holeSettingsPanel.setPortlet(this.element);
                this.settingsLayout.show(this, PANEL_HOLE);
                this.displayedSettings = PANEL_HOLE;
            } else if (PortletType.Template.equals(this.element.getType())) {
                this.templateSettingsPanel.setPortlet(this.element);
                this.settingsLayout.show(this, PANEL_TEMPLATE);
                this.displayedSettings = PANEL_TEMPLATE;
            }
        } else {
            this.settingsLayout.show(this, PANEL_EMPTY);
            this.displayedSettings = PANEL_EMPTY;
        }
    }

    /**
   * Get the displayed document.
   * 
   * @return the displayed document.
   */
    @Override
    public DocumentModel getDocument() {
        return this.documentSettingsPanel.getDocument();
    }

    /**
   * Get the displayed portlet.
   * 
   * @return the displayed portlet.
   */
    @Override
    public PortletModel getElement() {
        PortletModel portlet = null;
        if (this.displayedSettings.equals(PANEL_CONTAINER)) {
            portlet = this.containerSettingsPanel.getPortlet();
        } else if (this.displayedSettings.equals(PANEL_LINKED)) {
            portlet = this.linkedSettingsPanel.getPortlet();
        } else if (this.displayedSettings.equals(PANEL_SIMPLE)) {
            portlet = this.simpleSettingsPanel.getPortlet();
        } else if (this.displayedSettings.equals(PANEL_DISPATCHER)) {
            portlet = this.dispatcherSettingsPanel.getPortlet();
        } else if (this.displayedSettings.equals(PANEL_HOLE)) {
            portlet = this.holeSettingsPanel.getPortlet();
        } else if (this.displayedSettings.equals(PANEL_TEMPLATE)) {
            portlet = this.templateSettingsPanel.getPortlet();
        }
        return portlet;
    }
}
