package test.projects.InlineTemp.canInline;

class A {

    private int ONE = 1;

    void f() {
        ONE = 2;
        int two = ONE;
    }
}
