package org.riverock.sql.parser.test;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import org.riverock.sql.parser.Parser;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.startup.StartupApplication;
import org.exolab.castor.xml.Marshaller;

/**
 * Author: mill
 * Date: Apr 29, 2003
 * Time: 10:13:18 AM
 *
 * $Id: TestParser.java,v 1.6 2006/06/05 19:19:31 serg_main Exp $
 */
public class TestParser {

    public TestParser() {
    }

    public static void writeToFile(Object obj, String fileName) throws Exception {
        String encoding = "utf-8";
        FileOutputStream fos = new FileOutputStream(fileName);
        Marshaller marsh = new Marshaller(new OutputStreamWriter(fos, encoding));
        marsh.setValidation(false);
        marsh.setMarshalAsDocument(true);
        marsh.setEncoding(encoding);
        marsh.marshal(obj);
        fos.flush();
        fos.close();
        fos = null;
    }

    private static Parser doProcess(String sql, String nameFile, String[] tables) throws Exception {
        Parser parser = null;
        parser = Parser.getInstance(sql);
        Object obj = null;
        switch(parser.typeStatement) {
            case Parser.SELECT:
                obj = parser.select;
                break;
            case Parser.INSERT:
                obj = parser.insert;
                break;
            case Parser.UPDATE:
                obj = parser.update;
                break;
            case Parser.DELETE:
                obj = parser.delete;
                break;
        }
        writeToFile(obj, GenericConfig.getGenericDebugDir() + nameFile);
        System.out.println("object " + obj);
        return parser;
    }

    public static void main(String[] args) throws Exception {
        StartupApplication.init();
        testSelect();
        testInsert();
    }

    private static void testUpdate() throws Exception {
        doProcess("update TEST_TABLE_UPDATE " + "set " + "id = 1 " + "where id in (select a.id_test from TEST_SELECT a) ", "parser-update-1.xml", new String[] { "" });
    }

    private static void testDelete() throws Exception {
        doProcess("delete from TEST_TABLE_UPDATE " + "where id in (select a.id_test from TEST_SELECT a) ", "parser-delete-1.xml", new String[] { "" });
    }

    private static void testInsert() throws Exception {
        doProcess("insert into WM_PRICE_IMPORT_TABLE " + "( is_group, id ) " + "values " + "( ?, 'abc' )", "parser-insert-01.xml", new String[] { "" });
        doProcess("insert into WM_PRICE_IMPORT_TABLE " + "( is_group, id ) " + "values " + "( ?, upper(?) )", "parser-insert-0.xml", new String[] { "" });
        doProcess("insert into test_insert(i,o,p) " + "values " + "(1,2,3 ) ", "parser-insert-1.xml", new String[] { "" });
        doProcess("insert into test_insert(i,o,p) " + "select 1,2,3 from dual ", "parser-insert-2.xml", new String[] { "" });
        doProcess("insert into test_insert(i,o,p) " + "(select 1,2,3 from dual) ", "parser-insert-3.xml", new String[] { "" });
        doProcess("insert into test_insert(i,o,p) " + "values " + "(?, ?, ?, ?) ", "parser-insert-4.xml", new String[] { "" });
    }

    private static void testSelect() throws Exception {
        doProcess("select b.id_firm id, concat(b.id_firm, ', ', b.full_name) NAME_FIRM " + "from   WM_LIST_COMPANY b " + "where  b.ID_FIRM in (10,12) and b.is_deleted=0 " + "order  by b.ID_FIRM ASC ", "parser-select-22.xml", new String[] { "WM_LIST_COMPANY" });
        doProcess("select * from dept partition(id)", "parser-select-13.xml", new String[] { "dept" });
    }
}
