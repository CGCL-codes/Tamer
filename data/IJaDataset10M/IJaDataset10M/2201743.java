package net.sourceforge.opencell.csv;

import net.sourceforge.opencell.csv.rule.text.Rule;
import net.sourceforge.opencell.csv.formatter.text.Writer;
import net.sourceforge.opencell.csv.formatter.bean.SimpleBeanFormatter;
import net.sourceforge.opencell.csv.formatter.bean.BeanFormatter;
import java.util.List;
import java.util.Locale;
import java.io.OutputStream;

/**
 * Created by Haruhiko Nishi
 * Date: 2009/03/22
 * Time: 23:14:01
 */
public interface Table {

    byte[] getNewLineChars();

    boolean endsWithNL();

    String getEncoding();

    void copyColumn(Rule rule, int from, int to, boolean override, String columnName);

    void removeColumn(int column);

    int getNumOfColumns();

    int getNumOfRows();

    Cell getValueAt(int row, int column);

    List<Cell> getRow(int row);

    List<Cell> getColumn(int column);

    Writer getCSVWriter(OutputStream errOut, String rowPrefix, String rowSuffix);

    Writer getXMLWriter(String docName, String objectName);

    <E> BeanFormatter getBeanFormatter(Class<E> clazz);
}
