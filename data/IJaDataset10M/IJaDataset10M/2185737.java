package sql;

import java.util.Date;

public class Cat {

    private String name;

    private Date birthDate;

    private String color;

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birth) {
        this.birthDate = birth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String string) {
        this.color = string;
    }

    public Object getColor() {
        return color;
    }
}
