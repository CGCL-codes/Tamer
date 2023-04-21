package org.jtools.rjdbc.samples.helloworld;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Database {

    public static final String tablename = "HELLOWORLDTABLE";

    public static final String keycolumnname = "KEYCOLUMN";

    public static final String[][] columns = { { "COLUMN01", "VARCHAR(24)" }, { "COLUMN02", "NUMERIC(5,0)" }, { "COLUMN03", "DECIMAL(7,4)" }, { "COLUMN04", "BIGINT" }, { "COLUMN05", "INTEGER" }, { "COLUMN06", "DATE" } };

    public static final String[][] literals = { { "'KEY1'", "'VALUE1'", "1", "13.89", "7654", "123", "null" }, { "'KEY2'", "'VALUE2'", "-1", "-113.89", "-7654", "-123", "null" }, { "'KEY3'", "'VALUE3'", "-1", "-113.89", "-7654", "-123", "null" }, { "'KEY4'", "'VALUE4'", "-1", "-113.89", "-7654", "-123", "null" }, { "'KEY5'", "null", "null", "null", "null", "null", "null" }, { "'KEY6'", "null", "null", "null", "null", "null", "null" } };

    public static final String[][] updateLiterals = { { "KEY3", null, null, null, null, null, null }, { "KEY4", "VALUE4u", "-2", "213.89", "7655", "456", null } };

    public static final Set<String> deleteKeys = new HashSet<String>(Arrays.asList(new String[] { "KEY5" }));

    public static final String[][] insertLiterals = { { "KEY7", null, null, null, null, null, null }, { "KEY8", "VALUE8i", "-2", "213.89", "7655", "456", null } };

    public static final String[][] ps_insertLiterals = { { "KEY9", null, null, null, null, null, null }, { "KEY10", "VALUE10i", "-2", "213.89", "7655", "456", null } };

    public static final String url = "jdbc:derby:" + Database.class.getSimpleName();
}
