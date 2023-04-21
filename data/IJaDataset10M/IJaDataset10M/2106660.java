package bueu.bexl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.Test;
import bueu.bexl.utils.Interval;

public class ExpresionsTest {

    @Test
    public void contants() {
        assertEquals(Bexl.get("null"), null);
        assertEquals(Bexl.get("5"), 5.0);
        assertEquals(Bexl.get("5.021"), 5.021);
        assertEquals(Bexl.get("''"), "");
        assertEquals(Bexl.get("'abc'"), "abc");
        assertEquals(Bexl.get("true"), true);
        assertEquals(Bexl.get("false"), false);
        assertNull(Bexl.get("{}"));
        assertEquals(Bexl.list("{}").length, 0);
        assertEquals(Bexl.get("{3, 'a', {1}}"), 3.0);
        assertArrayEquals(Bexl.list("{3, 'a', {1}}"), new Object[] { 3.0, "a", new Object[] { 1.0 } });
        assertEquals(Bexl.get("(4,5)"), new Interval<Double>(4.0, false, 5.0, false));
        assertEquals(Bexl.get("(4,5]"), new Interval<Double>(4.0, false, 5.0, true));
        assertEquals(Bexl.get("[4,5)"), new Interval<Double>(4.0, true, 5.0, false));
        assertEquals(Bexl.get("[4,5]"), new Interval<Double>(4.0, true, 5.0, true));
    }

    @Test
    public void minusExpr() {
        assertEquals(Bexl.get("-5"), -5.0);
        assertEquals(Bexl.get("-{}"), -0.0);
        assertEquals(Bexl.get("-{1}"), -1.0);
        assertEquals(Bexl.get("-null"), -0.0);
        error("-'a'");
    }

    @Test
    public void notExpr() {
        assertEquals(Bexl.get("!0"), true);
        assertEquals(Bexl.get("!1"), false);
        assertEquals(Bexl.get("!'a'"), false);
        assertEquals(Bexl.get("!null"), true);
        assertEquals(Bexl.get("!{}"), true);
        assertEquals(Bexl.get("!{1}"), false);
    }

    @Test
    public void addExpr() {
        assertEquals(Bexl.get("1+2"), 3.0);
        assertEquals(Bexl.get("1+'a'"), "1a");
        assertEquals(Bexl.get("1+null"), 1.0);
        assertArrayEquals(Bexl.list("1+{}"), new Object[] { 1.0 });
        assertArrayEquals(Bexl.list("{'a'}+{1}"), new Object[] { "a", 1.0 });
    }

    @Test
    public void substractExpr() {
        assertEquals(Bexl.get("null-1"), null);
        assertEquals(Bexl.get("2-1"), 1.0);
        assertEquals(Bexl.get("2-{}"), 2.0);
        assertEquals(Bexl.get("2-{1, 2}"), 1.0);
        assertEquals(Bexl.get("2-null"), 2.0);
        assertArrayEquals(Bexl.list("{'a', 'b'} - 'a'"), new Object[] { "b" });
        assertArrayEquals(Bexl.list("{'a', 'b'} - {'a', 'c'}"), new Object[] { "b" });
        error("2-'a'", "2-{'a'}");
    }

    @Test
    public void multiplyExpr() {
        assertEquals(Bexl.get("3*2"), 6.0);
        assertEquals(Bexl.get("3*{}"), 0.0);
        assertEquals(Bexl.get("3*{2, 5}"), 6.0);
        assertEquals(Bexl.get("2*null"), 0.0);
        error("2*'a'", "2*{'a'}");
    }

    @Test
    public void divExpr() {
        assertEquals(Bexl.get("3/2"), 1.5);
        assertEquals(Bexl.get("{}/3"), 0.0);
        assertEquals(Bexl.get("3/{}"), Double.POSITIVE_INFINITY);
        assertEquals(Bexl.get("{6}/3"), 2.0);
        assertEquals(Bexl.get("3/{2, 6}"), 1.5);
        assertEquals(Bexl.get("null/2"), 0.0);
        assertEquals(Bexl.get("2/null"), Double.POSITIVE_INFINITY);
        error("2/'a'", "2/{'a'}");
    }

