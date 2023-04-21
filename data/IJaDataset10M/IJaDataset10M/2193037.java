package com.cfdrc.sbmlforge;

import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import com.cfdrc.sbmlforge.gui.Preferences;
import com.cfdrc.sbmlforge.kegg.KEGGCompound;
import com.cfdrc.sbmlforge.kegg.KEGGDatabase;
import com.cfdrc.sbmlforge.kegg.KEGGEntryNotFoundException;
import com.cfdrc.sbmlforge.kegg.KEGGEnzyme;
import com.cfdrc.sbmlforge.kegg.KEGGFileLocator;
import com.cfdrc.sbmlforge.kegg.KEGGOrthology;
import com.cfdrc.sbmlforge.kegg.KEGGReaction;
import com.cfdrc.sbmlforge.kegg.KEGGDatabaseLoader;
import com.cfdrc.sbmlforge.kegg.KEGGException;

/**
 * JUnit Testing of KEGG Database
 * 
 * @author Zachary Groff
 */
public class TestKEGG {

    /** KEGGDatabase object containing data from files */
    public static KEGGDatabase database;

    /**
	 * Initialize by loading kegg database
	 * @throws KEGGException
	 */
    @org.junit.BeforeClass
    public static void setUp() throws KEGGException {
        Preferences preferences = new Preferences();
        preferences.loadSettings();
        database = new KEGGDatabaseLoader().load(new KEGGFileLocator(preferences));
    }

    /**
	 * Test ability to look up names in orthologies
	 */
    @org.junit.Test
    public void testOrthologyNames() {
        Map<String, String> orthologyToNameMap = new HashMap<String, String>();
        orthologyToNameMap.put("K00001", "E1.1.1.1, adh");
        orthologyToNameMap.put("K00004", "E1.1.1.4");
        orthologyToNameMap.put("K00027", "E1.1.1.38, sfcA, maeA");
        orthologyToNameMap.put("K00276", "E1.4.3.21, AOC2, AOC3, tynA");
        orthologyToNameMap.put("K05917", "CYP51");
        orthologyToNameMap.put("K05925", "E2.1.1.62");
        orthologyToNameMap.put("K05934", "E2.1.1.131, cobJ, cbiH");
        orthologyToNameMap.put("K12217", "icmO, trbC, dotL");
        orthologyToNameMap.put("K12311", "MAN2B1, LAMAN");
        orthologyToNameMap.put("K12341", "yadA");
        for (String key : new TreeSet<String>(orthologyToNameMap.keySet())) {
            KEGGOrthology orthology = getOrthology(key);
            String expectedName = orthologyToNameMap.get(key);
            String name = orthology.getName();
            assertEquals("Orthology entry " + key + " name '" + name + "' not equal to expected name '" + expectedName + "'", expectedName, name);
        }
    }

    /**
	 * Test ability to look up gene name in specified in orthology
	 */
    @org.junit.Test
    public void testGenes() {
        final String orthologyKey = "K00001";
        Map<String, String> geneIdToNameMap = new HashMap<String, String>();
        geneIdToNameMap.put("hsa:131", "ADH7");
        geneIdToNameMap.put("mmu:11529", "Adh7");
        geneIdToNameMap.put("spu:579220", "");
        geneIdToNameMap.put("osa:4350054", "Os11g0210500");
        geneIdToNameMap.put("fgr:FG10200.1", "");
        KEGGOrthology orthology = getOrthology(orthologyKey);
        for (String geneId : new TreeSet<String>(geneIdToNameMap.keySet())) {
            String expectedGeneName = geneIdToNameMap.get(geneId);
            String actualName = orthology.getGenes().get(geneId);
            assertEquals("Orthology " + orthologyKey + ", Gene " + geneId + " expected name: '" + expectedGeneName + "' actual name: '" + actualName + "'", expectedGeneName, actualName);
        }
    }

