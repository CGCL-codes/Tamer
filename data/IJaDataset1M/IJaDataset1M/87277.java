package org.fudaa.dodico.rubar.io;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.dodico.common.TestIO;
import org.fudaa.dodico.mesure.EvolutionReguliere;

/**
 * @author CANEL Christophe (Genesis)
 *
 */
public class TestRubarVENFileFormat extends TestIO {

    private static final int NB_ELEM = 9860;

    public TestRubarVENFileFormat() {
        super("lmfa05.ven");
    }

    public void testLecture() {
        assertCorrect(read(this.fic_));
    }

    private void assertCorrect(RubarVENResult result) {
        assertEquals(NB_ELEM, result.getNbElt());
        assertEquals(0, result.getEvolIdx(0));
        assertEquals(0, result.getEvolIdx(1));
        assertEquals(2, result.getEvolIdx(2));
        assertEquals(1, result.getEvolIdx(3));
        assertEquals(1, result.getEvolIdx(4));
        assertEquals(0, result.getEvolIdx(5));
        assertEquals(2, result.getEvolIdx(6));
        assertEquals(1, result.getEvolIdx(7));
        assertEquals(2, result.getEvolIdx(8));
        assertEquals(2, result.getEvolIdx(9));
        for (int i = 10; i < NB_ELEM; i++) {
            assertEquals(0, result.getEvolIdx(i));
        }
        assertEquals(3, result.getNbEvolX());
        assertEquals(3, result.getNbEvolY());
        assertEquals(new double[] { 1.0, 1.0 }, result.getEvolutionAlongX(0));
        assertEquals(new double[] { 1.0, 0.0 }, result.getEvolutionAlongY(0));
        assertEquals(new double[] { 2.0 }, result.getEvolutionAlongX(1));
        assertEquals(new double[] { 1.5 }, result.getEvolutionAlongY(1));
        assertEquals(new double[] { 1.0, 3.0, 2.0 }, result.getEvolutionAlongX(2));
        assertEquals(new double[] { 1.0, 2.0, 3.0 }, result.getEvolutionAlongY(2));
    }

    private void assertEquals(double[] value, EvolutionReguliere evo) {
        assertEquals(value.length, evo.getNbValues());
        for (int i = 0; i < value.length; i++) {
            assertDoubleEquals(value[i], evo.getY(i));
            assertDoubleEquals((double) i, evo.getX(i));
        }
    }

    private static RubarVENResult read(File file) {
        final RubarVENReader reader = (RubarVENReader) new RubarVENFileFormat().createReader();
        reader.setNbElt(NB_ELEM);
        reader.setFile(file);
        final CtuluIOOperationSynthese result = reader.read();
        assertFalse(result.containsFatalError());
        return (RubarVENResult) result.getSource();
    }

    public void testEcriture() {
        RubarVENResult result = new RubarVENResult();
        result.eltEvolIdx = new int[NB_ELEM];
        Arrays.fill(result.eltEvolIdx, 0);
        result.eltEvolIdx[2] = 2;
        result.eltEvolIdx[3] = 1;
        result.eltEvolIdx[4] = 1;
        result.eltEvolIdx[6] = 2;
        result.eltEvolIdx[7] = 1;
        result.eltEvolIdx[8] = 2;
        result.eltEvolIdx[9] = 2;
        result.evolsX = new EvolutionReguliere[3];
        result.evolsY = new EvolutionReguliere[3];
        result.evolsX[0] = new EvolutionReguliere(new double[] { 0.0, 1.0 }, new double[] { 1.0, 1.0 }, true);
        result.evolsY[0] = new EvolutionReguliere(new double[] { 0.0, 1.0 }, new double[] { 1.0, 0.0 }, true);
        result.evolsX[1] = new EvolutionReguliere(new double[] { 0.0 }, new double[] { 2.0 }, true);
        result.evolsY[1] = new EvolutionReguliere(new double[] { 0.0 }, new double[] { 1.5 }, true);
        result.evolsX[2] = new EvolutionReguliere(new double[] { 0.0, 1.0, 2.0 }, new double[] { 1.0, 3.0, 2.0 }, true);
        result.evolsY[2] = new EvolutionReguliere(new double[] { 0.0, 1.0, 2.0 }, new double[] { 1.0, 2.0, 3.0 }, true);
        final RubarVENWriter writer = (RubarVENWriter) new RubarVENFileFormat().createWriter();
        File file = null;
        try {
            file = File.createTempFile("Test", "RubarVENFileFormat");
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        writer.setFile(file);
        final CtuluIOOperationSynthese operation = writer.write(result);
        assertFalse(operation.containsFatalError());
        assertCorrect(read(file));
    }
}
