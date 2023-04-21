package org.fudaa.dodico.rubar.io;

import java.io.File;
import java.io.IOException;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.dodico.common.TestIO;
import org.fudaa.dodico.h2d.rubar.H2dRubarTimeConditionMutable;
import org.fudaa.dodico.mesure.EvolutionReguliere;

/**
 * @author CANEL Christophe (Genesis)
 *
 */
public class TestRubarCLIFileFormat extends TestIO {

    public TestRubarCLIFileFormat() {
        super("lmfa05.cli");
    }

    public void testLecture() {
        assertEquals(getConditions(), read("lmfa05.cli"));
        assertEquals(getConditionsWithTransOld(), read("lmfa05_trans_old.cli"));
        assertEquals(getConditionsWithTransNew(), read("lmfa05_trans_new.cli"));
    }

    private void assertEquals(H2dRubarTimeConditionMutable[] expected, H2dRubarTimeConditionMutable[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    private void assertEquals(H2dRubarTimeConditionMutable expected, H2dRubarTimeConditionMutable actual) {
        assertEquals(expected.isTrans(), actual.isTrans());
        assertEquals(expected.getHEvol(), actual.getHEvol());
        assertEquals(expected.getQnEvol(), actual.getQnEvol());
        assertEquals(expected.getQtEvol(), actual.getQtEvol());
        if (expected.isTrans()) {
            assertEquals(expected.getHCatEvol(), actual.getHCatEvol());
            assertEquals(expected.getDiamEvol(), actual.getDiamEvol());
            assertEquals(expected.getEtEvol(), actual.getEtEvol());
        }
    }

    private void assertEquals(EvolutionReguliere expected, EvolutionReguliere actual) {
        assertEquals(expected.getNbValues(), actual.getNbValues());
        for (int i = 0; i < expected.getNbValues(); i++) {
            assertDoubleEquals(expected.getY(i), actual.getY(i));
            assertDoubleEquals(expected.getX(i), actual.getX(i));
        }
    }

    public H2dRubarTimeConditionMutable[] getConditionsWithTransNew() {
        H2dRubarTimeConditionMutable[] conditions = new H2dRubarTimeConditionMutable[3];
        conditions[0] = new H2dRubarTimeConditionMutable(2, true);
        conditions[0].h_.add(0.0, 0.2);
        conditions[0].h_.add(0.436, 1.8);
        conditions[0].qn_.add(0.0, 0.004);
        conditions[0].qn_.add(0.436, 0.0184);
        conditions[0].qt_.add(0.0, 0.623);
        conditions[0].qt_.add(0.436, 14.7);
        conditions[0].hcat_.add(0.0, 0.5);
        conditions[0].hcat_.add(0.436, 0.0);
        conditions[0].diam_.add(0.0, 0.1);
        conditions[0].diam_.add(0.436, 0.9);
        conditions[0].et_.add(0.0, 1.0);
        conditions[0].et_.add(0.436, 2.3);
        conditions[1] = new H2dRubarTimeConditionMutable(2, true);
        conditions[1].h_.add(0.0, 0.7);
        conditions[1].h_.add(0.436, 0.7);
        conditions[1].qn_.add(0.0, 0.0063);
        conditions[1].qn_.add(0.436, 0.0063);
        conditions[1].qt_.add(0.0, 2.3);
        conditions[1].qt_.add(0.436, 2.3);
        conditions[1].hcat_.add(0.0, 1.0);
        conditions[1].hcat_.add(0.436, 1.0);
        conditions[1].diam_.add(0.0, 0.4);
        conditions[1].diam_.add(0.436, 0.4);
        conditions[1].et_.add(0.0, 1.7);
        conditions[1].et_.add(0.436, 1.7);
        conditions[2] = new H2dRubarTimeConditionMutable(2, true);
        conditions[2].h_.add(0.0, 1.8);
        conditions[2].h_.add(0.436, 0.2);
        conditions[2].qn_.add(0.0, 0.0184);
        conditions[2].qn_.add(0.436, 0.004);
        conditions[2].qt_.add(0.0, 14.7);
        conditions[2].qt_.add(0.436, 0.623);
        conditions[2].hcat_.add(0.0, 0.0);
        conditions[2].hcat_.add(0.436, 0.5);
        conditions[2].diam_.add(0.0, 0.9);
        conditions[2].diam_.add(0.436, 0.1);
        conditions[2].et_.add(0.0, 2.3);
        conditions[2].et_.add(0.436, 1.0);
        return conditions;
    }

    public H2dRubarTimeConditionMutable[] getConditionsWithTransOld() {
        H2dRubarTimeConditionMutable[] conditions = new H2dRubarTimeConditionMutable[3];
        conditions[0] = new H2dRubarTimeConditionMutable(2, true);
        conditions[0].h_.add(0.0, 0.2);
        conditions[0].h_.add(0.436, 1.8);
        conditions[0].qn_.add(0.0, 0.004);
        conditions[0].qn_.add(0.436, 0.0184);
        conditions[0].qt_.add(0.0, 0.623);
        conditions[0].qt_.add(0.436, 14.7);
        conditions[0].hcat_.add(0.0, 0.5);
        conditions[0].hcat_.add(0.436, 0.0);
        conditions[0].diam_.add(0.0, 0.0);
        conditions[0].diam_.add(0.436, 0.0);
        conditions[0].et_.add(0.0, 0.0);
        conditions[0].et_.add(0.436, 0.0);
        conditions[1] = new H2dRubarTimeConditionMutable(2, true);
        conditions[1].h_.add(0.0, 0.7);
        conditions[1].h_.add(0.436, 0.7);
        conditions[1].qn_.add(0.0, 0.0063);
        conditions[1].qn_.add(0.436, 0.0063);
        conditions[1].qt_.add(0.0, 2.3);
        conditions[1].qt_.add(0.436, 2.3);
        conditions[1].hcat_.add(0.0, 1.0);
        conditions[1].hcat_.add(0.436, 1.0);
        conditions[1].diam_.add(0.0, 0.0);
        conditions[1].diam_.add(0.436, 0.0);
        conditions[1].et_.add(0.0, 0.0);
        conditions[1].et_.add(0.436, 0.0);
        conditions[2] = new H2dRubarTimeConditionMutable(2, true);
        conditions[2].h_.add(0.0, 1.8);
        conditions[2].h_.add(0.436, 0.2);
        conditions[2].qn_.add(0.0, 0.0184);
        conditions[2].qn_.add(0.436, 0.004);
        conditions[2].qt_.add(0.0, 14.7);
        conditions[2].qt_.add(0.436, 0.623);
        conditions[2].hcat_.add(0.0, 0.0);
        conditions[2].hcat_.add(0.436, 0.5);
        conditions[2].diam_.add(0.0, 0.0);
        conditions[2].diam_.add(0.436, 0.0);
        conditions[2].et_.add(0.0, 0.0);
        conditions[2].et_.add(0.436, 0.0);
        return conditions;
    }

    public H2dRubarTimeConditionMutable[] getConditions() {
        H2dRubarTimeConditionMutable[] conditions = new H2dRubarTimeConditionMutable[3];
        conditions[0] = new H2dRubarTimeConditionMutable(2, false);
        conditions[0].h_.add(0.0, 0.2);
        conditions[0].h_.add(0.436, 1.8);
        conditions[0].qn_.add(0.0, 0.004);
        conditions[0].qn_.add(0.436, 0.0184);
        conditions[0].qt_.add(0.0, 0.623);
        conditions[0].qt_.add(0.436, 14.7);
        conditions[1] = new H2dRubarTimeConditionMutable(2, false);
        conditions[1].h_.add(0.0, 0.7);
        conditions[1].h_.add(0.436, 0.7);
        conditions[1].qn_.add(0.0, 0.0063);
        conditions[1].qn_.add(0.436, 0.0063);
        conditions[1].qt_.add(0.0, 2.3);
        conditions[1].qt_.add(0.436, 2.3);
        conditions[2] = new H2dRubarTimeConditionMutable(2, false);
        conditions[2].h_.add(0.0, 1.8);
        conditions[2].h_.add(0.436, 0.2);
        conditions[2].qn_.add(0.0, 0.0184);
        conditions[2].qn_.add(0.436, 0.004);
        conditions[2].qt_.add(0.0, 14.7);
        conditions[2].qt_.add(0.436, 0.623);
        return conditions;
    }

    private static H2dRubarTimeConditionMutable[] read(String file) {
        return read(getFile(TestRubarCLIFileFormat.class, file));
    }

    private static H2dRubarTimeConditionMutable[] read(File file) {
        final RubarCLIReader reader = (RubarCLIReader) new RubarCLIFileFormat().createReader();
        reader.setFile(file);
        final CtuluIOOperationSynthese result = reader.read();
        assertFalse(result.containsFatalError());
        return (H2dRubarTimeConditionMutable[]) result.getSource();
    }

    public void testEcriture() {
        H2dRubarTimeConditionMutable[] conditions = getConditions();
        final RubarCLIWriter writer = (RubarCLIWriter) new RubarCLIFileFormat().createWriter();
        File file = null;
        try {
            file = File.createTempFile("Test", "RubarCLIFileFormat");
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        writer.setFile(file);
        CtuluIOOperationSynthese operation = writer.write(conditions);
        assertFalse(operation.containsFatalError());
        assertEquals(getConditions(), read(file));
        conditions = getConditionsWithTransNew();
        try {
            file = File.createTempFile("Test", "RubarCLIFileFormat");
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        writer.setFile(file);
        operation = writer.write(conditions);
        assertFalse(operation.containsFatalError());
        assertEquals(getConditionsWithTransNew(), read("lmfa05_trans_new.cli"));
    }
}
