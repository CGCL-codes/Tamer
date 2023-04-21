package com.bigfatgun.fixjures.dao;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.List;
import java.util.Set;

final class MyBusinessObjectDAOImpl extends AbstractDAO<MyBusinessObject> implements MyBusinessObjectDAO {

    private static final Function<MyBusinessObject, Long> EXTRACT_ACCOUNT_BALANCE = new Function<MyBusinessObject, Long>() {

        @Override
        public Long apply(MyBusinessObject myBusinessObject) {
            return myBusinessObject.getAccountBalance();
        }
    };

    private static final Function<MyBusinessObject, MyBusinessObject> EXTRACT_PARENT = new Function<MyBusinessObject, MyBusinessObject>() {

        @Override
        public MyBusinessObject apply(MyBusinessObject myBusinessObject) {
            return myBusinessObject.getParent();
        }
    };

    private static final Function<MyBusinessObject, String> ID_FUNCTION = new Function<MyBusinessObject, String>() {

        @Override
        public String apply(MyBusinessObject myBusinessObject) {
            return myBusinessObject.getId();
        }
    };

    private static final Predicate<Long> POSITIVE = new Predicate<Long>() {

        @Override
        public boolean apply(Long aLong) {
            return aLong.compareTo(0L) > 0;
        }
    };

    private static final Predicate<MyBusinessObject> POSITIVE_ACCOUNT_BALANCE = Predicates.compose(POSITIVE, EXTRACT_ACCOUNT_BALANCE);

    private static final Ordering<MyBusinessObject> ASCENDING_ID = Ordering.natural().onResultOf(ID_FUNCTION);

    private static final Ordering<MyBusinessObject> ASCENDING_ACCOUNT_BALANCE = Ordering.natural().onResultOf(EXTRACT_ACCOUNT_BALANCE);

    private static final Ordering<Object> ASCENDING_HASH = Ordering.natural().onResultOf(new Function<Object, Comparable>() {

        @Override
        public Comparable apply(Object o) {
            return o.hashCode();
        }
    });

    public MyBusinessObjectDAOImpl(final DAOHelper<MyBusinessObject> helper) {
        super(helper, ID_FUNCTION);
    }

    @Override
    public MyBusinessObject find(String id) {
        return doSelect(id);
    }

    @Override
    public List<MyBusinessObject> findAll() {
        return Lists.newArrayList(getHelper().findAll());
    }

    @Override
    public List<MyBusinessObject> findByAccountBalanceGreaterThan(final long minimumBalance) {
        return Lists.newArrayList(getHelper().findAllWhere(new Predicate<MyBusinessObject>() {

            @Override
            public boolean apply(MyBusinessObject myBusinessObject) {
                return myBusinessObject.getAccountBalance().compareTo(minimumBalance) >= 0;
            }
        }));
    }

    @Override
    public int countByAccountBalanceGreaterThan(long minimumBalance) {
        return findByAccountBalanceGreaterThan(minimumBalance).size();
    }

    @Override
    public List<MyBusinessObject> findAllOrderedByAccountBalance() {
        return getHelper().findAllOrdered(ASCENDING_ACCOUNT_BALANCE);
    }

    @Override
    public List<MyBusinessObject> findByPositiveAccountBalanceOrderedByIdDescending() {
        return getHelper().findAllOrderedWhere(ASCENDING_ID.reverse(), POSITIVE_ACCOUNT_BALANCE);
    }

    @Override
    public List<MyBusinessObject> findByNegativeAccountBalanceOrderedByIdDescending() {
        return getHelper().findAllOrderedWhere(ASCENDING_ID.reverse(), Predicates.not(POSITIVE_ACCOUNT_BALANCE));
    }

    @Override
    public void delete(MyBusinessObject obj) {
        doDelete(obj);
    }

    @Override
    public void insert(MyBusinessObject obj) {
        MyBusinessObject prior = doInsert(obj);
        if (prior != null) {
            System.err.format("WARNING: Insert of new object overwrote prior value: %s%n", prior);
        }
    }

    @Override
    public void update(MyBusinessObject obj) {
        doUpdate(obj);
    }

    @Override
    public List<MyBusinessObject> findChildren(final MyBusinessObject parent) {
        return Lists.newArrayList(getHelper().findAllWhere(Predicates.compose(Predicates.equalTo(parent), EXTRACT_PARENT)));
    }

    List<MyBusinessObject> findAllOrderByHashCodeForFun() {
        return getHelper().findAllOrdered(ASCENDING_HASH);
    }

    List<MyBusinessObject> findPaged(int pageSize, int pageNumber) {
        return getHelper().findAllOrderedWhere(ASCENDING_ACCOUNT_BALANCE, DAOPredicates.page(pageSize, pageNumber), false);
    }

    MyBusinessObject createUnsavedDummy(final String id, final Long accountBalance) {
        return new MyBusinessObject() {

            @Override
            public String getId() {
                return id;
            }

            @Override
            public Long getAccountBalance() {
                return accountBalance;
            }

            @Override
            public MyBusinessObject getParent() {
                return null;
            }

            @Override
            public Collection<String> getSomeStrings() {
                return null;
            }

            @Override
            public Set<Integer> getUniqueNumbers() {
                return null;
            }
        };
    }
}
