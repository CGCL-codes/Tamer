package ch.olsen.routes.atom.lib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.olsen.products.util.configuration.Configuration;
import ch.olsen.products.util.configuration.InternalConfigurationException;
import ch.olsen.products.util.configuration.ListOfUnnamedProperties;
import ch.olsen.products.util.configuration.StringProperty;
import ch.olsen.products.util.configuration.ListOfNamedProperties.PropertyFactory;
import ch.olsen.routes.atom.Atom;
import ch.olsen.routes.atom.AtomAbstr;
import ch.olsen.routes.atom.AtomException;
import ch.olsen.routes.atom.AtomInput;
import ch.olsen.routes.atom.AtomInputAbstr;
import ch.olsen.routes.atom.AtomOutputAbstr;
import ch.olsen.routes.atom.LinkOutput;
import ch.olsen.routes.atom.RoutesStep;
import ch.olsen.routes.cell.library.LibraryAutoDeploy;
import ch.olsen.routes.data.AggregatedDataElement;
import ch.olsen.routes.data.DataElement;
import ch.olsen.routes.data.DataType;
import ch.olsen.routes.data.NullDataElement;
import ch.olsen.routes.framework.RoutesFramework;

/**
 * https://corp.olsen.ch/twiki/bin/view/Routes/OlsenRoutesAtomAggregator
 * 
 * The Aggregator atom is helpful in taking individual data elements and 
 * aggregate them in a single AggregatedDataElement.
 *
 * The number of elements it aggregates is variable, and so are the names.
 *
 * These two variables are controlled by the AggregatorConfiguration 
 * inner class, which contains a list of =StringProperty=s. 
 * Thus the size of list and its contents specify the element names of the output.
 *
 * The values have to be taken by the inputs, one per each element in 
 * the output. We will give to the inputs the same name of the 
 * corresponding element in the output.
 *
 * The list of inputs is created and mantained by the computeAtomProperties() 
 * method. This method is triggered every time the user changes the 
 * properties of the atom, and at atom creation. At every call, the existing 
 * inputs and the parameters are compared, and differences are 
 * then reflected into the inputs.
 *
 * Once the inputs are synchronized with the parameters, we start building 
 * the output. To define the output we need both to define its 
 * data type and it inputs are linked, we can start preparing the data at 
 * the output so we don't have to waste time in polling afterwards.
 *
 * Since the output data type are specified by the links at inputs, 
 * we should note that output changes every time one of the inputs are 
 * rewired. This is why we override the setLink() method of the inputs.
 *
 * The buildOutput() methods gets all the input types of the inputs and 
 * creates a corresponding output data type.
 *
 * The update() method instead prepares the final data. 
 * Update is both called when some property of the atom has changed or 
 * when new data is available at the inputs. 
 *
 *
 * Aggregates data binding a name to a data type. A variable number of inputs is provided
 * and the output is dynamically inferred by looking at number and names of inputs
 *  
 * @author alex
 *
 */
@LibraryAutoDeploy(name = "Aggregator", desc = "Aggregator takes n inputs " + "(n specified by atom properties), gives them a name " + "(according to properties) and builds an " + "Aggregated Data Element accordingly at output", path = "Main")
public class Aggregator extends AtomAbstr implements Atom {

    private static final long serialVersionUID = 1L;

    AggregatorConfiguration cfg = new AggregatorConfiguration();

    public AggregatedOutput output;

    DataType type;

    AggregatedDataElement aggData;

    public Aggregator(RoutesFramework framework) throws AtomException {
        super(framework, "Aggregator takes n inputs " + "(n specified by atom properties), gives them a name " + "(according to properties) and builds an " + "Aggregated Data Element accordingly at output");
        output = new AggregatedOutput(this, "output", "Output");
        super.addOutput(output);
        Map<String, DataElement> emptyMap = new HashMap<String, DataElement>();
        aggData = AggregatedDataElement.Factory.newAggregatedDE(emptyMap);
        computeAtomProperties();
    }

