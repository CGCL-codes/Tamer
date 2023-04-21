package com.volatileengine.platform;

import java.util.List;
import com.volatileengine.scene.SceneMesh;

/**
 * @author Administrator
 * 
 */
class SceneMeshDrawCommand extends GPUCommand {

    private List<SceneMesh> sceneMeshes;

    public SceneMeshDrawCommand(List<SceneMesh> meshes) {
        sceneMeshes = meshes;
    }

    @Override
    public void execute(GPU executioner) {
        executioner.draw(sceneMeshes);
    }
}
