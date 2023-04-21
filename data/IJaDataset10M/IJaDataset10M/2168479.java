package org.xmlcml.cml.tools;

import java.util.List;
import org.junit.Assert;
import org.xmlcml.cml.base.AbstractTool;
import org.xmlcml.cml.base.CMLConstants;
import org.xmlcml.cml.element.CMLBond;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.test.MoleculeAtomBondFixture;
import org.xmlcml.cml.testutil.JumboTestUtils;

public class MoleculeToolFixture {

    public static void abov(CMLMolecule mol, int knownUnpaired, String[] expected) {
        mol.setBondOrders(CMLBond.SINGLE);
        PiSystemControls piSystemManager = new PiSystemControls();
        piSystemManager.setUpdateBonds(true);
        piSystemManager.setKnownUnpaired(knownUnpaired);
        piSystemManager.setDistributeCharge(true);
        MoleculeTool.getOrCreateTool(mol).adjustBondOrdersToValency(piSystemManager);
        List<CMLBond> bonds = mol.getBonds();
        String[] found = new String[bonds.size()];
        int i = 0;
        for (CMLBond bond : bonds) {
            found[i++] = bond.getOrder();
        }
        Assert.assertEquals("expected orders", found, expected);
    }

    private MoleculeAtomBondFixture fixture = new MoleculeAtomBondFixture();

    public AbstractTool moleculeTool1;

    public AbstractTool moleculeTool2;

    public AbstractTool moleculeTool3;

    public AbstractTool moleculeTool4;

    public MoleculeTool moleculeTool5;

    public MoleculeTool moleculeTool5a;

    public AbstractTool moleculeTool6;

    public AbstractTool moleculeTool7;

    public AbstractTool moleculeTool8;

    public AbstractTool moleculeTool9;

    public MoleculeTool moleculeTool10;

    public AbstractTool moleculeToolXom0;

    public MoleculeTool moleculeToolXml0;

    public AbstractTool moleculeToolBond0;

    public AbstractTool moleculeToolXmlBonds;

    String benzeneS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='benzene'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='1'/>" + "    <atom id='a2' elementType='C' hydrogenCount='1'/>" + "    <atom id='a3' elementType='C' hydrogenCount='1'/>" + "    <atom id='a4' elementType='C' hydrogenCount='1'/>" + "    <atom id='a5' elementType='C' hydrogenCount='1'/>" + "    <atom id='a6' elementType='C' hydrogenCount='1'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='A'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='A'/>" + "    <bond id='b3' atomRefs2='a3 a4' order='A'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='A'/>" + "    <bond id='b5' atomRefs2='a5 a6' order='A'/>" + "    <bond id='b6' atomRefs2='a6 a1' order='A'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule benzene = null;

    String[] benzeneOrder = new String[] { "2", "1", "2", "1", "2", "1" };

