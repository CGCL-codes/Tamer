package com.gwesm.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import com.gwesm.presenters.CampaignStatsPresenter;

public class CampaignStatsView extends SWTView {

    Label campNameLabel;

    Label statsLabel;

    CampaignStatsPresenter presenter;

    public CampaignStatsView(final Composite parent, final CampaignStatsPresenter presenter) {
        super(parent, presenter);
        this.presenter = presenter;
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        setLayout(layout);
        this.campNameLabel = new Label(this, SWT.NONE);
        GridData campNameLayoutData = new GridData();
        campNameLabel.setLayoutData(campNameLayoutData);
        this.statsLabel = new Label(this, SWT.NONE);
        GridData statsLabelLayoutData = new GridData();
        statsLabelLayoutData.horizontalAlignment = SWT.RIGHT;
        statsLabelLayoutData.grabExcessHorizontalSpace = true;
        statsLabel.setLayoutData(statsLabelLayoutData);
        refresh();
    }

    public void refresh() {
        this.campNameLabel.setText(this.presenter.getCampaignName());
        this.statsLabel.setText("" + this.presenter.getOwnedSkillCount() + " / " + this.presenter.getAvailableSkillCount());
    }
}
