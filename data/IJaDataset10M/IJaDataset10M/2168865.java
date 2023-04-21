package ee.fctwister.euro2008.pages;

import java.util.ArrayList;
import org.apache.tapestry.html.BasePage;
import ee.fctwister.euro2008.DAO.TableDAO;
import ee.fctwister.euro2008.DTO.FixtureDTO;

public abstract class InsertResult extends BasePage {

    public abstract String getFixtureSelected();

    public abstract void setFixtureSelected(String fixture);

    public ArrayList<FixtureDTO> getFixtureListSource() {
        try {
            return TableDAO.getAllFixtures();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<FixtureDTO>();
        }
    }
}
