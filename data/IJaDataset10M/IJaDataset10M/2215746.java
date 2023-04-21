package ch.olsen.routes.atom.lib;

import java.util.List;
import ch.olsen.products.util.logging.Logger;
import ch.olsen.routes.atom.AtomAbstr;
import ch.olsen.routes.atom.AtomException;
import ch.olsen.routes.atom.AtomInput;
import ch.olsen.routes.atom.AtomInputAbstr;
import ch.olsen.routes.atom.AtomOutput;
import ch.olsen.routes.atom.AtomOutputAbstr;
import ch.olsen.routes.atom.Link;
import ch.olsen.routes.atom.RoutesStep;
import ch.olsen.routes.cell.library.LibraryAutoDeploy;
import ch.olsen.routes.data.BooleanDataElement;
import ch.olsen.routes.data.DataElement;
import ch.olsen.routes.data.DataType;
import ch.olsen.routes.data.NullDataElement;
import ch.olsen.routes.framework.RoutesFramework;

/**
 * This atom generates continously null events on its outputs as long as the condition input is met
 * @author vito
 *
 */
@LibraryAutoDeploy(name = "While", desc = "Loops the output until condition is true. The looping " + "is achived through sending events at the output.", path = "Main")
public class WhileLoop extends AtomAbstr {

    private static final long serialVersionUID = 1L;

    public AtomInput start;

    public AtomInput condition;

    public AtomOutput output;

    AtomOutput internalOut;

    public WhileLoop(RoutesFramework framework) throws AtomException {
        super(framework, "Loops the output until condition is true. The looping " + "is achived through sending events at the output.");
        start = new AtomInputAbstr(this, "start", "Ensure tge while loop is started") {

            private static final long serialVersionUID = 1L;

            public List<RoutesStep> receive_internal(DataElement data) throws AtomException {
                return run();
            }

            public DataType getType() {
                return NullDataElement.Factory.getType();
            }
        };
        super.addInput(start);
        condition = new AtomInputAbstr(this, "condition", "While Condition") {

            private static final long serialVersionUID = 1L;

            public List<RoutesStep> receive_internal(DataElement data) {
                return null;
            }

            public DataType getType() {
                return BooleanDataElement.Factory.getType();
            }
        };
        super.addInput(condition);
        output = new AtomOutputAbstr(this, "output", "Output") {

            private static final long serialVersionUID = 1L;

            public DataElement get_internal() {
                return NullDataElement.Factory.newNullDE();
            }

            public DataType getType() {
                return NullDataElement.Factory.getType();
            }
        };
        super.addOutput(output);
        internalOut = new AtomOutputAbstr(this, "iOut", "Internal Out") {

            private static final long serialVersionUID = 1L;

            public DataElement get_internal() {
                return null;
            }

            public DataType getType() {
                return NullDataElement.nullDataType;
            }
        };
        super.addHiddenOutput(internalOut);
        Link.Factory.newLink(this, "loop", internalOut, start);
    }

    private List<RoutesStep> run() throws AtomException {
        DataElement de;
        synchronized (this) {
            de = condition.get();
            if (de == null) {
                getLogger().debug("No condition set, exiting...");
                return null;
            }
            boolean isLooping = de.toBooleanDE().booleanValue();
            if (!isLooping) {
                getLogger().debug("Condition is false, exiting...");
                return null;
            }
            Logger log = getLogger();
            if (log.isDebug()) log.debug("Looping...");
            return aggregateSteps(output.generateFireEvents(NullDataElement.Factory.newNullDE(), 0), internalOut.generateFireEvents(NullDataElement.Factory.newNullDE(), 1));
        }
    }

    public String describe() {
        return "While";
    }
}
