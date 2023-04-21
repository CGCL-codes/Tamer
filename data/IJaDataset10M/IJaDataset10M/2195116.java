package org.stars.daostars;

/**
 *	Factory dei dao. 
 *
 * @author Francesco Benincasa (908099)
 * @date 05/nov/07, 17:57:34
 *
 */
public class DaoBaseFactory {

    /**
	 * Crea un dao che gestisce il tipo di eb passato come parametro del metodo.
	 * 
	 * @param <D>
	 * 		tipo di dao
	 * @param <U>
	 * 		tipo di eb gestito dal dao
	 * @param daoType
	 * 		classe del dao da istanziare
	 * @return
	 * 		instanza del dao
	 *
	public static <D extends DaoBase<U>,U extends EntityBeanBase> D makeDao(Class<D> daoType)
	{		
		return Helper.makeObject(daoType);
	}*/
    public static <U> DaoBase<U> makeDaoBase(Class<U> entityType) {
        return new DaoBase<U>(entityType);
    }

    public static <U> DaoBase<U> makeDaoBase(Class<U> entityType, String connectionName) {
        return new DaoBase<U>(entityType, connectionName);
    }
}
