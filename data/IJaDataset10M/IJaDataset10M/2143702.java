package sim.app.ubik.workerStates.auxiliaryStates;

import sim.app.ubik.BasicWorker;
import sim.app.ubik.Ubik;
import sim.app.ubik.building.OfficeFloor;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

/**
 * Clase para implementar el patrón estado por los estados de los agentes.
 * Se debe tener en cuenta propiedades del trabajador como que el fitness permita que se mueva o actualizar la dirección actual que sigue.
 * @author Emilio Serrano, Juan Botia and Jose Manuel Cadenas
 */
public abstract class AuxiliaryState implements Steppable {

    protected boolean finished = false;

    public BasicWorker worker;

    protected SparseGrid2D wokersGrid;

    protected OfficeFloor floor;

    protected Ubik ubik;

    protected SimState state;

    protected Int2D workerLocation;

    public AuxiliaryState(BasicWorker w, SimState state) {
        worker = w;
        this.state = state;
        ubik = (Ubik) state;
        floor = ubik.building.getFloor(worker.getFloor());
        wokersGrid = ubik.building.getWorkersGrid(worker.getFloor());
        workerLocation = wokersGrid.getObjectLocation(worker);
    }

    /**
     * Si el grid no es el de la planta del trabajador se llama a este constructor. Por ejemplo en las escaleras o si se programan
     * estados que impiquen moverse por otra planta.
     * La planta no se inicializa
     * @param w
     * @param state
     * @param workersGrid
     */
    public AuxiliaryState(BasicWorker w, SimState state, SparseGrid2D workersGrid) {
        worker = w;
        this.state = state;
        ubik = (Ubik) state;
        this.wokersGrid = workersGrid;
        workerLocation = wokersGrid.getObjectLocation(worker);
    }

    public abstract boolean isFinished();
}
