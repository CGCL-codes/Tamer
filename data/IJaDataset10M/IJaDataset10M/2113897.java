package kfschmidt.geom3d;

/**
 *
 *
 *
 *
 *   @author Karl Schmidt <kfschmidt@bwh.harvard.edu>
 *   This software is provided for use free of any costs,
 *   Be advised that NO guarantee is made regarding it's quality,
 *   and there is no ongoing support for this codebase.
 *
 *   (c) Karl Schmidt 2003
 *
 *   REVISION HISTORY:
 *
 *
 */
public class MeshMaker implements Runnable {

    public static final int CURRENTLY_MARCHING = 10;

    public static final int CURRENTLY_SMOOTHING = 11;

    public static final int CURRENTLY_INDEXING = 12;

    public static final int CURRENTLY_GEN_NORMALS = 13;

    public static final int FINISHED = 20;

    MeshMakerListener mListener;

    boolean mExitAtNextPass;

    TriangularMesh mMesh;

    Thread mThread;

    double mThreshold = 0.5d;

    ScalarField3D mField;

    Grid3D mGrid;

    SurfaceSmoother mSmoother;

    public MeshMaker(MeshMakerListener listener, ScalarField3D field, Grid3D grid, SurfaceSmoother smoother) {
        mListener = listener;
        mField = field;
        mGrid = grid;
        mSmoother = smoother;
    }

    public MeshMaker(MeshMakerListener listener, ScalarField3D field, Grid3D grid, SurfaceSmoother smoother, double threshold) {
        mThreshold = threshold;
        mListener = listener;
        mField = field;
        mGrid = grid;
        mSmoother = smoother;
    }

    public void makeMeshLowPriority() {
        mExitAtNextPass = false;
        if (mThread == null || !mThread.isAlive()) {
            mThread = new Thread(this);
            mThread.start();
        } else {
            restart();
        }
        mThread.setPriority(Thread.MIN_PRIORITY);
    }

    public void makeMesh() {
        mExitAtNextPass = false;
        if (mThread == null || !mThread.isAlive()) {
            mThread = new Thread(this);
            mThread.start();
        } else {
            restart();
        }
    }

    public TriangularMesh getMesh() {
        return mMesh;
    }

    public void restart() {
        mThread.interrupt();
    }

    public void abort() {
        mExitAtNextPass = true;
        mThread.interrupt();
    }

    public void run() {
        while (!mExitAtNextPass) {
            try {
                Thread.sleep(500);
                if (Thread.interrupted()) throw new InterruptedException("Interrupted while updating vertex normals");
                if (mListener != null) mListener.meshMakerUpdate(CURRENTLY_MARCHING, this);
                MarchingCubes marcher = new MarchingCubes();
                Triangle[] surf = marcher.marchCubes(mField, mGrid, mThreshold);
                if (surf == null) return;
                mMesh = new TriangularMesh(surf);
                if (mListener != null) mListener.meshMakerUpdate(CURRENTLY_INDEXING, this);
                mMesh.updateIndex();
                if (mListener != null) mListener.meshMakerUpdate(CURRENTLY_SMOOTHING, this);
                if (mSmoother != null) mSmoother.smooth(mMesh);
                if (mListener != null) mListener.meshMakerUpdate(CURRENTLY_GEN_NORMALS, this);
                mMesh.calculateVertexNormals();
                if (mListener != null) mListener.meshMakerUpdate(FINISHED, this);
                mExitAtNextPass = true;
            } catch (InterruptedException ex) {
            }
        }
    }
}
