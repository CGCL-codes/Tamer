package org.mitre.caasd.aixmj.feature.navaid;

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;
import org.mitre.caasd.aixmj.data.AixmCodeLandingAidCategoryType;
import org.mitre.caasd.aixmj.data.AixmCodeNavaidServiceType;
import org.mitre.caasd.aixmj.data.AixmCodeStatusNavaidType;
import org.mitre.caasd.aixmj.data.AixmCodeYesNoType;
import org.mitre.caasd.aixmj.data.AixmConstants;
import org.mitre.caasd.aixmj.data.ObjectHash;
import org.mitre.caasd.aixmj.feature.general.AixmElevatedPoint;
import org.mitre.caasd.aixmj.feature.general.AixmFeature;
import org.mitre.caasd.aixmj.feature.general.AixmFeatureType;
import org.mitre.caasd.aixmj.feature.timeslice.AixmTimeSlice;
import org.mitre.caasd.aixmj.util.AixmUtil;

/**
 * One or more Navaid Equipment providing navigation services.  
 * The Navaid Equipment share business rules like paired frequencies.
 * 
 * @author SCHASE
 *
 */
public class AixmNavaidTimeSlice extends AixmTimeSlice {

    /**
     * Type of the navaid service such as ILS, MLS, VORTAC, VOR/DME, etc.
     */
    private AixmCodeNavaidServiceType type;

    /**
     * The coded identifier given to the navaid system.
     */
    private String designator;

    /**
     * The long name given to the composite navaid.
     */
    private String name;

    /**
     * A code indicating the landing precision of a navaid when used as a landing system.
     */
    private AixmCodeLandingAidCategoryType landingCategory;

    /**
     * Indicates the state of the navaid.
     */
    private AixmCodeStatusNavaidType operationalStatus;

    /**
     * Indicates if the navaid has been flight checked.
     */
    private AixmCodeYesNoType flightChecked;

    /**
     * List of navaid equipment.
     */
    private List<AixmFeature> navaidEquipmentList;

    /**
     * This is the location used when the navaid acts as a significant point.  This location is usually the location of one of the navaid equipments.
     */
    private AixmElevatedPoint location;

    /**
     * Indicates that the navaid is installed at a particular landing area. Typically used for ILS and MLS systems.
     */
    private AixmFeature runwayDirection;

    public AixmNavaidTimeSlice parseElements(Element element) {
        String gmlId = element.attributeValue("id");
        Object o = ObjectHash.getObject(gmlId);
        if (o != null) return (AixmNavaidTimeSlice) o;
        this.setGmlId(gmlId);
        this.designator = AixmUtil.parseString("designator", element);
        this.name = AixmUtil.parseString("name", element);
        this.type = (AixmCodeNavaidServiceType) AixmUtil.parseEnum("type", element, AixmCodeNavaidServiceType.ARSR);
        this.landingCategory = (AixmCodeLandingAidCategoryType) AixmUtil.parseEnum("landingCategory", element, AixmCodeLandingAidCategoryType.I);
        this.operationalStatus = (AixmCodeStatusNavaidType) AixmUtil.parseEnum("operationalStatus", element, AixmCodeStatusNavaidType.CONDITIONAL);
        this.flightChecked = (AixmCodeYesNoType) AixmUtil.parseEnum("flightChecked", element, AixmCodeYesNoType.NO);
        this.location = (AixmElevatedPoint) AixmUtil.parseObject(new AixmElevatedPoint(), element, "hasNavigableLocation", "ElevatedPoint");
        this.navaidEquipmentList = AixmUtil.parseFeatureList(AixmFeatureType.NavaidEquipment, element, "isComposedOf", "NavaidEquipment");
        Element e = element.element(QName.get("isInstalledAt", AixmConstants.AIXM_NAMESPACE));
        if (e != null && e.hasContent()) {
            Element e2 = e.element(QName.get("RunwayDirection", AixmConstants.AIXM_NAMESPACE));
            if (e2 != null && e2.hasContent()) {
                this.runwayDirection = new AixmFeature(AixmFeatureType.RunwayDirection).parseElements(e2);
            } else {
                e2 = e.element(QName.get("TouchDownLiftOff", AixmConstants.AIXM_NAMESPACE));
                if (e2 != null && e2.hasContent()) {
                }
            }
        }
        return this;
    }

