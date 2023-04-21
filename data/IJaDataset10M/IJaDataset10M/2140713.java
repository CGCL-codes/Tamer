package org.kalypso.nofdpidss.variant.view;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.gmlschema.feature.IFeatureType;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IConstructionContainer;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IEcologyContainer;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasure;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IProjectModel;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IVariant;
import org.kalypso.nofdpidss.variant.i18n.Messages;
import org.kalypso.ogc.gml.GisTemplateHelper;
import org.kalypso.template.gistreeview.Gistreeview;
import org.kalypso.template.types.LayerType;
import org.kalypso.ui.editor.gmleditor.ui.FeatureAssociationTypeElement;
import org.kalypso.ui.editor.gmleditor.ui.GMLContentProvider;
import org.kalypso.ui.editor.gmleditor.ui.GMLLabelProvider;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree_impl.model.feature.gmlxpath.GMLXPath;

/**
 * @author Dirk Kuch
 */
public class VMTree {

    private final String m_gmvFilePath;

    protected Feature m_selectedFeature;

    protected final IProjectModel m_model;

    private TreeViewer m_treeViewer;

    public VMTree(final IProjectModel model, final String gmvFilePath) {
        m_model = model;
        m_gmvFilePath = gmvFilePath;
    }

    @SuppressWarnings("deprecation")
    public void draw(final FormToolkit toolkit, final Composite body, final GridData layoutData) throws JAXBException, CoreException {
        final Composite treeBody = toolkit.createComposite(body);
        treeBody.setLayout(new GridLayout());
        treeBody.setLayoutData(layoutData);
        m_treeViewer = new TreeViewer(treeBody, SWT.BORDER | SWT.SINGLE);
        m_treeViewer.getTree().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        final IFile file = NofdpCorePlugin.getProjectManager().getActiveProject().getFile(m_gmvFilePath);
        if (!file.exists()) throw new IllegalStateException(Messages.VMTree_1 + file.getLocation().toOSString());
        final Gistreeview gisTree = GisTemplateHelper.loadGisTreeView(file);
        final GMLContentProvider contentProvider = new GMLContentProvider();
        m_treeViewer.setContentProvider(contentProvider);
        m_treeViewer.setLabelProvider(new GMLLabelProvider());
        m_treeViewer.setInput(m_model.getWorkspace());
        final LayerType input = gisTree.getInput();
        final String rootPathString = input.getFeatureXPath();
        final GMLXPath rootPath = new GMLXPath(rootPathString, null);
        contentProvider.setRootPath(rootPath);
        m_treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(final SelectionChangedEvent event) {
                final ISelection selection = m_treeViewer.getSelection();
                if (!(selection instanceof TreeSelection)) return;
                final TreeSelection treeSelection = (TreeSelection) selection;
                final Object element = treeSelection.getFirstElement();
                if (!(element instanceof Feature)) return;
                m_selectedFeature = (Feature) element;
            }
        });
        m_treeViewer.expandAll();
        m_treeViewer.setFilters(new ViewerFilter[] { new ViewerFilter() {

            final QName[] allowedFeatures = new QName[] { IConstructionContainer.QN_CONTAINER, IEcologyContainer.QN_CONTAINER, GmlConstants.QN_MEASURE_ABSTRACT_MEASURE_CAT_TYPE_11, GmlConstants.QN_MEASURE_ABSTRACT_MEASURE_CAT_TYPE_12, GmlConstants.QN_MEASURE_ABSTRACT_MEASURE_CAT_TYPE_13, GmlConstants.QN_MEASURE_ABSTRACT_MEASURE_CAT_TYPE_14, GmlConstants.QN_MEASURE_ABSTRACT_MEASURE_CAT_TYPE_21, GmlConstants.QN_MEASURE_ABSTRACT_MEASURE_CAT_TYPE_22, GmlConstants.QN_MEASURE_ABSTRACT_MEASURE_CAT_TYPE_23, GmlConstants.QN_MEASURE_ABSTRACT_MEASURE_CAT_TYPE_24 };

            @Override
            public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
                try {
                    final IVariant lastUsedVariant = m_model.getLastUsedVariant();
                    if (lastUsedVariant == null) return false;
                    final IMeasure[] measures = lastUsedVariant.getMeasures();
                    if (element instanceof Feature) {
                        final Feature feature = (Feature) element;
                        final IFeatureType featureType = feature.getFeatureType();
                        if (ArrayUtils.contains(allowedFeatures, featureType.getQName())) return true;
                        final IFeatureType substitutionGroupFT = feature.getFeatureType().getSubstitutionGroupFT();
                        if (ArrayUtils.contains(allowedFeatures, substitutionGroupFT.getQName())) {
                            for (final IMeasure measure : measures) {
                                if (feature.getId().equals(measure.getId())) return false;
                            }
                            return true;
                        }
                    } else if (element instanceof FeatureAssociationTypeElement) return true;
                } catch (final CoreException e) {
                    NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
                }
                return false;
            }
        } });
        m_treeViewer.getTree().update();
        m_treeViewer.getTree().layout();
    }

    public Feature getSelectedFeature() {
        return m_selectedFeature;
    }

    public void update() {
        m_treeViewer.refresh();
    }
}
