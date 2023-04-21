package com.cromoteca.meshcms.client.ui.controlpanel;

import com.cromoteca.meshcms.client.core.MeshCMS;
import com.cromoteca.meshcms.client.rpc.ServerCall;
import com.cromoteca.meshcms.client.server.ServerConfiguration;
import com.cromoteca.meshcms.client.toolbox.Useless;
import com.cromoteca.meshcms.client.ui.fields.BooleanField;
import com.cromoteca.meshcms.client.ui.fields.BooleanField.BooleanConnector;
import com.cromoteca.meshcms.client.ui.fields.Field;
import com.cromoteca.meshcms.client.ui.fields.FieldConnector;
import com.cromoteca.meshcms.client.ui.fields.FieldList;
import com.cromoteca.meshcms.client.ui.fields.IntegerField;
import com.cromoteca.meshcms.client.ui.fields.IntegerField.IntegerConnector;
import com.cromoteca.meshcms.client.ui.fields.StringArrayConnector;
import com.cromoteca.meshcms.client.ui.fields.TextField;
import com.cromoteca.meshcms.client.ui.filemanager.FileManager;
import com.cromoteca.meshcms.client.ui.widgets.ButtonBar;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class ServerConfigEditor extends FlowPanel implements FileManagerPanel {

    private FileManager fileManager;

    public ServerConfigEditor() {
        buildLayout();
    }

    private void buildLayout() {
        new ServerCall<ServerConfiguration>() {

            @Override
            public void callServer() {
                MeshCMS.SERVER.getServerConfiguration(getCallback());
            }

            @Override
            public void onResult(ServerConfiguration result) {
                buildLayout(result);
            }
        }.run();
    }

    private void buildLayout(final ServerConfiguration configuration) {
        clear();
        final FieldList fields = new FieldList();
        CaptionPanel interfaceFieldset = new CaptionPanel(MeshCMS.CONSTANTS.configInterface());
        add(interfaceFieldset);
        FlowPanel interfacePanel = new FlowPanel();
        interfaceFieldset.add(interfacePanel);
        Field thumbField = new BooleanField(MeshCMS.CONSTANTS.configThumbnailQuality());
        interfacePanel.add(thumbField);
        thumbField.setConnector(new BooleanConnector() {

            @Override
            public void storeValue(boolean value) {
                configuration.setHighQualityThumbnails(value);
            }

            @Override
            public boolean loadValue() {
                return configuration.isHighQualityThumbnails();
            }
        });
        fields.add(thumbField);
        CaptionPanel systemFieldset = new CaptionPanel(MeshCMS.CONSTANTS.configSystem());
        add(systemFieldset);
        FlowPanel systemPanel = new FlowPanel();
        systemFieldset.add(systemPanel);
        Field backupLifeField = new IntegerField(true, MeshCMS.CONSTANTS.configBackup(), IntegerField.POSITIVE_ONLY);
        systemPanel.add(backupLifeField);
        backupLifeField.setConnector(new IntegerConnector() {

            @Override
            public void storeValue(int value) {
                configuration.setBackupLife(value);
            }

            @Override
            public int loadValue() {
                return configuration.getBackupLife();
            }
        });
        fields.add(backupLifeField);
        Field statsLengthField = new IntegerField(true, MeshCMS.CONSTANTS.configHits(), IntegerField.POSITIVE_ONLY);
        systemPanel.add(statsLengthField);
        statsLengthField.setConnector(new IntegerConnector() {

            @Override
            public void storeValue(int value) {
                configuration.setStatsLength(value);
            }

            @Override
            public int loadValue() {
                return configuration.getStatsLength();
            }
        });
        fields.add(statsLengthField);
        Field visualExtsField = new TextField(false, MeshCMS.CONSTANTS.configVisual()).bigger();
        systemPanel.add(visualExtsField);
        visualExtsField.setConnector(new StringArrayConnector() {

            @Override
            public void storeValue(String[] value) {
                configuration.setVisualExtensions(value);
            }

            @Override
            public String[] loadValue() {
                return configuration.getVisualExtensions();
            }
        });
        fields.add(visualExtsField);
        CaptionPanel mailFieldset = new CaptionPanel(MeshCMS.CONSTANTS.configMailParams());
        add(mailFieldset);
        FlowPanel mailPanel = new FlowPanel();
        mailFieldset.add(mailPanel);
        Field mailServerField = new TextField(false, MeshCMS.CONSTANTS.configMail()).bigger();
        mailPanel.add(mailServerField);
        mailServerField.setConnector(new FieldConnector() {

            public void storeValue(String value) {
                configuration.setMailServer(value);
            }

            public String retrieveValue() {
                return configuration.getMailServer();
            }
        });
        fields.add(mailServerField);
        Field mailUsernameField = new TextField(false, MeshCMS.CONSTANTS.configSmtpUsername()).bigger();
        mailPanel.add(mailUsernameField);
        mailUsernameField.setConnector(new FieldConnector() {

            public void storeValue(String value) {
                configuration.setSmtpUsername(value);
            }

            public String retrieveValue() {
                return configuration.getSmtpUsername();
            }
        });
        fields.add(mailUsernameField);
        Field mailPasswordField = new TextField(false, MeshCMS.CONSTANTS.configSmtpPassword()).bigger();
        mailPanel.add(mailPasswordField);
        mailPasswordField.setConnector(new FieldConnector() {

            public void storeValue(String value) {
                configuration.setSmtpUsername(value);
            }

            public String retrieveValue() {
                return configuration.getSmtpUsername();
            }
        });
        fields.add(mailPasswordField);
        ButtonBar buttonBar = new ButtonBar();
        add(buttonBar);
        Button updateButton = new Button(MeshCMS.CONSTANTS.genericUpdate(), new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (fields.verifyAll()) {
                    fields.storeAll();
                    new ServerCall<Useless>() {

                        @Override
                        public void callServer() {
                            MeshCMS.SERVER.saveServerConfiguration(configuration, getCallback());
                        }

                        @Override
                        public void onResult(Useless result) {
                            fileManager.refreshView();
                        }
                    }.run();
                }
            }
        });
        buttonBar.add(updateButton);
        fields.addActionButton(updateButton);
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public Widget asWidget() {
        return this;
    }

    public String getTabCaption() {
        return MeshCMS.CONSTANTS.homeConfigure();
    }
}