    @Test
    public void modExpr() {
        assertEquals(Bexl.get("3%2"), 1.0);
        assertEquals(Bexl.get("{}%3"), 0.0);
        assertEquals(Bexl.get("3%{}"), Double.NaN);
        assertEquals(Bexl.get("{6}%4"), 2.0);
        assertEquals(Bexl.get("3%{2, 6}"), 1.0);
        assertEquals(Bexl.get("null/2"), 0.0);
        assertEquals(Bexl.get("2%null"), Double.NaN);
        error("2%'a'", "2%{'a'}");
    }

    @Test
    public void andExpr() {
        assertEquals(Bexl.get("3&&2"), true);
        assertEquals(Bexl.get("{}&&3"), false);
        assertEquals(Bexl.get("{6}&&4"), true);
        assertEquals(Bexl.get("null&&2"), false);
        assertEquals(Bexl.get("2&&'a'"), true);
        assertEquals(Bexl.get("2&&''"), false);
        assertEquals(Bexl.get("2&&{'a'}"), true);
    }

    @Test
    public void orExpr() {
        assertEquals(Bexl.get("3||0"), 3.0);
        assertEquals(Bexl.get("{}||3"), 3.0);
        assertEquals(Bexl.get("{}||0"), false);
        assertEquals(Bexl.get("{6}||4"), 6.0);
        assertEquals(Bexl.get("null||'a'"), "a");
        assertEquals(Bexl.get("null||''"), false);
        assertEquals(Bexl.get("2||{'a'}"), 2.0);
    }

    @Test
    public void ltExpr() {
        assertEquals(Bexl.get("3<2"), false);
        assertEquals(Bexl.get("4<7"), true);
        assertEquals(Bexl.get("'aa'<'bb'"), true);
        assertEquals(Bexl.get("'za4'<'za2'"), false);
        assertEquals(Bexl.get("'4'<5"), true);
        assertEquals(Bexl.get("'4'<4"), false);
        assertEquals(Bexl.get("'4'<{4}"), false);
        assertEquals(Bexl.get("4<null"), false);
        assertEquals(Bexl.get("null<4"), true);
        assertEquals(Bexl.get("null<null"), false);
        assertEquals(Bexl.get("{3, 1}<{'3', 1, 1}"), true);
        assertEquals(Bexl.get("{3, 2}<{'3', 1, 1}"), false);
    }

    @Test
    public void gtExpr() {
        assertEquals(Bexl.get("3>2"), true);
        assertEquals(Bexl.get("4>7"), false);
        assertEquals(Bexl.get("'aa'>'bb'"), false);
        assertEquals(Bexl.get("'za4'>'za2'"), true);
        assertEquals(Bexl.get("'4'>5"), false);
        assertEquals(Bexl.get("'4'>4"), false);
        assertEquals(Bexl.get("'4'>{4}"), false);
        assertEquals(Bexl.get("4>null"), true);
        assertEquals(Bexl.get("null>4"), false);
        assertEquals(Bexl.get("null>null"), false);
        assertEquals(Bexl.get("{3, 1}>{'3', 1, 1}"), false);
        assertEquals(Bexl.get("{3, 2}>{'3', 1, 1}"), true);
    }

    @Test
    public void leExpr() {
        assertEquals(Bexl.get("3<=2"), false);
        assertEquals(Bexl.get("4<=7"), true);
        assertEquals(Bexl.get("'aa'<='bb'"), true);
        assertEquals(Bexl.get("'za4'<='za2'"), false);
        assertEquals(Bexl.get("'4'<=5"), true);
        assertEquals(Bexl.get("'4'<=4"), true);
        assertEquals(Bexl.get("'4'<={4}"), true);
        assertEquals(Bexl.get("4<=null"), false);
        assertEquals(Bexl.get("null<=4"), true);
        assertEquals(Bexl.get("null<=null"), true);
        assertEquals(Bexl.get("{3, 1}<={'3', 1, 1}"), true);
        assertEquals(Bexl.get("{3, 2}<={'3', 1, 1}"), false);
    }

