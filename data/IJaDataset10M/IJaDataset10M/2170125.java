package session.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.annotation.ejb.RemoteBinding;
import session.remote.HospitalUnitManagerRemote;
import entity.HospitalUnit;
import exception.FullUnitException;

@Stateless
@RemoteBinding(jndiBinding = "FamilyDoctorManagerRemote")
public class HospitalUnitManagerBean implements HospitalUnitManagerRemote {

    @PersistenceContext(unitName = "MECS")
    private EntityManager em;

    @Override
    public HospitalUnit getHospitalUnit(int id) {
        return em.find(HospitalUnit.class, id);
    }

    @Override
    public void createHospitalUnit(String name, int bedsNumber) {
        HospitalUnit hospitalUnit = new HospitalUnit();
        hospitalUnit.setName(name);
        hospitalUnit.setBedsNumber(bedsNumber);
        hospitalUnit.setFreeBedsNumber(bedsNumber);
        em.persist(hospitalUnit);
    }

    @Override
    public void dismissPatient(int hospitalUnitID) {
        HospitalUnit hospitalUnit = em.find(HospitalUnit.class, hospitalUnitID);
        hospitalUnit.setFreeBedsNumber(hospitalUnit.getFreeBedsNumber() + 1);
        em.merge(hospitalUnit);
    }

    @Override
    public void hospitalizePatient(int hospitalUnitID) throws FullUnitException {
        HospitalUnit hospitalUnit = em.find(HospitalUnit.class, hospitalUnitID);
        if (hospitalUnit.getFreeBedsNumber() - 1 >= 0) {
            hospitalUnit.setFreeBedsNumber(hospitalUnit.getFreeBedsNumber() - 1);
            em.merge(hospitalUnit);
        } else throw new FullUnitException(hospitalUnit.getName(), hospitalUnitID);
    }

    @Override
    public void setHospitalUnit(int hospitalUnitID, String name, int bedsNumber, int freeBedsNumber) {
        HospitalUnit hospitalUnit = em.find(HospitalUnit.class, hospitalUnitID);
        hospitalUnit.setName(name);
        hospitalUnit.setBedsNumber(bedsNumber);
        hospitalUnit.setFreeBedsNumber(bedsNumber);
        em.merge(hospitalUnit);
    }
}
