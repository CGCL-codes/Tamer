package domain.components.meristems;

import domain.attributes.AttributesServiceFacade;
import domain.attributes.organ_attributes.RootsStructuralAttributes;
import domain.attributes.ramet_attributes.Resources;
import domain.components.AbstractPlantComponent;
import domain.components.AbstractPlantComposite;
import domain.components.organs.BaseFineRoots;
import domain.components.plant.IResourcesAllocator;

public class BaseFineRootsMeristem extends AbstractMeristem {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2444199782438369747L;

    private boolean firstStep;

    public BaseFineRootsMeristem(AbstractPlantComposite parent) {
        super(parent);
        parent.addChild(this);
        this.navigation.pitchDown(Math.PI);
        this.firstStep = true;
    }

    public AbstractPlantComponent growOrgan(IResourcesAllocator allocator) {
        String specName = allocator.getSpecName();
        AttributesServiceFacade attributesService = AttributesServiceFacade.getInstanceFor(specName);
        Resources realizedFineRootsResources;
        RootsStructuralAttributes newStructure;
        double scale = allocator.getBelowgroundResourcesReducer();
        realizedFineRootsResources = allocator.getPartitioningPattern().fineRootsResources.scale(scale);
        newStructure = attributesService.ccomputeFineRootsStructuralAttributes(realizedFineRootsResources);
        BaseFineRoots baseFineRoots = null;
        if (firstStep) {
            firstStep = false;
            baseFineRoots = new BaseFineRoots(this);
            baseFineRoots.addStructure(newStructure);
        } else {
            ((BaseFineRoots) this.childComponents.get(0)).addStructure(newStructure);
        }
        return baseFineRoots;
    }
}
