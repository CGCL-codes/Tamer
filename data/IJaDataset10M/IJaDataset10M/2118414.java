package core.data;

public class AccessGuiData {

    private String a_b_s = "";

    private String a_c_s = "";

    private int timeValue = 0;

    public AccessGuiData() {
    }

    public synchronized void setAmountBunny(String am_b) {
        this.a_b_s = am_b;
    }

    public synchronized void setAmountCarrot(String am_c) {
        this.a_c_s = am_c;
    }

    public synchronized void setTimerValue(int x) {
        this.timeValue = x;
    }

    public synchronized double getAmountBunny() {
        return convertStringToDouble(a_b_s);
    }

    public synchronized double getAmountCarrot() {
        return convertStringToDouble(a_c_s);
    }

    public synchronized int getTimerValue() {
        return timeValue;
    }

    public synchronized Pair getPair() {
        Pair temp = new Pair(getAmountBunny(), getAmountCarrot(), 0);
        return temp;
    }

    private double convertStringToDouble(String s) {
        double success = -1;
        try {
            success = Double.parseDouble(s);
        } catch (Exception e) {
            say("convertStringToDouble wirft einen Fehler : ");
            say(e.toString());
        }
        return success;
    }

    private void say(String s) {
        System.out.print("\nAccessGuiData ->" + s);
    }
}
