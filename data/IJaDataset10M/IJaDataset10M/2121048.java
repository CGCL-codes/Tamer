package br.com.visualmidia.business;

import junit.framework.TestCase;

/**
 * @author   Lucas
 */
public class VaderTest extends TestCase {

    private GDDate date = new GDDate("11/10/2006");

    private Vader _vader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _vader = new Vader(date);
    }

    public void testVaderDate() {
        assertEquals("11/10/2006", new GDDate(_vader.getVaderDate()).getFormatedDate());
        _vader.setVaderDate(new GDDate("10/10/2006"));
        assertEquals("10/10/2006", new GDDate(_vader.getVaderDate()).getFormatedDate());
    }
}
