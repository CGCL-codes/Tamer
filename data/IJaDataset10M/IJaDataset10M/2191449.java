package entity;

public class FileElement {

    public FileElement(String path, String name, long weigth, int sol) {
        this.name = name;
        this.weigth = weigth;
        this.solution = sol;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWeigth() {
        return weigth;
    }

    public void setWeigth(long weigth) {
        this.weigth = weigth;
    }

    public int getSolution() {
        return solution;
    }

    public void setSolution(int sol) {
        this.solution = sol;
    }

    public String getPath() {
        return this.path;
    }

    @Override
    public String toString() {
        return getWeigth() + " byte\t-\t" + getName();
    }

    private String path;

    private long weigth;

    private String name;

    private int solution;
}
