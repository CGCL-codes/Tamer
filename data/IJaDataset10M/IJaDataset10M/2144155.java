package org.googlecode.horseshoe.response;

import org.googlecode.horseshoe.HorseshoeException;
import org.googlecode.horseshoe.IHorseshoeProvider;
import org.googlecode.horseshoe.formatters.IHorseshoeRequestReader;
import org.googlecode.horseshoe.query.IQueryBuilder;
import java.util.List;

public class EntityWriter implements IHorseshoeProcessor {

    private IHorseshoeProvider _provider;

    private IHorseshoeRequestReader _reader;

    private IQueryBuilder _queryBuilder;

    /**
   * @param provider
   * @param reader
   * @param queryBuilder
   */
    public EntityWriter(IHorseshoeProvider provider, IHorseshoeRequestReader reader, IQueryBuilder queryBuilder) {
        _provider = provider;
        _reader = reader;
        _queryBuilder = queryBuilder;
    }

    /**
   * @see org.googlecode.horseshoe.response.IHorseshoeProcessor#processRequest(org.googlecode.horseshoe.response.IResponseWriter)
   */
    @SuppressWarnings("unchecked")
    public void processRequest(IResponseWriter writer) {
        List results = _queryBuilder.buildAndExecuteCriteria();
        if (results.size() == 0) {
            throw new HorseshoeException("No entity found for query arguments");
        }
        if (results.size() > 1) {
            throw new HorseshoeException("Query did not return unique result");
        }
        Object entity = results.get(0);
        _reader.updateEntity(entity);
        _provider.update(entity);
        writer.writeEntity(entity);
    }
}
