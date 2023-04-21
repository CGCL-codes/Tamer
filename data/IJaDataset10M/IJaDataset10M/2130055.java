package model.expense;

import message.MessageId;
import validation.ModelValidation;

public class ExpenseArticle {

    private String name;

    public ExpenseArticle(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        ModelValidation.instance().assertNotBlank(name, MessageId.name);
        this.name = name;
    }

    public String toString() {
        return getName();
    }
}
