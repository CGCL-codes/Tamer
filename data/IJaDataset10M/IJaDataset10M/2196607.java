package uk.ac.warwick.dcs.cokefolk.server.backend.sql;

import java.sql.SQLException;
import java.util.*;
import uk.ac.warwick.dcs.cokefolk.NotImplementedException;
import uk.ac.warwick.dcs.cokefolk.server.backend.DatabaseException;
import uk.ac.warwick.dcs.cokefolk.server.operations.types.*;
import uk.ac.warwick.dcs.cokefolk.server.typechecker.DKindTuple;

/**
 * SQLTupleSet
 * This class represents a tuple set that's stored in the database.
 * It differs from a LiteralTupleSet in that the values are not preloaded into the object, but instead
 * are retrieved from the database as required. This improves performance by removing the need
 * to load the entire database into the Environment when the server starts.
 * @author  Tom
 */
public class SQLTupleSet extends TupleSet {

    private SQLPersistence pp;

    private String relationName;

    private int size;

    private DKindTuple signature;

    /**
	 * Constructor.
	 * @param relationName The name of the relation the TupleSet represent.
	 * @param postgresPersistence The instance of SQLPersistence to use to connect to the database.
	 * @throws DatabaseException
	 */
    public SQLTupleSet(String relationName, SQLPersistence postgresPersistence) throws DatabaseException {
        this.pp = postgresPersistence;
        this.relationName = relationName;
        init();
    }

    private void init() throws DatabaseException {
        try {
            this.size = pp.getRelvarSize(relationName);
            this.signature = pp.getRelvarSignature(relationName);
        } catch (UnboundVariableException uve) {
            System.out.println(uve.toString());
        } catch (TypeMismatchException tme) {
            System.out.println(tme.toString());
        }
    }

    /**
	 * Returns a RelationIterator over the SQLTupleSet.
	 * @return A RelationIterator over the SQLTupleSet.
	 */
    @Override
    public RelationIterator iterator() {
        try {
            return new SQLTupleIterator(relationName, pp.fetchRelvarResultSet(relationName), this.pp, this);
        } catch (DatabaseException e) {
            return null;
        }
    }

    /**
	 * Returns the size of the SQLTupleSet.
	 * @return The size of the SQLTupleSet.
	 */
    @Override
    public int size() {
        return this.size;
    }

    /**
	 * Returns the signature of the SQLTupleSet.
	 * @return  Returns the signature.
	 * @uml.property  name="signature"
	 */
    @Override
    public DKindTuple getSignature() {
        return this.signature;
    }

    /**
	 * Returns true.
	 * @return true
	 */
    @Override
    public boolean isPersistent() {
        return true;
    }

    /**
	 * Closes the SQLTupleSet.
	 */
    @Override
    public void close() {
        throw new NotImplementedException();
    }

