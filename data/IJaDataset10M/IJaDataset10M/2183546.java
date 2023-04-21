package net.sf.joafip.memoryleak;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import net.sf.joafip.logger.JoafipLogger;
import net.sf.joafip.meminspector.service.MemInspectorException;
import net.sf.joafip.meminspector.service.inspect.MemInspector;
import net.sf.joafip.meminspector.service.show.ShowObjectTree;

/**
 * to analyze memory leak in hash set
 * 
 * @author luc peuvrier
 * 
 */
public final class MainTestHashSet2 {

    private static final JoafipLogger LOGGER = JoafipLogger.getLogger(MainTestHashSet2.class);

    public static void main(final String[] args) {
        try {
            final MainTestHashSet2 mainHashSet = new MainTestHashSet2();
            mainHashSet.run();
        } catch (Exception exception) {
            LOGGER.error("error", exception);
        }
    }

    private MainTestHashSet2() {
        super();
    }

    private void run() throws MemInspectorException {
        final MemInspector memInspector = new MemInspector();
        final Set<Integer> set = new HashSet<Integer>();
        for (int count = 0; count < 10000; count++) {
            set.add(count);
        }
        set.clear();
        memInspector.inspect(set, false);
        for (int count = 0; count < 20000; count++) {
            set.add(count);
        }
        set.clear();
        memInspector.inspect(set, true);
        if (memInspector.added()) {
            final String fileName = "runtime/mem.bin";
            memInspector.serialize(new File(fileName));
            ShowObjectTree.show(fileName);
        }
    }
}
