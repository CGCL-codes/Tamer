package org.hmaciel.sisingr.ejb.entity;

import java.util.LinkedList;
import java.util.List;
import org.hmaciel.sisingr.ejb.datatypes.ADBean;
import org.hmaciel.sisingr.ejb.datatypes.CEBean;
import org.hmaciel.sisingr.ejb.datatypes.TSBean;
import org.hmaciel.sisingr.ejb.vocabularios.EntityClassVoc;

/**
 * @author lgrundel
 * @author pclavijo
 */
public class PersonBean extends EntityBean {

    List<ADBean> addr = new LinkedList<ADBean>();

    TSBean birthTime;

    boolean deceasedInd;

    TSBean deceasedTime;

    CEBean administrativeGenderCode;

    List<CEBean> raceCodes = new LinkedList<CEBean>();

    public PersonBean() {
        super();
        this.classCode = EntityClassVoc.Person;
    }

    public List<ADBean> getAddr() {
        return addr;
    }

    public void setAddr(List<ADBean> addr) {
        this.addr = addr;
    }

    public CEBean getAdministrativeGenderCode() {
        return administrativeGenderCode;
    }

    public void setAdministrativeGenderCode(CEBean administrativeGenderCode) {
        this.administrativeGenderCode = administrativeGenderCode;
    }

    public TSBean getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(TSBean birthTime) {
        this.birthTime = birthTime;
    }

    public boolean isDeceasedInd() {
        return deceasedInd;
    }

    public void setDeceasedInd(boolean deceasedInd) {
        this.deceasedInd = deceasedInd;
    }

    public TSBean getDeceasedTime() {
        return deceasedTime;
    }

    public void setDeceasedTime(TSBean deceasedTime) {
        this.deceasedTime = deceasedTime;
    }

    public List<CEBean> getRaceCodes() {
        return raceCodes;
    }

    public void setRaceCodes(List<CEBean> raceCodes) {
        this.raceCodes = raceCodes;
    }
}
