package org.itx.equipment.l1;

import java.io.PrintStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.itx.equipment.l0.EQContractor;
import org.itx.qlfactory.QLFactory;
import org.jboss.annotation.ejb.RemoteBinding;

@Stateless
@RemoteBinding(jndiBinding = "Contractors/remote")
public class ContractorsBean implements Contractors {

    @EJB
    private Opers opers;

    @Resource(mappedName = "QLFactory/local")
    private QLFactory factory;

    HashMap<String, String> map;

    @EJB
    private Distributions distributions;

    @PersistenceContext(unitName = "EQv1l0")
    private EntityManager manager;

    @PostConstruct
    public void initialize() {
        this.map = this.factory.getQuerys(EQContractor.class);
    }

    private void validate(EQContractor ND) throws EQException {
        if ((ND.getName() == null) || (ND.getName().length() == 0)) throw new EQException("Пустое поле!");
    }

    public void rem(Long i) {
        rem((EQContractor) this.manager.find(EQContractor.class, i));
    }

    public void rem(EQContractor D) {
        EQContractor ND = new EQContractor();
        Reflector.copy(D, ND);
        ND.setUId(null);
        D.setVersion(ND);
        D.setOper(this.opers.get());
        ND.setTS(new Timestamp(System.currentTimeMillis()));
        ND.setCloseDate(new Date(System.currentTimeMillis()));
        this.manager.persist(D);
        this.manager.persist(ND);
    }

    public void add(EQContractor D) throws EQException {
        validate(D);
        D.setVersionRoot(D);
        D.setUId(null);
        D.setOper(this.opers.get());
        D.setOpenDate(new Date(System.currentTimeMillis()));
        D.setTS(new Timestamp(System.currentTimeMillis()));
        this.manager.merge(D);
    }

    public List<EQContractor> getAll() {
        Long t = Long.valueOf(System.currentTimeMillis());
        List l = this.manager.createQuery((String) this.map.get("EQContractor.All")).getResultList();
        System.out.println("Time query: " + (System.currentTimeMillis() - t.longValue()));
        return l;
    }

    public List<EQContractor> getAll(String search) {
        return this.manager.createQuery((String) this.map.get("EQContractor.AllWSearch")).setParameter("S", "%" + search + "%").getResultList();
    }

    public List<EQContractor> getAll(Long firstRow, Long maxRow) {
        return this.manager.createQuery((String) this.map.get("EQContractor.All")).setFirstResult(firstRow.intValue()).setMaxResults(maxRow.intValue()).getResultList();
    }

    public List<EQContractor> getAll(String search, Long firstRow, Long maxRow) {
        return this.manager.createQuery((String) this.map.get("EQContractor.AllWSearch")).setParameter("S", "%" + search + "%").setFirstResult(firstRow.intValue()).setMaxResults(maxRow.intValue()).getResultList();
    }

    public List<EQContractor> getDeleted() {
        return this.manager.createQuery((String) this.map.get("EQContractor.Deleted")).getResultList();
    }

    public List<EQContractor> getDeleted(String search) {
        return this.manager.createQuery((String) this.map.get("EQContractor.DeletedWSearch")).setParameter("S", "%" + search + "%").getResultList();
    }

    public List<EQContractor> getDeleted(Long firstRow, Long maxRow) {
        return this.manager.createQuery((String) this.map.get("EQContractor.Deleted")).setFirstResult(firstRow.intValue()).setMaxResults(maxRow.intValue()).getResultList();
    }

    public List<EQContractor> getDeleted(String search, Long firstRow, Long maxRow) {
        return this.manager.createQuery((String) this.map.get("EQContractor.DeletedWSearch")).setParameter("S", "%" + search + "%").setFirstResult(firstRow.intValue()).setMaxResults(maxRow.intValue()).getResultList();
    }

    public void update(EQContractor D) throws EQException {
        EQContractor ND = new EQContractor();
        Reflector.copy(D, ND);
        _update(ND);
    }

    private void _update(EQContractor ND) throws EQException {
        validate(ND);
        Long id = ND.getUId();
        ND.setUId(null);
        ND.setTS(new Timestamp(System.currentTimeMillis()));
        ND.setOper(this.opers.get());
        this.manager.persist(ND);
        this.manager.flush();
        EQContractor D = (EQContractor) this.manager.find(EQContractor.class, id);
        D.setVersion(ND);
        this.manager.persist(D);
        this.manager.flush();
        this.distributions.updateBy(D, ND);
    }

    public List<EQContractor> getHistory(Long i) {
        return getHistory((EQContractor) this.manager.find(EQContractor.class, i));
    }

    public List<EQContractor> getHistory(EQContractor D) {
        return this.manager.createQuery((String) this.map.get("EQContractor.History")).setParameter("H", D.getVersionRoot()).getResultList();
    }

    public Long getSizeAll() {
        return ((Long) this.manager.createQuery((String) this.map.get("EQContractor.SizeAll")).getSingleResult());
    }

    public Long getSizeDeleted() {
        return ((Long) this.manager.createQuery((String) this.map.get("EQContractor.SizeDeleted")).getSingleResult());
    }

    public Long getSizeAll(String search) {
        return ((Long) this.manager.createQuery((String) this.map.get("EQContractor.SizeAllWSearch")).setParameter("S", "%" + search + "%").getSingleResult());
    }

    public Long getSizeDeleted(String search) {
        return ((Long) this.manager.createQuery((String) this.map.get("EQContractor.SizeDeletedWSearch")).setParameter("S", "%" + search + "%").getSingleResult());
    }

    public EQContractor get(Long id) {
        return ((EQContractor) this.manager.find(EQContractor.class, id));
    }
}
