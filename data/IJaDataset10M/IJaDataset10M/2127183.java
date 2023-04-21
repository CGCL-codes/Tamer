package uk.ac.ebi.intact.application.dataConversion.psiDownload.xmlGenerator.psi25.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.w3c.dom.Element;
import uk.ac.ebi.intact.application.dataConversion.PsiVersion;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.UserSessionDownload;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.test.PsiDownloadTest;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.test.model.TestableBioSource;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.xmlGenerator.BioSource2xmlFactory;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.xmlGenerator.BioSource2xmlI;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.xmlGenerator.psi25.BioSource2xmlPSI25;
import uk.ac.ebi.intact.model.*;

/**
 * TODO document this ;o)
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: BioSource2xmlPSI25Test.java 4385 2005-10-24 17:40:30Z skerrien $
 */
public class BioSource2xmlPSI25Test extends PsiDownloadTest {

    /**
     * Returns this test suite. Reflection is used here to add all the testXXX() methods to the suite.
     */
    public static Test suite() {
        return new TestSuite(BioSource2xmlPSI25Test.class);
    }

    public void testBuildBioSource_nullArguments() {
        UserSessionDownload session = new UserSessionDownload(PsiVersion.getVersion2());
        Element parent = session.createElement("proteinInteractor");
        Element element = null;
        BioSource2xmlI bsi = BioSource2xmlFactory.getInstance(session);
        try {
            element = bsi.createOrganism(session, parent, null);
            fail("giving a null BioSource should throw an exception");
        } catch (IllegalArgumentException e) {
        }
        assertNull(element);
        BioSource bioSource = new BioSource(owner, "human", "9606");
        try {
            element = bsi.createOrganism(null, parent, bioSource);
            fail("giving a null session should throw an exception");
        } catch (IllegalArgumentException e) {
        }
        assertNull(element);
        try {
            element = bsi.createOrganism(session, null, bioSource);
            fail("giving a null parent Element should throw an exception");
        } catch (IllegalArgumentException e) {
        }
        assertNull(element);
    }

    public void testBuildOrganism_simple_ok() {
        UserSessionDownload session = new UserSessionDownload(PsiVersion.getVersion25());
        BioSource2xmlI bsi = BioSource2xmlFactory.getInstance(session);
        Element parent = session.createElement("interactor");
        BioSource bioSource = new BioSource(owner, "human", "9606");
        assertNotNull(bioSource);
        Element element = bsi.createOrganism(session, parent, bioSource);
        assertEquals(1, parent.getChildNodes().getLength());
        assertNotNull(element);
        assertEquals(1, parent.getElementsByTagName("organism").getLength());
        Element organism = (Element) parent.getElementsByTagName("organism").item(0);
        assertNotNull(organism);
        assertEquals(organism, element);
        assertEquals("organism", organism.getNodeName());
        assertEquals("9606", organism.getAttribute("ncbiTaxId"));
        assertEquals(1, organism.getChildNodes().getLength());
        assertEquals(1, organism.getElementsByTagName("names").getLength());
        Element names = (Element) organism.getElementsByTagName("names").item(0);
        assertEquals("names", names.getNodeName());
        assertNotNull(names);
        assertEquals(1, names.getChildNodes().getLength());
        assertHasShortlabel(names, "human");
    }

    public void testBuildHostOrganism_simple_ok() {
        UserSessionDownload session = new UserSessionDownload(PsiVersion.getVersion25());
        BioSource2xmlI bsi = BioSource2xmlFactory.getInstance(session);
        Element parent = session.createElement(BioSource2xmlPSI25.HOST_ORGANISM_PARENT_NODE);
        BioSource bioSource = new BioSource(owner, "human", "9606");
        assertNotNull(bioSource);
        Element element = bsi.createHostOrganism(session, parent, bioSource);
        assertEquals(1, parent.getChildNodes().getLength());
        assertNotNull(element);
        assertEquals(1, parent.getElementsByTagName("hostOrganism").getLength());
        Element organism = (Element) parent.getElementsByTagName("hostOrganism").item(0);
        assertNotNull(organism);
        assertEquals(organism, element);
        assertEquals("hostOrganism", organism.getNodeName());
        assertEquals("9606", organism.getAttribute("ncbiTaxId"));
        assertEquals(1, organism.getChildNodes().getLength());
        assertEquals(1, organism.getElementsByTagName("names").getLength());
        Element names = (Element) organism.getElementsByTagName("names").item(0);
        assertEquals("names", names.getNodeName());
        assertNotNull(names);
        assertEquals(1, names.getChildNodes().getLength());
        assertHasShortlabel(names, "human");
    }

