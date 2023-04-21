package com.completex.objective.components.persistency.test.oracle;

import com.completex.objective.components.persistency.DelegatePersistentObjectFactory;
import com.completex.objective.components.persistency.OdalPersistencyException;
import com.completex.objective.components.persistency.PersistentObject;
import com.completex.objective.components.persistency.core.impl.CompoundDelegateFactoryImpl;
import com.completex.objective.components.persistency.key.impl.AbstractKeyGenerator;
import com.completex.objective.components.persistency.key.impl.OdalBulkSequenceKeyGenerator100;
import com.completex.objective.components.persistency.key.impl.OdalSimpleSequenceKeyGenerator;
import com.completex.objective.components.persistency.test.oracle.gen.TestMasterAttr;
import com.completex.objective.components.persistency.test.oracle.gen.TestMasterExtAttr;
import com.completex.objective.components.persistency.test.util.BaseTestSchemaA;
import com.completex.objective.components.persistency.transact.Transaction;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Gennady Krizhevsky
 */
public class PersistencyImplOraFuncExtras07Test extends PersistencyImplOraFuncExtras06Test {

    public void testOdalSequence() {
        getLogger().debug("Enter PersistencyImplOraFuncExtras07Test::testOdalSequence");
        try {
            OdalSimpleSequenceKeyGenerator keyGenerator = new OdalSimpleSequenceKeyGenerator(BaseTestSchemaA.ODAL_SEQ_TABLE_NAME, false, databasePolicy, BaseTestSchemaA.ODAL_SEQ_NAME, logger);
            for (int i = 0; i < 3; i++) {
                Number value = (Number) keyGenerator.getNextKey(null, persistency, null);
                getLogger().info("Got id " + value);
            }
        } catch (OdalPersistencyException e) {
            getLogger().error("PersistencyImplOraFuncExtras07Test::testOdalSequence", e);
            fail("PersistencyImplOraFuncExtras07Test::testOdalSequence: " + e.getMessage());
        } finally {
            getLogger().debug("Exit PersistencyImplOraFuncExtras07Test::testOdalSequence");
        }
    }

    public void testOdalSequence02() {
        getLogger().debug("Enter PersistencyImplOraFuncExtras07Test::testOdalSequence02");
        try {
            OdalSimpleSequenceKeyGenerator keyGenerator = new OdalSimpleSequenceKeyGenerator();
            keyGenerator.setDatabasePolicy(databasePolicy);
            keyGenerator.setLogger(logger);
            HashMap staticParameters = new HashMap();
            staticParameters.put(AbstractKeyGenerator.SEQ_SAME_TRANSACTION_KEY, Boolean.FALSE);
            staticParameters.put(AbstractKeyGenerator.SEQ_TABLE_KEY, BaseTestSchemaA.ODAL_SEQ_TABLE_NAME);
            staticParameters.put(AbstractKeyGenerator.SEQ_KEY, BaseTestSchemaA.ODAL_SEQ_NAME);
            keyGenerator.setStaticParameters(staticParameters);
            for (int i = 0; i < 3; i++) {
                Number value = (Number) keyGenerator.getNextKey(null, persistency, null);
                getLogger().info("Got id " + value);
            }
        } catch (OdalPersistencyException e) {
            getLogger().error("PersistencyImplOraFuncExtras07Test::testOdalSequence02", e);
            fail("PersistencyImplOraFuncExtras07Test::testOdalSequence02: " + e.getMessage());
        } finally {
            getLogger().debug("Exit PersistencyImplOraFuncExtras07Test::testOdalSequence02");
        }
    }

    public void testOdalBulkSequence() {
        getLogger().debug("Enter PersistencyImplOraFuncExtras07Test::testOdalBulkSequence");
        try {
            OdalBulkSequenceKeyGenerator100 keyGenerator = new OdalBulkSequenceKeyGenerator100(BaseTestSchemaA.ODAL_SEQ_TABLE_NAME, false, databasePolicy, BaseTestSchemaA.ODAL_SEQ_NAME, logger);
            for (int i = 0; i < 3; i++) {
                Long value = keyGenerator.getNextKeyLong(null, persistency);
                getLogger().info("Got id " + value);
            }
        } catch (OdalPersistencyException e) {
            getLogger().error("PersistencyImplOraFuncExtras07Test::testOdalBulkSequence", e);
            fail("PersistencyImplOraFuncExtras07Test::testOdalBulkSequence: " + e.getMessage());
        } finally {
            getLogger().debug("Exit PersistencyImplOraFuncExtras07Test::testOdalBulkSequence");
        }
    }