    /**
	 * Test ability to look up names in compounds
	 */
    @org.junit.Test
    public void testCompoundNames() {
        Map<String, List<String>> compoundToNameMap = new HashMap<String, List<String>>();
        List<String> list = new ArrayList<String>();
        list.add("H2O");
        list.add("Water");
        compoundToNameMap.put("C00001", list);
        list = new ArrayList<String>();
        list.add("ATP");
        list.add("Adenosine 5'-triphosphate");
        compoundToNameMap.put("C00002", list);
        list = new ArrayList<String>();
        list.add("PQQ");
        list.add("Pyrrolo-quinoline quinone");
        list.add("Pyrroloquinoline-quinone");
        list.add("Pyrroloquinoline quinone");
        list.add("4,5-Dioxo-4,5-dihydro-1H-pyrrolo[2,3-f]quinoline-2,7,9- tricarboxylate");
        list.add("Methoxatin");
        compoundToNameMap.put("C00113", list);
        list = new ArrayList<String>();
        list.add("Choline");
        list.add("Bilineurine");
        compoundToNameMap.put("C00114", list);
        list = new ArrayList<String>();
        list.add("GM3");
        list.add("(N-Acetylneuraminyl)-D-galactosyl-D-glucosylceramide");
        list.add("alpha-N-Acetylneuraminyl-2,3-beta-D-galactosyl-1,4-beta-D- glucosylceramide");
        list.add("N-Acetylneuraminyl-2,3-alpha-D-galactosyl-1,4-beta-D- glucosylceramide");
        list.add("Neu5Ac-alpha2->3Gal-beta1->4Glc-beta1->1'Cer");
        compoundToNameMap.put("C04730", list);
        list = new ArrayList<String>();
        list.add("1-Methyl-4-phenyl-1,2,3,6-tetrahydropyridine N-oxide");
        compoundToNameMap.put("C04731", list);
        list = new ArrayList<String>();
        list.add("Penicillin X");
        compoundToNameMap.put("C17404", list);
        list = new ArrayList<String>();
        list.add("Penicillin O");
        compoundToNameMap.put("C17405", list);
        for (String key : new TreeSet<String>(compoundToNameMap.keySet())) {
            KEGGCompound compound = getCompound(key);
            List<String> expectedNames = compoundToNameMap.get(key);
            List<String> names = compound.getNames();
            assertEquals("Compound entry " + key + " name size '" + names.size() + "' not equal to expected name size '" + expectedNames.size() + "'", expectedNames.size(), names.size());
            for (int i = 0; i < names.size(); i++) assertEquals("Compound entry " + key + " name[" + i + "] '" + names.get(i) + "' not equal to expected name '" + expectedNames.get(i) + "'", expectedNames.get(i), names.get(i));
        }
    }

    /**
	 * Test ability to look up names in drugs
	 */
    @org.junit.Test
    public void testDrugNames() {
        Map<String, List<String>> drugToNameMap = new HashMap<String, List<String>>();
        List<String> list = new ArrayList<String>();
        list.add("Water (JP15/USP)");
        list.add("Water, purified (JP15/USP)");
        list.add("Purified water (JP15)");
        list.add("Sterile purified water (JP15)");
        list.add("Water for injection (JP15)");
        list.add("Sterile water (TN)");
        drugToNameMap.put("D00001", list);
        list = new ArrayList<String>();
        list.add("Nadide (JAN/USAN/INN)");
        list.add("Nicotinamide adenine dinucleotide");
        drugToNameMap.put("D00002", list);
        list = new ArrayList<String>();
        list.add("Hydrocortisone acetate - neomycin sulfate - colistin sulfate - thonzonium bromide mixt");
        list.add("Coly-Mycin S (TN)");
        drugToNameMap.put("D02139", list);
        list = new ArrayList<String>();
        list.add("Thonzonium bromide (USAN)");
        list.add("Tonzonium bromide (INN)");
        list.add("Thonzide (TN)");
        drugToNameMap.put("D02140", list);
        list = new ArrayList<String>();
        list.add("Minodronic acid hydrate (JAN)");
        list.add("Bonoteo (TN)");
        list.add("Recalbon (TN)");
        drugToNameMap.put("D09198", list);
        list = new ArrayList<String>();
        list.add("Shigyakuto");
        drugToNameMap.put("D09199", list);
        for (String key : new TreeSet<String>(drugToNameMap.keySet())) {
            KEGGCompound compound = getCompound(key);
            List<String> expectedNames = drugToNameMap.get(key);
            List<String> names = compound.getNames();
            assertEquals("Drug entry " + key + " name size '" + names.size() + "' not equal to expected name size '" + expectedNames.size() + "'", expectedNames.size(), names.size());
            for (int i = 0; i < names.size(); i++) assertEquals("Drug entry " + key + " name[" + i + "] '" + names.get(i) + "' not equal to expected name '" + expectedNames.get(i) + "'", expectedNames.get(i), names.get(i));
        }
    }

