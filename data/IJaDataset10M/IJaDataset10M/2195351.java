package ocumed.teams;

import java.util.Set;

/**
 *
 * @author Willi
 */
public interface ILand {

    public int getLandid();

    public void setLandid(int landid);

    public String getLandname();

    public void setLandname(String landname);

    public String getLandkurzform();

    public void setLandkurzform(String landkurzform);

    public Set<IOrt> getOrts();

    public void setOrts(Set<IOrt> orts);
}
