package example.chapter5.swing;

import example.chapter5.Book;

/**
 * Helper for access Book object in table.
 * 
 * @author Xuefeng
 */
public final class BookHelper {

    private static final String[] COLUMN_NAMES = { "ID", "书名", "作者" };

    public static int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    public static String getColumnName(int index) {
        if (index < 0 || index >= COLUMN_NAMES.length) {
            throw new IllegalArgumentException("Illegal column index Range(0, " + (COLUMN_NAMES.length - 1) + ")");
        }
        return COLUMN_NAMES[index];
    }

    public static Object getValueAt(Book book, int index) {
        Object obj = null;
        if (book == null || index < 0 || index >= COLUMN_NAMES.length) {
            throw new IllegalArgumentException("Illegal column index Range(0, " + (COLUMN_NAMES.length - 1) + ")");
        }
        switch(index) {
            case 0:
                obj = book.getId();
                break;
            case 1:
                obj = book.getName();
                break;
            case 2:
                obj = book.getAuthor();
                break;
            default:
                obj = "N/A";
                break;
        }
        return obj;
    }

    public static void setValueAt(Book book, Object obj, int index) {
        if (book == null || index < 0 || index >= COLUMN_NAMES.length) {
            throw new IllegalArgumentException("Illegal column index Range(0, " + (COLUMN_NAMES.length - 1) + ")");
        }
        if (obj == null) {
            throw new NullPointerException("Null pointer of Book object.");
        }
        String val = obj.toString();
        switch(index) {
            case 0:
                book.setId(val);
                break;
            case 1:
                book.setName(val);
                break;
            case 2:
                book.setAuthor(val);
                break;
            default:
                throw new IllegalArgumentException("No suche index: " + index);
        }
    }

    public static boolean isCellEditable(int col) {
        return BookHelper.isCellEditable(col);
    }
}
