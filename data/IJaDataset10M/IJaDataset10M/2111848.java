package com.tresys.slide.plugin.search;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.Match;
import org.eclipse.ui.IWorkingSet;
import com.tresys.slide.plugin.SLIDEPlugin;
import com.tresys.slide.plugin.builder.SLIDEProjectNature;
import com.tresys.slide.plugin.editors.fcfiles.FCFileKeywords;
import com.tresys.slide.plugin.editors.iffiles.IFFileKeywords;
import com.tresys.slide.plugin.editors.tefiles.TEFileKeywords;
import com.tresys.slide.plugin.text.rules.XMLDocumentPartitioner;
import com.tresys.slide.utility.policyxmlparser.Layer;
import com.tresys.slide.utility.policyxmlparser.Module;

public class SearchQuery implements ISearchQuery {

    private ISearchPageContainer m_selectedResources;

    private AbstractTextSearchResult m_searchResult;

    private SearchData m_searchData;

    private Vector m_vExtensions = new Vector();

    private Vector m_vLabels = new Vector();

    private byte[] m_FileData;

    private String m_szFileData;

    private Pattern m_ptnInterfaceByName;

    private Pattern m_ptnInterfaceSummary;

    private Pattern m_ptnInterfaceDescription;

    private Pattern m_ptnInterfaceContainingType;

    private Pattern m_ptnRuleDeclaringType;

    private Pattern m_ptnRuleCallingInterface;

    private Pattern m_ptnRuleAccessVector;

    private Pattern m_ptnFileContextContainingType;

    private Pattern m_ptnFileContextMatchingPath;

    private Pattern m_ptnFileContextContainingUser;

    private Pattern m_ptnFileContextContainingRole;

    private Pattern m_ptnFileContextContainingMCSLevel;

    private Pattern m_ptnTypeDeclaration;

    public SearchQuery(ISearchPageContainer selectedResources, SearchData i_searchData) {
        super();
        m_searchData = i_searchData;
        m_selectedResources = selectedResources;
        m_searchResult = new SearchResult(this);
        String sSearchText = m_searchData.getSearchText();
        if (!m_searchData.getBoolean(SearchData.CASE_SENSITIVE).booleanValue()) sSearchText = sSearchText.toLowerCase();
        boolean bAnyIF = false;
        boolean bAnyTE = false;
        boolean bAnyFC = false;
        if (m_searchData.getBoolean(SearchData.TE_DECLARING_TYPE).booleanValue() || m_searchData.getBoolean(SearchData.IF_CONTAINING_TYPE).booleanValue()) {
            m_ptnTypeDeclaration = Pattern.compile(TEFileKeywords.TYPE + " +.*" + sSearchText + "[^;]*;");
        }
        if (m_searchData.getBoolean(SearchData.IF_BY_NAME).booleanValue()) {
            m_ptnInterfaceByName = Pattern.compile(IFFileKeywords.INTERFACE + " *\\(`.*?" + sSearchText + "[^']*'");
            bAnyIF = true;
        }
        if (m_searchData.getBoolean(SearchData.IF_BY_SUMMARY).booleanValue()) {
            m_ptnInterfaceSummary = Pattern.compile(XMLDocumentPartitioner.IF_SUMMARY_XML_BEGIN + "(.*?)" + XMLDocumentPartitioner.IF_SUMMARY_XML_END, Pattern.DOTALL);
            bAnyIF = true;
        }
        if (m_searchData.getBoolean(SearchData.IF_BY_DESCRIPTION).booleanValue()) {
            m_ptnInterfaceDescription = Pattern.compile(XMLDocumentPartitioner.IF_DESCRIPTION_XML_BEGIN + "(.*?)" + XMLDocumentPartitioner.IF_DESCRIPTION_XML_END, Pattern.DOTALL);
            bAnyIF = true;
        }
        if (m_searchData.getBoolean(SearchData.IF_CONTAINING_TYPE).booleanValue()) {
            m_ptnInterfaceContainingType = Pattern.compile(IFFileKeywords.GEN_REQUIRE + "*\\(`([^']*)'\\)", Pattern.DOTALL);
            bAnyIF = true;
        }
        String sContext = "(?::c[0-9]+\\.c[0-9]+)?";
        String sMLS = "s[0-9]+(?:-s[0-9]+)?";
        if (m_searchData.getBoolean(SearchData.FC_CONTAINING_USER).booleanValue()) {
            m_ptnFileContextContainingUser = Pattern.compile(FCFileKeywords.GEN_CONTEXT + ".*\\([^:]*(" + sSearchText + ")[^:]*:[^:]*:[^,]*," + sMLS + sContext + "\\)");
            bAnyFC = true;
        }
        if (m_searchData.getBoolean(SearchData.FC_CONTAINING_ROLE).booleanValue()) {
            m_ptnFileContextContainingRole = Pattern.compile(FCFileKeywords.GEN_CONTEXT + ".*\\([^:]*:[^:]*(" + sSearchText + ")[^:]*:[^,]*," + sMLS + sContext + "\\)");
            bAnyFC = true;
        }
        if (m_searchData.getBoolean(SearchData.FC_CONTAINING_TYPE).booleanValue()) {
            m_ptnFileContextContainingType = Pattern.compile(FCFileKeywords.GEN_CONTEXT + ".*\\([^:]*:[^:]*:[^,]*(" + sSearchText + ")[^,]*," + sMLS + sContext + "\\)");
            bAnyFC = true;
        }
        if (m_searchData.getBoolean(SearchData.FC_CONTAINING_MLS_LEVEL).booleanValue()) {
            m_ptnFileContextContainingMCSLevel = Pattern.compile(FCFileKeywords.GEN_CONTEXT + ".*\\([^:]*:[^:]*:[^,]*,[^:)]*" + sSearchText + "[^:)]*" + sContext + "\\)");
            bAnyFC = true;
        }
        if (m_searchData.getBoolean(SearchData.FC_MATCHING_PATH).booleanValue()) {
            m_ptnFileContextMatchingPath = Pattern.compile("([^ \t\n\r]*)[ \t]*-?.?[\t ]*" + FCFileKeywords.GEN_CONTEXT + "\\([^)]*\\)");
            bAnyFC = true;
        }
        if (m_searchData.getBoolean(SearchData.TE_DECLARING_TYPE).booleanValue()) {
            m_ptnRuleDeclaringType = m_ptnTypeDeclaration;
            bAnyTE = true;
        }
        if (m_searchData.getBoolean(SearchData.TE_INTERFACE_CALL).booleanValue()) {
            m_ptnRuleCallingInterface = Pattern.compile("[^ \r\n\t]*" + sSearchText + "[^ \r\n\t]* *\\([^)]*\\)");
            bAnyTE = true;
        }
        if (m_searchData.getBoolean(SearchData.TE_ACCESS_VECTOR).booleanValue()) {
            m_ptnRuleAccessVector = Pattern.compile("(?:allow|dontaudit|auditallow|neverallow) .*?" + sSearchText + "[^\r\n;]*;");
            bAnyTE = true;
        }
        if (bAnyTE) {
            m_vExtensions.add(new String("te"));
            m_vLabels.add(new String("Type Enforcement"));
        }
        if (bAnyIF) {
            m_vExtensions.add(new String("if"));
            m_vLabels.add(new String("Interface"));
        }
        if (bAnyFC) {
            m_vExtensions.add(new String("fc"));
            m_vLabels.add(new String("File Context"));
        }
    }

