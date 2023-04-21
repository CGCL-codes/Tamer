package com.adobe.ac.samples.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import de.aixcept.flex2.annotations.ActionScript;
import de.aixcept.flex2.annotations.ActionScriptProperty;

@ActionScript(bindable = true, remoteObject = true)
@Entity(name = "todoitem")
public class TodoItem implements Serializable {

    private static final long serialVersionUID = 5750553524796306885L;

    private Integer id;

    private String title;

    @ActionScriptProperty(read = true, write = true, bindable = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ActionScriptProperty(read = true, write = true, bindable = true)
    @Column(name = "title", nullable = false, length = 50)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
