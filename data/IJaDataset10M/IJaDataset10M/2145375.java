package eu.cherrytree.paj.graphics;

import eu.cherrytree.paj.graphics.constantgeometry.ConstantMesh;
import eu.cherrytree.paj.graphics.constantgeometry.ConstantMeshManager;
import eu.cherrytree.paj.utilities.Bone;

public class MeshBone extends Bone {

    private ConstantMesh baseMesh;

    public MeshBone(ConstantMesh base, int bone_id, String bone_name) {
        super(bone_id, bone_name);
        baseMesh = base;
    }

    public void destroy() {
        ConstantMeshManager.freeMesh(baseMesh);
    }

    public ConstantMesh getMesh() {
        return baseMesh;
    }

    public void changeMesh(ConstantMesh mesh) {
        ConstantMeshManager.freeMesh(baseMesh);
        baseMesh = mesh;
    }
}
