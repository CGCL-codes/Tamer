package sketch.specs;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import sketch.ast.ASTUtils;
import sketch.generator.ExhaustiveValueGenerator;
import sketch.specs.annotation.Exhaust;
import sketch.util.Checker;

/**
 * This class is not used yet.
 * */
class SketchedExhaustBlock extends SketchedBlock {

    public SketchedExhaustBlock(Block realBlock) {
        super(realBlock);
    }

    public static final String COMMENT_REPRESENTATION = Exhaust.value();

    @Override
    protected List<Block> replicate() {
        List<Block> retBlocks = new LinkedList<Block>();
        Block innerBlock = this.realBlock;
        SpecValueDeclarationFinder finder = new SpecValueDeclarationFinder(innerBlock);
        List<VariableDeclarationStatement> varStmts = finder.getAllValueDeclaredStatements();
        varStmts = this.selectChooseOneStatement(varStmts);
        Map<SimpleName, VariableDeclarationFragment> varMap = ASTUtils.extractDefinedVariableMapping(varStmts);
        Set<SimpleName> vars = varMap.keySet();
        if (vars.isEmpty()) {
            retBlocks.add(ASTUtils.deepClone(innerBlock));
            return retBlocks;
        }
        UsedVariableVisitor usedVarVisitor = new UsedVariableVisitor(vars);
        usedVarVisitor.visit(innerBlock);
        Collection<SimpleName> usedVars = usedVarVisitor.getUsedVariables();
        if (usedVars.isEmpty()) {
            retBlocks.add(ASTUtils.deepClone(innerBlock));
            return retBlocks;
        }
        Checker.checkTrue(usedVars.size() == 1, "usedVars() > 1 did not implement yet.");
        SimpleName usedName = ((SimpleName[]) usedVars.toArray(new SimpleName[0]))[0];
        DeclaredValueExtractor extractor = new DeclaredValueExtractor(varMap.get(usedName));
        List<Expression> expressions = extractor.getArgumentExpression();
        ExhaustiveValueGenerator<Expression> generator = new ExhaustiveValueGenerator<Expression>(expressions);
        List<List<Expression>> allExpressionList = generator.allValues();
        Map<SimpleName, List<Expression>> map = new LinkedHashMap<SimpleName, List<Expression>>();
        for (List<Expression> expressionList : allExpressionList) {
            map.clear();
            map.put(usedName, expressionList);
            SimpleNameExhaustiveReplacementVisitor visitor = new SimpleNameExhaustiveReplacementVisitor(map);
            Block replicate_block = ASTUtils.deepClone(innerBlock);
            replicate_block.accept(visitor);
            retBlocks.add(replicate_block);
        }
        return retBlocks;
    }

    @Override
    protected SketchedBlock newInstance(Block block) {
        return new SketchedExhaustBlock(block);
    }

    private List<VariableDeclarationStatement> selectChooseOneStatement(List<VariableDeclarationStatement> statements) {
        List<VariableDeclarationStatement> chooseOneStatements = new LinkedList<VariableDeclarationStatement>();
        for (VariableDeclarationStatement statement : statements) {
            if (SpecValueDeclarationFinder.isChooseValeuStatement(statement)) {
                chooseOneStatements.add(statement);
            }
        }
        return chooseOneStatements;
    }
}
