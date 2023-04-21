package demo.list;

import org.jpropeller.collection.ObservableList;
import org.jpropeller.collection.impl.ObservableListDefault;
import org.jpropeller.properties.event.PropEvent;
import org.jpropeller.properties.event.PropListener;
import demo.bean.TestStringBean;

/**
 * Test operation of ListBeanShell
 * @author shingoki
 *
 */
public class ListBeanTest {

    /**
	 * Demonstrate ListBeanShell
	 * @param args
	 */
    public static void main(String[] args) {
        final ObservableList<Object> list = new ObservableListDefault<Object>();
        list.props().addListener(new PropListener() {

            @Override
            public <T> void propChanged(PropEvent<T> event) {
                System.out.println(event);
                System.out.println("modifications " + list.modifications().get());
            }
        });
        list.add("a");
        list.add(0, "b");
        TestStringBean bean = new TestStringBean();
        list.add(bean);
        bean.name().set("bean name");
        list.add(bean);
        bean.name().set("bean name2");
        list.remove(bean);
        bean.name().set("bean name3");
        list.remove(bean);
        bean.name().set("bean name4");
    }
}
