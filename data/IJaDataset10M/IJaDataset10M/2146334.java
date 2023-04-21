package org.fudaa.dodico.crue.io;

import java.util.List;
import org.fudaa.ctulu.CtuluLog;
import org.fudaa.dodico.crue.common.DateDurationConverter;
import org.fudaa.dodico.crue.io.common.ContexteSimulation;
import org.fudaa.dodico.crue.io.common.CrueFileType;
import org.fudaa.dodico.crue.io.rcal.RCALFile;
import org.fudaa.dodico.crue.io.rcal.RCALFile.Branches;
import org.fudaa.dodico.crue.io.rcal.RCALFile.BranchesVariables;
import org.fudaa.dodico.crue.io.rcal.RCALFile.Casiers;
import org.fudaa.dodico.crue.io.rcal.RCALFile.Noeuds;
import org.fudaa.dodico.crue.io.rcal.RCALFile.ResultatsCalculPermanent;
import org.fudaa.dodico.crue.io.rcal.RCALFile.ResultatsPasDeTemps;
import org.fudaa.dodico.crue.io.rcal.RCALFile.Sections;
import org.fudaa.dodico.crue.projet.coeur.CoeurConfigContrat;
import org.fudaa.dodico.crue.projet.coeur.TestCoeurConfig;

public class TestCrueRCAL extends AbstractIOTestCase {

    protected static final String FICHIER_TEST_XML = "/v1_2/M3-0_c10.rcal.xml";

    public TestCrueRCAL() {
        super(Crue10FileFormatFactory.getVersion(CrueFileType.RCAL, TestCoeurConfig.INSTANCE), FICHIER_TEST_XML);
    }

    public void testLecture() {
        final RCALFile donnees = readData(FICHIER_TEST_XML, TestCoeurConfig.INSTANCE);
        assertEquals("HelloWorld", donnees.getCommentaire());
        this.testContexte(donnees.getContextSimulation());
        this.testNoeuds(donnees.getNoeuds());
        this.testCasiers(donnees.getCasiers());
        this.testSections(donnees.getSections());
        this.testBranches(donnees.getBranches());
        this.testResultatsCalculPermanents(donnees.getResultatsCalculPermanents());
        this.testResultatsCalculTransitoire(donnees.getResultatsCalculTransitoire());
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

    private void testNoeuds(Noeuds noeuds) {
        assertEquals(2, noeuds.getVariablesRes().size());
        assertEquals("ZRef", noeuds.getVariablesRes().get(0).getNom());
        assertEquals(true, noeuds.getVariablesRes().get(0).isActive());
        assertEquals(false, noeuds.getVariablesRes().get(1).isActive());
        this.testNoms(new String[] { "Nd_N1", "Nd_N2", "Nd_N6", "Nd_N3", "Nd_N4", "Nd_N7", "Nd_N5" }, noeuds.getNoeuds());
    }

    private void testCasiers(Casiers casiers) {
        assertEquals(4, casiers.getVariablesRes().size());
        assertEquals("Qech", casiers.getVariablesRes().get(0).getNom());
        this.testNoms(new String[] { "Ca_N6", "Ca_N7" }, casiers.getCasiersProfil());
    }

    private void testSections(Sections sections) {
        assertEquals(20, sections.getVariablesRes().size());
        assertEquals("Dact", sections.getVariablesRes().get(0).getNom());
        this.testNoms(new String[] { "St_PROF3AM", "St_PROF6B" }, sections.getSectionsIdem());
        this.testNoms(new String[] { "St_B1_00050", "St_B1_00150", "St_B1_00250", "St_B1_00350", "St_B1_00450", "St_PROF5" }, sections.getSectionsInterpolee());
        this.testNoms(new String[] { "St_PROF1", "St_PROF10", "St_Prof11", "St_PROF2", "St_PROF3A", "St_PROF3AV", "St_PROF3B", "St_PROF4", "St_PROF6A", "St_PROF7", "St_PROF8", "St_PROF9", "St_PROFSTR1", "St_PROFSTR2" }, sections.getSectionsProfil());
        this.testNoms(new String[] { "St_B5_Amont", "St_B5_Aval", "St_B8_Amont", "St_B8_Aval" }, sections.getSectionsSansGeometrie());
    }

    private void testBranches(Branches branches) {
        this.testBranchesVariables(branches.getBranchesBarrageFilEau(), 1, "RegimeOrifice", new String[] {});
        this.testBranchesVariables(branches.getBranchesBarrageGenerique(), 1, "RegimeOrifice", new String[] {});
        this.testBranchesVariables(branches.getBranchesNiveauxAssocies(), 1, "Dz", new String[] {});
        this.testBranchesVariables(branches.getBranchesOrifice(), 2, "RegimeOrifice", new String[] { "Br_B8" });
        this.testBranchesVariables(branches.getBranchesPdc(), 1, "Dz", new String[] {});
        this.testBranchesVariables(branches.getBranchesSaintVenant(), 5, "Qlat", new String[] { "Br_B1", "Br_B2", "Br_B4" });
        this.testBranchesVariables(branches.getBranchesSeuilLateral(), 1, "RegimeSeuil", new String[] { "Br_B5" });
        this.testBranchesVariables(branches.getBranchesSeuilTransversal(), 1, "RegimeSeuil", new String[] { "Br_B3" });
        this.testBranchesVariables(branches.getBranchesStrickler(), 2, "Splan", new String[] { "Br_B6" });
    }

    private void testBranchesVariables(BranchesVariables branches, int nbVariablesRes, String firstVariablesRes, String[] noms) {
        assertEquals(nbVariablesRes, branches.getVariablesRes().size());
        assertEquals(firstVariablesRes, branches.getVariablesRes().get(0).getNom());
        this.testNoms(noms, branches.getBranches());
    }

    private void testResultatsCalculPermanents(List<ResultatsCalculPermanent> resultatsCalculPermanents) {
        assertEquals(1, resultatsCalculPermanents.size());
        ResultatsCalculPermanent resultat = resultatsCalculPermanents.get(0);
        assertEquals("Cc_P1", resultat.getNom());
        assertEquals("M3-0_c10.rcal.bin", resultat.getHref());
        assertEquals(0, resultat.getOffset());
    }

    private void testResultatsCalculTransitoire(List<ResultatsPasDeTemps> resultatsCalculTransitoire) {
        assertEquals(2, resultatsCalculTransitoire.size());
        ResultatsPasDeTemps resultat = resultatsCalculTransitoire.get(0);
        assertEquals("Cc_T1", resultat.getNom());
        assertEquals("M3-0_c10.rcal.bin", resultat.getHref());
        assertEquals(32761, resultat.getOffset());
        assertEquals("P0Y0M0DT0H0M0S", resultat.getTempsSimu());
    }

    private static RCALFile readData(String fichier, CoeurConfigContrat version) {
        final CtuluLog analyzer = new CtuluLog();
        final Crue10FileFormat<Object> fileFormat = Crue10FileFormatFactory.getInstance().getFileFormat(CrueFileType.RCAL, version);
        final RCALFile jeuDonneesLue = (RCALFile) fileFormat.read(fichier, analyzer, createDefault()).getMetier();
        testAnalyser(analyzer);
        return jeuDonneesLue;
    }
}
