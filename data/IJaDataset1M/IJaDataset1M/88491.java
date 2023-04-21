package db;

public class TableFieldRecord {

    public String Name;

    public String Type;

    public OptionField options;

    public TableFieldRecord(String NameField, String NameType, OptionField opc) {
        Name = NameField;
        Type = NameType;
        options = opc;
    }

    public String getName() {
        return Name;
    }

    public String getType() {
        return Type;
    }

    public OptionField getOptions() {
        return options;
    }
}
