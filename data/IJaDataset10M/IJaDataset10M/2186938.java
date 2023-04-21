package org.fudaa.dodico.crue.io;

import java.util.List;
import org.fudaa.ctulu.CtuluLog;
import org.fudaa.dodico.crue.common.DateDurationConverter;
import org.fudaa.dodico.crue.io.common.ContexteSimulation;
import org.fudaa.dodico.crue.io.common.CrueFileType;
import org.fudaa.dodico.crue.io.rptg.RPTGFile;
import org.fudaa.dodico.crue.io.rptg.RPTGFile.Branches;
import org.fudaa.dodico.crue.io.rptg.RPTGFile.Casiers;
import org.fudaa.dodico.crue.io.rptg.RPTGFile.ResultatsPrtGeo;
import org.fudaa.dodico.crue.io.rptg.RPTGFile.Sections;
import org.fudaa.dodico.crue.io.rptg.RPTGFile.SectionsVariables;
import org.fudaa.dodico.crue.projet.coeur.CoeurConfigContrat;
import org.fudaa.dodico.crue.projet.coeur.TestCoeurConfig;

public class TestCrueRPTG extends AbstractIOTestCase {

    protected static final String FICHIER_TEST_XML = "/v1_2/M3-0_c10.rptg.xml";

    public TestCrueRPTG() {
        super(Crue10FileFormatFactory.getVersion(CrueFileType.RPTG, TestCoeurConfig.INSTANCE), FICHIER_TEST_XML);
    }

    public void testLecture() {
        final RPTGFile donnees = readData(FICHIER_TEST_XML, TestCoeurConfig.INSTANCE);
        assertEquals("HelloWorld", donnees.getCommentaire());
        this.testContexte(donnees.getContextSimulation());
        this.testNoeuds(donnees.getNoeuds());
        this.testCasiers(donnees.getCasiers());
        this.testSections(donnees.getSections());
        this.testBranches(donnees.getBranches());
        this.testResultatsPrtGeo(donnees.getResultatsPrtGeo());
    }

    private void testContexte(ContexteSimulation contexte) {
        assertEquals("2005-03-28T13:24:36.000", DateDurationConverter.dateToXsd(contexte.getDateSimulation()));
        assertEquals("10.0.0", contexte.getVersion());
        assertEquals("EtuEx.etu.xml", contexte.getEtude());
        assertEquals("Sc_M3-0_c10", contexte.getScenario());
        assertEquals("RUN20110419155803", contexte.getRun());
        assertEquals("Mo_M3-0_c10", contexte.getModele());
    }

    private void testNoms(String[] noms, List<String> listeNoms) {
        assertEquals(noms.length, listeNoms.size());
        for (int i = 0; i < noms.length; i++) {
            assertEquals(noms[i], listeNoms.get(i));
        }
    }

    private void testNoeuds(List<String> noeuds) {
        this.testNoms(new String[] { "Nd_N1", "Nd_N2", "Nd_N6", "Nd_N3", "Nd_N4", "Nd_N7", "Nd_N5" }, noeuds);
    }

    private void testCasiers(Casiers casiers) {
        assertEquals(2, casiers.getVariablesRes().size());
        assertEquals("Zhaut", casiers.getVariablesRes().get(0).getNom());
        assertEquals(true, casiers.getVariablesRes().get(0).isActive());
        assertEquals(false, casiers.getVariablesRes().get(1).isActive());
        assertEquals(2, casiers.getVariablesResLoi().size());
        assertEquals("LoiZVol", casiers.getVariablesResLoi().get(1).getNom());
        assertEquals(false, casiers.getVariablesResLoi().get(0).isActive());
        assertEquals(true, casiers.getVariablesResLoi().get(1).isActive());
        assertEquals(new Integer(50), casiers.getVariablesResLoi().get(0).getNbrElem());
        assertEquals(new Integer(50), casiers.getVariablesResLoi().get(1).getNbrElem());
        this.testNoms(new String[] { "Ca_N6", "Ca_N7" }, casiers.getCasiersProfil());
    }

    private void testSections(Sections sections) {
        this.testSectionsVariables(sections.getSectionsIdem(), 5, "FenteTxD", 12, "LstLitKsup", new String[] { "St_PROF3AM", "St_PROF6B" });
        this.testSectionsVariables(sections.getSectionsInterpolee(), 4, "FenteTxD", 5, "LoiZBeta", new String[] { "St_B1_00050", "St_B1_00150", "St_B1_00250", "St_B1_00350", "St_B1_00450", "St_PROF5" });
        this.testSectionsVariables(sections.getSectionsProfil(), 5, "FenteTxD", 12, "LstLitKsup", new String[] { "St_PROF1", "St_PROF10", "St_Prof11", "St_PROF2", "St_PROF3A", "St_PROF3AV", "St_PROF3B", "St_PROF4", "St_PROF6A", "St_PROF7", "St_PROF8", "St_PROF9", "St_PROFSTR1", "St_PROFSTR2" });
        this.testNoms(new String[] { "St_B5_Amont", "St_B5_Aval", "St_B8_Amont", "St_B8_Aval" }, sections.getSectionsSansGeometrie());
    }

    private void testSectionsVariables(SectionsVariables sections, int nbVariablesRes, String firstVariablesRes, int nbVariablesResLoi, String firstVariablesResLoi, String[] noms) {
        assertEquals(nbVariablesRes, sections.getVariablesRes().size());
        assertEquals(firstVariablesRes, sections.getVariablesRes().get(0).getNom());
        assertEquals(nbVariablesResLoi, sections.getVariablesResLoi().size());
        assertEquals(firstVariablesResLoi, sections.getVariablesResLoi().get(0).getNom());
        this.testNoms(noms, sections.getSections());
    }

    private void testBranches(Branches branches) {
        this.testNoms(new String[] {}, branches.getBranchesBarrageFilEau());
        this.testNoms(new String[] {}, branches.getBranchesBarrageGenerique());
        this.testNoms(new String[] {}, branches.getBranchesNiveauxAssocies());
        this.testNoms(new String[] { "Br_B8" }, branches.getBranchesOrifice());
        this.testNoms(new String[] {}, branches.getBranchesPdc());
        this.testNoms(new String[] { "Br_B1", "Br_B2", "Br_B4" }, branches.getBranchesSaintVenant());
        this.testNoms(new String[] { "Br_B5" }, branches.getBranchesSeuilLateral());
        this.testNoms(new String[] { "Br_B3" }, branches.getBranchesSeuilTransversal());
        this.testNoms(new String[] { "Br_B6" }, branches.getBranchesStrickler());
    }

    private void testResultatsPrtGeo(ResultatsPrtGeo resultats) {
        assertEquals("M3-0_c10.rptg.bin", resultats.getHref());
        assertEquals(0, resultats.getOffset());
    }

    private static RPTGFile readData(String fichier, CoeurConfigContrat version) {
        final CtuluLog analyzer = new CtuluLog();
        final Crue10FileFormat<Object> fileFormat = Crue10FileFormatFactory.getInstance().getFileFormat(CrueFileType.RPTG, version);
        final RPTGFile jeuDonneesLue = (RPTGFile) fileFormat.read(fichier, analyzer, createDefault()).getMetier();
        testAnalyser(analyzer);
        return jeuDonneesLue;
    }
}
