package com.scythebill.birdlist.ui.panels;

import java.awt.Dimension;
import com.google.inject.Inject;
import com.scythebill.birdlist.model.sighting.ReportSet;
import com.scythebill.birdlist.model.sighting.edits.SingleLocationEdit;
import com.scythebill.birdlist.ui.guice.Wizard.WizardStartScoped;

@WizardStartScoped
public class SingleLocationEditPanel extends WizardPanelBase {

    private final SingleLocationEdit edit;

    private final ReportSet reportSet;

    @Inject
    public SingleLocationEditPanel(NavigableFrame navigableFrame, SingleLocationWhenAndWherePanel whenAndWhere, SingleLocationSpeciesListPanel speciesList, SingleLocationEdit edit, ReportSet reportSet) {
        super(navigableFrame);
        this.edit = edit;
        this.reportSet = reportSet;
        addPage(whenAndWhere);
        addPage(speciesList);
        setPreferredSize(new Dimension(800, 640));
    }

    @Override
    protected void save() {
        edit.apply(reportSet);
    }

    @Override
    protected String getWizardTitle() {
        return "Enter Sightings";
    }
}
