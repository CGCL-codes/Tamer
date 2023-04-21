package org.jal3d.loaders;

import java.io.InputStream;
import java.util.Vector;
import org.jal3d.CoreBone;
import org.jal3d.CoreSkeleton;
import org.jal3d.loaders.Loader.Mask;
import cat.arestorm.math.MutableQuaternion;
import cat.arestorm.math.MutableVector3D;
import cat.arestorm.math.Quaternion;
import cat.arestorm.math.Vector3D;

/**
 * This class represents an object witch is capable of loading a CoreSkeleton object from
 * the old CAL3D format.
 * 
 * @author Isaac "Atridas" Serrano Guasch
 * @since 1.0
 *
 */
public class SkeletonCAL implements DataLoader<CoreSkeleton> {

    private int loadingMode;

    public SkeletonCAL(final int lm) {
        loadingMode = lm;
    }

    /**
	 * Loads a Bone.
	 * 
	 * Loads a bone.
	 * 
	 * @param dataSrc the Source to read from.
	 * @return the CoreBone loaded.
	 * @throws IllegalArgumentException if the source does not provide the information
	 * desired.
	 */
    private CoreBone loadCoreBones(final Source dataSrc) {
        try {
            String name;
            name = dataSrc.readIntCountedString();
            float tx, ty, tz;
            tx = dataSrc.readFloat();
            ty = dataSrc.readFloat();
            tz = dataSrc.readFloat();
            float rx, ry, rz, rw;
            rx = dataSrc.readFloat();
            ry = dataSrc.readFloat();
            rz = dataSrc.readFloat();
            rw = dataSrc.readFloat();
            float txBoneSpace, tyBoneSpace, tzBoneSpace;
            txBoneSpace = dataSrc.readFloat();
            tyBoneSpace = dataSrc.readFloat();
            tzBoneSpace = dataSrc.readFloat();
            float rxBoneSpace, ryBoneSpace, rzBoneSpace, rwBoneSpace;
            rxBoneSpace = dataSrc.readFloat();
            ryBoneSpace = dataSrc.readFloat();
            rzBoneSpace = dataSrc.readFloat();
            rwBoneSpace = dataSrc.readFloat();
            int parentId;
            parentId = dataSrc.readInteger();
            MutableQuaternion rot = new MutableQuaternion(rx, ry, rz, rw);
            MutableQuaternion rotbs = new MutableQuaternion(rxBoneSpace, ryBoneSpace, rzBoneSpace, rwBoneSpace);
            MutableVector3D trans = new MutableVector3D(tx, ty, tz);
            if ((loadingMode & Mask.LOADER_ROTATE_X_AXIS) != 0) {
                if (parentId == -1) {
                    Quaternion xAxis90 = new Quaternion(0.7071067811f, 0.0f, 0.0f, 0.7071067811f);
                    rot.multSelf(xAxis90);
                    trans.transformSelf(xAxis90);
                }
            }
            Vector3D transbs = new Vector3D(txBoneSpace, tyBoneSpace, tzBoneSpace);
            int childCount = dataSrc.readInteger();
            if (childCount < 0) {
                throw new IllegalArgumentException("Illegal format of the file. " + "Reading the child count of a bone.");
            }
            Vector<Integer> children = new Vector<Integer>(childCount);
            for (; childCount > 0; childCount--) {
                int childId = dataSrc.readInteger();
                if (childId < 0) {
                    throw new IllegalArgumentException("Illegal format of the file. " + "Reading the child ids of a bone.");
                }
                children.add(childId);
            }
            return new CoreBone(name, parentId, children, trans, rot, transbs, rotbs);
        } catch (JalLoaderException e) {
            throw new IllegalArgumentException("The file is malformed.", e);
        }
    }

    @Override
    public CoreSkeleton load(final InputStream is) {
        try {
            Source dataSrc = new Source(is);
            int boneCount = dataSrc.readInteger();
            if (boneCount < 0) {
                throw new IllegalArgumentException("Illegal format of the file. " + "Reading the bone count.");
            }
            Vector<CoreBone> bones = new Vector<CoreBone>(boneCount);
            for (int boneId = 0; boneId < boneCount; boneId++) {
                CoreBone coreBone = loadCoreBones(dataSrc);
                bones.add(coreBone);
            }
            return new CoreSkeleton(bones);
        } catch (JalLoaderException e) {
            throw new IllegalArgumentException("The file is malformed.", e);
        }
    }
}