    /**
	 * Test ability to look up names in glycans
	 */
    @org.junit.Test
    public void testGlycanNames() {
        Map<String, List<String>> glycanToNameMap = new HashMap<String, List<String>>();
        List<String> list = new ArrayList<String>();
        list.add("(GlcNAc)1 (PP-Dol)1");
        glycanToNameMap.put("G00001", list);
        list = new ArrayList<String>();
        list.add("(GlcNAc)2 (PP-Dol)1");
        glycanToNameMap.put("G00002", list);
        list = new ArrayList<String>();
        list.add("(Gal2Ac3Ac4Ac6Ac)2 (Gal2Ac4Ac)1 (Gal2Ac4Ac6Ac)3 (Glc2Ac3Ac6Ac)1 (GlcNAc3Ac6Ac)3 (Cer)1");
        glycanToNameMap.put("G02726", list);
        list = new ArrayList<String>();
        list.add("(Gal)3 (GalNAc)2 (GlcNAc)3 (Neu5Ac)2");
        glycanToNameMap.put("G02727", list);
        list = new ArrayList<String>();
        list.add("(GlcNAc)2 (Man)24 (P)1 (Asn)1");
        glycanToNameMap.put("G13049", list);
        list = new ArrayList<String>();
        list.add("(GlcNAc)2 (Man)31 (P)1 (Asn)1");
        glycanToNameMap.put("G13050", list);
        for (String key : new TreeSet<String>(glycanToNameMap.keySet())) {
            KEGGCompound compound = getCompound(key);
            List<String> expectedNames = glycanToNameMap.get(key);
            List<String> names = compound.getNames();
            assertEquals("Glycan entry " + key + " name size '" + names.size() + "' not equal to expected name size '" + expectedNames.size() + "'", names.size(), expectedNames.size());
            for (int i = 0; i < names.size(); i++) assertEquals("Glycan entry " + key + " name[" + i + "] '" + names.get(i) + "' not equal to expected name '" + expectedNames.get(i) + "'", names.get(i), expectedNames.get(i));
        }
    }

    /**
	 * Test ability to look up names in enzymes
	 */
    @org.junit.Test
    public void testEnzymeNames() {
        Map<String, List<String>> enzymeToNameMap = new HashMap<String, List<String>>();
        List<String> list = new ArrayList<String>();
        list.add("alcohol dehydrogenase");
        list.add("aldehyde reductase");
        list.add("ADH");
        list.add("alcohol dehydrogenase (NAD)");
        list.add("aliphatic alcohol dehydrogenase");
        list.add("ethanol dehydrogenase");
        list.add("NAD-dependent alcohol dehydrogenase");
        list.add("NAD-specific aromatic alcohol dehydrogenase");
        list.add("NADH-alcohol dehydrogenase");
        list.add("NADH-aldehyde dehydrogenase");
        list.add("primary alcohol dehydrogenase");
        list.add("yeast alcohol dehydrogenase");
        enzymeToNameMap.put("1.1.1.1", list);
        list = new ArrayList<String>();
        list.add("alcohol dehydrogenase (NADP+)");
        list.add("aldehyde reductase (NADPH2)");
        list.add("NADP-alcohol dehydrogenase");
        list.add("NADP+-aldehyde reductase");
        list.add("NADP+-dependent aldehyde reductase");
        list.add("NADPH-aldehyde reductase");
        list.add("NADPH-dependent aldehyde reductase");
        list.add("nonspecific succinic semialdehyde reductase");
        list.add("ALR 1");
        list.add("low-Km aldehyde reductase");
        list.add("high-Km aldehyde reductase");
        list.add("alcohol dehydrogenase (NADP+)");
        enzymeToNameMap.put("1.1.1.2", list);
        list = new ArrayList<String>();
        list.add("[cytochrome c]-lysine N-methyltransferase");
        list.add("cytochrome c (lysine) methyltransferase");
        list.add("cytochrome c methyltransferase");
        list.add("cytochrome c-specific protein methylase III");
        list.add("cytochrome c-specific protein-lysine methyltransferase");
        list.add("S-adenosyl-L-methionine:[cytochrome c]-L-lysine 6-N-methyltransferase");
        enzymeToNameMap.put("2.1.1.59", list);
        list = new ArrayList<String>();
        list.add("calmodulin-lysine N-methyltransferase");
        list.add("S-adenosylmethionine:calmodulin (lysine) N-methyltransferase");
        list.add("S-adenosyl-L-methionine:calmodulin-L-lysine 6-N-methyltransferase");
        enzymeToNameMap.put("2.1.1.60", list);
        list = new ArrayList<String>();
        list.add("magnesium chelatase");
        list.add("protoporphyrin IX magnesium-chelatase");
        list.add("protoporphyrin IX Mg-chelatase");
        list.add("magnesium-protoporphyrin IX chelatase");
        list.add("magnesium-protoporphyrin chelatase");
        list.add("magnesium-chelatase");
        list.add("Mg-chelatase");
        list.add("Mg-protoporphyrin IX magnesio-lyase");
        enzymeToNameMap.put("6.6.1.1", list);
        list = new ArrayList<String>();
        list.add("cobaltochelatase");
        list.add("hydrogenobyrinic acid a,c-diamide cobaltochelatase");
        list.add("CobNST");
        list.add("CobNCobST");
        enzymeToNameMap.put("6.6.1.2", list);
        for (String key : new TreeSet<String>(enzymeToNameMap.keySet())) {
            KEGGEnzyme enzyme = getEnzyme(key);
            List<String> expectedNames = enzymeToNameMap.get(key);
            List<String> names = enzyme.getNames();
            assertEquals("Enzyme entry " + key + " name size '" + names.size() + "' not equal to expected name size '" + expectedNames.size() + "'", expectedNames.size(), names.size());
            for (int i = 0; i < names.size(); i++) assertEquals("Enzyme entry " + key + " name[" + i + "] '" + names.get(i) + "' not equal to expected name '" + expectedNames.get(i) + "'", expectedNames.get(i), names.get(i));
        }
    }

