package serl.psf.contrib;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import serl.psf.Plugin;
import serl.psf.analysis.AbstractAnalyzer;
import serl.psf.controlflow.CopyPropagationAnalysis;
import serl.psf.controlflow.ExpressionTree;
import serl.psf.controlflow.InterProceduralPathGenerator;
import serl.psf.controlflow.LoopBlock;
import serl.psf.controlflow.Path;
import serl.psf.controlflow.PathGroup;
import serl.psf.controlflow.PathNode;
import serl.psf.controlflow.ValueInContext;
import serl.psf.controlflow.ExpressionTree.TreeNode;
import serl.psf.util.Util;
import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.IfStmt;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LtExpr;
import soot.jimple.Stmt;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class GenericMinMaxRecognizer extends AbstractAnalyzer {

    PrintStream out;

    /**
	 * 
	 */
    public GenericMinMaxRecognizer() {
        out = new PrintStream(Plugin.getConsole().newMessageStream());
    }

    @Override
    public String getName() {
        return "MinMax Algorithm Recognizer";
    }

    @Override
    public boolean preProcessClass(SootClass sootClass) {
        if (sootClass.getName().equals("serl.dummy.Main")) return false;
        return true;
    }

    @Override
    public void postProcessClass(SootClass sootClass) {
    }

    TreeSet<Pattern> patterns;

    @Override
    public boolean preProcessMethod(SootMethod method, InterProceduralPathGenerator generator) {
        if (method.getName().equals("<init>")) return false;
        patterns = new TreeSet<Pattern>();
        return true;
    }

    @Override
    public void postProcessMethod(SootMethod method) {
        for (Pattern p : patterns) {
            out.println("============== Min/Max Pattern FOUND ==============");
            out.println(p.getMethod());
            out.println("Starting Line Number: " + p.getBegin());
            out.println("Ending Line Number: " + p.getEnd());
        }
        this.patterns = null;
    }

    int count = 0;

    @Override
    public boolean preProcessPathGroup(PathGroup group, SootMethod method) {
        return true;
    }

    @Override
    public void postProcessPathGroup(PathGroup group, SootMethod method) {
        count = 0;
    }

    CopyPropagationAnalysis analysis = null;

    @Override
    public boolean preProcessPath(Path path, PathGroup group) {
        analysis = new CopyPropagationAnalysis(path);
        return true;
    }

    @Override
    public void postProcessPath(Path path, PathGroup group) {
        analysis = null;
    }

    ExpressionTree exprTree = null;

    @Override
    public boolean preProcessPathNode(PathNode node, Path path, PathGroup group) {
        Stmt stmt = node.getStmt();
        if (stmt instanceof IfStmt || stmt instanceof AssignStmt) {
            if (loop == null) {
                if (path.getLoops(node).isEmpty()) return false;
                exprTree = new ExpressionTree(node, analysis);
                return true;
            } else {
                if (path.getLoops(node).contains(this.loop)) {
                    exprTree = new ExpressionTree(node, analysis);
                    return true;
                } else {
                    this.clearObjects();
                }
            }
        }
        return false;
    }

    @Override
    public void postProcessPathNode(PathNode node, Path path, PathGroup group) {
        exprTree = null;
    }

    LoopBlock loop;

    PathNode loopCond;

    TreeNode iInLoopCond;

    TreeNode arrayInLoopCond;

    PathNode elemCheck;

    Set<ValueInContext> elemLocals;

    PathNode maxAssignment;

    PathNode loopIncrement;

    private void clearObjects() {
        loop = null;
        loopCond = null;
        iInLoopCond = null;
        arrayInLoopCond = null;
        elemCheck = null;
        elemLocals = null;
        maxAssignment = null;
        loopIncrement = null;
    }

    @Override
    public void processPathNode(PathNode node, Path path, PathGroup group) {
        if (node.getStmt() instanceof IfStmt) {
            List<TreeNode> condList = exprTree.getSubtrees(exprTree.getRoot(), ConditionExpr.class);
            if (!condList.isEmpty()) {
                if (loopCond == null) {
                    testAndSetLoopCondition(node, path);
                } else if (elemCheck == null) {
                    testAndSetElementComparison(node, path);
                }
            }
        } else if (loopCond != null && elemCheck != null) {
            if (maxAssignment == null) {
                testAndSetMaxAssignment(node, path);
            } else if (loopIncrement == null) {
                testAndSetLoopIncrement(node, path);
            }
        }
    }

    private void testAndSetLoopIncrement(PathNode node, Path path) {
        if (node.getStmt() instanceof AssignStmt) {
            TreeNode assignNode = exprTree.getRoot();
            TreeNode lNode = assignNode.left();
            TreeNode rNode = assignNode.right();
            if (!exprTree.intersection(iInLoopCond, lNode, Local.class).isEmpty() && !exprTree.getSubtrees(rNode, BinopExpr.class).isEmpty()) {
                int begin = Util.getLineNumber(this.loopCond.getStmt());
                int end = Util.getLineNumber(node.getStmt());
                SootMethod method = path.getEntryMethod();
                Pattern pattern = new Pattern(begin, end, method);
                this.patterns.add(pattern);
            }
        }
    }

    private void testAndSetMaxAssignment(PathNode node, Path path) {
        if (node.getStmt() instanceof AssignStmt) {
            TreeNode assignNode = exprTree.getRoot();
            TreeNode lNode = assignNode.left();
            TreeNode rNode = assignNode.right();
            List<ValueInContext> lLocals = exprTree.getElements(lNode, Local.class);
            List<ValueInContext> rLocals = exprTree.getElements(rNode, Local.class);
            if ((!ExpressionTree.intersection(lLocals, elemLocals).isEmpty() || !ExpressionTree.intersection(rLocals, elemLocals).isEmpty()) && !exprTree.intersection(iInLoopCond, rNode, Local.class).isEmpty()) {
                this.maxAssignment = node;
            }
        }
    }

    private void testAndSetElementComparison(PathNode node, Path path) {
        List<TreeNode> condList = exprTree.getSubtrees(exprTree.getRoot(), ConditionExpr.class);
        if (!condList.isEmpty()) {
            TreeNode condNode = condList.get(0);
            Value condition = condNode.getValue();
            if (condition instanceof GeExpr || condition instanceof GtExpr || condition instanceof LeExpr || condition instanceof LtExpr) {
                TreeNode left = condNode.left();
                TreeNode right = condNode.right();
                List<TreeNode> lArrayList = exprTree.getSubtrees(left, ArrayRef.class);
                List<TreeNode> rArrayList = exprTree.getSubtrees(right, ArrayRef.class);
                if (!lArrayList.isEmpty() && !rArrayList.isEmpty()) {
                    TreeNode lArrayNode = lArrayList.get(0).left();
                    TreeNode rArrayNode = rArrayList.get(0).left();
                    if (!exprTree.intersection(lArrayNode, rArrayNode, Value.class).isEmpty() && !exprTree.intersection(lArrayNode, arrayInLoopCond, Value.class).isEmpty() && !exprTree.intersection(iInLoopCond, condNode, Local.class).isEmpty()) {
                        this.elemCheck = node;
                        TreeNode lIndxNode = lArrayList.get(0).right();
                        TreeNode rIndxNode = rArrayList.get(0).right();
                        elemLocals = exprTree.union(lIndxNode, rIndxNode, Local.class);
                    }
                }
            }
        }
    }

    private void testAndSetLoopCondition(PathNode node, Path path) {
        List<TreeNode> condList = exprTree.getSubtrees(exprTree.getRoot(), ConditionExpr.class);
        if (!condList.isEmpty()) {
            TreeNode condNode = condList.get(0);
            TreeNode left = condNode.left();
            TreeNode right = condNode.right();
            List<TreeNode> localList = exprTree.getSubtrees(left, Local.class);
            List<TreeNode> arrayList = exprTree.getSubtrees(right, LengthExpr.class);
            if (!localList.isEmpty() && !arrayList.isEmpty()) {
                loop = path.getLoopWithCondition(node);
                if (loop != null) {
                    loopCond = node;
                    iInLoopCond = localList.get(0);
                    arrayInLoopCond = arrayList.get(0).right();
                }
            }
        }
    }
}
