package edu.mit.aero.foamcut.msggen;

/**
 *
 * @author mschafer
 */
public class SFInt16Info extends FieldInfo {

    public SFInt16Info(String name, String type) {
        super(name, type, 1);
    }

    public String getJavaVarName() {
        return new String("m_" + m_name);
    }

    @Override
    public String getJavaVarDecl() {
        return new String("public short " + getJavaVarName() + ";");
    }

    @Override
    public String getJavaBBGet() {
        return new String(getJavaVarName() + " = bb.getShort();");
    }

    @Override
    public String getJavaBBPut() {
        return new String("bb.putShort(" + getJavaVarName() + ");");
    }

    @Override
    public int getSize() {
        return 2 * Byte.SIZE / 8;
    }

    @Override
    public String getCVarName() {
        return getJavaVarName();
    }

    @Override
    public String getCVarDecl() {
        return new String("int16_t " + getCVarName() + ";");
    }

    @Override
    public String getJavaAccessors() {
        String acc = new String();
        acc += "    public int get<Name>() { return <Var>; }" + s_le;
        acc += "    public void set<Name>(short val) { <Var> = val; }";
        acc = acc.replaceAll("<Name>", m_name);
        acc = acc.replaceAll("<Var>", getJavaVarName());
        return acc;
    }
}