    /**
	 * Test ability to look up orthology in enzymes
	 */
    @org.junit.Test
    public void testEnzymeOrthology() {
        Map<String, String> enzymeToOrthologyMap = new HashMap<String, String>();
        enzymeToOrthologyMap.put("1.1.1.1", "KO: K00001 alcohol dehydrogenase KO: K11440 choline dehydrogenase");
        enzymeToOrthologyMap.put("1.1.1.2", "KO: K00002 alcohol dehydrogenase (NADP+)");
        enzymeToOrthologyMap.put("3.4.13.7", null);
        enzymeToOrthologyMap.put("3.4.13.8", null);
        enzymeToOrthologyMap.put("6.6.1.1", "KO: K03403 magnesium chelatase KO: K03404 magnesium chelatase subunit ChlD KO: K03405 magnesium chelatase subunit ChlI KO: K06049");
        enzymeToOrthologyMap.put("6.6.1.2", "KO: K02230 cobaltochelatase CobN KO: K06050 KO: K09882 cobaltochelatase CobS KO: K09883 cobaltochelatase CobT");
        for (String key : new TreeSet<String>(enzymeToOrthologyMap.keySet())) {
            KEGGEnzyme enzyme = getEnzyme(key);
            String expectedOrthology = enzymeToOrthologyMap.get(key);
            String orthology = enzyme.getOrthology();
            assertEquals("Enzyme entry " + key + " orthology '" + orthology + "' not equal to expected orthology '" + expectedOrthology + "'", expectedOrthology, orthology);
        }
    }

    /**
	 * Test ability to look up particular gene in enzymes
	 */
    @org.junit.Test
    public void testEnzymeGenes() {
        final String enzymeKey = "1.1.1.1";
        Map<String, String> geneIdToNameMap = new HashMap<String, String>();
        geneIdToNameMap.put("hsa:131", "ADH7");
        geneIdToNameMap.put("mmu:11529", "Adh7");
        geneIdToNameMap.put("spu:579220", "");
        geneIdToNameMap.put("osa:4350054", "Os11g0210500");
        geneIdToNameMap.put("fgr:FG10200.1", "");
        KEGGEnzyme enzyme = getEnzyme(enzymeKey);
        for (String geneId : new TreeSet<String>(geneIdToNameMap.keySet())) {
            String expectedGeneName = geneIdToNameMap.get(geneId);
            String actualName = enzyme.getGenes().get(geneId);
            assertEquals("Enzyme " + enzymeKey + ", Gene " + geneId + " name: '" + actualName + "' not equal to expected name: '" + expectedGeneName + "'", expectedGeneName, actualName);
        }
    }

    /**
	 * Test ability to look up equation in reactions
	 */
    @org.junit.Test
    public void testReactionEquation() {
        Map<String, String> reactionToEquationMap = new HashMap<String, String>();
        reactionToEquationMap.put("R00001", "C00890 + n C00001 <=> (n+1) C02174");
        reactionToEquationMap.put("R00002", "16 C00002 + 16 C00001 + 8 C00138 <=> 8 C05359 + 16 C00009 + 16 C00008 + 8 C00139");
        reactionToEquationMap.put("R03933", "C05501 + C00007 + 2 C00662 + 2 C00080 <=> C02373 + C01953 + 2 C00001 + 2 C00667");
        reactionToEquationMap.put("R03934", "C00019 + C02379 <=> C00021 + C02381");
        reactionToEquationMap.put("R08717", "G00309 + C00001 <=> G00319 + C00159");
        reactionToEquationMap.put("R08718", "G00595 + C00001 <=> G00971 + C00159");
        for (String key : new TreeSet<String>(reactionToEquationMap.keySet())) {
            KEGGReaction reaction = getReaction(key);
            String expectedEquation = reactionToEquationMap.get(key);
            String equation = reaction.getEquation();
            assertEquals("Reaction entry " + key + " equation '" + equation + "' not equal to expected equation '" + expectedEquation + "'", expectedEquation, equation);
        }
    }

