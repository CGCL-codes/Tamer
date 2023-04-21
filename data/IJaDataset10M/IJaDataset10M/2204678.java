package net.sourceforge.ondex.mapping.orthologprediction;

import java.io.File;
import junit.framework.TestCase;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.CV;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.EvidenceType;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.ONDEXView;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.core.ONDEXIterator;
import net.sourceforge.ondex.core.memory.MemoryONDEXGraph;
import net.sourceforge.ondex.logging.ONDEXPluginLogger;
import net.sourceforge.ondex.mapping.MappingArguments;

/**
 * A very basic test case for paralog predication 
 * @author hindlem
 *
 */
public class OrthologPredictionTest extends TestCase {

    private ONDEXGraph og;

    private CV cv;

    private ConceptClass ccProtein;

    private EvidenceType et;

    private AttributeName aaAtt;

    private String ondexDir;

    private AttributeName taxidAtt;

    private String dc_dir;

    private EvidenceType et_blast;

    private AttributeName eval_att;

    private AttributeName bs_att;

    private AttributeName cov_att;

    private RelationType h_s_s_att;

    public void setUp() throws Exception {
        super.setUp();
        ondexDir = System.getProperty("ondex.dir");
        dc_dir = System.getProperty("dc.dir");
        if (ondexDir == null || ondexDir.length() == 0) {
            fail("ondex.dir is not set in -D variables can not continue");
        }
        if (dc_dir == null || dc_dir.length() == 0 || !new File(dc_dir).exists()) {
            fail("dc.dir is not set in -D variables can not continue");
        }
        og = new MemoryONDEXGraph(this.getClass().getName());
        cv = og.getMetaData().getFactory().createCV("cv");
        ccProtein = og.getMetaData().getFactory().createConceptClass("Protein");
        et = og.getMetaData().getFactory().createEvidenceType("et");
        aaAtt = og.getMetaData().getFactory().createAttributeName("AA", String.class);
        taxidAtt = og.getMetaData().getFactory().createAttributeName("TAXID", String.class);
        ONDEXConcept c = og.getFactory().createConcept("CD4PROT", cv, ccProtein, et);
        c.createConceptGDS(aaAtt, CD4AA, false);
        c.createConceptGDS(taxidAtt, "3702", false);
        c = og.getFactory().createConcept("CD4PROT_Varient", cv, ccProtein, et);
        c.createConceptGDS(aaAtt, CD4AA_Varient, false);
        c.createConceptGDS(taxidAtt, "3704", false);
        c = og.getFactory().createConcept("AT2G03220-MONOMERQ9SWH5", cv, ccProtein, et);
        c.createConceptGDS(aaAtt, AT2G03220, false);
        c.createConceptGDS(taxidAtt, "3702", false);
        c = og.getFactory().createConcept("AT1G14070-FUT7", cv, ccProtein, et);
        c.createConceptGDS(aaAtt, AT1G14070, false);
        c.createConceptGDS(taxidAtt, "3704", false);
        c = og.getFactory().createConcept("AT2G03210-FUT2", cv, ccProtein, et);
        c.createConceptGDS(aaAtt, AT2G03210, false);
        c.createConceptGDS(taxidAtt, "3704", false);
        et_blast = og.getMetaData().getFactory().createEvidenceType(MetaData.EVIDENCE_BLAST);
        eval_att = og.getMetaData().getFactory().createAttributeName(MetaData.ATT_E_VALUE, Double.class);
        bs_att = og.getMetaData().getFactory().createAttributeName(MetaData.ATT_BITSCORE, Double.class);
        cov_att = og.getMetaData().getFactory().createAttributeName("coverage", Double.class);
        h_s_s_att = og.getMetaData().getFactory().createRelationType(MetaData.RT_HAS_SIMILAR_SEQUENCE);
        og.getMetaData().getFactory().createAttributeName("AA", String.class);
        og.getMetaData().getFactory().createAttributeName("NA", String.class);
    }

    @Override
    public void tearDown() {
        og = null;
    }

