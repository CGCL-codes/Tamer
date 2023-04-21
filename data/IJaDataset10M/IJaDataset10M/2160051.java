package db;

import db.base.BaseKlient;

public class Klient extends BaseKlient {

    private static final long serialVersionUID = 1L;

    public Klient() {
        super();
    }

    /**
	 * Constructor for primary key
	 */
    public Klient(java.lang.Integer id) {
        super(id);
    }

    public Klient(Integer id, String imie, String nazwisko, String login, String haslo, String nrTelefonu, String email, boolean isAdmin) {
        super();
        setId(id);
        setImie(imie);
        setNazwisko(nazwisko);
        setLogin(login);
        setHaslo(haslo);
        setNrTelefonu(nrTelefonu);
        setEmail(email);
        setIsAdmin(isAdmin);
    }
}
