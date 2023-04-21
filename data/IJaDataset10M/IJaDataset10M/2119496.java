package ocumed.teams;

import java.util.Set;

/**
 *
 * @author Willi
 */
public interface IOrt {

    public int getOrtid();

    public void setOrtid(int ortid);

    public ILand getLand();

    public void setLand(ILand land);

    public String getOrtplz();

    public void setOrtplz(String ortplz);

    public String getOrtname();

    public void setOrtname(String ortname);

    public Set<IPatient> getPatients();

    public void setPatients(Set<IPatient> patients);
}
