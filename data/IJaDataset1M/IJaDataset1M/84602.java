package org.sss.common.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sss.common.model.Event;
import org.sss.common.model.IContext;
import org.sss.common.model.IEventRule;
import org.sss.common.model.IModule;
import org.sss.common.model.IModuleList;
import org.sss.common.model.IParent;
import org.sss.common.model.IResult;
import org.sss.common.model.IRule;

/**
 * 模块列表实现
 * @author Jason.Hoo (latest modification by $Author: hujianxin $)
 * @version $Revision: 707 $ $Date: 2012-04-08 11:25:57 -0400 (Sun, 08 Apr 2012) $
 */
public abstract class AbstractModuleList<E extends IModule> extends ArrayList<E> implements IModuleList<E> {

    static final Log log = LogFactory.getLog(AbstractModuleList.class);

    protected final transient Map<String, Object> attributes = new HashMap<String, Object>();

    protected final transient List<IRule> defaultRules = new ArrayList<IRule>();

    protected final transient List<IEventRule> eventRules = new ArrayList<IEventRule>();

    protected transient IParent parent;

    protected transient E module = null;

    protected transient String name;

    protected transient String url;

    protected transient int index = Event.UNSELECTED_INDEX;

    protected transient int pageSize = 0;

    protected transient int page = IDX_FIRST;

    protected transient boolean sorted = false;

    protected transient boolean disposed = false;

    public AbstractModuleList(IParent parent, int pageSize) {
        this.parent = parent;
        this.pageSize = pageSize;
    }

    public synchronized void dispose() {
        if (disposed) return;
        if (log.isDebugEnabled()) log.debug("ModuleList disposing:" + url);
        for (IModule module : this) module.dispose();
        this.clear();
        attributes.clear();
        defaultRules.clear();
        eventRules.clear();
        module = null;
        parent = null;
        name = null;
        url = null;
        try {
            super.finalize();
        } catch (Throwable e) {
        }
    }

    public boolean isDisposed() {
        return disposed;
    }

    @Override
    public void clear() {
        page = IDX_FIRST;
        super.clear();
    }

    public abstract E add(IResult... results);

    protected void setSize(int size) {
        page = IDX_FIRST;
        int oldSize = super.size();
        for (int i = oldSize - 1; i >= size; i--) remove(i);
        for (int i = oldSize; i < size; i++) add();
    }

    public void copyValues(IParent parent) {
        IModuleList<IModule> list = (IModuleList) parent;
        setSize(list.fullSize());
        for (int i = 0; i < list.fullSize(); i++) get(i).copyValues(list.get(i));
    }

    public abstract Class getModuleClass();

    public int getIndex() {
        if (index >= super.size()) setIndex(Event.UNSELECTED_INDEX);
        return index;
    }

    public void setIndex(int index) {
        if (pageSize != 0) index = (page - IDX_FIRST) * pageSize + index;
        if (index >= super.size() || index < 0) index = Event.UNSELECTED_INDEX;
        if (index != Event.UNSELECTED_INDEX) module = get(index); else module = null;
        this.index = index;
    }

    public void addChild() {
        for (int i = 0; i < super.size(); i++) get(i).addChild();
    }

    public void init(IContext ctx) {
        invokeDefaultRules(ctx);
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Object setAttribute(String name, Object attribute) {
        return attributes.put(name, attribute);
    }

    public Set getAttributeNameSet() {
        return attributes.keySet();
    }

    public IParent getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addDefaultRule(IRule rule) {
        defaultRules.add(rule);
    }

    public void addEventRule(IEventRule rule) {
        eventRules.add(rule);
    }

    protected void sort() {
        sorted = true;
        RuleUtils.sort(defaultRules);
        RuleUtils.sort2(eventRules);
    }

    public boolean invokeDefaultRules(IContext ctx) {
        if (!sorted) sort();
        return RuleUtils.invoke(defaultRules, ctx);
    }

    public boolean invokeEventRules(IContext ctx, Event event) {
        if (!sorted) sort();
        return RuleUtils.invoke(eventRules, ctx, event);
    }

    public String getSubUrl(Object name) {
        return url + "[" + name + "]";
    }

    public void updateSubUrls(int index) {
        for (int i = index; i < super.size(); i++) {
            IModule module = get(i);
            module.setName(String.valueOf(i));
            module.setUrl(getSubUrl(i));
            if (!module.isChildAdded()) module.addChild(); else module.updateSubUrls(0);
        }
    }

    @Override
    public boolean add(E module) {
        add(super.size(), module);
        return true;
    }

    @Override
    public void add(int index, E module) {
        this.setIndex(Event.UNSELECTED_INDEX);
        super.add(index, module);
        updateSubUrls(index);
    }

    @Override
    public boolean remove(Object o) {
        E e = this.remove(indexOf(o));
        return e != null;
    }

    @Override
    public E remove(int index) {
        this.setIndex(Event.UNSELECTED_INDEX);
        E e = super.remove(index);
        updateSubUrls(index);
        return e;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(super.size(), c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        this.setIndex(Event.UNSELECTED_INDEX);
        IModule[] modules = c.toArray(IModule.EMPTY);
        for (int i = 0; i < modules.length; i++) {
            modules[i].setName(String.valueOf(index));
            modules[i].setUrl(getSubUrl(index));
        }
        super.addAll(index, c);
        updateSubUrls(index);
        return true;
    }

    @Override
    public String toString() {
        return url;
    }

    @Override
    public int size() {
        if (pageSize == 0 || isEmpty()) return super.size();
        return page == getPages() ? super.size() - pageSize * (page - IDX_FIRST) : pageSize;
    }

    public void first() {
        page = IDX_FIRST;
    }

    public void last() {
        page = getPages();
    }

    public void next(int pages) {
        if (page < this.getPages()) page++;
    }

    public void previous(int pages) {
        if (page > IDX_FIRST) page--;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page >= IDX_FIRST && page <= getPages() ? page : IDX_FIRST;
    }

    public int getPages() {
        if (pageSize <= 0) return 0;
        return super.size() % pageSize == 0 ? super.size() / pageSize : super.size() / pageSize + 1;
    }

    public boolean hasPrevious() {
        return page > 1;
    }

    public boolean hasNext() {
        return page < getPages();
    }

    public int fullSize() {
        return super.size();
    }

    public int getRealIndex(int index) {
        if (pageSize == 0) return index;
        return (page - IDX_FIRST) * pageSize + index;
    }
}
