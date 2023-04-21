package org.torweg.pulse.component.blog.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.torweg.pulse.util.entity.AbstractNamableEntity;

/**
 * 
 * @author Thomas Weber, Daniel Dietz
 * @version $Revision$
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "blog_id" }) })
@XmlRootElement(name = "blog-tag")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.NONE)
public class BlogCategory extends AbstractNamableEntity {

    /**
	 * serialVersionUID.
	 */
    private static final long serialVersionUID = 9006828901502872968L;

    /**
	 * the blog the category belongs to.
	 */
    @ManyToOne(optional = false)
    private BlogContent blog;

    /**
	 * JAXB and Hibernate.
	 */
    @Deprecated
    protected BlogCategory() {
        super();
    }

    /**
	 * builds a new {@code BlogCategory} for the given name and {@code BlogContent}.
	 * 
	 * @param catName
	 *            the name of the category
	 * @param blogContent
	 *            the blog of the category
	 * @throws IllegalArgumentException
	 *             if either {@code blog} or {@code category} is {@code null}
	 */
    public BlogCategory(final String catName, final BlogContent blogContent) throws IllegalArgumentException {
        super();
        if (blogContent == null) {
            throw new IllegalArgumentException("The BlogContent must not be null.");
        }
        if (catName == null) {
            throw new IllegalArgumentException("The name must not be null.");
        }
        setName(catName);
        this.blog = blogContent;
    }

    /**
	 * returns the blog id for JAXB output.
	 * 
	 * @return the blog id for JAXB output
	 */
    @XmlAttribute(name = "blog-id")
    protected final Long getBlogIdJAXB() {
        return this.blog.getId();
    }

    /**
	 * returns the {@code BlogContent} the {@code BlogCategory} belongs to.
	 * 
	 * @return the {@code BlogContent}
	 */
    public final BlogContent getBlog() {
        return this.blog;
    }
}