    public void testDelegateFactory() {
        getLogger().debug("Enter PersistencyImplOraFuncExtras06Test::testDelegateFactory");
        Transaction transaction = null;
        try {
            unsetUseBatchModify();
            transaction = transactionManager.begin();
            Product product = new Product();
            product.setAliasName("product");
            persistency.insert(product);
            Car car = new Car();
            car.setAliasName("car");
            persistency.insert(car);
            Sedan sedan = new Sedan();
            sedan.setAliasName("sedan");
            ArrayList slaves = new ArrayList();
            ComplexSlave slave = new ComplexSlave();
            slave.setAliasTestSlaveId(SLAVE_3);
            slaves.add(slave);
            sedan.setSlaves(slaves);
            persistency.insert(sedan);
            Product loadedProduct = (Product) persistency.load(new Product(product.getAliasTestMasterId()));
            System.out.println(" *** loadedProduct: " + PersistentObject.toGenericString(loadedProduct));
            Car loadedCar = (Car) persistency.load(new Product(car.getAliasTestMasterId()));
            System.out.println(" *** loadedCar: " + PersistentObject.toGenericString(loadedCar));
            Sedan loadedSedan = (Sedan) persistency.load(new Product(sedan.getAliasTestMasterId()));
            System.out.println(" *** loadedSedan: " + PersistentObject.toGenericString(loadedSedan));
            assertNotNull(loadedProduct);
            assertNotNull(loadedCar);
            assertNotNull(loadedSedan);
        } catch (Exception e) {
            getLogger().error("PersistencyImplOraFuncExtras06Test::testDelegateFactory", e);
            fail("PersistencyImplOraFuncExtras06Test::testDelegateFactory: " + e.getMessage());
        } finally {
            rollback(transaction);
            getLogger().debug("Exit PersistencyImplOraFuncExtras06Test::testDelegateFactory");
        }
    }

    public static class Product extends ComplexMaster {

        public Product() {
        }

        public Product(Long aliasTestMasterId) {
            super(aliasTestMasterId);
        }

        public DelegatePersistentObjectFactory delegateFactory() {
            if (delegateFactory == null) {
                delegateFactory = new CompoundDelegateFactoryImpl(new PersistentObject[] { this, delegatingFactoryCar(), delegatingFactorySedan() }, new String[] { "product", "car", "sedan" }, Product.COL_ALIAS_NAME);
            }
            return delegateFactory;
        }

        protected Car delegatingFactoryCar() {
            return new Car();
        }

        protected Sedan delegatingFactorySedan() {
            return new Sedan();
        }
    }

    public static class Car extends Product {

        private transient int attrIndex;

        public Car() {
        }

        public Car(Long aliasTestMasterId) {
            super(aliasTestMasterId);
        }

        public boolean compound() {
            return true;
        }

        protected void doSetupCompound() {
            attrIndex = nextCompoundIndex();
            compoundEntry(attrIndex, ((TestMasterAttr) new TestMasterAttr().newPersistentInstance()));
            compoundCascadeAll(attrIndex);
        }

        public TestMasterAttr getMasterAttr() {
            return (TestMasterAttr) compoundEntry(attrIndex);
        }

        protected final void setMasterAttr(TestMasterAttr masterAttr) {
            throw new UnsupportedOperationException();
        }
    }

    public static class Sedan extends Car {

        private transient int extAttrIndex;

        public Sedan() {
        }

        public Sedan(Long aliasTestMasterId) {
            super(aliasTestMasterId);
        }

        protected void doSetupCompound() {
            super.doSetupCompound();
            compoundEntry(extAttrIndex = nextCompoundIndex(), ((TestMasterExtAttr) new TestMasterExtAttr().newPersistentInstance()));
            compoundCascadeAll(extAttrIndex);
        }

        public TestMasterExtAttr getMasterExtAttr() {
            return (TestMasterExtAttr) compoundEntry(extAttrIndex);
        }

        protected final void setMasterExtAttr(TestMasterExtAttr masterExtAttr) {
            throw new UnsupportedOperationException();
        }
    }
}
