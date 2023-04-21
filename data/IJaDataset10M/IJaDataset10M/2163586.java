package org.kalypso.nofdpidss.hydraulic.computation.processing.worker.map;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.kalypso.contribs.eclipse.core.runtime.ExceptionHelper;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.contribs.eclipse.jface.operation.ICoreRunnableWithProgress;
import org.kalypso.contribs.eclipse.ui.progress.ConsoleHelper;
import org.kalypso.contribs.java.io.MyPrintStream;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.WorkspaceSync;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.result.IVariantResultMember;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasure;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IProjectModel;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IVariant;
import org.kalypso.nofdpidss.hydraulic.computation.i18n.Messages;
import org.kalypso.nofdpidss.hydraulic.computation.processing.interfaces.ICalculationHandler;
import org.kalypso.nofdpidss.hydraulic.computation.processing.interfaces.IHydraulicCalculationCase;
import org.kalypso.ogc.gml.GisTemplateHelper;
import org.kalypso.template.gismapview.CascadingLayer;
import org.kalypso.template.gismapview.Gismapview;
import org.kalypso.template.gismapview.ObjectFactory;
import org.kalypso.template.gismapview.Gismapview.Layers;
import org.kalypso.template.types.StyledLayerType;
import org.kalypso.template.types.StyledLayerType.Property;
import org.kalypso.template.types.StyledLayerType.Style;
import org.kalypsodeegree.KalypsoDeegreePlugin;
import org.xml.sax.SAXException;

public class MapGenerator implements ICoreRunnableWithProgress {

    public static final String INUNDATION_DEPTH_RASTER_LAYER = "INUNDATION_DEPTH_RASTER";

    private static final String INUNDATION_DEPHT_LAYER = "INUNDATION_DEPHT";

    public static final String INUNDATION_AREA_LAYER = "INUNDATION_AREA";

    public static final String LAYER_ID = "layer_id";

    private static final String TEMPLATE_FLOW_NETWORK = "hydraulicComputation/templates/";

    private final IHydraulicCalculationCase m_calculationCase;

    private final MyPrintStream m_outputStream;

    private final IProjectModel m_projectModel;

    private final ICalculationHandler m_handler;

    protected IFile m_dgm = null;

    protected IFile m_dgmSld = null;

    private final boolean m_generateResultLayer;

    public MapGenerator(final IProjectModel projectModel, final IHydraulicCalculationCase calculationCase, final ICalculationHandler handler, final MyPrintStream outputStream, final boolean generateResultLayer) {
        m_projectModel = projectModel;
        m_calculationCase = calculationCase;
        m_handler = handler;
        m_outputStream = outputStream;
        m_generateResultLayer = generateResultLayer;
    }

    private JAXBElement<? extends StyledLayerType> getResultModel(final ObjectFactory factory) throws CoreException {
        final CascadingLayer cascadingResultLayer = new CascadingLayer();
        cascadingResultLayer.setVisible(true);
        cascadingResultLayer.setName(Messages.MapGenerator_4);
        cascadingResultLayer.setLinktype("gmt");
        cascadingResultLayer.setType("simple");
        cascadingResultLayer.setActuate("onRequest");
        final List<Property> inputProperties = cascadingResultLayer.getProperty();
        final Property p1 = new Property();
        p1.setName("showChildren");
        p1.setValue("true");
        inputProperties.add(p1);
        final Property p2 = new Property();
        p2.setName("deleteable");
        p2.setValue("false");
        inputProperties.add(p2);
        final List<JAXBElement<? extends StyledLayerType>> layers = cascadingResultLayer.getLayer();
        final StyledLayerType flowNetwork = new StyledLayerType();
        flowNetwork.setName(Messages.MapGenerator_0);
        flowNetwork.setVisible(true);
        flowNetwork.setLinktype("gmt");
        flowNetwork.setType("simple");
        flowNetwork.setHref("./resultDataFlowNetwork.gmt");
        flowNetwork.setActuate("onRequest");
        final List<Property> flowNetworkProperties = flowNetwork.getProperty();
        flowNetworkProperties.add(p1);
        flowNetworkProperties.add(p2);
        layers.add(factory.createLayer(flowNetwork));
        if (m_handler.createGeodataSets()) {
            layers.add(getInundationLayers(factory));
        }
        layers.add(getDGMLayer(factory));
        return factory.createCascadingLayer(cascadingResultLayer);
    }

