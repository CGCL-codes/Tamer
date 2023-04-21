package com.bluebrim.page.impl.server;

import java.util.Map;
import com.bluebrim.layout.shared.CoDesktopLayout;
import com.bluebrim.layout.shared.CoLayoutParameters;
import com.bluebrim.page.shared.CoContextualPage;
import com.bluebrim.page.shared.CoPageContext;

/**
 * Abstract superclass to pages that belongs to a <code>CoPageContext</code>
 * 
 * @author G�ran St�ck 2002-04-19
 */
public abstract class CoContextualPageImpl extends CoAbstractPage implements CoContextualPage {

    private CoPageContext m_pageContext;

    public CoContextualPageImpl(CoPageContext pageContext) {
        m_pageContext = pageContext;
    }

    /**
	 * PENDING: Do something here. Perhaps as proposed in the comment
	 */
    public void bindTextVariableValues(Map values) {
    }

    public CoPageContext getPageContext() {
        return m_pageContext;
    }

    public CoDesktopLayout getDesktop() {
        return m_pageContext.getDesktop();
    }

    public CoLayoutParameters getLayoutParameters() {
        return m_pageContext.getLayoutParameters();
    }

    public boolean isLeftSide() {
        return m_pageContext.isLeftSide(this);
    }

    public String getName() {
        return m_pageContext.getContextualNameFor(this);
    }
}
