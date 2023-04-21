package org.kalypso.nofdpidss.hydraulic.computation.processing.worker.workspace.model.measures;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.kalypso.contribs.eclipse.core.resources.ResourceUtilities;
import org.kalypso.contribs.eclipse.core.runtime.ExceptionHelper;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.contribs.eclipse.ui.progress.ConsoleHelper;
import org.kalypso.contribs.java.io.MyPrintStream;
import org.kalypso.jts.JTSUtilities;
import org.kalypso.model.wspm.core.gml.ProfileFeatureFactory;
import org.kalypso.model.wspm.core.profil.IProfil;
import org.kalypso.model.wspm.sobek.core.interfaces.IBranch;
import org.kalypso.model.wspm.sobek.core.interfaces.ICrossSectionNode;
import org.kalypso.model.wspm.sobek.core.interfaces.INode;
import org.kalypso.model.wspm.sobek.core.interfaces.ISobekConstants;
import org.kalypso.model.wspm.sobek.core.interfaces.ISobekModelMember;
import org.kalypso.model.wspm.sobek.core.interfaces.INode.TYPE;
import org.kalypso.model.wspm.sobek.core.utils.FNGmlUtils;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.base.IHydraulModel;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.base.IWaterBodies;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.base.IWaterBody;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.result.IVariantResultMember;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IDoppelTrapezProfilMeasure;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasure;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureBankRecessionAndFillUp;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureBottomLevelChange;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureDiversion;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureFloodplainLowering;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureMeandering;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasurePolder;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureRetardingBasin;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureWeir;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IVariant;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.implementation.DoppelTrapezProfileMemberHandler;
import org.kalypso.nofdpidss.hydraulic.computation.i18n.Messages;
import org.kalypso.nofdpidss.hydraulic.computation.processing.worker.utils.HCUtils;
import org.kalypso.nofdpidss.hydraulic.computation.processing.worker.utils.workspace.IWorkspaceProvider;
import org.kalypso.nofdpidss.hydraulic.computation.processing.worker.workspace.model.helper.ICoverageModifier;
import org.kalypso.nofdpidss.hydraulic.computation.processing.worker.workspace.model.helper.MeanderingUtil;
import org.kalypso.nofdpidss.hydraulic.computation.processing.worker.workspace.model.helper.RoughnessHelper;
import org.kalypso.nofdpidss.hydraulic.computation.processing.worker.workspace.model.modifier.MeanderingDgmModifier;
import org.kalypso.nofdpidss.hydraulic.computation.utils.exceptions.HCExHelper;
import org.kalypso.nofdpidss.hydraulic.computation.utils.profiles.ProfileBuilder;
import org.kalypso.nofdpidss.profiles.ProfileHelper;
import org.kalypso.ogc.gml.FeatureUtils;
import org.kalypso.ogc.gml.serialize.GmlSerializer;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.feature.GMLWorkspace;
import org.kalypsodeegree.model.geometry.GM_Curve;
import org.kalypsodeegree.model.geometry.GM_Point;
import org.kalypsodeegree_impl.gml.binding.commons.ICoverage;
import org.kalypsodeegree_impl.gml.binding.commons.ICoverageCollection;
import org.kalypsodeegree_impl.model.feature.FeatureHelper;
import org.kalypsodeegree_impl.model.geometry.JTSAdapter;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * @author Dirk Kuch
 */
public final class HydraulicMeasureMeandering extends AbstractHydraulicMeasure {

    Set<IMeasure> m_invalidMeasures = new LinkedHashSet<IMeasure>();

    private static final int BUFFER_PROFILE = 10;

    private final DoppelTrapezProfileMemberHandler m_handler;

    public HydraulicMeasureMeandering(final IWorkspaceProvider provider, final IVariantResultMember calculationCase, final IMeasure measure) {
        super(provider, calculationCase, measure);
        m_handler = new DoppelTrapezProfileMemberHandler(measure.getModel(), (Feature) measure.getProperty(IDoppelTrapezProfilMeasure.QN_DOPPEL_TRAPEZ_PROFILE_MEMBER));
    }

