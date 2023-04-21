package com.ctp.arquilliandemo.ex1.domain;

import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(TradeTransaction.class)
public abstract class TradeTransaction_ {

    public static volatile SingularAttribute<TradeTransaction, Integer> amount;

    public static volatile SingularAttribute<TradeTransaction, Date> timestamp;

    public static volatile SingularAttribute<TradeTransaction, Long> id;

    public static volatile SingularAttribute<TradeTransaction, Share> share;

    public static volatile SingularAttribute<TradeTransaction, TransactionType> type;

    public static volatile SingularAttribute<TradeTransaction, User> user;
}
