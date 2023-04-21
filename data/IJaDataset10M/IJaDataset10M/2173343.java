package org.t2framework.samples.lucy.dbflute.bsbhv;

import java.util.List;
import org.seasar.dbflute.*;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.EntityRowHandler;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.PagingBean;
import org.seasar.dbflute.cbean.PagingHandler;
import org.seasar.dbflute.cbean.PagingInvoker;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.dbflute.cbean.ResultBeanBuilder;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.jdbc.StatementConfig;
import org.t2framework.samples.lucy.dbflute.allcommon.*;
import org.t2framework.samples.lucy.dbflute.exentity.*;
import org.t2framework.samples.lucy.dbflute.bsentity.dbmeta.*;
import org.t2framework.samples.lucy.dbflute.cbean.*;

/**
 * The behavior of PERSON that the type is TABLE. <br />
 * 
 * <pre>
 * [primary-key]
 *     ID
 * 
 * [column]
 *     ID, NAME, AGE
 * 
 * [sequence]
 *     
 * 
 * [identity]
 *     ID
 * 
 * [version-no]
 *     
 * 
 * [foreign-table]
 *     
 * 
 * [referrer-table]
 *     
 * 
 * [foreign-property]
 *     
 * 
 * [referrer-property]
 * 
 * </pre>
 * 
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsPersonBhv extends org.seasar.dbflute.bhv.AbstractBehaviorWritable {

    /** @return The name on database of table. (NotNull) */
    public String getTableDbName() {
        return "PERSON";
    }

    /** @return The instance of DBMeta. (NotNull) */
    public DBMeta getDBMeta() {
        return PersonDbm.getInstance();
    }

    /** @return The instance of DBMeta as my table type. (NotNull) */
    public PersonDbm getMyDBMeta() {
        return PersonDbm.getInstance();
    }

    public Entity newEntity() {
        return newMyEntity();
    }

    public ConditionBean newConditionBean() {
        return newMyConditionBean();
    }

    public Person newMyEntity() {
        return new Person();
    }

    public PersonCB newMyConditionBean() {
        return new PersonCB();
    }

    @Override
    protected DBDef getCurrentDBDef() {
        return DBCurrent.getInstance().currentDBDef();
    }

    @Override
    protected StatementConfig getDefaultStatementConfig() {
        return DBFluteConfig.getInstance().getDefaultStatementConfig();
    }

    /**
	 * Select the count by the condition-bean. {IgnorePagingCondition}
	 * 
	 * @param cb
	 *            The condition-bean of Person. (NotNull)
	 * @return The selected count.
	 */
    public int selectCount(PersonCB cb) {
        assertCBNotNull(cb);
        return delegateSelectCount(cb);
    }

    /**
	 * Select the cursor by the condition-bean. <br /> Attention: It has a
	 * mapping cost from result set to entity.
	 * 
	 * @param cb
	 *            The condition-bean of Person. (NotNull)
	 * @param entityRowHandler
	 *            The handler of entity row of Person. (NotNull)
	 */
    public void selectCursor(PersonCB cb, EntityRowHandler<Person> entityRowHandler) {
        assertCBNotNull(cb);
        assertObjectNotNull("entityRowHandler<Person>", entityRowHandler);
        delegateSelectCursor(cb, entityRowHandler);
    }

    /**
	 * Select the entity by the condition-bean.
	 * 
	 * @param cb
	 *            The condition-bean of Person. (NotNull)
	 * @return The selected entity. (Nullalble)
	 * @exception org.seasar.dbflute.exception.EntityDuplicatedException
	 *                When the entity has been duplicated.
	 */
    public Person selectEntity(final PersonCB cb) {
        return helpSelectEntityInternally(cb, new InternalSelectEntityCallback<Person, PersonCB>() {

            public List<Person> callbackSelectList(PersonCB cb) {
                return selectList(cb);
            }
        });
    }

    /**
	 * Select the entity by the condition-bean with deleted check.
	 * 
	 * @param cb
	 *            The condition-bean of Person. (NotNull)
	 * @return The selected entity. (NotNull)
	 * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException
	 *                When the entity has already been deleted.
	 * @exception org.seasar.dbflute.exception.EntityDuplicatedException
	 *                When the entity has been duplicated.
	 */
    public Person selectEntityWithDeletedCheck(final PersonCB cb) {
        return helpSelectEntityWithDeletedCheckInternally(cb, new InternalSelectEntityWithDeletedCheckCallback<Person, PersonCB>() {

            public List<Person> callbackSelectList(PersonCB cb) {
                return selectList(cb);
            }
        });
    }

    public Person selectByPKValueWithDeletedCheck(Integer id) {
        Person entity = new Person();
        entity.setId(id);
        final PersonCB cb = newMyConditionBean();
        cb.acceptPrimaryKeyMapString(getDBMeta().extractPrimaryKeyMapString(entity));
        return selectEntityWithDeletedCheck(cb);
    }

    /**
	 * Select the list as result bean.
	 * 
	 * @param cb
	 *            The condition-bean of Person. (NotNull)
	 * @return The result bean of selected list. (NotNull)
	 */
    public ListResultBean<Person> selectList(PersonCB cb) {
        assertCBNotNull(cb);
        return new ResultBeanBuilder<Person>(getTableDbName()).buildListResultBean(cb, delegateSelectList(cb));
    }

    /**
	 * Select the page as result bean.
	 * 
	 * @param cb
	 *            The condition-bean of Person. (NotNull)
	 * @return The result bean of selected page. (NotNull)
	 */
    public PagingResultBean<Person> selectPage(final PersonCB cb) {
        assertCBNotNull(cb);
        final PagingInvoker<Person> invoker = new PagingInvoker<Person>(getTableDbName());
        final PagingHandler<Person> handler = new PagingHandler<Person>() {

            public PagingBean getPagingBean() {
                return cb;
            }

            public int count() {
                return selectCount(cb);
            }

            public List<Person> paging() {
                return selectList(cb);
            }
        };
        return invoker.invokePaging(handler);
    }

    /**
	 * Select the scalar value derived by a function. <br /> Call a function
	 * method after this method called like as follows:
	 * 
	 * <pre>
	 * personBhv.scalarSelect(Date.class).max(new ScalarQuery(PersonCB cb) {
	 *     cb.specify().columnXxxDatetime(); // the required specification of target column
	 *     cb.query().setXxxName_PrefixSearch(&quot;S&quot;); // query as you like it
	 * });
	 * </pre>
	 * 
	 * @param <RESULT>
	 *            The type of result.
	 * @param resultType
	 *            The type of result. (NotNull)
	 * @return The scalar value derived by a function. (Nullable)
	 */
    public <RESULT> SLFunction<PersonCB, RESULT> scalarSelect(Class<RESULT> resultType) {
        PersonCB cb = newMyConditionBean();
        cb.xsetupForScalarSelect();
        cb.getSqlClause().disableSelectIndex();
        return new SLFunction<PersonCB, RESULT>(cb, resultType);
    }

    /**
	 * Insert the entity.
	 * 
	 * @param person
	 *            The entity of insert target. (NotNull)
	 * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException
	 *                When the entity already exists. (Unique Constraint
	 *                Violation)
	 */
    public void insert(Person person) {
        assertEntityNotNull(person);
        delegateInsert(person);
    }

    @Override
    protected void doCreate(Entity person) {
        insert((Person) person);
    }

    /**
	 * Update the entity modified-only. {UpdateCountZeroException,
	 * ConcurrencyControl}
	 * 
	 * @param person
	 *            The entity of update target. (NotNull) {PrimaryKeyRequired,
	 *            ConcurrencyColumnRequired}
	 * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException
	 *                When the entity has already been deleted.
	 * @exception org.seasar.dbflute.exception.EntityDuplicatedException
	 *                When the entity has been duplicated.
	 * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException
	 *                When the entity already exists. (Unique Constraint
	 *                Violation)
	 */
    public void update(final Person person) {
        helpUpdateInternally(person, new InternalUpdateCallback<Person>() {

            public int callbackDelegateUpdate(Person entity) {
                return delegateUpdate(entity);
            }
        });
    }

    @Override
    protected void doModify(Entity entity) {
        update((Person) entity);
    }

    @Override
    protected void doModifyNonstrict(Entity entity) {
        update((Person) entity);
    }

    /**
	 * Insert or update the entity modified-only. {ConcurrencyControl(when
	 * update)}
	 * 
	 * @param person
	 *            The entity of insert or update target. (NotNull)
	 * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException
	 *                When the entity has already been deleted.
	 * @exception org.seasar.dbflute.exception.EntityDuplicatedException
	 *                When the entity has been duplicated.
	 * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException
	 *                When the entity already exists. (Unique Constraint
	 *                Violation)
	 */
    public void insertOrUpdate(final Person person) {
        helpInsertOrUpdateInternally(person, new InternalInsertOrUpdateCallback<Person, PersonCB>() {

            public void callbackInsert(Person entity) {
                insert(entity);
            }

            public void callbackUpdate(Person entity) {
                update(entity);
            }

            public PersonCB callbackNewMyConditionBean() {
                return newMyConditionBean();
            }

            public int callbackSelectCount(PersonCB cb) {
                return selectCount(cb);
            }
        });
    }

    @Override
    protected void doCreateOrUpdate(Entity person) {
        insertOrUpdate((Person) person);
    }

    @Override
    protected void doCreateOrUpdateNonstrict(Entity entity) {
        insertOrUpdate((Person) entity);
    }

    /**
	 * Delete the entity. {UpdateCountZeroException, ConcurrencyControl}
	 * 
	 * @param person
	 *            The entity of delete target. (NotNull) {PrimaryKeyRequired,
	 *            ConcurrencyColumnRequired}
	 * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException
	 *                When the entity has already been deleted.
	 * @exception org.seasar.dbflute.exception.EntityDuplicatedException
	 *                When the entity has been duplicated.
	 */
    public void delete(Person person) {
        helpDeleteInternally(person, new InternalDeleteCallback<Person>() {

            public int callbackDelegateDelete(Person entity) {
                return delegateDelete(entity);
            }
        });
    }

    @Override
    protected void doRemove(Entity person) {
        delete((Person) person);
    }

    /**
	 * Batch insert the list. This method use 'Batch Update' of
	 * java.sql.PreparedStatement.
	 * 
	 * @param personList
	 *            The list of the entity. (NotNull)
	 * @return The array of inserted count.
	 */
    public int[] batchInsert(List<Person> personList) {
        assertObjectNotNull("personList", personList);
        return delegateInsertList(personList);
    }

    /**
	 * Batch update the list. All columns are update target. {NOT modified only}
	 * <br /> This method use 'Batch Update' of java.sql.PreparedStatement.
	 * 
	 * @param personList
	 *            The list of the entity. (NotNull)
	 * @return The array of updated count.
	 * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException
	 *                When the entity has already been deleted.
	 */
    public int[] batchUpdate(List<Person> personList) {
        assertObjectNotNull("personList", personList);
        return delegateUpdateList(personList);
    }

    /**
	 * Batch delete the list. <br /> This method use 'Batch Update' of
	 * java.sql.PreparedStatement.
	 * 
	 * @param personList
	 *            The list of the entity. (NotNull)
	 * @return The array of deleted count.
	 * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException
	 *                When the entity has already been deleted.
	 */
    public int[] batchDelete(List<Person> personList) {
        assertObjectNotNull("personList", personList);
        return delegateDeleteList(personList);
    }

    /**
	 * Query update the several entities. {NoConcurrencyControl}
	 * 
	 * @param person
	 *            Entity. (NotNull) {PrimaryKeyNotRequired}
	 * @param cb
	 *            Condition-bean. (NotNull)
	 * @return The updated count.
	 */
    public int queryUpdate(Person person, PersonCB cb) {
        assertObjectNotNull("person", person);
        assertCBNotNull(cb);
        setupCommonColumnOfUpdateIfNeeds(person);
        filterEntityOfUpdate(person);
        assertEntityOfUpdate(person);
        return invoke(createQueryUpdateEntityCBCommand(person, cb));
    }

    /**
	 * Query delete the several entities. {NoConcurrencyControl}
	 * 
	 * @param cb
	 *            Condition-bean. (NotNull)
	 * @return The deleted count.
	 */
    public int queryDelete(PersonCB cb) {
        assertCBNotNull(cb);
        return invoke(createQueryDeleteCBCommand(cb));
    }

    protected int delegateSelectCount(PersonCB cb) {
        return invoke(createSelectCountCBCommand(cb));
    }

    protected void delegateSelectCursor(PersonCB cb, EntityRowHandler<Person> entityRowHandler) {
        invoke(createSelectCursorCBCommand(cb, entityRowHandler, Person.class));
    }

    protected int doCallReadCount(ConditionBean cb) {
        return delegateSelectCount((PersonCB) cb);
    }

    protected List<Person> delegateSelectList(PersonCB cb) {
        return invoke(createSelectListCBCommand(cb, Person.class));
    }

    @SuppressWarnings("unchecked")
    protected List<Entity> doCallReadList(ConditionBean cb) {
        return (List) delegateSelectList((PersonCB) cb);
    }

    protected int delegateInsert(Person e) {
        if (!processBeforeInsert(e)) {
            return 1;
        }
        return invoke(createInsertEntityCommand(e));
    }

    protected int doCallCreate(Entity entity) {
        return delegateInsert(downcast(entity));
    }

    protected int delegateUpdate(Person e) {
        if (!processBeforeUpdate(e)) {
            return 1;
        }
        return invoke(createUpdateEntityCommand(e));
    }

    protected int doCallModify(Entity entity) {
        return delegateUpdate(downcast(entity));
    }

    protected int delegateDelete(Person e) {
        if (!processBeforeDelete(e)) {
            return 1;
        }
        return invoke(createDeleteEntityCommand(e));
    }

    protected int doCallRemove(Entity entity) {
        return delegateDelete(downcast(entity));
    }

    protected int[] delegateInsertList(List<Person> ls) {
        if (ls.isEmpty()) {
            return new int[] {};
        }
        return invoke(createBatchInsertEntityCommand(helpFilterBeforeInsertInternally(ls)));
    }

    @SuppressWarnings("unchecked")
    protected int[] doCreateList(List<Entity> ls) {
        return delegateInsertList((List) ls);
    }

    protected int[] delegateUpdateList(List<Person> ls) {
        if (ls.isEmpty()) {
            return new int[] {};
        }
        return invoke(createBatchUpdateEntityCommand(helpFilterBeforeUpdateInternally(ls)));
    }

    @SuppressWarnings("unchecked")
    protected int[] doModifyList(List<Entity> ls) {
        return delegateUpdateList((List) ls);
    }

    protected int[] delegateDeleteList(List<Person> ls) {
        if (ls.isEmpty()) {
            return new int[] {};
        }
        return invoke(createBatchDeleteEntityCommand(helpFilterBeforeDeleteInternally(ls)));
    }

    @SuppressWarnings("unchecked")
    protected int[] doRemoveList(List<Entity> ls) {
        return delegateDeleteList((List) ls);
    }

    @Override
    protected boolean hasVersionNoValue(Entity entity) {
        return false;
    }

    @Override
    protected boolean hasUpdateDateValue(Entity entity) {
        return false;
    }

    protected Person downcast(Entity entity) {
        return helpDowncastInternally(entity, Person.class);
    }
}
