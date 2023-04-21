package net.sourceforge.jcv.controller.members.education;

import com.salmonllc.sql.DataStoreException;
import net.sourceforge.jcv.controller.common.expressions.DateExpression;
import net.sourceforge.jcv.controller.members.MembersBaseListController;
import net.sourceforge.jcv.model.EducationModel;
import net.sourceforge.jcv.util.PropsManager;
import java.sql.SQLException;

public class EducationListController extends MembersBaseListController {

    public com.salmonllc.html.HtmlText _lblRowStartDate;

    public com.salmonllc.html.HtmlText _lblRowEndDate;

    public EducationModel _dsEducation;

    public void listInitialize() throws Exception {
        _lblRowStartDate.setExpression(_dsEducation, new DateExpression(EducationModel.DEGREE_STARTDATE, getApplicationName()));
        _lblRowEndDate.setExpression(_dsEducation, new DateExpression(EducationModel.DEGREE_ENDDATE, getApplicationName()));
    }

    public String getDetailPage() {
        return PAGE_EDUCATION_DETAIL;
    }

    public String getRecordId(int row) throws DataStoreException {
        if (row == -1) return null; else return String.valueOf(_dsEducation.getDegreeDegreeid(row));
    }

    public void retrieveList() throws DataStoreException, SQLException {
        _dsEducation.retrieveByResumeId(getResumeId());
    }

    public String getDisplayBoxHeadingCaption() {
        return PropsManager.getLangValue(PROP_HEADING_CAPTION_EDUCATION_LIST, this);
    }
}
