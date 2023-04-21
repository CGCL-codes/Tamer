package com.ecco.persistent;

import java.util.List;
import javax.ejb.Remote;

@Remote
public interface AccObColAgentRemote {

    public void save(AccObCol persistentInstance);

    public void delete(AccObCol persistentInstance);

    public AccObCol update(AccObCol detachedInstance);

    public AccObCol findAOCById(Integer aoc_id);

    public List<AccObCol> findAll();

    public void addAccObCol(Integer acc_id, Integer ob_id, Integer col_id, String comment);

    public void updateAccObCol(Integer aoc_id, Integer acc_id, Integer ob_id, Integer col_id, String comment);

    public void removeAOCById(Integer aoc_id);

    public Boolean checkAOCByAcc_Ob_Col(Account account, Objects objects, Colour colour);
}
