package net.nothinginteresting.tablelayout.layout;

/**
 * @author Dmitri Gorbenko
 * 
 */
public interface TableLayout {

    enum UNITS {

        PIXELS("PIXELS"), PERCENTS("PERCENTS");

        private final String name;

        private UNITS(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    enum HORIZONTAL_ALIGN {

        LEFT("LEFT"), RIGHT("RIGHT"), CENTER("CENTER"), FILL("FILL");

        private final String name;

        private HORIZONTAL_ALIGN(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    enum VERTICAL_ALIGN {

        TOP("TOP"), BOTTOM("BOTTOM"), CENTER("CENTER"), FILL("FILL");

        private final String name;

        private VERTICAL_ALIGN(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
	 * Indicates that a default should be used (value is -1).
	 */
    static final int DEFAULT = -1;

    /**
	 * @param col
	 * @return
	 */
    TableData column(int col);

    /**
	 * @param row
	 * @return
	 */
    TableData row(int row);

    /**
	 * @param col
	 * @param row
	 * @return
	 */
    void init(int col, int row);

    /**
     * @return
     */
    Integer getColumnsCount();

    /**
     * @return
     */
    Integer getRowsCount();
}
