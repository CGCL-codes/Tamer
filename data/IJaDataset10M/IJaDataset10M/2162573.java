package ontool.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ontool.model.ClassModel;
import ontool.model.FunctionModel;
import ontool.model.InternalClassModel;
import ontool.model.PlaceModel;
import ontool.util.CollectionUtils;
import org.apache.commons.pool.KeyedObjectPool;
import org.apache.log4j.Category;

/**
 * Run-time place.
 *
 * @author <a href="mailto:asrgomes@dca.fee.unicamp.br">Antonio S.R. Gomes<a/>
 * @version $Id: Place.java,v 1.1 2003/10/22 03:06:40 asrgomes Exp $
 */
public class Place extends GenericPlace implements EffectorHandler {

    public static final boolean DEBUG = Constants.DEBUG && true;

    private Category logger;

    /** Objects contained in this place */
    private List objects = new ArrayList();

    private FunctionModel[] functions;

    private ClassModel classModel;

    private InternalClassModel icm;

    private PlaceModel placeModel;

    /** Objects through the i-th sensor. */
    private List[] sensorScope_cache;

    /** Combinations for each function [0 .. functions.length]*/
    private List[] funcCombinations;

    private KeyedObjectPool combinationPool;

    private KeyedObjectPool actionPool;

    private String containerName;

    /**
	 * Creates a new place.
	 * @param engine simulation engine
	 * @param model place model
	 * @param scope reference scope
	 */
    public Place(Engine engine, PlaceModel placeModel, Page scope) {
        super(engine, placeModel, scope);
        this.placeModel = placeModel;
        this.classModel = (ClassModel) placeModel.getClassType();
        if (classModel instanceof InternalClassModel) icm = (InternalClassModel) classModel;
        logger = Category.getInstance(Place.class.getName() + "[" + getName() + "@" + getPage().getName());
        containerName = getName() + "@" + getPage().getName();
        List funcList = classModel.getChildren(FunctionModel.class);
        if (!funcList.isEmpty()) {
            functions = new FunctionModel[funcList.size()];
            int idx = 0;
            for (Iterator i = funcList.iterator(); i.hasNext(); ) functions[idx++] = (FunctionModel) i.next();
        }
        combinationPool = getEngine().getCombinationPool();
        actionPool = getEngine().getActionPool();
    }

    /**
	 * @see ontool.engine.GenericPlace#getContainerName()
	 */
    public String getContainerName() {
        return containerName;
    }

    /**
	 * @see ontool.engine.ObjectContainer#putObject(ontool.engine.NetObject)
	 */
    public void putObject(NetObject obj) {
        assert obj.__getOwner() == null : "owner == null";
        assert !objects.contains(obj) : "place " + getContainerName() + " does no contain " + obj.__getName();
        ObjectContainer oldContainer = obj.__getContainer();
        assert oldContainer == null : "Object is still placed in " + oldContainer.getContainerName();
        if (logger.isDebugEnabled()) logger.debug("adding object " + obj.__getName());
        if (obj.__getModel() != getModel().getGenericType()) throw new EngineException("invalid class model in place type " + getModel().getGenericType() + " : " + obj.__getModel());
        objects.add(obj);
        obj.__setContainer(this);
        if (bNotify) notifyEvent(new PlaceEvent(this, PlaceEvent.EV_PUT_OBJECT, obj));
    }

    /**
	 * @see ontool.engine.ObjectContainer#removeObject(ontool.engine.NetObject)
	 */
    public void removeObject(NetObject obj) {
        if (logger.isDebugEnabled()) logger.debug("removing object " + obj.__getName());
        obj.__setContainer(null);
        objects.remove(obj);
        if (bNotify) notifyEvent(new PlaceEvent(this, PlaceEvent.EV_REMOVE_OBJECT, obj));
    }

    /**
	 * @see ontool.engine.ObjectContainer#getObjects()
	 */
    public List getObjects() {
        return objects;
    }

    /**
	 * @see ontool.engine.GenericPlace#compile()
	 */
    public void compile() {
        logger.info("compiling place " + getName());
        super.compile();
        if (bNotify) notifyEvent(new PlaceEvent(this, PlaceEvent.EV_COMPILE));
    }

    /**
	 * Indicates if this place is active.
	 * return flag
	 */
    public boolean isActive() {
        return functions != null && functions.length > 0;
    }

    /**
	 * Creates actions for objects in this place.
	 * 
	 * @param action list to include the new actions
	 * @throws UserException if an exception was thrown from within the user
	 *          code
	 * @throws EngineException if this place is passive
	 */
    public void createActions(List actions) throws UserException {
        if (isActive()) {
            if (logger.isDebugEnabled()) logger.debug("creating actions");
            createCombinations();
            for (int i = 0; i < functions.length; i++) createActionsForFunc(functions[i], actions);
        } else throw new EngineException("Passive places cannot create actions");
    }

    /**
	 * @see ontool.engine.EffectorHandler#sendObjectToEffector(int, ontool.engine.NetObject, ontool.engine.Action)
	 */
    public void sendObjectToEffector(int effectorId, NetObject outgoingObj, Action action) {
        if (outgoingObj != null) {
            assert outgoingObj.__getOwner() == null;
            assert outgoingObj.__getContainer() == null;
            GenericPlace[] toPlace = getV2();
            toPlace[effectorId].putObject(outgoingObj);
        }
    }

