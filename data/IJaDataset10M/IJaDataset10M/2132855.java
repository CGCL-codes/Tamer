package org.techytax.dao;

import java.util.List;
import org.techytax.domain.BookValue;
import org.techytax.domain.KeyId;
import org.techytax.domain.RemainingValue;

public class BookValueDao extends BaseDao {

    private void encrypt(BookValue boekwaarde) {
        boekwaarde.setSaldo(intEncryptor.encrypt(boekwaarde.getSaldo()));
    }

    private void encrypt(RemainingValue restwaarde) {
        restwaarde.setRestwaarde(intEncryptor.encrypt(restwaarde.getRestwaarde()));
    }

    private void decrypt(BookValue boekwaarde) {
        if (boekwaarde != null) {
            boekwaarde.setSaldo(intEncryptor.decrypt(boekwaarde.getSaldo()));
        }
    }

    public void insertBookValue(BookValue boekwaarde) throws Exception {
        encrypt(boekwaarde);
        sqlMap.insert("insertBoekwaarde", boekwaarde);
    }

    public BookValue getPreviousBookValue(BookValue boekwaarde) throws Exception {
        BookValue vorigeBoekwaarde = (BookValue) sqlMap.queryForObject("getVorigeBoekwaarde", boekwaarde);
        decrypt(vorigeBoekwaarde);
        return vorigeBoekwaarde;
    }

    public BookValue getBookValueThisYear(BookValue boekwaarde) throws Exception {
        BookValue boekwaardeDitJaar = (BookValue) sqlMap.queryForObject("getBoekwaardeDitJaar", boekwaarde);
        decrypt(boekwaardeDitJaar);
        return boekwaardeDitJaar;
    }

    @SuppressWarnings("unchecked")
    public List<BookValue> getAllBoekwaardes() throws Exception {
        return sqlMap.queryForList("getAllBoekwaardes", null);
    }

    public void updateBookValue(BookValue boekwaarde) throws Exception {
        encrypt(boekwaarde);
        sqlMap.insert("updateBoekwaarde", boekwaarde);
    }

    @SuppressWarnings("unchecked")
    public List<RemainingValue> getAllRestwaardes() throws Exception {
        return sqlMap.queryForList("getAllRestwaardes", null);
    }

    public void updateRestwaarde(RemainingValue restwaarde) throws Exception {
        encrypt(restwaarde);
        sqlMap.insert("updateRestwaarde", restwaarde);
    }

    public void updateRemainingValueByActivumId(RemainingValue restwaarde) throws Exception {
        encrypt(restwaarde);
        sqlMap.insert("updateRemainingValueByActivumId", restwaarde);
    }

    public void insertRemainingValue(RemainingValue restwaarde) throws Exception {
        encrypt(restwaarde);
        sqlMap.insert("insertRestwaarde", restwaarde);
    }

    @SuppressWarnings("unchecked")
    public List<BookValue> getBookValues(KeyId key) throws Exception {
        List<BookValue> bookValues = sqlMap.queryForList("getBookValues", key);
        for (BookValue boekwaarde : bookValues) {
            decrypt(boekwaarde);
        }
        return bookValues;
    }

    @SuppressWarnings("unchecked")
    public BookValue getBookValue(KeyId key) throws Exception {
        BookValue bookValue = (BookValue) sqlMap.queryForObject("getBookValue", key);
        decrypt(bookValue);
        return bookValue;
    }

    @SuppressWarnings("unchecked")
    public List<BookValue> getBookValuesForChart(KeyId key) throws Exception {
        List<BookValue> bookValues = sqlMap.queryForList("getBookValuesForChart", key);
        for (BookValue boekwaarde : bookValues) {
            decrypt(boekwaarde);
        }
        return bookValues;
    }
}
