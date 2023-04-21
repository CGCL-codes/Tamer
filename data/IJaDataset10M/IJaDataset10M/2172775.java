package com.googlecode.projeto1.client.panels.manage;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.projeto1.client.LoginManager;
import com.googlecode.projeto1.client.PanelSwitcher;
import com.googlecode.projeto1.client.panels.manage.adminTab.AdminTab;
import com.googlecode.projeto1.client.panels.manage.createTab.CreateTab;
import com.googlecode.projeto1.client.panels.manage.demandTab.DemandsTab;
import com.googlecode.projeto1.client.panels.manage.editTab.EditTab;
import com.googlecode.projeto1.client.panels.manage.poi.PoiTab;
import com.googlecode.projeto1.client.panels.modality.ModalityPanel;
import com.gwtext.client.core.NameValuePair;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.ColumnLayout;

/**
 * @author Alcione Pinheiro
 * @author Diego Rodrigues
 * @author João Felipe
 * @version LARbc 1.0
 */
public class ManagePanel extends Panel {

    private AbsolutePanel managePanel;

    private Panel buttonsVoltarPanel;

    public ManagePanel() {
        super();
        buttonsVoltarPanel = new Panel();
        buttonsVoltarPanel.setLayout(new ColumnLayout());
        managePanel = new AbsolutePanel();
        managePanel.add(buttonsVoltarPanel, 827, 40);
        {
            TabPanel tabPanel = new TabPanel();
            tabPanel.setAnimationEnabled(true);
            managePanel.add(tabPanel, 5, 5);
            tabPanel.setSize("600px", "400px");
            CreateTab createTab = new CreateTab();
            tabPanel.add(createTab, "Criar", false);
            createTab.setSize("620px", "300px");
            EditTab editTab = new EditTab();
            tabPanel.add(editTab, "Editar", false);
            editTab.setSize("620px", "500px");
            DemandsTab demandsTab = new DemandsTab();
            tabPanel.add(demandsTab, "Analisar Demandas", false);
            demandsTab.setSize("620px", "500px");
            PoiTab poiTab = new PoiTab();
            tabPanel.add(poiTab, "Pontos de Interesse", false);
            poiTab.setSize("620px", "300px");
            AdminTab adminsMainPanel = new AdminTab();
            tabPanel.add(adminsMainPanel, "Administradores", false);
            adminsMainPanel.setSize("620px", "400px");
            Button logoutButton = getLogoutButton();
            tabPanel.add(logoutButton, "Sair", false);
            tabPanel.selectTab(0);
        }
        this.add(managePanel);
        this.setFrame(true);
    }

    private Button getLogoutButton() {
        Button button = new Button("Sair");
        button.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                MessageBox.show(new MessageBoxConfig() {

                    {
                        setTitle("Logout");
                        setMsg("Você deseja realmente realizar logout?");
                        setIconCls(MessageBox.QUESTION);
                        setButtons(MessageBox.YESNO);
                        setButtons(new NameValuePair[] { new NameValuePair("yes", "Sim"), new NameValuePair("no", "Não") });
                        setCallback(new MessageBox.PromptCallback() {

                            public void execute(String btnID, String text) {
                                if (btnID.equals("yes")) {
                                    LoginManager.setLogged(null);
                                    PanelSwitcher.switchPanel(new ModalityPanel());
                                }
                            }
                        });
                    }
                });
            }
        });
        return button;
    }
}
