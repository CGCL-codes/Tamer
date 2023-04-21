package grobid;

import java.util.ArrayList;

/**
 * Interface for author representation.
 *
 * User: Patrice Lopez
 *
 */
public interface Author {

    public String getFirstName();

    public void setFirstName(String f);

    public String getMiddleName();

    public void setMiddleName(String f);

    public String getLastName();

    public void setLastName(String f);

    public String getDisplayName();

    public void setDisplayName(String name);

    public String getTitle();

    public void setTitle(String f);

    public String getSuffix();

    public void setSuffix(String s);

    public ArrayList<String> getAffiliationBlocks();

    public void addAffiliationBlocks(String f);

    public ArrayList<Affiliation> getAffiliations();

    public void addAffiliation(Affiliation f);

    public ArrayList<String> getAffiliationMarkers();

    public void addAffiliationMarker(String s);

    public ArrayList<String> getMarkers();

    public void addMarker(String f);

    public String getEmail();

    public void setEmail(String f);

    public boolean getCorresp();

    public void setCorresp(boolean b);

    public boolean notNull();
}
