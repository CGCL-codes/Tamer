package de.fhbrs.litsearch;

import com.softtech.jdbc.SQLResults;
import java.lang.String;
import java.util.regex.Pattern;

/**
 * Parse search strings and query the database.
 *
 * @version 	1.2 23 Oct 2006
 * @author 	Christian Clever, Marco Werner
 * 
 */
public class MediumBean {

    private int id;

    private String[] author;

    private String authorString;

    private String signatur;

    private String edition;

    private String editor;

    private String year;

    private String title1;

    private String title2;

    private String institute;

    private String address;

    private String shelf;

    private String vol;

    private String room;

    private String remarks;

    private String page;

    private String bibtexkuerzel;

    private String publisher;

    private String journal;

    private String litart;

    private String isbn;

    private String issn;

    private String inventorynumber;

    private boolean released;

    private boolean old;

    private LanguageBean langObject;

    private UserBean userObject;

    private int editFieldWidth;

    /**
	 * Constructor. Fills only the form field width var. 
	 *
	 */
    public MediumBean() {
        this.editFieldWidth = 65;
    }

    /** 
     * Fills the Variables with data from database.
     * @param Id as an String (given as an Request Parameter from a JSP
     */
    public void initializeMedium(String idString) {
        this.setId(Integer.parseInt(idString));
        JDBCBean dbCon = new JDBCBean();
        SQLResults res = dbCon.getResultSet("select * from literatur where id=" + id + "");
        this.setAuthorString(res.getString(0, "author"));
        this.setSignatur(res.getString(0, "signatur"));
        this.setEdition(res.getString(0, "edition"));
        this.setEditor(res.getString(0, "editor"));
        this.setYear(res.getString(0, "year"));
        this.setTitle1(res.getString(0, "title1"));
        this.setTitle2(res.getString(0, "title2"));
        this.setInstitute(res.getString(0, "institute"));
        this.setAddress(res.getString(0, "address"));
        this.setShelf(res.getString(0, "shelf"));
        this.setIsbn(res.getString(0, "isbn"));
        this.setIssn(res.getString(0, "issn"));
        this.setVol(res.getString(0, "vol"));
        this.setRoom(res.getString(0, "room"));
        this.setRemarks(res.getString(0, "remarks"));
        this.setInventorynumber(res.getString(0, "inventorynumber"));
        this.setPage(res.getString(0, "page"));
        this.setLitart(res.getString(0, "litart"));
        this.setReleased(res.getBoolean(0, "released"));
        this.setOld(res.getBoolean(0, "old"));
        this.setBibtexkuerzel(res.getString(0, "bibtexkuerzel"));
        this.setPublisher(res.getString(0, "publisher"));
        this.setJournal(res.getString(0, "journal"));
    }

    /** 
     * Markes the database-entry with current id as marked (eg. this.deleteMedium()).
     */
    public void deleteMedium() {
        JDBCBean dbCon = new JDBCBean();
        dbCon.executeQuery("update literatur set deleted=1 where id=" + this.getId() + "");
    }

    /** 
     * Fills the Variables with empty Strings.
     */
    public void initializeNewMedium() {
        this.setAuthor(this.convertToArray("\"\""));
        this.setSignatur("");
        this.setEdition("");
        this.setEditor("");
        this.setYear("");
        this.setTitle1("");
        this.setTitle2("");
        this.setInstitute("");
        this.setAddress("");
        this.setShelf("");
        this.setVol("");
        this.setRemarks("");
        this.setInventorynumber("");
        this.setIsbn("");
        this.setIssn("");
        this.setPage("");
        this.setLitart("");
        this.setReleased(false);
        this.setOld(false);
        this.setBibtexkuerzel("");
        this.setPublisher("");
        this.setJournal("");
    }

    public void setLanguage(LanguageBean object) {
        this.langObject = object;
    }

