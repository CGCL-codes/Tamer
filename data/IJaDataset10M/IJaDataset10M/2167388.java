package org.nexopenframework.persistence.model;

import org.nexopenframework.domain.entity.BaseEntityImpl;

/**
 * <p>NexOpen Framework</p>
 *
 * @author Francesc Xavier Magdaleno
 * @version $Revision $,$Date: 2009-07-07 21:31:44 +0100 $ 
 * @since 2.0.0.GA
 */
public class Author extends BaseEntityImpl {

    private static final long serialVersionUID = 1L;

    private String alias;

    private Person person;

    public Author() {
    }

    protected Author(final Author copy) {
        this.setId(copy.getId());
        this.setAlias(copy.getAlias());
        this.setPerson(copy.getPerson());
    }

    public String getAlias() {
        return alias;
    }

    public final void setAlias(final String alias) {
        this.alias = alias;
    }

    public Person getPerson() {
        return person;
    }

    public final void setPerson(final Person person) {
        this.person = person;
    }
}
