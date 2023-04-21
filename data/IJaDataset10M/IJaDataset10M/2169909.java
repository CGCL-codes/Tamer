package pipes.gui;

import java.util.List;
import java.util.Random;
import pipes.evolve.GameRegistrar;
import pipes.evolve.GameTypeBuilder;
import pipes.functional.Game;
import pipes.functional.GameFunctions;
import pipes.functional.GameType;
import pipes.root.GamePlay;
import evolve.BuiltinRegistrar;
import evolve.LibraryRegistrar;
import evolve.ObjectRegistry;
import evolve.Runner;
import evolve.TypeBuilder;
import functional.Cons;
import functional.Environment;
import functional.Obj;
import functional.Symbol;
import functional.repl.Initialize;
import functional.type.FunctionType;
import functional.type.Type;

public class PipeGameRunner implements Runner {

    FunctionType mTarget = new FunctionType(GameType.POSITION, new Type[] { GameType.GAME });

    ObjectRegistry mObjectReg;

    Environment mEnv;

    public PipeGameRunner(boolean allowFollow) {
        mObjectReg = new ObjectRegistry();
        BuiltinRegistrar.registerBuiltins(mObjectReg);
        LibraryRegistrar.registerLibrary(mObjectReg);
        GameRegistrar.registerGame(mObjectReg, allowFollow);
        mEnv = Initialize.initWithLibraries();
        GameFunctions.install(mEnv);
    }

    public Environment environment() {
        return mEnv;
    }

    public ObjectRegistry registry() {
        return mObjectReg;
    }

    public double run(Environment env, Symbol target, Random random) {
        GamePlay game = GamePlay.create(random.nextLong());
        Cons runOnce = Cons.list(target, new Game(game));
        Cons application = Cons.list(new Symbol("gamePlace"), new Game(game), runOnce);
        int plays = 0;
        try {
            Obj result;
            do {
                result = application.eval(env);
                ++plays;
            } while (!(result.isNull() || game.isGameOver()));
            while (!game.isGameOver()) {
                game.updateFlow();
            }
        } catch (GameFunctions.GameEvalException ex) {
        }
        return game.score() + (plays / 100.0);
    }

    public FunctionType targetType() {
        return mTarget;
    }

    public double maxScore() {
        return 250;
    }

    public int iterations() {
        return 2;
    }

    public long timeoutInterval() {
        return 4000;
    }

    public List<TypeBuilder.Constraint> typeConstraints() {
        return GameTypeBuilder.typeConstraints();
    }
}
