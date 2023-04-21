package org.kalypso.nofdpidss.measure.construction.edit.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureDikeConstruction;
import org.kalypso.nofdpidss.core.view.widgets.MyWizardPage;
import org.kalypso.nofdpidss.measure.construction.i18n.Messages;
import org.kalypso.util.swt.WizardFeatureLabel;
import org.kalypso.util.swt.WizardFeatureTextBox;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class PageEditDykeConstruction extends MyWizardPage implements IMeasureEditDykeConstruction {

    private final Feature m_node;

    private WizardFeatureTextBox m_wallHeightBegin;

    private WizardFeatureTextBox m_wallHeightEnd;

    private WizardFeatureTextBox m_slopeRiverSide;

    private WizardFeatureTextBox m_slopeLandSide;

    private WizardFeatureTextBox m_topWidth;

    public PageEditDykeConstruction(final Feature node) {
        super("editPageDykeConstruction");
        m_node = node;
        setTitle(Messages.PageEditDykeConstruction_1);
        setDescription(Messages.PageEditDykeConstruction_2);
    }

    /**
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
    public void createControl(final Composite parent) {
        setPageComplete(false);
        final Group container = new Group(parent, SWT.NULL);
        container.setLayout(new GridLayout(2, false));
        container.setText(Messages.PageEditDykeConstruction_21);
        setControl(container);
        new WizardFeatureLabel(m_node, IMeasureDikeConstruction.QN_DIKE_TOP_WIDTH, container);
        m_topWidth = new WizardFeatureTextBox(m_node, IMeasureDikeConstruction.QN_DIKE_TOP_WIDTH);
        m_topWidth.draw(container, new GridData(GridData.FILL, GridData.FILL, true, false), SWT.BORDER);
        new WizardFeatureLabel(m_node, IMeasureDikeConstruction.QN_CREST_HEIGHT_BEGIN, container);
        m_wallHeightBegin = new WizardFeatureTextBox(m_node, IMeasureDikeConstruction.QN_CREST_HEIGHT_BEGIN);
        m_wallHeightBegin.draw(container, new GridData(GridData.FILL, GridData.FILL, true, false), SWT.BORDER);
        m_wallHeightBegin.addModifyListener(new Runnable() {

            public void run() {
                checkPage();
            }
        });
        new WizardFeatureLabel(m_node, IMeasureDikeConstruction.QN_CREST_HEIGHT_END, container);
        m_wallHeightEnd = new WizardFeatureTextBox(m_node, IMeasureDikeConstruction.QN_CREST_HEIGHT_END);
        m_wallHeightEnd.draw(container, new GridData(GridData.FILL, GridData.FILL, true, false), SWT.BORDER);
        m_wallHeightEnd.addModifyListener(new Runnable() {

            public void run() {
                checkPage();
            }
        });
        new WizardFeatureLabel(m_node, IMeasureDikeConstruction.QN_SLOPE_LANDSIDE, container);
        m_slopeLandSide = new WizardFeatureTextBox(m_node, IMeasureDikeConstruction.QN_SLOPE_LANDSIDE);
        m_slopeLandSide.draw(container, new GridData(GridData.FILL, GridData.FILL, true, false), SWT.BORDER);
        m_slopeLandSide.addModifyListener(new Runnable() {

            public void run() {
                checkPage();
            }
        });
        new WizardFeatureLabel(m_node, IMeasureDikeConstruction.QN_SLOPE_RIVERSIDE, container);
        m_slopeRiverSide = new WizardFeatureTextBox(m_node, IMeasureDikeConstruction.QN_SLOPE_RIVERSIDE);
        m_slopeRiverSide.draw(container, new GridData(GridData.FILL, GridData.FILL, true, false), SWT.BORDER);
        m_slopeRiverSide.addModifyListener(new Runnable() {

            public void run() {
                checkPage();
            }
        });
        checkPage();
    }

    public Double getSlopeLandSide() {
        return m_slopeLandSide.getAsDouble();
    }

    public Double getSlopeRiverSide() {
        return m_slopeRiverSide.getAsDouble();
    }

    public Double getTopWidth() {
        return m_topWidth.getAsDouble();
    }

    public Double getWallHeightBegin() {
        return m_wallHeightBegin.getAsDouble();
    }

    public Double getWallHeightEnd() {
        return m_wallHeightEnd.getAsDouble();
    }

    @Override
    protected void checkPage() {
        if (!checkPositiveNumber(m_topWidth)) return;
        if (!checkPositiveNumber(m_slopeLandSide)) return;
        if (!checkPositiveNumber(m_slopeRiverSide)) return;
        if (!checkNumber(m_wallHeightBegin)) return;
        if (!checkNumber(m_wallHeightEnd)) return;
        setErrorMessage(null);
        setMessage(null);
        setPageComplete(true);
    }
}
