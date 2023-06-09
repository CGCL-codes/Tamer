package org.rubypeople.rdt.internal.core.search;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.rubypeople.rdt.core.search.IRubySearchScope;
import org.rubypeople.rdt.core.search.SearchDocument;
import org.rubypeople.rdt.core.search.SearchParticipant;
import org.rubypeople.rdt.core.search.SearchPattern;
import org.rubypeople.rdt.core.search.SearchRequestor;
import org.rubypeople.rdt.internal.core.search.indexing.SourceIndexer;
import org.rubypeople.rdt.internal.core.search.matching.MatchLocator;

public class RubySearchParticipant extends SearchParticipant {

    private IndexSelector indexSelector;

    @Override
    public SearchDocument getDocument(String documentPath) {
        return new RubySearchDocument(documentPath, this);
    }

    @Override
    public void indexDocument(SearchDocument document, IPath indexLocation) {
        document.removeAllIndexEntries();
        String documentPath = document.getPath();
        if (org.rubypeople.rdt.internal.core.util.Util.isRubyLikeFileName(documentPath)) {
            new SourceIndexer(document).indexDocument();
        }
    }

    public IPath[] selectIndexes(SearchPattern pattern, IRubySearchScope scope) {
        if (this.indexSelector == null) {
            this.indexSelector = new IndexSelector(scope, pattern);
        }
        return this.indexSelector.getIndexLocations();
    }

    public void locateMatches(SearchDocument[] indexMatches, SearchPattern pattern, IRubySearchScope scope, SearchRequestor requestor, IProgressMonitor monitor) throws CoreException {
        MatchLocator matchLocator = new MatchLocator(pattern, requestor, scope, monitor == null ? null : new SubProgressMonitor(monitor, 95));
        if (monitor != null && monitor.isCanceled()) throw new OperationCanceledException();
        matchLocator.locateMatches(indexMatches);
    }
}
