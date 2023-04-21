package org.eledge.pages;

import static org.eledge.Eledge.created;
import static org.eledge.Eledge.dbcommit;
import static org.eledge.Eledge.discardChanges;
import java.util.List;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.eledge.components.ClassSelectionModel;
import org.eledge.domain.Assignment;
import org.eledge.domain.AssignmentQuestion;
import org.eledge.domain.QuestionBank;
import org.eledge.domain.auto._QuestionBank;

/**
 * @author robertz
 * 
 */
public abstract class EditAssignmentQuestions extends EledgeSecureBasePage {

    public abstract AssignmentQuestion getCurrentQuestion();

    public abstract String getNewQuestionType();

    public abstract void setAssignment(Assignment a);

    public abstract Assignment getAssignment();

    public abstract int getQidx();

    public abstract String getReturnPage();

    public abstract String getPermissionName();

    private IPropertySelectionModel qTypesModel;

    public Block getPreviewBlock() {
        IPage page = getRequestCycle().getPage(getCurrentQuestion().getQuestionPageName());
        return (Block) page.getComponent(getCurrentQuestion().getPreviewBlockName());
    }

    public Block getEditBlock() {
        IPage page = getRequestCycle().getPage(getCurrentQuestion().getQuestionPageName());
        return (Block) page.getComponent(getCurrentQuestion().getEditBlockName());
    }

    public void addQuestionListener(IRequestCycle cycle) {
        String cname = getNewQuestionType();
        int numQuestions;
        try {
            numQuestions = ((Integer) getProperty("numNewQs")).intValue();
        } catch (Exception e) {
            numQuestions = 1;
        }
        setProperty("numNewQs", new Integer(1));
        try {
            for (int i = 0; i < numQuestions; i++) {
                AssignmentQuestion question = (AssignmentQuestion) Class.forName(cname).newInstance();
                created(question);
                question.setAssignment(getAssignment());
                QuestionBank bank;
                if (getAssignment().getQuestionBanks().isEmpty()) {
                    bank = QuestionBank.createBank(getAssignment(), "Default", 10);
                } else if (getAssignment().getQuestionBanks().size() == 1) {
                    bank = getAssignment().getQuestionBanks().get(0);
                } else {
                    List<?> results = ExpressionFactory.matchExp(_QuestionBank.NAME_PROPERTY, "default").filterObjects(getAssignment().getQuestionBanks());
                    if (results.isEmpty()) {
                        bank = getAssignment().getQuestionBanks().get(0);
                    } else {
                        bank = (QuestionBank) results.get(0);
                    }
                }
                question.setQuestionBank(bank);
                question.initializeNewQuestion();
            }
            dbcommit(cycle);
        } catch (PageRedirectException pe) {
            throw pe;
        } catch (Exception e) {
            discardChanges();
            e.printStackTrace();
            throw new ApplicationRuntimeException(e);
        }
    }

    public void formFinished(IRequestCycle cycle) {
        if (getCurrentQuestion() != null) {
            setAssignment(getCurrentQuestion().getAssignment());
        }
    }

    public IPropertySelectionModel getQuestionTypesModel() {
        if (qTypesModel == null) {
            qTypesModel = new ClassSelectionModel(getRequestCycle(), "question-types", "org.eledge.domain.questiontypes", getAssignment());
        }
        return qTypesModel;
    }

    public int getNumq() {
        return getAssignment().getQuestions().size();
    }

    public String getTitle() {
        return getAssignment().getTitle();
    }

    public String getQname() {
        return "q" + getQidx();
    }

    public String getFid() {
        return "F" + getQidx();
    }

    public boolean getHasReturnPage() {
        return getReturnPage() != null;
    }

    public boolean getHasPermissionName() {
        return getPermissionName() != null;
    }
}
