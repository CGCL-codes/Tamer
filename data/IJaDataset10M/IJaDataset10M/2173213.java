package net.sf.betterj.model.compile;

import net.sf.betterj.model.parameter.AbstractParameter;
import net.sf.betterj.model.parameter.AbstractParameterAttributes;
import net.sf.betterj.model.parameter.IParameter;
import net.sf.betterj.model.parameter.EParameterAttribute;
import net.sf.betterj.model.parameter.IParameterAttributes;
import net.sf.betterj.model.type.IType;
import net.sf.betterj.model.type.access.IAccessAttributes;

/**
 * @author Viktor Halitsyn
 */
public class TestUtils {

    /**
     * raw interface implementation. isConstant() returns true.
     * 
     * @return instance of IParameterAttributes
     */
    public static IParameterAttributes getConstantAttributes() {
        return new AbstractParameterAttributes(EParameterAttribute.CONSTANT, null) {
        };
    }

    /**
     * raw interface implementation. Name of test type is "TestType"
     * 
     * @return instance of IType
     */
    public static IType getTestType(final String typeName) {
        return new IType() {

            @Override
            public String getName() {
                return typeName;
            }

            @Override
            public void setName(String value) {
            }
        };
    }

    public static IType getTestType() {
        return getTestType("TestType");
    }

    public static IAccessAttributes getPublicAccessAttribute() {
        return new IAccessAttributes() {

            @Override
            public boolean isInternal() {
                return false;
            }

            @Override
            public boolean isPrivate() {
                return false;
            }

            @Override
            public boolean isProtected() {
                return false;
            }

            @Override
            public boolean isPublic() {
                return true;
            }

            @Override
            public void addValue(int value) {
            }

            @Override
            public void resetValue() {
            }
        };
    }

    public static IAccessAttributes getProtectedAccessAttribute() {
        return new IAccessAttributes() {

            @Override
            public boolean isInternal() {
                return false;
            }

            @Override
            public boolean isPrivate() {
                return false;
            }

            @Override
            public boolean isProtected() {
                return true;
            }

            @Override
            public boolean isPublic() {
                return false;
            }

            @Override
            public void addValue(int value) {
            }

            @Override
            public void resetValue() {
            }
        };
    }

    /**
     * Creates a IParameter instance based on given name and type specificators
     * 
     * @param paramName
     * @param paramType
     * @return
     */
    public static IParameter getMethodParameter(final String paramType, final String paramName) {
        return new AbstractParameter() {

            @Override
            public String getName() {
                return paramName;
            }

            @Override
            public IType getType() {
                return getTestType(paramType);
            }
        };
    }
}