    String nickS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + ">" + " <atomArray> " + "  <atom id='a1' elementType='O'/>" + "  <atom id='a2' elementType='O'/>" + "  <atom id='a3' elementType='O'/>" + "  <atom id='a4' elementType='O'/>" + "  <atom id='a5' elementType='O'/>" + "  <atom id='a6' elementType='O'/>" + "  <atom id='a7' elementType='O'/>" + "  <atom id='a8' elementType='O'/>" + "  <atom id='a9' elementType='N'/>" + "  <atom id='a10' elementType='N'/>" + "  <atom id='a11' elementType='N'/>" + "  <atom id='a12' elementType='N'/>" + "  <atom id='a13' elementType='N'/>" + "  <atom id='a14' elementType='C'/>" + "  <atom id='a15' elementType='H'/>" + "  <atom id='a16' elementType='C'/>" + "  <atom id='a17' elementType='C'/>" + "  <atom id='a18' elementType='H'/>" + "  <atom id='a19' elementType='C'/>" + "  <atom id='a20' elementType='C'/>" + "  <atom id='a21' elementType='C'/>" + "  <atom id='a22' elementType='H'/>" + "  <atom id='a23' elementType='C'/>" + "  <atom id='a24' elementType='C'/>" + "  <atom id='a25' elementType='H'/>" + "  <atom id='a26' elementType='C'/>" + "  <atom id='a27' elementType='C'/>" + "  <atom id='a28' elementType='C'/>" + "  <atom id='a29' elementType='C'/>" + "  <atom id='a30' elementType='C'/>" + "  <atom id='a31' elementType='C'/>" + "  <atom id='a32' elementType='C'/>" + "  <atom id='a33' elementType='C'/>" + "  <atom id='a34' elementType='C'/>" + "  <atom id='a35' elementType='C'/>" + "  <atom id='a36' elementType='H'/>" + "  <atom id='a37' elementType='H'/>" + "  <atom id='a38' elementType='C'/>" + "  <atom id='a39' elementType='H'/>" + "  <atom id='a40' elementType='H'/>" + "  <atom id='a41' elementType='H'/>" + " </atomArray>" + " <bondArray> " + "  <bond atomRefs2='a1 a9' /> " + "  <bond atomRefs2='a2 a9' /> " + "  <bond atomRefs2='a3 a10' /> " + "  <bond atomRefs2='a4 a10' />" + "  <bond atomRefs2='a5 a34' />" + "  <bond atomRefs2='a6 a34' />" + "  <bond atomRefs2='a6 a35' />" + "  <bond atomRefs2='a7 a11' />" + "  <bond atomRefs2='a8 a11' /> " + "  <bond atomRefs2='a9 a16' /> " + "  <bond atomRefs2='a10 a19'/>" + "  <bond atomRefs2='a11 a23' />" + "  <bond atomRefs2='a12 a32' />" + "  <bond atomRefs2='a13 a33' />" + "  <bond atomRefs2='a14 a15' />" + "  <bond atomRefs2='a14 a16' />" + "  <bond atomRefs2='a14 a27' />" + "  <bond atomRefs2='a16 a17' />" + "  <bond atomRefs2='a17 a18' />" + "  <bond atomRefs2='a17 a19' />" + "  <bond atomRefs2='a19 a28' />" + "  <bond atomRefs2='a20 a21' />" + "  <bond atomRefs2='a20 a29' />" + "  <bond atomRefs2='a20 a34' />" + "  <bond atomRefs2='a21 a22' />" + "  <bond atomRefs2='a21 a23' />" + "  <bond atomRefs2='a23 a24' />" + "  <bond atomRefs2='a24 a25' />" + "  <bond atomRefs2='a24 a30' />" + "  <bond atomRefs2='a26 a27' />" + "  <bond atomRefs2='a26 a30' />" + "  <bond atomRefs2='a26 a31' />" + "  <bond atomRefs2='a27 a28' />" + "  <bond atomRefs2='a28 a29' />" + "  <bond atomRefs2='a29 a30' />" + "  <bond atomRefs2='a31 a32' />" + "  <bond atomRefs2='a31 a33' />" + "  <bond atomRefs2='a35 a36' />" + "  <bond atomRefs2='a35 a37' />" + "  <bond atomRefs2='a35 a38' />" + "  <bond atomRefs2='a38 a39' />" + "  <bond atomRefs2='a38 a40' />" + "  <bond atomRefs2='a38 a41' /> " + " </bondArray>" + " <formula formalCharge='0' concise='C 19 H 9 N 5 O 8'> " + "  <atomArray elementType='C H N O' count='19.0 9.0 5.0 8.0' />" + " </formula> " + "</molecule> " + CMLConstants.S_EMPTY;

    CMLMolecule nick = null;

