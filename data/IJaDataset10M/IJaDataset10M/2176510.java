package domain.structure_new.components.plant;

import infrastructure.utilities.BinaryNode;
import infrastructure.utilities.BinarySearchTree;
import infrastructure.utilities.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import domain.structure_new.attributes.ramet_attributes.Demand;
import domain.structure_new.attributes.ramet_attributes.Resources;
import domain.structure_new.attributes.ramet_attributes.Storage;
import domain.structure_new.attributes.ramet_attributes.Structure;
import domain.structure_new.components.AbstractOwnObservable;
import domain.structure_new.components.AbstractPlantComponent;
import domain.structure_new.components.AbstractPlantComposite;
import domain.structure_new.components.IOwnObserver;
import domain.structure_new.components.organs.CGOElementWithMeris;
import domain.structure_new.components.organs.FineRoots;
import domain.structure_new.components.organs.Leaf;

/**
 * 
 * @author Uwe Grueters, email: uwe.grueters@bot2.bio.uni-giessen.de
 */
public class RametNetwork extends AbstractRametContainer implements IRametParent, IOwnObserver {

    private static final long serialVersionUID = 1L;

    private static int MAX_ID;

    private static int COUNT_RAMETNETWORKS;

    private int motherID;

    private int fatherID;

    private int maxRametID;

    private int countRamets;

    private double carbonDemand_g;

    private BinarySearchTree cgoElementSearchTree = new BinarySearchTree();

    /**
	 * Constructor:
	 * 
	 * @param parent
	 */
    public RametNetwork(AbstractPlantComposite parent, Resources resources) {
        super(parent);
        parent.addChild(this);
        setIdentity(MAX_ID);
        MAX_ID++;
        COUNT_RAMETNETWORKS++;
        setMyName();
        if (!resources.isEmpty()) {
            this.totalStructuralCarbon_g = resources.carbon_g;
            this.totalNitrogenInStructure_g = resources.nitrogen_g;
            new Ramet(this, resources);
            countRamets++;
        }
    }

    /**
	 * 
	 */
    private void setMyName() {
        String fullClassName = RametNetwork.class.getName();
        String className = Utils.getClassName(fullClassName);
        super.setMyName(className);
    }

    /**
	 * @return the mAX_ID
	 */
    public static int getMAX_ID() {
        return MAX_ID;
    }

    /**
	 * @return the maxRametID
	 */
    public int getMaxRametID() {
        return maxRametID;
    }

    /**
	 * @return the countRamets
	 */
    public int getCountRamets() {
        return countRamets;
    }

    /**
	 * @return the fatherID
	 */
    public int getFatherID() {
        return fatherID;
    }

    /**
	 * @return the motherID
	 */
    public int getMotherID() {
        return motherID;
    }

    /**
	 * @param countRamets
	 *            the countRamets to set
	 */
    public void setCountRamets(int countRamets) {
        this.countRamets = countRamets;
    }

    /**
	 * @param maxRametID
	 *            the maxRametID to set
	 */
    public void setMaxRametID(int maxRametID) {
        this.maxRametID = maxRametID;
    }

    public void placeOnDemand_g(int myID, double requiredCarbon_g) {
        carbonDemand_g += requiredCarbon_g;
    }

    /**
	 * 
	 */
    public void reallocateCStorage() {
        double carbonDemandStoragdRatio_g_per_g = computeCarbonDemandStorageRatio_g_per_g();
        Iterator<AbstractPlantComponent> childIter = childComponents.iterator();
        while (childIter.hasNext()) {
            AbstractPlantComponent next = childIter.next();
            if (next instanceof Ramet) {
                Ramet nextRamet = (Ramet) next;
                double amountToBeRemovedFromStorage_g = Math.min(1.0d, carbonDemandStoragdRatio_g_per_g) * nextRamet.getCarbonStorage_g().getAmountStored_g();
                nextRamet.getCarbonStorage_g().getFromStorage(amountToBeRemovedFromStorage_g);
                double amountAvailableToSatisfyDemand_g = Math.min(1.0d, 1 / carbonDemandStoragdRatio_g_per_g) * nextRamet.getCarbonDemand_g().getDemand_g();
                nextRamet.getCarbonDemand_g().satisfyDemand(amountAvailableToSatisfyDemand_g);
            }
        }
    }

