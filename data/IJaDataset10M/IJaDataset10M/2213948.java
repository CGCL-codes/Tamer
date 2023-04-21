package astcentric.structure.validation.regex;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import astcentric.structure.basic.Node;
import astcentric.structure.basic.NodeException;
import astcentric.structure.basic.NodeTool;
import astcentric.structure.fl.FunctionDelegation;
import astcentric.structure.regex.IntegerProvider;
import astcentric.structure.regex.RegexTerm;
import astcentric.structure.regex.Repetition;
import astcentric.structure.tool.Compiler;
import astcentric.structure.validation.basic.FunctionCompiler;

class RegexRepetitionFactory<T> extends AbstractRegexTermFactory<T> {

    RegexRepetitionFactory(Compiler<RegexTerm<T>, RegexCompilationContext> factory) {
        super(factory);
    }

    public RegexTerm<T> compile(Node specification, RegexCompilationContext context) {
        List<Node> children = NodeTool.getNodesOf(specification);
        int numberOfChildren = children.size();
        if (numberOfChildren < 2) {
            throw new NodeException(specification, "2 or 3 children expected instead of " + numberOfChildren);
        }
        RegexTerm<T> term = _factory.compile(children.get(0), context);
        IntegerProvider minProvider = compileIntgerProvider(children.get(1), context);
        if (numberOfChildren == 2) {
            return new Repetition<T>(term, minProvider);
        }
        IntegerProvider maxProvider = compileIntgerProvider(children.get(2), context);
        return new Repetition<T>(term, minProvider, maxProvider);
    }

    private IntegerProvider compileIntgerProvider(Node node, RegexCompilationContext context) {
        final FunctionDelegation function = new FunctionDelegation(null);
        FunctionCompiler.COMPILER.compile(function, Arrays.<Node>asList(), node, context.getFunctionCompilationContext());
        return new IntegerProvider() {

            public int getInteger() {
                Object result = function.calculate(Arrays.asList(), Collections.emptyMap());
                return result instanceof Number ? ((Number) result).intValue() : 0;
            }
        };
    }
}
