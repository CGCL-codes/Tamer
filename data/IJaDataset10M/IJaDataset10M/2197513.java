package org.xorm.tests.xml;

public interface Book {

    public String getTitle();

    public void setTitle(String title);

    public String getAuthor();

    public void setAuthor(String author);

    public String getDescription();

    public void setDescription(String description);

    public Library getLibrary();

    public void setLibrary(Library library);
}
