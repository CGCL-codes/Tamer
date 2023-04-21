package de.grogra.ray2.radiosity;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.vecmath.Color3f;

/**
 * A PatchGroup is a triangle witch contains 4 sub triangles. 
 * @author Ralf Kopsch
 */
public class PatchGroup {

    private SubPatch fourPatches[] = new SubPatch[4];

    private Color3f reflectance;

    private Color3f[] fourEmittances = new Color3f[4];

    private int parentID = 0;

    private boolean visible = true;

    private static int mashid = 1;

    /**
	 * Creates a new patch group.
	 * @param vertices a list of 3 or 4 vertices.
	 * @param id the group id.
	 * @param emitted the emmited color.
	 * @param reflected the refelcted color.
	 * @param radios the radiosity color.
	 * @param visible false, if this group and all sub patches should be invisible. 
	 */
    public PatchGroup(Vector3d[] vertices, int id, Color3f emitted, Color3f reflected, Color3f radios, boolean visible) {
        this.visible = visible;
        init(vertices, id, reflected, emitted, radios);
    }

    /**
	 * Creates a new patch group by subdividing the given patch.
	 * @param p the patch to subdivide.
	 * @param id the group id.
	 * @param refl the refelcted color.
	 * @param emitted the emmited color.
	 * @param radios the radiosity color.
	 */
    public PatchGroup(SubPatch p, int id, Color3f refl, Color3f emitted, Color3f radios) {
        init(p.getVertices(), id, refl, emitted, radios);
    }

    private void init(Vector3d[] vertices, int id, Color3f refl, Color3f emitted, Color3f radios) {
        this.parentID = id;
        if (vertices.length == 3) {
            Vector3d middleSide0 = new Vector3d();
            middleSide0.add(vertices[0], vertices[1]);
            middleSide0.scale(0.5f);
            Vector3d middleSide1 = new Vector3d();
            middleSide1.add(vertices[1], vertices[2]);
            middleSide1.scale(0.5f);
            Vector3d middleSide2 = new Vector3d();
            middleSide2.add(vertices[2], vertices[0]);
            middleSide2.scale(0.5f);
            fourPatches[0] = new SubPatch((Vector3d) vertices[0].clone(), (Vector3d) middleSide0.clone(), (Vector3d) middleSide2.clone());
            fourPatches[1] = new SubPatch((Vector3d) vertices[1].clone(), (Vector3d) middleSide1.clone(), (Vector3d) middleSide0.clone());
            fourPatches[2] = new SubPatch((Vector3d) vertices[2].clone(), (Vector3d) middleSide2.clone(), (Vector3d) middleSide1.clone());
            fourPatches[3] = new SubPatch((Vector3d) middleSide0.clone(), (Vector3d) middleSide1.clone(), (Vector3d) middleSide2.clone());
        } else {
            Vector3d middle = new Vector3d();
            middle.add(vertices[0], vertices[2]);
            middle.scale(0.5f);
            fourPatches[0] = new SubPatch((Vector3d) vertices[0].clone(), (Vector3d) vertices[1].clone(), (Vector3d) middle.clone());
            fourPatches[1] = new SubPatch((Vector3d) vertices[1].clone(), (Vector3d) vertices[2].clone(), (Vector3d) middle.clone());
            fourPatches[2] = new SubPatch((Vector3d) vertices[2].clone(), (Vector3d) vertices[3].clone(), (Vector3d) middle.clone());
            fourPatches[3] = new SubPatch((Vector3d) vertices[3].clone(), (Vector3d) vertices[0].clone(), (Vector3d) middle.clone());
        }
        for (int i = 0; i < 4; i++) {
            this.fourEmittances[i] = emitted;
            this.fourPatches[i].setRadiosity(radios);
        }
        this.reflectance = refl;
    }

    /**
	 * Projects the given groups on all hemicubes of the current patch group.
	 * @param allGroups a list of patch groups.
	 */
    public void project(Vector<PatchGroup> allGroups) {
        int numGroups = allGroups.size();
        for (int p = 0; p < 4; p++) {
            ViewCube vCube = new ViewCube(fourPatches[p].getCenter(), fourPatches[p].getNormal());
            for (int g = 0; g < numGroups; g++) {
                PatchGroup pg = allGroups.get(g);
                if (pg.getParentPatchID() != this.parentID) {
                    for (int i = 0; i < 4; i++) {
                        SubPatch otherPatch = pg.getPatch(i);
                        vCube.projPatchOnCube(otherPatch);
                    }
                }
            }
            vCube.computeFormFactorMap(this.fourPatches[p].getFFMap());
        }
    }

