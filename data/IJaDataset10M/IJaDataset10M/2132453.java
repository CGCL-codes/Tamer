package se.marianna.simpleDB;

public interface Where {

    /**
	 * 
	 * 
	 * @param row
	 * @param table
	 * @return if the tupel should be included
	 */
    public boolean include(int row, Relation table) throws MissingColumnName;

    public static Where ALL = new Where() {

        public boolean include(int row, Relation table) throws MissingColumnName {
            return true;
        }
    };

    public static Where NONE = new Where() {

        public boolean include(int row, Relation table) throws MissingColumnName {
            return false;
        }
    };
}
