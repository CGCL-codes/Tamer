package com.FSS.util;

import java.io.File;
import com.FSS.File.IFile;
import com.FSS.FileList.ChildBox;
import com.FSS.FileList.IBox;
import com.FSS.FileList.ParentBox;
import com.FSS.config.Config;

public class ArchiveManager {

    private IBox[] parent;

    private IBox[] child;

    public ArchiveManager() {
        child = new ChildBox[5];
        for (int i = 0; i < child.length; i++) {
            File file = new File(Config.getValue("DAT") + "child" + i + ".dat");
            if (file.exists()) child[i] = (IBox) FileListUtil.loadFromDisk(file.getAbsolutePath()); else child[i] = new ChildBox();
        }
        child[0].setBoxName("��Ⱥ����");
        child[1].setBoxName("��������");
        child[2].setBoxName("���п���");
        child[3].setBoxName("����");
        child[4].setBoxName("�豸����");
        parent = new ParentBox[7];
        for (int i = 0; i < parent.length; i++) {
            File file = new File(Config.getValue("DAT") + "parent" + i + ".dat");
            if (file.exists()) parent[i] = (IBox) FileListUtil.loadFromDisk(file.getAbsolutePath()); else parent[i] = new ParentBox();
        }
        parent[0].setBoxName("���鵵��");
        parent[1].setBoxName("��ѧ����");
        parent[2].setBoxName("��ʦҵ��");
        parent[3].setBoxName("��Ƶ���");
        parent[4].setBoxName("���񵵰�");
        parent[5].setBoxName("�Ƽ�����");
        parent[6].setBoxName("ʵ�ﵵ��");
        parent[0].addChild(child[0]);
        parent[0].addChild(child[1]);
        parent[5].addChild(child[2]);
        parent[5].addChild(child[3]);
        parent[5].addChild(child[4]);
    }

    public void saveSelf() {
        for (int i = 0; i < child.length; i++) {
            File file = new File(Config.getValue("DAT") + "child" + i + ".dat");
            FileListUtil.WriteToDisk(file.getAbsolutePath(), child[i]);
        }
        for (int i = 0; i < parent.length; i++) {
            File file = new File(Config.getValue("DAT") + "parent" + i + ".dat");
            FileListUtil.WriteToDisk(file.getAbsolutePath(), parent[i]);
        }
    }

    public ParentBox[] getParents() {
        return (ParentBox[]) parent;
    }

    public void moveTo(IFile file, String boxName) {
        for (IBox b : parent) {
            if (b.getBoxName().equals(boxName)) {
                b.add(file);
                return;
            }
        }
        for (IBox b : child) {
            if (b.getBoxName().equals(boxName)) {
                b.add(file);
                return;
            }
        }
    }
}
