package net.sf.webwarp.modules.permission;

import java.util.List;
import java.util.Set;

/**
 * This permission provider interface extends the default interface. It provides methods to access the whole
 * permissioning system only with named ids. This allows for communicating with the permissioning system without knowing
 * the exact ids of the objects referenced.
 * 
 * @version $Id: ExtendedPermissionProvider.java,v 1.4.2.2 2005/08/22 08:01:27 mos Exp $
 * @author atr
 */
public interface PermissionProvider {

    /**
     * Creates a new subject group with the given name. Note that you also must define ObjectSubject rules to see the
     * subject groups in the GUI system.
     * 
     * @param name
     *            The new subject group name
     */
    public SubjectType defineSubjectType(String name, String description);

    /**
     * Accesses a subject group given its name.
     * 
     * @param name
     *            The group's name
     * @return The subject group given the name or null @
     */
    public SubjectType getSubjectType(String name);

    /**
     * Removes a subject group. Note: You can only remove subject groups when no subjects were related to it.
     * 
     * @param name
     *            The subject group name
     * @return The subject group removed or null. @
     */
    public SubjectType deleteSubjectType(String name);

    /**
     * Get all type ids for subjects.
     * 
     * @return all subject type IDs
     */
    public List<SubjectType> getSubjectTypes();

    /**
     * Creates a new object group with the given name. Note that you also must define ObjectPermission and ObjectSubject
     * rules to assign any rights in the GUI system.
     * 
     * @param name
     *            The new object group name
     */
    public ObjectType defineObjectType(String name, String description);

    /**
     * Removes a object group. Note: You can only remove object groups when no objects were related to it.
     * 
     * @param name
     *            The object group name
     * @return The object group removed or null. @
     */
    public ObjectType deleteObjectType(String name);

    /**
     * Accesses a object group given its name.
     * 
     * @param name
     *            The group's name
     * @return The object group given the name or null @
     */
    public ObjectType getObjectType(String name);

    /**
     * Get all type ids for objects.
     * 
     * @return all object type IDs
     */
    public List<ObjectType> getObjectTypes();

    /**
     * Defines a new access right. Note that the right is totally uninterpreted by the permissioning system. In fact the
     * right is not much more than a named flag for a concrete assigned permission. Its up to the defining system how
     * the right is actually to be interpreted. Note also: you also must define ObjectPermission rules to define what
     * rights are assignable to an object group.
     * 
     * @param name
     *            The new right's name (also id)
     */
    public PermissionRight defineRightType(String name);

    /**
     * Get the list of all assignable right types
     * 
     * @return The list of possible rights
     */
    public List<PermissionRight> getRightTypes();

    /**
     * Delete the right given its name/id. Note: You can only delete rights when no more permissions are attached to it.
     * 
     * @param name
     * @return The right removed @
     */
    public PermissionRight deleteRightType(String name);

    /**
     * Accesses a right given its name/id.
     * 
     * @param name
     *            The right's name/id
     * @return The right instance or null @
     */
    public boolean isRightDefined(String name);

    /**
     * Adds an object/subject rule. Such a role defines what subjects (groups) can actually be assigned to objects of a
     * given type (group). It does not impose any facts on which rights actually can be assigned. This is defined by the
     * ObjectPermission rules.
     * 
     * @param objectGroup
     *            The object group
     * @param subjectGroup
     *            The subject group
     */
    public void defineObjectSubjectMapping(String objectGroup, String subjectGroup);

    /**
     * Deletes an object/subject rule. Such a role defines what subjects (groups) can actually be assigned to objects of
     * a given type (group). It does not impose any facts on which rights actually can be assigned. This is defined by
     * the ObjectPermission rules.
     * 
     * @param objectType
     *            The object type
     * @param subjectType
     *            The subject type
     */
    public void deleteObjectSubjectMapping(String objectType, String subjectType);

