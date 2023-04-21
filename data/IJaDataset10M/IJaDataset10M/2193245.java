/* This file is part of synon2hbm.
 *
 * synon2hbm is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * synon2hbm is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with synon2hbm; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.synon2hbm.synon.field.base;

import java.sql.Date;
import java.text.ParseException;

import junit.framework.TestCase;
import net.sf.synon2hbm.globals.GlobalStrategy;
import net.sf.synon2hbm.globals.Globals;

/**
 * @author cln
 * @version $Revision: 1.2 $
 */
public class DateFieldTest extends TestCase {

	class TestDateField extends DateField {
		public TestDateField(Date value) {
			super(value);
		}

		public TestDateField(String value) throws ParseException {
			super(value);
		}

		public String getTypeId() {
			return "TestDateField";
		}
	}

	final public void testDTE() {
		int dte = 1041224;
		DateField dt = new TestDateField(DateField.valueOf(dte)) {
		};
		assertEquals(dte, dt.intValue());
	}

	final public void testD8Æ() {
		int d8ae = 20041224;
		DateField dt = new TestDateField(DateField.valueOf8(d8ae)) {
		};
		assertEquals(d8ae, dt.int8Value());
	}

	/**
	 * Test constructors and equals.
	 * 
	 * Test that initialization time and the constructor used dosn't affect
	 * whether daet fields are considered equal.
	 * <p>
	 * The Date field actually used to store the date has more information than
	 * day, month and year, and therefore two date fields may not be equal if
	 * this additional information isn't initialized correctly.
	 * 
	 * @throws ParseException
	 */
	public final void testConstructors_and_equals() throws ParseException {
		new GlobalStrategy();
		Globals.setLocale("da", "DK");
		DateField d1 = new TestDateField(DateField.valueOf(1061230));
		DateField d2 = new TestDateField(DateField.valueOf(1061231));
		DateField sd1 = new TestDateField("2006-12-30");
		DateField sd2 = new TestDateField("2006-12-31");
		assertFalse(d1.equals(d2));
		assertFalse(sd1.equals(sd2));
		assertEquals(d1, sd1);
		assertEquals(d2, sd2);
		DateField d3 = new TestDateField(DateField.valueOf(1061230));
		DateField sd3 = new TestDateField("2006-12-30");
		assertEquals(d1, d3);
		assertEquals(sd1, sd3);
	}

	final public void testFormatting() throws ParseException {
		new GlobalStrategy();
		Globals.setLocale("da", "DK");
		DateField dt1 = new TestDateField(DateField.valueOf(1071224));
		String dt1Formatted = dt1.formattedValue();
		assertEquals("2007-12-24", dt1Formatted);
		TestDateField dt2 = new TestDateField(dt1Formatted);
		assertEquals(dt1, dt2);
	}

}
