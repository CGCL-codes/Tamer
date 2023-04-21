package net.customware.confluence.plugin.scaffolding.macro;

import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.core.SpaceContentEntityObject;
import com.atlassian.confluence.mail.MailContentManager;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.security.Permission;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.renderer.v2.macro.MacroException;
import com.atlassian.user.User;
import net.customware.confluence.plugin.scaffolding.ListInfo;
import org.randombits.confluence.metadata.reference.ContentReference;
import org.randombits.confluence.filtering.param.criteria.content.LabelParameter;
import org.randombits.confluence.filtering.param.ParameterException;
import org.randombits.confluence.filtering.criteria.Criterion;
import org.randombits.storage.Storage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author David Peterson
 */
public class ContentOptionsMacro extends AbstractOptionsMacro {

    private static final String ALL_BRANCHES = "all";

    private static final String ANCESTOR_PARAM = "ancestor";

    private static final String BRANCHES_PARAM = "branches";

    private static final String CHILD_BRANCHES = "children";

    private static final String PARENT_BRANCHES = "parents";

    private static final String PARENT_PARAM = "parent";

    private static final String SPACEDESCRIPTION_TYPE = "spacedesc";

    private static final String SPACES_PARAM = "spaces";

    private static final String TYPES_PARAM = "types";

    private PageManager pageManager;

    private SpaceManager spaceManager;

    private PermissionManager permissionManager;

    private MailContentManager mailContentManager;

    /**
     * Constructs the PageOptionsMacro.
     */
    public ContentOptionsMacro() {
    }

