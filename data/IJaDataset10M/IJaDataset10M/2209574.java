package org.mkh.cmd;

public class DataModel {

    private int width = 122;

    private int height = 2;

    private int area = 2;

    /**
	 * @return the height
	 */
    public int getArea() {
        return area;
    }

    public void pre() {
        System.out.println("In method pre");
    }

    public void post() {
        System.out.println("In method post =============");
    }
}
