package com.zzsoft.framework.e2p.dw;

import util.basedatatype.ObjectBase;
import util.basedatatype.string.StringBase;
import framework.zze2p.mod.Pojo_0I;
import framework.zze2p.mod.pojodb.PojoDB;
import framework.zze2p.mod.pojodb.PojoDB_I;

public class DWCubeAdm implements DWCubeAdmI {

    protected int iSize;

    protected int iSize1;

    protected int nMax[];

    protected Object[] root;

    protected Class cellClass = PojoDB.class;

    protected boolean isNotReady = true;

    public DWCubeAdm() {
        super();
    }

    public DWCubeAdm(int[] nMax) {
        super();
        this.init(nMax);
    }

    public void removeCube_ForDimension_0(int icmStart, int icmEnd) {
        if (icmEnd >= icmStart) {
            for (int icm = icmStart; icm <= icmEnd; ++icm) {
                this.removeForDimension0(icm);
            }
        } else {
            for (int icm = icmEnd; icm < this.nMax[0]; ++icm) {
                this.removeForDimension0(icm);
            }
            for (int icm = 0; icm < icmStart; ++icm) {
                this.removeForDimension0(icm);
            }
        }
    }

    public void removeForDimension0(int icm) {
        if (icm < 0 || icm >= this.nMax[0]) return;
        this.root[icm] = null;
    }

    public DWCubeAdmI addCellValue(int[] indexs, String attName, Object value) {
        if (indexs == null || isNotReady || indexs.length != iSize || value == null) return this;
        Pojo_0I pojo = this.getCell_WithNewPojo(indexs);
        if (pojo == null) return null;
        Object v = pojo.get(attName);
        v = ObjectBase.add(v, value);
        pojo.set(attName, v);
        return this;
    }

    public DWCubeAdmI addCellValue(String attName, Object value, int... index) {
        return this.addCellValue(index, attName, value);
    }

    public Pojo_0I getCell(int[] index) {
        if (index == null || isNotReady || index.length != iSize) return null;
        Object o = root;
        try {
            for (int i = 0; i < iSize; ++i) {
                o = ((Object[]) o)[index[i]];
                if (o == null) return null;
            }
            return (Pojo_0I) o;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Pojo_0I getCell_WithNewArrayElement(int[] index) {
        if (index == null || isNotReady || index.length != iSize) return null;
        Object o = root;
        Object[] osOld = null;
        try {
            for (int i = 0; i < iSize; ++i) {
                osOld = ((Object[]) o);
                o = osOld[index[i]];
                if (o == null) {
                    if (i < iSize1) {
                        o = new Object[nMax[i + 1]];
                        osOld[index[i]] = o;
                    }
                }
            }
            return (Pojo_0I) o;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Pojo_0I getCell_WithNewPojo(int[] index) {
        if (index == null || isNotReady || index.length != iSize) return null;
        Object o = root;
        Object[] osOld = null;
        try {
            for (int i = 0; i < iSize; ++i) {
                osOld = ((Object[]) o);
                o = osOld[index[i]];
                if (o == null) {
                    if (i < iSize1) {
                        o = new Object[nMax[i + 1]];
                        osOld[index[i]] = o;
                    } else {
                        o = cellClass.newInstance();
                        osOld[index[i]] = o;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return (Pojo_0I) o;
    }

    public Object getCellValue(int[] indexs, String attName) {
        Pojo_0I pojo = this.getCell(indexs);
        if (pojo == null) return null;
        return pojo.get(attName);
    }

    public Object[] getCellValues_Array(int[] indexs, String[] attName) {
        Pojo_0I pojo = this.getCell(indexs);
        if (pojo == null) return null;
        return pojo.getS(attName);
    }

    public Pojo_0I getCellValues_Clone(int[] indexs, Object oAttNames) {
        String[] attNames = StringBase.get2StringArray(oAttNames);
        return getCellValues_Clone(indexs, attNames);
    }

    public Pojo_0I getCellValues_Clone(int[] indexs, String[] attNames) {
        Object[] values = this.getCellValues_Array(indexs, attNames);
        if (values == null) return null;
        try {
            Pojo_0I pojo = (Pojo_0I) this.cellClass.newInstance();
            pojo.setS(attNames, values);
            return pojo;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getCellValue(String attName, int... indexs) {
        return this.getCellValue(indexs, attName);
    }

    public boolean init(int[] nMax) {
        if (nMax == null) return false;
        this.iSize = nMax.length;
        this.iSize1 = this.iSize - 1;
        this.nMax = nMax;
        this.root = new Object[nMax[0]];
        this.isNotReady = false;
        return true;
    }

    public DWCubeAdmI setCell(int[] index, Pojo_0I pojo) {
        if (index == null || isNotReady || index.length != iSize) return null;
        Object o = root;
        Object[] osOld = null;
        try {
            for (int i = 0; i < iSize; ++i) {
                osOld = ((Object[]) o);
                o = osOld[index[i]];
                if (o == null) {
                    if (i < iSize1) {
                        o = new Object[nMax[i + 1]];
                        osOld[index[i]] = o;
                    } else {
                        osOld[index[i]] = pojo;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public DWCubeAdmI setCellValue(int[] indexs, String attName, Object value) {
        if (indexs == null || isNotReady || indexs.length != iSize || attName == null) return this;
        Pojo_0I pojo = this.getCell_WithNewPojo(indexs);
        if (pojo == null) return this;
        pojo.set(attName, value);
        return this;
    }

    public DWCubeAdmI setCellValue(String attName, Object value, int... indexs) {
        this.setCellValue(indexs, attName, value);
        return this;
    }

    public DWCubeAdmI setCellValues(int[] indexs, Object oAttNames, Object oValues) {
        String[] attNames = StringBase.get2StringArray(oAttNames);
        if (indexs == null || isNotReady || indexs.length != iSize || attNames == null || oValues == null) return this;
        Object[] values = null;
        if (oValues instanceof Object[]) {
            values = (Object[]) oValues;
        } else if (oValues instanceof Pojo_0I) {
            values = ((Pojo_0I) oValues).getS(attNames);
        } else {
            values = new Object[] { oValues };
        }
        if (ZZDW_Base.isAllNull(values)) return this;
        Pojo_0I pojo = this.getCell_WithNewPojo(indexs);
        if (pojo == null) return this;
        pojo.setS(attNames, values);
        return this;
    }

    public Object[] getRoot() {
        return root;
    }

    public void setRoot(Object[] root) {
        this.root = root;
    }
}
