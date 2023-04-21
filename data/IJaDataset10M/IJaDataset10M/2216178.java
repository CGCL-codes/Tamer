package delphorm.entite.questionnaire;

import java.util.ArrayList;
import java.util.List;

public class InstanceQuestion {

    private Question question;

    private List instancesReponse = new ArrayList();

    private Long id;

    private Integer place;

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<InstanceReponse> getInstancesReponse() {
        return instancesReponse;
    }

    public void setInstancesReponse(List instancesReponse) {
        this.instancesReponse = instancesReponse;
    }
}
