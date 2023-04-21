package net.sf.brightside.aikido.metamodel.beans;

import java.util.LinkedList;
import java.util.List;
import net.sf.brightside.aikido.metamodel.Practice;
import net.sf.brightside.aikido.metamodel.Sensei;

public class SenseiBean implements Sensei {

    private String name;

    private String rank;

    private String club;

    private List<Practice> practices = new LinkedList<Practice>();

    @Override
    public String getClub() {
        return club;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Practice> getPractices() {
        return practices;
    }

    @Override
    public String getRank() {
        return rank;
    }

    @Override
    public void setClub(String club) {
        this.club = club;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setPractices(List<Practice> practices) {
        this.practices = practices;
    }
}
