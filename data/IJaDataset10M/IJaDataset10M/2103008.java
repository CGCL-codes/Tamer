package uk.ac.shef.wit.saxon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionCannotHandle;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchContent;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchStructure;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionRuneExecution;
import uk.ac.shef.wit.runes.runestone.Runestone;
import uk.ac.shef.wit.runes.runestone.Structure;
import uk.ac.shef.wit.runes.runestone.StructureAndContent;

/**
 * This is the main working class in Saxon. A rule specifies what should
 * be added to the Runestone model and where it should be added based on
 * matching a subset of the Runestone model.
 * @author Mark A. Greenwood
 * @version $Id: Rule.java 539 2008-11-03 14:19:12Z greenwoodma $
 */
public abstract class Rule {

    /**
	 * The transition that this rule uses to match a subset of the runestone model.
	 */
    private Transition transition;

    /**
	 * The name of the rule, used mosty for debug purposes.
	 */
    private String name;

    /**
	 * A map of default rule options. This is here so that each rule doesn't have to specify
	 * a value for each possible option.
	 */
    private static Map<String, Boolean> defaults = new HashMap<String, Boolean>();

    /**
	 * This map holds the user supplied options to influence the operation of this rule instance.
	 * Note the currently only boolean options are supported. This may change in the future if
	 * we find we need to support other option types.
	 */
    private Map<String, Boolean> options = new HashMap<String, Boolean>();

    /**
	 * This set holds all the things that are allRequired for this rule to be matched against
	 * the underlying data stored in the Runes framework.
	 */
    private Set<String> allRequired = null;

    private Set<String> required = new HashSet<String>();

    /**
	 * Add items to the list of data items required to be present for this rule
	 * to work correctly
	 * @param r the names of data items needed for the rule to work correctly
	 */
    public void addRequired(String... r) {
        required.addAll(Arrays.asList(r));
    }

    private Set<String> provided = new HashSet<String>();

    /**
	 * Add items to the list of data items that this rule instance provides
	 * @param p the names of data items this rule provides
	 */
    public void addProvided(String... p) {
        provided.addAll(Arrays.asList(p));
    }

    static {
        defaults.put("allMatches", false);
        defaults.put("backwards", false);
    }

    /**
	 * The transition that controls what apart from the starting node this rule
	 * matches against
	 * @return the transition this rule follows
	 */
    public Transition getTransition() {
        return transition;
    }

    /**
	 * Returns the name of the rule instance
	 * @return the name of this rule instance
	 */
    public String getName() {
        return name;
    }

    /**
	 * Returns the boolean value of an option used to control the operating
	 * of this rule instance
	 * @param name the name of the option
	 * @return the boolean value of the option
	 * @throws RunesExceptionRuneExecution 
	 */
    public boolean getOption(String name) throws RunesExceptionRuneExecution {
        Boolean val = options.get(name);
        if (val == null) val = defaults.get(name);
        if (val == null) throw new RunesExceptionRuneExecution("\nUnsupported Saxon Rule Option: " + name, null);
        return val;
    }

    protected Rule(Transition transition) {
        this("Rule", transition, Collections.<String, Boolean>emptyMap());
    }

    /**
	 * Create a new rule instance from the provided values
	 * @param name the name of the Rule being created
	 * @param transition the transition describing the rule
	 * @param options the rule specific options
	 */
    protected Rule(String name, Transition transition, Map<String, Boolean> options) {
        this.name = name;
        this.transition = transition;
        this.options.putAll(options);
    }

    /**
	 * Matches the rule against the underlying data but instead of annotating using the RHS simply returns
	 * the paths through the data which were found by this rule
	 * @param stone provides access to the underlying data
	 * @param n the code for the node from which this rule should be applied
	 * @return the set of Paths through the underlying data which this rule matches
	 * @throws RunesExceptionRuneExecution
	 * @throws RunesExceptionCannotHandle
	 * @throws RunesExceptionNoSuchStructure
	 * @throws RunesExceptionNoSuchContent
	 */
    public Set<Path> match(Runestone stone, String type, int n) throws RunesExceptionRuneExecution, RunesExceptionCannotHandle, RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent {
        Set<List<SteppingStone>> paths = new HashSet<List<SteppingStone>>();
        Set<Path> arrays = new HashSet<Path>();
        if (transition != null) {
            Map<String, Object> config = new HashMap<String, Object>();
            config.put("backwards", getOption("backwards"));
            @SuppressWarnings("unchecked") Set<List<SteppingStone>> found = transition.transitionFrom(stone, n, config);
            for (List<SteppingStone> path : found) {
                if (n != 0) path.add(0, new SteppingStone(n, type, config));
                if (!getOption("allMatches")) {
                    Iterator<List<SteppingStone>> it = paths.iterator();
                    while (it.hasNext()) {
                        List<SteppingStone> c = it.next();
                        if (Collections.indexOfSubList(c, path) == 0) {
                            path = null;
                            break;
                        }
                        if (Collections.indexOfSubList(path, c) == 0) {
                            it.remove();
                        }
                    }
                }
                if (path != null) paths.add(path);
            }
            if (paths.size() > 0) {
                for (List<SteppingStone> path : paths) {
                    List<SteppingStone> trimmed = new ArrayList<SteppingStone>();
                    Map<String, List<List<SteppingStone>>> chunks = new HashMap<String, List<List<SteppingStone>>>();
                    SteppingStone previous = null;
                    for (SteppingStone node : path) {
                        Map options = node.options;
                        boolean hidden = (options.containsKey("hidden") ? (Boolean) options.get("hidden") : false);
                        if (!hidden) {
                            Set<String> named = new HashSet<String>();
                            if (options.containsKey("named")) named = (Set<String>) options.get("named");
                            for (String name : named) {
                                List<List<SteppingStone>> soFar = chunks.get(name);
                                if (soFar == null) {
                                    List<SteppingStone> nodes = new ArrayList<SteppingStone>();
                                    nodes.add(node);
                                    List<List<SteppingStone>> chunk = new ArrayList<List<SteppingStone>>();
                                    chunk.add(nodes);
                                    chunks.put(name, chunk);
                                } else {
                                    List<SteppingStone> last = soFar.get(soFar.size() - 1);
                                    if (last.get(last.size() - 1).equals(previous)) {
                                        last.add(node);
                                    } else {
                                        List<SteppingStone> nodes = new ArrayList<SteppingStone>();
                                        nodes.add(node);
                                        soFar.add(nodes);
                                    }
                                }
                            }
                            trimmed.add(node);
                            previous = node;
                        }
                    }
                    Map<String, List<SteppingStone[]>> finalChunks = new HashMap<String, List<SteppingStone[]>>();
                    for (Map.Entry<String, List<List<SteppingStone>>> entry : chunks.entrySet()) {
                        List<SteppingStone[]> chunkList = new ArrayList<SteppingStone[]>();
                        for (List<SteppingStone> nodes : entry.getValue()) {
                            chunkList.add(nodes.toArray(new SteppingStone[nodes.size()]));
                        }
                        finalChunks.put(entry.getKey(), chunkList);
                    }
                    arrays.add(new Path(trimmed.toArray(new SteppingStone[trimmed.size()]), finalChunks));
                }
            }
        } else {
            throw new RuntimeException("A rule (" + getName() + ") without a transition has been fired, I thought this could never happen!\n" + this);
        }
        return arrays;
    }

