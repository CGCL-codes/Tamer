package objects.production;

import objects.GalaxyException;
import objects.Race;
import objects.ShipType;
import objects.TechBlock;
import server.order.AbstractOrderCommand;
import util.Utils;
import java.util.ListIterator;
import java.util.Map;

public class PartialTechShipProduction extends ShipProduction {

    @Override
    protected IProducing createProduction(Race owner, ShipType shipType, ListIterator<String> cmd) throws GalaxyException {
        if (!cmd.hasNext()) return new ShipProduction.Producing(shipType);
        TechBlock techs = new TechBlock();
        for (TechBlock.Tech tech : TechBlock.Tech.values()) {
            if (!cmd.hasNext()) throw new GalaxyException("Required {0} level", tech);
            String levelStr = cmd.next();
            double max = owner.getTechBlock().getTechLevel(tech);
            double level;
            try {
                level = Utils.round00(AbstractOrderCommand.parseDouble(levelStr, max));
            } catch (NumberFormatException err) {
                throw new GalaxyException("Illegal technology level: {0}", levelStr);
            }
            if (level < 1.0 || level > max) throw new GalaxyException("{0} tech out of range", tech);
            techs.setTechLevel(tech, level);
        }
        return new Producing(shipType, shipType.adjustTech(techs));
    }

    @Override
    public IProducing loadProduction(Race race, org.w3c.dom.Element elem, boolean past) throws GalaxyException {
        String name = elem.getAttribute("name");
        ShipType shipType = race.findShipType(name + '_');
        if (shipType == null) shipType = race.findShipType(name);
        if (shipType == null) throw new GalaxyException("Unknown ship type {0}", name);
        TechBlock techs = new TechBlock();
        for (TechBlock.Tech tech : TechBlock.Tech.values()) {
            String s = elem.getAttribute(tech.lowerName());
            if (s != null) techs.setTechLevel(tech, Double.parseDouble(s));
        }
        ShipProduction.Producing prod;
        if (techs.summ() == 0) prod = new ShipProduction.Producing(shipType); else prod = new Producing(shipType, techs);
        if (elem.hasAttribute("progress")) prod.setProgress(Double.valueOf(elem.getAttribute("progress")));
        return prod;
    }

    public class Producing extends ShipProduction.Producing {

        public TechBlock tech;

        public Producing(ShipType type, TechBlock tech) {
            super(type);
            this.tech = tech;
        }

        @Override
        public void outputProd(Map<String, Object> object) {
            super.outputProd(object);
            for (TechBlock.Tech t : TechBlock.Tech.values()) if (tech.getTechLevel(t) > 0) object.put(t.lowerName(), tech.getTechLevel(t));
        }

        @Override
        public boolean equals(Object object) {
            return object != null && object instanceof Producing && equals((Producing) object);
        }

        public final boolean equals(Producing prod) {
            return super.equals(prod) && tech.equals(prod.tech);
        }

        @Override
        public double shipCost(Race owner) {
            double cost = 0;
            ShipType shipType = getShipType();
            TechBlock currtech = owner.getTechBlock();
            for (TechBlock.Tech t : TechBlock.Tech.values()) cost += shipType.moduleMass(t) * (tech.getTechLevel(t) / currtech.getTechLevel(t) + 1.0) / 2.0;
            return super.shipCost(owner) * cost / shipType.mass();
        }
    }
}
