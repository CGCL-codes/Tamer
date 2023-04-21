package org.kalypso.nofdpidss.report.worker.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.kalypso.core.jaxb.TemplateUtilitites;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.ogc.gml.FeatureUtils;
import org.kalypso.ogc.gml.GisTemplateHelper;
import org.kalypso.template.gismapview.Gismapview;
import org.kalypso.template.types.StyledLayerType;
import org.kalypso.template.types.StyledLayerType.Property;
import org.kalypsodeegree.model.feature.Feature;
import org.xml.sax.SAXException;

/**
 * @author Dirk Kuch
 */
public class ReportMapBuilder {

    private static final String IS_TEMPORARY_LAYER = "IS_TEMPORARY_LAYER";

    public static boolean build(final Feature[] measures, final IFile iMap, final IFile iTemplateMap) throws JAXBException, CoreException, SAXException, ParserConfigurationException, IOException {
        final Gismapview mapview = GisTemplateHelper.loadGisMapView(iTemplateMap);
        final List<JAXBElement<? extends StyledLayerType>> layers = mapview.getLayers().getLayer();
        final List<JAXBElement<? extends StyledLayerType>> myLayers = new ArrayList<JAXBElement<? extends StyledLayerType>>();
        for (final JAXBElement<? extends StyledLayerType> layer : layers) {
            final StyledLayerType slt = layer.getValue();
            final List<Property> properties = slt.getProperty();
            for (final Property property : properties) {
                if (IS_TEMPORARY_LAYER.equals(property.getName())) {
                    continue;
                }
            }
            myLayers.add(layer);
        }
        layers.clear();
        for (final Feature measure : measures) {
            final StyledLayerType slt = new StyledLayerType();
            slt.setVisible(true);
            slt.setFeaturePath("#fid#" + measure.getId());
            slt.setLinktype("gml");
            slt.setHref("project:/gmlBase/project.gml");
            slt.setName(FeatureUtils.getFeatureName(GmlConstants.NS_MEASURES, measure));
            slt.setTitle(FeatureUtils.getFeatureName(GmlConstants.NS_MEASURES, measure));
            final List<Property> properties = slt.getProperty();
            final Property property = new StyledLayerType.Property();
            property.setName(IS_TEMPORARY_LAYER);
            property.setValue(Boolean.TRUE.toString());
            properties.add(property);
            final JAXBElement<StyledLayerType> layerType = TemplateUtilitites.OF_GISMAPVIEW.createLayer(slt);
            layers.add(layerType);
        }
        layers.addAll(myLayers);
        GisTemplateHelper.saveGisMapView(mapview, iMap, "UTF-8");
        return true;
    }
}