    /**
	 * @return
	 */
    private double computeCarbonDemandStorageRatio_g_per_g() {
        Storage carbonStorage = new Storage(Storage.Type.Carbon);
        carbonStorage.setMaximumStorableAmount_g(0.0d);
        Demand carbonDemand = new Demand();
        Iterator<AbstractPlantComponent> childIter = childComponents.iterator();
        while (childIter.hasNext()) {
            AbstractPlantComponent next = childIter.next();
            if (next instanceof Ramet) {
                Ramet nextRamet = (Ramet) next;
                Storage newStorage = nextRamet.getCarbonStorage_g();
                if (newStorage.getAmountStored_g() > 0.0d) {
                    carbonStorage.addStorage(newStorage);
                }
                carbonDemand.addDemand(nextRamet.getCarbonDemand_g());
            }
        }
        double carbonDemandStorageRatio_g_per_g = carbonDemand.getDemand_g() / carbonStorage.getAmountStored_g();
        return carbonDemandStorageRatio_g_per_g;
    }

    /**
	 * @return the cOUNT_RAMETNETWORKS
	 */
    public static int getCOUNT_RAMETNETWORKS() {
        return COUNT_RAMETNETWORKS;
    }

    /**
	 * @return the cgoElementSearchTree
	 */
    public BinarySearchTree getCgoElementSearchTree() {
        return cgoElementSearchTree;
    }

    public void setCgoElementSearchTree(BinarySearchTree tree) {
        this.cgoElementSearchTree = tree;
    }

    public void correctPaths() {
        Iterator<AbstractPlantComponent> rametIter = this.createChildIterator();
        if (rametIter.hasNext()) {
            Ramet nextRamet = (Ramet) rametIter.next();
            nextRamet.getPath().removeFirst();
        }
        Iterator<BinaryNode> cgoIter = this.getCgoElementSearchTree().createDepthFirstIterator();
        if (cgoIter.hasNext()) {
            CGOElementWithMeris cgoElement = (CGOElementWithMeris) cgoIter.next().element;
            cgoElement.getPath().removeFirst();
        }
    }

    public void update(AbstractOwnObservable observable, Object obj) {
        if (obj instanceof Resources) {
        } else if (obj instanceof Structure) {
        } else if (obj instanceof CGOElementWithMeris) {
            CGOElementWithMeris cgoElement = (CGOElementWithMeris) obj;
            detachRametNetworkAt(cgoElement);
        }
    }

    private void detachRametNetworkAt(CGOElementWithMeris cgoElement) {
        BinaryNode myOldNode = this.getCgoElementSearchTree().findNode(cgoElement);
        BinaryNode myRightNode = this.getCgoElementSearchTree().removeNode(cgoElement);
        BinarySearchTree myNewCGOElementSearchTree = this.getCgoElementSearchTree();
        if (myRightNode != null) {
            BinarySearchTree rightSubtree = new BinarySearchTree(myRightNode);
            RametNetwork newRametNetwork = new RametNetwork(this.getClonalPlant(), new Resources());
            Iterator<BinaryNode> nodeIter = rightSubtree.createDepthFirstIterator();
            if (nodeIter.hasNext()) {
                CGOElementWithMeris nextCGOElement = (CGOElementWithMeris) nodeIter.next().element;
                if (nextCGOElement.serializableObserver instanceof RametNetwork) {
                    nextCGOElement.deleteSerializableObserver();
                    nextCGOElement.addSerializableObserver(newRametNetwork);
                }
            }
            newRametNetwork.setCgoElementSearchTree(rightSubtree);
            int size = this.getChildComponents().size();
            ArrayList<AbstractPlantComponent> rametsToTransfer = new ArrayList<AbstractPlantComponent>();
            for (int i = size - 1; i >= 0; i--) {
                Ramet rametToTransfer = (Ramet) this.getChild(i);
                if (rightSubtree.find(rametToTransfer) != null) {
                    rametsToTransfer.add(rametToTransfer);
                }
            }
            for (AbstractPlantComponent rametToTransfer : rametsToTransfer) {
                newRametNetwork.addChild(rametToTransfer);
                this.removeChild(rametToTransfer);
            }
            if (!myNewCGOElementSearchTree.checkForFurtherRightNodes(myOldNode)) {
                getClonalPlant().correctPaths();
                this.setRemoved(true);
                this.getParent().removeChild(this);
            }
        }
    }
}
