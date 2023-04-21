package org.hip.vif.forum.groups.ui;

import org.hip.vif.core.interfaces.IMessages;
import org.hip.vif.forum.groups.Activator;
import org.hip.vif.forum.groups.data.ContributionContainer;
import org.hip.vif.forum.groups.data.ContributionWrapper;
import org.hip.vif.web.util.VIFViewHelper;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Base class for views displaying the list of contributions pending in the workflow.  
 * 
 * @author Luthiger
 * Created: 10.07.2011
 */
@SuppressWarnings("serial")
public abstract class AbstractContributionsProcessView extends CustomComponent {

    protected VerticalLayout initComponent(String inFirstname, String inFamilyname, String inGroupTitle, IMessages inMessages, String inTitleKey) {
        VerticalLayout outLayout = new VerticalLayout();
        setCompositionRoot(outLayout);
        outLayout.setSizeFull();
        outLayout.setStyleName("vif-view");
        String lTitle = String.format(inMessages.getFormattedMessage(inTitleKey, inFirstname, inFamilyname, inGroupTitle));
        outLayout.addComponent(new Label(String.format(VIFViewHelper.TMPL_TITLE, "vif-pagetitle", lTitle), Label.CONTENT_XHTML));
        return outLayout;
    }

    /**
	 * Table | chk : Nr. : Question : Status |
	 * 
	 * @param inData {@link ContributionContainer}
	 * @param inCheckedState int the contribution's workflow state (e.g. <code>WorkflowAwareContribution.S_PRIVATE</code>)
	 * @param inListener {@link ValueChangeListener}
	 * @return Table
	 */
    protected Table createTable(ContributionContainer inData, final int inCheckedState, final ValueChangeListener inListener) {
        Table outTable = new Table();
        outTable.setStyleName("vif-table");
        outTable.setWidth("100%");
        outTable.setContainerDataSource(inData);
        outTable.addGeneratedColumn(ContributionContainer.CONTRIBUTION_CHECK, new Table.ColumnGenerator() {

            public Component generateCell(Table inSource, Object inItemId, Object inColumnId) {
                ContributionWrapper lContribution = (ContributionWrapper) inItemId;
                return lContribution.getState() == inCheckedState ? VIFViewHelper.createCheck(lContribution, new VIFViewHelper.IConfirmationModeChecker() {

                    public boolean inConfirmationMode() {
                        return isConfirmationMode();
                    }
                }) : new Label();
            }
        });
        outTable.addGeneratedColumn(ContributionContainer.CONTRIBUTION_TEXT, new Table.ColumnGenerator() {

            public Component generateCell(Table inSource, Object inItemId, Object inColumnId) {
                return new Label(((ContributionWrapper) inItemId).getContributionText(), Label.CONTENT_XHTML);
            }
        });
        outTable.setColumnCollapsingAllowed(true);
        outTable.setColumnReorderingAllowed(true);
        outTable.setSelectable(true);
        outTable.setImmediate(true);
        outTable.setPageLength(0);
        outTable.setColumnExpandRatio(ContributionContainer.CONTRIBUTION_TEXT, 1);
        outTable.addListener(inListener);
        outTable.setVisibleColumns(ContributionContainer.NATURAL_COL_ORDER);
        outTable.setColumnHeaders(VIFViewHelper.getColumnHeaders(ContributionContainer.COL_HEADERS, Activator.getMessages()));
        return outTable;
    }

    protected abstract boolean isConfirmationMode();
}
