package org.janken.svr;

import java.util.List;

public interface JankenDao {

    public abstract void saveData(JankenData idata);

    public abstract long findSize();

    public abstract List<JankenData> findAllData();

    public abstract List<JankenData> findByNickname(String inickname);

    public abstract List<JankenData> findByState(JankenState type, JankenState result);

    public abstract JankenData findById(Long iid);

    public abstract void deleteData(List<JankenData> ilist);

    public abstract long deleteAllData();
}
