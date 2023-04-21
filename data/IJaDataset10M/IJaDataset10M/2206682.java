package org.jtools.io.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jpattern.condition.Condition;
import org.jtools.condition.And;
import org.jtools.condition.Not;
import org.jtools.condition.Or;

/**
 * @author Rainer
 * 
 * To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code
 * and Comments
 */
public class AntLikeFileSetFilter extends SimpleFileSetFilter {

    protected static final String[] DEFAULTEXCLUDES = { "**/*~", "**/~*", "**/#*#", "**/.#*", "**/%*%", "**/._*", "**/CVS", "**/CVS/**", "**/.cvsignore", "**/SCCS", "**/SCCS/**", "**/vssver.scc", "**/.svn", "**/.svn/**", "**/.DS_Store" };

    protected String[] defaultExcludes = null;

    protected boolean defaultValuesComputed = false;

    protected int includeOffset = 0;

    protected boolean useDefaultExcludes = true;

    public AntLikeFileSetFilter() {
    }

    public AntLikeFileSetFilter(AntLikeFileSetFilter source) {
        super(source);
        if (source != null) {
            this.includeOffset = source.includeOffset;
            this.defaultExcludes = (source.defaultExcludes == null) ? null : new String[source.defaultExcludes.length];
            if (defaultExcludes != null) System.arraycopy(source.defaultExcludes, 0, this.defaultExcludes, 0, defaultExcludes.length);
            this.defaultValuesComputed = source.defaultValuesComputed;
            this.includeOffset = source.includeOffset;
            this.useDefaultExcludes = source.useDefaultExcludes;
        }
    }

    public synchronized void addExcludePattern(String pattern) {
        addFilter(includeOffset++, new AntPatternFileFilter(true, pattern));
    }

    @Override
    public void addFilter(Condition filter) {
        throw new RuntimeException("invalid method. use addIncludePattern/addExcludePattern");
    }

    public synchronized void addIncludePattern(String pattern) {
        super.addFilter(new AntPatternFileFilter(false, pattern));
    }

    @Override
    public synchronized Collection<Condition<? super FilterableFile>> getFilters() {
        if (useDefaultExcludes && !defaultValuesComputed) {
            if (defaultExcludes == null) defaultExcludes = DEFAULTEXCLUDES;
            for (String excl : defaultExcludes) addExcludePattern(excl);
            if (includeOffset == getFilterCount()) addIncludePattern(null);
            defaultValuesComputed = true;
        }
        return super.getFilters();
    }

    public void setDefaultExcludes(String[] pattern) {
        defaultExcludes = pattern;
    }

    public synchronized void setUseDefaultExcludes(boolean onOff) {
        useDefaultExcludes = onOff;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Condition<? super FilterableFile> createCombinedFilter() {
        List<Condition<? super FilterableFile>> allFilters = new ArrayList<Condition<? super FilterableFile>>(getFilters());
        return And.valueOf(Not.valueOf(Or.valueOf(allFilters.subList(0, includeOffset))), Or.valueOf(allFilters.subList(includeOffset, allFilters.size())));
    }
}