    /**
	 * Removes a collection of DTuples from the SQLTupleSet.
	 * @param collection The collection of DTuples to remove.
	 * @return true - if the removal was successful, false - otherwise
	 */
    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new NotImplementedException();
    }

    /**
	 * Adds a DTuple to the SQLTupleSet.
	 * @param tuple The DTuple to add.
	 * @return true - if the addition was successful, false - otherwise.
	 */
    @Override
    public boolean add(DTuple tuple) {
        return addAll(Collections.singleton(tuple));
    }

    /**
	 * Add a collection of DTuples to the SQLTupleSet.
	 * @param collection The collection of DTuples to add the SQLTupleSet.
	 * @return true - if the addition was successful, false - otherwise.
	 */
    @Override
    public boolean addAll(Collection<? extends DTuple> collection) {
        throw new NotImplementedException();
    }

    /**
	 * Clears the SQLTupleSet.
	 */
    @Override
    public void clear() {
        throw new NotImplementedException();
    }

    /**
	 * Determines if the SQLTupleSet contains the given object.
	 * @param obj The object to look for.
	 * @return true - if the SQLTupleSet contains obj, false - otherwise.
	 */
    @Override
    public boolean contains(Object obj) {
        if (obj instanceof DTuple) {
            return containsAll(Collections.singleton((DTuple) obj));
        } else {
            return false;
        }
    }

    /**
	 * Determins if the SQLTupleSet contains all of the objects in collection.
	 * @param collection The collection of objects to look for.
	 * @return true - if the SQLTupleSet contains all objects in collection, false - otherwise.
	 */
    @Override
    public boolean containsAll(Collection<?> collection) {
        boolean allContained = true;
        try {
            for (Iterator it = collection.iterator(); it.hasNext(); ) {
                DTuple tup = (DTuple) it.next();
                if (!pp.relvarContains(relationName, tup)) {
                    allContained = false;
                    break;
                }
            }
        } catch (DatabaseException dbe) {
            throw new IllegalStateException(dbe.toString());
        }
        return allContained;
    }

    @Override
    public boolean equate(Object obj, boolean using) {
        if (!using) {
            return this.equals(obj);
        } else {
            throw new NotImplementedException();
        }
    }

    @Override
    public List<String> usingAttribs() {
        return Collections.emptyList();
    }

    /**
	 * Determines if the SQLTupleSet is emtpy.
	 * @return true - if the SQLTupleSet is empty, false - otherwise.
	 */
    @Override
    public boolean isEmpty() {
        if (this.size == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Removes the object from the SQLTupleSet.
	 * @param obj The object to remove from the SQLTupleSet.
	 * @return true - if the removal was successful, false - otherwise.
	 */
    @Override
    public boolean remove(Object obj) {
        if (obj instanceof DTuple) {
            return removeAll(Collections.singleton((DTuple) obj));
        } else {
            return false;
        }
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new NotImplementedException();
    }

    /**
	 * Returns the tuple comparator.
	 * @return The comparator
	 */
    public Comparator<DTuple> comparator() {
        return new TupleComparator.JoinedComparator(new TupleComparator(signature));
    }

    /**
	 * Clones the SQLTupleSet.
	 * @return A LiteralTupleSet representing the values of the SQLTupleSet. Note, an SQLTupleSet is not returned
	 * because this would not make sense in the context of the system, since SQLTupleSet is used to represent
	 * a handle on a stored relvar, and cloning the SQLTupleSet instance does not store another relation in the database.
	 */
    @Override
    public LiteralTupleSet clone() throws CloneNotSupportedException {
        try {
            return new LiteralTupleSet(pp.fetchRelvar(relationName));
        } catch (DatabaseException dbe) {
            return null;
        } catch (TypeMismatchException e) {
            throw new CloneNotSupportedException();
        }
    }
}

/**
 * An iterator over the SQLTupleSet.
 * @author  Tom
 */
class SQLTupleIterator extends RelationIterator {

    DKindTuple signature;

    String relationName;

    java.sql.ResultSet rs;

    SQLPersistence pp;

    /**
	 * Constructor.
	 * @param relationName The name of the relation to iterate over.
	 * @param rs The ResultSet representing the relation.
	 * @param pp The instance of SQLPersistence to use to connect to the database.
	 * @param owner The owning SQLTupleSet.
	 */
    public SQLTupleIterator(String relationName, java.sql.ResultSet rs, SQLPersistence pp, SQLTupleSet owner) {
        this.signature = owner.getSignature();
        this.relationName = relationName;
        this.rs = rs;
        this.pp = pp;
        this.owner = owner;
    }

    /**
	 * Determines if the iterator has another DTuple left to return.
	 * @return true - if the iterator has another DTuple left to return, false - otherwise.
	 * @throws IllegalStateException
	 */
    public boolean hasNext() throws IllegalStateException {
        checkState();
        boolean next = false;
        try {
            next = rs.next();
            rs.previous();
            return next;
        } catch (SQLException e) {
            owner.fail(new DatabaseException(e));
            throw new IllegalStateException(e.getLocalizedMessage());
        }
    }

    /**
	 * Returns the next DTuple.
	 * @return The next DTuple.
	 */
    public DTuple next() {
        checkState();
        DTuple tup = null;
        try {
            rs.next();
            tup = pp.resultSetToDTuple(rs);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } catch (DatabaseException e) {
            throw new IllegalStateException(e);
        }
        return tup;
    }

    /**
	 * 
	 */
    public void remove() {
        throw new NotImplementedException();
    }

    /**
	 * 
	 */
    @Override
    public void update(Map<String, DType> attribAssignments) {
        throw new NotImplementedException();
    }
}
