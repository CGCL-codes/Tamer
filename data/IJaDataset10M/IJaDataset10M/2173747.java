package no.jish.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.jish.luclipse.Activator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

public class WorkspaceClassFinder {

    public Map<String, IType> findAllSubclassesInWorkspace(String parentClassName) {
        final List<IType> referenceMatches = new ArrayList<IType>();
        SearchParticipant[] searchParticipants = new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() };
        IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
        SearchRequestor searchRequestor = new SearchRequestor() {

            @Override
            public void acceptSearchMatch(SearchMatch match) throws CoreException {
                if ((match.getAccuracy() == SearchMatch.A_ACCURATE) && !match.isInsideDocComment()) {
                    IType element = (IType) match.getElement();
                    int flags = element.getFlags();
                    if (Flags.isPublic(flags)) {
                        referenceMatches.add(element);
                    }
                }
            }
        };
        try {
            SearchPattern searchPattern = SearchPattern.createPattern(parentClassName, IJavaSearchConstants.CLASS, IJavaSearchConstants.DECLARATIONS, SearchPattern.R_EQUIVALENT_MATCH | SearchPattern.R_PATTERN_MATCH | SearchPattern.R_CASE_SENSITIVE);
            SearchEngine searchEngine = new SearchEngine();
            searchEngine.search(searchPattern, searchParticipants, scope, searchRequestor, null);
        } catch (CoreException e) {
            e.printStackTrace();
        }
        Map<String, IType> workspaceAnalyzerMap = new HashMap<String, IType>();
        for (IType element : referenceMatches) {
            workspaceAnalyzerMap.put(element.getFullyQualifiedName().trim(), element);
            try {
                IType[] allSubtypes = element.newTypeHierarchy(element.getJavaProject(), null).getAllSubtypes(element);
                for (IType subType : allSubtypes) {
                    int flags = subType.getFlags();
                    if (Flags.isPublic(flags) && !Flags.isAbstract(flags)) {
                        workspaceAnalyzerMap.put(subType.getFullyQualifiedName().trim(), subType);
                    }
                }
            } catch (JavaModelException e) {
                Activator.log("Could not build hierarchy", e);
            }
        }
        return workspaceAnalyzerMap;
    }
}
