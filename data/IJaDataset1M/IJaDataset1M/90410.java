package org.gamenet.application.mm8leveleditor.data.mm6.outdoor;

import java.util.ArrayList;
import java.util.List;
import org.gamenet.util.ByteConversions;

public class TerrainNormalMapData {

    private float terranNormalDistanceMap0[][] = null;

    private float terranNormalDistanceMap1[][] = null;

    private int terranNormalHandleMap0[][] = null;

    private int terranNormalHandleMap1[][] = null;

    private List terrainNormalVertexList = null;

    public TerrainNormalMapData() {
        super();
    }

    public int initialize(byte[] data, int offset, int MAP_WIDTH, int MAP_HEIGHT) {
        int normalsCount = ByteConversions.getIntegerInByteArrayAtPosition(data, offset);
        offset += 4;
        terranNormalDistanceMap0 = new float[MAP_HEIGHT][MAP_WIDTH];
        terranNormalDistanceMap1 = new float[MAP_HEIGHT][MAP_WIDTH];
        for (int heightIndex = 0; heightIndex < terranNormalDistanceMap0.length; heightIndex++) {
            for (int widthIndex = 0; widthIndex < terranNormalDistanceMap0[heightIndex].length; widthIndex++) {
                terranNormalDistanceMap0[heightIndex][widthIndex] = ByteConversions.getFloatInByteArrayAtPosition(data, offset);
                offset += 4;
                terranNormalDistanceMap1[heightIndex][widthIndex] = ByteConversions.getFloatInByteArrayAtPosition(data, offset);
                offset += 4;
            }
        }
        terranNormalHandleMap0 = new int[MAP_HEIGHT][MAP_WIDTH];
        terranNormalHandleMap1 = new int[MAP_HEIGHT][MAP_WIDTH];
        for (int heightIndex = 0; heightIndex < terranNormalHandleMap0.length; heightIndex++) {
            for (int widthIndex = 0; widthIndex < terranNormalHandleMap0[heightIndex].length; widthIndex++) {
                terranNormalHandleMap0[heightIndex][widthIndex] = ByteConversions.getUnsignedShortInByteArrayAtPosition(data, offset);
                offset += 2;
                terranNormalHandleMap1[heightIndex][widthIndex] = ByteConversions.getUnsignedShortInByteArrayAtPosition(data, offset);
                offset += 2;
            }
        }
        terrainNormalVertexList = new ArrayList();
        offset = IntVertex.populateObjects(data, offset, terrainNormalVertexList, normalsCount);
        return offset;
    }

    public int updateData(byte[] newData, int offset) {
        ByteConversions.setIntegerInByteArrayAtPosition(terrainNormalVertexList.size(), newData, offset);
        offset += 4;
        for (int heightIndex = 0; heightIndex < terranNormalDistanceMap0.length; heightIndex++) {
            for (int widthIndex = 0; widthIndex < terranNormalDistanceMap0[heightIndex].length; widthIndex++) {
                ByteConversions.setFloatInByteArrayAtPosition(terranNormalDistanceMap0[heightIndex][widthIndex], newData, offset);
                offset += 4;
                ByteConversions.setFloatInByteArrayAtPosition(terranNormalDistanceMap1[heightIndex][widthIndex], newData, offset);
                offset += 4;
            }
        }
        for (int heightIndex = 0; heightIndex < terranNormalHandleMap0.length; heightIndex++) {
            for (int widthIndex = 0; widthIndex < terranNormalHandleMap0[heightIndex].length; widthIndex++) {
                ByteConversions.setShortInByteArrayAtPosition((short) terranNormalHandleMap0[heightIndex][widthIndex], newData, offset);
                offset += 2;
                ByteConversions.setShortInByteArrayAtPosition((short) terranNormalHandleMap1[heightIndex][widthIndex], newData, offset);
                offset += 2;
            }
        }
        return offset;
    }

    public int getRecordSize() {
        int newDataSize = 0;
        newDataSize += 4;
        newDataSize += terranNormalDistanceMap0.length * terranNormalDistanceMap0[0].length * 4;
        newDataSize += terranNormalDistanceMap1.length * terranNormalDistanceMap1[0].length * 4;
        newDataSize += terranNormalHandleMap0.length * terranNormalHandleMap0[0].length * 2;
        newDataSize += terranNormalHandleMap1.length * terranNormalHandleMap1[0].length * 2;
        newDataSize += terrainNormalVertexList.size() * IntVertex.getRecordSize();
        return newDataSize;
    }

    public List getTerrainNormalVertexList() {
        return this.terrainNormalVertexList;
    }

    public float[][] getTerranNormalDistanceMap0() {
        return this.terranNormalDistanceMap0;
    }

    public float[][] getTerranNormalDistanceMap1() {
        return this.terranNormalDistanceMap1;
    }

    public int[][] getTerranNormalHandleMap0() {
        return this.terranNormalHandleMap0;
    }

    public int[][] getTerranNormalHandleMap1() {
        return this.terranNormalHandleMap1;
    }
}
