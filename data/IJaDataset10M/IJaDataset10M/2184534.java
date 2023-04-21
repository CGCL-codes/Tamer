package meditor;

import jscl.text.ParseException;
import junit.framework.Test;
import junit.framework.TestSuite;

public class SimplificationTest extends MeditorTest {

    public static Test suite() {
        return new TestSuite(SimplificationTest.class);
    }

    public void test1() throws ParseException {
        assertEquals("(sqrt(-1)*log(1-sqrt(-1)*x)-sqrt(-1)*log(1+sqrt(-1)*x))/2", simplify(expand("integral(1/(1+x^2),x)")));
    }

    public void test2() throws ParseException {
        assertEquals("x", simplify(elementary("cos(acos(x))")));
    }

    public void test3() throws ParseException {
        assertEquals("x", simplify(elementary("sin(asin(x))")));
    }

    public void test4() throws ParseException {
        assertEquals("x", simplify(elementary("tan(atan(x))")));
    }

    public void test5() throws ParseException {
        assertEquals("x", simplify(elementary("cosh(acosh(x))")));
    }

    public void test6() throws ParseException {
        assertEquals("x", simplify(elementary("sinh(asinh(x))")));
    }

    public void test7() throws ParseException {
        assertEquals("x", simplify(elementary("tanh(atanh(x))")));
    }

    public void test8() throws ParseException {
        assertEquals("-sqrt(-1)*log(2)+sqrt(-1)*log(sqrt((1-2*exp(sqrt(-1)*x)^2+exp(sqrt(-1)*x)^4)/exp(sqrt(-1)*x)^2)+3*exp(sqrt(-1)*x)+sqrt((1-2*exp(sqrt(-1)*x)^2+exp(sqrt(-1)*x)^4)/exp(sqrt(-1)*x)^2)^2*exp(sqrt(-1)*x)-exp(sqrt(-1)*x)^3)", simplify(elementary("acos(cos(x))")));
    }

    public void test9() throws ParseException {
        assertEquals("-sqrt(-1)*log(2)+sqrt(-1)*log(sqrt((1+2*exp(sqrt(-1)*x)^2+exp(sqrt(-1)*x)^4)/exp(sqrt(-1)*x)^2)-3*exp(sqrt(-1)*x)+sqrt((1+2*exp(sqrt(-1)*x)^2+exp(sqrt(-1)*x)^4)/exp(sqrt(-1)*x)^2)^2*exp(sqrt(-1)*x)-exp(sqrt(-1)*x)^3)", simplify(elementary("asin(sin(x))")));
    }

    public void test10() throws ParseException {
        assertEquals("(sqrt(-1)*log(1/exp(sqrt(-1)*x)^2))/2", simplify(elementary("atan(tan(x))")));
    }

    public void test11() throws ParseException {
        assertEquals("-log(2)+log(sqrt((1-2*exp(x)^2+exp(x)^4)/exp(x)^2)+3*exp(x)+sqrt((1-2*exp(x)^2+exp(x)^4)/exp(x)^2)^2*exp(x)-exp(x)^3)", simplify(elementary("acosh(cosh(x))")));
    }

    public void test12() throws ParseException {
        assertEquals("-log(2)+log(sqrt((1+2*exp(x)^2+exp(x)^4)/exp(x)^2)+3*exp(x)-sqrt((1+2*exp(x)^2+exp(x)^4)/exp(x)^2)^2*exp(x)+exp(x)^3)", simplify(elementary("asinh(sinh(x))")));
    }

    public void test13() throws ParseException {
        assertEquals("log(exp(x)^2)/2", simplify(elementary("atanh(tanh(x))")));
    }

    public void test14() throws ParseException {
        assertEquals("(1+exp(sqrt(-1)*x)^2)/(2*exp(sqrt(-1)*x))", simplify(elementary("cos(x)")));
    }

    public void test15() throws ParseException {
        assertEquals("(sqrt(-1)-sqrt(-1)*exp(sqrt(-1)*x)^2)/(2*exp(sqrt(-1)*x))", simplify(elementary("sin(x)")));
    }

    public void test16() throws ParseException {
        assertEquals("sqrt(-1)*log(x+sqrt(-1+x^2))", simplify(elementary("acos(x)")));
    }

    public void test17() throws ParseException {
        assertEquals("sqrt(-1)*log(-sqrt(-1)*x+sqrt(1-x^2))", simplify(elementary("asin(x)")));
    }