    @Test
    public void geExpr() {
        assertEquals(Bexl.get("3>=2"), true);
        assertEquals(Bexl.get("4>=7"), false);
        assertEquals(Bexl.get("'aa'>='bb'"), false);
        assertEquals(Bexl.get("'za4'>='za2'"), true);
        assertEquals(Bexl.get("'4'>=5"), false);
        assertEquals(Bexl.get("'4'>=4"), true);
        assertEquals(Bexl.get("'4'>={4}"), true);
        assertEquals(Bexl.get("4>=null"), true);
        assertEquals(Bexl.get("null>=4"), false);
        assertEquals(Bexl.get("null>=null"), true);
        assertEquals(Bexl.get("{3, 1}>={'3', 1, 1}"), false);
        assertEquals(Bexl.get("{3, 2}>={'3', 1, 1}"), true);
    }

    @Test
    public void eqExpr() {
        assertEquals(Bexl.get("3==2"), false);
        assertEquals(Bexl.get("'3'==3"), true);
        assertEquals(Bexl.get("4==null"), false);
        assertEquals(Bexl.get("null==4"), false);
        assertEquals(Bexl.get("null==null"), true);
        assertEquals(Bexl.get("{3, 1}=='3'"), true);
        assertEquals(Bexl.get("{3, 1}=={'3'}"), false);
        assertEquals(Bexl.get("{3, 1}=={'3', 4}"), false);
        assertEquals(Bexl.get("{3, '4'}=={'3', 4}"), true);
    }

    @Test
    public void neqExpr() {
        assertEquals(Bexl.get("3!=2"), true);
        assertEquals(Bexl.get("'3'!=3"), false);
        assertEquals(Bexl.get("4!=null"), true);
        assertEquals(Bexl.get("null!=4"), true);
        assertEquals(Bexl.get("null!=null"), false);
        assertEquals(Bexl.get("{3, 1}!='3'"), false);
        assertEquals(Bexl.get("{3, 1}!={'3'}"), true);
        assertEquals(Bexl.get("{3, 1}!={'3', 4}"), true);
        assertEquals(Bexl.get("{3, '4'}!={'3', 4}"), false);
    }

    @Test
    public void inExpr() {
        assertEquals(Bexl.get("3 in 2"), false);
        assertEquals(Bexl.get("'3' in 3"), true);
        assertEquals(Bexl.get("4 in null"), false);
        assertEquals(Bexl.get("null in 4"), true);
        assertEquals(Bexl.get("null in null"), true);
        assertEquals(Bexl.get("3 in {3, 1}"), true);
        assertEquals(Bexl.get("4 in {3, 1}"), false);
        assertEquals(Bexl.get("{3, 1} in {'3', 4, 1}"), true);
        assertEquals(Bexl.get("{3, 4} in {'3', 2}"), false);
        assertEquals(Bexl.get("5 in [4, 5]"), true);
        assertEquals(Bexl.get("5 in [4, 5)"), false);
        assertEquals(Bexl.get("4 in [4, 5]"), true);
        assertEquals(Bexl.get("4 in (4, 5)"), false);
    }

    @Test
    public void conditionalExpr() {
        assertEquals(Bexl.get("3?1:2"), 1.0);
        assertEquals(Bexl.get("0?1:2"), 2.0);
        assertEquals(Bexl.get("null?1:null"), null);
        assertEquals(Bexl.get("{2}?null:2"), null);
        assertEquals(Bexl.get("{}?null:2"), 2.0);
    }

    @Test
    public void functions() {
        assertEquals(Bexl.get("sum({4, 6})"), 10.0);
        assertEquals(Bexl.get("avg({4, 6})"), 5.0);
        assertEquals(Bexl.get("count({4, 6})"), 2);
        assertEquals(Bexl.get("max({4, 6})"), 6.0);
        assertEquals(Bexl.get("min({4, 6})"), 4.0);
        assertEquals(Bexl.get("number('8')"), 8.0);
        assertEquals(Bexl.get("string(4)"), "4");
        assertEquals(Bexl.get("boolean(4)"), true);
        assertEquals(Bexl.get("round(4.56)"), 5l);
        assertEquals(Bexl.get("round(4.46)"), 4l);
        assertEquals(Bexl.get("ceiling(4.5)"), 5.0);
        assertEquals(Bexl.get("floor(4.5)"), 4.0);
        assertEquals(Bexl.get("abs(-4.5)"), 4.5);
        assertEquals(Bexl.get("length('hola')"), 4);
        assertEquals(Bexl.get("upperCase('hola')"), "HOLA");
        assertEquals(Bexl.get("lowerCase('HOLA')"), "hola");
        error("notFound()");
    }

