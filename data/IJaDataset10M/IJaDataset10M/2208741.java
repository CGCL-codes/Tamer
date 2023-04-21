package org.middleheaven.global.text;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.middleheaven.global.Culture;
import org.middleheaven.global.text.writeout.FormatNotFoundException;
import org.middleheaven.global.text.writeout.NumberWriteoutFormat;
import org.middleheaven.tool.test.MiddleHeavenTestCase;

public class NumberWordsFormatTest extends MiddleHeavenTestCase {

    @Test(expected = FormatNotFoundException.class)
    public void testaNumberToWordsUnknown() {
        NumberWriteoutFormat.getInstance(Culture.valueOf("xx", "XX"));
    }

    @Test
    public void testaNumberToWordsPtPT() {
        NumberWriteoutFormat format = NumberWriteoutFormat.getInstance(Culture.valueOf("pt", "PT"));
        assertEquals("zero", format.inWords(0));
        assertEquals("cem", format.inWords(100));
        assertEquals("vinte e cinco", format.inWords(25));
        assertEquals("cento e cinquenta e nove", format.inWords(159));
        assertEquals("tr�s mil cento e cinquenta e nove", format.inWords(3159));
        assertEquals("treze mil cento e cinquenta e noventa e nove cent�simos", format.inWords(13150.99));
        assertEquals("duzentos e treze mil cento e cinquenta e noventa e nove cent�simos", format.inWords(213150.99));
        assertEquals("um milh�o , duzentos e treze mil cento e cinquenta e oitocentos e noventa e nove mil�simos", format.inWords(1213150.899));
        ;
        assertEquals("um milh�o , tr�s mil cento e cinquenta e nove d�cimos", format.inWords(1003150.9));
        assertEquals("um milh�o , tr�s mil cento e cinquenta e nove mil quatrocentos e setenta e seis d�cimos mil�simos", format.inWords(1003150.9476));
    }

    @Test
    public void testaNumberToWordsEnGB() {
        NumberWriteoutFormat format = NumberWriteoutFormat.getInstance(Culture.valueOf("en", "GB"));
        assertEquals("zero", format.inWords(0));
        assertEquals("one hundred", format.inWords(100));
        assertEquals("one hundred and one", format.inWords(101));
        assertEquals("twenty-five", format.inWords(25));
        assertEquals("one hundred and fifty-nine", format.inWords(159));
        assertEquals("two thousand and one", format.inWords(2001));
        assertEquals("three thousand , one hundred and fifty-nine", format.inWords(3159));
        assertEquals("thirteen thousand , one hundred and fifty and ninety-nine hundredth", format.inWords(13150.99));
        assertEquals("two hundred and thirteen thousand , one hundred and fifty and ninety-nine hundredth", format.inWords(213150.99));
        assertEquals("one million , two hundred and thirteen thousand , one hundred and fifty and eight hundred and ninety-nine thousandth", format.inWords(1213150.899));
        ;
        assertEquals("one million and three thousand , one hundred and fifty and nine tenth", format.inWords(1003150.9));
        assertEquals("one million and three thousand , one hundred and fifty and nine thousand , four hundred and seventy-six millionth", format.inWords(1003150.9476));
    }

    @Test
    public void testaNumberToWordsEsES() {
        NumberWriteoutFormat format = NumberWriteoutFormat.getInstance(Culture.valueOf("es", "ES"));
        assertEquals("cero", format.inWords(0));
        assertEquals("cien", format.inWords(100));
        assertEquals("ciento cincuenta", format.inWords(150));
        assertEquals("ciento uno", format.inWords(101));
        assertEquals("veinticinco", format.inWords(25));
        assertEquals("cuarenta y dos", format.inWords(42));
        assertEquals("ciento cincuenta y nueve", format.inWords(159));
        assertEquals("dos mil uno", format.inWords(2001));
        assertEquals("tres mil ciento cincuenta y nueve", format.inWords(3159));
        assertEquals("trece mil ciento cincuenta y noventa y nueve centesimas", format.inWords(13150.99));
        assertEquals("doscientos trece mil ciento cincuenta y noventa y nueve centesimas", format.inWords(213150.99));
        assertEquals("uno mill�n doscientos trece mil ciento cincuenta y ochocientos noventa y nueve milesimas", format.inWords(1213150.899));
        ;
        assertEquals("uno mill�n tres mil ciento cincuenta y nueve decimas", format.inWords(1003150.9));
        assertEquals("uno mill�n tres mil ciento cincuenta y nueve mil cuatrocientos setenta y seis decimas milesimas", format.inWords(1003150.9476));
    }

    @Test
    public void testaNumberToWordsFrFR() {
        NumberWriteoutFormat format = NumberWriteoutFormat.getInstance(Culture.valueOf("fr", "FR"));
        assertEquals("z�ro", format.inWords(0));
        assertEquals("cent", format.inWords(100));
        assertEquals("cent et un", format.inWords(101));
        assertEquals("cent cinquante", format.inWords(150));
        assertEquals("vignt et cinq", format.inWords(25));
        assertEquals("quarante et deux", format.inWords(42));
        assertEquals("cent cinquante et neuf", format.inWords(159));
        assertEquals("deux mile et un", format.inWords(2001));
        assertEquals("troi mile cent cinquante et neuf", format.inWords(3159));
        assertEquals("treize mile cent cinquante etnoventa et neuf centisime", format.inWords(13150.99));
    }
}
