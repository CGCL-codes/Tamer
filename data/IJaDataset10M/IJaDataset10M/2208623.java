package fr.smasim.systems;

import java.awt.Color;
import java.util.Vector;
import fr.smasim.model.Agent;
import fr.smasim.model.Cell;
import fr.smasim.model.Factory;
import fr.smasim.model.MultiAgentSystem;
import fr.smasim.model.exceptions.InvalidCoordinatesException;
import fr.smasim.plugins.annotations.Plugin;
import fr.smasim.plugins.annotations.PluginOption;

@Plugin(author = "R. Muller", name = "Amibes", comment = "Amibes tendant � se regroupper.", year = 2007)
public class FactoryBactery implements Factory {

    private class BacteryCell extends Cell {

        private static final int EXCITEMENT_MAX = 3;

        private static final int EXCITEMENT_MED = 2;

        private static final int EXCITEMENT_NON = 0;

        private int excitement;

        private int nextexcitement;

        public BacteryCell(MultiAgentSystem context, int x, int y) {
            this(context, x, y, true);
        }

        public BacteryCell(MultiAgentSystem context, int x, int y, boolean set) {
            super(context, x, y, set);
            excitement = BacteryCell.EXCITEMENT_NON;
            nextexcitement = excitement;
        }

        @Override
        public Color getColor() {
            switch(getExcitement()) {
                case EXCITEMENT_NON:
                    return Color.white;
                case EXCITEMENT_MED:
                    return Color.yellow;
                case EXCITEMENT_MAX:
                    return Color.orange;
                default:
                    return Color.black;
            }
        }

        public int getExcitement() {
            return excitement;
        }

        public void startWave() {
            if (getExcitement() == BacteryCell.EXCITEMENT_NON) nextexcitement = BacteryCell.EXCITEMENT_MAX;
        }

        public void update(MultiAgentSystem origin) {
            int exc = BacteryCell.EXCITEMENT_NON;
            if (getExcitement() == BacteryCell.EXCITEMENT_MAX) exc = BacteryCell.EXCITEMENT_MED; else if (getExcitement() != BacteryCell.EXCITEMENT_MED) {
                boolean done = false;
                for (int i = -1; i <= 1 && !done; i++) for (int j = -1; j <= 1 && !done; j++) if (i != 0 || j != 0) try {
                    if (((BacteryCell) mySystem().getCellAt(i + x, j + y)).getExcitement() == BacteryCell.EXCITEMENT_MAX) {
                        done = true;
                        exc = BacteryCell.EXCITEMENT_MAX;
                    }
                } catch (InvalidCoordinatesException e) {
                }
            }
            nextexcitement = exc;
        }

        public void finalizeStep() {
            excitement = nextexcitement;
        }
    }

    public class BacteryAgent extends Agent {

        private static final int RESILIENCE_TIME = 3;

        private double alpha;

        private int nextx, nexty;

        public int resilience;

        private java.util.Random rnd = new java.util.Random();

        public BacteryAgent(MultiAgentSystem context, int x, int y, double alpha) {
            this(context, x, y, true, alpha);
        }

        public BacteryAgent(MultiAgentSystem context, int x, int y, boolean set, double alpha) {
            super(context, x, y, set);
            this.alpha = alpha;
            nextx = x;
            nexty = y;
            resilience = 0;
        }

        public void update(MultiAgentSystem destination) {
            Vector<Integer> xi = new Vector<Integer>();
            Vector<Integer> yi = new Vector<Integer>();
            if (resilience > 0) resilience--;
            try {
                if (((BacteryCell) destination.getCellAt(x, y)).getExcitement() == BacteryCell.EXCITEMENT_NON) {
                    for (int i = -1; i <= 1; i++) for (int j = -1; j <= 1; j++) if (i != 0 || j != 0) try {
                        if (((BacteryCell) destination.getCellAt(x + i, y + j)).getExcitement() == BacteryCell.EXCITEMENT_MAX) {
                            xi.add(x + i);
                            yi.add(y + j);
                        }
                    } catch (InvalidCoordinatesException e2) {
                    }
                } else if (((BacteryCell) destination.getCellAt(x, y)).getExcitement() == BacteryCell.EXCITEMENT_MAX) resilience = BacteryAgent.RESILIENCE_TIME;
            } catch (InvalidCoordinatesException e1) {
                e1.printStackTrace();
            }
            try {
                if (xi.size() > 0) {
                    int i = rnd.nextInt(xi.size());
                    int nx = mySystem().adaptX(xi.get(i));
                    int ny = mySystem().adaptY(yi.get(i));
                    if (mySystem().getAgentAt(nx, ny) == null) {
                        nextx = nx;
                        nexty = ny;
                        resilience = BacteryAgent.RESILIENCE_TIME;
                    }
                } else if (resilience <= 0 && rnd.nextDouble() <= this.alpha) {
                    ((BacteryCell) mySystem().getCellAt(x, y)).startWave();
                    resilience = BacteryAgent.RESILIENCE_TIME;
                }
            } catch (InvalidCoordinatesException e2) {
                e2.printStackTrace();
            }
        }

        public void finalizeStep() {
            try {
                mySystem().getCellAt(x, y).finalizeStep();
                if (mySystem().getAgentAt(nextx, nexty) == null) {
                    mySystem().setAgentAt(x, y, null);
                    x = nextx;
                    y = nexty;
                    mySystem().setAgentAt(x, y, this);
                } else {
                    nextx = x;
                    nexty = y;
                }
            } catch (InvalidCoordinatesException e) {
                e.printStackTrace();
            }
        }
    }

    private int quantity;

    private double alpha;

    private static final java.util.Random rnd = new java.util.Random();

    private long seed;

    public FactoryBactery(@PluginOption(name = "Quantit�", hint = "Nombre d'amibes.") int nbbact, @PluginOption(name = "Probabilit�", hint = "Probabilit� qu'une amibe d�marre une vague.") double proba) {
        quantity = nbbact;
        alpha = proba;
        seed = FactoryBactery.rnd.nextLong();
    }

    public Cell createCell(MultiAgentSystem context, int x, int y) {
        return new BacteryCell(context, x, y);
    }

    public void initializeCells(MultiAgentSystem system) {
        for (int i = 0; i < system.getWidth(); i++) for (int j = 0; j < system.getHeight(); j++) try {
            system.setCellAt(i, j, createCell(system, i, j));
        } catch (InvalidCoordinatesException e) {
            e.printStackTrace();
        }
    }

    public Agent createAgent(MultiAgentSystem context, int x, int y) {
        return new BacteryAgent(context, x, y, alpha);
    }

    public void initializeAgents(MultiAgentSystem system) {
        FactoryBactery.rnd.setSeed(seed);
        double probabi = (double) quantity / (double) (system.getWidth() * system.getHeight());
        int nbp = 0;
        for (int x = 0; x < system.getWidth() && nbp < quantity; x++) for (int y = 0; y < system.getHeight() && nbp < quantity; y++) {
            try {
                system.setAgentAt(x, y, null);
            } catch (Exception e) {
            }
            if (FactoryBactery.rnd.nextDouble() <= probabi) {
                createAgent(system, x, y);
                nbp++;
            }
        }
    }
}
