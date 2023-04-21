package mpi;

import mpjbuf.*;

public class MultistridedPackerBoolean extends MultistridedPacker {

    public MultistridedPackerBoolean(int rank, int[] indexes, int extent, int size) {
        super(rank, indexes, extent, size);
    }

    public void pack(mpjbuf.Buffer mpjbuf, Object buf, int offset) throws MPIException {
        try {
            mpjbuf.strGather((boolean[]) buf, offset, rank, 0, rank, indexes);
        } catch (Exception e) {
            throw new MPIException(e);
        }
    }

    public void unpack(mpjbuf.Buffer mpjbuf, Object buf, int offset) throws MPIException {
        try {
            mpjbuf.strScatter((boolean[]) buf, offset, rank, 0, rank, indexes);
        } catch (Exception e) {
            throw new MPIException(e);
        }
    }

    public void unpackPartial(mpjbuf.Buffer mpjbuf, int length, Object buf, int offset) throws MPIException {
        int[] cIndexes = new int[2 * rank];
        for (int i = 0; i < 2 * rank; i++) cIndexes[i] = indexes[i];
        int cubeRank = rank - 1;
        int cubeSize = size;
        while (length > 0) {
            cubeSize /= indexes[cubeRank];
            int numCubes = length / cubeSize;
            if (numCubes > 0) {
                cIndexes[cubeRank] = numCubes;
                int blockSize = numCubes * cubeSize;
                try {
                    mpjbuf.strScatter((boolean[]) buf, offset, cubeRank + 1, 0, rank, cIndexes);
                } catch (Exception e) {
                    throw new MPIException(e);
                }
                offset += blockSize;
                length -= blockSize;
            }
            cubeRank--;
        }
    }

    public void pack(mpjbuf.Buffer mpjbuf, Object buf, int offset, int count) throws MPIException {
        if (count == 1) {
            try {
                mpjbuf.strGather((boolean[]) buf, offset, rank, 0, rank, indexes);
            } catch (Exception e) {
                throw new MPIException(e);
            }
        } else {
            int cRank = rank + 1;
            int[] cIndexes = new int[2 * cRank];
            for (int i = 0; i < rank; i++) cIndexes[i] = indexes[i];
            cIndexes[rank] = count;
            for (int i = 0; i < rank; i++) cIndexes[cRank + i] = indexes[rank + i];
            cIndexes[cRank + rank] = extent;
            try {
                mpjbuf.strGather((boolean[]) buf, offset, cRank, 0, cRank, cIndexes);
            } catch (Exception e) {
                throw new MPIException(e);
            }
        }
    }

    public void unpack(mpjbuf.Buffer mpjbuf, Object buf, int offset, int count) throws MPIException {
        if (count == 1) {
            try {
                mpjbuf.strScatter((boolean[]) buf, offset, rank, 0, rank, indexes);
            } catch (Exception e) {
                throw new MPIException(e);
            }
        } else {
            int cRank = rank + 1;
            int[] cIndexes = new int[2 * cRank];
            for (int i = 0; i < rank; i++) cIndexes[i] = indexes[i];
            cIndexes[rank] = count;
            for (int i = 0; i < rank; i++) cIndexes[cRank + i] = indexes[rank + i];
            cIndexes[cRank + rank] = extent;
            try {
                mpjbuf.strScatter((boolean[]) buf, offset, cRank, 0, cRank, cIndexes);
            } catch (Exception e) {
                throw new MPIException(e);
            }
        }
    }
}