    public void testCrossSpecies() {
        Mapping mapping = new Mapping();
        MappingArguments ma = new MappingArguments();
        ma.addOption(Mapping.BITSCORE_ARG, 100);
        ma.addOption(Mapping.E_VALUE_ARG, 0.1);
        ma.addOption(Mapping.OVERLAP_ARG, 0);
        ma.addOption(Mapping.CUTOFF_ARG, 3);
        ma.addOption(Mapping.SEQ_ALIGNMENT_PROG_ARG, "decypher");
        ma.addOption(Mapping.PROGRAM_DIR_ARG, dc_dir);
        ma.addOption(Mapping.SEQ_TYPE_ARG, "AA");
        mapping.setArguments(ma);
        mapping.setONDEXGraph(og);
        mapping.addMappingListener(new ONDEXPluginLogger());
        mapping.start();
        ONDEXView<ONDEXRelation> relations = og.getRelations();
        assertEquals(2, relations.size());
        while (relations.hasNext()) {
            ONDEXRelation relation = relations.next();
            assertNotNull(relation.getRelationGDS(eval_att));
            assertNotNull(relation.getRelationGDS(bs_att));
            assertNotNull(relation.getRelationGDS(eval_att));
            assertNotNull(relation.getRelationGDS(cov_att));
            System.out.println(eval_att.getId() + " " + relation.getRelationGDS(eval_att).getValue());
            System.out.println(bs_att.getId() + " " + relation.getRelationGDS(bs_att).getValue());
            System.out.println(eval_att.getId() + " " + relation.getRelationGDS(eval_att).getValue());
            System.out.println(cov_att.getId() + " " + relation.getRelationGDS(cov_att).getValue());
            ONDEXIterator<EvidenceType> ets = relation.getEvidence();
            assertEquals(ets.size(), 1);
            while (ets.hasNext()) {
                EvidenceType tet = ets.next();
                assertEquals(et_blast, tet);
            }
            assertEquals(h_s_s_att, relation.getOfType());
        }
    }

    private static String CD4AA = "VVLGKKGDTVELTCNASQNTTTQFHWKNSNQIKILGKQGSFLTKGSSKLRDRIDSRKSLWDQGCFSMIIK" + "NLKIEDSETYICEVENKKEEVELLVFGLTANSDTHLLQGQSLTLTLESPPGSSPSVKCRSPRGKNIQGGR" + "TLSVPQLERQDSGTWTCTVSQDQNTVEFKIDIVVLAFQKASSTVYKKEGEQVEFSFPLAFTLEKLTGSGE" + "LWWQAERASSSKSWITFDLKNKEVSVKQVTQDPKLQKKKLPLNLTLPQALPQYAGSGNLTLALEAKTGK" + "LHQEVNLVVMRATQFQENLTCEVWGPTSPKLMLSLKLENKAATVSKQAKAVWVLNPEEGMWQCLLSDSGQ" + "VLLESNIKVLPTWPTPVQPMALIVLGGVAGLLLFTGLGIFFCVRCRH";

    private static String CD4AA_Varient = "VVLGKKGDTVELTCNASQNTTTQFHWKNSNQIKILGKQGSFLTKGSSKLRDRIDSRKSLWDQGCFSMIIK" + "NLKIEDSETYICEVENKKEEVELLVFGLTANSDTHLLQGQSLTLTLESPPGSSPSVKCRSPRGKNIQGGR" + "TLSVPQLERQDSGTWTCTVSQDQNTVEFKIDIVVLAFQKASSTVYKKEGEQVEFSFPLAFTLEKLTGSGE" + "LWWQAERASSSKSWITFDLKNKEVSVKQVKKKKKLPLNLTLPQALPQYAGSGNLTLALEAKTGK" + "LHQEVNLVVMRATQFQENLTCEVWGPTSPKLMLSLKLENKAATVSKQAKAVWVLNPEEGMWQCLLSDSGQ" + "VLLESNIKVLPTWPTPVQPMALIVLGGVAGLLLFTGLGIFFCVRCRH";

