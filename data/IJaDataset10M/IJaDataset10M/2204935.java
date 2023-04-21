package com.datas.service.nabavka;

import java.util.List;
import com.datas.bean.model.nabavka.Prijemnica;
import com.datas.bean.model.nabavka.PrijemnicaStavke;
import com.datas.bean.model.nabavka.PrijemnicaStavkeModel;

/**
 * @author kimi
 * 
 */
public interface NabavkaService {

    /** prijemnica */
    List<Prijemnica> selectList(Prijemnica prijemnica);

    Prijemnica selectObject(Prijemnica prijemnica);

    void insert(Prijemnica prijemnica);

    void insert(Prijemnica prijemnica, String idModul);

    void update(Prijemnica prijemnica);

    void delete(Prijemnica prijemnica);

    int proknjizi(Prijemnica prijemnica);

    int proknjizi(Prijemnica prijemnica, String idModul);

    int storno(Prijemnica prijemnica);

    /** prijemnica stavke */
    List<PrijemnicaStavke> selectList(PrijemnicaStavke prijemnicaStavke);

    PrijemnicaStavke selectObject(PrijemnicaStavke prijemnicaStavke);

    void insert(PrijemnicaStavke prijemnicaStavke);

    void update(PrijemnicaStavke prijemnicaStavke);

    void delete(PrijemnicaStavke prijemnicaStavke);

    void getUpdatedModel(PrijemnicaStavkeModel model, int changedField);
}
