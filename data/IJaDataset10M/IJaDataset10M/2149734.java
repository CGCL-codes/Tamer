package org.sempere.commons.email;

import static junit.framework.Assert.*;
import org.junit.Test;

/**
 * Unit tests class for EmailMessageData class.
 * 
 * @author bsempere
 */
public class EmailMessageDataTest {

    @Test
    public void isEmptyWhenFromIsNull() throws Exception {
        EmailMessageData bean = new EmailMessageData();
        bean.setSubject("subject");
        bean.setTo(new String[] { "to@to.to" });
        assertTrue(bean.isEmpty());
    }

    @Test
    public void isEmptyWhenSubjectIsNull() throws Exception {
        EmailMessageData bean = new EmailMessageData();
        bean.setFrom("from@from.from");
        bean.setTo(new String[] { "to@to.to" });
        assertTrue(bean.isEmpty());
    }

    @Test
    public void isEmptyWhenToIsNull() throws Exception {
        EmailMessageData bean = new EmailMessageData();
        bean.setFrom("from@from.from");
        bean.setSubject("subject");
        bean.setTo(null);
        assertTrue(bean.isEmpty());
    }

    @Test
    public void isEmptyWhenToIsEmpty() throws Exception {
        EmailMessageData bean = new EmailMessageData();
        bean.setFrom("from@from.from");
        bean.setSubject("subject");
        bean.setTo(new String[] {});
        assertTrue(bean.isEmpty());
    }

    @Test
    public void isNotEmpty() throws Exception {
        EmailMessageData bean = new EmailMessageData();
        bean.setFrom("from@from.from");
        bean.setSubject("subject");
        bean.setTo(new String[] { "to@to.to" });
        assertFalse(bean.isEmpty());
    }
}