    public void test18() throws ParseException {
        assertEquals("(sqrt(-1)*log((sqrt(-1)+x)/(sqrt(-1)-x)))/2", simplify(elementary("atan(x)")));
    }

    public void test19() throws ParseException {
        assertEquals("log(x+sqrt(-1+x^2))", simplify(elementary("acosh(x)")));
    }

    public void test20() throws ParseException {
        assertEquals("log(x+sqrt(1+x^2))", simplify(elementary("asinh(x)")));
    }

    public void test21() throws ParseException {
        assertEquals("log((1+x)/(1-x))/2", simplify(elementary("atanh(x)")));
    }

    public void test22() throws ParseException {
        assertEquals("(sqrt(-1)-sqrt(-1)*exp(sqrt(-1)*x)^2)/(1+exp(sqrt(-1)*x)^2)", simplify(elementary("tan(x)")));
    }

    public void test23() throws ParseException {
        assertEquals("-(1-exp(x)^2)/(1+exp(x)^2)", simplify(elementary("tanh(x)")));
    }

    public void test24() throws ParseException {
        assertEquals("-1-sqrt(3)+sqrt(2)*sqrt(2+sqrt(3))", simplify("sqrt(2*sqrt(3)+4)-(1+sqrt(3))"));
    }

    public void test25() throws ParseException {
        assertEquals("log((sqrt(-1)-2*sqrt(-1)*((-exp(sqrt(-1)*x)^2)^(1/4))^2-sqrt(-1)*exp(sqrt(-1)*x)^2)/(1+exp(sqrt(-1)*x)^2))-log(sqrt(-1)+2*sqrt(exp(sqrt(-1)*x)^2/(1+2*exp(sqrt(-1)*x)^2+exp(sqrt(-1)*x)^4))-2*sqrt(-1)*sqrt(exp(sqrt(-1)*x)^2/(1+2*exp(sqrt(-1)*x)^2+exp(sqrt(-1)*x)^4))^2-2*sqrt(-1)*sqrt(exp(sqrt(-1)*x)^2/(1+2*exp(sqrt(-1)*x)^2+exp(sqrt(-1)*x)^4))^2*exp(sqrt(-1)*x)^2)", simplify(elementary("log(tan(x/2+pi/4))-asinh(tan(x))")));
    }

    public void test26() throws ParseException {
        assertEquals("(sqrt(2)-sqrt(3))/2", simplify(elementary("cos(5*pi/6)+sin(pi/4)")));
    }

    public void test27() throws ParseException {
        assertEquals("1/0", simplify("1/0"));
    }

    public void test28() throws ParseException {
        assertEquals("0/0", simplify("0/0"));
    }

    public void test29() throws ParseException {
        assertEquals("x/0", simplify("x/0"));
    }

    public void test30() throws ParseException {
        assertEquals("log(0)", simplify("log(0)"));
    }

    public void test31() throws ParseException {
        assertEquals("0", simplify("log(1)"));
    }

    public void test32() throws ParseException {
        assertEquals("sqrt(-1)*pi", simplify("log(-1)"));
    }

    public void test33() throws ParseException {
        assertEquals("log(2)", simplify("log(2)"));
    }

    public void test34() throws ParseException {
        assertEquals("sqrt(-1)*pi+log(2)", simplify("log(-2)"));
    }

    public void test35() throws ParseException {
        assertEquals("sqrt(-1)*pi+2*log(2)", simplify("log(-4)"));
    }

    public void test36() throws ParseException {
        assertEquals("0", simplify("sqrt(0)"));
    }

    public void test37() throws ParseException {
        assertEquals("1", simplify("sqrt(1)"));
    }

    public void test38() throws ParseException {
        assertEquals("sqrt(-1)", simplify("sqrt(-1)"));
    }

    public void test39() throws ParseException {
        assertEquals("sqrt(2)", simplify("sqrt(2)"));
    }

    public void test40() throws ParseException {
        assertEquals("sqrt(-1)*sqrt(2)", simplify("sqrt(-2)"));
    }

    public void test41() throws ParseException {
        assertEquals("2*sqrt(-1)", simplify("sqrt(-4)"));
    }

    public void test42() throws ParseException {
        assertEquals("1", simplify("exp(0)"));
    }

    public void test43() throws ParseException {
        assertEquals("exp(1)", simplify("exp(1)"));
    }

    public void test44() throws ParseException {
        assertEquals("1/exp(1)", simplify("exp(-1)"));
    }

    public void test45() throws ParseException {
        assertEquals("exp(1)^2", simplify("exp(2)"));
    }

