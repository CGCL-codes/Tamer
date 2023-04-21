package applicationWorkbench.uielements;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import persister.data.IndexCard;
import persister.factory.Settings;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;

public class StoryCardTab {

    private static int select = 1;

    private Button btnDescription;

    private Button btnEstimateActual;

    private Button btnEstimateBestCase;

    private Button btnEstimateRemaining;

    private Button btnEstimateWorstCase;

    private Button btnLargeDis;

    private Button btnMediumDis;

    private Button btnSmallDis;

    private Button btnStateAcc;

    private Button btnStateComp;

    private Button btnStateDef;

    private Button btnStateProg;

    private ProjectModel projectModel;

    private TabItem tab;

    public TabItem create(TabFolder parent, ProjectModel pm) {
        setTab(new TabItem(parent, SWT.NONE));
        setProjectModel(pm);
        getTab().setText("StoryCard");
        Composite storyCardTabComposite = new Composite(parent, SWT.NONE);
        GridLayout layoutItem1 = new GridLayout(1, false);
        storyCardTabComposite.setLayout(layoutItem1);
        Group groupStoryCardState = new Group(storyCardTabComposite, SWT.NONE);
        groupStoryCardState.setText("Change State");
        groupStoryCardState.setLayout(new GridLayout(6, false));
        GridData gridDataStoryCardState = new GridData();
        gridDataStoryCardState.grabExcessHorizontalSpace = true;
        gridDataStoryCardState.horizontalAlignment = GridData.FILL;
        gridDataStoryCardState.verticalAlignment = GridData.FILL;
        groupStoryCardState.setLayoutData(gridDataStoryCardState);
        Label state = new Label(groupStoryCardState, SWT.NONE);
        state.setText("&Status:");
        state.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        Composite compositeStateDis = new Composite(groupStoryCardState, SWT.NONE);
        GridLayout layoutStateDis = new GridLayout(3, false);
        compositeStateDis.setLayout(layoutStateDis);
        btnStateDef = new Button(groupStoryCardState, SWT.RADIO);
        btnStateDef.setText("Defined");
        btnStateDef.setSelection((Settings.getState()).equals(null));
        if (select == 1) btnStateDef.setSelection(true);
        btnStateDef.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                super.widgetSelected(e);
                StoryCardModel.setStoryCardStatus(null);
                select = 1;
            }
        });
        btnStateProg = new Button(groupStoryCardState, SWT.RADIO);
        btnStateProg.setText("In-Progress");
        if (select == 2) btnStateProg.setSelection(true);
        btnStateProg.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                super.widgetSelected(e);
                StoryCardModel.setStoryCardStatus(IndexCard.STATUS_IN_PROGRESS);
                select = 2;
            }
        });
        btnStateAcc = new Button(groupStoryCardState, SWT.RADIO);
        btnStateAcc.setText("Accepted");
        if (select == 3) btnStateAcc.setSelection(true);
        btnStateAcc.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                super.widgetSelected(e);
                StoryCardModel.setStoryCardStatus(IndexCard.STATUS_ACCEPTED);
                select = 3;
            }
        });
        btnStateComp = new Button(groupStoryCardState, SWT.RADIO);
        btnStateComp.setText("Complete");
        if (select == 4) btnStateComp.setSelection(true);
        btnStateComp.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                super.widgetSelected(e);
                StoryCardModel.setStoryCardStatus(IndexCard.STATUS_COMPLETED);
                select = 4;
            }
        });
        Group compositeShowInformationArea = new Group(storyCardTabComposite, SWT.NONE);
        compositeShowInformationArea.setText("Show Information Setup");
        GridLayout layoutShowInformationArea = new GridLayout(2, false);
        compositeShowInformationArea.setLayout(layoutShowInformationArea);
        GridData gridDataShowInformationArea = new GridData();
        gridDataShowInformationArea.grabExcessHorizontalSpace = true;
        gridDataShowInformationArea.horizontalAlignment = GridData.FILL;
        gridDataShowInformationArea.verticalAlignment = GridData.FILL;
        compositeShowInformationArea.setLayoutData(gridDataShowInformationArea);
        compositeShowInformationArea.pack();
        Label estimateLabel = new Label(compositeShowInformationArea, SWT.NONE);
        estimateLabel.setText("&Estimate:");
        estimateLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        Composite compositeEstimateArea = new Composite(compositeShowInformationArea, SWT.NONE);
        GridLayout layoutEstimateArea = new GridLayout(2, false);
        compositeEstimateArea.setLayout(layoutEstimateArea);
        btnEstimateBestCase = new Button(compositeEstimateArea, SWT.CHECK);
        btnEstimateBestCase.setText("Show best case");
        btnEstimateBestCase.setSelection(Settings.isEstimate_bestCase());
        btnEstimateBestCase.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                super.widgetSelected(e);
                Settings.setEstimate_bestCase(button.getSelection());
                setEstimates(button.getSelection(), 0);
            }
        });
        btnEstimateWorstCase = new Button(compositeEstimateArea, SWT.CHECK);
        btnEstimateWorstCase.setText("Show worst case");
        btnEstimateWorstCase.setSelection(Settings.isEstimate_worstCase());
        btnEstimateWorstCase.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                super.widgetSelected(e);
                Settings.setEstimate_worstCase(button.getSelection());
                setEstimates(button.getSelection(), 1);
            }
        });
        btnEstimateActual = new Button(compositeEstimateArea, SWT.CHECK);
        btnEstimateActual.setText("Show actual");
        btnEstimateActual.setSelection(Settings.isEstimate_actual());
        btnEstimateActual.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                super.widgetSelected(e);
                Settings.setEstimate_actual(button.getSelection());
                setEstimates(button.getSelection(), 3);
            }
        });
        btnEstimateRemaining = new Button(compositeEstimateArea, SWT.CHECK);
        btnEstimateRemaining.setText("Show remaining");
        btnEstimateRemaining.setSelection(Settings.isEstimate_remaining());
        btnEstimateRemaining.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                super.widgetSelected(e);
                Settings.setEstimate_remaining(button.getSelection());
                setEstimates(button.getSelection(), 2);
            }
        });
        btnDescription = new Button(compositeEstimateArea, SWT.CHECK);
        btnDescription.setText("Show Description");
        btnDescription.setSelection(Settings.isDescription());
        btnDescription.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                super.widgetSelected(e);
                System.out.println("Show Description clicked!" + button.getSelection());
                Settings.setDescription(button.getSelection());
                setEstimates(button.getSelection(), 2);
            }
        });
        getTab().setControl(storyCardTabComposite);
        return getTab();
    }

    private void setEstimates(boolean bool, int estimateElements) {
        String select = null;
        if (bool == true) select = "true";
        if (bool == false) select = "false";
        List<StoryCardModel> scmList = new ArrayList<StoryCardModel>();
        scmList = getProjectModel().getStoryCardModelList();
        for (IterationCardModel icm : getProjectModel().getIterations()) {
            scmList.addAll(icm.getChildrenList());
        }
        switch(estimateElements) {
            case 0:
                {
                    for (StoryCardModel scm : scmList) {
                        scm.setBestCaseEstimate(select);
                    }
                }
            case 1:
                {
                    for (StoryCardModel scm : scmList) {
                        scm.setWorstCaseEstimate(select);
                    }
                }
            case 2:
                {
                    for (StoryCardModel scm : scmList) {
                        scm.setRemainingEstimate(select);
                    }
                }
            case 3:
                {
                    for (StoryCardModel scm : scmList) {
                        scm.setActualEstimate(select);
                    }
                }
        }
    }

    public ProjectModel getProjectModel() {
        return projectModel;
    }

    public void setProjectModel(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    public TabItem getTab() {
        return tab;
    }

    public void setTab(TabItem tab) {
        this.tab = tab;
    }
}