    /**
	 * Apply this rule to the supplied model, starting at the supplied RepresentationNode.
	 * @param stone provides access to the underlying data 
	 * @param n the code for the node from which this rule should be applied
	 * @throws RunesExceptionRuneExecution 
	 * @throws RunesExceptionNoSuchStructure 
	 * @throws RunesExceptionCannotHandle 
	 * @throws RunesExceptionNoSuchContent
	 */
    public void apply(Runestone stone, String type, int n) throws RunesExceptionRuneExecution, RunesExceptionCannotHandle, RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent {
        Set<Path> found = match(stone, type, n);
        if (found.size() > 0) {
            annotate(stone, found);
        }
    }

    /**
	 * Matches the rule against the data but doesn't apply the RHS, rather it returns the length of the longest match
	 * @param stone provides access to the underlying data
	 * @param n the code for the node from which this rule should be applied
	 * @return the length of the longest match for this rule against the underlying data
	 * @throws RunesExceptionRuneExecution
	 * @throws RunesExceptionCannotHandle
	 * @throws RunesExceptionNoSuchStructure
	 * @throws RunesExceptionNoSuchContent
	 */
    public int matchCount(Runestone stone, String type, int n) throws RunesExceptionRuneExecution, RunesExceptionCannotHandle, RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent {
        Set<Path> paths = match(stone, type, n);
        int count = 0;
        for (Path p : paths) {
            count = Math.max(count, p.getSteppingStone().length);
        }
        return count;
    }

    /**
	 * Gives details of the information added to the data by this rule
	 * @param stone provides access to the underlying model
	 * @return a set of information about what may be added to the data by this rule
	 * @throws RunesExceptionCannotHandle
	 */
    public Set<String> analyseProvided(Runestone stone) {
        return provided;
    }

    /**
	 * Gives details of the information required to be in the data for this rule to run
	 * @param stone provides access to the underlying model
	 * @return a set of information about what must be provided for this tule to run
	 * @throws RunesExceptionCannotHandle
	 */
    public Set<String> analyseRequired(Runestone stone) {
        if (allRequired != null) return allRequired;
        allRequired = transition.analyseRequired();
        allRequired.addAll(required);
        return allRequired;
    }

    /**
	 * When the rule matches against the model this method is called to allow the rule to
	 * update the model acorddingly.
	 * @param stone provides access to the model against which the rule has matched
	 * @param paths the set of paths the rule has matched against within the model
	 * @throws RunesExceptionCannotHandle 
	 * @throws RunesExceptionNoSuchContent 
	 * @throws RunesExceptionNoSuchStructure 
	 */
    protected abstract void annotate(Runestone stone, Set<Path> paths) throws RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent, RunesExceptionCannotHandle;

    /**
	 * This method provides a simple way to add a node and supported edges representing a path
	 * to the runestone model.
	 * @param stone provides access to the runestone model
	 * @param path the path to add the node for
	 * @throws RunesExceptionNoSuchStructure 
	 * @throws RunesExceptionCannotHandle 
	 * @throws RunesExceptionNoSuchContent 
	 */
    protected final int addSaxonNode(Runestone stone, String type, Path path) throws RunesExceptionCannotHandle, RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent {
        final Structure saxonEntities = stone.getStructure("saxon_entity", "saxon_entity_has_rule_name", "saxon_entity_has_type", "saxon_entity_has_path");
        final StructureAndContent<String> ruleNames = stone.getStructureAndContent("rule_name");
        final StructureAndContent<String> types = stone.getStructureAndContent("type");
        final StructureAndContent<Path> paths = stone.getStructureAndContent("path");
        final int rule_name = ruleNames.encode(getName());
        final int entity_type = types.encode(type);
        final int entity_path = paths.encode(path);
        return saxonEntities.encode(rule_name, entity_type, entity_path);
    }

    /**
	 * Provides a human readable description of this Saxon Rule. Note that currently
	 * this provides no details on the right-hand-side of the rule.
	 * @return a human readable description of this Saxon rule.
	 */
    @Override
    public String toString() {
        return "Rule:" + name + "\n" + transition;
    }
}
