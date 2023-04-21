package vrml.field;

import vrml.*;

public class MFRotation extends MField {

    float[] val;

    public MFRotation() {
    }

    public MFRotation(float rotations[][]) {
        this.val = new float[rotations.length * 4];
        for (int i = 0; i < rotations.length; i++) {
            this.val[i * 4] = rotations[i][0];
            this.val[i * 4 + 1] = rotations[i][1];
            this.val[i * 4 + 2] = rotations[i][2];
            this.val[i * 4 + 3] = rotations[i][3];
        }
    }

    public MFRotation(float rotations[]) {
        this.val = new float[rotations.length];
        System.arraycopy(rotations, 0, this.val, 0, rotations.length);
    }

    public MFRotation(int size, float rotations[]) {
        this.val = new float[size];
        System.arraycopy(rotations, 0, this.val, 0, size);
    }

    public void getValue(float rotations[][]) {
        for (int i = 0; i < rotations.length; i++) {
            rotations[i][0] = this.val[i * 4];
            rotations[i][1] = this.val[i * 4 + 1];
            rotations[i][2] = this.val[i * 4 + 2];
            rotations[i][3] = this.val[i * 4 + 3];
        }
    }

    public void getValue(float rotations[]) {
        System.arraycopy(val, 0, rotations, 0, rotations.length);
    }

    public void get1Value(int index, float rotations[]) {
        rotations[0] = this.val[index * 4];
        rotations[1] = this.val[index * 4 + 1];
        rotations[2] = this.val[index * 4 + 2];
        rotations[3] = this.val[index * 4 + 3];
    }

    public void get1Value(int index, SFRotation rotation) {
        rotation.x = this.val[index * 4];
        rotation.y = this.val[index * 4 + 1];
        rotation.z = this.val[index * 4 + 2];
        rotation.angle = this.val[index * 4 + 3];
    }

    public void setValue(float rotations[][]) {
        this.val = new float[rotations.length * 4];
        for (int i = 0; i < rotations.length; i++) {
            this.val[i * 4] = rotations[i][0];
            this.val[i * 4 + 1] = rotations[i][1];
            this.val[i * 4 + 2] = rotations[i][2];
            this.val[i * 4 + 3] = rotations[i][3];
        }
    }

    public void setValue(float rotations[]) {
        this.val = new float[rotations.length];
        System.arraycopy(rotations, 0, val, 0, rotations.length);
    }

    public void setValue(int size, float rotations[]) {
        this.val = new float[size];
        System.arraycopy(rotations, 0, val, 0, size);
    }

    public void setValue(MFRotation rotations) {
        this.val = new float[rotations.val.length];
        System.arraycopy(rotations.val, 0, this.val, 0, val.length);
    }

    public void setValue(ConstMFRotation rotations) {
        this.val = new float[rotations.val.length];
        System.arraycopy(rotations.val, 0, this.val, 0, val.length);
    }

    public void set1Value(int index, ConstSFRotation rotation) {
        this.val[index * 4] = rotation.x;
        this.val[index * 4 + 1] = rotation.y;
        this.val[index * 4 + 2] = rotation.z;
        this.val[index * 4 + 3] = rotation.angle;
    }

    public void set1Value(int index, SFRotation rotation) {
        this.val[index * 4] = rotation.x;
        this.val[index * 4 + 1] = rotation.y;
        this.val[index * 4 + 2] = rotation.z;
        this.val[index * 4 + 3] = rotation.angle;
    }

    public void set1Value(int index, float axisX, float axisY, float axisZ, float angle) {
        this.val[index * 4] = axisX;
        this.val[index * 4 + 1] = axisY;
        this.val[index * 4 + 2] = axisZ;
        this.val[index * 4 + 3] = angle;
    }

    public void addValue(ConstSFRotation rotation) {
    }

    public void addValue(SFRotation rotation) {
    }

    public void addValue(float axisX, float axisY, float axisZ, float angle) {
    }

    public void insertValue(int index, ConstSFRotation rotation) {
    }

    public void insertValue(int index, SFRotation rotation) {
    }

    public void insertValue(int index, float axisX, float axisY, float axisZ, float angle) {
    }

    public String toString() {
        StringBuffer ret = new StringBuffer('[');
        for (int i = 0; i < val.length; i++) {
            ret.append(' ');
            ret.append(val[i]);
        }
        ret.append(" ]");
        return ret.toString();
    }
}
