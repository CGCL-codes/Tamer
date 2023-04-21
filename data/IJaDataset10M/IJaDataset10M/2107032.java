package ru.spbspu.staub.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The <code>TestDifficultyPK</code> class represents a primary key of the {@link TestDifficulty} entity.
 *
 * @author Alexander V. Elagin
 */
@Embeddable
public class TestDifficultyPK implements Serializable {

    private static final long serialVersionUID = 6673970679487117717L;

    private Integer fkTest;

    private Integer fkDifficulty;

    public TestDifficultyPK() {
    }

    public TestDifficultyPK(Integer fkTest, Integer fkDifficulty) {
        this.fkTest = fkTest;
        this.fkDifficulty = fkDifficulty;
    }

    @Column(name = "fk_test", nullable = false)
    public Integer getFkTest() {
        return fkTest;
    }

    public void setFkTest(Integer fkTest) {
        this.fkTest = fkTest;
    }

    @Column(name = "fk_difficulty", nullable = false)
    public Integer getFkDifficulty() {
        return fkDifficulty;
    }

    public void setFkDifficulty(Integer fkDifficulty) {
        this.fkDifficulty = fkDifficulty;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof TestDifficultyPK)) {
            return false;
        }
        TestDifficultyPK other = (TestDifficultyPK) otherObject;
        return fkDifficulty.equals(other.fkDifficulty) && fkTest.equals(other.fkTest);
    }

    @Override
    public int hashCode() {
        int result;
        result = fkTest.hashCode();
        result = 31 * result + fkDifficulty.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TestDifficultyPK");
        sb.append("{fkTest=").append(fkTest);
        sb.append(", fkDifficulty=").append(fkDifficulty);
        sb.append('}');
        return sb.toString();
    }
}
