package ch.epfl.lbd.applications.trdw.datawarehouses;

import java.util.ArrayList;
import org.junit.Test;
import ch.epfl.lbd.applications.trdw.cubes.PresenceCube;
import ch.epfl.lbd.applications.trdw.dimensions.SpaceDimension;
import ch.epfl.lbd.database.connection.DatabaseConnection;
import ch.epfl.lbd.database.objects.RelationalTable;
import ch.epfl.lbd.database.olap.Cube;
import ch.epfl.lbd.database.olap.Measure;
import ch.epfl.lbd.database.providers.mondrian.olap.MondrianDataWarehouse;
import ch.epfl.lbd.database.providers.mondrian.olap.MondrianVirtualMeasure;
import ch.epfl.lbd.database.providers.postgresql.connection.PostgreSqlConnection;

public class TrajectoryDataWarehouse extends MondrianDataWarehouse {

    public static final long serialVersionUID = 0x432225;

    protected DatabaseConnection connection;

    protected ArrayList<RelationalTable> openTables;

    public TrajectoryDataWarehouse() throws Exception {
        super("unknown", "Trajectory Data Warehouse");
        openTables = new ArrayList<RelationalTable>();
        cubes = new ArrayList<Cube>();
        connection = new PostgreSqlConnection("WEB-INF/src/connections.properties", "connection4");
        connection.openConnection();
        RelationalTable facts = new RelationalTable("trdw_episode_facts");
        RelationalTable spaceDimension = new RelationalTable("trdw_space_dimension");
        facts.loadObject(connection);
        spaceDimension.loadObject(connection);
        openTables.add(spaceDimension);
        openTables.add(facts);
        PresenceCube presenceCube = new PresenceCube(facts);
        SpaceDimension spaceDim = new SpaceDimension(spaceDimension);
        this.addDimension(spaceDim);
        String sql = "case when get_trj_space_area_intersections(trdw_episode_facts.geom) > 0 then	ceil(1/get_trj_space_area_intersections(trdw_episode_facts.geom)) else 0 end";
        Measure episodePresence = new MondrianVirtualMeasure("Episode Presence", facts, Measure.DATATYPE_NUMERIC, Measure.AGGR_SUM, sql);
        presenceCube.addDimension(spaceDim);
        presenceCube.addMeasure(episodePresence);
        this.addCube(presenceCube);
    }

    public void closeTables() throws Exception {
        for (RelationalTable tbl : openTables) {
            tbl.releaseObject();
        }
        connection.closeConnection();
    }

    @Test
    public void run() throws Exception {
        logger.info(this.getSchema());
        this.closeTables();
    }
}
