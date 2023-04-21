package cw.coursemanagementmodul.extention;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import cw.accountmanagementmodul.pojo.AccountPosting;
import cw.coursemanagementmodul.pojo.CoursePosting;
import cw.coursemanagementmodul.pojo.manager.CoursePostingManager;

/**
 *
 * @author André Salmhofer (CreativeWorkers)
 */
public class CoursePostingEditReversePostingPostingCategoryExtention implements EditReversePostingPostingCategoryExtentionPoint {

    private EditReversePostingPresentationModel reversePostingModel;

    private CoursePosting coursePosting;

    public void initPresentationModel(EditReversePostingPresentationModel editReversePostingModel) {
        reversePostingModel = editReversePostingModel;
        AccountPosting p = reversePostingModel.getPostingPresentationModel().getBean();
        if (p.isBalancePosting()) {
            p = p.getPreviousPosting();
        }
        coursePosting = CoursePostingManager.getInstance().get(p);
    }

    public JComponent getView() {
        return new JLabel(coursePosting.getCourseAddition().getCourse().getName());
    }

    public void save() {
        CoursePosting coursePostingNew = new CoursePosting();
        coursePostingNew.setCourseAddition(coursePosting.getCourseAddition());
        coursePostingNew.setPosting(reversePostingModel.getReversePostingPresentationModel().getBean());
        CoursePostingManager.getInstance().save(coursePostingNew);
    }

    public List<String> validate() {
        return new ArrayList();
    }

    public String getKey() {
        return "Kurs-Buchung";
    }

    public void dispose() {
    }
}
