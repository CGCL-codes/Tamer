package org.xptools.xpairports.generator;

import java.util.ArrayList;
import org.xptools.xpairports.model.Airport;
import org.xptools.xpairports.model.Constants;
import org.xptools.xpairports.parser.LineCodes;

class AirportGenerator implements IPartGenerator {

    private Airport airport;

    private ArrayList<IPartGenerator> subGenerators;

    public AirportGenerator(Airport airport) {
        this.airport = airport;
        subGenerators = new ArrayList<IPartGenerator>();
        subGenerators.addAll(PartGeneratorFactory.CreateGenerators(airport.getRunways()));
        subGenerators.addAll(PartGeneratorFactory.CreateGenerators(airport.getSeaRunways()));
        subGenerators.addAll(PartGeneratorFactory.CreateGenerators(airport.getHelipads()));
        subGenerators.add(PartGeneratorFactory.CreateSeperator());
        subGenerators.addAll(PartGeneratorFactory.CreateGenerators(airport.getLightObjects()));
        subGenerators.add(PartGeneratorFactory.CreateSeperator());
        subGenerators.addAll(PartGeneratorFactory.CreateGenerators(airport.getPavements()));
        if (airport.getBoundary() != null) subGenerators.add(PartGeneratorFactory.CreateGenerator(airport.getBoundary()));
        subGenerators.add(PartGeneratorFactory.CreateSeperator());
        subGenerators.addAll(PartGeneratorFactory.CreateGenerators(airport.getLines()));
        subGenerators.addAll(PartGeneratorFactory.CreateGenerators(airport.getTaxiSigns()));
        subGenerators.add(PartGeneratorFactory.CreateSeperator());
        subGenerators.addAll(PartGeneratorFactory.CreateGenerators(airport.getRamps()));
        if (airport.getTower() != null) subGenerators.add(PartGeneratorFactory.CreateGenerator(airport.getTower()));
        subGenerators.add(PartGeneratorFactory.CreateSeperator());
        subGenerators.addAll(PartGeneratorFactory.CreateGenerators(airport.getLightBeacons()));
        subGenerators.addAll(PartGeneratorFactory.CreateGenerators(airport.getWindSocks()));
        subGenerators.add(PartGeneratorFactory.CreateSeperator());
        subGenerators.addAll(PartGeneratorFactory.CreateGenerators(airport.getATCs()));
    }

    @Override
    public String generate() {
        StringBuilder builder = new StringBuilder();
        builder.append(LineCodes.AIRPORT_LINE_CODE);
        builder.append(" ");
        builder.append(airport.getElevation());
        builder.append(" ");
        builder.append(airport.hasControlTower() ? 1 : 0);
        builder.append(" ");
        builder.append(airport.displayDefaultBuildings() ? 1 : 0);
        builder.append(" ");
        builder.append(airport.getIDCode());
        builder.append(" ");
        builder.append(airport.getName());
        builder.append(Constants.NL);
        for (IPartGenerator generator : subGenerators) builder.append(generator.generate());
        return builder.toString();
    }
}
