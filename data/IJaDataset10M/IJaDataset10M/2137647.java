package fr.vtt.gattieres.gcs.data;

public class CategoryData implements Comparable {

    private String code;

    private String name;

    private boolean custom;

    public CategoryData(String code, String name, boolean custom) throws InvalidDataException {
        this.setName(name);
        this.setCode(code);
        this.setCustom(custom);
    }

    public CategoryData(String code, String name) throws InvalidDataException {
        this(code, name, false);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) throws InvalidDataException {
        if (name == null || "".equals(name)) {
            throw new InvalidDataException("Category name cannot be null or empty");
        }
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) throws InvalidDataException {
        if (code == null || "".equals(code)) {
            throw new InvalidDataException("Category code cannot be null or empty");
        }
        this.code = code;
    }

    public int compareTo(Object o) {
        if (o == null || !(o instanceof CategoryData)) {
            return -1;
        }
        CategoryData c = (CategoryData) o;
        return this.name.compareTo(c.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof CategoryData)) {
            return false;
        }
        CategoryData c = (CategoryData) o;
        return this.code.equals(c.getCode());
    }

    @Override
    public String toString() {
        return this.getCode() + " > " + this.getName();
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }
}
