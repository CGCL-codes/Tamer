package org.oobench.io;

import org.oobench.common.*;

public abstract class IOBenchmark extends AbstractBenchmark {

    public int getMajorNumber() {
        return 4;
    }

    public abstract void write(int count);

    public abstract void read(int count);

    public abstract void writeAndRead(int count);

    public void test(int count, String typeOfIO) {
        count = getCountWithDefault(count);
        System.out.println("*** Testing IO (" + typeOfIO + ")");
        for (int c = 0; c < 10; c++) {
            if (c == 9) {
                beginAction(1, "write", count, typeOfIO);
            }
            write(count);
            if (c == 9) {
                endAction();
                beginAction(2, "read", count, typeOfIO);
            }
            read(count);
            if (c == 9) {
                endAction();
                beginAction(3, "writeAndRead", count, typeOfIO);
            }
            writeAndRead(count);
            if (c == 9) {
                endAction();
            }
        }
    }

    public static void testIO(Class theClass, int count, String typeOfMessage, String[] args) {
        try {
            IOBenchmark bench = (IOBenchmark) theClass.newInstance();
            bench.setArguments(args);
            bench.test(count, typeOfMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