    /**
	 * Performs a given action.
	 * 
	 * @param action
	 * @throws UserException
	 */
    public void performAction(Action action) throws UserException {
        InternalNetObjectImpl obj = (InternalNetObjectImpl) action.getRequester();
        if (logger.isDebugEnabled()) logger.debug("Object " + obj.__getName() + " performing");
        obj.__attachAction(action, true);
        obj.setEffectorHandler(this);
        try {
            obj.__perform();
        } catch (Exception e) {
            throw new UserException(obj, e);
        } catch (AssertionError e) {
            throw new UserException(obj, e);
        } finally {
            obj.__detachAction();
            obj.setEffectorHandler(null);
        }
    }

    public void dump() {
        logger.debug("objects in " + getName() + "= " + CollectionUtils.dumpNetObjects(getObjects()));
    }

    /**
	 * Prepares this place to the begining of the solving scheduling process.
	 */
    public void prepare() {
        if (isActive()) {
            double ib = getEngine().getInitialBalance();
            for (Iterator i = getObjects().iterator(); i.hasNext(); ) {
                InternalNetObjectImpl obj = (InternalNetObjectImpl) i.next();
                obj.setBalance(ib);
                obj.setFireCount(0);
            }
        }
    }

    public void cleanUp() {
        try {
            for (int i = 0; i < funcCombinations.length; i++) {
                for (Iterator iter = funcCombinations[i].iterator(); iter.hasNext(); ) combinationPool.returnObject(functions[i], iter.next());
            }
        } catch (Exception e) {
            throw new EngineException("error returning combinations to pool", e);
        }
    }

    /**
	 * Creates combinations for each function.
	 */
    private void createCombinations() {
        if (logger.isDebugEnabled()) logger.debug("creating combinations");
        GenericPlace[] F1 = getF1();
        if (sensorScope_cache == null) sensorScope_cache = new List[F1.length];
        for (int i = 0; i < sensorScope_cache.length; i++) sensorScope_cache[i] = F1[i].getObjects();
        for (int i = 0; i < functions.length; i++) createCombsForFunction(sensorScope_cache, functions[i]);
    }

    /**
	 * Creates combinations for a function.
	 * @param funcId
	 * @see #combinations
	 */
    private void createCombsForFunction(List[] scope, FunctionModel funcModel) {
        if (logger.isDebugEnabled()) logger.debug("creating combinations for function " + funcModel);
        int[] idx = new int[icm.getSensorCount()];
        for (int i = 0; i < idx.length; i++) idx[i] = 0;
        int[] n = new int[idx.length];
        int maxCombCount = 1;
        int[] alpha = funcModel.getAlpha();
        for (int i = 0; i < alpha.length; i++) {
            int _i = alpha[i];
            n[_i] = scope[_i].size();
            maxCombCount *= n[_i];
        }
        if (logger.isDebugEnabled()) logger.debug("number of combinations = " + maxCombCount);
        int combCount = 0;
        if (funcCombinations == null) funcCombinations = new List[functions.length];
        List combinations = funcCombinations[funcModel.getIndex()];
        if (combinations == null) combinations = funcCombinations[funcModel.getIndex()] = new ArrayList(); else combinations.clear();
        try {
            if (alpha.length == 0) {
                NetObject[] comb = (NetObject[]) combinationPool.borrowObject(classModel);
                combinations.add(comb);
                return;
            } else {
                if (maxCombCount == 0) return;
                while (combCount < maxCombCount) {
                    if (logger.isDebugEnabled()) logger.debug("indexes = " + CollectionUtils.dump(idx));
                    NetObject[] comb = (NetObject[]) combinationPool.borrowObject(classModel);
                    combinations.add(comb);
                    for (int i = 0; i < alpha.length; i++) {
                        int _i = alpha[i];
                        comb[_i] = (NetObject) scope[_i].get(idx[_i]);
                    }
                    for (int i = alpha.length - 1; i >= 0; i--) {
                        int _i = alpha[i];
                        idx[_i]++;
                        if (idx[_i] == n[_i]) {
                            idx[_i] = 0;
                            continue;
                        }
                        break;
                    }
                    combCount++;
                }
            }
        } catch (Exception any) {
            throw new EngineException("could not create combinations", any);
        }
    }

    /**
	 * Creates actions for the given function for all objects located in this
	 * place.
	 *
	 * @param funcModel
	 * @param actions
	 * @throws UserException
	 */
    private void createActionsForFunc(FunctionModel funcModel, List actions) throws UserException {
        try {
            int funcId = funcModel.getIndex();
            for (Iterator i = getObjects().iterator(); i.hasNext(); ) {
                InternalNetObjectImpl obj = (InternalNetObjectImpl) i.next();
                int combCount = funcCombinations[funcId].size();
                if (logger.isDebugEnabled()) {
                    logger.debug("creating action for active object " + obj.__getName() + " func " + funcModel);
                    logger.debug("number of actions to create = " + combCount);
                }
                for (Iterator ii = funcCombinations[funcId].iterator(); ii.hasNext(); ) {
                    NetObject[] comb = (NetObject[]) ii.next();
                    boolean ignore = false;
                    for (int j = 0; j < comb.length; j++) {
                        if (comb[j] == obj) {
                            ignore = true;
                            break;
                        }
                    }
                    if (ignore) continue;
                    obj.preEval();
                    obj.generateActions(actions, comb, funcModel, actionPool);
                    obj.postEval();
                }
            }
        } catch (UserException ue) {
            throw (UserException) ue;
        } catch (Exception any) {
            throw new EngineException("Error creating actions", any);
        }
    }
}
