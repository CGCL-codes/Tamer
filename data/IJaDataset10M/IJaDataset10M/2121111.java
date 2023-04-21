package com.ecyrd.jspwiki.tags;

import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import com.ecyrd.jspwiki.providers.ProviderException;

/**
 *  Includes body, if the request context matches.  To understand more about
 *  RequestContexts, please look at the WikiContext class.
 *
 *  @since 2.0
 *  @see com.ecyrd.jspwiki.WikiContext
 */
public class CheckRequestContextTag extends WikiTagBase {

    private static final long serialVersionUID = 0L;

    private String m_context;

    private String[] m_contextList = {};

    /**
     *  {@inheritDoc}
     */
    @Override
    public void initTag() {
        super.initTag();
        m_context = null;
        m_contextList = new String[0];
    }

    /**
     *  Returns the context.
     *  
     *  @return Return the context.
     */
    public String getContext() {
        return m_context;
    }

    /**
     *  Set the context to check for.
     *  
     *  @param arg One of the RequestsContexts.
     */
    public void setContext(String arg) {
        m_context = arg;
        m_contextList = StringUtils.split(arg, '|');
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int doWikiStartTag() throws IOException, ProviderException {
        for (int i = 0; i < m_contextList.length; i++) {
            String ctx = m_wikiContext.getRequestContext();
            String checkedCtx = m_contextList[i];
            if (checkedCtx.length() > 0) {
                if (checkedCtx.charAt(0) == '!') {
                    if (!ctx.equalsIgnoreCase(checkedCtx.substring(1))) {
                        return EVAL_BODY_INCLUDE;
                    }
                } else if (ctx.equalsIgnoreCase(m_contextList[i])) {
                    return EVAL_BODY_INCLUDE;
                }
            }
        }
        return SKIP_BODY;
    }
}
