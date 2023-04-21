package r1_5_1.rule244;

class ForbiddenClasses extends ForbiddenClass {

    void f() {
        new Thread();
        Thread.yield();
        java.lang.Thread.yield();
        new ForbiddenClass();
        r1_5_1.rule244.ForbiddenClass.f();
        new Object();
    }
}
