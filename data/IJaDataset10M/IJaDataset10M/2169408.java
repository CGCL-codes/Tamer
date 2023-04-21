package tp1POO.Persistencia;

import tp1POO.Modelo.ObjetoPOO;

/**
 * Fabrica DAO para banco de dados
 */
public class FactoryDAODB implements AbstractFactoryDAO {

    /**
	 * Instancia uma DAO para banco de dados
	 */
    @Override
    public DAO criarDAO(ObjetoPOO parObj) {
        return new DAOBD();
    }
}