    private JAXBElement<? extends StyledLayerType> getDGMLayer(final ObjectFactory factory) throws CoreException {
        final IFolder dgmFolder = m_calculationCase.getDemFolder();
        dgmFolder.accept(new IResourceVisitor() {

            public boolean visit(final IResource resource) throws CoreException {
                if (resource instanceof IFile) {
                    if (resource.getName().toLowerCase().endsWith(".gml")) {
                        m_dgm = (IFile) resource;
                        return true;
                    } else if (resource.getName().toLowerCase().endsWith(".sld")) {
                        m_dgmSld = (IFile) resource;
                        return true;
                    }
                }
                return true;
            }
        });
        final Property p1 = new Property();
        p1.setName("deleteable");
        p1.setValue("false");
        final StyledLayerType ascii = new StyledLayerType();
        ascii.setVisible(false);
        ascii.setName(Messages.MapGenerator_31);
        ascii.setFeaturePath("coverageMember");
        ascii.setLinktype("gml");
        ascii.setType("simple");
        ascii.setHref(String.format("./../%s/%s", dgmFolder.getName(), m_dgm.getName()));
        ascii.setActuate("onRequest");
        final List<Style> asciiStyles = ascii.getStyle();
        final Style asciiStyle = new StyledLayerType.Style();
        asciiStyle.setStyle("DGM");
        asciiStyle.setLinktype("sld");
        asciiStyle.setType("simple");
        asciiStyle.setHref(String.format("./../%s/%s", dgmFolder.getName(), m_dgmSld.getName()));
        asciiStyle.setActuate("onRequest");
        asciiStyles.add(asciiStyle);
        return factory.createLayer(ascii);
    }

