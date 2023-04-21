package view;

public class CurrentValues {

    public static String catalog = "";

    public static int num;

    public static int predpr_id;

    private static int cbProductValueId = 1;

    private static int cbSyrValueId = 1;

    public static int getNum() {
        return num;
    }

    public static void setNum(int num) {
        CurrentValues.num = num;
    }

    public static void setCatalog(String c) {
        catalog = c;
    }

    public static String getCatalog() {
        return catalog;
    }

    public static void setPredprId(int id) {
        predpr_id = id;
    }

    public static Integer getPredprId() {
        return new Integer(predpr_id);
    }

    public static int getcbProductValueId() {
        return cbProductValueId;
    }

    public static void setcbProductValueId(int cbProductValueId) {
        CurrentValues.cbProductValueId = cbProductValueId;
    }

    public static int getcbSyrValueId() {
        return cbSyrValueId;
    }

    public static void setcbSyrValueId(int cbSyrValueId) {
        CurrentValues.cbSyrValueId = cbSyrValueId;
    }
}
