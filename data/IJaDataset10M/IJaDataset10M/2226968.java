package ru.spbspu.staub.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The <code>TestStatistics</code> class represents the TestStatistics entity.
 *
 * @author Alexander V. Elagin
 */
@Entity
@Table(schema = "staub", name = "test_statistics")
public class TestStatistics implements Serializable {

    private static final long serialVersionUID = 4596809351103608580L;

    private Integer fkTest;

    private Test test;

    private Integer totalAnswers;

    private Integer correctAnswers;

    private Integer correctAnswersPercent;

    private Date lastUpdate;

    @Id
    @Column(name = "fk_test", nullable = false, length = 10)
    public Integer getFkTest() {
        return fkTest;
    }

    public void setFkTest(Integer fkTest) {
        this.fkTest = fkTest;
    }

    @OneToOne
    @JoinColumn(name = "fk_test", referencedColumnName = "id", insertable = false, updatable = false)
    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    @Basic
    @Column(name = "total_answers", length = 10)
    public Integer getTotalAnswers() {
        return totalAnswers;
    }

    public void setTotalAnswers(Integer totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    @Basic
    @Column(name = "correct_answers", length = 10)
    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    @Basic
    @Column(name = "correct_answers_pc", length = 10)
    public Integer getCorrectAnswersPercent() {
        return correctAnswersPercent;
    }

    public void setCorrectAnswersPercent(Integer correctAnswersPercent) {
        this.correctAnswersPercent = correctAnswersPercent;
    }

    @Basic
    @Column(name = "last_update")
    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestStatistics)) {
            return false;
        }
        TestStatistics that = (TestStatistics) o;
        return fkTest.equals(that.fkTest);
    }

    @Override
    public int hashCode() {
        return fkTest.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TestStatistics");
        sb.append("{test=").append(test);
        sb.append(", totalAnswers=").append(totalAnswers);
        sb.append(", correctAnswers=").append(correctAnswers);
        sb.append(", correctAnswersPercent=").append(correctAnswersPercent);
        sb.append(", lastUpdate=").append(lastUpdate);
        sb.append('}');
        return sb.toString();
    }
}
