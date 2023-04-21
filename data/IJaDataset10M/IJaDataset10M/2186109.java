package org.btn.objects;

public class User {

    private String name, email, kontonummer, telefonnummer, password, typ, strassenname, hausnummer, ort, land, plz, statistik;

    private int id, bewertung;

    public User() {
    }

    ;

    public User(int id, String name, String email, String kontonummer, String telefonnummer, String password, String typ, int bewertung, String strassenname, String hausnummer, String ort, String land, String plz, String statistik) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
        this.setKontonummer(kontonummer);
        this.setTelefonnummer(telefonnummer);
        this.setPassword(password);
        this.setTyp(typ);
        this.setBewertung(bewertung);
        this.setStrassenname(strassenname);
        this.setHausnummer(hausnummer);
        this.setOrt(ort);
        this.setLand(land);
        this.setPlz(plz);
        this.setStatistik(statistik);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKontonummer() {
        return kontonummer;
    }

    public void setKontonummer(String kontonummer) {
        this.kontonummer = kontonummer;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public int getBewertung() {
        return bewertung;
    }

    public void setBewertung(int bewertung) {
        this.bewertung = bewertung;
    }

    public String getStrassenname() {
        return strassenname;
    }

    public void setStrassenname(String strassenname) {
        this.strassenname = strassenname;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getStatistik() {
        return statistik;
    }

    public void setStatistik(String statistik) {
        this.statistik = statistik;
    }
}
