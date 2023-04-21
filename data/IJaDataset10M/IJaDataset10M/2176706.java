package database.exceptions;

public class MultipleClassNamesException extends Exception {

    private static final long serialVersionUID = 6687752455123058456L;

    public long[] classIDs;

    public MultipleClassNamesException(long[] user_ids) {
        classIDs = user_ids;
    }

    public long[] getClassIDs() {
        return classIDs;
    }
}
