package de.tum.in.botl.adapter.impl;

import java.io.IOException;
import java.util.zip.ZipException;
import junit.framework.TestCase;
import de.tum.in.botl.adapter.AdapterFactory;
import de.tum.in.botl.adapter.FileExporter;
import de.tum.in.botl.adapter.FileImporter;
import de.tum.in.botl.metamodel.Metamodel;
import de.tum.in.botl.model.ModelFragment;
import de.tum.in.botl.ruleSet.RuleSet;
import de.tum.in.botl.ruleSet.RuleSetFactory;
import de.tum.in.botl.test.TestHelper;
import de.tum.in.botl.util.Config;

public class Poseidon16AdapterTest extends TestCase {

    private AdapterFactory adapterFactory;

    private RuleSetFactory ruleSetFactory;

    RuleSet rs;

    protected void setUp() throws Exception {
        super.setUp();
        adapterFactory = Config.getInstance().getAdapterFactory();
        ;
        ruleSetFactory = Config.getInstance().getDefaultRuleSetFactory();
        rs = ruleSetFactory.load("examples/poseidon16/trafo.botl.xml");
    }

    public void testArgoAdapter() throws ImportException, ExportException, ZipException, IOException {
        Metamodel mm = rs.getSourceMetamodel("UML Source");
        FileImporter poseidonImp = adapterFactory.getPoseidon16Importer(mm);
        ModelFragment mf = poseidonImp.start("examples/poseidon16/input.zargo");
        FileImporter xmlImp = adapterFactory.getGenericXMLImporter(mm);
        ModelFragment xmlMf = xmlImp.start("examples/poseidon16/input.xml");
        assertEquals(TestHelper.getStringWithoutIds(xmlMf), TestHelper.getStringWithoutIds(mf));
        FileExporter poseidonExp = adapterFactory.getPoseidon16Exporter();
        poseidonExp.start(xmlMf, "examples/poseidon16/tmp.xmi");
        ModelFragment xmlMf2 = poseidonImp.start("examples/poseidon16/tmp.xmi");
        assertEquals(TestHelper.getStringWithoutIds(xmlMf), TestHelper.getStringWithoutIds(xmlMf2));
    }
}
