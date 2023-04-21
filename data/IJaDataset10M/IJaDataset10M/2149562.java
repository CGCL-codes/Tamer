package ru.susu.algebra.partition.polyhedron;

import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import ru.susu.algebra.partition.Partition;
import ru.susu.algebra.partition.polyhedron.checkers.IVertexChecker;
import ru.susu.algebra.partition.polyhedron.checkers.SimplexMethodVertexChecker;
import ru.susu.algebra.partition.polyhedron.providers.AllVertexesProvider;
import ru.susu.algebra.partition.polyhedron.providers.IVertexesProvider;
import ru.susu.algebra.properties.MapPropertySource;

/**
 * @author akargapolov
 * @since: 11.03.2010
 */
public class VertexesFinder implements Runnable {

    public static final String CHECKERS_ARRAY = "CheckersArray";

    public static final String START_NUMBER = "StartNumber";

    public static final String END_NUMBER = "EndNumber";

    public static final String VERTEXES_PROVIDER = "VertexesProvider";

    public static final IVertexChecker[] DEFAULT_CHECKERS = new IVertexChecker[] { new SimplexMethodVertexChecker() };

    public static final Integer DEFAULT_START_NUMBER = 1;

    public static final Integer DEFAULT_END_NUMBER = 15;

    public static final IVertexesProvider DEFAULT_VERTEXES_PROVIDER = new AllVertexesProvider();

    private IVertexChecker[] _checkers;

    private Integer _startNumber;

    private Integer _endNumber;

    private IVertexesProvider _vertexesProvider;

    public VertexesFinder(MapPropertySource properties) {
        _checkers = (IVertexChecker[]) properties.getValue(CHECKERS_ARRAY, DEFAULT_CHECKERS);
        _startNumber = (Integer) properties.getValue(START_NUMBER, DEFAULT_START_NUMBER);
        _endNumber = (Integer) properties.getValue(END_NUMBER, DEFAULT_END_NUMBER);
        _vertexesProvider = (IVertexesProvider) properties.getValue(VERTEXES_PROVIDER, DEFAULT_VERTEXES_PROVIDER);
    }

    private Map<Integer, Set<Partition>> _vertexes;

    public Map<Integer, Set<Partition>> getVertexes() {
        return _vertexes;
    }

    public Set<Partition> getVertexes(int number) {
        return _vertexes.get(number);
    }

    public void run() {
        _vertexes = new HashMap<Integer, Set<Partition>>();
        try {
            PrintStream sysout = System.out;
            for (int number = _startNumber; number <= _endNumber; number++) {
                sysout.println("Start - " + number);
                TreeSet<Partition> p4Number = new TreeSet<Partition>();
                Date start = new Date();
                List<Partition> current = _vertexesProvider.getVertexes(number, _vertexes);
                for (Partition p : current) if (check(p, current)) p4Number.add(p);
                _vertexes.put(number, p4Number);
                Date end = new Date();
                String str = number + " - " + p4Number.size() + " vertexes; time " + (end.getTime() - start.getTime()) + "ms" + "\nPartitions checked: " + current.size() + "\n";
                sysout.print(str);
                sysout.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean check(Partition partition, List<Partition> allPartitions) {
        for (IVertexChecker checker : _checkers) if (!IVertexChecker.CHECKED_RESULT.equals(checker.check(partition, allPartitions))) {
            return false;
        }
        return true;
    }
}
