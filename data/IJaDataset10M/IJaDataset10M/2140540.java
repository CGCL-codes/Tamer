package com.google.code.openperfmon.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MonitorDefinition extends AbstractEntity implements Comparable<MonitorDefinition> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = true)
    private String inputVariables;

    @Column(nullable = false)
    private String body;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInputVariables() {
        return inputVariables;
    }

    public void setInputVariables(String inputVariables) {
        this.inputVariables = inputVariables;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int compareTo(MonitorDefinition o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object obj) {
        return name.equals(((MonitorDefinition) obj).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
