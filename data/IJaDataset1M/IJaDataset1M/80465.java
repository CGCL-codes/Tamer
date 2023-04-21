package org.utupiu.nibbana.waf.view;

import org.utupiu.nibbana.core.NibbanaException;
import org.utupiu.nibbana.waf.AbstractWAFBean;
import org.utupiu.nibbana.waf.AbstractWAFEntry;
import org.utupiu.nibbana.waf.enums.SnippetStyleType;

public abstract class AbstractSnippetBean extends AbstractWAFBean {

    protected SnippetEntry snippetEntry;

    public SnippetStyleType style;

    public String source;

    public void init(AbstractWAFEntry wafEntry) throws NibbanaException {
        this.snippetEntry = (SnippetEntry) wafEntry;
    }

    @Override
    public String perform() throws NibbanaException {
        try {
            if (hasPreProcess) {
                preProcess();
            }
            process();
            if (hasPostProcess) {
                postProcess();
            }
            return null;
        } catch (Exception e) {
            throw new NibbanaException(e);
        }
    }
}