    public void test46() throws ParseException {
        assertEquals("1/exp(1)^2", simplify("exp(-2)"));
    }

    public void test47() throws ParseException {
        assertEquals("1/exp(1)^4", simplify("exp(-4)"));
    }

    public void test48() throws ParseException {
        assertEquals("0", simplify("sqrt(17/12)-(1/6*sqrt(51))"));
    }

    public void test49() throws ParseException {
        assertEquals("log(2)+log(3)", simplify("log(6)"));
    }

    public void test50() throws ParseException {
        assertEquals("(r/b)^k*(1/r)^k", simplify("(r/b)^k*r^(-k)"));
    }

    public void test51() throws ParseException {
        assertEquals("(1+x)^(1/x)", simplify("(x + 1)^(1/x)"));
    }

    public void test52() throws ParseException {
        assertEquals("1+x", simplify("(x^2-1)/(x-1)"));
    }

    public void test53() throws ParseException {
        assertEquals("-(1+x^2)/(sqrt(-1)-x)", simplify("(x^2+1)/(x-sqrt(-1))"));
    }

    public void test54() throws ParseException {
        assertEquals("x", simplify("cos(acos(x))"));
    }

    public void test55() throws ParseException {
        assertEquals("x", simplify("sin(asin(x))"));
    }

    public void test56() throws ParseException {
        assertEquals("x", simplify("tan(atan(x))"));
    }

    public void test57() throws ParseException {
        assertEquals("x", simplify("cosh(acosh(x))"));
    }

    public void test58() throws ParseException {
        assertEquals("x", simplify("sinh(asinh(x))"));
    }

    public void test59() throws ParseException {
        assertEquals("x", simplify("tanh(atanh(x))"));
    }

    public void test60() throws ParseException {
        assertEquals("abs(x)", simplify("abs(abs(x))"));
    }

    public void test61() throws ParseException {
        assertEquals("1", simplify("abs(sgn(x))"));
    }

    public void test62() throws ParseException {
        assertEquals("1", simplify("sgn(abs(x))"));
    }

    public void test63() throws ParseException {
        assertEquals("sgn(x)", simplify("sgn(sgn(x))"));
    }

    public void test64() throws ParseException {
        assertEquals("x", simplify(elementary("sgn(x)*abs(x)")));
    }

    public void test65() throws ParseException {
        assertEquals("x", simplify("conjugate(conjugate(x))"));
    }

    public void test66() throws ParseException {
        assertEquals("conjugate(x)^2", simplify("conjugate(x^2)"));
    }

    public void test67() throws ParseException {
        assertEquals("conjugate(a)+conjugate(b)*conjugate(c)", simplify("conjugate(a+b*c)"));
    }

    public void test68() throws ParseException {
        assertEquals("conjugate(-x)", expand("conjugate(-x)"));
        assertEquals("-conjugate(x)", simplify("conjugate(-x)"));
    }

    public void test69() throws ParseException {
        assertEquals("conjugate(x+sqrt(-1)*y)", expand("conjugate(x+sqrt(-1)*y)"));
        assertEquals("conjugate(x)-sqrt(-1)*conjugate(y)", simplify("conjugate(x+sqrt(-1)*y)"));
    }

    public void test70() throws ParseException {
        assertEquals("1/-x", expand("1/-x"));
        assertEquals("-1/x", simplify("1/-x"));
    }

    public void test71() throws ParseException {
        assertEquals("exp(conjugate(x))", simplify("conjugate(exp(x))"));
        assertEquals("log(conjugate(x))", simplify("conjugate(log(x))"));
    }

    public void test72() throws ParseException {
        assertEquals("(-1)^x", expand("(-1)^x"));
        assertEquals("(-1)^x", simplify("(-1)^x"));
    }

    public void test73() throws ParseException {
        assertEquals("x^(1/-2)", expand("x^(1/-2)"));
        assertEquals("sqrt(1/x)", simplify("x^(1/-2)"));
    }

    public void test74() throws ParseException {
        assertEquals("(exp(a)*exp(b)*exp(e)*exp(f)*exp(i)*exp(k)*exp(o)*exp(p)*exp(s)*exp(t))/(exp(c)*exp(d)*exp(g)*exp(h)*exp(j)*exp(l)*exp(m)*exp(n)*exp(q)*exp(r))", simplify("exp(a+b-c-d+e+f-g-h+i-j+k-l-m-n+o+p-q-r+s+t)"));
    }
}
