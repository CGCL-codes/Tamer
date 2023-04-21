package org.jcr_blog.etc;

import de.planetswebdesign.utils.jsf.manager.AbstractManager;
import de.planetswebdesign.utils.jsf.entities.Entity;
import de.planetswebdesign.utils.jsf.manager.ManagerException;
import de.planetswebdesign.utils.jsf.manager.EntityNotFoundException;
import java.util.ArrayList;
import javax.jcr.query.RowIterator;
import org.jcr_blog.entities.Blog;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * This is the enterprise tier controller for all blog entities.
 * It finds and persists blogs in the JCR repository.
 *
 * It encapsulates all JCR specific behavior.
 * 
 * @author Sebastian Prehn
 */
@AutoCreate()
@Name("blogManager")
@Scope(ScopeType.EVENT)
public class BlogManager extends AbstractManager<Blog> {

    @In(value = "jcrSession")
    private Session session;

    @In
    private CurrentUserService currentUserService;

    @Logger
    private Log log;

    static final String NODE_NAME = "blog";

    private static final String PROPERTY_STRING_TITLE = "title";

    static final String PROPERTY_STRING_RESOURCE_NAME = "resourceName";

    private static final String PROPERTY_BOOLEAN_DEFAULT_BLOG = "defaultBlog";

    private static final String PROPERTY_BOOLEAN_ALLOW_COMMENTS = "allowComments";

    private static final String PROPERTY_STRING_MODIFICATION_USER = "modificationUser";

    private static final String PROPERTY_DATE_MODIFICATION_DATE = "modificationDate";

    private static final String PROPERTY_STRING_AUTHOR = "author";

    private static final String PROPERTY_STRING_TEMPLATE = "template";

    private NodeIterator findAllNodes() throws RepositoryException {
        return this.session.getWorkspace().getQueryManager().createQuery("//blog", Query.XPATH).execute().getNodes();
    }

    private Node getApplicationNode() throws RepositoryException {
        NodeIterator nodes = session.getWorkspace().getQueryManager().createQuery("//application[@type=\"blog\"]", Query.XPATH).execute().getNodes();
        if (nodes.hasNext()) {
            return nodes.nextNode();
        } else {
            Node node = session.getRootNode().addNode("application");
            node.setProperty("type", "blog");
            return node;
        }
    }

    private Node findNodeById(String id) throws ItemNotFoundException, RepositoryException {
        Node node = session.getNodeByUUID(id);
        if (!NODE_NAME.equals(node.getName())) {
            throw new ItemNotFoundException("node " + node.getName() + " is not of type blog");
        }
        return node;
    }

    private Node findNodeByResourceName(String name) throws ItemNotFoundException, RepositoryException {
        NodeIterator nodes = session.getWorkspace().getQueryManager().createQuery("//" + NODE_NAME + "[@" + PROPERTY_STRING_RESOURCE_NAME + "=\"" + name + "\"]", Query.XPATH).execute().getNodes();
        if (nodes.hasNext()) {
            return nodes.nextNode();
        }
        throw new ItemNotFoundException(name);
    }

    public List<Blog> findAllBlogs() throws ManagerException {
        try {
            NodeIterator nodes = findAllNodes();
            List<Blog> blogs = nodes.getSize() >= 0 ? new ArrayList<Blog>((int) nodes.getSize()) : new LinkedList<Blog>();
            while (nodes.hasNext()) {
                blogs.add(nodeToBlog(nodes.nextNode()));
            }
            return blogs;
        } catch (RepositoryException ex) {
            throw new ManagerException(ex);
        }
    }

    @Override
    public Blog findById(String id) throws EntityNotFoundException, ManagerException {
        try {
            return nodeToBlog(findNodeById(id));
        } catch (ItemNotFoundException ex) {
            throw new EntityNotFoundException(id);
        } catch (RepositoryException ex) {
            throw new ManagerException(ex);
        }
    }

    public Blog findByResourceName(String blogResourceName) throws EntityNotFoundException, ManagerException {
        try {
            return nodeToBlog(findNodeByResourceName(blogResourceName));
        } catch (ItemNotFoundException ex) {
            throw new EntityNotFoundException(blogResourceName);
        } catch (RepositoryException ex) {
            throw new ManagerException(ex);
        }
    }

    @Override
    public Blog create() {
        Blog blog = new Blog();
        blog.setAllowComments(false);
        return blog;
    }

    @Override
    public void delete(String id) throws ManagerException {
        try {
            Node node = findNodeById(id);
            if (node != null) {
                node.remove();
                session.getRootNode().save();
            }
        } catch (ItemNotFoundException ex) {
        } catch (RepositoryException ex) {
            throw new ManagerException(ex);
        }
    }

