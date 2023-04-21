package uk.ac.ebi.intact.application.dataConversion.psiDownload.xmlGenerator.psi2;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.w3c.dom.Element;
import uk.ac.ebi.intact.application.dataConversion.PsiVersion;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.PsiDownloadTest;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.UserSessionDownload;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.model.TestableProtein;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.xmlGenerator.AbstractAnnotatedObject2xml;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.xmlGenerator.psi1.AnnotatedObject2xmlPSI1;
import uk.ac.ebi.intact.model.*;

/**
 * TODO document this ;o)
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id:AnnotatedObject2xmlPSI2Test.java 5298 2006-07-07 09:35:05 +0000 (Fri, 07 Jul 2006) baranda $
 */
public class AnnotatedObject2xmlPSI2Test extends PsiDownloadTest {

    /**
     * Returns this test suite. Reflection is used here to add all the testXXX() methods to the suite.
     */
    public static Test suite() {
        return new TestSuite(AnnotatedObject2xmlPSI2Test.class);
    }

    private Protein createProtein() {
        String sequence = "MSEPEVPFKVVAQFPYKSDYEDDLNFEKDQEIIVTSVEDAEWYFGEYQDSNGDVIEGIFP" + "KSFVAVQGSEVGKEAESSPNTGSTEQRTIQPEVEQKDLPEPISPETKKETLSGPVPVPAA" + "TVPVPAATVPVPAATAVSAQVQHDSSSGNGERKVPMDSPKLKARLSMFNQDITEQVPLPK" + "STHLDLENIPVKKTIVADAPKYYVPPGIPTNDTSNLERKKSLKENEKKIVPEPINRAQVE" + "SGRIETENDQLKKDLPQMSLKERIALLQEQQRLQAAREEELLRKKAKLEQEHERSAVNKN" + "EPYTETEEAEENEKTEPKPEFTPETEHNEEPQMELLAHKEITKTSREADEGTNDIEKEQF" + "LDEYTKENQKVEESQADEARGENVAEESEIGYGHEDREGDNDEEKEEEDSEENRRAALRE" + "RMAKLSGASRFGAPVGFNPFGMASGVGNKPSEEPKKKQHKEKEEEEPEQLQELPRAIPVM" + "PFVDPSSNPFFRKSNLSEKNQPTETKTLDPHATTEHEQKQEHGTHAYHNLAAVDNAHPEY" + "SDHDSDEDTDDHEFEDANDGLRKHSMVEQAFQIGNNESENVNSGEKIYPQEPPISHRTAE" + "VSHDIENSSQNTTGNVLPVSSPQTRVARNGSINSLTKSISGENRRKSINEYHDTVSTNSS" + "ALTETAQDISMAAPAAPVLSKVSHPEDKVPPHPVPSAPSAPPVPSAPSVPSAPPVPPAPP" + "ALSAPSVPPVPPVPPVSSAPPALSAPSIPPVPPTPPAPPAPPAPLALPKHNEVEEHVKSS" + "APLPPVSEEYHPMPNTAPPLPRAPPVPPATFEFDSEPTATHSHTAPSPPPHQNVTASTPS" + "MMSTQQRVPTSVLSGAEKESRTLPPHVPSLTNRPVDSFHESDTTPKVASIRRSTTHDVGE" + "ISNNVKIEFNAQERWWINKSAPPAISNLKLNFLMEIDDHFISKRLHQKWVVRDFYFLFEN" + "YSQLRFSLTFNSTSPEKTVTTLQERFPSPVETQSARILDEYAQRFNAKVVEKSHSLINSH" + "IGAKNFVSQIVSEFKDEVIQPIGARTFGATILSYKPEEGIEQLMKSLQKIKPGDILVIRK" + "AKFEAHKKIGKNEIINVGMDSAAPYSSVVTDYDFTKNKFRVIENHEGKIIQNSYKLSHMK" + "SGKLKVFRIVARGYVGW";
        Protein protein = new TestableProtein("EBI-333333", owner, yeast, "bbc1_yeast", proteinType, sequence);
        protein.setFullName("Myosin tail region-interacting protein MTI1");
        protein.addXref(new InteractorXref(owner, uniprot, "P47068", null, null, identity));
        protein.addXref(new InteractorXref(owner, uniprot, "P47067", null, null, secondaryAc));
        protein.addXref(new InteractorXref(owner, uniprot, "Q8X1F4", null, null, secondaryAc));
        protein.addXref(new InteractorXref(owner, sgd, "S000003557", "BBC1", null, secondaryAc));
        protein.addXref(new InteractorXref(owner, go, "GO:0030479", "C:actin cortical patch (sensu Fungi)", null, null));
        protein.addXref(new InteractorXref(owner, go, "GO:0017024", "F:myosin I binding", null, null));
        protein.addXref(new InteractorXref(owner, go, "GO:0030036", "P:actin cytoskeleton organization and biogenesis", null, null));
        protein.addXref(new InteractorXref(owner, go, "GO:0007010", "P:cytoskeleton organization and biogenesis", null, null));
        protein.addAlias(new InteractorAlias(owner, protein, geneName, "BBC1"));
        protein.addAlias(new InteractorAlias(owner, protein, geneNameSynonym, "MTI1"));
        protein.addAlias(new InteractorAlias(owner, protein, locusName, "YJL020C/YJL021C"));
        protein.addAlias(new InteractorAlias(owner, protein, orfName, "J1305/J1286"));
        protein.addAnnotation(new Annotation(owner, comment, "an interresting comment."));
        protein.addAnnotation(new Annotation(owner, remark, "an interresting remark."));
        return protein;
    }

