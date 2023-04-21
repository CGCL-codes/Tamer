package org.openliberty.arisid;

import javax.security.auth.Subject;
import org.openliberty.arisid.stack.ConnectionException;
import org.openliberty.arisid.stack.DeclarationException;
import org.openliberty.arisid.stack.IPrincipalIdentifier;
import org.openliberty.arisid.stack.MappingException;
import org.openliberty.arisid.stack.NoSuchContextException;
import org.openliberty.arisid.stack.NoSuchSubjectException;
import org.openliberty.arisid.stack.PolicyException;
import org.openliberty.arisid.stack.SchemaException;
import org.openliberty.arisid.stack.SubjectNotUniqueException;

/**
 * An interaction used for modify an identity accessed via an ArisId Service.
 */
public interface IModifyInteraction extends IInteraction {

    public boolean isModify();

    /**
	 * The modify method allows attributes of a Subject to be modified within
	 * the attribute service.
	 * 
	 * @param subjectIdentifierKey
	 *            The subject to be modified.
	 * @param modVals
	 *            modVals an array of {@link IAttributeValue} to be applied to
	 *            the subject. Note: Dynamic policy constraints may also be
	 *            specified by adding them to specific IAttributeValue values.
	 * @param roleVals
	 *            The roles (of the declared roles) that should be set. If a
	 *            declared value is not present, that is treated as a clear. A
	 *            null value means no changes for roles are to be made.
	 * @param user
	 *            The user context under which the modify is to be performed or
	 *            null if the transaction is to be done using the application
	 *            credential context alone.
	 * @throws ConnectionException
	 *             Occurs when there was a connection error trying to connect to
	 *             the appropriate attribute authority.
	 * @throws PolicyException
	 *             Occurs when the modify is refused due to policy restrictions
	 * @throws NoSuchContextException
	 *             Occurs when the Subject cannot be mapped to an appropriate
	 *             attribute authority context.
	 * @throws NoSuchSubjectException
	 *             Occurs when no subject could be located for the modify
	 *             request.
	 * @throws SubjectNotUniqueException
	 *             Is thrown when the subject index maps to more than 1 logical
	 *             subject.
	 * @throws SchemaException
	 *             Is thrown when the attributes being modified cannot be mapped
	 *             to the schema within the attribute service. This may or may
	 *             not indicate a partial modify was completed. Exception should
	 *             indicate details.
	 * @throws MappingException
	 *             Is thrown when an error has occurred mapping attribute values
	 *             to the attribute authority schema.
	 * @throws DeclarationException
	 *             The Interaction was not declared as an Modify Interaction.
	 */
    public void doModify(IPrincipalIdentifier subjectIdentifierKey, IAttributeValue[] modVals, String[] roleVals, Subject user) throws ConnectionException, PolicyException, NoSuchContextException, NoSuchSubjectException, SubjectNotUniqueException, SchemaException, MappingException, DeclarationException;
}
