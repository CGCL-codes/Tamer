package org.gvt.inspector;

import java.util.Set;
import org.biopax.paxtools.model.level2.physicalEntity;
import org.biopax.paxtools.model.level2.xref;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.gvt.ChisioMain;
import org.gvt.util.AbstractOptionsPack;

/**
 * This class is abstract class for handling dialogs with Stream Direction Groups
 *
 * @author Shatlyk Ashyralyev
 * 
 * Copyright: I-Vis Research Group, Bilkent University, 2007
 */
public abstract class AbstractQueryParamWithStreamDialog extends AbstractQueryParamDialog {

    /**
	 * Direction of stream
	 */
    protected Group streamDirectionGroup;

    protected Button downstreamButton;

    protected Button upstreamButton;

    protected Button bothBotton;

    /**
	 * Constructor
	 */
    public AbstractQueryParamWithStreamDialog(ChisioMain main) {
        super(main);
    }

    /**
	 * Method for creating Stream Direction Group
	 */
    protected void createStreamDirectionGroup(int horizontalSpan, int verticalSpan, boolean isBothButton) {
        streamDirectionGroup = new Group(shell, SWT.NONE);
        streamDirectionGroup.setText("Stream Direction:");
        GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        gridData.horizontalSpan = horizontalSpan;
        gridData.verticalSpan = verticalSpan;
        streamDirectionGroup.setLayoutData(gridData);
        streamDirectionGroup.setLayout(new GridLayout());
        downstreamButton = new Button(streamDirectionGroup, SWT.RADIO);
        downstreamButton.setText("Downstream");
        gridData = new GridData(GridData.BEGINNING, GridData.CENTER, false, false);
        downstreamButton.setLayoutData(gridData);
        upstreamButton = new Button(streamDirectionGroup, SWT.RADIO);
        upstreamButton.setText("Upstream");
        gridData = new GridData(GridData.BEGINNING, GridData.CENTER, false, false);
        upstreamButton.setLayoutData(gridData);
        if (isBothButton) {
            bothBotton = new Button(streamDirectionGroup, SWT.RADIO);
            bothBotton.setText("Both");
            gridData = new GridData(GridData.BEGINNING, GridData.CENTER, false, false);
            bothBotton.setLayoutData(gridData);
        }
    }
}
