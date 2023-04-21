package uk.ac.cisban.symba.backend.util.conversion.helper;

import com.ibm.lsid.LSIDException;
import fugeOM.Common.Protocol.*;
import fugeOM.service.RealizableEntityService;
import fugeOM.service.RealizableEntityServiceException;
import fugeOM.util.generatedJAXB2.FugeOMCollectionFuGEType;
import fugeOM.util.generatedJAXB2.FugeOMCommonProtocolGenericEquipmentType;
import fugeOM.util.generatedJAXB2.FugeOMCommonProtocolGenericParameterType;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

public class CisbanGenericEquipmentHelper {

    private final int NUMBER_ELEMENTS = 2;

    private final CisbanParameterHelper cp;

    private final CisbanSoftwareHelper cs;

    private final RealizableEntityService reService;

    public CisbanGenericEquipmentHelper(RealizableEntityService reService, CisbanIdentifiableHelper ci) {
        this.reService = reService;
        this.cp = new CisbanParameterHelper(reService, ci);
        this.cs = new CisbanSoftwareHelper(reService, ci);
    }

    public GenericEquipment unmarshalGenericEquipment(FugeOMCommonProtocolGenericEquipmentType genericEquipmentXML, GenericEquipment genericEquipment) throws URISyntaxException, RealizableEntityServiceException, LSIDException {
        Set<GenericParameter> genericParameters = new HashSet<GenericParameter>();
        for (FugeOMCommonProtocolGenericParameterType genericParameterXML : genericEquipmentXML.getGenericParameter()) {
            genericParameters.add((GenericParameter) cp.unmarshalParameter(genericParameterXML));
        }
        if (!genericParameters.isEmpty()) genericEquipment.setParameters(genericParameters);
        Set<GenericEquipment> genericEquipments = new HashSet<GenericEquipment>();
        for (FugeOMCommonProtocolGenericEquipmentType.GenericEquipmentParts referencedXML : genericEquipmentXML.getGenericEquipmentParts()) {
            genericEquipments.add((GenericEquipment) reService.findIdentifiable(referencedXML.getGenericEquipmentRef()));
        }
        if (!genericEquipments.isEmpty()) genericEquipment.setGenEquipParts(genericEquipments);
        Set<GenericSoftware> genericSoftwares = new HashSet<GenericSoftware>();
        for (FugeOMCommonProtocolGenericEquipmentType.Software referencedXML : genericEquipmentXML.getSoftware()) {
            genericSoftwares.add((GenericSoftware) reService.findIdentifiable(referencedXML.getGenericSoftwareRef()));
        }
        if (!genericSoftwares.isEmpty()) genericEquipment.setSoftware(genericSoftwares);
        return genericEquipment;
    }

    public FugeOMCommonProtocolGenericEquipmentType marshalGenericEquipment(FugeOMCommonProtocolGenericEquipmentType genericEquipmentXML, GenericEquipment genericEquipment) throws URISyntaxException, RealizableEntityServiceException {
        genericEquipment = (GenericEquipment) reService.greedyGet(genericEquipment);
        for (Object obj : genericEquipment.getParameters()) {
            GenericParameter parameter = (GenericParameter) obj;
            genericEquipmentXML.getGenericParameter().add((FugeOMCommonProtocolGenericParameterType) cp.marshalParameter(parameter));
        }
        for (Object obj : genericEquipment.getGenEquipParts()) {
            GenericEquipment equipment = (GenericEquipment) obj;
            FugeOMCommonProtocolGenericEquipmentType.GenericEquipmentParts parts = new FugeOMCommonProtocolGenericEquipmentType.GenericEquipmentParts();
            parts.setGenericEquipmentRef(equipment.getIdentifier());
            genericEquipmentXML.getGenericEquipmentParts().add(parts);
        }
        for (Object obj : genericEquipment.getSoftware()) {
            GenericSoftware software = (GenericSoftware) obj;
            FugeOMCommonProtocolGenericEquipmentType.Software softwareXML = new FugeOMCommonProtocolGenericEquipmentType.Software();
            softwareXML.setGenericSoftwareRef(software.getIdentifier());
            genericEquipmentXML.getSoftware().add(softwareXML);
        }
        return genericEquipmentXML;
    }

    public FugeOMCommonProtocolGenericEquipmentType generateRandomXML(FugeOMCommonProtocolGenericEquipmentType genericEquipmentXML, FugeOMCommonProtocolGenericEquipmentType partXML, FugeOMCollectionFuGEType frXML) {
        for (int i = 0; i < NUMBER_ELEMENTS; i++) {
            FugeOMCommonProtocolGenericParameterType parameterXML = new FugeOMCommonProtocolGenericParameterType();
            genericEquipmentXML.getGenericParameter().add((FugeOMCommonProtocolGenericParameterType) cp.generateRandomXML(parameterXML, frXML));
        }
        if (partXML != null) {
            FugeOMCommonProtocolGenericEquipmentType.GenericEquipmentParts parts = new FugeOMCommonProtocolGenericEquipmentType.GenericEquipmentParts();
            parts.setGenericEquipmentRef(partXML.getIdentifier());
            genericEquipmentXML.getGenericEquipmentParts().add(parts);
        }
        if (frXML.getProtocolCollection() != null) {
            for (int i = 0; i < NUMBER_ELEMENTS; i++) {
                FugeOMCommonProtocolGenericEquipmentType.Software softwareXML = new FugeOMCommonProtocolGenericEquipmentType.Software();
                softwareXML.setGenericSoftwareRef(frXML.getProtocolCollection().getSoftware().get(i).getValue().getIdentifier());
                genericEquipmentXML.getSoftware().add(softwareXML);
            }
        }
        return genericEquipmentXML;
    }

    public GenericEquipment getLatestVersion(GenericEquipment genericEquipment, boolean isLatestEquipment) throws RealizableEntityServiceException {
        genericEquipment = (GenericEquipment) reService.greedyGet(genericEquipment);
        Set<GenericParameter> set = new HashSet<GenericParameter>();
        for (Object obj : genericEquipment.getEquipmentParameters()) {
            set.add((GenericParameter) cp.getLatestVersion((Parameter) obj));
        }
        genericEquipment.setEquipmentParameters(set);
        Set<GenericEquipment> set1 = new HashSet<GenericEquipment>();
        for (Object obj : genericEquipment.getGenEquipParts()) {
            set1.add(getLatestVersion((GenericEquipment) obj, false));
        }
        genericEquipment.setGenEquipParts(set1);
        Set<GenericSoftware> set2 = new HashSet<GenericSoftware>();
        for (Object obj : genericEquipment.getSoftware()) {
            set2.add((GenericSoftware) cs.getLatestVersion((Software) obj));
        }
        genericEquipment.setSoftware(set2);
        return genericEquipment;
    }
}