    @Test
    public void properties() {
        Bean inner1 = new Bean();
        inner1.setString("inner1");
        inner1.setStrings(new String[] { "hola1", "adios1" });
        inner1.setBool(true);
        inner1.setBools(new boolean[] { false, true });
        inner1.setNumber(15);
        inner1.setNumbers(new double[] { 14, 15 });
        inner1.setEnums(new Bean.Enum[] { Bean.Enum.one, Bean.Enum.two });
        Bean inner2 = new Bean();
        inner2.setString("inner2");
        inner2.setStrings(new String[] { "hola2", "adios2" });
        inner2.setBool(true);
        inner2.setBools(new boolean[] { false, true });
        inner2.setNumber(25);
        inner2.setNumbers(new double[] { 24, 25 });
        inner2.setEnums(new Bean.Enum[] { Bean.Enum.two, Bean.Enum.three });
        ArrayList<Bean> list = new ArrayList<Bean>();
        list.add(inner1);
        list.add(inner2);
        HashSet<Bean> set = new HashSet<Bean>();
        set.add(inner1);
        set.add(inner2);
        Bean bean = new Bean();
        bean.setString("bean");
        bean.setStrings(new String[] { "hola", "adios" });
        bean.setBool(true);
        bean.setBools(new boolean[] { false, true });
        bean.setInner(inner1);
        bean.setInnerList(list);
        bean.setInnerSet(set);
        bean.setInners(new Bean[] { inner1, inner2 });
        bean.setNumber(5);
        bean.setNumbers(new double[] { 4, 5 });
        bean.setEnums(new Bean.Enum[] { Bean.Enum.one, Bean.Enum.three });
        assertEquals(Bexl.get(bean, "string"), "bean");
        assertEquals(Bexl.get(bean, "strings"), "hola");
        assertEquals(Bexl.get(bean, "strings[1]"), "adios");
        assertArrayEquals(Bexl.list(bean, "strings"), new Object[] { "hola", "adios" });
        assertEquals(Bexl.get(bean, "bool"), true);
        assertEquals(Bexl.get(bean, "bools"), false);
        assertEquals(Bexl.get(bean, "bools[1]"), true);
        assertArrayEquals(Bexl.list(bean, "bools"), new Object[] { false, true });
        assertEquals(Bexl.get(bean, "number"), 5.0);
        assertEquals(Bexl.get(bean, "numbers"), 4.0);
        assertEquals(Bexl.get(bean, "numbers[1]"), 5.0);
        assertArrayEquals(Bexl.list(bean, "numbers"), new Object[] { 4.0, 5.0 });
        assertEquals(Bexl.get(bean, "inner"), inner1);
        assertEquals(Bexl.get(bean, "inners"), inner1);
        assertArrayEquals(Bexl.list(bean, "inners"), new Object[] { inner1, inner2 });
        assertEquals(Bexl.get(bean, "inner.string"), "inner1");
        assertEquals(Bexl.get(bean, "inners.string"), "inner1");
        assertEquals(Bexl.get(bean, "inners[1].string"), "inner2");
        assertEquals(Bexl.get(bean, "innerList[1].string"), "inner2");
        assertNull(Bexl.get(bean, "string.string"));
        assertArrayEquals(Bexl.list(bean, "inners.string"), new Object[] { "inner1", "inner2" });
        assertArrayEquals(Bexl.list(bean, "inners.strings"), new Object[] { "hola1", "adios1", "hola2", "adios2" });
        assertArrayEquals(Bexl.list(bean, "inners.numbers"), new Object[] { 14.0, 15.0, 24.0, 25.0 });
        assertArrayEquals(Bexl.list(bean, "inners[string=='inner2'].strings"), new Object[] { "hola2", "adios2" });
        assertArrayEquals(Bexl.list(bean, "innerList[string=='inner2'].strings"), new Object[] { "hola2", "adios2" });
        assertArrayEquals(Bexl.list(bean, "innerSet[string=='inner2'].strings"), new Object[] { "hola2", "adios2" });
        assertArrayEquals(Bexl.list(bean.getInners(), "[string=='inner2'].strings"), new Object[] { "hola2", "adios2" });
        assertArrayEquals(Bexl.list(bean.getInnerList(), "[string=='inner2'].strings"), new Object[] { "hola2", "adios2" });
        assertArrayEquals(Bexl.list(bean.getInnerSet(), "[string=='inner2'].strings"), new Object[] { "hola2", "adios2" });
        assertArrayEquals(Bexl.list(bean, "inners.enums"), new Object[] { Bean.Enum.one, Bean.Enum.two, Bean.Enum.two, Bean.Enum.three });
    }

