package org.llama.jmex.terra.pass;

import org.llama.jmex.task.TaskRunner;
import org.llama.jmex.terra.HeightMapManager;
import org.llama.jmex.terra.TerraManager;
import org.llama.jmex.terra.TerraView;
import org.llama.jmex.terra.TerraViewTask;
import org.llama.jmex.terra.XYKey;

public class DistanceDecompressionPass extends DistancePass {

    public void remove(XYKey xy, TerraView tv) {
    }

    public void add(XYKey xy, final TerraView tv) {
        final TerraManager tm = tv.getTerraManager();
        if (tm.getMapAsByteBuffer(xy.x, xy.y) != null) return;
        if (tm.getCompressedMap(xy.x, xy.y) == null) return;
        xy.update(xy.x, xy.y, HeightMapManager.K_MAP);
        if (tv.getTaskManager().containsTask(xy)) return;
        xy = new XYKey(xy);
        TerraViewTask task = new TerraViewTask(tv, xy) {

            public boolean runTask() {
                try {
                    long count = System.currentTimeMillis();
                    tm.decompressMap(xy.x, xy.y, false, true);
                    tv.add(xy);
                    tv.updateNextFrame();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            public String toString() {
                return new String("Task: " + xy);
            }
        };
        tv.getTaskManager().addTask(task, runner);
    }

    private TaskRunner runner;

    public DistanceDecompressionPass(int distance, TaskRunner runner) {
        super(distance, HeightMapManager.K_MAP, false);
        this.runner = runner;
    }
}
