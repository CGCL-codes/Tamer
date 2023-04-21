package uk.ac.imperial.ma.metric.blackboardIntegration;

import java.rmi.RemoteException;
import com.webct.platform.sdk.gradebook.MemberAttrValueVO;
import com.webct.platform.sdk.gradebook.SectionColumnVO;
import com.webct.platform.sdk.gradebook.exceptions.MemberGradeBookException;

public class GradeBookColumn {

    private SectionColumnVO sectionColumnVO;

    private GradeBook gradeBook;

    protected GradeBookColumn(final SectionColumnVO sectionColumnVO, final GradeBook gradeBook) {
        this.sectionColumnVO = sectionColumnVO;
        this.gradeBook = gradeBook;
    }

    public long getId() {
        return sectionColumnVO.getId();
    }
}