    private JAXBElement<? extends StyledLayerType> getInundationLayers(final ObjectFactory factory) {
        final CascadingLayer cascadingResultLayer = new CascadingLayer();
        cascadingResultLayer.setVisible(true);
        cascadingResultLayer.setName(Messages.MapGenerator_42);
        cascadingResultLayer.setLinktype("gmt");
        cascadingResultLayer.setType("simple");
        cascadingResultLayer.setActuate("onRequest");
        final List<Property> inputProperties = cascadingResultLayer.getProperty();
        final Property p1 = new Property();
        p1.setName("showChildren");
        p1.setValue("true");
        inputProperties.add(p1);
        final Property p2 = new Property();
        p2.setName("deleteable");
        p2.setValue("false");
        inputProperties.add(p2);
        final List<JAXBElement<? extends StyledLayerType>> layers = cascadingResultLayer.getLayer();
        final StyledLayerType area = new StyledLayerType();
        area.setVisible(true);
        area.setName(Messages.MapGenerator_43);
        area.setFeaturePath("featureMember");
        area.setLinktype("shape");
        area.setType("simple");
        area.setHref("./../floodZones/inundationArea#" + KalypsoDeegreePlugin.getDefault().getCoordinateSystem());
        area.setActuate("onRequest");
        final List<Property> areaProperties = area.getProperty();
        areaProperties.add(p1);
        areaProperties.add(p2);
        final Property pArea = new Property();
        pArea.setName(LAYER_ID);
        pArea.setValue(INUNDATION_AREA_LAYER);
        areaProperties.add(pArea);
        final List<Style> areaStyles = area.getStyle();
        final Style areaStyle = new StyledLayerType.Style();
        areaStyle.setStyle("ide");
        areaStyle.setLinktype("sld");
        areaStyle.setType("simple");
        areaStyle.setHref("./../floodZones/inundationArea.sld");
        areaStyle.setActuate("onRequest");
        areaStyles.add(areaStyle);
        layers.add(factory.createLayer(area));
        final StyledLayerType depth = new StyledLayerType();
        depth.setVisible(true);
        depth.setName(Messages.MapGenerator_54);
        depth.setFeaturePath("featureMember");
        depth.setLinktype("shape");
        depth.setType("simple");
        depth.setHref("./../floodZones/inundationDepth#" + KalypsoDeegreePlugin.getDefault().getCoordinateSystem());
        depth.setActuate("onRequest");
        final List<Property> depthProperties = depth.getProperty();
        depthProperties.add(p1);
        depthProperties.add(p2);
        final Property pDepth = new Property();
        pDepth.setName(LAYER_ID);
        pDepth.setValue(INUNDATION_DEPHT_LAYER);
        depthProperties.add(pDepth);
        final List<Style> depthStyles = depth.getStyle();
        final Style depthStyle = new StyledLayerType.Style();
        depthStyle.setStyle("ide");
        depthStyle.setLinktype("sld");
        depthStyle.setType("simple");
        depthStyle.setHref("./../floodZones/inundationDepth.sld");
        depthStyle.setActuate("onRequest");
        depthStyles.add(depthStyle);
        layers.add(factory.createLayer(depth));
        final StyledLayerType ascii = new StyledLayerType();
        ascii.setVisible(true);
        ascii.setName(Messages.MapGenerator_65);
        ascii.setFeaturePath("coverageMember");
        ascii.setLinktype("gml");
        ascii.setType("simple");
        ascii.setHref("./../floodZones/floodZones.gml");
        ascii.setActuate("onRequest");
        final List<Style> asciiStyles = ascii.getStyle();
        final Style asciiStyle = new StyledLayerType.Style();
        asciiStyle.setStyle("Inundation Depth");
        asciiStyle.setLinktype("sld");
        asciiStyle.setType("simple");
        asciiStyle.setHref("./../floodZones/floodZonesAscii.sld");
        asciiStyle.setActuate("onRequest");
        asciiStyles.add(asciiStyle);
        final List<Property> depthRasterProperties = ascii.getProperty();
        depthRasterProperties.add(p1);
        depthRasterProperties.add(p2);
        final Property pDepthRaster = new Property();
        pDepthRaster.setName(LAYER_ID);
        pDepthRaster.setValue(INUNDATION_DEPTH_RASTER_LAYER);
        depthRasterProperties.add(pDepthRaster);
        layers.add(factory.createLayer(ascii));
        return factory.createCascadingLayer(cascadingResultLayer);
    }

    /**
   * input data layers, points to inputData.gmt (static)
   * 
   * @param templateFolder
   */
    private JAXBElement<StyledLayerType> getBaseModelLayer(final IFolder folder, final ObjectFactory factory, final IFolder templateFolder) throws CoreException, IOException, JAXBException, SAXException, ParserConfigurationException {
        final StyledLayerType layerInputData = new StyledLayerType();
        layerInputData.setName(Messages.MapGenerator_28);
        layerInputData.setVisible(false);
        layerInputData.setLinktype("gmt");
        layerInputData.setType("simple");
        layerInputData.setHref("./baseModel.gmt");
        layerInputData.setActuate("onRequest");
        final List<Property> properties = layerInputData.getProperty();
        final Property p1 = new Property();
        p1.setName("showChildren");
        p1.setValue("true");
        properties.add(p1);
        final Property p2 = new Property();
        p2.setName("deleteable");
        p2.setValue("false");
        properties.add(p2);
        final IVariantResultMember variantResultMember = m_calculationCase.getVariantResultMember();
        final IVariant variant = variantResultMember.getLinkedVariant(m_projectModel);
        final IMeasure[] measures = variant.getMeasures();
        final Gismapview mapview = factory.createGismapview();
        final Layers myLayers = new Layers();
        final List<JAXBElement<? extends StyledLayerType>> layers = myLayers.getLayer();
        for (final IMeasure measure : measures) {
            layers.add(getMeasureLayer(factory, measure));
        }
        final IFolder mapFolder = m_calculationCase.getMapFolder();
        final IFile iMap = mapFolder.getFile("baseModelMeasures.gmt");
        mapview.setLayers(myLayers);
        GisTemplateHelper.saveGisMapView(mapview, iMap, "UTF-8");
        return factory.createLayer(layerInputData);
    }