    private static String AT2G03220 = "MDQNSYRRRSSPIRTTTGGSKSVNFSELLQMKYLSSGTMKL" + "TRTFTTCLIVFSVLVAFSMIFHQHPSDSNRIMGFAEARVLDAGVFPNVTNINSDKLLGGLLASGFD" + "EDSCLSRYQSVHYRKPSPYKPSSYLISKLRNYEKLHKRCGPGTESYKKALKQLDQEHIDGDGECKY" + "VVWISFSGLGNRILSLASVFLYALLTDRVLLVDRGKDMDDLFCEPFLGMSWLLPLDFPMTDQFDGL" + "NQESSRCYGYMVKNQVIDTEGTLSHLYLHLVHDYGDHDKMFFCEGDQTFIGKVPWLIVKTDNYFVP" + "SLWLIPGFDDELNKLFPQKATVFHHLGRYLFHPTNQVWGLVTRYYEAYLSHADEKIGIQVRVFDED" + "PGPFQHVMDQISSCTQKEKLLPEVDTLVERSRHVNTPKHKAVLVTSLNAGYAENLKSMYWEYPTST" + "GEIIGVHQPSQEGYQQTEKKMHNGKALAEMYLLSLTDNLVTSAWSTFGYVAQGLGGLKPWILYRPE" + "NRTTPDPSCGRAMSMEPCFHSPPFYDCKAKTGIDTGTLVPHVRHCEDISWGLKLV";

    private static String AT1G14070 = "MLLLLSFSNIFKHQLLGATINVGSKDSVKPRDRLLGGLLTAD" + "FDEDSCLSRYQSSLYRKPSPYRTSEYLISKLRNYEMLHKRCGPGTDAYKRATEKLGHDHENVGDSSDG" + "ECKYIVWVAVYGLGNRILTLASVFLYALLTERIILVDQRKDISDLFCEPFPGTSWLLPLDFPLMGQID" + "SFNREYSHCYGTMLKNHTINSTTIPSHLYLHLLHDYRDQDKMFFCQKDQSLVDKVPWLVVKSNLYFIP" + "SLWLNPSFQTELIKLFPQKDTVFYHLARYLFHPTNQVWGMVTRSYNAYLSRADEILGIQVRVFSRQTK" + "YFQHVMDQIVACTQREKLLPEFAAQEEAQVTNTSNPSKLKAVLVTSLNPEYSNNLKKMYWEHPTTTGD" + "IVEVYQPSRERFQQTDKKLHDQKALAEMYLLSLTDKLVTSALSTFGYVAQGLGGLKPWILYTPKKFKS" + "PNPPCGRVISMEPCFLTPPVHGCEAKKGINTAKIVPFVRHCEDLRHYGLKLVDDTKNEL";

    private static String AT2G03210 = "MRITEILALFMVLVPVSLVIVAMFGYDQGNGFVQASRFITMEPN" + "VTSSSDDSSLVQRDQEQKDSVDMSLLGGLLVSGFKKESCLSRYQSYLYRKASPYKPSLHLLSKLRAYEEL" + "HKRCGPGTRQYTNAERLLKQKQTGEMESQGCKYVVWMSFSGLGNRIISIASVFLYAMLTDRVLLVEGGEQ" + "FADLFCEPFLDTTWLLPKDFTLASQFSGFGQNSAHCHGDMLKRKLINESSVSSLSHLYLHLAHDYNEHDK" + "MFFCEEDQNLLKNVPWLIMRTNNFFAPSLFLISSFEEELGMMFPEKGTVFHHLGRYLFHPSNQVWGLITR" + "YYQAYLAKADERIGLQIRVFDEKSGVSPRVTKQIISCVQNENLLPRLSKGEEQYKQPSEEELKLKSVLVT" + "SLTTGYFEILKTMYWENPTVTRDVIGIHQPSHEGHQQTEKLMHNRKAWAEMYLLSLTDKLVISAWSTFGY" + "VAQGLGGLRAWILYKQENQTNPNPPCGRAMSPDPCFHAPPYYDCKAKKGTDTGNVVPHVRHCEDISWGLK" + "LVDNF";
}
