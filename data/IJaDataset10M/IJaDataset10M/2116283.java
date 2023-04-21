package ru.adv.db.handler;

import ru.adv.db.config.*;

/**
 * Представление атрибута объекта, по которому производится сортировка.
 * @see Sort
 * @see SQLSelect
 * @see Handler
 * @version $Revision: 1.7 $
 */
public class SQLSortIdentifier extends SQLAttrIdentifier {

    private boolean asc = true;

    private Boolean ignoreCase = null;

    /**
	* Конструктор с указанием сортировки
	* @param aliasName имя алиаса в {@link SQLSelect}
	* @param attrName имя атрибута
	* @param asc если true то сортировка по возрастанию, иначе по убыванию
	*/
    public SQLSortIdentifier(DBConfig dbc, String aliasName, String attrName, boolean asc) throws SQLStatementException {
        super(dbc, aliasName, attrName);
        this.asc = asc;
    }

    /**
	* Конструктор, сортровка по возрастнию
	* @param aliasName имя алиаса в {@link SQLSelect}
	* @param attrName имя атрибута
	*/
    public SQLSortIdentifier(DBConfig dbc, String aliasName, String attrName) throws SQLStatementException {
        this(dbc, aliasName, attrName, true);
    }

    /**
	* Конструктор с указанием сортировки
	* @param aliasName имя алиаса в {@link SQLSelect}
	* @param attrName имя атрибута
	* @param asc если true то сортировка по возрастанию, иначе по убыванию
	* @param ignoreCase если true, то сортировка с игнорированием регистра
	*/
    public SQLSortIdentifier(DBConfig dbc, String aliasName, String attrName, boolean asc, boolean ignoreCase) throws SQLStatementException {
        this(dbc, aliasName, attrName, asc);
        this.ignoreCase = new Boolean(ignoreCase);
    }

    /**
	* Сортировка по возрастанию?
	*/
    public boolean isAsc() {
        return asc;
    }

    /**
	* Установлена ли значение указывающее как работать с регистрами бУковок?
	*/
    public boolean isSetIgnoreCase() {
        return ignoreCase != null;
    }

    /**
	* Сортировка c игнорированием регистра?
	*/
    public boolean isIgnoreCase() {
        return isIgnoreCase() ? ignoreCase.booleanValue() : false;
    }
}
