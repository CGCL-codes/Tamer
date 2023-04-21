package org.jugile.demo.domain;

import org.jugile.daims.Bo;
import org.jugile.daims.anno.*;
import org.jugile.util.Time;
import org.jugile.util.Money;
import org.jugile.demo.common.*;

@DaimsObject(table = "author_t")
public class AuthorBase extends Bo {

    @Fld(size = 300)
    private String name;

    public String getName() {
        return name;
    }

    public Author setName(String v) {
        return (Author) setFld("name", v);
    }

    @Fld(size = 300)
    private String key;

    public String getKey() {
        return key;
    }

    public Author setKey(String v) {
        return (Author) setFld("key", v);
    }

    @Connection1N(o = "authorMain")
    private BookCollection cMainBooks = new BookCollection();

    public BookCollection getMainBooks() {
        return (BookCollection) getAll("cMainBooks");
    }

    public Author addMainBook(Book o) {
        add("cMainBooks", o);
        return (Author) this;
    }

    public void removeMainBook(Book o) {
        remove("cMainBooks", o);
    }

    @Connection1N(o = "authorCo")
    private BookCollection cCoBooks = new BookCollection();

    public BookCollection getCoBooks() {
        return (BookCollection) getAll("cCoBooks");
    }

    public Author addCoBook(Book o) {
        add("cCoBooks", o);
        return (Author) this;
    }

    public void removeCoBook(Book o) {
        remove("cCoBooks", o);
    }

    @Connection1N(o = "author")
    private VisitCollection cVisits = new VisitCollection();

    public VisitCollection getVisits() {
        return (VisitCollection) getAll("cVisits");
    }

    public Author addVisit(Visit o) {
        add("cVisits", o);
        return (Author) this;
    }

    public void removeVisit(Visit o) {
        remove("cVisits", o);
    }

    @Connection1N(o = "authorOld")
    private VisitCollection cOldVisits = new VisitCollection();

    public VisitCollection getOldVisits() {
        return (VisitCollection) getAll("cOldVisits");
    }

    public Author addOldVisit(Visit o) {
        add("cOldVisits", o);
        return (Author) this;
    }

    public void removeOldVisit(Visit o) {
        remove("cOldVisits", o);
    }

    protected Domain d() {
        return Domain.getDomain();
    }
}
