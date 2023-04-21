package org.kalypso.nofdpidss.core.base.gml.model.project.base;

import javax.xml.namespace.QName;
import org.eclipse.core.runtime.CoreException;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IAllowedString;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataModel;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public interface ISmallVegtationSuitabilityInputList extends Feature {

    public static final QName QN_VEGETATION_STRUCTURE = new QName(GmlConstants.NS_MEASURES, "vegStructureAttribMember");

    public static final QName QN_SALINITY = new QName(GmlConstants.NS_MEASURES, "salinityAttribMember");

    public static final QName QN_SOIL_NUTRIENT = new QName(GmlConstants.NS_MEASURES, "soilNutrientsAttribMember");

    public static final QName QN_SOIL_MOISTURE = new QName(GmlConstants.NS_MEASURES, "soilHumidityAttribMember");

    public IAllowedString getVegetationStructure(IGeodataModel model) throws CoreException;

    public IAllowedString getSalinity(IGeodataModel model) throws CoreException;

    public IAllowedString getSoilNutrient(IGeodataModel model) throws CoreException;

    public IAllowedString getSoilMoisture(IGeodataModel model) throws CoreException;
}
