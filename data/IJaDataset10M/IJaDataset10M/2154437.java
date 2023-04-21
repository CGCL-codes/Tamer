package models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ���Һ� ��Ŀ��
 */
public class Subject {

    private Long id;

    private String subjectOfName;

    private List<ExaminationOfPaper> paperOfList = new ArrayList<ExaminationOfPaper>();

    public Subject() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setPaperOfList(List<ExaminationOfPaper> paperOfList) {
        this.paperOfList = paperOfList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectOfName() {
        return subjectOfName;
    }

    public void setSubjectOfName(String subjectOfName) {
        this.subjectOfName = subjectOfName;
    }

    public List<ExaminationOfPaper> getPaperOfList() {
        return paperOfList;
    }

    public void addPaper(ExaminationOfPaper paper) {
        paperOfList.add(paper);
        paper.setSubject(this);
    }

    public void removePaper(ExaminationOfPaper paper) {
        paperOfList.remove(paper);
        paper.setSubject(null);
    }
}