    IMeasureMeandering getMeandering() {
        return (IMeasureMeandering) getMeasure();
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.interactiveplanning.main.hydraulic.computation.measures.IHydraulicMeasure#modifyProfiles(org.kalypso.model.wspm.sobek.core.interfaces.ICrossSectionNode[])
   */
    public void modifyCrossSection(final ICrossSectionNode[] nodes, final IProgressMonitor monitor, final MyPrintStream outputStream) throws Exception {
    }

    /**
   * <pre>
   *     MEANDERING
   *      + +            lower
   *    +     +          snap
   * --+xxxxxxxx+xxxxxxxxxx+------------------- branch   (x - will be deleted (and all measure
   *  upper       +     +                                 on this part of branch too)!)
   *  snap          + +
   * 
   * 
   *  Profiles on oldBranch will also be deleted. Two new (DoppelTrapez)profiles will be added to
   *  the start and end of meandering measure geometry (branch - part).
   * </pre>
   * 
   * @throws Exception
   * @see org.kalypso.nofdpidss.ui.view.interactiveplanning.main.hydraulic.computation.measures.IHydraulicMeasure#modifyFlowNetwork()
   */
    public void modifyFlowNetwork(final IHydraulModel model, final IProgressMonitor monitor, final MyPrintStream outputStream) throws Exception {
        ConsoleHelper.writeLine(outputStream, String.format(Messages.HydraulicMeasureMeandering_3));
        final ISobekModelMember sobek = model.getSobekModelMember();
        final IMeasureMeandering meandering = getMeandering();
        final IBranch branch = HCUtils.determineBranch(sobek.getBranchMembers(), meandering.getBranch().getFeature());
        if (branch == null) throw HCExHelper.getCoreException(IStatus.ERROR, getMeasure(), Messages.HydraulicMeasureMeandering_0 + meandering.getBranch().getId());
        final GM_Curve gmMeandering = meandering.getGeometry();
        final LineString jtsMeandering = (LineString) JTSAdapter.export(gmMeandering);
        final GM_Curve gmBranch = branch.getCurve();
        final LineString jtsBranch = (LineString) JTSAdapter.export(gmBranch);
        final GM_Point gmUpperSnapPoint = meandering.getUpperSnapPoint();
        final Point jtsUpperSnapPoint = (Point) JTSAdapter.export(gmUpperSnapPoint);
        final GM_Point gmLowerSnapPoint = meandering.getLowerSnapPoint();
        final Point jtsLowerSnapPoint = (Point) JTSAdapter.export(gmLowerSnapPoint);
        final LineString jtsBranchNew = MeanderingUtil.determineNewBranchLineString(jtsBranch, jtsMeandering, jtsUpperSnapPoint, jtsLowerSnapPoint);
        final LineString jtsBranchObsolete = MeanderingUtil.determineObsoleteBranchLineString(jtsBranch, jtsMeandering, jtsUpperSnapPoint, jtsLowerSnapPoint);
        FeatureUtils.updateProperty(model.getWorkspace(), branch.getFeature(), ISobekConstants.QN_HYDRAULIC_BRANCH_RIVER_LINE, JTSAdapter.wrap(jtsBranch));
        final IVariant variant = getVariantResultMember().getLinkedVariant(getProvider().getProjectModel());
        final IMeasure[] measures = variant.getMeasures();
        ConsoleHelper.writeLine(outputStream, String.format(Messages.HydraulicMeasureMeandering_4));
        for (final IMeasure measure : measures) {
            if (measure.equals(getMeasure())) continue;
            if (isBranchRelevant(measure)) if (measure.relaysOnGeometry(jtsBranchObsolete)) {
                m_invalidMeasures.add(measure);
                ConsoleHelper.writeLine(outputStream, String.format(Messages.HydraulicMeasureMeandering_5, measure.getName()));
            }
            final INode[] nodes = sobek.getNodeMembers();
            for (final INode node : nodes) {
                if (node.relaysOnGeometry(jtsBranchObsolete)) {
                    try {
                        FeatureUtils.deleteFeature(model.getWorkspace(), node.getFeature());
                    } catch (final Exception e) {
                        throw ExceptionHelper.getCoreException(IStatus.ERROR, getClass(), Messages.HydraulicMeasureMeandering_1 + node.getName());
                    }
                }
            }
        }
        final Point p1 = JTSUtilities.pointOnLine(jtsBranchNew, BUFFER_PROFILE);
        final Point p2 = JTSUtilities.pointOnLine((LineString) JTSUtilities.invert(jtsBranchNew), BUFFER_PROFILE);
        final IProfil profil1 = ProfileBuilder.createVerticalProfile(p1, jtsBranchNew, m_handler, getMeandering().getBottomLevelUpStream(), getMeandering().getBottomLevelDownStream());
        final IProfil profil2 = ProfileBuilder.createVerticalProfile(p2, jtsBranchNew, m_handler, getMeandering().getBottomLevelUpStream(), getMeandering().getBottomLevelDownStream());
        ProfileHelper.setMarkers(profil1);
        ProfileHelper.setMarkers(profil2);
        final Feature root = model.getWorkspace().getRootFeature();
        final Feature waterbody = FeatureHelper.addFeature(root, IWaterBodies.QN_HYDRAULIC_WATER_BODY_MEMBER, IWaterBody.QN_HYDRAULIC_WATER_BODY);
        ConsoleHelper.writeLine(outputStream, String.format(Messages.HydraulicMeasureMeandering_6));
        addProfile(sobek, branch, waterbody, profil1, p1);
        addProfile(sobek, branch, waterbody, profil2, p2);
    }

    private void addProfile(final ISobekModelMember model, final IBranch branch, final Feature waterbody, final IProfil profil, final Point point) throws Exception {
        final GM_Point gm = (GM_Point) JTSAdapter.wrap(point);
        final Feature profileFeature = FeatureHelper.addFeature(waterbody, IWaterBody.QN_HYDRAULIC_PROFILE_MEMBER, GmlConstants.QN_PROFILE);
        RoughnessHelper.updateRoughnessesOnDoppelTrapezMember(getMeandering(), profil);
        ProfileFeatureFactory.toFeature(profil, profileFeature);
        final ICrossSectionNode csn = (ICrossSectionNode) FNGmlUtils.createNode(model, TYPE.eCrossSectionNode, gm, new INode[] {});
        FeatureUtils.setInternalLinkedFeature(model.getWorkspace(), csn.getFeature(), ISobekConstants.QN_LN_LINKS_TO_BRANCH, branch.getFeature());
        FeatureUtils.setInternalLinkedFeature(model.getWorkspace(), csn.getFeature(), ISobekConstants.QN_HYDRAULIC_CROSS_SECTION_NODE_LINKED_PROFILE, profileFeature);
    }

    /**
   * IMeasureMeandering Hydraulic computations modifies the relating IBranch.<br>
   * If a relevant measure lays on the modified branch part this measure can't be applied
   * 
   * @param measure
   * @return
   */
    private boolean isBranchRelevant(final IMeasure measure) {
        if (measure instanceof IMeasurePolder) return true; else if (measure instanceof IMeasureRetardingBasin) return true; else if (measure instanceof IMeasureWeir) return true; else if (measure instanceof IMeasureFloodplainLowering) return true; else if (measure instanceof IMeasureBankRecessionAndFillUp) return true; else if (measure instanceof IMeasureBottomLevelChange) return true; else if (measure instanceof IMeasureDiversion) return true; else if (measure instanceof IMeasureMeandering) return true;
        return false;
    }

    public void modifyDEM(final IFile iDgm) throws CoreException {
        try {
            final IMeasureMeandering measure = getMeandering();
            if (!measure.isAffectingDEM()) return;
            final URL gmlUrl = ResourceUtilities.createURL(iDgm);
            final GMLWorkspace workspace = GmlSerializer.createGMLWorkspace(gmlUrl, null);
            final ICoverageCollection coverages = (ICoverageCollection) workspace.getRootFeature().getAdapter(ICoverageCollection.class);
            if (coverages == null || coverages.size() == 0) return;
            if (coverages.size() > 1) throw ExceptionHelper.getCoreException(IStatus.ERROR, this.getClass(), Messages.HydraulicMeasureMeandering_2);
            final ICoverage coverage = coverages.get(0);
            if (coverage == null) return;
            final ICoverageModifier modifier = new MeanderingDgmModifier(measure);
            modifier.engage(coverage);
        } catch (final Exception ex) {
            throw new CoreException(StatusUtilities.statusFromThrowable(ex));
        }
    }

    public IMeasure[] getInvalidMeasures() {
        return m_invalidMeasures.toArray(new IMeasure[] {});
    }
}
