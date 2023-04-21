package org.jsizzle.examples.helpdesk;

import java.util.List;

public interface Helpdesk {

    Person addCustomer(String name);

    Person addAnalyst(String name);

    Issue addIssue(Person customer, Person analyst);

    List<? extends Issue> getAnalystOpenIssues(Person analyst);
}
