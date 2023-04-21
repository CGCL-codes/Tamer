package net.sourceforge.javabits.cli;

import java.io.Reader;
import java.util.Collection;
import net.sourceforge.javabits.error.Problem;

/**
 * @author Jochen Kuhnle
 */
public interface ErrorParser {

    public <C extends Collection<? super Problem>> C parseErrors(Reader in, C problemCollection);
}
