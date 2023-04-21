package miscellaneous;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestHowAComparatorWorks {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        MyDummyType dummy1 = new MyDummyType();
        Class.forName("MyDummyType").newInstance();
        dummy1.setImplicationTypeId(new Long(1));
        List list = new ArrayList();
        Comparator newComparator = new Comparator() {

            public int compare(Object thisObj, Object otherObj) {
                if (thisObj == null || otherObj == null) return 0;
                MyDummyType thisType = (MyDummyType) thisObj;
                MyDummyType otherType = (MyDummyType) otherObj;
                Long thisTypeId = thisType.getImplicationTypeId();
                Long otherTypeId = otherType.getImplicationTypeId();
                if (thisTypeId != null && otherTypeId != null) return thisTypeId.compareTo(otherTypeId);
                if (thisTypeId == null && otherTypeId != null) return 1;
                if (thisTypeId != null && otherTypeId == null) return -1;
                return 0;
            }
        };
        Collections.sort(list, newComparator);
    }
}

class MyDummyType {

    Long implicationTypeId;

    public Long getImplicationTypeId() {
        return implicationTypeId;
    }

    public void setImplicationTypeId(Long implicationTypeId) {
        this.implicationTypeId = implicationTypeId;
    }
}