    /**
     * Check whether the given object type can be assigned to the given subject type.
     * 
     * @param objectType
     * @param subjectType
     * @return
     */
    public boolean isObjectSubjectMappingDefined(String objectType, String subjectType);

    /**
     * Adds an object/right rule. Such a role defines that a specific right can actually be assigned to objects of a
     * given type (group). It does not impose any facts which subjects actually can be assigned. This is defined by the
     * ObjectSubject rules.
     * 
     * @param objectType
     *            The object type
     * @param right
     *            The right name/id
     */
    public void defineObjectRightMapping(String objectType, String right);

    /**
     * Deletes an object/right rule. Such a role defines that a specific right can actually be assigned to objects of a
     * given type (group). It does not impose any facts which subjects actually can be assigned. This is defined by the
     * ObjectSubject rules.
     * 
     * @param objectType
     *            The object type
     * @param right
     *            The right name/id
     */
    public void deleteObjectRightMapping(String objectType, String right);

    /**
     * Check whether the given right can be assigned to the given object type.
     * 
     * @param objectType
     * @param right
     * @return
     */
    public boolean isObjectRightMappingDefined(String objectType, String right);

    /**
     * Insert a new system ressource.
     * 
     * @param group
     *            An existing type
     * @param name
     *            The user presentable name of the resource
     * @param refId
     *            The reference id/external id. This id along with the resource groups must be unique over all secured
     *            resources.
     */
    public PermissionObject defineObject(String objectType, String name, String refId);

    /**
     * Collects all resources with the given group name.
     * 
     * @param objectType
     *            The type name
     * @return All resources that belong to the given group @
     */
    public List<PermissionObject> getObjects(String objectType);

    /**
     * Get the object given its type and ID
     * 
     * @param type
     *            The object type
     * @param refId
     *            The object ID
     * @return The object found or null
     */
    public PermissionObject getObject(String type, String refId);

    /**
     * Insert a new system subject.
     * 
     * @param subjectType
     *            An existing subject type
     * @param name
     *            The user presentable name of the subject
     * @param refId
     *            The reference id/external id. This id along with the subject groups must be unique over all system
     *            subjects.
     */
    public PermissionSubject defineSubject(String subjectType, String name, String refId);

    /**
     * Reads an system subject by its subject group and reference id.
     * 
     * @param subjectType
     *            The subject type
     * @param refId
     *            The reference id/external id. This id along with the subject groups must be unique over all secured
     *            subjects.
     * @return The subject object found or null @
     */
    public PermissionSubject getSubject(String subjectType, String refId);

    /**
     * Collects all subjects with the given group name.
     * 
     * @param subjectType
     *            The subject type
     * @return All subjects that belong to the given group @
     */
    public List<PermissionSubject> getSubjects(String subjectType);

    /**
     * Returns all rights that are assignable to the given object group(type).
     * 
     * @param groupName
     *            The object group name
     * @return The assignable rights to this group
     */
    public List<PermissionRight> getAssignableRightsForObjectType(String groupName);

    /**
     * Returns all subject types (groups) that are assignable to the given object group(type).
     * 
     * @param objectType
     *            The object type name
     * @return The assignable subject groups to this object group
     */
    public List<SubjectType> getAssignableSubjectTypesForObjectType(String objectType);

    /**
     * Returns all object types (groups) that are assignable to the given subject group(type).
     * 
     * @param subjectType
     *            The subject type name
     * @return The assignable object groups to this object group
     */
    public List<ObjectType> getAssignableObjectTypesForSubjectType(String subjectType);

    /**
     * Determines all assigned rights that a specific subject has on a specific object.
     * 
     * @param subjectType
     *            The subject type
     * @param refId
     *            The subject's external/ref ID
     * @param objectType
     *            The object type
     * @param objectId
     *            The object's external/ref ID
     * @return The assigned rights (if any) or an empty list
     */
    public List<PermissionRight> getRights(String subjectType, String refId, String objectType, String objectId);

