package jp.lnc.MeshLoader.DirectxMeshLoder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import android.util.Log;
import jp.lnc.MeshLoader.GenerickMesh.PanMesh;
import jp.lnc.MeshLoader.GenerickMesh.PanPrigon;

public class VertexAndTextur {

    private static final String tag = "VERTEX";

    List<float[]> mVertexList = new ArrayList<float[]>();

    List<float[]> mTextureCoord = new ArrayList<float[]>();

    ArrayList<int[]> mVertexIndex = new ArrayList<int[]>();

    XfileMeshTree mMeshTree;

    public void printData() {
        printTextureCoord();
        printVertexList();
        printVertexIndex();
    }

    VertexAndTextur(PanMesh panMesh, XfileMeshTree test) {
        mMeshTree = test;
        meshCompile(0, panMesh, mMeshTree);
        printData();
        createPanMesh(panMesh);
    }

    private void createPanMesh(PanMesh panMesh) {
        int prigonNum = mVertexIndex.size();
        for (int i = 0; i < prigonNum; i++) {
            PanPrigon Prigon = panMesh.newPrigon();
            int[] vertexs = mVertexIndex.get(i);
            setPanPrigon(vertexs, Prigon);
        }
    }

    private void setPanPrigon(int[] vertexs, PanPrigon prigon) {
        int vertexCount = vertexs.length;
        prigon.vertexNum = vertexCount;
        for (int j = 0; j < vertexCount; j++) {
            prigon.setAllData(j, mVertexList.get(vertexs[j]), mTextureCoord.get(vertexs[j]));
        }
    }

    XfileMeshTree bean;

    public void meshCompile(int tabNum, PanMesh panMesh, XfileMeshTree mMeshTree2) {
        for (int i = 0; i < mMeshTree2.subTree.size(); i++) {
            bean = mMeshTree2.subTree.get(i);
            switch(bean.xType.typeNo) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    break;
                case 7:
                    createPrigon(bean);
                    break;
                case 8:
                    createMaterialList(bean);
                    break;
                case 9:
                    createMaterial(bean);
                    break;
                case 10:
                    createTextureCoord(bean);
                    break;
                default:
                    System.out.println(bean.xType.getString());
            }
            meshCompile(tabNum + 1, panMesh, bean);
        }
    }

    private void createTextureCoord(XfileMeshTree bean) {
        int meshNum = Integer.valueOf(((String) bean.string.get(0)).replaceAll(";", ""));
        int index = 0;
        float[] newTextureCoord = newTextureCoord();
        for (index++; index < (meshNum * 3 + 1); index++) {
            if (index % 3 != 0) {
                newTextureCoord[(index % 3 - 1)] = 1 - Float.valueOf(((String) bean.string.get(index)).replace(";", ""));
            } else {
                newTextureCoord = newTextureCoord();
            }
        }
    }

    private void createMaterial(XfileMeshTree bean) {
    }

    private void createMaterialList(XfileMeshTree bean) {
        int materialNum = Integer.valueOf(((String) bean.string.get(0)).replaceAll(";", ""));
        int machNum = Integer.valueOf(((String) bean.string.get(1)).replaceAll(";", ""));
        for (int i = 0; i < machNum; i++) {
            int mach = Integer.valueOf(((String) bean.string.get(i + 2)).replaceAll("[;,]", ""));
        }
    }

    private void createPrigon(XfileMeshTree bean) {
        System.out.println(bean.string.get(0));
        List<float[]> tops = new ArrayList<float[]>();
        int meshNum = Integer.valueOf(((String) bean.string.get(0)).replaceAll(";", ""));
        int index = 0;
        float[] newTop = newVertexList();
        for (index++; index < (meshNum * 4 + 1); index++) {
            if (index % 4 != 0) {
                float top = Float.valueOf(((String) bean.string.get(index)).replace(";", ""));
                newTop[(index % 4 - 1)] = top;
            } else {
                tops.add(newTop);
                newTop = newVertexList();
            }
        }
        int prigonNum = Integer.valueOf(((String) bean.string.get(index++)).replaceAll(";", ""));
        int i;
        int topMax = 0;
        for (i = 0; i < prigonNum * 3; i++) {
            String tmp = (String) bean.string.get(index + i);
            if (i % 3 == 1) {
                int[] Prigon = newVertexIndex(topMax);
                String[] strings = tmp.split("[,;]");
                for (int j = 0; j < topMax; j++) {
                    Prigon[j] = Integer.valueOf(strings[j]);
                }
            } else if (i % 3 == 0) {
                topMax = Integer.valueOf(tmp.replace(";", ""));
            }
        }
    }

    private int[] newVertexIndex(int topMax) {
        int[] ret = new int[topMax];
        mVertexIndex.add(ret);
        return ret;
    }

    private float[] newVertexList() {
        float[] ret = new float[3];
        mVertexList.add(ret);
        return ret;
    }

    private float[] newTextureCoord() {
        float[] ret = new float[2];
        mTextureCoord.add(ret);
        return ret;
    }

    private void printVertexIndex() {
        String printString;
        for (int i = 0; i < mVertexIndex.size(); i++) {
            int[] bean = mVertexIndex.get(i);
            printString = "";
            for (int j = 0; j < bean.length; j++) {
                printString += (j + ":" + bean[j] + " ");
            }
            Log.d(tag, "VertexIndex " + printString);
        }
    }

    private void printVertexList() {
        String printString;
        for (int i = 0; i < mVertexList.size(); i++) {
            float[] bean = mVertexList.get(i);
            printString = "";
            for (int j = 0; j < bean.length; j++) {
                printString += (j + ":" + bean[j] + " ");
            }
            Log.d(tag, "VertexList " + printString);
        }
    }

    private void printTextureCoord() {
        String printString;
        for (int i = 0; i < mTextureCoord.size(); i++) {
            float[] bean = mTextureCoord.get(i);
            printString = "";
            for (int j = 0; j < bean.length; j++) {
                printString += (j + ":" + bean[j] + " ");
            }
            Log.d(tag, "TextureCoords " + printString);
        }
    }
}
