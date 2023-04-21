package edu.unc.med.lccc.tcgasra.main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import edu.unc.med.lccc.tcgasra.datalogic.ExperimentHibDAO;
import edu.unc.med.lccc.tcgasra.datalogic.SampleHibDAO;
import edu.unc.med.lccc.tcgasra.datalogic.StudyAttibuteHibDAO;
import edu.unc.med.lccc.tcgasra.datalogic.StudyHibDAO;
import edu.unc.med.lccc.tcgasra.datalogic.StudyLinkHibADO;
import edu.unc.med.lccc.tcgasra.datalogic.StudyTypeHibDAO;
import edu.unc.med.lccc.tcgasra.hibernateobj.Experiment;
import edu.unc.med.lccc.tcgasra.hibernateobj.Sample;
import edu.unc.med.lccc.tcgasra.hibernateobj.Study;
import edu.unc.med.lccc.tcgasra.hibernateobj.StudyAttribute;
import edu.unc.med.lccc.tcgasra.hibernateobj.StudyLink;
import edu.unc.med.lccc.tcgasra.util.Debug;
import edu.unc.med.lccc.tcgasra.xsdobj.study.*;

public class CreateStudyXML {

    public static void main(String[] args) throws DatatypeConfigurationException, JAXBException, IOException {
        int sample_id = 689;
        Integer sampleID = Integer.valueOf(sample_id);
        SampleHibDAO sampleHibDAO = new SampleHibDAO();
        Sample sample = sampleHibDAO.getSampleBySampleId(sampleID);
        Integer experimentID = sample.getExperimentId();
        ExperimentHibDAO experimentHibDAO = new ExperimentHibDAO();
        Experiment experiment = experimentHibDAO.getExperimentByExperimentId(experimentID);
        Integer studyID = experiment.getStudyId();
        String fileName = "study.xml";
        CreateStudyXML.createStudyXML(studyID, fileName);
        Debug.traceIn("Creating study xml file is", "Done");
    }

