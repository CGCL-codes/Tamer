package wei.liu.myhealth.entity;

import java.util.Date;

/**
 * Create by 2012-4-21
 * 
 * @author Liuw
 * @copyright Copyright(c) 2012- Liuw
 */
public class MyIndex extends BaseBean {

    private float minNum;

    private float maxNum;

    private Date create;

    private Norm norm;

    public int getId() {
        return super.id;
    }

    public void setId(int id) {
        super.id = id;
    }

    public int getNid() {
        return super.nid;
    }

    public void setNid(int nid) {
        super.nid = nid;
    }

    public float getMinNum() {
        return this.minNum;
    }

    public void setMinNUm(float minNum) {
        this.minNum = minNum;
    }

    public float getMaxNum() {
        return this.maxNum;
    }

    public void setMaxNum(float maxNum) {
        this.maxNum = maxNum;
    }

    public Date getCreate() {
        return this.create;
    }

    public Norm getNorm() {
        return this.norm;
    }

    public void setNorm(Norm norm) {
        this.norm = norm;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public MyIndex() {
    }

    public MyIndex(int id, int nid, float minNum, float maxNum, Date create, Norm norm) {
        super.setId(id);
        super.setNid(nid);
        this.setMinNUm(minNum);
        this.setMaxNum(maxNum);
        this.setCreate(create);
        this.setNorm(norm);
    }

    public MyIndex(int nid, float minNum, float maxNum, Date create, Norm norm) {
        super.setNid(nid);
        this.setMinNUm(minNum);
        this.setMaxNum(maxNum);
        this.setCreate(create);
        this.setNorm(norm);
    }
}
