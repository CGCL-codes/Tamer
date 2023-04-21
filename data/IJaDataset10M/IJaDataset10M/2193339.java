package org.kalypso.nofdpidss.profiles.chart.panels;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.kalypso.model.wspm.core.IWspmConstants;
import org.kalypso.model.wspm.core.KalypsoModelWspmCoreExtensions;
import org.kalypso.model.wspm.core.profil.IProfil;
import org.kalypso.model.wspm.core.profil.IProfilPointMarker;
import org.kalypso.model.wspm.core.profil.IProfilPointPropertyProvider;
import org.kalypso.model.wspm.core.profil.changes.PointMarkerEdit;
import org.kalypso.model.wspm.core.profil.util.ProfilObsHelper;
import org.kalypso.model.wspm.ui.profil.operation.ProfilOperation;
import org.kalypso.model.wspm.ui.profil.operation.ProfilOperationJob;
import org.kalypso.model.wspm.ui.view.ProfilViewData;
import org.kalypso.nofdpidss.profiles.INofdpWspmConstants;
import org.kalypso.nofdpidss.profiles.i18n.Messages;
import org.kalypso.nofdpidss.profiles.marker.NofdpPointMarker;
import org.kalypso.observation.result.IComponent;
import org.kalypso.observation.result.IRecord;
import org.kalypso.observation.result.TupleResult;

/**
 * @author Dirk Kuch
 */
public class BOKPanel extends NofdpAbstractProfileView {

    public BOKPanel(final IProfil pem, final ProfilViewData viewdata) {
        super(pem, viewdata);
    }

    @Override
    protected String getMarkerType() {
        return INofdpWspmConstants.MARKER_TYPE_BOK;
    }

    @Override
    protected void render(final IProfilPointMarker[] markersBOK, final FormToolkit toolkit, final Composite body) {
        final Composite cValues = toolkit.createComposite(body);
        cValues.setLayout(new GridLayout(2, true));
        cValues.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        toolkit.createLabel(cValues, "edge 1");
        toolkit.createLabel(cValues, "edge 2");
        for (final IProfilPointMarker marker : markersBOK) {
            final IRecord point = marker.getPoint();
            final IComponent breite = ProfilObsHelper.getPropertyFromId(point, IWspmConstants.POINT_PROPERTY_BREITE);
            final Object value = point.getValue(breite);
            final Text text = toolkit.createText(cValues, String.format("%2.3f", value), SWT.READ_ONLY | SWT.BORDER);
            text.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        }
    }

    @Override
    protected void renderCreate(final FormToolkit toolkit, final Composite body) {
        final Button bCreate = toolkit.createButton(body, Messages.BOKPanel_3, SWT.NONE);
        bCreate.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        bCreate.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final IProfil iProfil = getProfil();
                final IProfilPointPropertyProvider provider = KalypsoModelWspmCoreExtensions.getPointPropertyProviders(iProfil.getType());
                final IComponent component = provider.getPointProperty(getMarkerType());
                if (!iProfil.hasPointProperty(component)) iProfil.addPointProperty(component);
                final TupleResult result = iProfil.getResult();
                final IRecord rStart = result.get(0);
                final IRecord rEnd = result.get(result.size() - 1);
                final ProfilOperation operation = new ProfilOperation(Messages.BOKPanel_4, getProfil(), true);
                operation.addChange(new PointMarkerEdit(new NofdpPointMarker(rStart, component), true));
                operation.addChange(new PointMarkerEdit(new NofdpPointMarker(rEnd, component), true));
                new ProfilOperationJob(operation).schedule();
            }
        });
    }
}