    /**
	 * Test ability to look up enzymes in reactions
	 */
    @org.junit.Test
    public void testReactionEnzymes() {
        Map<String, String> reactionToEnzymeMap = new HashMap<String, String>();
        reactionToEnzymeMap.put("R00001", "3.6.1.10");
        reactionToEnzymeMap.put("R00002", "1.18.6.1");
        reactionToEnzymeMap.put("R03933", "1.14.15.6");
        reactionToEnzymeMap.put("R03934", "2.1.1.108");
        reactionToEnzymeMap.put("R08717", "3.2.1.24");
        reactionToEnzymeMap.put("R08718", "3.2.1.24");
        for (String key : new TreeSet<String>(reactionToEnzymeMap.keySet())) {
            KEGGReaction reaction = getReaction(key);
            String expectedEnzymes = reactionToEnzymeMap.get(key);
            String enzymes = reaction.getEnzymes();
            assertEquals("Reaction entry " + key + " enzymes '" + enzymes + "' not equal to expected enzymes '" + expectedEnzymes + "'", expectedEnzymes, enzymes);
        }
    }

    /**
	 * Test ability to look up orthology in reactions
	 */
    @org.junit.Test
    public void testReactionOrthology() {
        Map<String, String> reactionToOrthologyMap = new HashMap<String, String>();
        reactionToOrthologyMap.put("R00001", null);
        reactionToOrthologyMap.put("R00002", null);
        reactionToOrthologyMap.put("R03933", "KO: K00498 cytochrome P450, family 11, subfamily A (cholesterol monooxygenase (side-chain-cleaving)) [EC:1.14.15.6]");
        reactionToOrthologyMap.put("R03934", null);
        reactionToOrthologyMap.put("R08717", "KO: K12311 lysosomal alpha-mannosidase [EC:3.2.1.24] KO: K12312 epididymis-specific alpha-mannosidase [EC:3.2.1.24]");
        reactionToOrthologyMap.put("R08718", "KO: K12311 lysosomal alpha-mannosidase [EC:3.2.1.24] KO: K12312 epididymis-specific alpha-mannosidase [EC:3.2.1.24]");
        for (String key : new TreeSet<String>(reactionToOrthologyMap.keySet())) {
            KEGGReaction reaction = getReaction(key);
            String expectedOrthology = reactionToOrthologyMap.get(key);
            String orthology = reaction.getOrthology();
            assertEquals("Reaction entry " + key + " orthology '" + orthology + "' not equal to expected orthology '" + expectedOrthology + "'", expectedOrthology, orthology);
        }
    }

    /**
	 * Get an orthology and assert that it is not null
	 */
    private KEGGOrthology getOrthology(String orthologyId) {
        KEGGOrthology orthology = null;
        try {
            orthology = database.getOrthologies().get(orthologyId);
        } catch (KEGGEntryNotFoundException e) {
            orthology = null;
        }
        assertNotNull("Ortology id " + orthologyId + " is null ", orthology);
        return orthology;
    }

    /**
	 * Get a compound and assert that it is not null
	 */
    private KEGGCompound getCompound(String compoundId) {
        KEGGCompound compound = null;
        try {
            compound = database.getCompounds().get(compoundId);
        } catch (KEGGEntryNotFoundException e) {
            compound = null;
        }
        assertNotNull("Compound id " + compoundId + " is null ", compound);
        return compound;
    }

    /**
	 * Get a enzyme and assert that it is not null
	 */
    private KEGGEnzyme getEnzyme(String enzymeId) {
        KEGGEnzyme enzyme = null;
        try {
            enzyme = database.getEnzymes().get(enzymeId);
        } catch (KEGGEntryNotFoundException e) {
            enzyme = null;
        }
        assertNotNull("Enzyme id " + enzymeId + " is null ", enzyme);
        return enzyme;
    }

    /**
	 * Get a reaction and assert that it is not null
	 */
    private KEGGReaction getReaction(String reactionId) {
        KEGGReaction reaction = null;
        try {
            reaction = database.getReactions().get(reactionId);
        } catch (KEGGEntryNotFoundException e) {
            reaction = null;
        }
        assertNotNull("Reaction id " + reactionId + " is null ", reaction);
        return reaction;
    }
}