    private void testBuildNames_nullArguments(PsiVersion version) {
        UserSessionDownload session = new UserSessionDownload(version);
        Element parentElement = session.createElement("proteinInteractor");
        Element namesElement = null;
        AbstractAnnotatedObject2xml aao = null;
        if (version.equals(PsiVersion.VERSION_1)) {
            aao = new AnnotatedObject2xmlPSI1();
        } else if (version.equals(PsiVersion.VERSION_2)) {
            aao = new AnnotatedObject2xmlPSI2();
        } else {
            fail("Unsupported version of PSI");
        }
        try {
            namesElement = aao.createNames(session, parentElement, null);
            fail("giving a null AnnotatedObject should throw an exception");
        } catch (IllegalArgumentException e) {
        }
        assertNull(namesElement);
        Protein protein = createProtein();
        try {
            namesElement = aao.createNames(null, parentElement, protein);
            fail("giving a null session should throw an exception");
        } catch (IllegalArgumentException e) {
        }
        assertNull(namesElement);
        try {
            namesElement = aao.createNames(session, null, protein);
            fail("giving a null parent Element should throw an exception");
        } catch (IllegalArgumentException e) {
        }
        assertNull(namesElement);
    }

    public void testBuildNames_nullArguments_PSI2() {
        testBuildNames_nullArguments(PsiVersion.VERSION_2);
    }

    public void testBuildNames_protein_ok_PSI2() {
        UserSessionDownload session = new UserSessionDownload(PsiVersion.VERSION_2);
        AbstractAnnotatedObject2xml aao = new AnnotatedObject2xmlPSI2();
        Element parentElement = session.createElement("proteinInteractor");
        Protein protein = createProtein();
        Element namesElement = aao.createNames(session, parentElement, protein);
        assertNotNull(namesElement);
        assertEquals(1, parentElement.getChildNodes().getLength());
        Element _primaryRef = (Element) parentElement.getChildNodes().item(0);
        assertEquals(namesElement, _primaryRef);
        assertEquals("names", namesElement.getNodeName());
        assertEquals(6, namesElement.getChildNodes().getLength());
        assertHasShortlabel(namesElement, "bbc1_yeast");
        assertHasAlias(namesElement, "BBC1");
        assertHasAlias(namesElement, "MTI1");
        assertHasAlias(namesElement, "YJL020C/YJL021C");
        assertHasAlias(namesElement, "J1305/J1286");
        assertHasFullname(namesElement, "Myosin tail region-interacting protein MTI1");
    }

    public void testBuildNamesNoFullname_protein_ok_PSI2() {
        UserSessionDownload session = new UserSessionDownload(PsiVersion.VERSION_2);
        Element parentElement = session.createElement("proteinInteractor");
        Protein protein = createProtein();
        protein.setFullName(null);
        AbstractAnnotatedObject2xml aao = new AnnotatedObject2xmlPSI2();
        Element namesElement = aao.createNames(session, parentElement, protein);
        assertNotNull(namesElement);
        assertEquals(1, parentElement.getChildNodes().getLength());
        Element _primaryRef = (Element) parentElement.getChildNodes().item(0);
        assertEquals(namesElement, _primaryRef);
        assertEquals("names", namesElement.getNodeName());
        assertEquals(5, namesElement.getChildNodes().getLength());
        assertHasShortlabel(namesElement, "bbc1_yeast");
        assertHasAlias(namesElement, "BBC1");
        assertHasAlias(namesElement, "MTI1");
        assertHasAlias(namesElement, "YJL020C/YJL021C");
        assertHasAlias(namesElement, "J1305/J1286");
        assertEquals(0, namesElement.getElementsByTagName("fullname").getLength());
    }
}