    /**
     * Evaluates all permission that a specific subject currently has, e.g. all permissions of a user. A permission
     * encapsulates the subject, the object and the assigned right. So when a subject has different rights on an object
     * multiple permission are stored/returned.
     * 
     * @param subjectType
     *            The subject group
     * @param refId
     *            The subject's external/ref ID
     */
    public List<Permission> getSubjectPermissions(String subjectType, String refId);

    /**
     * Evaluates all permission that a specific subject currently has on a given object type, e.g. all permissions of a
     * user on report groups.<br>
     * A permission encapsulates the subject, the object and the assigned right. So when a subject has different rights
     * on an object multiple permission are stored/returned.
     * 
     * @param subjectType
     *            The subject type
     * @param refId
     *            The subject's external/ref ID
     */
    public List<Permission> getSubjectPermissions(String subjectType, String refId, String groupName);

    /**
     * Get the current rights of a subject on a certain object.
     * 
     * @param subjectType
     *            The subject type
     * @param subjectId
     *            The subject ID
     * @param objectType
     *            The object type
     * @param objectId
     *            The object ID
     * @return The rights found or an empty list otherwise
     */
    public List<PermissionRight> getSubjectPermissionRights(String subjectType, String subjectId, String objectType, String objectId);

    /**
     * Evaluates all permissions assigned to subjects of a given subject group, e.g. all permissions assigned to users.
     * 
     * @param subjectType
     *            The subject type
     * @return The list of found permissions
     */
    public List<Permission> getSubjectTypePermissions(String subjectType);

    /**
     * Evaluates all permissions that were assigned to specific object, e.g. all permissions to a given report. <br>
     * A permission encapsulates the subject, the object and the assigned right. So when a subject has different rights
     * on an object multiple permission are stored/returned.
     * 
     * @param objectType
     *            The object type
     * @param refId
     *            The object's external/ref ID
     */
    public List<Permission> getObjectPermissions(String objectType, String refId);

    /**
     * Evaluates all permissions assigned to objects of a given object group, e.g. all permissions to reports.
     * 
     * @param objectType
     *            The object type
     * @return The list of found permissions @
     */
    public List<Permission> getObjectTypePermissions(String objectType);

    /**
     * Permission check
     * 
     * @param subject
     * @param object
     * @param right
     */
    public boolean isPermittedAny(PermissionSubject subject, PermissionObject object, String[] rights);

    /**
     * Permission check
     * 
     * @param subject
     * @param object
     * @param right
     */
    public boolean isPermittedAll(PermissionSubject subject, PermissionObject object, String[] rights);

    /**
     * Grant a right to a subject on a certain object
     * 
     * @param subject
     * @param object
     * @param right
     */
    public void grantPermission(PermissionSubject subject, PermissionObject object, String[] rights);

    /**
     * Grant a right to a subject on all objects of a certain type
     * 
     * @param subject
     * @param objectType
     *            The object type
     * @param right
     */
    public void grantPermissionForObjectType(PermissionSubject subject, String objectType, String[] rights);

    /**
     * Grant rights to an object for all subjects of a certain type
     * 
     * @param object
     * @param subjectType
     *            The subject type
     * @param right
     */
    public void grantPermissionForSubjectType(PermissionObject object, String subjectType, String[] rights);

    /**
     * Revoke rights to a subject on a certain object
     * 
     * @param subject
     * @param object
     * @param right
     */
    public void revokePermission(PermissionSubject subject, PermissionObject object, String[] rights);

    /**
     * Grant rights to a subject on all objects of a certain type
     * 
     * @param subject
     * @param objectType
     *            The object type
     * @param right
     */
    public void revokePermissionForObjectType(PermissionSubject subject, String objectType, String[] rights);

    /**
     * Revoke rights to an object for all subjects of a certain type
     * 
     * @param object
     * @param subjectType
     *            The subject type
     * @param right
     */
    public void revokePermissionForSubjectType(PermissionObject object, String subjectType, String[] rights);
}
