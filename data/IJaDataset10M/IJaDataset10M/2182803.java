package com.fakeco.fakeproduct;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import com.fakeco.fakeproduct.id.FakeId;

public class FakeProduct {

    private FakeId id;

    public FakeProduct() {
        id = new FakeId();
    }

    public int getSeven() {
        return 7;
    }

    public void createInner() {
        StaticInnerClass clazz = new StaticInnerClass();
        clazz.actionPerformed(null);
    }

    public static void main(String[] args) {
        List list;
        System.out.println("Hello World");
    }

    public class TestNothing {

        public TestNothing() {
        }

        public void testThisTestIsNotAUnitTestAndItShouldntBeRun() {
            throw new IllegalStateException();
        }
    }

    private static class StaticInnerClass extends AbstractAction {

        private StaticInnerClass() {
        }

        public void actionPerformed(ActionEvent e) {
            System.out.println("Hello World");
        }
    }
}
