package net.sf.refactorit.cli.actions;

import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.metrics.MetricsAction;
import net.sf.refactorit.metrics.MetricsModel;
import net.sf.refactorit.ui.options.profile.Profile;
import net.sf.refactorit.ui.options.profile.ProfileUtil;
import net.sf.refactorit.ui.treetable.BinTreeTableModel;

public class MetricsModelBuilder implements ModelBuilder {

    private String profileFilename;

    public MetricsModelBuilder() {
        this("");
    }

    public MetricsModelBuilder(String profileFilename) {
        this.profileFilename = profileFilename;
    }

    public BinTreeTableModel populateModel(Project p) {
        MetricsModel model = new MetricsModel(MetricsAction.getDefaultColumnNames(), MetricsAction.getDefaultActionIndexes());
        setProfile(model);
        model.populate(p, p);
        return model;
    }

    private void setProfile(MetricsModel result) {
        MetricsModel.State state = new MetricsModel.State();
        state.setProfile(getProfile(profileFilename));
        result.setState(state);
    }

    private Profile getProfile(String profileFilename) {
        if ("".equals(profileFilename)) {
            return Profile.createDefaultMetrics();
        } else {
            return ProfileUtil.createProfile(profileFilename);
        }
    }

    public boolean supportsProfiles() {
        return true;
    }
}
