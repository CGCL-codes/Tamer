package drools_persistence;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.sf.joafip.asm.Type;
import net.sf.joafip.entity.MutableInteger;
import net.sf.joafip.java.util.PTreeMap;
import net.sf.joafip.service.FilePersistence;
import net.sf.joafip.service.FilePersistenceException;
import net.sf.joafip.service.IExclusiveDataAccessSession;
import net.sf.joafip.service.IFilePersistence;
import net.sf.joafip.service.ISaveEventListener;
import net.sf.joafip.service.JoafipJavaAgent;
import net.sf.joafip.store.entity.classinfo.ClassInfo;
import net.sf.joafip.store.service.classinfo.ClassInfoException;
import org.apache.log4j.Logger;
import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.common.InternalFactHandle;
import org.drools.common.InternalRuleBase;
import org.drools.common.SingleThreadedObjectStore;
import org.drools.conflict.DepthConflictResolver;
import org.drools.reteoo.ReteooRuleBase;
import org.drools.reteoo.ReteooStatefulSession;
import org.drools.util.JavaIteratorAdapter;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class DroolPersistence implements ISaveEventListener {

    private static final Logger _log = Logger.getLogger(DroolPersistence.class);

    private static final String RULES_BASE_KEY = "rulesBaseKey";

    private static final DroolPersistence INSTANCE = new DroolPersistence();

    /** the file persistence manager, null when service closed/not initialized */
    private IFilePersistence filePersistence;

    private Exception openerTrace;

    private Exception closerTrace;

    private IExclusiveDataAccessSession dataAccessSession;

    private boolean persistenceEnable;

    public static DroolPersistence getInstance() {
        return INSTANCE;
    }

    private DroolPersistence() {
        super();
        setCloserTrace();
        setOpenerTrace();
    }

    /**
	 * to initialize the service without persistence in file: all in memory
	 * 
	 */
    public void initialize() {
        if (_log.isInfoEnabled()) {
            _log.info("DroolPersistence.initialize(): all in memory ");
        }
        persistenceEnable = false;
        dataAccessSession = new InMemoryDataAccessSession();
    }

    /**
	 * to initialize the service with persistence in file
	 * 
	 * @param path
	 *            directory path where store files
	 * @throws DroolsPersistenceException
	 */
    public void initialize(final File path) throws DroolsPersistenceRuntimeException {
        System.out.println("DroolPersistence.initialize(): in file");
        if (!JoafipJavaAgent.isTransformerInstalled()) {
            throw new DroolsPersistenceRuntimeException("can run only with joafip-java-agent");
        }
        persistenceEnable = true;
        if (filePersistence != null) {
            throw new DroolsPersistenceRuntimeException("already initialized", openerTrace);
        }
        try {
            filePersistence = new FilePersistence(1, null, path, true, true, 1024 * 10, 100, false, false);
            if (false) {
                filePersistence.setStoreOnlyMarkedStorable(true);
                filePersistence.setStorable(new Class[] { n3_project.helpers.Triple.class, n3_project.helpers.TripleResult.class, unif.TripleUser.class, org.drools.ClockType.PSEUDO_CLOCK.getClass(), org.drools.ClockType.REALTIME_CLOCK.getClass(), java.util.concurrent.locks.ReentrantLock.class, org.drools.RuleBaseConfiguration.class, org.drools.SessionConfiguration.class, org.drools.ClockType.class, org.drools.RuleBaseConfiguration.LogicalOverride.class, org.drools.RuleBaseConfiguration.AssertBehaviour.class, org.drools.RuleBaseConfiguration.SequentialAgenda.class, org.drools.spi.Constraint.class, org.drools.spi.Constraint.ConstraintType.class, org.drools.spi.PatternExtractor.class, org.drools.conflict.DepthConflictResolver.class, org.drools.conf.EventProcessingOption.class, org.drools.common.DefaultFactHandle.class, org.drools.common.EqualityKey.class, org.drools.common.SingleBetaConstraints.class, org.drools.common.RuleBasePartitionId.class, org.drools.common.TripleBetaConstraints.class, org.drools.common.EmptyBetaConstraints.class, org.drools.common.DoubleBetaConstraints.class, org.drools.common.AgendaItem.class, org.drools.common.PropagationContextImpl.class, org.drools.common.BinaryHeapQueueAgendaGroup.class, org.drools.reteoo.Rete.class, org.drools.reteoo.InitialFactHandle.class, org.drools.reteoo.InitialFactHandleDummyObject.class, org.drools.reteoo.RightTuple.class, org.drools.reteoo.LeftTuple.class, org.drools.reteoo.JoinNode.class, org.drools.reteoo.NotNode.class, org.drools.reteoo.AlphaNode.class, org.drools.reteoo.EntryPointNode.class, org.drools.reteoo.RuleTerminalNode.class, org.drools.reteoo.ReteooStatefulSession.class, org.drools.reteoo.ReteooRuleBase.class, org.drools.reteoo.ObjectTypeNode.class, org.drools.reteoo.QueryTerminalNode.class, org.drools.reteoo.CompositeLeftTupleSinkAdapter.class, org.drools.reteoo.SingleLeftTupleSinkAdapter.class, org.drools.reteoo.LeftInputAdapterNode.class, org.drools.reteoo.CompositeObjectSinkAdapter.class, org.drools.reteoo.SingleObjectSinkAdapter.class, org.drools.reteoo.EmptyObjectSinkAdapter.class, org.drools.reteoo.CompositeObjectSinkAdapter.class, org.drools.reteoo.CompositeObjectSinkAdapter.FieldIndex.class, org.drools.reteoo.CompositeObjectSinkAdapter.HashKey.class, org.drools.reteoo.InitialFactImpl.class, org.drools.reteoo.LeftTupleSinkNodeList.class, org.drools.reteoo.ObjectSinkNodeList.class, org.drools.process.instance.event.DefaultSignalManagerFactory.class, org.drools.process.instance.impl.DefaultProcessInstanceManagerFactory.class, org.drools.rule.Rule.class, org.drools.rule.GroupElement.class, org.drools.rule.GroupElement.Type.class, org.drools.rule.Package.class, org.drools.rule.BehaviorManager.class, org.drools.rule.Declaration.class, org.drools.rule.VariableConstraint.class, org.drools.rule.LiteralConstraint.class, org.drools.rule.Pattern.class, org.drools.rule.VariableRestriction.class, org.drools.rule.EntryPoint.class, org.drools.rule.LiteralRestriction.class, org.drools.rule.Query.class, org.drools.util.RightTupleList.class, org.drools.util.LeftTupleList.class, org.drools.util.ChainedProperties.class, org.drools.util.AbstractHashTable.class, org.drools.util.AbstractHashTable.SingleIndex.class, org.drools.util.AbstractHashTable.DoubleCompositeIndex.class, org.drools.util.AbstractHashTable.EqualityEquals.class, org.drools.util.AbstractHashTable.FieldIndex.class, org.drools.util.AbstractHashTable.HashTableIterator.class, org.drools.util.AbstractHashTable.Index.class, org.drools.util.AbstractHashTable.InstanceEquals.class, org.drools.util.AbstractHashTable.ObjectComparator.class, org.drools.util.AbstractHashTable.TripleCompositeIndex.class, org.drools.util.LinkedList.class, org.drools.util.LinkedList.JavaUtilIterator.class, org.drools.util.LinkedList.LinkedListIterator.class, org.drools.util.ObjectHashMap.class, org.drools.util.ObjectHashMap.ObjectEntry.class, org.drools.util.BinaryHeapQueue.class, org.drools.base.ClassFieldReader.class, org.drools.base.ClassObjectType.class, org.drools.base.ValueType.class, org.drools.base.SalienceInteger.class, org.drools.base.EnabledBoolean.class, org.drools.base.field.ObjectFieldImpl.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.ArrayEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.ArrayNotEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.BigDecimalEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.BigDecimalNotEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.BigIntegerEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.BigIntegerNotEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.BooleanEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.BooleanNotEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.ByteEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.ByteNotEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.CharacterEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.CharacterNotEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.DateEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.DateNotEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.DoubleEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.DoubleNotEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.FactTemplateEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.FactTemplateNotEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.FloatEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.FloatNotEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.IntegerEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.IntegerNotEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.LongEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.LongNotEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.ObjectEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.ObjectNotEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.ShortEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.ShortNotEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.StringEqualEvaluator.class, org.drools.base.evaluators.EqualityEvaluatorsDefinition.StringNotEqualEvaluator.class, org.drools.base.evaluators.Operator.class });
                filePersistence.setStorable(new String[] { "org.drools.base.evaluators.EqualityEvaluatorsDefinition$ObjectEqualsComparator" });
            }
            filePersistence.setRecordSaveActions(true);
            filePersistence.setStoreSerializeInOneRecord(new Class[] { n3_project.helpers.Triple.class, n3_project.helpers.TripleResult.class, unif.TripleUser.class, java.util.concurrent.locks.ReentrantLock.class, java.util.Properties.class, java.util.concurrent.atomic.AtomicInteger.class, java.util.concurrent.atomic.AtomicLong.class, java.lang.Class.class });
            filePersistence.setNoLazyLoad(new Class[] { org.drools.reteoo.ReteooFactHandleFactory.class });
            filePersistence.setNoLazyLoad(new String[] { "java.util.Collections$SynchronizedMap" });
            filePersistence.setSubstitutionOfJavaUtilCollection();
            final ReteooRuleBaseSubstituteMgr reteooRuleBaseSubstituteMgr = new ReteooRuleBaseSubstituteMgr();
            filePersistence.setSubstituteObjectManager(ReteooRuleBase.class, ReteooRuleBaseEntity.class, reteooRuleBaseSubstituteMgr);
            final ReteooStatefulSessionSubstituteMgr reteooStatefulSessionSubstituteMgr = new ReteooStatefulSessionSubstituteMgr();
            filePersistence.setSubstituteObjectManager(ReteooStatefulSession.class, ReteooStatefulSessionEntity.class, reteooStatefulSessionSubstituteMgr);
            final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                filePersistence.keptInMemory("java.lang.ClassLoader@thread", contextClassLoader);
            }
            final ClassLoader classLoader = getClass().getClassLoader();
            if (classLoader != contextClassLoader) {
                filePersistence.keptInMemory("java.lang.ClassLoader@Class", classLoader);
            }
            filePersistence.keptInMemory("org.drools.conflict.DepthConflictResolver@instance", DepthConflictResolver.getInstance());
            filePersistence.keptInMemory("java.util.Collections$EmptyMap", java.util.Collections.EMPTY_MAP);
            filePersistence.setSaveEventListener(this);
            final String voidTypeDesc = Type.getDescriptor(void.class);
            final String objectTypeDesc = Type.getDescriptor(Object.class);
            final String factHandleTypeDesc = Type.getDescriptor(FactHandle.class);
            final String internalFactHandleTypeDesc = Type.getDescriptor(InternalFactHandle.class);
            Class<?> clazz = SingleThreadedObjectStore.class;
            filePersistence.addInterceptMethodEnd(clazz, "clear", voidTypeDesc, new String[] {});
            filePersistence.addInterceptMethodEnd(clazz, "getObjectForHandle", objectTypeDesc, new String[] { factHandleTypeDesc });
            filePersistence.addInterceptMethodEnd(clazz, "getHandleForObject", internalFactHandleTypeDesc, new String[] { objectTypeDesc });
            filePersistence.addInterceptMethodEnd(clazz, "reconnect", internalFactHandleTypeDesc, new String[] { factHandleTypeDesc });
            filePersistence.addInterceptMethodEnd(clazz, "getHandleForObjectIdentity", internalFactHandleTypeDesc, new String[] { objectTypeDesc });
            filePersistence.addInterceptMethodEnd(clazz, "updateHandle", voidTypeDesc, new String[] { internalFactHandleTypeDesc, objectTypeDesc });
            filePersistence.addInterceptMethodEnd(clazz, "addHandle", voidTypeDesc, new String[] { internalFactHandleTypeDesc, objectTypeDesc });
            filePersistence.addInterceptMethodEnd(clazz, "removeHandle", voidTypeDesc, new String[] { factHandleTypeDesc });
            clazz = JavaIteratorAdapter.class;
            filePersistence.addInterceptMethodEnd(clazz, "next", objectTypeDesc, new String[] {});
            dataAccessSession = filePersistence.createExclusiveDataAccessSession();
            dataAccessSession.open();
        } catch (Exception exception) {
            throw new DroolsPersistenceRuntimeException(exception);
        }
        setOpenerTrace();
        if (_log.isInfoEnabled()) {
            _log.info("DroolPersistence.initialize(): path " + path);
        }
    }

    public void keptInMemory(final String key, final Object object) {
        assertOpened();
        if (persistenceEnable) {
            try {
                filePersistence.keptInMemory(key, object);
            } catch (FilePersistenceException exception) {
                throw new DroolsPersistenceRuntimeException(exception);
            }
        }
    }

    /**
	 * to close the service
	 * 
	 * @throws DroolsPersistenceException
	 */
    public void close() throws DroolsPersistenceRuntimeException {
        if (persistenceEnable) {
            final Collection<ClassInfo> allClassInformation;
            if (filePersistence == null) {
                throw new DroolsPersistenceRuntimeException("Drools Persistence already closed", closerTrace);
            }
            try {
                if (dataAccessSession.isOpened()) {
                    dataAccessSession.close();
                }
                allClassInformation = filePersistence.allClassInformation();
                filePersistence.close();
            } catch (Exception exception) {
                throw new DroolsPersistenceRuntimeException(exception);
            }
            filePersistence = null;
            dataAccessSession = null;
            if (_log.isInfoEnabled()) {
                System.out.println("drools accessed classes");
                int count = 0;
                for (ClassInfo classInfo : allClassInformation) {
                    try {
                        final String name = classInfo.getName();
                        if (name.startsWith("org.drools.")) {
                            System.out.print("- ");
                            System.out.println(name);
                            count++;
                        }
                    } catch (ClassInfoException exception) {
                        exception.printStackTrace();
                    }
                }
                System.out.println("total number of classes=" + count);
                System.out.println("\ntransformed classes:");
                for (String className : JoafipJavaAgent.getTransformedSet()) {
                    System.out.print("- ");
                    System.out.println(className);
                }
            }
        }
        setCloserTrace();
        System.out.println("DroolPersistence.close()");
    }

    public boolean isOpen() {
        return filePersistence != null;
    }

    private void assertOpened() {
        if ((persistenceEnable && filePersistence == null) || dataAccessSession == null) {
            throw new DroolsPersistenceRuntimeException("Drools Persistence not initialized", closerTrace);
        }
    }

    private void setCloserTrace() {
        closerTrace = new Exception("closer");
        openerTrace = null;
    }

    private void setOpenerTrace() {
        openerTrace = new Exception("opener");
        closerTrace = null;
    }

    public void save() {
        assertOpened();
        try {
            dataAccessSession.save();
        } catch (Exception exception) {
            throw new DroolsPersistenceRuntimeException(exception);
        }
    }

    /**
	 * add a rule base in storage
	 * 
	 * @param ruleBase
	 * @param ruleBaseId
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public void addRuleBase(final RuleBase ruleBase) {
        assertOpened();
        try {
            final String ruleBaseId = ((InternalRuleBase) ruleBase).getId();
            Map<String, RuleBase> map = (Map<String, RuleBase>) dataAccessSession.getObject(RULES_BASE_KEY);
            if (map == null) {
                map = new PTreeMap<String, RuleBase>();
                dataAccessSession.setObject(RULES_BASE_KEY, map);
            }
            map.put(ruleBaseId, ruleBase);
            dataAccessSession.save();
        } catch (Exception exception) {
            throw new DroolsPersistenceRuntimeException(exception);
        }
    }

    /**
	 * get a rule base from storage
	 * 
	 * @param ruleBaseId
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public RuleBase getRuleBase(final String ruleBaseId) {
        assertOpened();
        try {
            final Map<String, RuleBase> map = (Map<String, RuleBase>) dataAccessSession.getObject(RULES_BASE_KEY);
            final RuleBase ruleBase;
            if (map == null) {
                ruleBase = null;
            } else {
                ruleBase = map.get(ruleBaseId);
            }
            return ruleBase;
        } catch (Exception exception) {
            throw new DroolsPersistenceRuntimeException(exception);
        }
    }

    public int getClassIdentifier(Class<?> clazz) {
        assertOpened();
        try {
            return dataAccessSession.getClassIdentifier(clazz);
        } catch (Exception exception) {
            throw new DroolsPersistenceRuntimeException(exception);
        }
    }

    @Override
    public boolean doSave() {
        return true;
    }

    @Override
    public void saveDone(final boolean closing) {
        System.out.println("------ save, closing=" + closing);
        if (_log.isInfoEnabled()) {
            final StringBuilder stringBuilder = new StringBuilder();
            final Map<String, MutableInteger> writedSet = filePersistence.getWroteObjectSet();
            stringBuilder.append("---- saved, for closing : ");
            stringBuilder.append(closing);
            stringBuilder.append(" ----\n");
            int total = 0;
            for (Map.Entry<String, MutableInteger> wrote : writedSet.entrySet()) {
                stringBuilder.append("  ");
                stringBuilder.append(wrote.getKey());
                stringBuilder.append(" : ");
                final int value = wrote.getValue().getValue();
                stringBuilder.append(value);
                stringBuilder.append('\n');
                total += value;
            }
            stringBuilder.append("---- total ");
            stringBuilder.append(total);
            stringBuilder.append(" saved ----\n");
            stringBuilder.append("---- array saved size ----\n");
            final List<String> wroteArrays = filePersistence.getWroteArrays();
            for (String wrote : wroteArrays) {
                stringBuilder.append(wrote);
                stringBuilder.append('\n');
            }
            _log.info(stringBuilder.toString());
        }
    }
}
