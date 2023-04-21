package com.todo.utils;

import java.util.Comparator;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import com.todo.objects.ToDoDate;
import com.todo.objects.ToDoItem;

public interface Constants {

    /**
     * Priority Status Constants
     * ============================================================================ 
     */
    public static final int PRIORITY_NORMAL = 1;

    public static final int PRIORITY_HIGH = 1 << 1;

    /**
     * Completed Status Constants
     * ============================================================================ 
     */
    public static final int COMPLETED_NO = 1;

    public static final int COMPLETED_YES = 1 << 1;

    /**
     * Time Constants
     * ============================================================================
     */
    public static final int TIME_AM = 0;

    public static final int TIME_PM = 1;

    public static final String STR_DATE = "DATE";

    public static final String STR_TIME = "TIME";

    public static final String STR_DATETIME = "DATETIME";

    /**
     * Color Constants
     * ============================================================================
     */
    public final Color LIGHT_GRAY = new Color(Display.getDefault(), 230, 230, 230);

    public final Color WHITE = new Color(Display.getDefault(), 255, 255, 255);

    /**
     * Sorting Constants
     * ============================================================================
     */
    public static final int ASCENDING = 0;

    public static final int DESCENDING = 1;

    /**
     * String Constants
     * ============================================================================
     */
    public static final String SHELL_NAME = "ToDo";

    public static final String[] COLUMN_NAMES = { "", "", "Date Posted", "Due Date", "Date Completed", "Category", "Description" };

    /**
     * Initial Shell Size Constants
     * ============================================================================
     */
    public static final int SHELL_WIDTH = 650;

    public static final int SHELL_HEIGHT = 300;

    /**
     * Column Ids
     * ============================================================================
     */
    public static final int COLUMN_COMPLETED = 0;

    public static final int COLUMN_PRIORITY = 1;

    public static final int COLUMN_POSTED_DATE = 2;

    public static final int COLUMN_DUE_DATE = 3;

    public static final int COLUMN_COMPLETED_DATE = 4;

    public static final int COLUMN_CATGEGORY = 5;

    public static final int COLUMN_DESCRIPTION = 6;

    /**
     * Image Constants
     * ============================================================================
     */
    public static final Image IMAGE_NORMAL = Utils.getImage("normal.gif");

    public static final Image IMAGE_HIGH = Utils.getImage("high.gif");

    public static final Image IMAGE_CHECK2 = Utils.getImage("check2.gif");

    public static final Image IMAGE_BLANK = Utils.getImage("blank.gif");

    public static final Image IMAGE_ALARM = Utils.getImage("alarm.gif");

    /**
	 * Preferences Constants
     * ============================================================================ 
	 */
    public static final String PREF_ALT_ROW_COLORS = "ALT_ROW_COLORS";

    public static final String PREF_SHOW_GRID = "SHOW_GRID";

    public static final String PREF_KEEP_BLANKS_AT_BOTTOM = "KEEP_BLANKS_AT_BOTTOM";

    public static final String PREF_MAXIMIZED = "MAXIMIZED";

    public static final String PREF_X_POS = "X_POS";

    public static final String PREF_Y_POS = "Y_POS";

    public static final String PREF_HEIGHT = "HEIGHT";

    public static final String PREF_WIDTH = "WIDTH";

    public static final String PREF_COLUMN_POS = "COLUMN_POS";

    /**
	 * Button Constants
     * ============================================================================ 
	 */
    public static final int BUTTON_CUSTOM = 1;

    public static final int BUTTON_OK = 1 << 1;

    public static final int BUTTON_CANCEL = 1 << 2;

    /**
	 * Comparator Constants
     * ============================================================================ 
	 */
    public static final Comparator COMP_POSTED_DATE = new Comparator() {

        public int compare(Object obj1, Object obj2) {
            ToDoItem i1 = (ToDoItem) obj1;
            ToDoItem i2 = (ToDoItem) obj2;
            if (i1.getPostedDate().before(i2.getPostedDate())) {
                return -1;
            }
            return 1;
        }
    };

    public static final Comparator COMP_DUE_DATE = new Comparator() {

        public int compare(Object obj1, Object obj2) {
            ToDoDate d1 = ((ToDoItem) obj1).getDueDate();
            ToDoDate d2 = ((ToDoItem) obj2).getDueDate();
            if (d1 == null && d2 == null) {
                return 1;
            }
            if (d1 == null ^ d2 == null) {
                if (d1 == null) {
                    return 1;
                }
                return -1;
            }
            if (d1.before(d2)) {
                return -1;
            }
            return 1;
        }
    };

    public static final Comparator COMP_COMPLETED_DATE = new Comparator() {

        public int compare(Object obj1, Object obj2) {
            ToDoDate d1 = ((ToDoItem) obj1).getCompletedDate();
            ToDoDate d2 = ((ToDoItem) obj2).getCompletedDate();
            if (d1 == null && d2 == null) {
                return 1;
            }
            if (d1 == null ^ d2 == null) {
                if (d1 == null) {
                    return 1;
                }
                return -1;
            }
            if (d1.before(d2)) {
                return -1;
            }
            return 1;
        }
    };

    public static final Comparator COMP_COMPLETED = new Comparator() {

        public int compare(Object obj1, Object obj2) {
            ToDoItem i1 = (ToDoItem) obj1;
            ToDoItem i2 = (ToDoItem) obj2;
            if (i1.isCompleted() && !i2.isCompleted()) {
                return 1;
            }
            return -1;
        }
    };

    public static final Comparator COMP_PRIORITY = new Comparator() {

        public int compare(Object obj1, Object obj2) {
            ToDoItem i1 = (ToDoItem) obj1;
            ToDoItem i2 = (ToDoItem) obj2;
            if (i1.getPriority() == Constants.PRIORITY_NORMAL && i2.getPriority() == Constants.PRIORITY_HIGH) {
                return 1;
            }
            return -1;
        }
    };

    public static final Comparator COMP_CATEGORY = new Comparator() {

        public int compare(Object obj1, Object obj2) {
            ToDoItem i1 = (ToDoItem) obj1;
            ToDoItem i2 = (ToDoItem) obj2;
            if (i1.getCategory().trim().equals("")) {
                return 1;
            } else if (i2.getCategory().trim().equals("")) {
                return -1;
            }
            int result = i1.getCategory().toUpperCase().compareTo(i2.getCategory().toUpperCase());
            if (result > 0) {
                return 1;
            } else if (result == 0) {
                int result2 = i1.getCategory().trim().compareTo(i2.getCategory().trim());
                if (result2 >= 0) return 1;
                return -1;
            } else {
                return -1;
            }
        }
    };

    public static final Comparator COMP_DESCRIPTION = new Comparator() {

        public int compare(Object obj1, Object obj2) {
            ToDoItem i1 = (ToDoItem) obj1;
            ToDoItem i2 = (ToDoItem) obj2;
            if (i1.getDescription().trim().equals("")) {
                return 1;
            } else if (i2.getDescription().trim().equals("")) {
                return -1;
            }
            int result = i1.getDescription().toUpperCase().compareTo(i2.getDescription().toUpperCase());
            if (result > 0) {
                return 1;
            } else if (result == 0) {
                int result2 = i1.getDescription().trim().compareTo(i2.getDescription().trim());
                if (result2 >= 0) return 1;
                return -1;
            } else {
                return -1;
            }
        }
    };
}
