package org.fao.fenix.metadataeditor.domain.struct;

/**
 *
 * @author etj
 */
public class FieldDate extends Field {

    private boolean now = false;

    public FieldDate(String name) {
        super(name);
    }

    public boolean isNow() {
        return now;
    }

    public void setNow(boolean now) {
        this.now = now;
    }

    @Override
    public void accept(FieldVisitor fieldVisitor) {
        fieldVisitor.visit(this);
    }

    protected void copyInternal(FieldDate field) {
        super.copyInternal(field);
        field.now = now;
    }

    public FieldDate copy() {
        FieldDate ret = new FieldDate(getName());
        copyInternal(ret);
        return ret;
    }
}