    /**
	 * Returns String "selected" if first parameter equals second parameter 
	 * @param a
	 * @param b
	 * @return 
	 */
    public String isSelected(String a, String b) {
        return a.equals(b) ? " selected=\"selected\"" : "";
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getAddress() {
        String myWert = this.address;
        String myFieldName = "address";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /**
	 * Returns the Address
	 * @return
	 */
    public String getAddressPrint() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address.trim();
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getBibtexkuerzel() {
        String myWert = this.bibtexkuerzel;
        String myFieldName = "bibtexkuerzel";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /**
	 * Return the bibtex shortcut
	 * @return
	 */
    public String getBibtexkuerzelPrint() {
        return this.bibtexkuerzel;
    }

    public void setBibtexkuerzel(String bibtexkuerzel) {
        this.bibtexkuerzel = bibtexkuerzel.trim();
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getEditor() {
        String myWert = this.editor;
        String myFieldName = "editor";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /**
	 * Return the editor
	 * @return
	 */
    public String getEditorPrint() {
        return this.editor;
    }

    public void setEditor(String editor) {
        this.editor = editor.trim();
    }

    public String getEdition() {
        String myWert = this.edition;
        String myFieldName = "edition";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    public String getEditionPrint() {
        return this.edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getInstitute() {
        String myWert = this.institute;
        String myFieldName = "institute";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /**
	 * Returns the institute
	 * @return
	 */
    public String getInstitutePrint() {
        return this.institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute.trim();
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html select  field.
	 * @return
	 */
    public String getJournal() {
        String myWert = this.journal;
        String myReturn = "";
        String myFieldName = "journal";
        if (this.getUserObject().isEditor()) {
            JDBCBean dbCon = new JDBCBean();
            SQLResults res = dbCon.getResultSet("select * from journals ");
            myReturn += "<select name=\"" + myFieldName + "\">";
            myReturn += "<option value=\"clearme\"" + this.isSelected(myWert, "") + ">--- nicht gesetzt ---</option>";
            for (int i = 0; i < res.getRowCount(); i++) {
                String name = res.getString(i, "name");
                String shortcut = res.getString(i, "shortcut");
                myReturn += "<option value=\"" + shortcut + "\"" + this.isSelected(myWert, shortcut) + ">" + name + "</option>";
            }
            myReturn += "</select>";
            myReturn += "<span class=\"oldshortcut\">" + myWert + "</span>";
            return myReturn;
        } else {
            return myWert;
        }
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html select  field.
	 * @return
	 */
    public String getJournalNew() {
        String myReturn = "";
        String myFieldName = "journal";
        JDBCBean dbCon = new JDBCBean();
        SQLResults res = dbCon.getResultSet("select * from journals ");
        myReturn += "<select name=\"" + myFieldName + "\">";
        myReturn += "<option value=\"\" selected>--- Bitte ausw&auml;hlen ---</option>";
        for (int i = 0; i < res.getRowCount(); i++) {
            String name = res.getString(i, "name");
            String shortcut = res.getString(i, "shortcut");
            myReturn += "<option value=\"" + shortcut + "\">" + name + "</option>";
        }
        myReturn += "</select>";
        return myReturn;
    }

    /**
	 * Return the journal
	 * @return
	 */
    public String getJournalPrint() {
        return this.journal;
    }

    public void setJournal(String journal) {
        if (journal.equals("clearme")) this.journal = ""; else this.journal = journal.trim();
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html select field.
	 * @return
	 */
    public String getLitart() {
        String myWert = this.litart;
        String myFieldName = "litart";
        String myReturn = "";
        if (this.getUserObject().isEditor()) {
            myReturn += "<select name=\"" + myFieldName + "\">";
            myReturn += "<option value=\"artikel\"" + this.isSelected(myWert, "artikel") + ">Artikel";
            myReturn += "<option value=\"greylit\"" + this.isSelected(myWert, "greylit") + ">Graue Literatur";
            myReturn += "<option value=\"book\"" + this.isSelected(myWert, "book") + ">Buch";
            myReturn += "</select>";
        } else {
            myReturn = myWert;
        }
        return myReturn;
    }

    /**
	 * Return the kind of literature of give String. (Book, Artikle oder Grey Literateratu) depending on current language
	 * @param typ
	 * @return
	 */
    public String getLitartPrint(String typ) {
        String myReturn = "";
        if (typ.equals("book")) myReturn = this.langObject.getTextFor("export.book"); else if (typ.equals("artikel")) myReturn = this.langObject.getTextFor("export.article"); else if (typ.equals("greylit")) myReturn = this.langObject.getTextFor("export.greylit");
        return myReturn;
    }

    /***
	 * Returns the kind of literature of the current media. Result depending on current language
	 * @return
	 */
    public String getLitartPrint() {
        String myReturn = "";
        if (this.litart.equals("book")) myReturn = this.langObject.getTextFor("export.book"); else if (this.litart.equals("artikel")) myReturn = this.langObject.getTextFor("export.article"); else if (this.litart.equals("greylit")) myReturn = this.langObject.getTextFor("export.greylit");
        return myReturn;
    }

    /***
	 * Returns the kind of literature of the current media. Result depending on current language
	 * @return
	 */
    public String getLitartExport() {
        return this.litart;
    }

    public void setLitart(String litart) {
        this.litart = litart.trim();
    }

    /**
	 * Return true, if given String is a valid ISBN Number
	 * @param eingabe
	 * @return
	 */
    public boolean isbnTest(String eingabe) {
        if (eingabe.length() != 10) return false;
        String isbn = eingabe;
        int ergebnis = 0;
        for (int i = 0; i < 9; i++) {
            int j = (int) (isbn.charAt(i)) - 48;
            ergebnis += ((j * (10 - i)) % 11);
        }
        ergebnis = 11 - (ergebnis % 11);
        if ((int) isbn.charAt(9) - 48 == ergebnis) return true; else return false;
    }

    /**
	 * Writes all Medium-Variables to Database by updating an existing record. Only works, if given String equals "yes"
	 * @return
	 */
    public void saveMedium(String save) {
        String update = "";
        if (save == null) return;
        if (!this.getUserObject().isPublisher()) this.setReleased(false);
        this.setAuthorString(this.convertToString(this.author));
        if (save.equals("yes") && this.userObject.isEditor()) {
            update += "update literatur set";
            update += " author='" + replaceAll(this.authorString, "\"", "\\\"") + "',";
            update += " edition='" + this.edition + "',";
            update += " editor='" + this.editor + "',";
            update += " signatur='" + this.signatur + "',";
            update += " title1='" + this.title1 + "',";
            update += " year='" + this.year + "',";
            update += " title2='" + this.title2 + "',";
            update += " institute='" + this.institute + "',";
            update += " address='" + this.address + "',";
            update += " shelf='" + this.shelf + "',";
            update += " vol='" + this.vol + "',";
            update += " isbn='" + this.isbn + "',";
            update += " issn='" + this.issn + "',";
            update += " room='" + this.room + "',";
            update += " remarks='" + this.remarks + "',";
            update += " inventorynumber='" + this.inventorynumber + "',";
            update += " page='" + this.page + "',";
            update += " bibtexkuerzel='" + this.bibtexkuerzel + "',";
            update += " publisher='" + this.publisher + "',";
            update += " journal='" + this.journal + "',";
            update += " litart='" + this.litart + "',";
            update += " old=" + this.old + ",";
            update += " released=" + this.released + "";
            update += " where id='" + this.getId() + "'";
            JDBCBean dbCon = new JDBCBean();
            dbCon.getResultSet(update);
        } else {
        }
    }

    /**
	 * Writes current Variables to database by creating a new record
	 *
	 */
    public void insertMedium() {
        String insert = "";
        String released = this.getUserObject().isPublisher() ? "1" : "0";
        insert += "insert into literatur (author,edition,editor,title1,year,signatur,title2,institute," + "address,shelf,vol,remarks,inventorynumber,isbn,issn,room,page,bibtexkuerzel,publisher," + "journal,litart,old,released) ";
        insert += "values (";
        insert += "'" + replaceAll(this.convertToString(this.getAuthor()), "\"", "\\\"") + "',";
        insert += "'" + this.edition + "',";
        insert += "'" + this.editor + "',";
        insert += "'" + this.title1 + "',";
        insert += "'" + this.year + "',";
        insert += "'" + this.signatur + "',";
        insert += "'" + this.title2 + "',";
        insert += "'" + this.institute + "',";
        insert += "'" + this.address + "',";
        insert += "'" + this.shelf + "',";
        insert += "'" + this.vol + "',";
        insert += "'" + this.remarks + "',";
        insert += "'" + this.inventorynumber + "',";
        insert += "'" + this.isbn + "',";
        insert += "'" + this.issn + "',";
        insert += "'" + this.room + "',";
        insert += "'" + this.page + "',";
        insert += "'" + this.bibtexkuerzel + "',";
        insert += "'" + this.publisher + "',";
        insert += "'" + this.journal + "',";
        insert += "'" + this.litart + "',";
        insert += "'0',";
        insert += released;
        insert += ")";
        JDBCBean dbCon = new JDBCBean();
        dbCon.getResultSet(insert);
    }

    /**
	 * Replaces substring in strings
	 * @param String. the whole String
	 * @param String search. Substring to replace
	 * @param String replace. Substringreplacement
	 * @return String with replaces ubstrings
	 */
    public String replace(String s, String search, String replace) {
        int start = 0, pos = 0;
        StringBuffer result = new StringBuffer(s.length());
        while ((pos = s.indexOf(search, start)) >= 0) {
            result.append(s.substring(start, pos));
            result.append(replace);
            start = pos + search.length();
        }
        result.append(s.substring(start));
        return result.toString();
    }

    /**
	 * Returns true, if the record is an old one.( is not updated yet)
	 * @return
	 */
    public boolean getOld() {
        return old;
    }

    public void setOld(boolean old) {
        this.old = old;
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getInventorynumber() {
        String myWert = this.inventorynumber;
        String myFieldName = "inventorynumber";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /**
	 * returns the Content of the page variable
	 * @return
	 */
    public String getInventoryNnumberPrint() {
        return this.inventorynumber;
    }

    public void setInventorynumber(String in) {
        this.inventorynumber = in.trim();
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getPage() {
        String myWert = this.page;
        String myFieldName = "page";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /**
	 * returns the Content of the page variable
	 * @return
	 */
    public String getPagePrint() {
        return this.page;
    }

    public void setPage(String page) {
        this.page = page.trim();
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getPublisher() {
        String myWert = this.publisher;
        String myFieldName = "publisher";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /**
	 * Return the content of publisher var
	 * @return
	 */
    public String getPublisherPrint() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher.trim();
    }

    /**
	 * Returns true, if medium is online
	 * @return
	 */
    public boolean isReleased() {
        return released;
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html select field.
	 * @return
	 */
    public String getReleased() {
        String myFieldName = "released";
        String onsel = "";
        String offsel = "";
        if (this.isReleased()) {
            onsel = "selected";
        } else {
            offsel = "selected";
        }
        if (this.getUserObject().isPublisher()) {
            return "<select name=\"" + myFieldName + "\"><option value=\"on\" " + onsel + ">Online</option><option value=\"off\" " + offsel + ">Offline</option></select>";
        } else {
            return "";
        }
    }

    /**
	 * Set released to true, if gives parameter equales "on"
	 * @param released
	 */
    public void setReleased(String released) {
        if (released == null) {
            return;
        } else {
            if (released.equals("on")) this.setReleased(true); else this.setReleased(false);
        }
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getRemarks() {
        String myWert = this.remarks;
        String myFieldName = "remarks";
        if (this.getUserObject().isEditor()) {
            return "<textarea  name=\"" + myFieldName + "\"  cols=\"50\">" + myWert + "</textarea>";
        } else {
            return myWert;
        }
    }

    /**
	 * returns remarks
	 * @return
	 */
    public String getRemarksPrint() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks.trim();
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getShelf() {
        String myWert = this.shelf;
        String myFieldName = "shelf";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /**
	 * returns value of shelf (Schriftenreihe)
	 * @return
	 */
    public String getShelfPrint() {
        return this.shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf.trim();
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getSignatur() {
        String myWert = this.signatur;
        String myFieldName = "signatur";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /**
	 * Return sgnature
	 * @return
	 */
    public String getSignaturPrint() {
        return this.signatur;
    }

    public void setSignatur(String signatur) {
        this.signatur = signatur.trim();
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getTitle1() {
        String myWert = this.title1;
        String myFieldName = "title1";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /**
	 * Returns title1
	 * @return
	 */
    public String getTitle1Print() {
        return this.title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1.trim();
    }

    /**
	 * Returns a html select Box with valid rooms. 
	 * @return
	 */
    public String getRoom() {
        String myWert = this.room;
        String myFieldName = "room";
        String myReturn = "";
        if (this.getUserObject().isEditor()) {
            JDBCBean dbCon = new JDBCBean();
            SQLResults res = dbCon.getResultSet("select * from rooms ");
            myReturn += "<select name=\"" + myFieldName + "\">";
            myReturn += "<option value=\"clearme\"" + this.isSelected(myWert, "") + ">--- nicht gesetzt ---</option>";
            for (int i = 0; i < res.getRowCount(); i++) {
                String value = res.getString(i, "name");
                myReturn += "<option value=\"" + value + "\"" + this.isSelected(myWert, value) + ">" + value + "</option>";
            }
            myReturn += "</select>";
        } else {
            myReturn = myWert;
        }
        return myReturn;
    }

    /**
	 * Returns room
	 * @return
	 */
    public String getRoomPrint() {
        return this.room;
    }

    public void setRoom(String room) {
        if (room.equals("clearme")) this.room = ""; else this.room = room.trim();
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getIsbn() {
        String myWert = this.isbn;
        String myFieldName = "isbn";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /**
	 * Returns ISBN Number
	 * @return
	 */
    public String getIsbnPrint() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn.trim();
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getIssn() {
        String myWert = this.issn;
        String myFieldName = "issn";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /**
	 * Returns ISSN Number
	 * @return
	 */
    public String getIssnPrint() {
        return this.issn;
    }

    public void setIssn(String issn) {
        this.issn = issn.trim();
    }

    /**
	 * Return filtered input String
	 * @param text
	 * @return
	 */
    public String filter(String text) {
        text = replaceAll(text, "\"", "\\\"");
        return text;
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getTitle2() {
        String myWert = this.title2;
        String myFieldName = "title2";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /**
	 * Returns titel2
	 * @return
	 */
    public String getTitle2Print() {
        return this.title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2.trim();
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getVol() {
        String myWert = this.vol;
        String myFieldName = "vol";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /**
	 * Returns Volume
	 * @return
	 */
    public String getVolPrint() {
        return this.vol;
    }

    public void setVol(String vol) {
        this.vol = vol.trim();
    }

    /** 
     * Returns the Year. If user is editor, an HTML INPUT Field is returned.
     * @return Year as String or input field
     */
    public String getYear() {
        String myWert = this.year;
        String myFieldName = "year";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    /** 
     * Returns the Year. 
     * @return Year as String 
     */
    public String getYearPrint() {
        return this.year;
    }

    /** 
     * Sets Year.
     * @param year as String.
     */
    public void setYear(String year) {
        this.year = year.trim();
    }

    public String getAuthorString() {
        String myWert = convertToString(this.author);
        String myFieldName = "authorString";
        if (this.getUserObject().isEditor()) {
            return "<input type=\"input\" name=\"" + myFieldName + "\" size=\"" + this.editFieldWidth + "\" value=\"" + myWert + " \">";
        } else {
            return myWert;
        }
    }

    public String[] getAuthor() {
        return author;
    }

    /**
	 * Returns the Variable indicatd by the MethodName. if the current User is an editor (or publisher or admin), this method return an html input field.
	 * @return
	 */
    public String getAuthors() {
        String myString = "";
        if (this.getUserObject().isEditor()) {
            myString += "<a href=\"javascript:add();\">add</a> | <a href=\"javascript:remove();show();\">remove</a><br>";
        }
        for (int i = 0; i < this.getAuthor().length; i++) {
            if (this.getUserObject().isEditor()) {
                myString += "<span id=\"eintrag" + i + "\"><input type=\"text\" name=\"authorArray[]\" size=\"" + this.getEditFieldWidth() + "\" value=\"" + this.getAuthor(i) + "\"></span><br>";
            } else {
                myString += this.getAuthor(i) + "; ";
            }
        }
        if (!this.getUserObject().isEditor()) myString = myString.substring(0, myString.length() - 2);
        return myString;
    }

    /**
	 * Returns Authors seperated by semicolons
	 * @return
	 */
    public String getAuthorsPrint() {
        String myString = "";
        for (int i = 0; i < this.getAuthor().length; i++) {
            if (this.getAuthor(i).length() > 0) {
                myString += this.getAuthor(i) + "; ";
            }
        }
        if (myString.length() >= 2) {
            myString = myString.substring(0, myString.length() - 2);
        }
        return myString;
    }

    /**
	 * Return author no i from array
	 * @param i
	 * @return
	 */
    public String getAuthor(int i) {
        if (i < this.author.length) return author[i]; else return "";
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }

    /**
	 * sets Author Array from given Author String
	 * @param authorString
	 */
    public void setAuthorString(String authorString) {
        this.authorString = authorString.trim();
        this.setAuthor(convertToArray(authorString));
    }

    public UserBean getUserObject() {
        return userObject;
    }

    public void setUserObject(UserBean userObject) {
        this.userObject = userObject;
    }

    /**
	 * Returns from header, if user is an editor
	 * @return
	 */
    public String getFormStart() {
        if (this.userObject.isEditor()) return "<form method=\"post\" action=\"index.jsp?content=details&id=" + this.getId() + "\" onSubmit=\"return checkValues(this);\">"; else return "";
    }

    /**
	 * Return form footer, if user is an editor
	 * @return
	 */
    public String getFormEnd() {
        if (this.userObject.isEditor()) return "</form>"; else return "";
    }

    /**
	 * Return form addons (hidden field etc.) if user is an editor
	 * @return
	 */
    public String getFormAddons() {
        String myReturn = "";
        if (this.userObject.isEditor()) {
            myReturn = "<input type=\"hidden\" name=\"id\" value=\"" + this.getId() + "\">" + "<input type=\"hidden\" name=\"update\" value=\"yes\">" + "<input type=\"submit\" value=\"" + this.langObject.getTextFor("search.Update") + "\">";
            if (this.getUserObject().isPublisher()) myReturn += "<input type=\"button\" value=\"" + this.langObject.getTextFor("search.Delete") + "\" onClick=\"return sicherDelete();\">";
        } else {
            myReturn = "";
        }
        return myReturn;
    }

    public LanguageBean getLangObject() {
        return langObject;
    }

    public void setLangObject(LanguageBean langObject) {
        this.langObject = langObject;
    }

    /**
	 * reuturn the length of form input fields
	 * @return
	 */
    public int getEditFieldWidth() {
        return editFieldWidth;
    }

    public void setEditFieldWidth(int editFieldWidth) {
        this.editFieldWidth = editFieldWidth;
    }

    /**
	 * Converts an given String to an Array (Form of giver String: "Field on","field two","...",....
	 * @param x
	 * @return
	 */
    public String[] convertToArray(String x) {
        String regex = "\",\"";
        String[] carved = Pattern.compile(regex).split(x);
        carved[0] = carved[0].substring(1, carved[0].length());
        carved[carved.length - 1] = carved[carved.length - 1].substring(0, carved[carved.length - 1].length() - 1);
        return carved;
    }

    /**
	 * return a String from given Array (Form: "Field on","field two","...",....
	 * @param x
	 * @return
	 */
    public String convertToString(String[] x) {
        if (x.length == 0 || x == null) return ("\"\"");
        String myString = "\"";
        for (int i = 0; i < x.length; i++) {
            if (!x[i].equals("") && x[i] != null) myString += x[i] + "\",\"";
        }
        if (myString.length() > 1) {
            myString = myString.substring(0, myString.length() - 2);
            return myString;
        } else return "\"\"";
    }

    /**
	 * Replaces all substrings in strings
	 * @param String. the whole String
	 * @param String search. Substring to replace
	 * @param String replace. Substringreplacement
	 * @return String with replaces ubstrings
	 */
    public static String replaceAll(String source, String search, String replace) {
        if (search.equals(replace)) {
            return source;
        }
        StringBuffer result = new StringBuffer();
        int len = search.length();
        if (len == 0) {
            return source;
        }
        int pos = 0;
        int nPos;
        do {
            nPos = source.indexOf(search, pos);
            if (nPos != -1) {
                result.append(source.substring(pos, nPos));
                result.append(replace);
                pos = nPos + len;
            } else {
                result.append(source.substring(pos));
            }
        } while (nPos != -1);
        return result.toString();
    }
}
