package at.voctrainee.model.bo;

public class WordBo extends PersistableBo {

    private String name;

    public WordBo(String name) {
        System.out.println("Bo: Erstelle neues Wort: " + name);
        setName(name);
    }

    public void setName(String newname) {
        this.name = newname;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof WordBo)) {
            return false;
        }
        final WordBo other = (WordBo) object;
        if (!(name == null ? other.name == null : name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
}
