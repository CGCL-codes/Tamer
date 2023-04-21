package CheckPossibleAccessRights1.com.p1;

public class Class10 extends Class1 {

    /**
   * @audit MinimizeAccessViolation
   */
    void f100() {
        tmp1_4 = 1;
        tmp2_4 = 2;
        tmp3_4 = 3;
        tmp4_4 = 4;
    }
}

public class Class11 {

    /**
   * @audit MinimizeAccessViolation
   */
    void f101() {
        new Class1().tmp1_5 = 1;
        new Class1().tmp2_5 = 2;
        new Class1().tmp3_5 = 3;
        new Class1().tmp4_5 = 4;
    }
}
