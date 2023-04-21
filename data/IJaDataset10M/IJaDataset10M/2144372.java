package org.jopenray.server.user;

import java.util.ArrayList;
import java.util.List;
import org.jopenray.server.card.Card;

public class User {

    private String firstName;

    private String lastName;

    private final List<Card> cards = new ArrayList<Card>();

    private int id;

    private static int nextId = 0;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = nextId;
        nextId++;
    }

    public User(int userId, String firstName, String lastName) {
        this.id = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User u = (User) obj;
            return u.id == this.id;
        }
        return super.equals(obj);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return this.getFirstName() + " " + this.getLastName();
    }

    public void setFirstName(String text) {
        text = text.trim();
    }

    public void setLastName(String text) {
        text = text.trim();
    }
}
