package net.sf.buildbox.devmodel;

import net.sf.buildbox.buildrobot.model.Identifiable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic property, with {@link #getName() name} representing certain semantics, and carrying a {@link #getValues() list of values}.
 */
public class Detail extends Identifiable {

    static final long serialVersionUID = 20101023;

    private String name;

    private String detailType = String.class.getName();

    private List<String> values = new ArrayList<String>();

    /**
     * @return identification of the detail. Each name has globally defined semantics //todo: the definition needs to be elaborated, maybe defined as a restriction
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return java class indicating type of each {@link #getValues value}
     */
    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    /**
     * @return list of values for this detail
     */
    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public void addValue(String value) {
        values.add(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Detail)) return false;
        Detail detail = (Detail) o;
        if (!detailType.equals(detail.detailType)) return false;
        if (!name.equals(detail.name)) return false;
        if (!values.equals(detail.values)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + detailType.hashCode();
        result = 31 * result + values.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Detail");
        sb.append("{detailType='").append(detailType).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", values=").append(values);
        sb.append('}');
        return sb.toString();
    }
}
