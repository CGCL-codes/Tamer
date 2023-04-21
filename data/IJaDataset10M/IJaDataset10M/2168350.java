package ru.concretesoft.concretesplitviewer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mytinski Leonid
 *
 * Класс описывающий дистанцию
 */
public class Distance {

    private String name;

    private int length;

    private int numberOfCP;

    private Lap[] laps;

    private List<Group> groups;

    /** Creates a new instance of Distance 
     *
     * @param  name  название дистанции (обычно название группы)
     * @param  length  длина дистанции 
     * @param  numberOfCP  количество пунктов
     * @param  lengths  массив содержащий длины перегонов
     *
     */
    public Distance(String name, int length, int numberOfCP, int[] lengths) {
        this(name, length, numberOfCP, lengths, null);
    }

    public Distance(String name, int length, int numberOfCP, int[] lengths, int[] cPsNumbers) {
        this.name = name;
        this.length = length;
        this.numberOfCP = numberOfCP;
        laps = new Lap[numberOfCP];
        if ((lengths != null) && (cPsNumbers != null) && (lengths.length == cPsNumbers.length)) {
            laps[0] = new Lap(Lap.START_CONTROL_POINT, cPsNumbers[0], lengths[0]);
            for (int i = 1; i < lengths.length; i++) {
                laps[i] = new Lap(cPsNumbers[i - 1], cPsNumbers[i], lengths[i]);
            }
        } else if (lengths != null) {
            setLengthsOfDists(lengths);
        }
        groups = new ArrayList<Group>();
    }

    public Distance(String name, int length, int numberOfCP) {
        this(name, length, numberOfCP, null, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public int getLength() {
        return length;
    }

    public int getNumberOfCP() {
        return numberOfCP;
    }

    public int getLengthOfDist(int n) {
        if (laps[n - 1] == null) return -1;
        return laps[n - 1].getLength();
    }

    public void setLengthOfDist(int n, int l) {
        if (laps[n - 1] == null) {
            laps[n - 1] = new Lap(l);
        } else {
            laps[n - 1].setLength(l);
        }
    }

    /** Method for set all lengths of laps.
     *
     *  @param  ls  array of lengths
     *
     *  @return  <code>true</code> if set lengths. <code>false</code> if length of array not equals with number of control points in distance
     */
    public boolean setLengthsOfDists(int[] ls) {
        if ((ls == null) || (ls.length != getNumberOfCP())) return false; else {
            for (int i = 0; i < ls.length; i++) setLengthOfDist(i + 1, ls[i]);
        }
        return true;
    }

    public void removeGroup(Group g) {
        groups.remove(g);
    }

    public void addGroup(Group g) {
        if (!groups.contains(g)) groups.add(g); else ;
    }

    public void setGroups(List<Group> grs) {
        groups = new ArrayList<Group>(grs);
    }

    public List<Group> getGroups() {
        return new ArrayList<Group>(groups);
    }

    public boolean equals(Distance d) {
        return (getLength() == d.getLength()) && (getNumberOfCP() == d.getNumberOfCP());
    }
}
