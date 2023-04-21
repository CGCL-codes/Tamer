package ee.fctwister.cpanel.DTO;

import java.io.Serializable;

public class FixtureTypesDTO implements Serializable {

    private static final long serialVersionUID = 639860023393294698L;

    private int fixtureTypeID;

    private String fixtureTypeName;

    private int fixtureTypeGroupID;

    public int getFixtureTypeID() {
        return fixtureTypeID;
    }

    public void setFixtureTypeID(int fixtureTypeID) {
        this.fixtureTypeID = fixtureTypeID;
    }

    public String getFixtureTypeName() {
        return fixtureTypeName;
    }

    public void setFixtureTypeName(String fixtureTypeName) {
        this.fixtureTypeName = fixtureTypeName;
    }

    public int getFixtureTypeGroupID() {
        return fixtureTypeGroupID;
    }

    public void setFixtureTypeGroupID(int fixtureTypeGroupID) {
        this.fixtureTypeGroupID = fixtureTypeGroupID;
    }
}
