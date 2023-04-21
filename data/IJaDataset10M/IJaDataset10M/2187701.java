package net.sf.orcc.backends.cpp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.orcc.OrccException;
import net.sf.orcc.backends.AbstractBackend;
import net.sf.orcc.backends.ActorPrinter;
import net.sf.orcc.backends.NetworkPrinter;
import net.sf.orcc.backends.cpp.transformations.SerDesAdder;
import net.sf.orcc.ir.Actor;
import net.sf.orcc.ir.ExprString;
import net.sf.orcc.ir.Expression;
import net.sf.orcc.ir.util.ActorVisitor;
import net.sf.orcc.network.Connection;
import net.sf.orcc.network.Instance;
import net.sf.orcc.network.Network;
import net.sf.orcc.network.attributes.IAttribute;
import net.sf.orcc.network.attributes.IValueAttribute;
import org.eclipse.core.resources.IFile;

/**
 * C++ back-end.
 * 
 * @author Matthieu Wipliez
 * @author Ghislain Roquier
 * 
 */
public class CppBackendImpl extends AbstractBackend {

    public static final String DEFAULT_PARTITION = "default_partition";

    public static Boolean printHeader = false;

    private boolean classify;

    private boolean merge;

    private boolean needSerDes = false;

    private boolean normalize;

    private Map<Connection, Integer> computeFifoKind(Network network) throws OrccException {
        Map<Connection, Integer> fifoKind = new HashMap<Connection, Integer>();
        for (Connection connection : network.getConnections()) {
            int kind = 0;
            Instance src = network.getGraph().getEdgeSource(connection).getInstance();
            Instance tgt = network.getGraph().getEdgeTarget(connection).getInstance();
            String srcName = getPartNameAttribute(src);
            String tgtName = getPartNameAttribute(tgt);
            if (src.isSerdes() || tgt.isSerdes()) {
                needSerDes = true;
                kind = 1;
            } else if (!srcName.equals(tgtName)) {
                kind = 2;
            }
            fifoKind.put(connection, kind);
        }
        return fifoKind;
    }

    private Map<String, List<Instance>> computeMapping(Network network) throws OrccException {
        Map<String, List<Instance>> threads = new HashMap<String, List<Instance>>();
        for (Instance instance : network.getInstances()) {
            String component = getPartNameAttribute(instance);
            if (component != null) {
                List<Instance> list = threads.get(component);
                if (list == null) {
                    list = new ArrayList<Instance>();
                    threads.put(component, list);
                }
                list.add(instance);
            } else {
                throw new OrccException("instance " + instance.getId() + " has no partName attribute!");
            }
        }
        return threads;
    }

    @Override
    public void doInitializeOptions() {
        classify = getAttribute("net.sf.orcc.backends.classify", false);
        normalize = getAttribute("net.sf.orcc.backends.normalize", false);
        merge = getAttribute("net.sf.orcc.backends.merge", false);
    }

    @Override
    protected void doTransformActor(Actor actor) throws OrccException {
        ActorVisitor<?>[] transformations = {};
        for (ActorVisitor<?> transformation : transformations) {
            transformation.doSwitch(actor);
        }
    }

    private void doTransformNetwork(Network network) throws OrccException {
        if (classify) {
            network.classify();
            if (normalize) {
                network.normalizeActors();
            }
            if (merge) {
                network.mergeActors();
            }
        }
        new SerDesAdder().transform(network);
    }

    @Override
    protected void doVtlCodeGeneration(List<IFile> files) throws OrccException {
    }

    @Override
    protected void doXdfCodeGeneration(Network network) throws OrccException {
        network.flatten();
        transformActors(network.getActors());
        doTransformNetwork(network);
        printActors(network.getActors());
        write("Printing network...\n");
        network.computeTemplateMaps();
        printNetwork(network);
    }

    private String getPartNameAttribute(Instance instance) throws OrccException {
        String partName = DEFAULT_PARTITION;
        IAttribute attr = instance.getAttribute("partName");
        if (attr != null) {
            Expression expr = ((IValueAttribute) attr).getValue();
            partName = ((ExprString) expr).getValue();
        }
        return partName;
    }

    @Override
    protected boolean printActor(Actor actor) {
        ActorPrinter actorPrinter = new ActorPrinter("Cpp_actorImpl");
        ActorPrinter headerPrinter = new ActorPrinter("Cpp_actorDecl");
        actorPrinter.setTypePrinter(new CppTypePrinter());
        headerPrinter.setTypePrinter(new CppTypePrinter());
        actorPrinter.setExpressionPrinter(new CppExprPrinter());
        headerPrinter.setExpressionPrinter(new CppExprPrinter());
        String hier = path + File.separator + actor.getPackage().replace('.', File.separatorChar);
        new File(hier).mkdirs();
        actorPrinter.print(actor.getSimpleName() + ".cpp", hier, actor, "actor");
        headerPrinter.print(actor.getSimpleName() + ".h", hier, actor, "actor");
        return false;
    }

    private void printCMake(Network network) {
        NetworkPrinter networkPrinter = new NetworkPrinter("Cpp_CMakeLists");
        networkPrinter.getOptions().put("needSerDes", needSerDes);
        networkPrinter.print("CMakeLists.txt", path, network, "Cpp_CMakeLists");
    }

    /**
	 * Prints the given network.
	 * 
	 * @param network
	 *            a network
	 * @throws OrccException
	 *             if something goes wrong
	 */
    private void printNetwork(Network network) throws OrccException {
        NetworkPrinter printer = new NetworkPrinter("Cpp_network");
        printer.setExpressionPrinter(new CppExprPrinter());
        printer.setTypePrinter(new CppTypePrinter());
        printer.getOptions().put("threads", computeMapping(network));
        printer.getOptions().put("fifoKind", computeFifoKind(network));
        printer.getOptions().put("needSerDes", needSerDes);
        printer.print(network.getName() + ".cpp", path, network, "network");
        printCMake(network);
    }
}
