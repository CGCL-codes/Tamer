package openminer.classifier.decisiontree.id3;

import java.util.*;

public class Matrix {

    /**Ԫ���������Ԫ���*/
    private ArrayList mMetaDataList = new ArrayList();

    /**Ԫ���б�*/
    private ArrayList mInstanceList = new ArrayList();

    /**the current index of instance*/
    private int mCurrentIndex = -1;

    /**Constructing a Matrix class*/
    public Matrix() {
    }

    /**���������Ԫ����б�*/
    public ArrayList getMetaDataList() {
        return mMetaDataList;
    }

    public void setMetaDataList(ArrayList metaDataList) {
        mMetaDataList = metaDataList;
    }

    /**add meta data into the meta data list*/
    public void addMetaData(MetaData metadata) {
        mMetaDataList.add(metadata);
    }

    /**��������������������*/
    public int getIndexByName(String name) {
        int i = 0;
        for (i = 0; i < mMetaDataList.size() && !((MetaData) (mMetaDataList.get(i))).getName().toUpperCase().equals(name.toUpperCase()); i++) ;
        if (i >= mMetaDataList.size()) return -1; else return i;
    }

    /**���������Ҷ�Ӧ��Ԫ���*/
    public MetaData getMetaData(int index) {
        if (index < 0 || index >= mMetaDataList.size()) return null;
        return (MetaData) (mMetaDataList.get(index));
    }

    /**���Ԫ���б�*/
    public ArrayList getInstanceList() {
        return mInstanceList;
    }

    /**���Ԫ�����*/
    public int getInstanceCount() {
        return mInstanceList.size();
    }

    /** set Instance list*/
    public void setInstanceList(ArrayList instancelist) {
        mInstanceList = instancelist;
    }

    /**add an instance into the instance list*/
    public void addInstance(Instance instance) {
        mInstanceList.add(instance);
    }

    /**get the next of currennt instance */
    public Instance getNext() {
        mCurrentIndex++;
        if (mCurrentIndex < 0 || mCurrentIndex >= mInstanceList.size()) return null;
        return (Instance) (mInstanceList.get(mCurrentIndex));
    }

    /**reset the current index to -1*/
    public void ResetIndex() {
        mCurrentIndex = -1;
    }

    public String toString() {
        String title = "";
        for (int i = 0; i < mMetaDataList.size(); i++) title += (MetaData) (mMetaDataList.get(i));
        String content = "";
        for (int j = 0; j < mInstanceList.size(); j++) content += (Instance) (mInstanceList.get(j)) + "\n";
        return title + "\n" + content;
    }
}
