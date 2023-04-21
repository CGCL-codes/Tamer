package net.sf.plexian.store;

public class OriginalName implements Comparable {

    public int compareTo(Object o) {
        if (o instanceof OriginalName) {
            int other = ((OriginalName) o).weight;
            if (other == weight) {
                return 0;
            } else if (other > weight) {
                return 1;
            }
            return -1;
        }
        return 0;
    }

    private String name;

    private int weight;

    public OriginalName(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
