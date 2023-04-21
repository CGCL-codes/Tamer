package mx.ipn.persistencia.dao;

import java.util.ArrayList;
import mx.ipn.to.FactorTO;

public interface FactorDAO {

    public boolean insertFactor(FactorTO factorTO);

    public short deleteFactor(int idFactor);

    public short updateFactor(FactorTO factorTO);

    public FactorTO findFactorById(int idFactor);

    public ArrayList<FactorTO> selectFactor();
}
