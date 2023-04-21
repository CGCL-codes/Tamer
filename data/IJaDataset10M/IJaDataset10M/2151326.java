package org.kalypso.nofdpidss.flow.network.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NotImplementedException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;
import org.kalypso.contribs.eclipse.core.runtime.ExceptionHelper;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.gmlschema.property.IPropertyType;
import org.kalypso.model.wspm.sobek.core.interfaces.IBoundaryNode;
import org.kalypso.model.wspm.sobek.core.interfaces.IBranch;
import org.kalypso.model.wspm.sobek.core.interfaces.IConnectionNode;
import org.kalypso.model.wspm.sobek.core.interfaces.IModelMember;
import org.kalypso.model.wspm.sobek.core.interfaces.INode;
import org.kalypso.model.wspm.sobek.core.interfaces.ISobekConstants;
import org.kalypso.model.wspm.sobek.core.interfaces.ISobekModelMember;
import org.kalypso.model.wspm.sobek.core.interfaces.INode.TYPE;
import org.kalypso.model.wspm.sobek.core.model.BoundaryNode;
import org.kalypso.model.wspm.sobek.core.model.ConnectionNode;
import org.kalypso.model.wspm.sobek.core.utils.FNGmlUtils;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasure;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureBankRecessionAndFillUp;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureBottomLevelChange;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureContainers;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureDiversion;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureFloodplainLowering;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasurePolder;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureRetardingBasin;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureWeir;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IProjectModel;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolProject;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool.POOL_TYPE;
import org.kalypso.nofdpidss.flow.network.i18n.Messages;
import org.kalypso.nofdpidss.flow.network.wizard.node.boundary.SobekWizardEditBoundaryNode;
import org.kalypso.ogc.gml.FeatureUtils;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.geometry.GM_Curve;
import org.kalypsodeegree.model.geometry.GM_Point;

/**
 * @author Dirk Kuch
 */
public class FNHelper {

