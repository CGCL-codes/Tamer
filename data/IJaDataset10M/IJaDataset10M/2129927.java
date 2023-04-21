package net.sourceforge.jfunctions.structures.graph;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import net.sourceforge.jfunctions.functions.Transformation;
import net.sourceforge.jfunctions.math.numbers.NumericGroup;
import net.sourceforge.jfunctions.structures.graph.path.PathElement;
import example.structures.graph.Relationship;
import example.structures.graph.Resource;
import example.structures.graph.ResourceGraph;
import example.structures.graph.ResourceGraphFactory;

public class GraphToolkitTest {

    @Test
    public void testPathWithoutWeight() {
        ResourceGraph example = new ResourceGraphFactory().createExample();
        List<PathElement<Resource, Relationship, Integer>> path = GraphToolkit.path(example, example.node(0), example.node(4));
        assertEquals(2, path.size());
        assertEquals(2, path.get(path.size() - 1).distance().intValue());
    }

    @Test
    public void testPathWithWeight() {
        ResourceGraph example = new ResourceGraphFactory().createExample();
        List<PathElement<Resource, Relationship, Integer>> path = GraphToolkit.path(example, example.node(0), example.node(4), new Transformation<Relationship, Integer>() {

            @Override
            public Integer apply(Relationship argument) {
                return argument.getDistance();
            }
        }, NumericGroup.INTEGER);
        assertEquals(3, path.size());
        assertEquals(5, path.get(path.size() - 1).distance().intValue());
    }
}
