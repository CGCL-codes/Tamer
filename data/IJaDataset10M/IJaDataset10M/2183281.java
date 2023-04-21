package net.sf.balm.persistence.hibernate3;

import java.util.Collections;
import java.util.List;
import net.sf.balm.common.lang.ArrayUtils;
import net.sf.balm.persistence.Page;
import net.sf.balm.persistence.QueryRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.util.Assert;

public class HibernateQueryRequestExecutor implements QueryRequestExecutor {

    private static final Log logger = LogFactory.getLog(HibernateQueryRequestExecutor.class);

    private HibernateTemplateExt hibernateTemplateExtension;

    public HibernateQueryRequestExecutor(HibernateTemplateExt hibernateTemplateExtension) {
        this.hibernateTemplateExtension = hibernateTemplateExtension;
    }

    public HibernateTemplateExt getHibernateTemplateExtension() {
        return this.hibernateTemplateExtension;
    }

    /**
     * HibernateQueryRequest不用被Spring容器接管，而是在查询的时候将真正的查询操作委托给HibernateTemplate
     * 
     * @param queryRequest
     * @return 查询结果列表
     */
    public List<?> getResultList(QueryRequest query) {
        AbstractQueryRequest queryRequest = (AbstractQueryRequest) query;
        boolean preCache = hibernateTemplateExtension.isCacheQueries();
        try {
            hibernateTemplateExtension.setCacheQueries(queryRequest.isCachedEnabled());
            String statement2Execute = queryRequest.getStatement2Execute();
            String[] names2Execute = queryRequest.getParameterNames2Execute();
            Object[] values2Execute = queryRequest.getParameterValues2Execute();
            if (logger.isDebugEnabled()) {
                logger.debug("query hql: " + statement2Execute);
                logger.debug("query parameter names: " + ArrayUtils.toString(names2Execute));
                logger.debug("query parameter values: " + ArrayUtils.toString(values2Execute));
            }
            return hibernateTemplateExtension.findByNamedParam(statement2Execute, names2Execute, values2Execute);
        } finally {
            hibernateTemplateExtension.setCacheQueries(preCache);
        }
    }

    /**
     * @param queryRequest
     * @return 查询结果为单一结果，如果结果不为单一结果，则抛出异常
     * @throws IncorrectResultSizeDataAccessException
     */
    public Object getSingleResult(QueryRequest query) {
        AbstractQueryRequest queryRequest = (AbstractQueryRequest) query;
        boolean preCache = hibernateTemplateExtension.isCacheQueries();
        try {
            hibernateTemplateExtension.setCacheQueries(queryRequest.isCachedEnabled());
            String statement2Execute = queryRequest.getStatement2Execute();
            String[] names2Execute = queryRequest.getParameterNames2Execute();
            Object[] values2Execute = queryRequest.getParameterValues2Execute();
            if (logger.isDebugEnabled()) {
                logger.debug("query hql: " + statement2Execute);
                logger.debug("query parameter names: " + ArrayUtils.toString(names2Execute));
                logger.debug("query parameter values: " + ArrayUtils.toString(values2Execute));
            }
            return hibernateTemplateExtension.findUniqueResultByNamedParam(statement2Execute, names2Execute, values2Execute);
        } finally {
            hibernateTemplateExtension.setCacheQueries(preCache);
        }
    }

    /**
     * 分页查询
     * 
     * @param queryRequest
     * @return
     */
    public Page getPageResult(QueryRequest query) {
        AbstractQueryRequest queryRequest = (AbstractQueryRequest) query;
        String statement2Execute = queryRequest.getStatement2Execute();
        String statement2Count = null;
        if (queryRequest.isManualCount()) {
            statement2Count = queryRequest.getStatement4Count();
        } else {
            int beginPos = statement2Execute.toLowerCase().indexOf("from");
            Assert.isTrue(beginPos != -1, statement2Execute + " invalid , from clause not found!");
            statement2Count = "select count(*) " + statement2Execute.substring(beginPos);
        }
        final String[] names2Execute = queryRequest.getParameterNames2Execute();
        final Object[] values2Execute = queryRequest.getParameterValues2Execute();
        long totalCount = ((Number) hibernateTemplateExtension.findUniqueResultByNamedParam(statement2Count, names2Execute, values2Execute)).longValue();
        if (totalCount < 1) {
            return new Page(0, 0, queryRequest.getMaxSizePerPage(), Collections.EMPTY_LIST);
        }
        final int startIndex = Page.getStartOfPage(queryRequest.getStartPageNo(), queryRequest.getMaxSizePerPage());
        List temp = hibernateTemplateExtension.findByNamedParam(statement2Execute, names2Execute, values2Execute, startIndex, queryRequest.getMaxSizePerPage());
        return new Page(queryRequest.getStartPageNo(), totalCount, queryRequest.getMaxSizePerPage(), temp);
    }
}
