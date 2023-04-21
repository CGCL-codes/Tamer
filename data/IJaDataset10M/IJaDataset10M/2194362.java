package remote.entities.nnodes.utils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

/**
 * @author Kasza Miklós
 */
@Entity
public class StringPairEnt {

    private Long _id;

    private String _firstString;

    private String _secondString;

    public StringPairEnt() {
    }

    public StringPairEnt(String first, String second) {
        _firstString = first;
        _secondString = second;
    }

    public String getFirstString() {
        return _firstString;
    }

    @TableGenerator(name = "stringPairEntIdGenerator", table = "IdGenerator", pkColumnName = "IdKey", valueColumnName = "IdValue", pkColumnValue = "NSStringPairEnt")
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "stringPairEntIdGenerator")
    public Long getId() {
        return _id;
    }

    public String getSecondString() {
        return _secondString;
    }

    public void setFirstString(String first) {
        _firstString = first;
    }

    public void setId(Long id) {
        _id = id;
    }

    public void setSecondString(String second) {
        _secondString = second;
    }
}
