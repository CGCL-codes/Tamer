package view.timeView;

public class Graph {

    public static final String INPUT = TimeView.INPUT;

    public static final String OUTPUT = TimeView.OUTPUT;

    public static final String STATEVARIABLE = TimeView.STATEVARIABLE;

    public static final String STATE = TimeView.STATE;

    public static final boolean NUMBER = TimeView.NUMBER;

    public static final boolean STRING = TimeView.STRING;

    private String name = "";

    private String type = "";

    private String Unit = "";

    private boolean dataType = NUMBER;

    public Graph(String nm, String typ, boolean datTyp) {
        name = nm;
        type = typ;
        dataType = datTyp;
    }

    public Graph(String nm, String typ, boolean datTyp, String unitVal) {
        name = nm;
        type = typ;
        Unit = unitVal;
        dataType = datTyp;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean getDataType() {
        return dataType;
    }

    public String getUnit() {
        if (Unit.equalsIgnoreCase("")) return "N/A"; else return Unit;
    }
}