    public static void createStudyXML(Integer studyID, String fileName) throws JAXBException, IOException {
        StudyHibDAO studyHibDAO = new StudyHibDAO();
        Study study = studyHibDAO.getStudyByStudyId(studyID);
        Integer studyTypeID = study.getExistingType();
        StudyAttibuteHibDAO studyAttibuteHibDAO = new StudyAttibuteHibDAO();
        List study_attribute = studyAttibuteHibDAO.getStudyAttributeByStudyId(studyID);
        StudyTypeHibDAO studyTypeHibDAO = new StudyTypeHibDAO();
        edu.unc.med.lccc.tcgasra.hibernateobj.StudyType study_type = studyTypeHibDAO.getStudyTypeByStudyTypeId(studyTypeID);
        StudyLinkHibADO studyLinkHibADO = new StudyLinkHibADO();
        List study_link = studyLinkHibADO.getStudyLinkByStudyId(studyID);
        ObjectFactory of = new ObjectFactory();
        STUDYSET studySet = new STUDYSET();
        JAXBContext jaxbContext = JAXBContext.newInstance("edu.unc.med.lccc.tcgasra.xsdobj.study");
        StudyType studyType = new StudyType();
        studyType.setAccession(study.getAccession());
        studyType.setCenterName(study.getCenterName());
        studyType.setAlias(study.getCenterProjectName());
        StudyType.DESCRIPTOR studyType_descriptor = new StudyType.DESCRIPTOR();
        studyType.setDESCRIPTOR(studyType_descriptor);
        studyType.getDESCRIPTOR().setSTUDYTITLE(study.getTitle());
        studyType.getDESCRIPTOR().setSTUDYDESCRIPTION(study.getDescription());
        studyType.getDESCRIPTOR().setCENTERPROJECTNAME(study.getCenterProjectName());
        studyType.getDESCRIPTOR().setPROJECTID(BigInteger.valueOf(study.getProjectId()));
        studyType.getDESCRIPTOR().setCENTERNAME(study.getCenterName());
        JAXBElement<String> studyAbstract = of.createStudyTypeDESCRIPTORSTUDYABSTRACT(study.getAbstract_());
        studyType.getDESCRIPTOR().setSTUDYABSTRACT(studyAbstract);
        studyType.getDESCRIPTOR().getSTUDYABSTRACT().setNil(true);
        studyType.getDESCRIPTOR().getSTUDYABSTRACT().setValue(study.getAbstract_());
        StudyType.DESCRIPTOR.STUDYTYPE studyType_descriptor_studyType = new StudyType.DESCRIPTOR.STUDYTYPE();
        studyType_descriptor_studyType.setExistingStudyType(study_type.getName());
        studyType_descriptor_studyType.setNewStudyType(study.getNewType());
        studyType.getDESCRIPTOR().setSTUDYTYPE(studyType_descriptor_studyType);
        StudyType.DESCRIPTOR.RELATEDSTUDIES studyType_descriptor_relatedStudies = new StudyType.DESCRIPTOR.RELATEDSTUDIES();
        studyType.getDESCRIPTOR().setRELATEDSTUDIES(studyType_descriptor_relatedStudies);
        StudyType.STUDYATTRIBUTES studyType_studyAttributes = new StudyType.STUDYATTRIBUTES();
        AttributeType attributeType = null;
        for (int i = 0; i < study_attribute.size(); i++) {
            attributeType = new AttributeType();
            StudyAttribute studyAttribute_obj = (StudyAttribute) study_attribute.get(i);
            attributeType.setTAG(studyAttribute_obj.getTag());
            attributeType.setUNITS(studyAttribute_obj.getUnits());
            attributeType.setVALUE(studyAttribute_obj.getValue());
            studyType_studyAttributes.getSTUDYATTRIBUTE().add(attributeType);
        }
        if (study_attribute.size() > 0) {
            studyType.setSTUDYATTRIBUTES(studyType_studyAttributes);
        }
        StudyType.STUDYLINKS studyType_studyLinks = new StudyType.STUDYLINKS();
        studyType.setSTUDYLINKS(studyType_studyLinks);
        StudyLink studyLinkTemp = null;
        for (int i = 0; i < study_link.size(); i++) {
            LinkType linkType2 = new LinkType();
            LinkType.URLLINK linkType_urllink = null;
            LinkType.XREFLINK linkType_xrfflink = null;
            LinkType.ENTREZLINK linkType_entrezlink = null;
            studyLinkTemp = (StudyLink) study_link.get(i);
            if (studyLinkTemp.getType().equalsIgnoreCase("XREF_LINK")) {
                linkType_xrfflink = new LinkType.XREFLINK();
                linkType2.setXREFLINK(linkType_xrfflink);
                linkType2.getXREFLINK().setDB(studyLinkTemp.getDb());
                linkType2.getXREFLINK().setID(studyLinkTemp.getId());
                linkType2.getXREFLINK().setLABEL(studyLinkTemp.getLabel());
            } else if (studyLinkTemp.getType().equalsIgnoreCase("ENTREZ_LINK")) {
                linkType_entrezlink = new LinkType.ENTREZLINK();
                linkType2.setENTREZLINK(linkType_entrezlink);
                linkType2.getENTREZLINK().setQUERY(studyLinkTemp.getQuery());
                linkType2.getENTREZLINK().setDB(studyLinkTemp.getDb());
                linkType2.getENTREZLINK().setID(new BigInteger(studyLinkTemp.getId()));
                linkType2.getENTREZLINK().setLABEL(studyLinkTemp.getLabel());
            } else if (studyLinkTemp.getType().equalsIgnoreCase("URL_LINK")) {
                linkType_urllink = new LinkType.URLLINK();
                linkType2.setURLLINK(linkType_urllink);
                linkType2.getURLLINK().setLABEL(studyLinkTemp.getLabel());
                linkType2.getURLLINK().setURL(studyLinkTemp.getUrl());
            }
            studyType.getSTUDYLINKS().getSTUDYLINK().add(linkType2);
        }
        PrintWriter writer = new PrintWriter(new FileWriter(fileName, false));
        Marshaller marshaller = jaxbContext.createMarshaller();
        studySet.getSTUDY().add(studyType);
        JAXBElement<StudyType> studyElement = of.createSTUDY(studyType);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(studyElement, writer);
    }
}
