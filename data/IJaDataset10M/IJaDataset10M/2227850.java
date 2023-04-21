package backend.param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import backend.AbstractArguments;
import backend.AbstractONDEXPlugin;
import backend.param.args.ArgumentDefinition;
import backend.param.exceptions.ReplicatedValueException;

/**
 * 
 * @author hindlem
 *
 */
public class Paramiters {

    private String ondexName = null;

    private ArrayList<AbstractONDEXPluginInit> plugins = new ArrayList<AbstractONDEXPluginInit>();

    private HashMap<String, GraphInit> graphs = new HashMap<String, GraphInit>();

    private GraphInit defaultGraph;

    public void setOndexName(String ondexName) {
        this.ondexName = ondexName;
    }

    public String getOndexName() {
        return ondexName;
    }

    public void addPlugin(AbstractONDEXPluginInit plugin) {
        plugins.add(plugin);
    }

    public void setDefaultGraph(GraphInit graph) {
        this.defaultGraph = graph;
    }

    public void addGraph(GraphInit graph) throws ReplicatedValueException {
        if (defaultGraph.getName().equals(graph.getName()) || graphs.put(graph.getName(), graph) != null) {
            throw new ReplicatedValueException("Graph " + graph.getName() + " apears more than once in ONDEX Paramiters");
        }
    }

    public ArrayList<AbstractONDEXPluginInit> getPluginInits() {
        return plugins;
    }

    public void setPluginInits(ArrayList<AbstractONDEXPluginInit> plugins) {
        this.plugins = plugins;
    }

    public Collection<GraphInit> getGraphs() {
        return graphs.values();
    }

    public GraphInit getDefaultGraph() {
        return defaultGraph;
    }

    /**
	 * 
	 * @author hindlem
	 *
	 */
    public static class AbstractONDEXPluginInit {

        private AbstractONDEXPlugin<?> plugin = null;

        private AbstractArguments arguments;

        private String name;

        private String graphInput;

        private String graphOutput;

        public AbstractArguments getArguments() {
            return arguments;
        }

        public AbstractONDEXPlugin<?> getPlugin() {
            return plugin;
        }

        public void setPlugin(AbstractONDEXPlugin<?> plugin) {
            this.plugin = plugin;
        }

        /**
		 *	Add's the arguments missing and sets them to the default.
		 *	Unless the argument is required && defaultValue != null;
		 */
        public void complementMissingArguments() {
            if (plugin == null) return;
            ArgumentDefinition<?>[] arguList = plugin.getArgumentDefinitions();
            if (arguList == null) {
                arguList = new ArgumentDefinition<?>[0];
                System.err.println("WARNING: Intermediate Plugin " + name + " is missing ArgumentDefinitions");
            }
            Map<String, List<Object>> options = arguments.getOptions();
            for (int a = 0; a < arguList.length; a++) {
                ArgumentDefinition<?> argDef = arguList[a];
                if (!argDef.isRequiredArgument() && argDef.getDefaultValue() != null) {
                    if (!options.containsKey(argDef.getName())) {
                        List<Object> defaultOption = new ArrayList<Object>(1);
                        defaultOption.add(argDef.getDefaultValue());
                        options.put(argDef.getName(), defaultOption);
                    }
                }
            }
        }

        public void setArguments(AbstractArguments args) {
            this.arguments = args;
        }

        public String getGraphInput() {
            return graphInput;
        }

        public void setGraphInput(String graphInput) {
            this.graphInput = graphInput;
        }

        public String getGraphOutput() {
            return graphOutput;
        }

        public void setGraphOutput(String graphOutput) {
            this.graphOutput = graphOutput;
        }
    }

    /**
	 * 
	 * @author hindlem
	 *
	 */
    public static class GraphInit {

        private String name;

        private String type = "berkeley";

        private HashMap<String, String> options = new HashMap<String, String>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void addOption(String name, String value) throws ReplicatedValueException {
            if (options.put(name, value) != null) {
                throw new ReplicatedValueException(name + " is declared more than once");
            }
        }

        public HashMap<String, String> getOptions() {
            return options;
        }
    }
}
