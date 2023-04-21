package pl.zgora.uz.issi.listbeans;

import java.util.List;
import javax.persistence.EntityManager;
import model.ReviewerArea;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

@Name("reviewerAreaListBean")
public class ReviewerAreaListBean {

    @In
    EntityManager entityManager;

    @DataModel
    private List<ReviewerArea> reviewerAreaDataModel;

    @DataModelSelection
    private ReviewerArea countrySelected;

    @Factory("reviewerAreaDataModel")
    public void findCountry() {
        reviewerAreaDataModel = entityManager.createQuery("from ReviewerArea").getResultList();
    }

    public void delete(Integer id) {
        ReviewerArea obj = entityManager.find(ReviewerArea.class, id);
        entityManager.remove(obj);
        System.out.println("reviewerAreaSelected = " + id + "this = " + this);
    }
}
