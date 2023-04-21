package com.completex.objective.components.persistency.test.hsql.gen;

import java.util.Date;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Blob;

public interface ITestSlave {

    Long getTestSlaveId();

    void setTestSlaveId(Long testSlaveId);

    boolean izNullTestSlaveId();

    void setNullTestSlaveId();

    Long getTestMasterId();

    void setTestMasterId(Long testMasterId);

    boolean izNullTestMasterId();

    void setNullTestMasterId();

    Long getTestMaster2Id();

    void setTestMaster2Id(Long testMaster2Id);

    boolean izNullTestMaster2Id();

    void setNullTestMaster2Id();

    String getName();

    void setName(String name);

    boolean izNullName();

    void setNullName();

    Date getCreationDate();

    void setCreationDate(Date creationDate);

    boolean izNullCreationDate();

    void setNullCreationDate();

    Date getLastUpdated();

    void setLastUpdated(Date lastUpdated);

    boolean izNullLastUpdated();

    void setNullLastUpdated();

    String getColChar();

    void setColChar(String colChar);

    boolean izNullColChar();

    void setNullColChar();
}
