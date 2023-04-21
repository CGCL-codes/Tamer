package org.pocui.swing.example.composites.stammdaten.anzeigen;

import javax.swing.JTabbedPane;
import org.pocui.core.actions.EmptyActionConfiguration;
import org.pocui.core.composites.CompositeInitializationException;
import org.pocui.core.resources.EmptyResourceConfiguration;
import org.pocui.swing.composites.AbsComposite;
import org.pocui.swing.example.composites.stammdaten.anzeigen.anschrift.VisualisiereAnschriftComposite;
import org.pocui.swing.example.composites.stammdaten.anzeigen.anschrift.VisualisiereAnschriftSelectionInOut;
import org.pocui.swing.example.composites.stammdaten.anzeigen.bemerkung.VisualisiereBemerkungComposite;
import org.pocui.swing.example.composites.stammdaten.anzeigen.bemerkung.VisualisiereBemerkungSelectionInOut;
import org.pocui.swing.example.composites.stammdaten.anzeigen.vertragsdaten.VisualisiereVertragsdatenComposite;
import org.pocui.swing.example.composites.stammdaten.anzeigen.vertragsdaten.VisualisiereVertragsdatenSelectionInOut;

/**
 * Composite to visualize Kunden data
 * @author Kai Benjamin Joneleit
 *
 */
public class VisualisiereKundendatensatzComposite extends AbsComposite<EmptyActionConfiguration, EmptyResourceConfiguration, VisualisiereKundendatensatzSelectionInOut> {

    private static final long serialVersionUID = 6972809148143397753L;

    private VisualisiereAnschriftComposite kundenAdresseComposite;

    private VisualisiereVertragsdatenComposite vertragsdatenComposite;

    private VisualisiereBemerkungComposite zusatzinfosComposite;

    public VisualisiereKundendatensatzComposite() throws CompositeInitializationException {
        super(EmptyActionConfiguration.getInstance(), EmptyResourceConfiguration.getInstance());
    }

    @Override
    protected void initGUI() throws CompositeInitializationException {
        JTabbedPane jTabbedPaneKundendaten = new javax.swing.JTabbedPane();
        kundenAdresseComposite = new VisualisiereAnschriftComposite();
        vertragsdatenComposite = new VisualisiereVertragsdatenComposite();
        zusatzinfosComposite = new VisualisiereBemerkungComposite();
        setBorder(javax.swing.BorderFactory.createTitledBorder("Kundendatensatz visualisieren"));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        jTabbedPaneKundendaten.addTab("Kundenadresse", kundenAdresseComposite);
        jTabbedPaneKundendaten.addTab("Vertragsdaten", vertragsdatenComposite);
        jTabbedPaneKundendaten.addTab("Infos", zusatzinfosComposite);
        add(jTabbedPaneKundendaten);
    }

    @Override
    protected void initListener() throws CompositeInitializationException {
    }

    @Override
    public void doSetSelection(VisualisiereKundendatensatzSelectionInOut oldSelection, VisualisiereKundendatensatzSelectionInOut selection) {
        VisualisiereVertragsdatenSelectionInOut vertragsdatenAnzeigenSelection = new VisualisiereVertragsdatenSelectionInOut();
        vertragsdatenAnzeigenSelection.setSelektiertesProdukt(selection.getSelektiertesProdukt());
        vertragsdatenAnzeigenSelection.setVertragsbeginn(selection.getVertragsbeginn());
        vertragsdatenAnzeigenSelection.setVertragsende(selection.getVertragsende());
        vertragsdatenAnzeigenSelection.setProdukte(selection.getProdukte());
        vertragsdatenComposite.setSelection(vertragsdatenAnzeigenSelection);
        VisualisiereAnschriftSelectionInOut kundenadresseAnzeigenSelection = new VisualisiereAnschriftSelectionInOut();
        kundenadresseAnzeigenSelection.setBundeslaender(selection.getBundeslaender());
        kundenadresseAnzeigenSelection.setHausnummer(selection.getHausnummer());
        kundenadresseAnzeigenSelection.setPostleitzahl(selection.getPostleitzahl());
        kundenadresseAnzeigenSelection.setSelektiertesBundesland(selection.getSelektiertesBundesland());
        kundenadresseAnzeigenSelection.setStrasse(selection.getStrasse());
        kundenadresseAnzeigenSelection.setWohnort(selection.getWohnort());
        kundenAdresseComposite.setSelection(kundenadresseAnzeigenSelection);
        VisualisiereBemerkungSelectionInOut zusatzinfosSelection = new VisualisiereBemerkungSelectionInOut();
        zusatzinfosSelection.setInfos(selection.getZusatzInfos());
        zusatzinfosComposite.setSelection(zusatzinfosSelection);
    }

    @Override
    protected void addAllListener() {
    }

    @Override
    protected void doCleanUp() {
    }

    @Override
    protected void removeAllListener() {
    }
}
