package application.agents.agents3d;

import infrastructure.utilities.RandomGen;
import javax.media.j3d.Transform3D;
import sim.engine.SimState;
import sim.util.Int3D;
import sim.util.Proxiable;
import application.agents.AbstractAgent;
import application.agents.AtmosphericEnvironmentLayerAgent;
import application.agents.CanopyLayerAgent;
import application.agents.IAtmosphericEnvironmentManipulator;
import application.agents.agents3d.InspectableLeafAgent.MyProxy;
import application.model.UIBM;
import domain.environment.AnalyticalReferenceAtmosphericEnvironment;
import domain.environment.CanopyLayer;
import domain.environment.ExperimentalAtmosphericEnvironment;
import domain.environment.IAtmosphericEnvironment;
import domain.environment.RadiationProfileService;
import domain.environment.SimpleAtmosphericEnvironment;
import domain.functions.attributes.IRespiratoryAttributesService;
import domain.functions.mass_energy_exchange.MassEnergyExchangeVisitor;
import domain.functions.mass_energy_exchange.RespirationVisitor;
import domain.structure_new.attributes.organ_attributes.LeafStructuralAttributes;
import domain.structure_new.components.organs.Leaf;
import domain.structure_new.components.organs.LeafArea;

/**
 *
 * @author Uwe Grueters,	email: uwe.grueters@bot2.bio.uni-giessen.de
 */
public class LeafAreaAgent extends AbstractVegetativeOrganAgent implements Proxiable {

    private static final long serialVersionUID = -9152831744266224628L;

    /**
		 * This field variable is only true at leaf construction time.
		 * 
		 * Note: Since the constructor is called each time the object is
		 * deserialized, the behavior is not correct with serialization.
		 */
    private boolean hasChanged;

    private IAtmosphericEnvironment atmosphericEnvironment;

    /**
		 * Constructor
		 * 
		 * @param leafArea
		 */
    public LeafAreaAgent(LeafArea leafArea) {
        super(leafArea);
    }

    @Override
    public void step(SimState state) {
        super.step(state);
        UIBM uibm = (UIBM) state;
        double period_h = UIBM.timeStep_h;
        LeafArea leafArea = getLeafArea();
        if (!leafArea.isSenescent() && !leafArea.isDead()) {
            if (this.isConductingExperiment()) {
                IAtmosphericEnvironment env = SimpleAtmosphericEnvironment.getInstance(uibm.calendar);
                this.atmosphericEnvironment = new ExperimentalAtmosphericEnvironment(env);
            } else {
                Int3D myIntLocation = this.getNavigation().getIntLocation3D();
                AtmosphericEnvironmentLayerAgent environmentAgent = (AtmosphericEnvironmentLayerAgent) uibm.getLayeredAtmosphericEnvironmentGrid().get(myIntLocation.getX(), myIntLocation.getY(), myIntLocation.getZ());
                this.atmosphericEnvironment = environmentAgent.getEnvironment();
            }
            MassEnergyExchangeVisitor photosynthesisVisitor = MassEnergyExchangeVisitor.getInstance();
            RespirationVisitor respirationVisitor = RespirationVisitor.getInstance();
            boolean illuminatedSurface = computeIlluminationProbality(uibm, atmosphericEnvironment);
            leafArea.getPhotosyntheticalAttributesService().accept(photosynthesisVisitor, atmosphericEnvironment, illuminatedSurface, period_h);
            IRespiratoryAttributesService respiratoryAttributesService = leafArea.getRespiratoryAttributesService();
            respiratoryAttributesService.accept(respirationVisitor, atmosphericEnvironment, period_h);
        }
        if (leafArea.isRemoved()) {
            leafArea.setChanged();
            leafArea.notifyObservers(this);
            System.out.println(this + " has been removed at time: " + uibm.schedule.getSteps());
        }
    }

    /**
		 * @see application.agents.agents3d.Abstract3dAgent#hasChanged()
		 * 
		 * @returns true at the first call to this method and false afterwards This
		 *          shall result in the LeafAgent's attributes being read by the
		 *          portrayal only immediately after LeafAgent's creation, but no
		 *          later.
		 * 
		 * Note: It works, but we are not sure whether it accomplishes the task.
		 */
    @Override
    public boolean hasChanged() {
        if (this.hasChanged == true) {
            this.hasChanged = false;
            return true;
        } else {
            return false;
        }
    }

