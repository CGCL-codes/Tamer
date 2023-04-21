package org.ascape.examples.boids.view;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static final void main(String[] args) {
        int shortest = Integer.MAX_VALUE;
        int cycles = 0;
        while (shortest > 17) {
            cycles++;
            List near = new ArrayList();
            List far = new ArrayList();
            List order = new ArrayList();
            near.add(new Integer(10));
            near.add(new Integer(5));
            near.add(new Integer(2));
            near.add(new Integer(1));
            int tripLength = 0;
            for (int i = 0; i < 3; i++) {
                Integer first = (Integer) near.remove((int) (Math.random() * near.size()));
                Integer second = (Integer) near.remove((int) (Math.random() * near.size()));
                tripLength += Math.max(first.intValue(), second.intValue());
                far.add(first);
                far.add(second);
                order.add(first);
                order.add(second);
                if (i < 2) {
                    Integer back = (Integer) far.remove((int) (Math.random() * far.size()));
                    tripLength += back.intValue();
                    far.remove(back);
                    near.add(back);
                    order.add(back);
                }
            }
            if (tripLength < shortest) {
                shortest = tripLength;
                System.out.println("Cycle: " + cycles);
                System.out.println(tripLength);
                System.out.println(order);
            }
        }
    }
}
