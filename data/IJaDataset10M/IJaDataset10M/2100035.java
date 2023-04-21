package furniture.core;

import java.util.ArrayList;
import java.util.List;

public class SpecificTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Bean b = new Bean("xx", 10);
        Bean b1 = new Bean("xx1", 20);
        Bean b2 = new Bean("xx2", 50);
        Bean b3 = new Bean("xx3", 30);
        Bean b4 = new Bean("xx4", 110);
        List<Bean> list = new ArrayList<Bean>();
        list.add(b);
        list.add(b);
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
        Query q = new BizQuery(b);
        Query q1 = new BizQuery(b1);
        for (Bean bean : list) {
            if (q.and(q1).findBy(bean)) {
                System.out.println(bean);
            }
        }
    }
}

class Bean {

    String name;

    int age;

    public Bean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Bean [name=" + name + ", age=" + age + "]";
    }
}

class BizQuery extends AbstractQuery {

    Bean bean;

    public BizQuery(Bean bean) {
        this.bean = bean;
    }

    @Override
    public boolean findBy(Object candidate) {
        Bean b = (Bean) candidate;
        return b.age <= bean.age;
    }
}

interface Query {

    boolean findBy(Object candidate);

    Query and(Query query);

    Query or(Query query);

    Query not();
}

abstract class AbstractQuery implements Query {

    public abstract boolean findBy(Object candidate);

    @Override
    public Query and(Query query) {
        return new AndQuery(this, query);
    }

    @Override
    public Query or(Query query) {
        return new OrQuery(this, query);
    }

    @Override
    public Query not() {
        return new NotQuery(this);
    }
}

class AndQuery extends AbstractQuery {

    Query left;

    Query right;

    public AndQuery(Query left, Query right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean findBy(Object candidate) {
        return left.findBy(candidate) && right.findBy(candidate);
    }
}

class OrQuery extends AbstractQuery {

    Query left;

    Query right;

    public OrQuery(Query left, Query right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean findBy(Object candidate) {
        return left.findBy(candidate) || right.findBy(candidate);
    }
}

class NotQuery extends AbstractQuery {

    Query left;

    public NotQuery(Query left) {
        this.left = left;
    }

    @Override
    public boolean findBy(Object candidate) {
        return !left.findBy(candidate);
    }
}
