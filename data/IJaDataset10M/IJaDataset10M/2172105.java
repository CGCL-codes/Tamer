package net.sourceforge.freejava.cli;

public class EditResult {

    public static final int NONE = 0;

    public static final int SAVE = 10;

    public static final int SAVE_SAME = 11;

    public static final int SAVE_DIFF = 12;

    public static final int DELETE = 20;

    public static final int RENAME = 30;

    public static final int MOVE = 40;

    public static final int COPY = 50;

    protected String[] tags;

    public Boolean changed;

    public int operation;

    /** dest pathname for RENAME, MOVE, COPY */
    public Object dest;

    /** saved, deleted, renamed, moved, copied */
    public boolean done;

    public boolean error;

    public Throwable cause;

    public EditResult(String... tags) {
        this.tags = tags;
        this.operation = NONE;
    }

    public String getOperationName() {
        switch(operation) {
            case NONE:
                return "none";
            case SAVE:
            case SAVE_DIFF:
            case SAVE_SAME:
                return "save";
            case DELETE:
                return "none";
            case RENAME:
                return "renm";
            case MOVE:
                return "move";
            case COPY:
                return "copy";
        }
        return "????";
    }

    public void save(Boolean changed) {
        this.changed = changed;
        if (changed == null) operation = SAVE; else operation = changed ? SAVE_DIFF : SAVE_SAME;
    }

    public void delete() {
        this.operation = DELETE;
    }

    public void renameTo(Object dest) {
        this.operation = RENAME;
        this.dest = dest;
    }

    public void moveTo(Object dest) {
        this.operation = MOVE;
        this.dest = dest;
    }

    public void copyTo(Object dest) {
        this.operation = COPY;
        this.dest = dest;
    }

    public void error(Throwable cause) {
        this.error = true;
        this.cause = cause;
    }

    public void setDone() {
        this.done = true;
    }

    public static EditResult pass(String... tags) {
        EditResult result = new EditResult(tags);
        return result;
    }

    public static EditResult compareAndSave(String... tags) {
        EditResult result = new EditResult(tags);
        result.save(null);
        return result;
    }

    public static EditResult saveSame(String... tags) {
        EditResult result = new EditResult(tags);
        result.save(false);
        return result;
    }

    public static EditResult saveDiff(String... tags) {
        EditResult result = new EditResult(tags);
        result.save(true);
        return result;
    }

    public static EditResult rm(String... tags) {
        EditResult result = new EditResult(tags);
        result.delete();
        return result;
    }

    public static EditResult ren(Object dest, String... tags) {
        EditResult result = new EditResult(tags);
        result.renameTo(dest);
        return result;
    }

    public static EditResult mv(Object dest, String... tags) {
        EditResult result = new EditResult(tags);
        result.moveTo(dest);
        return result;
    }

    public static EditResult cp(Object dest, String... tags) {
        EditResult result = new EditResult(tags);
        result.copyTo(dest);
        return result;
    }

    public static EditResult err(Throwable cause, String... tags) {
        EditResult result = new EditResult(tags);
        result.error(cause);
        return result;
    }
}
