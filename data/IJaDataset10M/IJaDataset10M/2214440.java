package ctest.list;

import java.util.Collections;
import java.util.List;
import ctest.ConcurrencyTest;
import ctest.Result;
import ctest.Worker;

public class ListTest extends ConcurrencyTest {

    List list = null;

    protected Worker getWorker(int threadNum, Result result) {
        return new ListTestWorker(threadNum, result, list);
    }

    public void setList(String str) throws Exception {
        String className = "java.util." + str;
        this.list = (List) Class.forName(className).newInstance();
    }

    public static void main(String[] args) throws Exception {
        ListTest tester = new ListTest();
        tester.setList(args[0]);
        tester.test(Integer.parseInt(args[1]));
    }
}
