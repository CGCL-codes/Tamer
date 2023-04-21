package network.protocol;

/** 
 * @author Erik
 *
 * if someone want to convert a building to another this is the
 * package thats handle it.
 *
 */
public class ConvertPackage {

    private char type;

    private int building;

    /**
	 * convert a correct formated string to a ConvertPackage
	 * format: type(char) + \n + building(int) + \n
	 * @param data
	 */
    public ConvertPackage(String data) {
        int start = 0, end = 0;
        end = data.indexOf('\n', start);
        type = data.charAt(0);
        start = end + 1;
        end = data.indexOf('\n', start);
        building = Integer.parseInt(data.substring(start, end));
    }

    public ConvertPackage(char type, int building) {
        this.type = type;
        this.building = building;
    }

    /**
	 * convert the ConvertPackage to a correct formated string
	 * @return
	 */
    public String toPackage() {
        StringBuffer res = new StringBuffer();
        res.append(type);
        res.append('\n');
        res.append(building);
        res.append('\n');
        return res.toString();
    }

    public char getType() {
        return type;
    }

    public int getBuilding() {
        return building;
    }
}
