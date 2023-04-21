package org.jdna.bmt.web.client.ui.browser;

import org.jdna.bmt.web.client.ui.input.InputBuilder;
import org.jdna.bmt.web.client.ui.layout.Simple2ColFormLayoutPanel;
import org.jdna.bmt.web.client.ui.util.DataDialog;
import org.jdna.bmt.web.client.ui.util.DialogHandler;
import org.jdna.bmt.web.client.ui.util.HelpLabel;
import org.jdna.bmt.web.client.ui.util.TitlePanel;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ScanOptionsPanel extends DataDialog<PersistenceOptionsUI> implements ChangeHandler, ClickHandler {

    protected Simple2ColFormLayoutPanel propPanel = null;

    private CheckBox subdirs;

    private CheckBox missing;

    private CheckBox fanart;

    private CheckBox metadata;

    private CheckBox importAsTV;

    public ScanOptionsPanel(DialogHandler<PersistenceOptionsUI> handler) {
        this(new PersistenceOptionsUI(), handler);
    }

    public ScanOptionsPanel(String title, PersistenceOptionsUI options, DialogHandler<PersistenceOptionsUI> handler) {
        super(title, options, handler);
        initPanels();
    }

    public ScanOptionsPanel(PersistenceOptionsUI options, DialogHandler<PersistenceOptionsUI> handler) {
        this("Scan Options", options, handler);
    }

    @Override
    protected Widget getBodyWidget() {
        VerticalPanel panel = new VerticalPanel();
        panel.setWidth("100%");
        final PersistenceOptionsUI options = getData();
        TitlePanel dp = null;
        dp = new TitlePanel(new HelpLabel("Metadata/Fanart Options", "Metadata/Fanart Scan Options"));
        propPanel = new Simple2ColFormLayoutPanel();
        propPanel.getFlexTable().addClickHandler(this);
        dp.setWidth("100%");
        propPanel.setWidth("100%");
        subdirs = InputBuilder.checkbox().bind(options.getIncludeSubDirs()).widget();
        fanart = InputBuilder.checkbox().bind(options.getUpdateFanart()).widget();
        metadata = InputBuilder.checkbox().bind(options.getUpdateMetadata()).widget();
        missing = InputBuilder.checkbox().bind(options.getScanOnlyMissingMetadata()).widget();
        importAsTV = InputBuilder.checkbox().bind(options.getImportTVAsRecordings()).widget();
        metadata.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                missing.setValue(event.getValue());
                missing.setEnabled(event.getValue());
                importAsTV.setEnabled(event.getValue());
            }
        });
        propPanel.add("Scan All Sub Folders", subdirs);
        propPanel.add("Update Fanart", fanart);
        propPanel.add("Update Metadata", metadata);
        propPanel.add("Only update items that have not been updated previously", missing);
        propPanel.add("Import TV Files as Recordings", importAsTV);
        dp.setContent(propPanel);
        panel.add(dp);
        return panel;
    }

    @Override
    protected void updateButtonPanel(HorizontalPanel buttonPan) {
        okButton.setText("Scan");
    }

    @Override
    protected Widget getHeaderWidget() {
        return new Label(getData().getScanPath().get().getPath());
    }

    public void onChange(ChangeEvent event) {
    }

    public void onClick(ClickEvent event) {
    }
}
