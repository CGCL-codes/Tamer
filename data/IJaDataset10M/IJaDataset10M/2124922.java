package net.sf.brightside.aikido.metamodel;

import java.io.Serializable;
import java.util.List;

public interface Dojo {

    public String getName();

    public void setName(String name);

    public City getCity();

    public void setCity(City city);

    public List<Club> getClubs();

    public Serializable takeId();

    public List<Practice> getPractices();
}