    @Override
    public void save(Blog blog, Entity p) throws ManagerException {
        try {
            Node node;
            if (blog.getId() == null || blog.getId().isEmpty()) {
                Node parent = getApplicationNode();
                node = parent.addNode(NODE_NAME);
            } else {
                node = this.findNodeById(blog.getId());
            }
            blog.setModificationDate(new Date());
            blog.setModificationUser(currentUserService.getUserName());
            blogToNode(blog, node);
            blog.setId(node.getUUID());
            if (blog.isDefaultBlog()) {
                for (Node defaultNode : findDefaultBlogResourceNodes()) {
                    if (!defaultNode.getUUID().equals(node.getUUID())) {
                        defaultNode.setProperty(PROPERTY_BOOLEAN_DEFAULT_BLOG, false);
                    }
                }
            }
            session.getRootNode().save();
            log.info("U: #0 C: BlogManager.save E: #1", currentUserService.getUserName(), blog.getId());
        } catch (RepositoryException ex) {
            throw new ManagerException(ex);
        }
    }

    private void blogToNode(Blog blog, Node node) throws RepositoryException {
        node.addMixin("mix:referenceable");
        node.setProperty(PROPERTY_STRING_TITLE, blog.getTitle());
        node.setProperty(PROPERTY_STRING_RESOURCE_NAME, blog.getResourceName());
        node.setProperty(PROPERTY_STRING_TEMPLATE, blog.getTemplate());
        node.setProperty(PROPERTY_STRING_AUTHOR, blog.getAuthor());
        Calendar c = Calendar.getInstance();
        c.setTime(blog.getModificationDate());
        node.setProperty(PROPERTY_DATE_MODIFICATION_DATE, c);
        node.setProperty(PROPERTY_STRING_MODIFICATION_USER, blog.getModificationUser());
        node.setProperty(PROPERTY_BOOLEAN_ALLOW_COMMENTS, blog.getAllowComments());
        node.setProperty(PROPERTY_BOOLEAN_DEFAULT_BLOG, blog.isDefaultBlog());
    }

    private Blog nodeToBlog(Node node) throws RepositoryException {
        Blog b = new Blog();
        b.setId(node.getUUID());
        if (node.hasProperty(PROPERTY_STRING_TITLE)) {
            b.setTitle(node.getProperty(PROPERTY_STRING_TITLE).getString());
        }
        if (node.hasProperty(PROPERTY_STRING_RESOURCE_NAME)) {
            b.setResourceName(node.getProperty(PROPERTY_STRING_RESOURCE_NAME).getString());
        }
        if (node.hasProperty(PROPERTY_STRING_TEMPLATE)) {
            b.setTemplate(node.getProperty(PROPERTY_STRING_TEMPLATE).getString());
        }
        if (node.hasProperty(PROPERTY_STRING_AUTHOR)) {
            b.setAuthor(node.getProperty(PROPERTY_STRING_AUTHOR).getString());
        }
        if (node.hasProperty(PROPERTY_DATE_MODIFICATION_DATE)) {
            b.setModificationDate(node.getProperty(PROPERTY_DATE_MODIFICATION_DATE).getDate().getTime());
        }
        if (node.hasProperty(PROPERTY_STRING_MODIFICATION_USER)) {
            b.setModificationUser(node.getProperty(PROPERTY_STRING_MODIFICATION_USER).getString());
        }
        if (node.hasProperty(PROPERTY_BOOLEAN_ALLOW_COMMENTS)) {
            b.setAllowComments(node.getProperty(PROPERTY_BOOLEAN_ALLOW_COMMENTS).getBoolean());
        }
        if (node.hasProperty(PROPERTY_BOOLEAN_DEFAULT_BLOG)) {
            b.setDefaultBlog(node.getProperty(PROPERTY_BOOLEAN_DEFAULT_BLOG).getBoolean());
        }
        return b;
    }

    public String findDefaultBlogResourceName() throws ManagerException {
        try {
            Query query = session.getWorkspace().getQueryManager().createQuery("//" + NODE_NAME + "[@" + PROPERTY_BOOLEAN_DEFAULT_BLOG + "=\"true\"]/@" + PROPERTY_STRING_RESOURCE_NAME, Query.XPATH);
            RowIterator rows = query.execute().getRows();
            if (rows.hasNext()) {
                return rows.nextRow().getValue(PROPERTY_STRING_RESOURCE_NAME).getString();
            }
            return null;
        } catch (RepositoryException ex) {
            throw new ManagerException(ex);
        }
    }

    /**
     * finds all jcr nodes with property defaultBlog set to true
     * This should match 1 node maximum. but really finding ALL
     * nodes gives extra robustness.
     * 
     * @return
     * @throws RepositoryException
     */
    private List<Node> findDefaultBlogResourceNodes() throws RepositoryException {
        List<Node> result = new ArrayList<Node>(1);
        Query query = session.getWorkspace().getQueryManager().createQuery("//" + NODE_NAME + "[@" + PROPERTY_BOOLEAN_DEFAULT_BLOG + "=\"true\"]", Query.XPATH);
        NodeIterator nodes = query.execute().getNodes();
        while (nodes.hasNext()) {
            result.add(nodes.nextNode());
        }
        return result;
    }
}
