package microlabs.dst.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;
import microlabs.dst.shared.AddEdgeRules;
import microlabs.dst.shared.Evaluation;

@PersistenceCapable
public class ProblemJDO {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String name;

    @Persistent
    private String problemText;

    @Persistent
    private String nodes;

    @Persistent
    private Evaluation eval;

    @Persistent
    private AddEdgeRules rules;

    @Persistent
    private String[] arguments;

    public ProblemJDO(String name, String problemText, String nodes, String[] arguments, Evaluation eval, AddEdgeRules rules) {
        this.name = name;
        this.problemText = problemText;
        this.nodes = nodes;
        this.arguments = arguments;
        this.eval = eval;
        this.rules = rules;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    public String getProblemText() {
        return problemText;
    }

    public void setProblemText(String problemText) {
        this.problemText = problemText;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public Evaluation getEval() {
        return eval;
    }

    public void setEval(Evaluation eval) {
        this.eval = eval;
    }

    public AddEdgeRules getRules() {
        return rules;
    }

    public void setRules(AddEdgeRules rules) {
        this.rules = rules;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }
}
