package org.mga.common.fields.mysql;

public class Note extends org.mga.common.fields.Note {

    /**
	 *
	 */
    public Note() {
    }

    /**
	 * @param label
	 * @param size
	 * @param required
	 * @param relation
	 */
    public Note(String label, boolean required, String relation) {
        super(label, required, relation);
        this.setSize(64000);
    }

    @Override
    public String getDBTypeName() {
        return "Varchar(" + this.getSize() + ")";
    }
}
