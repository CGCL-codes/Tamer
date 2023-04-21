package com.groundspeak.mochalua;

/**
 *
 * @author a.fornwald
 */
class Function extends Collectable {

    private LuaFunction m_LuaFunction;

    private JavaFunction m_JavaFunction;

    private Table m_Environment;

    private UpValue[] m_UpValues;

    public Function(LuaFunction luaFunction, Table environment) {
        super();
        this.m_Environment = environment;
        this.m_LuaFunction = luaFunction;
        this.m_JavaFunction = null;
        this.m_UpValues = new UpValue[luaFunction.GetUpValuesQuantity()];
    }

    public Function(JavaFunction javaFunction, Table environment, int iUpValuesQuantity) {
        this.m_Environment = environment;
        this.m_JavaFunction = javaFunction;
        this.m_LuaFunction = null;
        if (iUpValuesQuantity > 0) {
            this.m_UpValues = new UpValue[iUpValuesQuantity];
        }
    }

    public boolean IsLuaFunction() {
        return this.m_JavaFunction == null && this.m_LuaFunction != null;
    }

    public boolean IsJavaFunction() {
        return this.m_LuaFunction == null && this.m_JavaFunction != null;
    }

    public final LuaFunction GetLuaFunction() {
        return this.m_LuaFunction;
    }

    public final JavaFunction GetJavaFunction() {
        return this.m_JavaFunction;
    }

    public final void SetEnvironment(Table environment) {
        this.m_Environment = environment;
    }

    public final Table GetEnvironment() {
        return this.m_Environment;
    }

    public final UpValue GetUpValue(int iIndex) {
        return this.m_UpValues[iIndex];
    }

    public final void SetUpValue(int iIndex, UpValue value) {
        this.m_UpValues[iIndex] = value;
    }

    public final int GetUpValuesQuantity() {
        if (this.m_UpValues == null) {
            return 0;
        } else {
            return m_UpValues.length;
        }
    }
}