    public static void deleteBranch(final IModelMember modelBuilder, final Feature feature, final Shell shell) throws CoreException {
        final PoolProject pool = (PoolProject) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eProject);
        final IProjectModel model = pool.getModel();
        final IMeasure[] measures = FNHelper.getBranchMeasures(modelBuilder.getBranchMembers(), feature, model.getMeasureMembers().getContainer());
        String m = "";
        for (final IMeasure measure : measures) {
            m += " " + measure.getName() + ",";
        }
        if (m.length() > 2) {
            m = Messages.FNHelper_5 + m.substring(0, m.length() - 1) + Messages.FNHelper_6;
        }
        if (!MessageDialog.openConfirm(shell.getShell(), Messages.FNHelper_0, Messages.FNHelper_1 + " " + FeatureUtils.getFeatureName(ISobekConstants.NS_SOBEK, feature) + " ? " + Messages.FNHelper_7 + m)) return;
        try {
            final QName qn = feature.getFeatureType().getQName();
            final QName[] nofdpNodes = new QName[] { ISobekConstants.QN_NOFDP_POLDER_NODE, ISobekConstants.QN_NOFDP_RETARDIN_BASIN_NODE, ISobekConstants.QN_NOFDP_WEIR_NODE };
            if (ArrayUtils.contains(nofdpNodes, qn)) FeatureUtils.deleteFeature(modelBuilder.getWorkspace(), feature); else modelBuilder.deleteFoo(feature);
            for (final IMeasure measure : measures) {
                FeatureUtils.deleteFeature(measure.getModel().getCommandableWorkspace(), measure);
            }
        } catch (final Exception e1) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e1));
        }
    }

    private static IMeasure[] getBranchMeasures(final IBranch[] branches, final Feature fBranch, final IMeasureContainers container) throws CoreException {
        final List<IMeasure> intersection = new ArrayList<IMeasure>();
        IBranch branch = null;
        for (final IBranch b : branches) {
            if (b.getFeature().equals(fBranch)) branch = b;
        }
        if (branch == null) throw ExceptionHelper.getCoreException(IStatus.ERROR, FNHelper.class, Messages.FNHelper_8 + fBranch.getId());
        final IMeasure[] measures = container.getAllMeasures();
        for (final IMeasure measure : measures) {
            if (measure instanceof IMeasurePolder) {
                final IMeasurePolder polder = (IMeasurePolder) measure;
                final IBranch fBr = polder.getBranch();
                if (branch.equals(fBr)) intersection.add(polder);
            } else if (measure instanceof IMeasureRetardingBasin) {
                final IMeasureRetardingBasin rb = (IMeasureRetardingBasin) measure;
                final IBranch fBr = rb.getBranch();
                if (branch.equals(fBr)) intersection.add(rb);
            } else if (measure instanceof IMeasureWeir) {
                final IMeasureWeir weir = (IMeasureWeir) measure;
                final IBranch fBr = weir.getBranch();
                if (branch.equals(fBr)) intersection.add(weir);
            } else if (measure instanceof IMeasureFloodplainLowering) {
                final IMeasureFloodplainLowering fp = (IMeasureFloodplainLowering) measure;
                final IBranch fBr = fp.getBranch();
                if (branch.equals(fBr)) intersection.add(fp);
            } else if (measure instanceof IMeasureBankRecessionAndFillUp) {
                final IMeasureBankRecessionAndFillUp bank = (IMeasureBankRecessionAndFillUp) measure;
                final IBranch fBr = bank.getBranch();
                if (branch.equals(fBr)) intersection.add(bank);
            } else if (measure instanceof IMeasureBottomLevelChange) {
                final IMeasureBottomLevelChange bottom = (IMeasureBottomLevelChange) measure;
                final IBranch fBr = bottom.getBranch();
                if (branch.equals(fBr)) intersection.add(bottom);
            } else if (measure instanceof IMeasureDiversion) {
                final IMeasureDiversion diversion = (IMeasureDiversion) measure;
                final IBranch firstBranch = diversion.getFirstBranch();
                final IBranch secondBranch = diversion.getSecondBranch();
                if (branch.equals(firstBranch) || branch.equals(secondBranch)) intersection.add(diversion);
            }
        }
        return intersection.toArray(new IMeasure[] {});
    }

    public static void deleteFlowNetworkElement(final IModelMember model, final Object[] features, final QName[] canProcess, final Shell shell) throws Exception {
        for (final Object obj : features) {
            if (!(obj instanceof Feature)) continue;
            final Feature feature = (Feature) obj;
            final QName qname = feature.getFeatureType().getQName();
            if (!ArrayUtils.contains(canProcess, qname)) continue;
            if (ISobekConstants.QN_HYDRAULIC_SOBEK_BRANCH.equals(qname)) {
                FNHelper.deleteBranch(model, feature, shell);
            } else {
                QName qn = null;
                final IPropertyType[] properties = feature.getFeatureType().getProperties();
                for (final IPropertyType pt : properties) {
                    if ("name".equals(pt.getQName().getLocalPart())) qn = pt.getQName();
                }
                if (MessageDialog.openQuestion(shell, Messages.FNHelper_12, Messages.FNHelper_13 + FeatureUtils.getFeatureName(qn.getNamespaceURI(), feature))) FeatureUtils.deleteFeature(model.getWorkspace(), feature);
            }
        }
    }

    private static void connectionNodeToBoundaryNode(final IConnectionNode cn) throws Exception {
        final IBoundaryNode boundaryNode = (IBoundaryNode) FNGmlUtils.createNode(cn.getModelMember(), TYPE.eBoundaryNode, cn.getLocation(), new INode[] {});
        final Map<QName, Object> map = new HashMap<QName, Object>();
        map.put(ISobekConstants.QN_HYDRAULIC_NAME, cn.getName());
        map.put(ISobekConstants.QN_HYDRAULIC_DESCRIPTION, cn.getDescription());
        FeatureUtils.updateProperties(cn.getModelMember().getWorkspace(), boundaryNode.getFeature(), map);
        for (final IBranch branch : cn.getInflowingBranches()) boundaryNode.addInflowingBranch(branch);
        for (final IBranch branch : cn.getOutflowingBranches()) boundaryNode.addOutflowingBranch(branch);
        final IBranch[] inflowing = boundaryNode.getInflowingBranches();
        for (final IBranch branch : inflowing) updateBranchNode(branch, boundaryNode);
        final IBranch[] outflowing = boundaryNode.getOutflowingBranches();
        for (final IBranch branch : outflowing) updateBranchNode(branch, boundaryNode);
        new UIJob(Messages.NodeUtils_1) {

            @Override
            public IStatus runInUIThread(final IProgressMonitor monitor) {
                final IWorkbenchWizard wizard = new SobekWizardEditBoundaryNode(boundaryNode);
                wizard.init(PlatformUI.getWorkbench(), null);
                final WizardDialog dialog = new WizardDialog(null, wizard);
                dialog.open();
                final int returnCode = dialog.getReturnCode();
                try {
                    if (Window.OK == returnCode) cn.delete(); else boundaryNode.delete();
                } catch (final Exception e) {
                    return new Status(IStatus.ERROR, this.getClass().getName(), e.getMessage());
                }
                return Status.OK_STATUS;
            }
        }.schedule();
    }

    private static void boundaryNodeToConnectionNode(final BoundaryNode bn) throws Exception {
        final IConnectionNode connectionNode = (IConnectionNode) FNGmlUtils.createNode(bn.getModel(), TYPE.eConnectionNode, bn.getLocation(), new INode[] {});
        final Map<QName, Object> map = new HashMap<QName, Object>();
        map.put(ISobekConstants.QN_HYDRAULIC_NAME, bn.getName());
        map.put(ISobekConstants.QN_HYDRAULIC_DESCRIPTION, bn.getDescription());
        FeatureUtils.updateProperties(bn.getModel().getWorkspace(), connectionNode.getFeature(), map);
        for (final IBranch branch : bn.getInflowingBranches()) connectionNode.addInflowingBranch(branch);
        for (final IBranch branch : bn.getOutflowingBranches()) connectionNode.addOutflowingBranch(branch);
        final IBranch[] inflowing = connectionNode.getInflowingBranches();
        for (final IBranch branch : inflowing) updateBranchNode(branch, connectionNode);
        final IBranch[] outflowing = connectionNode.getOutflowingBranches();
        for (final IBranch branch : outflowing) updateBranchNode(branch, connectionNode);
        bn.delete();
    }

    /**
   * @see org.kalypso.model.wspm.sobek.core.interfaces.INodeUtils#switchBoundaryConnectionNode(org.kalypsodeegree.model.feature.Feature)
   */
    public static void switchBoundaryConnectionNode(final ISobekModelMember model, final Feature node) throws Exception {
        final QName nqn = node.getFeatureType().getQName();
        if (ISobekConstants.QN_HYDRAULIC_CONNECTION_NODE.equals(nqn)) connectionNodeToBoundaryNode(new ConnectionNode(model, node)); else if (ISobekConstants.QN_HYDRAULIC_BOUNDARY_NODE.equals(nqn)) boundaryNodeToConnectionNode(new BoundaryNode(model, node)); else throw new NotImplementedException();
    }

    private static void updateBranchNode(final IBranch branch, final INode node) throws Exception {
        final GM_Curve curve = branch.getCurve();
        final GM_Point pn = node.getLocation();
        if (curve.getStartPoint().intersects(pn)) branch.setUpperNode(node); else if (curve.getEndPoint().intersects(pn)) branch.setLowerNode(node);
    }
}
