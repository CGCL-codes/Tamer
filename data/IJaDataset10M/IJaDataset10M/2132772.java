package org.tonguetied.audit;

import junitx.extensions.EqualsHashCodeTestCase;
import org.tonguetied.audit.AuditLogRecord.Operation;
import org.tonguetied.keywordmanagement.Keyword;

/**
 * Test of equals and hashCode method for the class {@link AuditLogRecord}.
 * 
 * @author bsion
 * 
 */
public class AuditLogRecordEqualsHashCodeTest extends EqualsHashCodeTestCase {

    public AuditLogRecordEqualsHashCodeTest(String name) {
        super(name);
    }

    @Override
    protected Object createInstance() throws Exception {
        Keyword keyword = new Keyword();
        keyword.setKeyword("test");
        AuditLogRecord record = new AuditLogRecord(Operation.insert, keyword, keyword.toString(), null, "user");
        return record;
    }

    @Override
    protected Object createNotEqualInstance() throws Exception {
        Keyword keyword = new Keyword();
        keyword.setKeyword("test");
        AuditLogRecord record = new AuditLogRecord(Operation.delete, keyword, keyword.toString(), null, "user");
        return record;
    }
}
