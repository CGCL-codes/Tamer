package com.hazelcast.impl;

import java.util.concurrent.atomic.AtomicLong;

class TransactionFactory {

    AtomicLong ids = new AtomicLong(0);

    final FactoryImpl factory;

    TransactionFactory(FactoryImpl factory) {
        this.factory = factory;
    }

    public TransactionImpl newTransaction() {
        return new TransactionImpl(factory, newTransactionId());
    }

    public long newTransactionId() {
        return ids.incrementAndGet();
    }
}
