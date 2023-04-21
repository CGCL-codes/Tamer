package uk.org.dbmm.model;

public class RosterLine {

    private int number;

    private String name;

    private String type;

    private String grade;

    private String troopClass;

    private String general;

    private boolean mounted;

    private boolean dbe;

    private boolean chariot;

    private float ap;

    private float me;

    private int id;

    private boolean armyBaggage;

    private String extras;

    public RosterLine() {
        super();
        setNumber(0);
        setName("");
        setTroopClass("");
        setType("");
        setGrade("");
        setGeneral("");
        setExtras("");
        setAp(0f);
        setMe(0f);
        setId(0);
    }

    public RosterLine(ListItem line) {
        super();
        setNumber(1);
        setName(line.getName());
        setTroopClass(line.getTroopClass());
        setType(line.getType());
        setGrade(line.getGrade());
        setGeneral(line.getGeneral());
        setExtras(line.getExtras());
        setAp(line.getAp());
        setMe(line.getMe());
        setId(line.getId());
    }

    public RosterLine(int number, String name, String troopClass, String type, String grade, String general, String extras, float ap, float me, int id) {
        super();
        setNumber(number);
        setName(name);
        setTroopClass(troopClass);
        setType(type);
        setGrade(grade);
        setGeneral(general);
        setExtras(extras);
        setAp(ap);
        setMe(me);
        setId(id);
    }

    public boolean isSame(ListItem newLine) {
        if (newLine.getId() == id) {
            return true;
        } else {
            return false;
        }
    }

    public String getTroopClass() {
        return troopClass;
    }

    public void setTroopClass(String troopClass) {
        this.troopClass = troopClass;
    }

    public float getAp() {
        return ap;
    }

    public void setAp(float ap) {
        this.ap = ap;
    }

    public boolean getChariot() {
        return chariot;
    }

    public void setChariot(boolean chariot) {
        this.chariot = chariot;
    }

    public boolean getDbe() {
        return dbe;
    }

    public void setDbe(boolean dbe) {
        this.dbe = dbe;
    }

    public String getGeneral() {
        return general;
    }

    public void setGeneral(String general) {
        this.general = general;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public float getMe() {
        return me;
    }

    public void setMe(float me) {
        this.me = me;
    }

    public boolean getMounted() {
        return mounted;
    }

    public void setMounted(boolean mounted) {
        this.mounted = mounted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getColumn(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return new Integer(getNumber());
            case 1:
                return getName();
            case 2:
                return getTroopClass();
            case 3:
                return getType();
            case 4:
                return getGrade();
            case 5:
                return getGeneral();
            case 6:
                return getExtras();
            case 7:
                return new Float(getAp());
            case 8:
                return new Float(getMe());
            default:
                return "";
        }
    }

    public static String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return "#";
            case 1:
                return "Name";
            case 2:
                return "";
            case 3:
                return "";
            case 4:
                return "";
            case 5:
                return "";
            case 6:
                return "";
            case 7:
                return "AP";
            case 8:
                return "ME";
            default:
                return "";
        }
    }

    public void increment() {
        number++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @return the armyBaggage
	 */
    public boolean isArmyBaggage() {
        return armyBaggage;
    }

    /**
	 * @param armyBaggage the armyBaggage to set
	 */
    public void setArmyBaggage(boolean armyBaggage) {
        this.armyBaggage = armyBaggage;
    }

    /**
	 * @return the extras
	 */
    public String getExtras() {
        return extras;
    }

    /**
	 * @param extras the extras to set
	 */
    public void setExtras(String extras) {
        this.extras = extras;
    }
}
