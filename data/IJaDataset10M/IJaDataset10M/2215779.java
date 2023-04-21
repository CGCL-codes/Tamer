package org.peaseplate.lang;

import org.testng.annotations.*;

@Test(sequential = true)
@SuppressWarnings("boxing")
public class ParserNumberTest extends AbstractParserTest {

    @Test
    public void primaryTests() throws Exception {
    }

    @Test
    public void testMutiplicativeOperators() throws Exception {
        System.out.println("Multiplicative operators:");
        test("42 * 42", 42 * 42);
        test("-42 / 42", -42 / 42);
        test("42 % -42", 42 % -42);
        test("0.42d * 0.17d", 0.42d * 0.17d);
        test("0.42d / -0.17d", 0.42d / -0.17d);
        test("-0.42d % 0.17d", -0.42d % 0.17d);
        test("+42 * +0.17 / +0.42", +42 * +0.17 / +0.42);
    }

    @Test
    public void testAdditiveOperators() throws Exception {
        System.out.println("Additive operators:");
        test("42 + 42", 42 + 42);
        test("42 - 42", 42 - 42);
        test("0.42d + 0.17d", 0.42d + 0.17d);
        test("0.42d - 0.17d", 0.42d - 0.17d);
        test("42 + 0.17 - 0.42", 42 + 0.17 - 0.42);
    }

    @Test
    public void testShiftOperators() throws Exception {
        System.out.println("Shift operators:");
        test("42 << 2", (42 << 2));
        test("42 >> -2", (42 >> -2));
        test("42 >>> 2", (42 >>> 2));
    }

    @Test
    public void testRelationalOperators() throws Exception {
        System.out.println("Relational operators:");
        test("42 < 42", (42 < 42));
        test("42 < 17", (42 < 17));
        test("17 < 42", (17 < 42));
        test("42 <= 42", (42 <= 42));
        test("42 <= 17", (42 <= 17));
        test("17 <= 42", (17 <= 42));
        test("42 > 42", (42 > 42));
        test("42 > 17", (42 > 17));
        test("17 > 42", (17 > 42));
        test("42 >= 42", (42 >= 42));
        test("42 >= 17", (42 >= 17));
        test("17 >= 42", (17 >= 42));
    }

    @Test
    public void testEqualityOperators() throws Exception {
        System.out.println("Equality operators:");
        test("42 == 42", (42 == 42));
        test("42 == 17", (42 == 17));
        test("17 == 42", (17 == 42));
        test("42 != 42", (42 != 42));
        test("42 != 17", (42 != 17));
        test("17 != 42", (17 != 42));
    }

    @Test
    public void testBitwiseOperators() throws Exception {
        System.out.println("Bitwise operators:");
        test("42 & 42", (42 & 42));
        test("42 & 17", (42 & 17));
        test("17 & 42", (17 & 42));
        test("42 ^ 42", (42 ^ 42));
        test("42 ^ 17", (42 ^ 17));
        test("17 ^ 42", (17 ^ 42));
        test("42 | 42", (42 | 42));
        test("42 | 17", (42 | 17));
        test("17 | 42", (17 | 42));
    }

    @Test
    public void testConditionalOrder() throws Exception {
        System.out.println("Conditional order:");
        test("true && false", true && false);
        test("true || false", true || false);
        test("false || true", false || true);
    }

    @Test
    public void testOperatorOrder() throws Exception {
        System.out.println("Operator order:");
        test("42 + 42 * 42 - 42 / 42", 42 + 42 * 42 - 42 / 42);
        test("42 * 42 + 42 / 42 - 42", 42 * 42 + 42 / 42 - 42);
        test("(42 + 42) * (42 - 42) / 42", (42 + 42) * (42 - 42) / 42);
        test("((42 * 42) + 42) / (42 - 17)", ((42 * 42) + 42) / (42 - 17));
        test("42 << 4 + 17 >> 2", 42 << 4 + 17 >> 2);
        test("42 + 4 << 17 - 2", 42 + 4 << 17 - 2);
        test("42 / 2 < 17 + 2", 42 / 2 < 17 + 2);
        test("42 / 2 - 4 == 17 * 0.5 * 2", 42 / 2 - 4 == 17 * 0.5 * 2);
        test("true && false || true", true && false || true);
        test("false || true && false", false || true && false);
        test("!true && !true || false", !true && !true || false);
    }
}