    public void testBuildBioSource_complex_ok() {
        UserSessionDownload session = new UserSessionDownload(PsiVersion.getVersion25());
        BioSource2xmlI bsi = BioSource2xmlFactory.getInstance(session);
        Element parent = session.createElement("interactor");
        BioSource bioSource = new TestableBioSource("EBI-111", owner, "human", "9606");
        bioSource.setFullName("Homo Sapiens");
        assertNotNull(bioSource);
        CvAliasType aliasType = new CvAliasType(owner, "otherName");
        aliasType.addXref(new Xref(owner, psi, "MI:wxyz", null, null, identity));
        bioSource.addAlias(new Alias(owner, bioSource, aliasType, "homme"));
        CvCellType cellType = (CvCellType) createCvObject(CvCellType.class, "a_431", "Human epidermoid carcinoma", "MI:0001");
        assertNotNull(cellType);
        bioSource.setCvCellType(cellType);
        CvTissue tissue = (CvTissue) createCvObject(CvTissue.class, "brain", "Brain [cerebrum]", "MI:0002");
        assertNotNull(tissue);
        bioSource.setCvTissue(tissue);
        CvCompartment compartment = (CvCompartment) createCvObject(CvCompartment.class, "compartment", "compartment fullname", "MI:0003");
        assertNotNull(compartment);
        bioSource.setCvCompartment(compartment);
        Element element = bsi.createOrganism(session, parent, bioSource);
        assertEquals(1, parent.getChildNodes().getLength());
        assertNotNull(element);
        assertEquals(1, parent.getElementsByTagName("organism").getLength());
        Element organism = (Element) parent.getElementsByTagName("organism").item(0);
        assertNotNull(organism);
        assertEquals("organism", organism.getNodeName());
        assertEquals("9606", organism.getAttribute("ncbiTaxId"));
        assertEquals(4, organism.getChildNodes().getLength());
        assertEquals(4, organism.getElementsByTagName("names").getLength());
        Element names = (Element) organism.getElementsByTagName("names").item(0);
        assertNotNull(names);
        assertEquals(3, names.getChildNodes().getLength());
        assertHasShortlabel(names, "human");
        assertHasFullname(names, "Homo Sapiens");
        assertHasAlias(names, "homme", "otherName", "MI:wxyz");
        Element cellTypeElement = (Element) organism.getElementsByTagName("cellType").item(0);
        assertNotNull(cellTypeElement);
        names = (Element) cellTypeElement.getElementsByTagName("names").item(0);
        assertNotNull(names);
        assertEquals(2, names.getChildNodes().getLength());
        assertHasShortlabel(names, "a_431");
        assertHasFullname(names, "Human epidermoid carcinoma");
        Element xrefElement = (Element) cellTypeElement.getElementsByTagName("xref").item(0);
        assertHasPrimaryRef(xrefElement, "MI:0001", "psi-mi", null, null);
        Element tissueElement = (Element) organism.getElementsByTagName("tissue").item(0);
        assertNotNull(tissueElement);
        names = (Element) tissueElement.getElementsByTagName("names").item(0);
        assertNotNull(names);
        assertEquals(2, names.getChildNodes().getLength());
        assertHasShortlabel(names, "brain");
        assertHasFullname(names, "Brain [cerebrum]");
        xrefElement = (Element) tissueElement.getElementsByTagName("xref").item(0);
        assertHasPrimaryRef(xrefElement, "MI:0002", "psi-mi", null, null);
        Element compartmentElement = (Element) organism.getElementsByTagName("compartment").item(0);
        assertNotNull(compartmentElement);
        names = (Element) compartmentElement.getElementsByTagName("names").item(0);
        assertNotNull(names);
        assertEquals(2, names.getChildNodes().getLength());
        assertHasShortlabel(names, "compartment");
        assertHasFullname(names, "compartment fullname");
        xrefElement = (Element) compartmentElement.getElementsByTagName("xref").item(0);
        assertHasPrimaryRef(xrefElement, "MI:0003", "psi-mi", null, null);
    }
}