    private JAXBElement<? extends StyledLayerType> getMeasureLayer(final ObjectFactory factory, final IMeasure measure) {
        final CascadingLayer cascadingLayer = new CascadingLayer();
        cascadingLayer.setVisible(true);
        cascadingLayer.setName(measure.getName());
        cascadingLayer.setLinktype("gmt");
        cascadingLayer.setType("simple");
        cascadingLayer.setActuate("onRequest");
        final List<Property> properties = cascadingLayer.getProperty();
        final Property p1 = new Property();
        p1.setName("showChildren");
        p1.setValue("false");
        properties.add(p1);
        final Property p2 = new Property();
        p1.setName("deleteable");
        p1.setValue("false");
        properties.add(p2);
        final List<JAXBElement<? extends StyledLayerType>> layers = cascadingLayer.getLayer();
        final StyledLayerType layer = new StyledLayerType();
        layer.setName(measure.getName());
        layer.setVisible(true);
        layer.setFeaturePath(String.format("#fid#%s", measure.getId()));
        layer.setLinktype("gml");
        layer.setType("simple");
        layer.setHref("../workspace/project.gml");
        layer.setActuate("onRequest");
        layers.add(factory.createLayer(layer));
        return factory.createCascadingLayer(cascadingLayer);
    }

    public IStatus execute(final IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
        try {
            ConsoleHelper.writeLine(m_outputStream, String.format(Messages.MapGenerator_78));
            final IFolder mapFolder = m_calculationCase.getMapFolder();
            final IProject globalProject = NofdpCorePlugin.getProjectManager().getBaseProject();
            final IFolder templateFolder = globalProject.getFolder(TEMPLATE_FLOW_NETWORK);
            if (!templateFolder.exists()) throw ExceptionHelper.getCoreException(IStatus.ERROR, this.getClass(), Messages.MapGenerator_1);
            templateFolder.accept(new IResourceVisitor() {

                public boolean visit(final IResource resource) throws CoreException {
                    if (resource instanceof IFile) {
                        if (resource.getName().toLowerCase().endsWith(".gmt") || resource.getName().toLowerCase().endsWith(".sld")) resource.copy(mapFolder.getFullPath().append(resource.getName()), true, monitor);
                    }
                    return true;
                }
            });
            final ObjectFactory factory = new ObjectFactory();
            final Gismapview mapview = factory.createGismapview();
            final Layers myLayers = new Layers();
            final List<JAXBElement<? extends StyledLayerType>> layers = myLayers.getLayer();
            layers.add(getBaseModelLayer(mapFolder, factory, templateFolder));
            if (m_generateResultLayer == true) {
                layers.add(getResultModel(factory));
            }
            mapview.setLayers(myLayers);
            final IFile mapFile = mapFolder.getFile("map.gmt");
            GisTemplateHelper.saveGisMapView(mapview, mapFile, "UTF-8");
            WorkspaceSync.sync(mapFolder, IResource.DEPTH_INFINITE);
            ConsoleHelper.writeLine(m_outputStream, String.format(Messages.MapGenerator_81));
            ConsoleHelper.writeLine(m_outputStream, String.format(""));
            return StatusUtilities.createOkStatus(Messages.MapGenerator_83);
        } catch (final Exception e) {
            throw ExceptionHelper.getCoreException(IStatus.ERROR, this.getClass(), e.getMessage());
        }
    }
}