    /**
	 * run the search operation
	 */
    public IStatus run(IProgressMonitor monitor) throws OperationCanceledException {
        Iterator itr = null;
        switch(m_selectedResources.getSelectedScope()) {
            case ISearchPageContainer.WORKSPACE_SCOPE:
                {
                    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                    IProject[] projects = root.getProjects();
                    Vector myProjects = new Vector();
                    for (int i = projects.length - 1; i >= 0; i--) myProjects.add(projects[i]);
                    itr = myProjects.iterator();
                }
                break;
            case ISearchPageContainer.SELECTION_SCOPE:
                itr = ((IStructuredSelection) m_selectedResources.getSelection()).iterator();
                break;
            case ISearchPageContainer.WORKING_SET_SCOPE:
                IWorkingSet[] set = m_selectedResources.getSelectedWorkingSets();
                Vector selection = new Vector();
                for (int i = set.length - 1; i >= 0; i--) {
                    IAdaptable[] subset = set[i].getElements();
                    for (int j = subset.length - 1; j >= 0; j--) selection.add(subset[j]);
                }
                itr = selection.iterator();
                break;
            case ISearchPageContainer.SELECTED_PROJECTS_SCOPE:
                {
                    Vector projects = new Vector();
                    itr = ((IStructuredSelection) m_selectedResources.getSelection()).iterator();
                    while (itr.hasNext()) {
                        IProject proj = null;
                        Object item = itr.next();
                        if (item instanceof IResource) proj = ((IResource) item).getProject(); else if (item instanceof Module) proj = ((Module) item).getProject(); else if (item instanceof Layer) proj = ((Layer) item).getProject();
                        if (proj != null && !projects.contains(proj)) projects.add(proj);
                    }
                    itr = projects.iterator();
                }
                break;
        }
        if (itr == null) return new Status(IStatus.WARNING, SLIDEPlugin.PLUGIN_ID, IStatus.OK, "No search elements selected", null);
        try {
            while (itr.hasNext()) {
                Object currSelection = itr.next();
                if (currSelection instanceof IProject) {
                    if (!((IProject) currSelection).isOpen()) continue;
                    if (!((IProject) currSelection).hasNature(SLIDEProjectNature.NATURE_ID)) continue;
                    search((IProject) currSelection);
                } else if (currSelection instanceof IContainer) search((IContainer) currSelection); else if (currSelection instanceof Layer) search((Layer) currSelection); else if (currSelection instanceof Module) search((Module) currSelection); else if (currSelection instanceof IFile) search((IFile) currSelection); else System.out.println("Unexpected type in SearchQuery::run: " + currSelection.getClass().getName());
            }
        } catch (CoreException ce) {
        }
        return new Status(IStatus.OK, SLIDEPlugin.PLUGIN_ID, IStatus.OK, "Search Complete", null);
    }

