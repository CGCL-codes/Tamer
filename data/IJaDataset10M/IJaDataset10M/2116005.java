package com.dqgen.example;

import com.dqgen.mapper.engine.Column;
import com.dqgen.mapper.engine.Table;
import com.dqgen.metamodel.MetaModel;

/**
 * Metamodel class for the table TRANSACTION
 */
public class Transaction_ extends MetaModel {

    /**
     * Default constructor
     */
    public Transaction_(Table table) {
        super(table);
    }

    public Column description;

    public Column dateModified;

    public Column type;

    public Column accountId;

    public Column deleted;

    public Column transactionDate;

    public Column dateAdded;

    public Column id;

    public Column userId;

    public Column destAccountId;

    public Column amount;

    public Column notes;

    @Override
    public Transaction_ newInstance(String id) {
        return (Transaction_) super.newInstance(id);
    }
}