    /**
     * The name of the macro.
     * 
     * @return the name of the macro.
     */
    @Override
    public String getName() {
        return "page-options";
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    /**
     * Provides information about Pages.
     * 
     * @param pageManager
     *            The current PageManager instance.
     */
    public void setPageManager(PageManager pageManager) {
        this.pageManager = pageManager;
    }

    /**
     * Provides information about Spaces.
     * 
     * @param spaceManager
     *            The current SpaceManager instance.
     */
    public void setSpaceManager(SpaceManager spaceManager) {
        this.spaceManager = spaceManager;
    }

    /**
     * Adds all decendents of the specified page to the <code>contentSet</code>.
     * 
     * @param contentSet
     *            The set of content objects.
     * @param page
     *            The page being processed.
     */
    private void addPageDecendents(SortedSet<SpaceContentEntityObject> contentSet, Page page) {
        if (page == null) return;
        Iterator<Page> i = page.getChildren().iterator();
        Page child;
        while (i.hasNext()) {
            child = i.next();
            contentSet.add(child);
            addPageDecendents(contentSet, child);
        }
    }

    private void addSpacePages(SortedSet<SpaceContentEntityObject> contentSet, Set<String> spaceKeys) {
        Iterator<String> i = spaceKeys.iterator();
        String spaceKey;
        Space space;
        while (i.hasNext()) {
            spaceKey = i.next();
            space = spaceManager.getSpace(spaceKey);
            if (space == null) throw new IllegalArgumentException("No space exists with the key '" + spaceKey + "'.");
            addSpacePages(contentSet, space);
        }
    }

    private void addSpacePages(SortedSet<SpaceContentEntityObject> contentSet, Space space) {
        contentSet.add(space.getDescription());
        contentSet.addAll(pageManager.getPages(space, true));
        contentSet.addAll(pageManager.getBlogPosts(space, true));
        contentSet.addAll(mailContentManager.getMail(space, true));
    }

    /**
     * Checks if the specified <code>ancestorPage</code> is an ancestor of the
     * specified <code>content</code> object. If the ancestor is
     * <code>null</code>,<code>true</code> is always returned.
     * 
     * @param ancestorPage
     *            The page to test against.
     * @param content
     *            The content.
     * @return <code>true</code> if the <code>ancestorPage</code> is
     *         <code>null</code>, or if <code>ancestorPage</code> is an
     *         ancestor of of <code>content</code>.
     */
    private boolean checkAncestor(Page ancestorPage, ContentEntityObject content) {
        if (ancestorPage != null) {
            if (content instanceof Page) {
                Page page = ((Page) content).getParent();
                while (page != null) {
                    if (ancestorPage.equals(page)) return true;
                    page = page.getParent();
                }
            }
            return false;
        }
        return true;
    }

    private boolean checkBranches(String branches, ContentEntityObject content) {
        if (ALL_BRANCHES.equals(branches)) return true;
        if (content instanceof Page) {
            Page page = (Page) content;
            List<Page> children = page.getChildren();
            if (PARENT_BRANCHES.equals(branches)) {
                return children != null && children.size() > 0;
            } else {
                return children == null || children.size() == 0;
            }
        }
        return true;
    }

    /**
     * Checks if the specified <code>parentPage</code> is the direct parent of
     * the specified <code>content</code> object. If the parent is
     * <code>null</code>,<code>true</code> is always returned.
     * 
     * @param parentPage
     *            The page to test against.
     * @param content
     *            The content.
     * @return <code>true</code> if the <code>parentPage</code> is
     *         <code>null</code>, or if <code>parentPage</code> is the
     *         direct parent of <code>content</code>.
     */
    private boolean checkParent(Page parentPage, ContentEntityObject content) {
        if (parentPage != null) {
            if (content instanceof Page) return parentPage.equals(((Page) content).getParent());
            return false;
        }
        return true;
    }

    /**
     * Checks if the specified content meets the space key requirements.
     * 
     * @param spaceKeys
     *            The set of space keys to check.
     * @param content
     *            The content object.
     * @return <code>true</code> if the content is ok.
     */
    private boolean checkSpaceKey(Set<String> spaceKeys, ContentEntityObject content) {
        if (spaceKeys != null) if (content instanceof SpaceContentEntityObject) return spaceKeys.contains(((SpaceContentEntityObject) content).getSpaceKey());
        return true;
    }

    /**
     * Returns <code>true</code> if the content object is one of the types
     * listed in <code>pageTypes</code>. If <code>pageTypes</code> is
     * <code>null</code>,<code>true</code> is always returned.
     * 
     * @param pageTypes
     *            The set of page types that are allowed.
     * @param content
     *            The content object.
     * @return <code>true</code> if the content matches one of the types.
     */
    private boolean checkType(Set<String> pageTypes, ContentEntityObject content) {
        String type = content.getType().toLowerCase();
        if (SPACEDESCRIPTION_TYPE.equals(type)) return false;
        if (pageTypes != null) return pageTypes.contains(type);
        return true;
    }

    /**
     * Checks if the specified content is in the trash. If not,
     * <code>true</code> is returned.
     * 
     * @param content
     *            The content to check.
     * @return <code>true</code> if the content is untrashed.
     */
    private boolean checkUntrashed(ContentEntityObject content) {
        return !content.isDeleted();
    }

    /**
     * Checks if the current user has view access to the specified content
     * object.
     * 
     * @param user
     *            The current user.
     * @param content
     *            The content object to check against.
     * @return <code>true</code> if the user has access.
     */
    private boolean checkUserAccess(User user, ContentEntityObject content) {
        if (permissionManager.hasPermission(user, Permission.ADMINISTER, PermissionManager.TARGET_APPLICATION)) return true;
        return permissionManager.hasPermission(user, Permission.VIEW, content);
    }

    /**
     * Checks if the content matches certain labels.
     * 
     * @param labelCriterion
     *            The criterion built from a {@link LabelParameter}. This may
     *            be <code>null</code> to skip the test.
     * @param ceo
     *            The content to check
     * @return <code>true</code> if <code>labelCriterion</code> is
     *         <code>null</code> or the content matches the specified
     *         criterion.
     */
    private boolean checkLabels(Criterion labelCriterion, ContentEntityObject ceo) {
        return null == labelCriterion || labelCriterion.matches(ceo);
    }

    /**
     * Finds the page with the specified page name. If the page name is not
     * prefixed with 'SPACEKEY:', where 'SPACEKEY' is any Space Key, it is
     * assumed to be in the current space, as specified by <code>spaceKey</code>.
     * 
     * @param space
     *            The space the current page is in.
     * @param pageName
     *            The page to find, relative to the current page.
     * @return the Page, or <code>null</code> if it could not be found.
     */
    private Page findPage(Space space, String pageName) {
        String spaceKey, targetSpace, targetPage;
        spaceKey = (space != null) ? space.getKey() : null;
        int split = pageName.indexOf(":");
        targetSpace = (split == -1) ? spaceKey : pageName.substring(0, split);
        targetPage = (split == -1) ? pageName : pageName.substring(split + 1);
        return pageManager.getPage(targetSpace, targetPage);
    }

    /**
     * Adds the appropriate page options ot the list.
     * 
     * @see AbstractOptionsMacro#addOptions(net.customware.confluence.plugin.scaffolding.ListInfo)
     */
    @Override
    protected void addOptions(ListInfo info) throws MacroException {
        ContentEntityObject content = info.getContent();
        Storage params = info.getMacroParams();
        if (content == null || content.getId() == 0) {
            return;
        }
        Set<String> spaceKeys = null;
        String spaces = params.getString(SPACES_PARAM, null);
        if (spaces != null) {
            spaceKeys = new java.util.HashSet<String>();
            spaceKeys.addAll(Arrays.asList(spaces.split(",")));
        }
        Page parentPage = null;
        String parent = params.getString(PARENT_PARAM, null);
        if (parent != null) {
            if (parent.equals("@self")) {
                parentPage = (Page) content;
            } else if (parent.equals("@parent")) {
                parentPage = ((Page) content).getParent();
            } else {
                parentPage = findPage(info.getSpace(), parent);
            }
            if (parentPage == null) throw new MacroException("Requested parent page '" + parent + "' does not exist.");
        }
        Page ancestorPage = null;
        String ancestor = params.getString(ANCESTOR_PARAM, null);
        if (ancestor != null) {
            ancestorPage = findPage(info.getSpace(), ancestor);
            if (ancestorPage == null) throw new MacroException("Requested ancestor page '" + ancestor + "' does not exist.");
        }
        String branches = params.getString(BRANCHES_PARAM, ALL_BRANCHES);
        if (!ALL_BRANCHES.equals(branches) && !PARENT_BRANCHES.equals(branches) && !CHILD_BRANCHES.equals(branches)) throw new MacroException("The '" + BRANCHES_PARAM + "' must be either '" + ALL_BRANCHES + "', '" + PARENT_BRANCHES + "' or '" + CHILD_BRANCHES + "'.");
        Set<String> pageTypes = null;
        String[] types = params.getStringArray(TYPES_PARAM, null);
        if (types != null) {
            pageTypes = new java.util.HashSet<String>();
            pageTypes.addAll(Arrays.asList(types));
        }
        Criterion labelCriterion = null;
        try {
            labelCriterion = new LabelParameter().createCriterion(info);
        } catch (ParameterException pe) {
            throw new MacroException(pe);
        }
        SortedSet<SpaceContentEntityObject> contentSet = new TreeSet<SpaceContentEntityObject>();
        if (parentPage != null) {
            contentSet.addAll(parentPage.getChildren());
        } else if (ancestorPage != null) {
            addPageDecendents(contentSet, ancestorPage);
        } else {
            if (spaceKeys == null) {
                spaceKeys = new java.util.HashSet<String>();
                if (info.getSpace().getKey() != null) spaceKeys.add(info.getSpace().getKey());
            }
            addSpacePages(contentSet, spaceKeys);
        }
        if (contentSet.size() > 0) {
            ContentEntityObject entity;
            Iterator<SpaceContentEntityObject> i = contentSet.iterator();
            while (i.hasNext()) {
                entity = i.next();
                if (!entity.equals(content) && checkUserAccess(info.getCurrentUser(), entity) && checkUntrashed(entity) && checkSpaceKey(spaceKeys, entity) && checkParent(parentPage, entity) && checkBranches(branches, entity) && checkType(pageTypes, entity) && checkAncestor(ancestorPage, entity) && checkLabels(labelCriterion, entity)) {
                    info.addListOption(new ContentReference(entity));
                }
            }
        }
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public void setUserAccessor(UserAccessor userAccessor) {
    }

    public void setMailContentManager(MailContentManager mailContentManager) {
        this.mailContentManager = mailContentManager;
    }
}
