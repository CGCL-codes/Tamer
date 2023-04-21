package org.gdbms.engine.data.edition;

import org.gdbms.engine.data.InnerDBUtils;
import org.gdbms.engine.data.driver.DBDriver;
import org.gdbms.engine.data.driver.DriverException;
import org.gdbms.engine.values.Value;

public class InsertEditionInfo implements EditionInfo {

    private String tableName;

    private DBDriver driver;

    private String[] fieldNames;

    private long internalBufferIndex;

    private InternalBuffer internalBuffer;

    public InsertEditionInfo(String tableName, InternalBuffer internalBuffer, long internalBufferIndex, String[] fieldNames, DBDriver driver) {
        this.tableName = tableName;
        this.internalBuffer = internalBuffer;
        this.internalBufferIndex = internalBufferIndex;
        this.fieldNames = fieldNames;
        this.driver = driver;
    }

    public String getSQL() throws DriverException {
        Value[] row = new Value[fieldNames.length];
        for (int i = 0; i < row.length; i++) {
            row[i] = internalBuffer.getFieldValue(internalBufferIndex, i);
        }
        return InnerDBUtils.createInsertStatement(tableName, row, fieldNames, driver);
    }
}
