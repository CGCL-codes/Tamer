package orm.dao;

import org.orm.*;
import orm.*;

public interface Te_fraseDAO {

    public Te_frase loadTe_fraseByORMID(int te_id) throws PersistentException;

    public Te_frase getTe_fraseByORMID(int te_id) throws PersistentException;

    public Te_frase loadTe_fraseByORMID(int te_id, org.hibernate.LockMode lockMode) throws PersistentException;

    public Te_frase getTe_fraseByORMID(int te_id, org.hibernate.LockMode lockMode) throws PersistentException;

    public Te_frase loadTe_fraseByORMID(PersistentSession session, int te_id) throws PersistentException;

    public Te_frase getTe_fraseByORMID(PersistentSession session, int te_id) throws PersistentException;

    public Te_frase loadTe_fraseByORMID(PersistentSession session, int te_id, org.hibernate.LockMode lockMode) throws PersistentException;

    public Te_frase getTe_fraseByORMID(PersistentSession session, int te_id, org.hibernate.LockMode lockMode) throws PersistentException;

    public Te_frase[] listTe_fraseByQuery(String condition, String orderBy) throws PersistentException;

    public Te_frase[] listTe_fraseByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException;

    public Te_frase[] listTe_fraseByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException;

    public Te_frase[] listTe_fraseByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException;

    public Te_frase loadTe_fraseByQuery(String condition, String orderBy) throws PersistentException;

    public Te_frase loadTe_fraseByQuery(String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException;

    public Te_frase loadTe_fraseByQuery(PersistentSession session, String condition, String orderBy) throws PersistentException;

    public Te_frase loadTe_fraseByQuery(PersistentSession session, String condition, String orderBy, org.hibernate.LockMode lockMode) throws PersistentException;

    public Te_frase createTe_frase();

    public boolean save(orm.Te_frase te_frase) throws PersistentException;

    public boolean delete(orm.Te_frase te_frase) throws PersistentException;

    public boolean refresh(orm.Te_frase te_frase) throws PersistentException;

    public boolean evict(orm.Te_frase te_frase) throws PersistentException;

    public Te_frase loadTe_fraseByCriteria(Te_fraseCriteria te_fraseCriteria);

    public Te_frase[] listTe_fraseByCriteria(Te_fraseCriteria te_fraseCriteria);
}
