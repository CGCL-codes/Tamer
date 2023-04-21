package com.puppycrawl.tools.checkstyle.coding;

public class InputDeclareVariableInItsBloc {

    private methodTest1() {
        int i = 3;
        if (System.currentTimeMillis() > 0) {
            i = 56;
            System.err.println(i);
        }
    }

    private methodTest2() {
        int i = 3;
        if (System.currentTimeMillis() > 0) {
            i = 56;
            System.err.println(i);
        }
        int j = 9;
        System.err.println(j);
        if (System.currentTimeMillis() > 0) {
            i = 51;
            System.err.println(i);
        }
    }
}
