package org.helianto.core;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Transient;
import org.helianto.core.def.UserType;

/**
 * The root user account.
 * 
 * @author Mauricio Fernandes de Castro
 * 			
 */
@javax.persistence.Entity
@DiscriminatorValue("R")
public class RootUser extends User {

    /**
	 * <<Transient>> Exposes the discriminator.
	 */
    @Transient
    public char getDiscriminator() {
        return 'R';
    }

    private static final long serialVersionUID = 1L;

    /** 
	 * Empty constructor.
	 */
    public RootUser() {
        super();
        setUserTypeAsEnum(UserType.ADMINISTRATOR);
    }

    /** 
	 * Key constructor.
	 * 
	 * @param entity
	 * @param credential
	 */
    public RootUser(Entity entity, Identity identity) {
        this();
        setEntity(entity);
        setIdentity(identity);
    }

    /** 
	 * Credential constructor.
	 * 
	 * <p>
	 * The credential is not used after its principal is read,
	 * although is here to force previous creation.
	 * </p>
	 * 
	 * @param entity
	 * @param credential
	 */
    public RootUser(Entity entity, Credential credential) {
        this(entity, credential.getIdentity());
    }

    /** 
	 * Parent constructor.
	 * 
	 * @param parent
	 * @param childCredential
	 */
    public RootUser(UserGroup parent, Credential childCredential) {
        this(parent.getEntity(), childCredential);
        parent.getChildAssociations().add(new UserAssociation(parent, childCredential));
    }
}
