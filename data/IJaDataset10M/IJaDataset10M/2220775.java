package maze.common.oro.perl5.impl;

import maze.common.oro.impl.AbstractActualPatternBuilder;
import maze.common.oro.perl5.Perl5ActualPatternBuilder;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;

/**
 *
 * @author Normunds Mazurs
 */
public class Perl5ActualPatternBuilderImpl extends AbstractActualPatternBuilder implements Perl5ActualPatternBuilder {

    protected static final PatternCompiler PATTERN_COMPILER = new Perl5Compiler();

    protected static final int PATTERN_COMPILER_OPTIONS = Perl5Compiler.SINGLELINE_MASK | Perl5Compiler.EXTENDED_MASK;

    @Override
    protected PatternCompiler getPatternCompiler() {
        return PATTERN_COMPILER;
    }

    @Override
    protected int getPatternCompilerOptions() {
        return PATTERN_COMPILER_OPTIONS;
    }

    protected Perl5ActualPatternBuilderImpl() {
    }

    public static final Perl5ActualPatternBuilder INSTANCE = new Perl5ActualPatternBuilderImpl();
}