    /**
	 * search the layer
	 * @param i_Layer
	 */
    protected void search(Layer i_Layer) {
        Collection modules = i_Layer.getModules().values();
        Iterator iter = modules.iterator();
        while (iter.hasNext()) {
            search((Module) iter.next());
        }
    }

    /**
	 * search the module
	 * @param i_Module
	 */
    protected void search(Module i_Module) {
        search(i_Module.getFCFile());
        search(i_Module.getIFFile());
        search(i_Module.getTEFile());
    }

    /**
	 * search a container
	 * @param i_Folder - container (file, folder, project) to search
	 */
    protected void search(IContainer i_Folder) {
        try {
            IResource[] items = i_Folder.members();
            for (int i = items.length - 1; i >= 0; i--) {
                if (items[i] instanceof IContainer) search((IContainer) items[i]); else if (items[i] instanceof IFile) search((IFile) items[i]); else System.out.println("Unknown type in container search: " + items[i].getClass().getName());
            }
        } catch (CoreException ce) {
            ce.printStackTrace();
        }
    }

    /**
	 * search the file 
	 * @param i_File - file to search
	 */
    protected void search(IFile i_File) {
        String sXtn = i_File.getFileExtension();
        if (sXtn == null) return;
        if (!m_vExtensions.contains(sXtn.toLowerCase())) return;
        if (sXtn.equalsIgnoreCase("te")) searchTEFile(i_File); else if (sXtn.equalsIgnoreCase("if")) searchIFFile(i_File); else if (sXtn.equalsIgnoreCase("fc")) searchFCFile(i_File);
    }

    /**
	 * read the file data
	 * @param i_File - file to read
	 * @return String of the file contents - NULL if empty
	 */
    protected String readFileData(IFile i_File) {
        m_szFileData = null;
        try {
            InputStream fileData = i_File.getContents();
            int nFileLength = fileData.available();
            if (m_FileData == null || nFileLength > m_FileData.length) m_FileData = new byte[nFileLength];
            int nRead = fileData.read(m_FileData);
            if (nRead > 0) {
                m_szFileData = new String(m_FileData, 0, nFileLength);
                if (!m_searchData.getBoolean(SearchData.CASE_SENSITIVE).booleanValue()) m_szFileData = m_szFileData.toLowerCase();
            }
        } catch (CoreException ce) {
        } catch (IOException ioe) {
        }
        return m_szFileData;
    }

    /**
	 * searcgIFFile for matches
	 * @param i_File - file to search
	 */
    protected void searchIFFile(IFile i_File) {
        String sSearchText = m_searchData.getSearchText();
        if (!m_searchData.getBoolean(SearchData.CASE_SENSITIVE).booleanValue()) sSearchText = sSearchText.toLowerCase();
        String sFileData = readFileData(i_File);
        if (sFileData == null || sFileData.length() == 0) return;
        if (m_ptnInterfaceByName != null) {
            Matcher m = m_ptnInterfaceByName.matcher(sFileData);
            while (m.find()) m_searchResult.addMatch(new Match(i_File, m.start(), m.end() - m.start()));
        }
        if (m_ptnInterfaceSummary != null) {
            Matcher m = m_ptnInterfaceSummary.matcher(sFileData);
            while (m.find()) {
                String sSummaryArea = m.group(1);
                if (sSummaryArea.indexOf(sSearchText) != -1) m_searchResult.addMatch(new Match(i_File, m.start(), m.end() - m.start()));
            }
        }
        if (m_ptnInterfaceDescription != null) {
            Matcher m = m_ptnInterfaceDescription.matcher(sFileData);
            while (m.find()) {
                String sSummaryArea = m.group(1);
                if (sSummaryArea.indexOf(sSearchText) != -1) m_searchResult.addMatch(new Match(i_File, m.start(), m.end() - m.start()));
            }
        }
        if (m_ptnInterfaceContainingType != null) {
            Matcher m = m_ptnInterfaceContainingType.matcher(sFileData);
            while (m.find()) {
                String sGenRequiresArea = m.group(0);
                Matcher m2 = m_ptnTypeDeclaration.matcher(sGenRequiresArea);
                while (m2.find()) {
                    m_searchResult.addMatch(new Match(i_File, m.start() + m2.start(), m2.end() - m2.start()));
                    break;
                }
            }
        }
    }

