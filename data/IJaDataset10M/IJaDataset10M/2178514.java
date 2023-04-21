package backend.parser.aracyc2.parser.concret;

import backend.parser.aracyc2.sink.AbstractNode;
import backend.parser.aracyc2.sink.Enzyme;
import backend.parser.aracyc2.sink.Protein;
import backend.parser.aracyc2.sink.factory.SinkFactory;

public class ProteinParser extends AbstractParser {

    private Protein protein;

    public void distribute(String key, String value) throws Exception {
        if (key.equals("CATALYZES")) {
            this.addCatalyzes(value);
        } else if (key.equals("COMPONENT-OF")) this.addComponentOf(value); else if (key.equals("TYPES") && value.equals("Protein-Complexes")) {
            protein.setComplex(true);
        }
    }

    public void addComponentOf(String value) throws Exception {
        Protein component = (Protein) SinkFactory.getInstance().create(Protein.class, value);
        protein.addComponent(component);
    }

    public void addCatalyzes(String value) throws Exception {
        try {
            Enzyme enzyme = (Enzyme) SinkFactory.getInstance().create(Enzyme.class, value);
            if (enzyme.addIs_a(protein) == true) {
                protein.addIsMemberOf(enzyme);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AbstractNode getNode() {
        return protein;
    }

    public void start(String uniqueId) throws Exception {
        protein = (Protein) SinkFactory.getInstance().create(Protein.class, uniqueId);
    }
}
