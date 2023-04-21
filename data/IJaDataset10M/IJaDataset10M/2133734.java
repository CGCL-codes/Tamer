package domain.functions.attributes;

import domain.environment.IAtmosphericEnvironment;
import domain.functions.mass_energy_exchange.MassEnergyExchangePattern;
import domain.structure_new.components.organs.IVegetativePhotoOrgan;
import domain.structure_new.components.organs.IVisitor;

/**
 *
 * @author Uwe Grueters,	email: uwe.grueters@bot2.bio.uni-giessen.de
 */
public interface IPhotosyntheticalAttributesService extends IVegetativePhotoOrgan {

    void accept(IVisitor visitor, IAtmosphericEnvironment environment, boolean illuminatedSurface, double hours);

    IVegetativePhotoOrgan getPhotoOrgan();

    MassEnergyExchangePattern getMassEnergyExchange();

    void setMassEnergyExchange(MassEnergyExchangePattern massEnergyExchange);

    double getOrganNitrogenConcentration_g_per_m2();
}