    /**
	 * define inputs and outputs according to current configuration values
	 */
    private void computeAtomProperties() throws AtomException {
        boolean hasChanged = false;
        Iterator<AtomInput> iter = super.inputs.values().iterator();
        while (iter.hasNext()) {
            AtomInput ai = iter.next();
            boolean found = false;
            for (StringProperty sp : cfg.inputs.getProperties()) {
                if (ai.getName().equals(sp.value())) found = true;
            }
            if (!found) {
                if (ai.getLink() != null) {
                    ai.getLink().remove();
                }
                iter.remove();
                hasChanged = true;
            }
        }
        for (StringProperty sp : cfg.inputs.getProperties()) {
            boolean found = false;
            for (AtomInput ai : inputs.values()) {
                if (ai.getName().equals(sp.value())) found = true;
            }
            if (!found) {
                AtomInput input = new AtomInputAbstr(this, sp.value(), sp.value()) {

                    private static final long serialVersionUID = 1L;

                    public DataType getType() {
                        return NullDataElement.Factory.getType();
                    }

                    public List<RoutesStep> receive_internal(DataElement data) throws AtomException {
                        return update(true);
                    }

                    @Override
                    public void setLink(LinkOutput link) throws AtomException {
                        if ((super.incomingLink == null ^ link == null) || !super.incomingLink.getType().isCompatible(link.getType())) {
                            super.setLink(link);
                            buildOutput();
                        } else {
                            super.setLink(link);
                        }
                    }
                };
                super.addInput(input);
                hasChanged = true;
            }
        }
        if (hasChanged) {
            buildOutput();
        }
    }

    /**
	 * rebuilds the output data type
	 */
    private void buildOutput() throws AtomException {
        Map<String, DataType> map = new HashMap<String, DataType>();
        for (AtomInput ai : inputs.values()) {
            DataType type = NullDataElement.Factory.getType();
            if (ai.getLink() != null) {
                type = ai.getLink().getType();
            }
            map.put(ai.getName(), type);
        }
        this.type = AggregatedDataElement.Factory.getTypeFromSubTypes(map);
        update(false);
    }

    protected class AggregatedOutput extends AtomOutputAbstr {

        private static final long serialVersionUID = 1L;

        public AggregatedOutput(Atom atom, String name, String desc) {
            super(atom, name, desc);
        }

        public DataElement get_internal() {
            return aggData;
        }

        public DataType getType() {
            return type;
        }
    }

    /**
	 * rebuilds aggData
	 */
    protected List<RoutesStep> update(boolean dispatch) throws AtomException {
        Map<String, DataElement> map = new HashMap<String, DataElement>();
        for (AtomInput ai : inputs.values()) {
            DataElement e = NullDataElement.Factory.newNullDE();
            if (ai.getLink() != null) {
                e = ai.getLink().get();
            }
            if (e == null) {
                if (dispatch) {
                    throw new AtomException("Unable to poll for data on input " + ai.getName());
                }
                return null;
            }
            map.put(ai.getName(), e);
        }
        this.aggData = AggregatedDataElement.Factory.newAggregatedDE(map);
        if (dispatch) return this.output.generateFireEvents(this.aggData);
        return null;
    }

    /**
	 * Returns the configuration to be used outside of the class
	 */
    public AggregatorConfiguration getParameters() {
        return cfg;
    }

    @Override
    public void prepareForRunning() throws AtomException {
        computeAtomProperties();
    }

    public static class AggregatorConfiguration extends Configuration<AggregatorConfiguration> {

        private static final long serialVersionUID = 1L;

        public ListOfUnnamedProperties<StringProperty> inputs;

        public AggregatorConfiguration() {
            this.inputs = new ListOfUnnamedProperties<StringProperty>("inputs", "Set of input names", new PropertyFactory<StringProperty>() {

                private static final long serialVersionUID = 1L;

                private int n = 1;

                public StringProperty newInstance() throws InternalConfigurationException {
                    return new StringProperty("name", "Name of the element in the Agregated Data", "elem" + n++, false);
                }
            }, false);
            this.inputs.add(new StringProperty("name", "Name of the element in the Agregated Data", "elem0", false));
        }

        public void clear() {
        }

        public String getDescription() {
            return "Define number and name of aggregated elements";
        }

        public String getName() {
            return "aggregator";
        }
    }

    public final String describe() {
        return "Aggregator";
    }
}