    /**
	 * searchFCFile for matches
	 * @param i_File - file to search
	 */
    protected void searchFCFile(IFile i_File) {
        String sSearchText = m_searchData.getSearchText();
        if (!m_searchData.getBoolean(SearchData.CASE_SENSITIVE).booleanValue()) sSearchText = sSearchText.toLowerCase();
        String sFileData = readFileData(i_File);
        if (sFileData == null) return;
        if (m_ptnFileContextContainingUser != null) {
            Matcher m = m_ptnFileContextContainingUser.matcher(sFileData);
            while (m.find()) m_searchResult.addMatch(new Match(i_File, m.start(), m.end() - m.start()));
        }
        if (m_ptnFileContextContainingRole != null) {
            Matcher m = m_ptnFileContextContainingRole.matcher(sFileData);
            while (m.find()) m_searchResult.addMatch(new Match(i_File, m.start(), m.end() - m.start()));
        }
        if (m_ptnFileContextContainingType != null) {
            Matcher m = m_ptnFileContextContainingType.matcher(sFileData);
            while (m.find()) m_searchResult.addMatch(new Match(i_File, m.start(), m.end() - m.start()));
        }
        if (m_ptnFileContextContainingMCSLevel != null) {
            Matcher m = m_ptnFileContextContainingMCSLevel.matcher(sFileData);
            while (m.find()) m_searchResult.addMatch(new Match(i_File, m.start(), m.end() - m.start()));
        }
        if (m_searchData.getBoolean(SearchData.FC_MATCHING_PATH).booleanValue()) {
            Matcher m = m_ptnFileContextMatchingPath.matcher(sFileData);
            while (m.find()) {
                String sPath = m.group(1);
                sPath = sPath.replaceAll("\\(", "(?:");
                if (Pattern.matches(sPath, sSearchText)) m_searchResult.addMatch(new Match(i_File, m.start(1), m.end(1) - m.start(1)));
            }
        }
    }

    /**
	 * searchTEFile for matches
	 * @param i_File - file to search
	 */
    protected void searchTEFile(IFile i_File) {
        String sFileData = readFileData(i_File);
        if (sFileData == null) return;
        if (m_ptnRuleDeclaringType != null) {
            Matcher m = m_ptnRuleDeclaringType.matcher(sFileData);
            while (m.find()) m_searchResult.addMatch(new Match(i_File, m.start(), m.end() - m.start()));
        }
        if (m_ptnRuleCallingInterface != null) {
            Matcher m = m_ptnRuleCallingInterface.matcher(sFileData);
            while (m.find()) m_searchResult.addMatch(new Match(i_File, m.start(), m.end() - m.start()));
        }
        if (m_ptnRuleAccessVector != null) {
            Matcher m = m_ptnRuleAccessVector.matcher(sFileData);
            while (m.find()) m_searchResult.addMatch(new Match(i_File, m.start(), m.end() - m.start()));
        }
    }

    /**
	 * getLabel of the search
	 */
    public String getLabel() {
        String szLabel = "'" + m_searchData.getSearchText() + "' in ";
        int nItems = m_vLabels.size();
        if (nItems > 1) {
            int nEnd = nItems - 1;
            for (int i = 0; i < nEnd; i++) {
                if (i > 0) szLabel += ", ";
                szLabel += (String) m_vLabels.get(i);
            }
            szLabel += " and ";
        }
        szLabel += (String) m_vLabels.get(nItems - 1);
        szLabel += " - " + m_searchResult.getMatchCount() + " matches in ";
        switch(m_selectedResources.getSelectedScope()) {
            case ISearchPageContainer.WORKSPACE_SCOPE:
                szLabel += "workspace";
                break;
            case ISearchPageContainer.SELECTED_PROJECTS_SCOPE:
                szLabel += "selected projects";
                break;
            case ISearchPageContainer.WORKING_SET_SCOPE:
                {
                    szLabel += "working set '";
                    IWorkingSet[] set = m_selectedResources.getSelectedWorkingSets();
                    for (int i = 0; i < set.length; i++) {
                        szLabel += set[i].getName();
                        if (i < set.length - 1) szLabel += ", ";
                    }
                    szLabel += "'";
                }
                break;
            case ISearchPageContainer.SELECTION_SCOPE:
                szLabel += "selection";
                break;
        }
        return (szLabel);
    }

    public boolean canRerun() {
        return false;
    }

    public boolean canRunInBackground() {
        return true;
    }

    public ISearchResult getSearchResult() {
        return m_searchResult;
    }
}
