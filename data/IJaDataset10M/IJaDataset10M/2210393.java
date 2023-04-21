package ccp.api.Molecule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import ccp.api.ChemComp.ChemCompVar;
import memops.api.general.Properties;
import memops.general.ApiException;
import junit.framework.TestCase;

/**
 * 
 * TestMolecule
 * 
 */
public class TestMolecule extends TestCase {

    Properties properties;

    HashMap conditions;

    ArrayList molResLinkEnds;

    Molecule molecule;

    private static final java.util.Map threeLetterCode = new HashMap();

    static {
        threeLetterCode.put("A", "ALA");
        threeLetterCode.put("C", "CYS");
        threeLetterCode.put("D", "ASP");
        threeLetterCode.put("E", "GLU");
        threeLetterCode.put("F", "PHE");
        threeLetterCode.put("G", "GLY");
        threeLetterCode.put("H", "HIS");
        threeLetterCode.put("I", "ILE");
        threeLetterCode.put("K", "LYS");
        threeLetterCode.put("L", "LEU");
        threeLetterCode.put("M", "MET");
        threeLetterCode.put("N", "ASN");
        threeLetterCode.put("P", "PRO");
        threeLetterCode.put("Q", "GLN");
        threeLetterCode.put("R", "ARG");
        threeLetterCode.put("S", "SER");
        threeLetterCode.put("T", "THR");
        threeLetterCode.put("V", "VAL");
        threeLetterCode.put("W", "TRP");
        threeLetterCode.put("Y", "TYR");
    }

    /**
     * Map single letter code to three letter code for amino acids
     */
    public static final java.util.Map THREE_LETTER_CODE = Collections.unmodifiableMap(threeLetterCode);

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestMolecule.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        properties = new Properties();
        properties.setConnection();
        properties.setProject();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for TestMolecule.
     * @param arg0
     */
    public TestMolecule(String arg0) {
        super(arg0);
    }

    public void testCreate() {
        Statement statement = null;
        ResultSet resultSet = null;
        Long dbId = null;
        try {
            molecule = new Molecule(properties.getProject(), "myTestMolecule" + new java.util.Date().getTime());
            String sequence = "ACDEFGHIKLMNPQRSTVWY";
            String molType = "protein";
            String linking;
            String code1Letter, code3Letter;
            int seqCode;
            for (int i = 0; i < sequence.length(); i++) {
                code1Letter = sequence.substring(i, i + 1);
                if (THREE_LETTER_CODE.containsKey(code1Letter)) {
                    code3Letter = (String) THREE_LETTER_CODE.get(code1Letter);
                } else {
                    code3Letter = null;
                }
                seqCode = i + 1;
                if (i == 0) {
                    linking = "start";
                } else if (i == sequence.length() - 1) {
                    linking = "end";
                } else {
                    linking = "middle";
                }
                conditions = new HashMap();
                conditions.put("molType", molType);
                conditions.put("ccpCode", code3Letter);
                ChemCompHead chemCompHead = properties.getProject().findFirstChemCompHead(conditions);
                conditions = new HashMap();
                conditions.put("isDefaultVar", Boolean.TRUE);
                conditions.put("linking", linking);
                ChemCompVar chemCompVar = chemCompHead.getChemComp().findFirstChemCompVar(conditions);
                molecule.newMolResidue(chemCompHead, chemCompVar.getDescriptor(), linking, new Integer(seqCode));
            }
            for (int i = 0; i < sequence.length() - 1; i++) {
                seqCode = i + 1;
                MolResidue molRes1 = molecule.findFirstMolResidue("seqCode", new Integer(seqCode));
                MolResLinkEnd molResLinkEnd1 = molRes1.findFirstMolResLinkEnd("linkCode", "next");
                MolResidue molRes2 = molecule.findFirstMolResidue("seqCode", new Integer(seqCode + 1));
                MolResLinkEnd molResLinkEnd2 = molRes2.findFirstMolResLinkEnd("linkCode", "prev");
                molResLinkEnds = new ArrayList();
                molResLinkEnds.add(molResLinkEnd1);
                molResLinkEnds.add(molResLinkEnd2);
                molecule.newMolResLink(molResLinkEnds);
            }
            System.out.println("isStdLinear: " + molecule.getIsStdLinear());
            System.out.println("sequence: " + sequence);
            System.out.println("sequence: " + molecule.getSeqString());
            assertEquals("sequence set", sequence, molecule.getSeqString());
            dbId = molecule.getDbId();
            statement = properties.getConnection().createStatement();
            String query = "select * from MOLE_MOLECULE where memopsbaseclassid=" + dbId + "";
            resultSet = statement.executeQuery(query);
            assertTrue("create molecule", resultSet.next());
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (ApiException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
