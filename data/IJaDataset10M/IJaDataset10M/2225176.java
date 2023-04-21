package org.pulloid;

public class Item {

    public int id;

    public String title;

    public String date;

    public Author authorObj;

    public String authorName;

    public String authorDateOfBirth;

    public static class Author {

        public String name;

        public String dateOfBirth;
    }
}
