package cat.quadriga.parsers.code.types.qdg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import cat.quadriga.parsers.code.BreakOrContinueException;
import cat.quadriga.parsers.code.CodeZoneClass;
import cat.quadriga.parsers.code.ErrorLog;
import cat.quadriga.parsers.code.ParameterClass;
import cat.quadriga.parsers.code.QuadrigaFunction;
import cat.quadriga.parsers.code.SymbolTable;
import cat.quadriga.parsers.code.Utils;
import cat.quadriga.parsers.code.statements.BlockCode;
import cat.quadriga.parsers.code.symbols.LocalVariableSymbol;
import cat.quadriga.parsers.code.types.BaseType;
import cat.quadriga.parsers.code.types.BaseTypeClass;
import cat.quadriga.parsers.code.types.UnknownType;
import cat.quadriga.render.simple.RenderManager;
import cat.quadriga.runtime.Clock;
import cat.quadriga.runtime.Entity;
import cat.quadriga.runtime.RuntimeEnvironment;
import cat.quadriga.runtime.RuntimeSystem;
import cat.quadriga.runtime.RuntimeThread;

public class CompleteThread extends BaseTypeClass implements RuntimeThread, Runnable {

    public final List<QuadrigaSystem> systems;

    public final QuadrigaFunction init, cleanUp;

    public CompleteThread(String binaryName, List<QuadrigaSystem> systems, QuadrigaFunction init, QuadrigaFunction cleanUp, String file) {
        super(binaryName);
        this.systems = Collections.unmodifiableList(new ArrayList<QuadrigaSystem>(systems));
        if (init == null) {
            this.init = new QuadrigaFunction(new LinkedList<ParameterClass>(), new BlockCode.TmpBlockCode(file).transformToBlockCode(), 0, Collections.<LocalVariableSymbol>emptyList(), new CodeZoneClass(0, 0, 0, 0, file));
        } else {
            this.init = init;
        }
        if (cleanUp == null) {
            this.cleanUp = new QuadrigaFunction(new LinkedList<ParameterClass>(), new BlockCode.TmpBlockCode(file).transformToBlockCode(), 0, Collections.<LocalVariableSymbol>emptyList(), new CodeZoneClass(0, 0, 0, 0, file));
        } else {
            this.cleanUp = cleanUp;
        }
    }

    public CompleteThread(String pack, String name, List<QuadrigaSystem> systems, QuadrigaFunction init, QuadrigaFunction cleanUp, String file) {
        this((pack.length() > 0) ? (pack + "." + name) : name, systems, init, cleanUp, file);
    }

    @Override
    public BaseType getMathematicResultType(BaseType other) {
        return UnknownType.empty;
    }

    @Override
    public boolean isMathematicallyOperable() {
        return false;
    }

    @Override
    public String treeStringRepresentation() {
        List<String> aux = new ArrayList<String>(systems.size());
        for (QuadrigaSystem s : systems) {
            aux.add(s.getBinaryName());
        }
        String linkedStatus;
        if (!isValid()) {
            linkedStatus = " <->";
        } else {
            linkedStatus = " <+>";
        }
        return Utils.treeStringRepresentation("thread" + linkedStatus, aux);
    }

    private boolean valid = false;

    private CompleteThread validVersion = null;

