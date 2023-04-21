package uk.org.ogsadai.database.jdbc.book;

import uk.org.ogsadai.database.jdbc.book.CreateJDBCBookData;

/**
 * <p>
 * Program to create simple sample OGSA-DAI "littleblackbook"-style tables with 
 * schema.
 * </p>
 * <p>
 * See {@link uk.org.ogsadai.database.jdbc.book.CreateJDBCBookData} for
 * usage.
 * </p> 
 * <code>
 * Default Settings:
 *     Driver:                 com.ibm.db2.jcc.DB2Driver
 *     URL:                    jdbc:db2://localhost:50000/ogsadai
 *     User:                   ogsadai
 *     Password:               ogsadai
 *     NameOfTableToCreateOfPrefixOfTables:  littleblackbook
 *     NumberOfTablesToCreate: 1
 *     NumberOfRowsToCreate:   10000 
 * </code>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class CreateDB2BookData extends CreateJDBCBookData {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) International Business Machines Corporation, 2002-2005, Copyright (c) The University of Edinburgh,  2002-2010.";

    /**
     * Constructor.
     */
    public CreateDB2BookData() {
        super();
        mDataCreator = new DB2BookDataCreator();
    }

    /**
     * Creates and populates table.
     * 
     * @param args
     *     Command-line arguments.
     */
    public static void main(String[] args) {
        CreateDB2BookData creator = new CreateDB2BookData();
        creator.execute(args);
    }

    @Override
    protected void setDefaultSettings() {
        mDriver = "com.ibm.db2.jcc.DB2Driver";
        mURL = "jdbc:db2://localhost:500000/ogsadai";
    }
}
