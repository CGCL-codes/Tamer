package domain.structure_new.components.meristems;

import infrastructure.utilities.Utils;
import domain.structure_new.attributes.plantcomponent_attributes.Navigation;
import domain.structure_new.attributes.ramet_attributes.Resources;
import domain.structure_new.components.AbstractPlantComposite;
import domain.structure_new.components.organs.FlowerWithMeris;
import domain.structure_new.components.plant.IResourcesAllocator;

/**
 *
 * @author Uwe Grueters,	email: uwe.grueters@bot2.bio.uni-giessen.de
 * @author Roland Dahlem,	email: roland.dahlem@mni.fh-giessen.de
 */
public class FlowerMeristem extends AbstractMeristem {

    private static final long serialVersionUID = 1L;

    public FlowerMeristem(AbstractPlantComposite parent, IResourcesAllocator allocator, AbstractMeristem.Type merisType, Navigation merisNavig) {
        super(parent);
        parent.addChild(this);
        String fullClassName = FlowerMeristem.class.getName();
        String className = Utils.getClassName(fullClassName);
        setIdentity(allocator, className);
        setMyName(className);
        allocator.addComponent(this);
        if (merisType == AbstractMeristem.Type.additionalFlowerMeris) {
            this.branchingOrder++;
        }
        this.merisType = merisType;
        this.navigation = merisNavig;
    }

    @Override
    public void growOrgan(IResourcesAllocator allocator) {
        FlowerWithMeris newFlower = new FlowerWithMeris(this, allocator, new Resources());
        this.setActive(false);
    }
}
