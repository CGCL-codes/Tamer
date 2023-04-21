package br.net.woodstock.rockframework.core.test.text;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.TestCase;
import br.net.woodstock.rockframework.text.StringFormat;
import br.net.woodstock.rockframework.text.StringFormatFactory;
import br.net.woodstock.rockframework.text.StringFormatTemplate;
import br.net.woodstock.rockframework.text.impl.CharsetTransformer;
import br.net.woodstock.rockframework.text.impl.RandomGenerator;
import br.net.woodstock.rockframework.text.impl.RandomGenerator.RandomPattern;

public class StringTest extends TestCase {

    public void xtest1() throws Exception {
        String s = "530000000012010";
        StringFormat format = StringFormatFactory.getInstance().getFormat("#####.######/####");
        System.out.println(format.format(s));
    }

    public void xtest2() throws Exception {
        String s = "53000.000001/2010";
        StringFormat format = StringFormatFactory.getInstance().getFormat("#####.######/####");
        System.out.println(format.parse(s));
    }

    public void xtest3() throws Exception {
        RandomGenerator randomString = new RandomGenerator(15);
        for (int i = 0; i < 5; i++) {
            System.out.println(randomString.generate());
        }
        randomString = new RandomGenerator(15, RandomPattern.DIGITS);
        for (int i = 0; i < 5; i++) {
            System.out.println(randomString.generate());
        }
        randomString = new RandomGenerator(15, RandomPattern.LETTER);
        for (int i = 0; i < 5; i++) {
            System.out.println(randomString.generate());
        }
    }

    public void xtest4() throws Exception {
        String texto = "Frase um. Frase dois. Frase 3";
        Pattern pattern = Pattern.compile("(\\.?.*dois.*\\.)");
        Matcher matcher = pattern.matcher(texto);
        System.out.println(matcher);
        System.out.println(matcher.matches());
        System.out.println(matcher.group());
        System.out.println(matcher.groupCount());
        System.out.println(matcher.regionStart());
        System.out.println(matcher.regionEnd());
    }

    public void xtest5() throws Exception {
        System.out.println(new CharsetTransformer(Charset.forName("ISO-8859-1"), Charset.forName("UTF-8")).transform("J�nior"));
    }

    public void xtest6() throws Exception {
        StringFormat format = new StringFormat("999.999.999-99", '9');
        String s = format.format("01234567890");
        String ss = format.parse(s);
        System.out.println(s);
        System.out.println(ss);
    }

    public void test7() throws Exception {
        StringFormat cpfFormat = StringFormatTemplate.getInstance().getFormat("cpf");
        StringFormat cepFormat = StringFormatTemplate.getInstance().getFormat("cep");
        String cpf = cpfFormat.format("12345678901");
        String cep = cepFormat.format("12345678");
        System.out.println(cpf);
        System.out.println(cep);
    }
}
