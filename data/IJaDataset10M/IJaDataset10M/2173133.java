package net.simpleframework.content;

import java.io.File;
import net.simpleframework.ado.lucene.AbstractLuceneManager;
import net.simpleframework.ado.lucene.LuceneQuery;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.ado.DataObjectException;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractContentLuceneManager extends AbstractLuceneManager {

    private final ComponentParameter compParameter;

    public AbstractContentLuceneManager(final ComponentParameter compParameter, final File indexPath) {
        super(indexPath);
        this.compParameter = compParameter;
    }

    protected ComponentParameter getComponentParameter() {
        return compParameter;
    }

    @Override
    protected LuceneQuery<?> createLuceneQuery(final Query query) {
        try {
            if (!indexExists()) {
                rebuildAllBackground(false);
                return null;
            }
            if (isLocked()) {
                return null;
            }
            return new LuceneQuery<IDataObjectBean>(this, query) {

                @Override
                protected IDataObjectBean toBean(final Document doc, final float score) {
                    return queryForObject(doc.get("id"));
                }
            };
        } catch (final Exception e) {
            throw DataObjectException.wrapException(e);
        }
    }

    protected IDataObjectBean queryForObject(final Object id) {
        final IContentPagerHandle hdl = (IContentPagerHandle) compParameter.getComponentHandle();
        return hdl.getTableEntityManager(compParameter).queryForObjectById(id, hdl.getEntityBeanClass());
    }

    @Override
    protected IDataObjectQuery<?> getAllData() {
        final IContentPagerHandle hdl = (IContentPagerHandle) compParameter.getComponentHandle();
        return hdl.getTableEntityManager(compParameter).query(null, hdl.getEntityBeanClass());
    }

    @Override
    public IApplication getApplication() {
        return WebUtils.application;
    }
}
