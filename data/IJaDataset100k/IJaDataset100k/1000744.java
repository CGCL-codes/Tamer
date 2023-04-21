package org.jpox.samples.abstractclasses;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Container of abstract objects with composite identity. Has the following :-
 * <ul>
 * <li>a field containing an abstract object (1-1 relation)</li>
 * <li>a second field containing an abstract object (1-1 relation)</li>
 * <li>a field containing a List of abstract objects (1-N relation)</li>
 * <li>a field containing a Set of abstract objects (1-N relation)</li>
 * </ul>
 *
 * @version $Revision: 1.1 $ 
 */
public class AbstractCompositeClassHolder {

    private int id;

    protected AbstractCompositeBase abstract1;

    protected AbstractCompositeBase abstract2;

    protected List abstractList1 = new ArrayList();

    protected Set abstractSet1 = new HashSet();

    public AbstractCompositeClassHolder(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAbstract1(AbstractCompositeBase abs) {
        abstract1 = abs;
    }

    public AbstractCompositeBase getAbstract1() {
        return abstract1;
    }

    public void setAbstract2(AbstractCompositeBase abs) {
        abstract2 = abs;
    }

    public AbstractCompositeBase getAbstract2() {
        return abstract2;
    }

    public List getAbstractList1() {
        return abstractList1;
    }

    public Set getAbstractSet1() {
        return abstractSet1;
    }
}
