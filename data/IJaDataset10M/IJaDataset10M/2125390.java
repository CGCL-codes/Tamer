package net.nothinginteresting.datamappers2.query;

import java.util.ArrayList;
import java.util.List;
import net.nothinginteresting.datamappers2.DomainObject;

/**
 * @author Dmitri Gorbenko
 * 
 */
public class Condition<T extends DomainObject> {

    public enum Type {

        AND, OR, CRITERIA, NOT
    }

    private final List<Criteria<T>> criterias;

    private final Type type;

    public Condition(Type type, List<Criteria<T>> criterias) {
        this.type = type;
        this.criterias = criterias;
    }

    public static <T extends DomainObject> Condition<T> and(Criteria<T> criteria1, Criteria<T> criteria2) {
        List<Criteria<T>> criterias = new ArrayList<Criteria<T>>();
        criterias.add(criteria1);
        criterias.add(criteria2);
        return new Condition<T>(Type.AND, criterias);
    }

    public static <T extends DomainObject> Condition<T> and(Criteria<T> criteria1, Criteria<T> criteria2, Criteria<T> criteria3) {
        List<Criteria<T>> criterias = new ArrayList<Criteria<T>>();
        criterias.add(criteria1);
        criterias.add(criteria2);
        criterias.add(criteria3);
        return new Condition<T>(Type.AND, criterias);
    }

    public static <T extends DomainObject> Condition<T> criteria(Criteria<T> criteria) {
        List<Criteria<T>> criterias = new ArrayList<Criteria<T>>();
        criterias.add(criteria);
        return new Condition<T>(Type.CRITERIA, criterias);
    }

    public List<Criteria<T>> getCriterias() {
        return criterias;
    }

    public Type getType() {
        return type;
    }
}
