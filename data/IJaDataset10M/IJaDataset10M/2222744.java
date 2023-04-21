package org.matsim.evacuation.otfvis.readerwriter;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.log4j.Logger;
import org.matsim.core.utils.misc.ByteBufferUtils;
import org.matsim.evacuation.otfvis.drawer.AgentDrawer;
import org.matsim.vis.otfvis.caching.SceneGraph;
import org.matsim.vis.otfvis.data.OTFDataReceiver;
import org.matsim.vis.otfvis.interfaces.OTFDataReader;

public class AgentReader extends OTFDataReader {

    Logger log = Logger.getLogger(AgentReader.class);

    boolean init = false;

    private float maxX = Float.NEGATIVE_INFINITY;

    private float minX = Float.POSITIVE_INFINITY;

    private float width;

    private int maxAgents = 0;

    private AgentDrawer drawer;

    public AgentReader() {
        super();
    }

    public void readAgent(ByteBuffer in) {
        String id = ByteBufferUtils.getString(in);
        float x = in.getFloat();
        float y = in.getFloat();
        int type = in.getInt();
        int user = in.getInt();
        float speed = in.getFloat();
        if (speed > 20) {
            return;
        }
        this.drawer.addAgent(x, y, type, user, speed, id);
        if (!this.init) {
            this.maxAgents++;
            if (x < this.minX) {
                this.minX = x;
            } else if (x > this.maxX) {
                this.maxX = x;
            }
        }
    }

    @Override
    public void readDynData(ByteBuffer in, SceneGraph graph) throws IOException {
        this.drawer = new AgentDrawer();
        int count = in.getInt();
        for (int i = 0; i < count; i++) readAgent(in);
        if (!this.init) {
            this.width = this.maxX - this.minX;
            this.init = true;
        }
        this.drawer.init(this.width, this.maxAgents);
    }

    @Override
    public void readConstData(ByteBuffer in) throws IOException {
    }

    @Override
    public void connect(OTFDataReceiver receiver) {
    }

    @Override
    public void invalidate(SceneGraph graph) {
        this.drawer.invalidate(graph);
    }
}