    @Override
    public CompleteThread getValid(SymbolTable symbolTable, ErrorLog errorLog) {
        if (valid) {
            return this;
        } else if (validVersion == null) {
            List<QuadrigaSystem> qs = new ArrayList<QuadrigaSystem>();
            for (QuadrigaSystem s : systems) {
                if (s.isValid()) {
                    qs.add(s);
                } else {
                    QuadrigaSystem aux = s.getValid(symbolTable, errorLog);
                    if (aux == null) {
                        break;
                    } else {
                        qs.add(aux);
                    }
                }
            }
            if (qs.size() != systems.size()) {
                return null;
            }
            QuadrigaFunction bc, bc2;
            if (init.isCorrectlyLinked()) {
                bc = init;
            } else {
                symbolTable.resetLocalVariables();
                bc = init.getLinkedVersion(symbolTable, errorLog);
                symbolTable.closeLocalVariables();
                if (bc == null) {
                    return null;
                }
            }
            if (cleanUp.isCorrectlyLinked()) {
                bc2 = cleanUp;
            } else {
                symbolTable.resetLocalVariables();
                bc2 = cleanUp.getLinkedVersion(symbolTable, errorLog);
                symbolTable.closeLocalVariables();
                if (bc2 == null) {
                    return null;
                }
            }
            validVersion = new CompleteThread(getBinaryName(), qs, bc, bc2, bc.file);
            validVersion.validVersion = validVersion;
            validVersion.valid = true;
        }
        return validVersion;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean isAssignableFrom(BaseType rightOperand) {
        return getBinaryName().compareTo(rightOperand.getBinaryName()) == 0;
    }

    @Override
    public boolean isSerializable() {
        return true;
    }

    private Clock clock;

    @Override
    public void init(RuntimeEnvironment runtime) {
        assert isValid();
        for (QuadrigaSystem qs : systems) {
            runtime.entitySystem.registerSystem((RuntimeSystem) qs, runtime);
            ((RuntimeSystem) qs).executeInit(runtime);
        }
        try {
            runtime.enterFunction(init.numLocalVariables);
            init.code.execute(runtime);
            runtime.exitFunction();
            runtime.commitEntities();
        } catch (BreakOrContinueException e) {
            throw new IllegalStateException(e);
        }
        for (QuadrigaSystem qs : systems) {
            runtime.entitySystem.registerSystem((RuntimeSystem) qs, runtime);
        }
        clock = new Clock();
        clock.update();
    }

    @Override
    public void cleanUp(RuntimeEnvironment runtime) {
        assert isValid();
        for (QuadrigaSystem qs : systems) {
            ((RuntimeSystem) qs).executeCleanUp(runtime);
        }
        try {
            runtime.enterFunction(cleanUp.numLocalVariables);
            cleanUp.code.execute(runtime);
            runtime.exitFunction();
            runtime.commitEntities();
        } catch (BreakOrContinueException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void execute(RuntimeEnvironment runtime) {
        assert isValid();
        runtime.dt = clock.update();
        runtime.timeMilis = clock.getTimeMiliSeconds();
        for (QuadrigaSystem qs : systems) {
            RuntimeSystem system = (RuntimeSystem) qs;
            List<Entity> updates = new LinkedList<Entity>();
            List<Entity> newEntities = new LinkedList<Entity>();
            List<Entity> deletedEntities = new LinkedList<Entity>();
            runtime.entitySystem.getSystemUpdateInformation(system, updates, newEntities, deletedEntities, runtime);
            if (system.hasNewOrDelete()) {
                if (system.hasDelete()) {
                    for (Entity entity : deletedEntities) {
                        system.deleteEntity(entity, runtime);
                    }
                    for (Entity entity : deletedEntities) {
                        entity.commitChanges();
                    }
                    runtime.commitEntities();
                }
                if (system.hasNew()) {
                    for (Entity entity : newEntities) {
                        system.newEntity(entity, runtime);
                    }
                    for (Entity entity : newEntities) {
                        entity.commitChanges();
                    }
                    runtime.commitEntities();
                }
            }
            if (system.hasUpdate()) {
                for (Entity entity : updates) {
                    system.update(entity, runtime);
                }
                for (Entity entity : updates) {
                    entity.commitChanges();
                }
                runtime.commitEntities();
            }
        }
    }

    public RuntimeEnvironment runtime;

    @Override
    public void run() {
        RuntimeEnvironment myRuntime = new RuntimeEnvironment();
        myRuntime.entitySystem = runtime.entitySystem;
        Clock fpsClock = new Clock();
        fpsClock.update();
        try {
            init(myRuntime);
            while (runtime.keepRunning && myRuntime.keepRunning) {
                float fps = 1.0f / fpsClock.update();
                RenderManager.instance.setFPS(getBinaryName(), fps);
                execute(myRuntime);
                synchronized (this) {
                    try {
                        wait(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        } finally {
            runtime.keepRunning = false;
        }
        cleanUp(myRuntime);
    }
}
