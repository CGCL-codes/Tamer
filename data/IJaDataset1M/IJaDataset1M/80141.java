package org.sss.common.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sss.common.model.I18nValue;
import org.sss.common.model.IDatafield;
import org.sss.common.model.IModule;
import org.sss.common.model.IParent;

/**
 * 实例列表实现示例
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 50 $ $Date: 2007-12-19 23:25:16 +0800 $
 */
public class EntityListImpl<E extends IModule> extends AbstractEntityList<E> {

    static final Log log = LogFactory.getLog(EntityListImpl.class);

    protected String name = null;

    protected Class entityClass = null;

    private int page = 1;

    private int pages = 10;

    public EntityListImpl(IModule root, IParent parent, Class entityClass) {
        super(root, parent);
        this.entityClass = entityClass;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    @Override
    public I18nValue getDescription() {
        return new I18nValue("test", name);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void preAdd(String name, IParent entity) {
        entity.setName(name);
        entity.setAttribute(IDatafield.URL, this.getAttribute(IDatafield.URL) + IDatafield.URL_DELIMITER + name);
        entity.addChild();
    }

    @Override
    public boolean add(E entity) {
        preAdd(String.valueOf(this.size()), entity);
        return super.add(entity);
    }

    @Override
    public void add(int index, E entity) {
        preAdd(String.valueOf(index), entity);
        super.add(index, entity);
    }

    @Override
    public void first() {
        page = 1;
        log.info("first...");
    }

    @Override
    public void last() {
        page = pages;
        log.info("last...");
    }

    @Override
    public void next(int pages) {
        if (page < this.pages) page++;
        log.info("next...");
    }

    @Override
    public void previous(int pages) {
        if (page > 1) page--;
        log.info("previous...");
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void setPage(int page) {
        if (page > 0 && page <= pages) this.page = page;
        log.info("setPage");
    }

    @Override
    public int getPages() {
        return pages;
    }

    @Override
    public boolean hasPrevious() {
        return page > 1;
    }

    @Override
    public boolean hasNext() {
        return page < pages;
    }

    @Override
    public int fullSize() {
        return 100;
    }
}
