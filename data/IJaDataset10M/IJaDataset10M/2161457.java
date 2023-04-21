package de.iritgo.aktera.ui.form;

import de.iritgo.aktera.model.ModelException;
import de.iritgo.aktera.model.ModelRequest;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Used to describe a formular.
 */
public class FormularDescriptor implements Serializable {

    /** Formular title. */
    protected String title;

    /** Formular icon. */
    protected String icon;

    /** General resource bundle. */
    protected String bundle;

    /** Title resource bundle. */
    protected String titleBundle;

    /** Field groups. */
    protected List<GroupDescriptor> groups;

    /** Map to retrieve groups by key. */
    protected Map<String, GroupDescriptor> groupsByKey;

    /** Pages. */
    protected List<PageDescriptor> pages;

    /** Map to retrieve pages by key. */
    protected Map<String, PageDescriptor> pagesByKey;

    /** Last created page. */
    protected PageDescriptor lastCreatedPage;

    /** Persistent attribute containing the unique id. */
    protected String idField;

    /** No changes allowed if true. */
    protected boolean readOnly;

    /** Width of the label column in percent. */
    protected int labelWidth;

    /** The persistents that are edited by this formular.*/
    protected PersistentDescriptor persistents;

    /** Formular commands. */
    protected CommandDescriptor commands;

    /** The current page. */
    protected int page = 0;

    /** The formular id (model or class name) */
    protected String id;

    /**
	 * Create a new, empty formular.
	 */
    public FormularDescriptor() {
        groups = new LinkedList<GroupDescriptor>();
        groupsByKey = new HashMap<String, GroupDescriptor>();
        pages = new LinkedList<PageDescriptor>();
        pagesByKey = new HashMap<String, PageDescriptor>();
        commands = new CommandDescriptor();
    }

    /**
	 * Add a new page.
	 *
	 * @param label The resource key of the page label.
	 * @param bundle The resource bundle to lookup for the label.
	 * @return The new page.
	 */
    public PageDescriptor addPage(String label, String bundle) {
        PageDescriptor page = new PageDescriptor(label, bundle);
        lastCreatedPage = page;
        pages.add(page);
        pagesByKey.put(label, page);
        return page;
    }

    /**
	 * Add a new group.
	 *
	 * @param label The resource key of the group label.
	 * @param bundle The resource bundle to lookup for the label.
	 * @return The new group.
	 */
    public GroupDescriptor addGroup(String label, String bundle) {
        GroupDescriptor group = new GroupDescriptor(label, bundle);
        groups.add(group);
        groupsByKey.put(label, group);
        if (lastCreatedPage != null) {
            lastCreatedPage.addGroup(group);
        }
        return group;
    }

    /**
	 * Return an iterator over all groups.
	 *
	 * @return A group iterator.
	 */
    public Iterator groupIterator() {
        return groups.iterator();
    }

    /**
	 * Get the groups.
	 *
	 * @return The formular groups
	 */
    public List<GroupDescriptor> getGroups() {
        return groups;
    }

    /**
	 * Retrieve a group by key.
	 *
	 * @param key The group key.
	 * @return The group.
	 * @throws ModelException If the group wasn't found.
	 */
    public GroupDescriptor getGroup(String key) throws ModelException {
        GroupDescriptor group = groupsByKey.get(key);
        if (group == null) {
            throw new ModelException("Unable to find group '" + key + "' in formular '" + id + "'");
        }
        return group;
    }

    /**
	 * Check if the formular contains a given group.
	 *
	 * @param key The group key.
	 * @return True if the formular contains a given group.
	 */
    public boolean hasGroup(String key) {
        return groupsByKey.get(key) != null;
    }

    /**
	 * Check if the formular contains a given group.
	 *
	 * @param key The group key.
	 * @return True if the formular contains a given group.
	 */
    public boolean containsGroup(String key) {
        return hasGroup(key);
    }

    /**
	 * Return an iterator over all pages.
	 *
	 * @return A page iterator.
	 */
    public Iterator pageIterator() {
        return pages.iterator();
    }

    /**
	 * Get the number of defined pages.
	 *
	 * @return The page count.
	 */
    public int getPageCount() {
        return pages.size();
    }

    /**
	 * Check wether this formular has pages or not.
	 *
	 * @return True if the formular contains pages.
	 */
    public boolean hasPages() {
        return pages.size() > 0;
    }

    /**
	 * Check wether this formular contains a page with the given name.
	 *
	 * @return True if the formular contains the page.
	 */
    public boolean hasPage(String name) {
        return true;
    }

    /**
	 * Get a page by index.
	 *
	 * @param index The page index.
	 * @return The page.
	 */
    public PageDescriptor getPage(int index) {
        return pages.get(index);
    }

    /**
	 * Set the id attribute name.
	 *
	 * @param idField The new id field.
	 */
    public void setIdField(String idField) {
        this.idField = idField;
    }

    /**
	 * Get the id attribute name.
	 *
	 * @return The id field.
	 */
    public String getIdField() {
        return idField;
    }