    /**
	 * Checks if this patch group must be subdivided.
	 * @param threshold the subdivision threshold.
	 * @return true, if this group must be subdivided.
	 */
    public boolean needSubDevide(float threshold) {
        if (!this.visible) {
            return false;
        }
        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < m; n++) {
                float diff = this.fourPatches[m].getMaxRadDifference(this.fourPatches[n]);
                if (diff > threshold) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Subdivides this patch group.
	 * @param group a global list of all groups.
	 * @param deprecatedPatches a list of deprecated patche groups.
	 * @param subDivList a list of divided patch groups.
	 * @param threshold the subdivision threshold. 
	 * @return true, if this group was subdivided.
	 */
    public boolean subdivide(Vector<PatchGroup> group, List<SubPatch> deprecatedPatches, Vector<PatchGroup> subDivList, float threshold) {
        if (needSubDevide(threshold)) {
            for (int i = 0; i < 4; i++) {
                PatchGroup newGroup = new PatchGroup(this.fourPatches[i], this.parentID, this.reflectance, this.fourEmittances[i], this.fourPatches[i].getRadiosity());
                group.add(newGroup);
                deprecatedPatches.add(this.fourPatches[i]);
                subDivList.add(newGroup);
            }
            group.remove(this);
            return true;
        }
        return false;
    }

    /**
	 * Clears all calculated radiosity values.  
	 */
    public void clearRadiosity() {
        for (int i = 0; i < 4; i++) {
            this.fourPatches[i].setRadiosity(0.0f, 0.0f, 0.0f);
            this.fourPatches[i].clearFFMap();
        }
    }

    /**
	 * Returns the sub path with the given number. 
	 * @param i the number.
	 * @return Returns the sub path with the given number.
	 */
    public SubPatch getPatch(int i) {
        return this.fourPatches[i];
    }

    /**
	 * Returns the parent id.
	 * @return Returns the parent id.
	 */
    public int getParentPatchID() {
        return this.parentID;
    }

    /**
	 * Returns the radiosity color of the sub patch with the given number.
	 * @param patchNum the sub patch number.
	 * @return Returns the radiosity color of the sub patch with the given number.
	 */
    public Color3f getRadiosity(int patchNum) {
        return this.fourPatches[patchNum].getRadiosity();
    }

    /**
	 * Returns the form factors of the sub patch with the given number.
	 * @param patchNum the sub patch number.
	 * @return Returns the form factors of the sub patch with the given number.
	 */
    public Map<SubPatch, FormFactor> getFormFactors(int patchNum) {
        return this.fourPatches[patchNum].getFFMap();
    }

    /**
	 * Returns the emittance color of the sub patch with the given number.
	 * @param patchNum the sub patch number.
	 * @return Returns the emittance color of the sub patch with the given number.
	 */
    public Color3f getEmittance(int patchNum) {
        return fourEmittances[patchNum];
    }

    /**
	 * Returns the refectance color.
	 * @return Returns the refectance color.
	 */
    public Color3f getReflectance() {
        return reflectance;
    }

    /**
	 * Converts the patch group into a mesh. 
	 * @return Converts the patch group into a mesh.
	 */
    public MyMeshVolume[] createMesh() {
        for (int i = 0; i < 4; i++) {
            this.fourPatches[i].clearFFMap();
        }
        if (!this.visible) {
            return null;
        }
        MyMeshVolume[] mv = new MyMeshVolume[4];
        for (int i = 0; i < 4; i++) {
            mv[i] = this.fourPatches[i].createMesh();
            mv[i].setMeshColor(this.fourPatches[i].getRadiosity());
            mv[i].setId(PatchGroup.mashid++);
        }
        return mv;
    }

    /**
	 * Returns true, if this group contains a deprecated patch.
	 * @param deprecatedPatches a list of deprecated patches.
	 * @return Returns true, if this group contains a deprecated patch.
	 */
    public boolean ffMapContains(List<SubPatch> deprecatedPatches) {
        for (int i = 0; i < 4; i++) {
            if (this.fourPatches[i].ffMapContains(deprecatedPatches)) {
                return true;
            }
        }
        return false;
    }
}