    public Element constructElements(Element element, Document d) {
        AixmUtil.constructSingleElement(this.designator, element, "designator");
        AixmUtil.constructSingleElement(this.name, element, "name");
        AixmUtil.constructSingleElement(this.type, element, "type");
        AixmUtil.constructSingleElement(this.landingCategory, element, "landingCategory");
        AixmUtil.constructSingleElement(this.operationalStatus, element, "operationalStatus");
        AixmUtil.constructSingleElement(this.flightChecked, element, "flightChecked");
        AixmUtil.constructContainerElement(this.location, element, d, "hasNavigableLocation", "ElevatedPoint");
        AixmUtil.constructMultipleElements(this.navaidEquipmentList, element, d, "isComposedOf", "NavaidEquipment");
        AixmUtil.constructContainerElement(this.runwayDirection, element, d, "isInstalledAt", "RunwayDirection");
        return element;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NavaidTimeSlice: gmlId=" + super.getGmlId() + "\n");
        sb.append("validTime : " + super.getValidTime() + "\n");
        sb.append("type: " + type);
        sb.append("\n");
        sb.append("designator: " + designator);
        sb.append("\n");
        sb.append("full name : " + name);
        sb.append("\n");
        sb.append("category: " + landingCategory);
        sb.append("\n");
        sb.append("operational status: " + operationalStatus);
        sb.append("\n");
        sb.append("flightChecked: " + flightChecked);
        sb.append("\n");
        sb.append("equipmentList: " + navaidEquipmentList);
        sb.append("\n");
        sb.append("location: " + location);
        sb.append("\n");
        sb.append("runwayDirection: " + runwayDirection);
        return sb.toString();
    }

    /**
     * @return the designator
     */
    public String getDesignator() {
        return designator;
    }

    /**
     * @param designator the designator to set
     */
    public void setDesignator(String designator) {
        this.designator = designator;
    }

    /**
     * @return the flightChecked
     */
    public AixmCodeYesNoType getFlightChecked() {
        return flightChecked;
    }

    /**
     * @param flightChecked the flightChecked to set
     */
    public void setFlightChecked(AixmCodeYesNoType flightChecked) {
        this.flightChecked = flightChecked;
    }

    /**
     * @return the landingCategory
     */
    public AixmCodeLandingAidCategoryType getLandingCategory() {
        return landingCategory;
    }

    /**
     * @param landingCategory the landingCategory to set
     */
    public void setLandingCategory(AixmCodeLandingAidCategoryType landingCategory) {
        this.landingCategory = landingCategory;
    }

    /**
     * @return the operationalStatus
     */
    public AixmCodeStatusNavaidType getOperationalStatus() {
        return operationalStatus;
    }

    /**
     * @param operationalStatus the operationalStatus to set
     */
    public void setOperationalStatus(AixmCodeStatusNavaidType operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    /**
     * @return the type
     */
    public AixmCodeNavaidServiceType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(AixmCodeNavaidServiceType type) {
        this.type = type;
    }

    /**
     * @return the location
     */
    public AixmElevatedPoint getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(AixmElevatedPoint location) {
        this.location = location;
    }

    /**
     * @return the navaidEquipmentList
     */
    public List<AixmFeature> getNavaidEquipmentList() {
        return navaidEquipmentList;
    }

    /**
     * @param navaidEquipmentList the navaidEquipmentList to set
     */
    public void setNavaidEquipmentList(List<AixmFeature> navaidEquipmentList) {
        this.navaidEquipmentList = navaidEquipmentList;
    }

    public void addNavaidEquipment(AixmFeature ane) {
        if (this.navaidEquipmentList == null) {
            this.navaidEquipmentList = new ArrayList<AixmFeature>();
        }
        navaidEquipmentList.add(ane);
    }

    /**
     * @return the runway
     */
    public AixmFeature getRunwayDirection() {
        return runwayDirection;
    }

    /**
     * @param runway the runway to set
     */
    public void setRunwayDirection(AixmFeature runwayDirection) {
        this.runwayDirection = runwayDirection;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
