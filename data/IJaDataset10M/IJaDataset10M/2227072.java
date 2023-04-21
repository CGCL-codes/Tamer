package org.ujorm.implementation.xmlSpeed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.ujorm.implementation.pojo.PojoImplChild;

/**
 * A POJO Object
 * @author Pavel Ponec
 */
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlType
public class PojoTree extends PojoImplChild implements Serializable {

    /** List<PojoTree> */
    private ArrayList<PojoTree> childs = new ArrayList<PojoTree>();

    /** Creates a new instance of UnifiedDataObjectImlp */
    public PojoTree() {
    }

    public ArrayList<PojoTree> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<PojoTree> childs) {
        this.childs = childs;
    }

    public List<PojoTree> addChild(PojoTree child) {
        getChilds().add(child);
        return getChilds();
    }

    public int size() {
        int result = 0;
        ArrayList<PojoTree> childs = getChilds();
        if (childs != null) for (PojoTree tree : childs) {
            result += tree.size() + 1;
        }
        return result;
    }

    public void init(ZCounter counter, int deep) {
        Long o0 = new Long(Long.MAX_VALUE);
        Integer o1 = new Integer(1);
        String o2 = "TEST";
        Date o3 = new Date();
        Float o4 = new Float(123456.456f);
        this.setP0(o0);
        this.setP1(o1);
        this.setP2(o2);
        this.setP3(o3);
        this.setP4(o4);
        this.setP5(o0);
        this.setP6(o1);
        this.setP7(o2);
        this.setP8(o3);
        this.setP9(o4);
        for (int i = 0; i < 10; i++) {
            if (deep <= 0 || counter.substract()) {
                return;
            }
            PojoTree item = new PojoTree();
            item.init(counter, deep - 1);
            this.addChild(item);
        }
    }
}
