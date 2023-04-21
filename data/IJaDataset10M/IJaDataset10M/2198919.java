package commons;

import org.makagiga.commons.Mnemonic;
import org.makagiga.test.AbstractTest;
import org.makagiga.test.Test;
import org.makagiga.test.TestMethod;

@Test(className = Mnemonic.class)
public final class TestMnemonic extends AbstractTest {

    @Test(methods = @TestMethod(name = "parse", parameters = "String"))
    public void test_parse() {
        assert Mnemonic.parse(null) == 0;
        assert Mnemonic.parse("") == 0;
        assert Mnemonic.parse("x") == 0;
        assert Mnemonic.parse("xy") == 0;
        assert Mnemonic.parse("xyz") == 0;
        assert Mnemonic.parse("&") == 0;
        assert Mnemonic.parse("& ") == 0;
        assert Mnemonic.parse(" &") == 0;
        assert Mnemonic.parse(" & ") == 0;
        assert Mnemonic.parse("Foo & Bar") == 0;
        assert Mnemonic.parse("&&") == 0;
        assert Mnemonic.parse("&&x") == 0;
        assert Mnemonic.parse("&&x&y") == 'y';
        assert Mnemonic.parse("&x") == 'x';
        assert Mnemonic.parse("&x&y") == 'x';
        assert Mnemonic.parse("&Foo Bar") == 'F';
        assert Mnemonic.parse("Fo&o Bar") == 'o';
        assert Mnemonic.parse("Foo &Bar") == 'B';
        assert Mnemonic.parse("Foo Ba&r") == 'r';
    }
}
