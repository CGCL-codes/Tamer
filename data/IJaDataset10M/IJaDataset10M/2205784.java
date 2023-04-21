package org.peaseplate.queryengine.internal;

import java.io.Writer;
import java.util.Locale;
import org.peaseplate.queryengine.InvocationStrategyService;
import org.peaseplate.queryengine.Query;
import org.peaseplate.queryengine.QueryExecutor;
import org.peaseplate.queryengine.TransformerService;
import org.peaseplate.typeconversion.TypeConversionService;
import org.peaseplate.utils.command.Command;
import org.peaseplate.utils.command.Scope;
import org.peaseplate.utils.exception.ExecuteException;

public class PPQuery implements Query {

    private final Command command;

    private final TypeConversionService typeConversionService;

    private final InvocationStrategyService invocationService;

    private final TransformerService transformerService;

    public PPQuery(final Command command, final TypeConversionService typeConversionService, final InvocationStrategyService invocationService, final TransformerService transformerService) {
        super();
        this.command = command;
        this.typeConversionService = typeConversionService;
        this.invocationService = invocationService;
        this.transformerService = transformerService;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public QueryExecutor withOutputWriter(final Writer writer) {
        return new PPQueryExecutor(this, typeConversionService, invocationService, transformerService).withOutputWriter(writer);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public QueryExecutor withErrorWriter(final Writer writer) {
        return new PPQueryExecutor(this, typeConversionService, invocationService, transformerService).withErrorWriter(writer);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public QueryExecutor withLocale(final Locale locale) {
        return new PPQueryExecutor(this, typeConversionService, invocationService, transformerService).withLocale(locale);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public QueryExecutor withLineSeparator(final String lineSeparator) {
        return new PPQueryExecutor(this, typeConversionService, invocationService, transformerService).withLineSeparator(lineSeparator);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public QueryExecutor withVariable(final String name, final Object value) {
        return new PPQueryExecutor(this, typeConversionService, invocationService, transformerService).withVariable(name, value);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Object execute() throws ExecuteException {
        return new PPQueryExecutor(this, typeConversionService, invocationService, transformerService).execute();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Object execute(final Object workingObject) throws ExecuteException {
        return new PPQueryExecutor(this, typeConversionService, invocationService, transformerService).execute(workingObject);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Object execute(final Scope scope) throws ExecuteException {
        return command.call(scope);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String toString() {
        return command.toString();
    }
}
