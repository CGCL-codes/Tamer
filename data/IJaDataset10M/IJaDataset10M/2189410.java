package net.sf.refactorit.audit;

import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.classmodel.CompilationUnit;
import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.commonIDE.IDEController;
import net.sf.refactorit.options.GlobalOptions;
import net.sf.refactorit.query.DelegateVisitor;
import net.sf.refactorit.query.DelegatingVisitor;
import net.sf.refactorit.ui.DialogManager;
import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 *
 * @author Villu Ruusmann
 */
public abstract class AuditRule extends DelegateVisitor {

    private final ArrayList violations = new ArrayList();

    private String auditName = "unset";

    private String categoryName = "unset";

    private String auditKey = "other";

    private Element configuration;

    private CompilationUnit compilationUnit;

    private boolean testRun = false;

    private DelegatingVisitor supervisor = null;

    private Priority priority = Priority.NORMAL;

    /**
   * Cumulative execution time in millis
   */
    private long time;

    private boolean firstRun = true;

    public AuditRule() {
        super();
    }

    public void setName(final String name) {
        auditName = name;
    }

    public void setTestRun(boolean testRun) {
        this.testRun = testRun;
    }

    public boolean isTestRun() {
        return this.testRun;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setCategory(final String category) {
        categoryName = category;
    }

    public void setSupervisor(final DelegatingVisitor supervisor) {
        this.supervisor = supervisor;
    }

    public Element getConfiguration() {
        return this.configuration;
    }

    public void setConfiguration(Element configuration) {
        this.configuration = configuration;
        this.priority = Priority.getPriorityByName(configuration.getAttribute("priority"));
        if (this.priority == null) {
            this.priority = Priority.NORMAL;
        }
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        if (this.priority == null) {
            this.priority = Priority.NORMAL;
        }
    }

    public Priority getPriority() {
        return this.priority;
    }

    public void incresePriority(Priority priority) {
        this.priority = this.priority.increase();
    }

    public void decreasePriority() {
        this.priority = this.priority.decrease();
    }

    /**
   * Some fields and parameters cannot be set in rule constructor, because
   * AuditRule gets needed information a little later (when executed on
   * some compilation unit - then we know the project, the audit is exectued on;
   * or when setConfiguration() method is invoked - we get profile info). This
   * init() method is called only once when rule visits its first
   * CompilationUnit.<br>
   * Override it to read information from profile or similar.<br>
   * Don`t forget to call super.init() to disable future calls of this method!
   */
    public void init() {
        firstRun = false;
    }

    long start;

    public void visit(CompilationUnit compilationUnit) {
        start = System.currentTimeMillis();
        this.compilationUnit = compilationUnit;
        if (firstRun) {
            init();
        }
    }

    public void leave(CompilationUnit compilationUnit) {
        this.compilationUnit = null;
        this.time += (System.currentTimeMillis() - start);
    }

    protected Project getProject() {
        if (compilationUnit != null) {
            return compilationUnit.getProject();
        }
        return getCurrentType().getProject();
    }

    public BinTypeRef getCurrentType() {
        return this.supervisor.getCurrentType();
    }

    public ArrayList getViolations() {
        return this.violations;
    }

    public void clearViolations() {
        violations.clear();
    }

    public void sortViolations() {
        Collections.sort(violations);
    }

    public void postProcess() {
    }

    public void finishedRun() {
    }

    public void addViolation(final RuleViolation violation) {
        violation.setAuditRule(this);
        this.violations.add(violation);
    }

    protected void removeViolation(RuleViolation violation) {
        this.violations.remove(violation);
    }

    protected BinTypeRef getBinTypeRef(String fqName) {
        return getProject().getTypeRefForName(fqName);
    }

    public String debugInfo() {
        return getClass().getName() + ": " + String.valueOf(time) + " ms.";
    }

    public static class Priority {

        public static final Priority DEFAULT = new Priority(0, "Default");

        public static final Priority LOW = new Priority(10, "Low");

        public static final Priority NORMAL = new Priority(20, "Normal");

        public static final Priority HIGH = new Priority(50, "High");

        private final int defaultScoreForDensityCalc;

        private final String description;

        private final String longDescription;

        private static final Priority[] priorityArray = new Priority[] { DEFAULT, LOW, NORMAL, HIGH };

        private int scoreForDensityCalculation;

        private Priority(int defaultScoreForDensityCalc, String description) {
            this.defaultScoreForDensityCalc = defaultScoreForDensityCalc;
            this.description = description;
            this.longDescription = description + " priority";
            loadScoreForDensityCalculation();
        }

        public String getDescription() {
            return this.description;
        }

        public Priority increase() {
            for (int i = 0; i < priorityArray.length - 1; i++) {
                if (priorityArray[i] == this) {
                    return priorityArray[i + 1];
                }
            }
            return this;
        }

        public Priority decrease() {
            for (int i = 1; i < priorityArray.length; i++) {
                if (priorityArray[i] == this) {
                    return priorityArray[i - 1];
                }
            }
            return this;
        }

        public static Priority getPriorityByName(String name) {
            Priority result = null;
            for (int i = 0; i < priorityArray.length; i++) {
                if (priorityArray[i].getName().equalsIgnoreCase(name)) {
                    result = priorityArray[i];
                    break;
                }
            }
            return result;
        }

        public static Priority[] getPriorityArray() {
            return priorityArray;
        }

        private void loadScoreForDensityCalculation() {
            String optionName = "audit.violation-priority-score." + getDescription().toLowerCase();
            String value = GlobalOptions.getOption(optionName);
            if (value == null) {
                GlobalOptions.setOption(optionName, "" + defaultScoreForDensityCalc);
                GlobalOptions.save();
                scoreForDensityCalculation = defaultScoreForDensityCalc;
            } else {
                try {
                    scoreForDensityCalculation = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    DialogManager.getInstance().showWarning(IDEController.getInstance().createProjectContext(), "Illegal numeric value in config file: " + value);
                    scoreForDensityCalculation = defaultScoreForDensityCalc;
                }
            }
        }

        public int getScoreForDensityCalculation() {
            return scoreForDensityCalculation;
        }

        public static Priority highest(Priority a, Priority b) {
            if (a.defaultScoreForDensityCalc > b.defaultScoreForDensityCalc) {
                return a;
            }
            return b;
        }

        public String getName() {
            return this.description.toUpperCase();
        }

        public String toString() {
            return longDescription;
        }
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public String getKey() {
        return auditKey;
    }

    public void setKey(String key) {
        this.auditKey = key;
    }
}