    public LeafArea getLeafArea() {
        return (LeafArea) this.getMyComponent();
    }

    /**
		 * This method returns the width of the actual underlying leaf
		 * divided by the width of the minimum leaf. 
		 */
    @Override
    protected double getScaleX() {
        return 0.1d;
    }

    /**
		 * This method returns the length of the actual underlying leaf
		 * divided by the length of the minimum leaf. The result is used 
		 * for y- and z-scaling in the getScale() method.  
		 */
    @Override
    protected double getScaleY() {
        return 0.1d;
    }

    @Override
    protected double getScaleZ() {
        return getLeafArea().getArea_cm2();
    }

    /**
		 * @return the atmosphericEnvironment
		 */
    public IAtmosphericEnvironment getAtmosphericEnvironment() {
        return atmosphericEnvironment;
    }

    @Override
    public Object propertiesProxy() {
        return new MyProxy();
    }

    /**
		 * This inner class is part of the Proxiable interface and is responsible
		 * for inspection of certain ClonalPlant properties.
		 * 
		 * @author Uwe Grueters, email: uwe.grueters@bot2.bio.uni-giessen.de
		 */
    public class MyProxy extends AbstractVegetativeOrganAgent.MySuperProxy implements IAtmosphericEnvironmentManipulator {

        private static final long serialVersionUID = -3497803478857798062L;

        public IAtmosphericEnvironment getAtmosphericEnvironment() {
            return atmosphericEnvironment;
        }

        public double getCo2Intercellular_microbar() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getGasExhangePattern().getCo2Intercellular_microbar();
        }

        public double getPhotosynthesis_micromol_per_m2_per_s() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getGasExhangePattern().getPhotosnynthesis_micromol_per_m2_per_s();
        }

        public double getPhotosynthesisRubisCoLimited_micromol_per_m2_per_s() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getGasExhangePattern().getPhotosynthesisRubisCoLimited_micromol_per_m2_per_s();
        }

        public double getPhotosynthesisRuBPRegenertionLimited_micromol_per_m2_per_s() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getGasExhangePattern().getPhotosynthesisRuBPRegenertionLimited_micromol_per_m2_per_s();
        }

        public double getPhotosynthesisTPULimited_micromol_per_m2_per_s() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getGasExhangePattern().getPhotosynthesisTPULimited_micromol_per_m2_per_s();
        }

        public double getDarkRespiration_micromol_per_m2_per_s() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getGasExhangePattern().getDarkRespiration_micromol_per_m2_per_s();
        }

        public double getTranspirationRate_mol_per_m2_per_s() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getGasExhangePattern().getTranspirationRate_mol_per_m2_per_s();
        }

        public double getAdaxialStomatalConductanceWaterVapour_mol_per_m2_per_s() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getGasExhangePattern().getAdaxialStomatalConductanceWaterVapour_mol_per_m2_per_s();
        }

        public double getAbaxialStomatalConductanceWaterVapour_mol_per_m2_per_s() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getGasExhangePattern().getAbaxialStomatalConductanceWaterVapour_mol_per_m2_per_s();
        }

        public double getBoundaryLayerConductanceWaterVapour_mol_per_m2_per_s() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getGasExhangePattern().getBoundaryLayerConductanceWaterVapour_mol_per_m2_per_s();
        }

        @Override
        public double getOrganTemperature_DegC() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getEnergyExhangePattern().getOrganTemperature_DegC();
        }

        public double getEnergyBalance_W_per_m2() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getEnergyExhangePattern().getEnergyBalance_W_per_m2();
        }

        public double getAbsorbedTotalRadiation_W_per_m2() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getEnergyExhangePattern().getAbsorbedTotalRadiationPerLeafArea_W_per_m2();
        }

        public double getEmittedThermalRadiation_W_per_m2() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getEnergyExhangePattern().getEmittedThermalRadiation_W_per_m2();
        }

        public double getSensibleHeatFlux_W_per_m2() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getEnergyExhangePattern().getSensibleHeatFlux_W_per_m2();
        }

        public double getLatentHeatFlux_W_per_m2() {
            return getLeafArea().getPhotosyntheticalAttributesService().getMassEnergyExchange().getEnergyExhangePattern().getLatentHeatFlux_W_per_m2();
        }
    }
}
