package tests;

import es.ulpgc.dis.heuriskein.model.solver.Population;
import es.ulpgc.dis.heuristicide.xml.PopulationXMLReader;

public class PopulationReadWriteTest {

    private PopulationReadWriteTest() {
    }

    public static void main(String[] args) {
        try {
            Population pop = PopulationXMLReader.read("/home/oscar/temp/eclipse/DefaultProjectName/populations/Population0.xml");
            System.out.println(pop.toHumanReadableString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
