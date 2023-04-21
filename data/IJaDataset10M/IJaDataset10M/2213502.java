package com.league.schedule.service.persistence;

import com.league.schedule.model.Field;
import com.liferay.portal.service.persistence.BasePersistence;

/**
 * The persistence interface for the field service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Guolin Wang
 * @see FieldPersistenceImpl
 * @see FieldUtil
 * @generated
 */
public interface FieldPersistence extends BasePersistence<Field> {

    /**
	* Caches the field in the entity cache if it is enabled.
	*
	* @param field the field
	*/
    public void cacheResult(com.league.schedule.model.Field field);

    /**
	* Caches the fields in the entity cache if it is enabled.
	*
	* @param fields the fields
	*/
    public void cacheResult(java.util.List<com.league.schedule.model.Field> fields);

    /**
	* Creates a new field with the primary key. Does not add the field to the database.
	*
	* @param field_id the primary key for the new field
	* @return the new field
	*/
    public com.league.schedule.model.Field create(long field_id);

    /**
	* Removes the field with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param field_id the primary key of the field
	* @return the field that was removed
	* @throws com.league.schedule.NoSuchFieldException if a field with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
    public com.league.schedule.model.Field remove(long field_id) throws com.league.schedule.NoSuchFieldException, com.liferay.portal.kernel.exception.SystemException;

    public com.league.schedule.model.Field updateImpl(com.league.schedule.model.Field field, boolean merge) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns the field with the primary key or throws a {@link com.league.schedule.NoSuchFieldException} if it could not be found.
	*
	* @param field_id the primary key of the field
	* @return the field
	* @throws com.league.schedule.NoSuchFieldException if a field with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
    public com.league.schedule.model.Field findByPrimaryKey(long field_id) throws com.league.schedule.NoSuchFieldException, com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns the field with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param field_id the primary key of the field
	* @return the field, or <code>null</code> if a field with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
    public com.league.schedule.model.Field fetchByPrimaryKey(long field_id) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns all the fields where field_name = &#63;.
	*
	* @param field_name the field_name
	* @return the matching fields
	* @throws SystemException if a system exception occurred
	*/
    public java.util.List<com.league.schedule.model.Field> findByfield_Show(java.lang.String field_name) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns a range of all the fields where field_name = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param field_name the field_name
	* @param start the lower bound of the range of fields
	* @param end the upper bound of the range of fields (not inclusive)
	* @return the range of matching fields
	* @throws SystemException if a system exception occurred
	*/
    public java.util.List<com.league.schedule.model.Field> findByfield_Show(java.lang.String field_name, int start, int end) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns an ordered range of all the fields where field_name = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param field_name the field_name
	* @param start the lower bound of the range of fields
	* @param end the upper bound of the range of fields (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching fields
	* @throws SystemException if a system exception occurred
	*/
    public java.util.List<com.league.schedule.model.Field> findByfield_Show(java.lang.String field_name, int start, int end, com.liferay.portal.kernel.util.OrderByComparator orderByComparator) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns the first field in the ordered set where field_name = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param field_name the field_name
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching field
	* @throws com.league.schedule.NoSuchFieldException if a matching field could not be found
	* @throws SystemException if a system exception occurred
	*/
    public com.league.schedule.model.Field findByfield_Show_First(java.lang.String field_name, com.liferay.portal.kernel.util.OrderByComparator orderByComparator) throws com.league.schedule.NoSuchFieldException, com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns the last field in the ordered set where field_name = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param field_name the field_name
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching field
	* @throws com.league.schedule.NoSuchFieldException if a matching field could not be found
	* @throws SystemException if a system exception occurred
	*/
    public com.league.schedule.model.Field findByfield_Show_Last(java.lang.String field_name, com.liferay.portal.kernel.util.OrderByComparator orderByComparator) throws com.league.schedule.NoSuchFieldException, com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns the fields before and after the current field in the ordered set where field_name = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param field_id the primary key of the current field
	* @param field_name the field_name
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next field
	* @throws com.league.schedule.NoSuchFieldException if a field with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
    public com.league.schedule.model.Field[] findByfield_Show_PrevAndNext(long field_id, java.lang.String field_name, com.liferay.portal.kernel.util.OrderByComparator orderByComparator) throws com.league.schedule.NoSuchFieldException, com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns all the fields.
	*
	* @return the fields
	* @throws SystemException if a system exception occurred
	*/
    public java.util.List<com.league.schedule.model.Field> findAll() throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns a range of all the fields.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param start the lower bound of the range of fields
	* @param end the upper bound of the range of fields (not inclusive)
	* @return the range of fields
	* @throws SystemException if a system exception occurred
	*/
    public java.util.List<com.league.schedule.model.Field> findAll(int start, int end) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns an ordered range of all the fields.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param start the lower bound of the range of fields
	* @param end the upper bound of the range of fields (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of fields
	* @throws SystemException if a system exception occurred
	*/
    public java.util.List<com.league.schedule.model.Field> findAll(int start, int end, com.liferay.portal.kernel.util.OrderByComparator orderByComparator) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Removes all the fields where field_name = &#63; from the database.
	*
	* @param field_name the field_name
	* @throws SystemException if a system exception occurred
	*/
    public void removeByfield_Show(java.lang.String field_name) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Removes all the fields from the database.
	*
	* @throws SystemException if a system exception occurred
	*/
    public void removeAll() throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns the number of fields where field_name = &#63;.
	*
	* @param field_name the field_name
	* @return the number of matching fields
	* @throws SystemException if a system exception occurred
	*/
    public int countByfield_Show(java.lang.String field_name) throws com.liferay.portal.kernel.exception.SystemException;

    /**
	* Returns the number of fields.
	*
	* @return the number of fields
	* @throws SystemException if a system exception occurred
	*/
    public int countAll() throws com.liferay.portal.kernel.exception.SystemException;
}
