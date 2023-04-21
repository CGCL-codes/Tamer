package org.perfectjpattern.jee.integration.dao.spring;

import static org.easymock.EasyMock.*;
import java.text.*;
import junit.framework.*;
import org.slf4j.*;

/**
 * Spring-Hibernate Generic DAO example test suite 
 *
 * @author <a href="mailto:bravegag@hotmail.com">Giovanni Azua</a>
 * @version $Revision: 1.0 $Date: Apr 20, 2008 1:58:38 PM $
 */
public class TestExample extends TestCase {

    public void testSpringGenericDao() throws ParseException {
        Logger myLoggerMock = createNiceMock(Logger.class);
        Example.setLogger(myLoggerMock);
        String myLogging = LINE_BREAK + "**************************************************************" + LINE_BREAK + "| Customer(id='2', name='Giovanni')" + LINE_BREAK + "|--> Order(id='2', date='11/17/07 8:00 AM')" + LINE_BREAK + "|    |-> Product(id='4', name='Nikon D40x', list price='350')" + LINE_BREAK + "|    |-> Product(id='5', name='Nikon D300', list price='2,000')" + LINE_BREAK + "|    |-> Product(id='2', name='Nikon D700', list price='3,200')" + LINE_BREAK + "|--> Order(id='3', date='1/15/08 5:35 PM')" + LINE_BREAK + "|    |-> Product(id='1', name='Nikon 70-200mm', " + "list price='1,800')" + LINE_BREAK + "|    |-> Product(id='6', name='Nikon 17-55mm', " + "list price='1,200')" + LINE_BREAK + "|    |-> Product(id='3', name='Nikon 80-400mm', " + "list price='1,500')" + LINE_BREAK + "**************************************************************";
        myLoggerMock.debug(myLogging);
        replay(myLoggerMock);
        Example.main(new String[0]);
        verify(myLoggerMock);
    }

    private static final String LINE_BREAK = System.getProperty("line.separator");
}
