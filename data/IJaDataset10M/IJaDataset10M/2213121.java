package struts.forms;

import java.util.Arrays;
import models.AnswerOfAttempt;
import models.Choose;
import org.apache.struts.action.ActionForm;

public class ChooseForm extends ActionForm {

    private AnswerOfAttempt[] aoas;

    private Choose choose;

    public Choose getChoose() {
        return choose;
    }

    public void setChoose(Choose choose) {
        this.choose = choose;
    }

    public int[] getTypes() {
        return new int[0];
    }

    public void setTypes(int[] types) {
        int examType = 0;
        for (int i = 0; i < types.length; i++) {
            examType += types[i];
        }
        choose.setExamType(examType);
    }

    public void setAttCon(int con) {
        aoas = new AnswerOfAttempt[con];
        System.out.println("sdddsdsddsd�εδ��Ķ���" + " " + con);
        for (int i = 0; i < aoas.length; i++) {
            aoas[i] = new AnswerOfAttempt();
        }
    }

    public AnswerOfAttempt[] getAnswers() {
        return aoas;
    }

    public void setAnswers(AnswerOfAttempt[] answers) {
        this.aoas = answers;
    }

    public int getAoas() {
        return 0;
    }

    public void setAoas(AnswerOfAttempt[] aoas) {
        this.aoas = aoas;
    }

    public void setAoas(int n) {
        System.out.println("�εδ��Ķ����Ķ���" + " " + n);
        for (AnswerOfAttempt aoa : aoas) {
            System.out.println("Ц����ϵЦ���� " + " " + " " + aoa + n++);
            choose.addAnswerOfList(aoa);
        }
    }

    public String getSubIN() {
        return choose.getSubjectId() + "@" + choose.getSubName();
    }

    public void setSubIN(String subIN) {
        String[] subIAN = subIN.split("@");
        choose.setSubjectId(Long.parseLong(subIAN[0]));
        choose.setSubName(subIAN[1]);
    }

    public String getPaprIN() {
        return choose.getPaprId() + "@" + choose.getPaprName();
    }

    public void setPaprIN(String paprIN) {
        String[] paprIAN = paprIN.split("@");
        choose.setPaprId(Long.parseLong(paprIAN[0]));
        choose.setPaprName(paprIAN[1]);
    }
}
