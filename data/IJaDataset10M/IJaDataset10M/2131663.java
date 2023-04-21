package absyn;

public class RecordType extends Type {

    public TypeFields fields;

    public RecordType(int pos, TypeFields fields) {
        super(pos);
        this.fields = fields;
    }
}
