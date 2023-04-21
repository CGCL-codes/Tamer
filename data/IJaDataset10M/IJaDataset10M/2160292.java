package ac.jp.u_tokyo.SyncLib.tests;

import ac.jp.u_tokyo.SyncLib.IDSync;
import ac.jp.u_tokyo.SyncLib.Sync;
import ac.jp.u_tokyo.SyncLib.SyncFactory;
import ac.jp.u_tokyo.SyncLib.graphCombinator.GraphCombinatorFactory;

public class GraphCombinatorByTripleIDSync extends IDSyncTest {

    @Override
    protected Sync createBackwardSync() {
        GraphCombinatorFactory f = new GraphCombinatorFactory();
        f.setParaVars("a", "b");
        f.setOtherVars("x", "y");
        f.setInitialValues(null, null);
        f.connectSync(new SyncFactory() {

            public Sync create() {
                return new IDSync(false);
            }
        }, "y", "b");
        f.connectSync(new SyncFactory() {

            public Sync create() {
                return new IDSync(false);
            }
        }, "x", "y");
        f.connectSync(new SyncFactory() {

            public Sync create() {
                return new IDSync(false);
            }
        }, "a", "x");
        return f.create();
    }

    @Override
    protected Sync createForwardSync() {
        GraphCombinatorFactory f = new GraphCombinatorFactory();
        f.setParaVars("a", "b");
        f.setOtherVars("x", "y");
        f.setInitialValues(null, null);
        f.connectSync(new SyncFactory() {

            public Sync create() {
                return new IDSync(true);
            }
        }, "a", "x");
        f.connectSync(new SyncFactory() {

            public Sync create() {
                return new IDSync(true);
            }
        }, "x", "y");
        f.connectSync(new SyncFactory() {

            public Sync create() {
                return new IDSync(true);
            }
        }, "y", "b");
        return f.create();
    }
}