    @Test
    public void asociaciones() {
        assertEquals(Bexl.get("(4 + 7) * 2 - 2 * 5"), 12.0);
        assertEquals(Bexl.get("4 + 7 * 2 - 2 * 5"), 8.0);
        assertEquals(Bexl.get("4 + (3 > 2 * 3? 5 : 4 * 2 - 4) + 1"), 9.0);
        assertEquals(Bexl.get("4 in {4,3}? 2 * (1 + 2) : 8"), 6.0);
        assertEquals(Bexl.get("4 + sum({4, 3})"), 11.0);
        assertEquals(Bexl.get("4 + 2 * avg({4, 3})"), 11.0);
        assertEquals(Bexl.get("4 * 2 + count({4, 3})"), 10.0);
    }

    @Test
    public void setValue() {
        Bean bean = new Bean();
        Bexl.set(bean, "string", "hola");
        assertEquals(bean.getString(), "hola");
        Bexl.set(bean, "strings", "hola");
        assertArrayEquals(bean.getStrings(), new Object[] { "hola" });
        Bexl.set(bean, "strings", new Integer[] { 5, 6 });
        assertArrayEquals(bean.getStrings(), new Object[] { "5", "6" });
        Bexl.set(bean, "number", "6");
        assertEquals(new Double(bean.getNumber()), new Double(6.0));
        Bexl.set(bean, "numbers", new Object[] { "6", 10 });
        assertEquals(bean.getNumbers().length, 2);
        assertEquals(new Double(bean.getNumbers()[0]), new Double(6.0));
        assertEquals(new Double(bean.getNumbers()[1]), new Double(10.0));
        Bexl.set(bean, "bool", 5);
        assertEquals(bean.isBool(), true);
        Bexl.set(bean, "bool", "false");
        assertEquals(bean.isBool(), false);
        Bexl.set(bean, "bools", new String[] { "true", "kk" });
        assertEquals(bean.getBools().length, 2);
        assertEquals(bean.getBools()[0], true);
        assertEquals(bean.getBools()[1], false);
        Bexl.set(bean, "enums", new String[] { "three", "one" });
        assertArrayEquals(bean.getEnums(), new Object[] { Bean.Enum.three, Bean.Enum.one });
        Bexl.set(bean, "inner", bean);
        assertEquals(bean.getInner(), bean);
        Bexl.set(bean, "inner", null);
        assertNull(bean.getInner());
        Bexl.set(bean, "inner.string", 6);
        assertNotNull(bean.getInner());
        assertEquals(bean.getInner().getString(), "6");
        Bexl.set(bean, "inners[3].string", 6);
        assertNotNull(bean.getInners());
        assertEquals(bean.getInners().length, 4);
        assertNull(bean.getInners()[0]);
        assertNull(bean.getInners()[1]);
        assertNull(bean.getInners()[2]);
        assertEquals(bean.getInners()[3].getString(), "6");
        Bexl.set(bean, "inners.string", 6);
        assertEquals(bean.getInners().length, 4);
        assertNotNull(bean.getInners()[0]);
        assertEquals(bean.getInners()[0].getString(), "6");
        assertNull(bean.getInners()[1]);
        assertNull(bean.getInners()[2]);
        assertNotNull(bean.getInners()[3]);
        assertEquals(bean.getInners()[3].getString(), "6");
        Bexl.set(bean, "innerList.string", 6);
        assertNotNull(bean.getInnerList());
        System.out.println(bean.getInnerList());
        assertEquals(bean.getInnerList().size(), 1);
        assertEquals(bean.getInnerList().get(0).getString(), "6");
        Bexl.set(bean, "innerList[3].string", 6);
        assertEquals(bean.getInnerList().size(), 4);
        assertEquals(bean.getInnerList().get(3).getString(), "6");
    }

    private final void error(String... exprs) {
        for (String expr : exprs) {
            try {
                Bexl.get(expr);
            } catch (BexlException e) {
                continue;
            }
            fail(expr);
        }
    }
}
