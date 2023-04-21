package com.daffodilwoods.daffodildb.server.sql99.dql.iterator.order;

import com.daffodilwoods.daffodildb.server.sql99.dql.iterator._Iterator;
import com.daffodilwoods.daffodildb.server.sql99.dql.queryexpression.orderbyclause._ExpressionOrderValues;
import com.daffodilwoods.database.resource.DException;

/**
    Title : MemoryIndexIterator
    This class comes into picture when select list contains subQuery as selectList which  is  used
    in ORDER BY clause.It uses all methods of its SuperClass. It just only override the
    navigation methods.  Since there was  a requirement to allignment of  the underLying Iterator.
    Only  for this reason this class come into existance.
     @author Sandeep Kadiyan
     @see MemoryIndexIterator
     @since version 4.0
 */
public class MemoryIndexIteratorForSubQuery extends MemoryIndexIterator {

    public MemoryIndexIteratorForSubQuery(_Iterator iterator0, _ExpressionOrderValues order0) throws DException {
        super(iterator0, order0);
    }

    public boolean first() throws DException {
        if (super.first()) {
            allignUnderLineIterator();
            return true;
        }
        return false;
    }

    public boolean next() throws DException {
        if (super.next()) {
            allignUnderLineIterator();
            return true;
        }
        return false;
    }

    public boolean previous() throws DException {
        if (super.previous()) {
            allignUnderLineIterator();
            return true;
        }
        return false;
    }

    public boolean last() throws DException {
        if (super.last()) {
            allignUnderLineIterator();
            return true;
        }
        return false;
    }
}
