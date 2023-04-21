package net.mufly.client.core;

import java.util.Set;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import net.mufly.domain.Tag;
import net.mufly.domain.Transaction;

public class MuflyUtils {

    public static DateTimeFormat dateFormater = DateTimeFormat.getFormat("dd/MM/yyyy");

    public static NumberFormat numberFormater = NumberFormat.getFormat("#,##0.00;-#,##0.00");

    public static final int TEXT_AREA_VISIBLE_LINES = 3;

    /**
	 * Process all the tags from one transaction a return a concat list of the transactions
	 */
    public static String getTagsFromTransaction(Transaction tran) {
        String tags = "";
        Set<Tag> tagList = tran.getTags();
        if (tagList == null) {
            return null;
        } else {
            for (Tag tag : tagList) {
                String tagsTmp = tag.getTagName();
                tags = tags + " " + tagsTmp;
            }
        }
        return tags.substring(1, tags.length());
    }

    public static void removeRows(FlexTable table) {
        int numRows = table.getRowCount();
        while (numRows > 1) {
            table.removeRow(numRows - 1);
            table.getFlexCellFormatter().setRowSpan(0, 1, numRows - 1);
            numRows = table.getRowCount();
        }
    }

    public static void applyAmountStyle(TextBox txt) {
        if (!txt.getText().trim().equals("")) {
            if (txt.getText().charAt(0) == '-') txt.setStyleName("gwt-TextBox negativeAmount"); else txt.setStyleName("gwt-TextBox positiveAmount");
        }
    }

    public static void removeAmountStyle(TextBox txt) {
        if (!txt.getText().trim().equals("")) {
            if (txt.getText().charAt(0) == '-') txt.setStyleName("gwt-TextBox negativeAmount"); else txt.setStyleName("gwt-TextBox positiveAmount");
        }
    }
}
