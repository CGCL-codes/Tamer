package org.magicdroid.app.ui;

import org.magicdroid.app.common.UIBinding;
import org.magicdroid.commons.Collect.Renderer;
import org.magicdroid.model.Account;
import org.magicdroid.model.Category;
import org.magicdroid.model.Expense;
import org.magicdroid.model.Person;

public interface EntityRenderers {

    Renderer<Person> PERSON = new Renderer<Person>() {

        @Override
        public String format(Person input) {
            return input.getName();
        }
    };

    Renderer<Person> PERSON_LONG = PERSON;

    Renderer<Category> CATEGORY = new Renderer<Category>() {

        @Override
        public String format(Category input) {
            return input.getName();
        }
    };

    Renderer<Category> CATEGORY_LONG = CATEGORY;

    Renderer<Account> ACCOUNT = new Renderer<Account>() {

        @Override
        public String format(Account input) {
            return input.getNumber();
        }
    };

    Renderer<Account> ACCOUNT_LONG = new Renderer<Account>() {

        @Override
        public String format(Account input) {
            return input.getNumber() + " " + input.getDescription();
        }
    };

    Renderer<Expense> EXPENSE_LONG = new Renderer<Expense>() {

        @Override
        public String format(Expense element) {
            String description = element.getDescription();
            if (description == null) description = "";
            description = description.toString();
            String amountLabel = "";
            Double amount = element.getAmount();
            if (amount != null) amountLabel = UIBinding.Bindings.DOUBLE.format(amount);
            return description + " " + amountLabel;
        }
    };
}
