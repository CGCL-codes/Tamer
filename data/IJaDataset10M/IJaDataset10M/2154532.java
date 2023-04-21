package fr.lig.sigma.astral.core.operators.window;

import fr.lig.sigma.astral.common.AxiomNotVerifiedException;
import fr.lig.sigma.astral.common.Batch;
import fr.lig.sigma.astral.common.InstanceCreationException;
import fr.lig.sigma.astral.common.Tuple;
import fr.lig.sigma.astral.common.event.EventNotifier;
import fr.lig.sigma.astral.common.event.EventProcessor;
import fr.lig.sigma.astral.common.event.SchedulerContainer;
import fr.lig.sigma.astral.common.structure.Relation;
import fr.lig.sigma.astral.common.structure.Stream;
import fr.lig.sigma.astral.core.operators.set.UnionRelation;
import fr.lig.sigma.astral.operators.Operator;
import fr.lig.sigma.astral.operators.OperatorFactory;
import fr.lig.sigma.astral.operators.RelationOperator;
import fr.lig.sigma.astral.operators.window.Window;
import fr.lig.sigma.astral.operators.window.WindowDescription;
import fr.lig.sigma.astral.query.AstralCore;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
@Component
@Provides
public class PartitionedWindowImpl extends RelationOperator implements Operator {

    @Requires(id = "in")
    private Stream in;

    @Requires(id = "core")
    private AstralCore core;

    @Property(mandatory = true)
    private List<String> partitionedBy;

    @Property(mandatory = true)
    private List<Map<String, String>> description;

    private OperatorFactory of;

    private List<String> attributes;

    private Map<List<Comparable>, Stream> substreams = new HashMap<List<Comparable>, Stream>();

    private ArrayList<Relation> windows = new ArrayList<Relation>();

    private EventProcessor processor;

    private static Logger log = Logger.getLogger(PartitionedWindowImpl.class);

    @Override
    public void processEvent(Batch timestamp) throws AxiomNotVerifiedException {
        while (in.hasNext(timestamp)) {
            Tuple t = in.pop();
            List<Comparable> values = new LinkedList<Comparable>();
            for (String attribute : attributes) {
                values.add(t.get(attribute));
            }
            Stream dest = substreams.get(values);
            if (dest == null) {
                long time = System.currentTimeMillis();
                dest = (Stream) entityFactory.instanciateEntity("StreamQueueImpl", getName() + ".v" + values.toString(), getAttributes());
                ((SchedulerContainer) dest).setScheduler(getScheduler());
                try {
                    Properties filter = new Properties();
                    filter.put("in", "(service.id=" + core.getEngine().getServiceId(dest) + ")");
                    filter.put("engine", "(service.id=" + core.getEngine().getServiceId(core.getEngine()) + ")");
                    filter.put("core", "(service.id=" + core.getEngine().getServiceId(core) + ")");
                    Properties prop = new Properties();
                    prop.put("requires.filters", filter);
                    prop.put("description", description);
                    RelationOperator win = (RelationOperator) of.instanciateSpecificOperator("Window", prop);
                    windows.add(win);
                    ((EventNotifier) win).registerNotifier(processor);
                } catch (InstanceCreationException e) {
                    throw new IllegalStateException("Can not create a Window", e);
                }
                substreams.put(values, dest);
                time = System.currentTimeMillis() - time;
                if (log.isTraceEnabled()) {
                    log.trace("Created new substream for values: " + values);
                    log.trace(time + "ms to build operators");
                }
            }
            dest.put(t, in.B(t).getId());
            if (log.isTraceEnabled()) log.trace(timestamp + ": New tuple inside the substream (values " + values + ")");
        }
    }

    @Override
    public int getMaxInputs() {
        return 1;
    }

    @Override
    public void prepare() throws Exception {
        String attributes[] = partitionedBy.toArray(new String[partitionedBy.size()]);
        of = core.getOf();
        List<String> attrList = Arrays.asList(attributes);
        if (!in.getAttributes().containsAll(attrList)) throw new IllegalStateException("Partitioning attribute(s) is(are) not in the attribute set");
        this.attributes = attrList;
        setOutput((Relation) entityFactory.instanciateEntity("RelationVolatileImpl", in.getName() + "[" + partitionedBy + "/" + description.get(0) + "]", in.getAttributes()));
        addInput(in, true);
        processor = new EventProcessor() {

            Batch lastTimestamp = Batch.MIN_VALUE;

            @Override
            public void processEvent(Batch timestamp) throws AxiomNotVerifiedException {
                log.trace("Partition union called: " + timestamp);
                if (lastTimestamp.equals(timestamp)) return;
                lastTimestamp = timestamp;
                UnionRelation.union(timestamp, windows, output, entityFactory, true);
            }

            @Override
            public EventProcessor[] waitFor() {
                return windows.toArray(new EventProcessor[windows.size()]);
            }
        };
    }

    @Override
    public String toString() {
        return "Partitioned Window";
    }
}