    /**
	 * Get the title.
	 *
	 * @return The title.
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * Set the title.
	 *
	 * @return title The new title.
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * Get the icon.
	 *
	 * @return The icon.
	 */
    public String getIcon() {
        return icon;
    }

    /**
	 * Set the icon.
	 *
	 * @return icon The new icon.
	 */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
	 * Get the ressource bundle name.
	 *
	 * @return The ressource bundle name.
	 */
    public String getBundle() {
        return bundle;
    }

    /**
	 * Set the ressource bundle name.
	 *
	 * @return bundle The new ressource bundle name.
	 */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    /**
	 * Get the read only state.
	 *
	 * @return The read onyl state.
	 */
    public boolean getReadOnly() {
        return readOnly;
    }

    /**
	 * Get the read only state.
	 *
	 * @return The read onyl state.
	 */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
	 * Set the read only state.
	 *
	 * @return readOnly The new read only state.
	 */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
	 * Set the label width.
	 *
	 * @param labelWidth The new label width.
	 */
    public void setLabelWidth(int labelWidth) {
        this.labelWidth = labelWidth;
    }

    /**
	 * Get the label width.
	 *
	 * @param labelWidth The new label width.
	 */
    public int getLabelWidth() {
        return labelWidth;
    }

    /**
	 * Sort all pages, groups and fields by comparing their relative position.
	 */
    public void sort() {
        Collections.sort(pages, new Comparator<PageDescriptor>() {

            public int compare(PageDescriptor page1, PageDescriptor page2) {
                return page1.getPosition() - page2.getPosition();
            }
        });
        Collections.sort(groups, new Comparator<GroupDescriptor>() {

            public int compare(GroupDescriptor group1, GroupDescriptor group2) {
                return group1.getPosition() - group2.getPosition();
            }
        });
        for (PageDescriptor page : pages) {
            page.sort();
        }
    }

    /**
	 * Get the index of the current page.
	 *
	 * @param req The model request.
	 * @param persistents The edited persistent descriptor.
	 * @return The current page.
	 */
    public PageDescriptor getCurrentPage(ModelRequest req, PersistentDescriptor persistents) {
        return getPage(page);
    }

    /**
	 * Get the persistents.
	 *
	 * @return The persistents.
	 */
    public PersistentDescriptor getPersistents() {
        return persistents;
    }

    /**
	 * Set the persistents.
	 *
	 * @return persistents The new persistents.
	 */
    public void setPersistents(PersistentDescriptor persistents) {
        this.persistents = persistents;
    }

    /**
	 * Get the forumlar commands.
	 *
	 * @return The forumlar commands.
	 */
    public CommandDescriptor getCommands() {
        return commands;
    }

    /**
	 * Set the current page.
	 *
	 * @param page The new current page.
	 */
    public void setPage(int page) {
        this.page = page;
    }

    /**
	 * Get the current page.
	 *
	 * @return The current page.
	 */
    public int getPage() {
        return page;
    }

    /**
	 * Get the number of the page that contains the specified field.
	 *
	 * @param name The field name.
	 * @return The page number or -1 if the field wasn't found.
	 */
    public int getPageWithField(String name) {
        int pageNum = 0;
        for (PageDescriptor page : pages) {
            for (GroupDescriptor group : page.getGroups()) {
                if (group.containsField(name)) {
                    return pageNum;
                }
            }
            ++pageNum;
        }
        return -1;
    }

    /**
	 * Check wether the formular has visible buttons.
	 *
	 * @return True if the formular has visible buttons.
	 */
    public boolean hasVisibleButtons() {
        return commands.hasVisibleButtons();
    }

    /**
	 * Set the title resource bundle.
	 *
	 * @param bundle The new title resource bundle.
	 */
    public void setTitleBundle(String titleBundle) {
        this.titleBundle = titleBundle;
    }

    /**
	 * Get the title resource bundle.
	 *
	 * @return The title resource bundle.
	 */
    public String getTitleBundle() {
        return titleBundle != null ? titleBundle : bundle;
    }

    /**
	 * Check if the formular contains a given field.
	 *
	 * @param name The name of the field to check.
	 * @return True if the formular contains the field.
	 */
    public boolean hasField(String name) {
        if (hasPages()) {
            for (PageDescriptor page : pages) {
                for (GroupDescriptor group : page.getGroups()) {
                    if (group.containsField(name)) {
                        return true;
                    }
                }
            }
        } else {
            for (GroupDescriptor group : groups) {
                if (group.containsField(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Check if the formular contains a given field.
	 *
	 * @param name The name of the field to check.
	 * @return True if the formular contains the field.
	 */
    public boolean containsField(String name) {
        return hasField(name);
    }

    /**
	 * Set the formular id.
	 *
	 * @param id The formular id.
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * Set the formular id.
	 *
	 * @return The formular id.
	 */
    public String getId() {
        return id;
    }

    public FieldDescriptor getField(String name) {
        for (GroupDescriptor group : groups) {
            try {
                return group.getField(name);
            } catch (ModelException notFound) {
            }
        }
        return null;
    }
}
