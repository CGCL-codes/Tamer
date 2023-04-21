package com.alturatec.businesstrip.mvc.verbindung;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jrcaf.mvc.annotations.Bind;
import org.jrcaf.mvc.validator.standard.NotEmptyValidator;

public class VerbindungBearbeitenComposite extends Composite {

    public VerbindungBearbeitenComposite(Composite aParent) {
        super(aParent, SWT.NONE);
        initialize();
    }

    @Bind("nls://linkName#text")
    private Label linkNameLabel;

    @Bind(value = "link.name#text", validators = { NotEmptyValidator.class }, updateEvent = SWT.Modify)
    private Text linkNameText = null;

    @Bind("nls://startOrt#text")
    private Label startSiteLabel;

    @Bind("link.startSite#selection, datasource.sites#items$name")
    private Combo startSiteCombo = null;

    @Bind("nls://suchen#text, map_call_action://startOrtSuchen")
    private Button startSiteSuchen = null;

    @Bind("nls://bearbeiten#text, call_action://startOrtBearbeiten$link.startSite")
    private Button startSiteBearbeiten = null;

    @Bind("nls://zielOrt#text")
    private Label destinationSiteLabel;

    @Bind("link.destinationSite#selection, datasource.sites#items$name")
    private Combo destinationSiteCombo = null;

    @Bind("nls://suchen#text, call_action://zielOrtSuchen")
    private Button destinationSiteSuchen = null;

    @Bind("nls://bearbeiten#text, call_action://zielOrtBearbeiten$link.destinationSite")
    private Button destinationSiteBearbeiten = null;

    @Bind("nls://CarLink#text, pkw#selection")
    private Button pkwRadioButton = null;

    @Bind("nls://PublicTransportationLink#text, oevm#selection")
    private Button oevmRadioButton = null;

    @Bind("link.einBahn#selection, nls://einBahn#text")
    private Button einbahnCheckBox = null;

    @Bind("nls://ortAnlegen#text, call_action://ortAnlegen")
    private Button ortAnlegenButton = null;

    @Bind(value = "subView://link#linkPart$link", param = "\"type specific link data\"")
    private Composite linkSubViewComposite;

    private void initialize() {
        setLayout(new GridLayout(4, false));
        linkNameLabel = new Label(this, SWT.NONE);
        linkNameText = new Text(this, SWT.BORDER);
        linkNameText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1));
        startSiteLabel = new Label(this, SWT.NONE);
        startSiteCombo = new Combo(this, SWT.READ_ONLY);
        startSiteCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 1));
        startSiteSuchen = new Button(this, SWT.NONE);
        startSiteSuchen.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 1, 1));
        startSiteBearbeiten = new Button(this, SWT.NONE);
        startSiteBearbeiten.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 1, 1));
        destinationSiteLabel = new Label(this, SWT.NONE);
        destinationSiteCombo = new Combo(this, SWT.READ_ONLY);
        destinationSiteCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 1));
        destinationSiteSuchen = new Button(this, SWT.NONE);
        destinationSiteSuchen.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 1, 1));
        destinationSiteBearbeiten = new Button(this, SWT.NONE);
        destinationSiteBearbeiten.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 1, 1));
        Composite radioComposite = new Composite(this, SWT.NONE);
        radioComposite.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
        RowLayout rowLayout = new RowLayout();
        rowLayout.spacing = 7;
        radioComposite.setLayout(rowLayout);
        pkwRadioButton = new Button(radioComposite, SWT.RADIO);
        oevmRadioButton = new Button(radioComposite, SWT.RADIO);
        einbahnCheckBox = new Button(this, SWT.CHECK);
        ortAnlegenButton = new Button(this, SWT.NONE);
        linkSubViewComposite = new Composite(this, SWT.NONE);
        linkSubViewComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1));
    }
}
