package osmosis.test;

import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openstreetmap.osmosis.core.container.v0_6.BoundContainer;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.EntityProcessor;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.RelationMember;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import osmosis.FileFormat;
import osmosis.SimpleSinkRunner;

class StatsTask implements Sink, EntityProcessor {

    int nNodes = 0;

    int nWays = 0;

    int nWayNodeRefs = 0;

    int nRelations = 0;

    int nRelationNodeRefs = 0;

    int nRelationWayRefs = 0;

    int nRelationRelationRefs = 0;

    public static void main(String args[]) {
        Options options = new Options();
        Option optionInput = new Option("input", true, "an input osm file");
        optionInput.setRequired(true);
        options.addOption(optionInput);
        CommandLine line = null;
        try {
            line = new GnuParser().parse(options, args);
        } catch (ParseException e) {
            System.out.println("parsing command line failed: " + e.getMessage());
            System.exit(1);
        }
        if (line == null) return;
        String input = line.getOptionValue("input");
        new SimpleSinkRunner(new StatsTask()).execute(FileFormat.PBF, input);
    }

    @Override
    public void process(EntityContainer ec) {
        ec.process(this);
    }

    @Override
    public void complete() {
        System.out.println(String.format("nodes: %d", nNodes));
        System.out.println(String.format("ways: %d", nWays));
        System.out.println(String.format("waynodes: %d", nWayNodeRefs));
        System.out.println(String.format("relations: %d", nRelations));
        System.out.println(String.format("relations nodes: %d", nRelationNodeRefs));
        System.out.println(String.format("relations ways: %d", nRelationWayRefs));
        System.out.println(String.format("relations relations: %d", nRelationRelationRefs));
    }

    @Override
    public void release() {
    }

    @Override
    public void process(BoundContainer arg0) {
    }

    @Override
    public void process(NodeContainer nc) {
        nNodes++;
    }

    @Override
    public void process(WayContainer wc) {
        nWays++;
        Way way = wc.getEntity();
        List<WayNode> nodes = way.getWayNodes();
        nWayNodeRefs += nodes.size();
    }

    @Override
    public void process(RelationContainer rc) {
        nRelations++;
        Relation relation = rc.getEntity();
        for (RelationMember m : relation.getMembers()) {
            switch(m.getMemberType()) {
                case Node:
                    nRelationNodeRefs += 1;
                    break;
                case Way:
                    nRelationWayRefs += 1;
                    break;
                case Relation:
                    nRelationRelationRefs += 1;
                    break;
                default:
                    break;
            }
        }
    }
}
