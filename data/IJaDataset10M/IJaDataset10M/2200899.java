package org.rubypeople.rdt.internal.ui.text.spelling;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.texteditor.spelling.ISpellingEngine;
import org.eclipse.ui.texteditor.spelling.ISpellingProblemCollector;
import org.eclipse.ui.texteditor.spelling.SpellingContext;
import org.rubypeople.rdt.core.RubyCore;

/**
 * Default spelling engine.
 *
 * @since 3.1
 */
public class DefaultSpellingEngine implements ISpellingEngine {

    /** Text content type */
    private static final IContentType TEXT_CONTENT_TYPE = Platform.getContentTypeManager().getContentType(IContentTypeManager.CT_TEXT);

    /** Ruby source content type */
    private static final IContentType RUBY_CONTENT_TYPE = Platform.getContentTypeManager().getContentType(RubyCore.RUBY_SOURCE_CONTENT_TYPE);

    /** Available spelling engines by content type */
    private Map fEngines = new HashMap();

    /**
	 * Initialize concrete engines.
	 */
    public DefaultSpellingEngine() {
        if (RUBY_CONTENT_TYPE != null) fEngines.put(RUBY_CONTENT_TYPE, new RubySpellingEngine());
        if (TEXT_CONTENT_TYPE != null) fEngines.put(TEXT_CONTENT_TYPE, new TextSpellingEngine());
    }

    public void check(IDocument document, IRegion[] regions, SpellingContext context, ISpellingProblemCollector collector, IProgressMonitor monitor) {
        ISpellingEngine engine = getEngine(context.getContentType());
        if (engine == null) engine = getEngine(TEXT_CONTENT_TYPE);
        if (engine != null) engine.check(document, regions, context, collector, monitor);
    }

    /**
	 * Returns a spelling engine for the given content type or
	 * <code>null</code> if none could be found.
	 *
	 * @param contentType the content type
	 * @return a spelling engine for the given content type or
	 *         <code>null</code> if none could be found
	 */
    private ISpellingEngine getEngine(IContentType contentType) {
        if (contentType == null) return null;
        if (fEngines.containsKey(contentType)) return (ISpellingEngine) fEngines.get(contentType);
        return getEngine(contentType.getBaseType());
    }
}