    String[] nickOrder = new String[] { "1", "1", "1", "1", "2", "1", "1", "1", "1", "1", "1", "1", "3", "3", "1", "1", "2", "2", "1", "1", "2", "1", "2", "1", "1", "2", "1", "1", "2", "1", "1", "2", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1" };

    String styreneS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='styrene'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='1'/>" + "    <atom id='a2' elementType='C' hydrogenCount='1'/>" + "    <atom id='a3' elementType='C' hydrogenCount='1'/>" + "    <atom id='a4' elementType='C' hydrogenCount='1'/>" + "    <atom id='a5' elementType='C' hydrogenCount='1'/>" + "    <atom id='a6' elementType='C' hydrogenCount='0'/>" + "    <atom id='a7' elementType='C' hydrogenCount='1'/>" + "    <atom id='a8' elementType='C' hydrogenCount='2'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='A'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='A'/>" + "    <bond id='b3' atomRefs2='a3 a4' order='A'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='A'/>" + "    <bond id='b5' atomRefs2='a5 a6' order='A'/>" + "    <bond id='b6' atomRefs2='a6 a1' order='A'/>" + "    <bond id='b7' atomRefs2='a6 a7' order='1'/>" + "    <bond id='b8' atomRefs2='a7 a8' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule styrene = null;

    String[] styreneOrder = new String[] { "2", "1", "2", "1", "2", "1", "1", "2" };

    String pyreneS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='pyrene'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='0'/>" + "    <atom id='a2' elementType='C' hydrogenCount='0'/>" + "    <atom id='a3' elementType='C' hydrogenCount='1'/>" + "    <atom id='a4' elementType='C' hydrogenCount='1'/>" + "    <atom id='a5' elementType='C' hydrogenCount='1'/>" + "    <atom id='a6' elementType='C' hydrogenCount='0'/>" + "    <atom id='a11' elementType='C' hydrogenCount='0'/>" + "    <atom id='a12' elementType='C' hydrogenCount='0'/>" + "    <atom id='a13' elementType='C' hydrogenCount='1'/>" + "    <atom id='a14' elementType='C' hydrogenCount='1'/>" + "    <atom id='a15' elementType='C' hydrogenCount='1'/>" + "    <atom id='a16' elementType='C' hydrogenCount='0'/>" + "    <atom id='a21' elementType='C' hydrogenCount='1'/>" + "    <atom id='a22' elementType='C' hydrogenCount='1'/>" + "    <atom id='a61' elementType='C' hydrogenCount='1'/>" + "    <atom id='a62' elementType='C' hydrogenCount='1'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a3 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='1'/>" + "    <bond id='b5' atomRefs2='a5 a6' order='1'/>" + "    <bond id='b6' atomRefs2='a6 a1' order='1'/>" + "    <bond id='b11' atomRefs2='a11 a12' order='1'/>" + "    <bond id='b12' atomRefs2='a12 a13' order='1'/>" + "    <bond id='b13' atomRefs2='a13 a14' order='1'/>" + "    <bond id='b14' atomRefs2='a14 a15' order='1'/>" + "    <bond id='b15' atomRefs2='a15 a16' order='1'/>" + "    <bond id='b16' atomRefs2='a16 a11' order='1'/>" + "    <bond id='b17' atomRefs2='a1 a11' order='1'/>" + "    <bond id='b21' atomRefs2='a2 a21' order='1'/>" + "    <bond id='b22' atomRefs2='a21 a22' order='1'/>" + "    <bond id='b23' atomRefs2='a22 a12' order='1'/>" + "    <bond id='b61' atomRefs2='a6 a61' order='1'/>" + "    <bond id='b62' atomRefs2='a61 a62' order='1'/>" + "    <bond id='b63' atomRefs2='a62 a16' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule pyrene = null;

    String[] pyreneOrder = new String[] { "2", "1", "2", "1", "2", "1", "2", "1", "2", "1", "2", "1", "1", "1", "2", "1", "1", "2", "1" };

    String tripheneS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='triphene'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='0'/>" + "    <atom id='a2' elementType='C' hydrogenCount='0'/>" + "    <atom id='a3' elementType='C' hydrogenCount='1'/>" + "    <atom id='a4' elementType='C' hydrogenCount='1'/>" + "    <atom id='a5' elementType='C' hydrogenCount='1'/>" + "    <atom id='a6' elementType='C' hydrogenCount='0'/>" + "    <atom id='a11' elementType='C' hydrogenCount='0'/>" + "    <atom id='a12' elementType='C' hydrogenCount='1'/>" + "    <atom id='a16' elementType='C' hydrogenCount='1'/>" + "    <atom id='a21' elementType='C' hydrogenCount='1'/>" + "    <atom id='a22' elementType='C' hydrogenCount='1'/>" + "    <atom id='a61' elementType='C' hydrogenCount='1'/>" + "    <atom id='a62' elementType='C' hydrogenCount='1'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a3 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='1'/>" + "    <bond id='b5' atomRefs2='a5 a6' order='1'/>" + "    <bond id='b6' atomRefs2='a6 a1' order='1'/>" + "    <bond id='b11' atomRefs2='a11 a12' order='1'/>" + "    <bond id='b16' atomRefs2='a16 a11' order='1'/>" + "    <bond id='b17' atomRefs2='a1 a11' order='1'/>" + "    <bond id='b21' atomRefs2='a2 a21' order='1'/>" + "    <bond id='b22' atomRefs2='a21 a22' order='1'/>" + "    <bond id='b23' atomRefs2='a22 a12' order='1'/>" + "    <bond id='b61' atomRefs2='a6 a61' order='1'/>" + "    <bond id='b62' atomRefs2='a61 a62' order='1'/>" + "    <bond id='b63' atomRefs2='a62 a16' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule triphene = null;

    String[] tripheneOrder = new String[] { "2", "1", "2", "1", "2", "1", "1", "2", "1", "1", "1", "2", "1", "2", "1" };

    String methyleneCyclohexeneS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='methyleneCyclohexene'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='1'/>" + "    <atom id='a2' elementType='C' hydrogenCount='1'/>" + "    <atom id='a3' elementType='C' hydrogenCount='0'/>" + "    <atom id='a4' elementType='C' hydrogenCount='2'/>" + "    <atom id='a5' elementType='C' hydrogenCount='2'/>" + "    <atom id='a6' elementType='C' hydrogenCount='2'/>" + "    <atom id='a7' elementType='C' hydrogenCount='2'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a3 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='1'/>" + "    <bond id='b5' atomRefs2='a5 a6' order='1'/>" + "    <bond id='b6' atomRefs2='a6 a1' order='1'/>" + "    <bond id='b7' atomRefs2='a3 a7' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule methyleneCyclohexene = null;

    String[] methyleneCyclohexeneOrder = new String[] { "2", "1", "1", "1", "1", "1", "2" };

    String methyleneCyclohexadieneS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='methyleneCyclohexadiene'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='1'/>" + "    <atom id='a2' elementType='C' hydrogenCount='1'/>" + "    <atom id='a3' elementType='C' hydrogenCount='0'/>" + "    <atom id='a4' elementType='C' hydrogenCount='1'/>" + "    <atom id='a5' elementType='C' hydrogenCount='1'/>" + "    <atom id='a6' elementType='C' hydrogenCount='2'/>" + "    <atom id='a7' elementType='C' hydrogenCount='2'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a3 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='1'/>" + "    <bond id='b5' atomRefs2='a5 a6' order='1'/>" + "    <bond id='b6' atomRefs2='a6 a1' order='1'/>" + "    <bond id='b7' atomRefs2='a3 a7' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule methyleneCyclohexadiene = null;

    String[] methyleneCyclohexadieneOrder = new String[] { "2", "1", "1", "2", "1", "1", "2" };

    String co2S = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='co2'>" + "  <atomArray>" + "    <atom id='a1' elementType='O' hydrogenCount='0'/>" + "    <atom id='a2' elementType='C' hydrogenCount='0'/>" + "    <atom id='a3' elementType='O' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule co2 = null;

    String[] co2Order = new String[] { "2", "2" };

    String azuleneS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='azulene'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='1'/>" + "    <atom id='a2' elementType='C' hydrogenCount='1'/>" + "    <atom id='a3' elementType='C' hydrogenCount='1'/>" + "    <atom id='a4' elementType='C' hydrogenCount='0'/>" + "    <atom id='a5' elementType='C' hydrogenCount='1'/>" + "    <atom id='a6' elementType='C' hydrogenCount='1'/>" + "    <atom id='a7' elementType='C' hydrogenCount='1'/>" + "    <atom id='a8' elementType='C' hydrogenCount='1'/>" + "    <atom id='a9' elementType='C' hydrogenCount='1'/>" + "    <atom id='a10' elementType='C' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a3 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='1'/>" + "    <bond id='b5' atomRefs2='a5 a6' order='1'/>" + "    <bond id='b6' atomRefs2='a6 a7' order='1'/>" + "    <bond id='b7' atomRefs2='a7 a8' order='1'/>" + "    <bond id='b8' atomRefs2='a8 a9' order='1'/>" + "    <bond id='b9' atomRefs2='a9 a10' order='1'/>" + "    <bond id='b10' atomRefs2='a1 a10' order='1'/>" + "    <bond id='b11' atomRefs2='a4 a10' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule azulene = null;

    String[] azuleneOrder = new String[] { "2", "1", "2", "1", "2", "1", "2", "1", "2", "1", "1" };

    String conjugatedS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='conjugated'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='2'/>" + "    <atom id='a2' elementType='C' hydrogenCount='1'/>" + "    <atom id='a3' elementType='C' hydrogenCount='1'/>" + "    <atom id='a4' elementType='C' hydrogenCount='0'/>" + "    <atom id='a5' elementType='C' hydrogenCount='1'/>" + "    <atom id='a6' elementType='C' hydrogenCount='0'/>" + "    <atom id='a7' elementType='C' hydrogenCount='0'/>" + "    <atom id='a8' elementType='C' hydrogenCount='1'/>" + "    <atom id='a9' elementType='C' hydrogenCount='2'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a3 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='1'/>" + "    <bond id='b5' atomRefs2='a5 a6' order='1'/>" + "    <bond id='b6' atomRefs2='a6 a7' order='1'/>" + "    <bond id='b7' atomRefs2='a7 a8' order='1'/>" + "    <bond id='b8' atomRefs2='a8 a9' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule conjugated = null;

    String formate1S = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='formate1'>" + "  <atomArray>" + "    <atom id='a1' elementType='O' hydrogenCount='0' formalCharge='-1'/>" + "    <atom id='a2' elementType='C' hydrogenCount='1'/>" + "    <atom id='a3' elementType='O' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule formate1 = null;

    String formate2S = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='formate2'>" + "  <atomArray>" + "    <atom id='a1' elementType='O' hydrogenCount='1'/>" + "    <atom id='a2' elementType='C' hydrogenCount='1'/>" + "    <atom id='a3' elementType='O' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule formate2 = null;

    String formate3S = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='formate3'>" + "  <atomArray>" + "    <atom id='a1' elementType='O' hydrogenCount='0'/>" + "    <atom id='a2' elementType='C' hydrogenCount='1'/>" + "    <atom id='a3' elementType='O' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule formate3 = null;

    String pyridineS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='pyridine'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='1'/>" + "    <atom id='a2' elementType='C' hydrogenCount='1'/>" + "    <atom id='a3' elementType='C' hydrogenCount='1'/>" + "    <atom id='a4' elementType='C' hydrogenCount='1'/>" + "    <atom id='a5' elementType='C' hydrogenCount='1'/>" + "    <atom id='a6' elementType='N' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a3 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='1'/>" + "    <bond id='b5' atomRefs2='a5 a6' order='1'/>" + "    <bond id='b6' atomRefs2='a6 a1' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule pyridine = null;

    String pyridiniumS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='pyridinium'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='1'/>" + "    <atom id='a2' elementType='C' hydrogenCount='1'/>" + "    <atom id='a3' elementType='C' hydrogenCount='1'/>" + "    <atom id='a4' elementType='C' hydrogenCount='1'/>" + "    <atom id='a5' elementType='C' hydrogenCount='1'/>" + "    <atom id='a6' elementType='N' hydrogenCount='1' formalCharge='1'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a3 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='1'/>" + "    <bond id='b5' atomRefs2='a5 a6' order='1'/>" + "    <bond id='b6' atomRefs2='a6 a1' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule pyridinium = null;

    String pyridone4S = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='pyridone4'>" + "  <atomArray>" + "    <atom id='a1' elementType='N' hydrogenCount='1'/>" + "    <atom id='a2' elementType='C' hydrogenCount='1'/>" + "    <atom id='a3' elementType='C' hydrogenCount='1'/>" + "    <atom id='a4' elementType='C' hydrogenCount='0'/>" + "    <atom id='a5' elementType='C' hydrogenCount='1'/>" + "    <atom id='a6' elementType='C' hydrogenCount='1'/>" + "    <atom id='a7' elementType='O' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a3 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='1'/>" + "    <bond id='b5' atomRefs2='a5 a6' order='1'/>" + "    <bond id='b6' atomRefs2='a6 a1' order='1'/>" + "    <bond id='b7' atomRefs2='a4 a7' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule pyridone4 = null;

    String nitroMethaneS = "" + "<molecule " + CMLConstants.CML_XMLNS + " title='MeN+(-O)O-'>" + "  <atomArray>" + "    <atom id='a1' elementType='N' hydrogenCount='0' formalCharge='1'/>" + "    <atom id='a2' elementType='C' hydrogenCount='3'/>" + "    <atom id='a3' elementType='O' hydrogenCount='0' formalCharge='-1'/>" + "    <atom id='a4' elementType='O' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a1 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a1 a4' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule nitroMethane = null;

    String nitricS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='nitric'>" + "  <atomArray>" + "    <atom id='a1' elementType='N' hydrogenCount='0'/>" + "    <atom id='a2' elementType='O' hydrogenCount='0'/>" + "    <atom id='a3' elementType='O' hydrogenCount='0' formalCharge='-1'/>" + "    <atom id='a4' elementType='O' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a1 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a1 a4' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule nitric = null;

    String oxalateS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='oxalate'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='0'/>" + "    <atom id='a2' elementType='O' hydrogenCount='0'/>" + "    <atom id='a3' elementType='O' hydrogenCount='0' formalCharge='-1'/>" + "    <atom id='a4' elementType='C' hydrogenCount='0'/>" + "    <atom id='a5' elementType='O' hydrogenCount='0'/>" + "    <atom id='a6' elementType='O' hydrogenCount='0' formalCharge='-1'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a1 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a1 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='1'/>" + "    <bond id='b5' atomRefs2='a4 a6' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule oxalate = null;

    String methylammoniumS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='methylammonium' formalCharge='1'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='3'/>" + "    <atom id='a2' elementType='N' hydrogenCount='3'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule methylammonium = null;

    String pyridinium1S = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='pyridinium1' formalCharge='1'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='1'/>" + "    <atom id='a2' elementType='C' hydrogenCount='1'/>" + "    <atom id='a3' elementType='C' hydrogenCount='1'/>" + "    <atom id='a4' elementType='C' hydrogenCount='1'/>" + "    <atom id='a5' elementType='C' hydrogenCount='1'/>" + "    <atom id='a6' elementType='N' hydrogenCount='1'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='2'/>" + "    <bond id='b3' atomRefs2='a3 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='2'/>" + "    <bond id='b5' atomRefs2='a5 a6' order='1'/>" + "    <bond id='b6' atomRefs2='a6 a1' order='2'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule pyridinium1 = null;

    String oxalate2S = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='oxalate2' formalCharge='-2'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='0'/>" + "    <atom id='a2' elementType='O' hydrogenCount='0'/>" + "    <atom id='a3' elementType='O' hydrogenCount='0'/>" + "    <atom id='a4' elementType='C' hydrogenCount='0'/>" + "    <atom id='a5' elementType='O' hydrogenCount='0'/>" + "    <atom id='a6' elementType='O' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a1 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a1 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='1'/>" + "    <bond id='b5' atomRefs2='a4 a6' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule oxalate2 = null;

    String diMethylIminiumS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='diMethylIminium Me2N-CH2(+)'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='2'/>" + "    <atom id='a2' elementType='N' hydrogenCount='0'/>" + "    <atom id='a3' elementType='C' hydrogenCount='3'/>" + "    <atom id='a4' elementType='C' hydrogenCount='3'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a2 a4' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule diMethylIminium = null;

    String munchnoneS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='munchnone'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='0'/>" + "    <atom id='a2' elementType='O' hydrogenCount='0'/>" + "    <atom id='a3' elementType='C' hydrogenCount='1'/>" + "    <atom id='a4' elementType='N' hydrogenCount='1'/>" + "    <atom id='a5' elementType='C' hydrogenCount='1'/>" + "    <atom id='a6' elementType='O' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a3 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='1'/>" + "    <bond id='b5' atomRefs2='a1 a5' order='1'/>" + "    <bond id='b6' atomRefs2='a1 a6' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule munchnone = null;

    String nitric2S = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='ON(O)O'>" + "  <atomArray>" + "    <atom id='a1' elementType='O' hydrogenCount='0'/>" + "    <atom id='a2' elementType='N' hydrogenCount='0'/>" + "    <atom id='a3' elementType='O' hydrogenCount='0'/>" + "    <atom id='a4' elementType='O' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a2 a4' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule nitric2 = null;

    String nitroMethane2S = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='CH3N(O)O'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='3'/>" + "    <atom id='a2' elementType='N' hydrogenCount='0'/>" + "    <atom id='a3' elementType='O' hydrogenCount='0'/>" + "    <atom id='a4' elementType='O' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a2 a4' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule nitroMethane2 = null;

    String carbonate2S = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='OC(O)O'>" + "  <atomArray>" + "    <atom id='a1' elementType='O' hydrogenCount='0'/>" + "    <atom id='a2' elementType='C' hydrogenCount='0'/>" + "    <atom id='a3' elementType='O' hydrogenCount='0'/>" + "    <atom id='a4' elementType='O' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a2 a4' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule carbonate2 = null;

    String hydrogenSulfateS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='HSO4'>" + "  <atomArray>" + "    <atom id='a1' elementType='S' hydrogenCount='0'/>" + "    <atom id='a2' elementType='O' hydrogenCount='0'/>" + "    <atom id='a3' elementType='O' hydrogenCount='1'/>" + "    <atom id='a4' elementType='O' hydrogenCount='0'/>" + "    <atom id='a5' elementType='O' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a1 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a1 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a1 a5' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule hydrogenSulfate = null;

    String methaneSulfonateS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='methaneSulfonate'>" + "  <atomArray>" + "    <atom id='a1' elementType='S' hydrogenCount='0'/>" + "    <atom id='a2' elementType='C' hydrogenCount='3'/>" + "    <atom id='a3' elementType='O' hydrogenCount='0'/>" + "    <atom id='a4' elementType='O' hydrogenCount='0'/>" + "    <atom id='a5' elementType='O' hydrogenCount='0'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a1 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a1 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a1 a5' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule methaneSulfonate = null;

    String benzophenoneS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='benzophenone'>" + "  <atomArray>" + "    <atom id='a1' elementType='C' hydrogenCount='1'/>" + "    <atom id='a2' elementType='C' hydrogenCount='1'/>" + "    <atom id='a3' elementType='C' hydrogenCount='1'/>" + "    <atom id='a4' elementType='C' hydrogenCount='1'/>" + "    <atom id='a5' elementType='C' hydrogenCount='1'/>" + "    <atom id='a6' elementType='C' hydrogenCount='0'/>" + "    <atom id='a7' elementType='C' hydrogenCount='0'/>" + "    <atom id='a8' elementType='O' hydrogenCount='0'/>" + "    <atom id='a9' elementType='C' hydrogenCount='0'/>" + "    <atom id='a10' elementType='C' hydrogenCount='1'/>" + "    <atom id='a11' elementType='C' hydrogenCount='1'/>" + "    <atom id='a12' elementType='C' hydrogenCount='1'/>" + "    <atom id='a13' elementType='C' hydrogenCount='1'/>" + "    <atom id='a14' elementType='C' hydrogenCount='1'/>" + "  </atomArray>" + "  <bondArray>" + "    <bond id='b1' atomRefs2='a1 a2' order='1'/>" + "    <bond id='b2' atomRefs2='a2 a3' order='1'/>" + "    <bond id='b3' atomRefs2='a3 a4' order='1'/>" + "    <bond id='b4' atomRefs2='a4 a5' order='1'/>" + "    <bond id='b5' atomRefs2='a5 a6' order='1'/>" + "    <bond id='b6' atomRefs2='a6 a1' order='1'/>" + "    <bond id='b7' atomRefs2='a6 a7' order='1'/>" + "    <bond id='b8' atomRefs2='a7 a8' order='1'/>" + "    <bond id='b9' atomRefs2='a7 a9' order='1'/>" + "    <bond id='b10' atomRefs2='a9 a10' order='1'/>" + "    <bond id='b11' atomRefs2='a10 a11' order='1'/>" + "    <bond id='b12' atomRefs2='a11 a12' order='1'/>" + "    <bond id='b13' atomRefs2='a12 a13' order='1'/>" + "    <bond id='b14' atomRefs2='a13 a14' order='1'/>" + "    <bond id='b15' atomRefs2='a9 a14' order='1'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule benzophenone = null;

    String sproutS = CMLConstants.S_EMPTY + "<molecule " + CMLConstants.CML_XMLNS + " title='sprout'>" + "  <atomArray>" + "    <atom id='a1' elementType='C'/>" + "    <atom id='a2' elementType='C'/>" + "    <atom id='a3' elementType='C'/>" + "    <atom id='a4' elementType='C'/>" + "    <atom id='a5' elementType='C'/>" + "    <atom id='a6' elementType='C'/>" + "    <atom id='a7' elementType='F'/>" + "    <atom id='a8' elementType='Cl'/>" + "    <atom id='a9' elementType='Br'/>" + "    <atom id='a10' elementType='I'/>" + "    <atom id='a11' elementType='H'/>" + "    <atom id='a12' elementType='C'/>" + "    <atom id='a13' elementType='O'/>" + "   </atomArray>" + "   <bondArray>" + "     <bond id='a1 a2' atomRefs2='a1 a2'/>" + "     <bond id='a2 a3' atomRefs2='a2 a3'/>" + "     <bond id='a3 a4' atomRefs2='a3 a4'/>" + "     <bond id='a4 a5' atomRefs2='a4 a5'/>" + "     <bond id='a5 a6' atomRefs2='a5 a6'/>" + "     <bond id='a1 a6' atomRefs2='a1 a6'/>" + "     <bond id='a1 a7' atomRefs2='a1 a7'/>" + "     <bond id='a2 a8' atomRefs2='a2 a8'/>" + "     <bond id='a3 a9' atomRefs2='a3 a9'/>" + "     <bond id='a4 a10' atomRefs2='a4 a10'/>" + "     <bond id='a5 a11' atomRefs2='a5 a11'/>" + "     <bond id='a6 a12' atomRefs2='a6 a12'/>" + "     <bond id='a12 a13' atomRefs2='a12 a13'/>" + "  </bondArray>" + "</molecule>" + CMLConstants.S_EMPTY;

    CMLMolecule sprout = null;

    public MoleculeToolFixture() {
        fixture.makeMol1();
        moleculeTool1 = MoleculeTool.getOrCreateTool(fixture.mol1);
        moleculeToolXom0 = MoleculeTool.getOrCreateTool(fixture.xomAtom[0].getMolecule());
        moleculeToolXml0 = MoleculeTool.getOrCreateTool(fixture.xmlAtom[0].getMolecule());
        moleculeToolBond0 = MoleculeTool.getOrCreateTool(fixture.xmlBonds.get(0).getMolecule());
        benzene = makeMol(benzene, benzeneS);
        nick = makeMol(nick, nickS);
        pyrene = makeMol(pyrene, pyreneS);
        triphene = makeMol(triphene, tripheneS);
        styrene = makeMol(styrene, styreneS);
        methyleneCyclohexene = makeMol(methyleneCyclohexene, methyleneCyclohexeneS);
        methyleneCyclohexadiene = makeMol(methyleneCyclohexadiene, methyleneCyclohexadieneS);
        co2 = makeMol(co2, co2S);
        azulene = makeMol(azulene, azuleneS);
        conjugated = makeMol(conjugated, conjugatedS);
        formate1 = makeMol(formate1, formate1S);
        formate2 = makeMol(formate2, formate2S);
        formate3 = makeMol(formate3, formate3S);
        pyridine = makeMol(pyridine, pyridineS);
        pyridinium = makeMol(pyridinium, pyridiniumS);
        pyridone4 = makeMol(pyridone4, pyridone4S);
        nitroMethane = makeMol(nitroMethane, nitroMethaneS);
        nitric = makeMol(nitric, nitricS);
        oxalate = makeMol(oxalate, oxalateS);
        benzophenone = makeMol(benzophenone, benzophenoneS);
        methylammonium = makeMol(methylammonium, methylammoniumS);
        munchnone = makeMol(munchnone, munchnoneS);
        pyridinium1 = makeMol(pyridinium1, pyridinium1S);
        oxalate2 = makeMol(oxalate2, oxalate2S);
        diMethylIminium = makeMol(diMethylIminium, diMethylIminiumS);
        nitric2 = makeMol(nitric2, nitric2S);
        nitroMethane2 = makeMol(nitroMethane2, nitroMethane2S);
        carbonate2 = makeMol(carbonate2, carbonate2S);
        hydrogenSulfate = makeMol(hydrogenSulfate, hydrogenSulfateS);
        methaneSulfonate = makeMol(methaneSulfonate, methaneSulfonateS);
        sprout = makeMol(sprout, sproutS);
    }

    private CMLMolecule makeMol(CMLMolecule mol, String s) {
        if (mol == null) {
            mol = (CMLMolecule) JumboTestUtils.parseValidString(s);
        }
        return mol;
    }

    public void setFixture(MoleculeAtomBondFixture fixture) {
        this.fixture = fixture;
    }

    public MoleculeAtomBondFixture getFixture() {
        return fixture;
    }

    public void makeMoleculeTool1() {
        fixture.makeMol1();
        moleculeTool1 = MoleculeTool.getOrCreateTool(fixture.mol1);
    }

    public void makeMoleculeTool2() {
        fixture.makeMol2();
        moleculeTool2 = MoleculeTool.getOrCreateTool(fixture.mol2);
    }

    public void makeMoleculeTool3() {
        fixture.makeMol3();
        moleculeTool3 = MoleculeTool.getOrCreateTool(fixture.mol3);
    }

    public void makeMoleculeTool4() {
        fixture.makeMol4();
        moleculeTool4 = MoleculeTool.getOrCreateTool(fixture.mol4);
    }

    public void makeMoleculeTool5() {
        fixture.makeMol5();
        moleculeTool5 = MoleculeTool.getOrCreateTool(fixture.mol5);
    }

    public void makeMoleculeTool5a() {
        fixture.makeMol5a();
        moleculeTool5a = MoleculeTool.getOrCreateTool(fixture.mol5a);
    }

    public void makeMoleculeTool6() {
        fixture.makeMol6();
        moleculeTool6 = MoleculeTool.getOrCreateTool(fixture.mol6);
    }

    public void makeMoleculeTool7() {
        fixture.makeMol7();
        moleculeTool7 = MoleculeTool.getOrCreateTool(fixture.mol7);
    }

    public void makeMoleculeTool8() {
        fixture.makeMol8();
        moleculeTool8 = MoleculeTool.getOrCreateTool(fixture.mol8);
    }

    public void makeMoleculeTool9() {
        fixture.makeMol9();
        moleculeTool9 = MoleculeTool.getOrCreateTool(fixture.mol9);
    }

    public void makeMoleculeTool10() {
        fixture.makeMol10();
        moleculeTool10 = MoleculeTool.getOrCreateTool(fixture.mol10);
    }

    public void makeMoleculeToolXomAtom0() {
        moleculeToolXom0 = MoleculeTool.getOrCreateTool(fixture.xomAtom[0].getMolecule());
    }

    public void makeMoleculeToolXmlAtom0() {
        moleculeToolXml0 = MoleculeTool.getOrCreateTool(fixture.xmlAtom[0].getMolecule());
    }

    public void makeMoleculeToolBond0() {
        moleculeToolBond0 = MoleculeTool.getOrCreateTool(fixture.xomBond[0].getMolecule());
    }

    public void makeMoleculeToolXmlBonds() {
        moleculeToolXmlBonds = MoleculeTool.getOrCreateTool(fixture.xmlBonds.get(0).getMolecule());
    }
}
