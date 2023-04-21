package rj.tools.jcsc;

/**
 * <code>Return</code> Collection of Return classes; used by the grammar to
 * return complex return values
 *
 * @author Ralph Jocham
 * @version __0.98.2__
 */
public interface Return {

    /**
    *  <code>MethodDeclaratorReturn</code> 
    *
    */
    public static final class MethodDeclaratorReturn {

        String mName;

        int mParaCount;

        /**
       * Creates a new <code>MethodDeclaratorReturn</code> instance.
       *
       * @param name a <code>StringBuffer</code> value
       * @param paracount an <code>int</code> value
       */
        public MethodDeclaratorReturn(String name, int paracount) {
            mName = name;
            mParaCount = paracount;
        }

        /**
       * <code>getMethodName</code>
       *
       * @return a <code>StringBuffer</code> value
       */
        public String getMethodName() {
            return mName;
        }

        /**
       * <code>getParaCount</code>
       *
       * @return an <code>int</code> value
       */
        public int getParaCount() {
            return mParaCount;
        }
    }

    /**
    * <code>FormalParameterReturn</code>
    *
    */
    public static final class FormalParameterReturn {

        String mType;

        String mName;

        /**
       * Creates a new <code>FormalParameterReturn</code> instance.
       *
       * @param type a <code>StringBuffer</code> value
       * @param name a <code>StringBuffer</code> value
       */
        public FormalParameterReturn(String type, String name) {
            mType = type;
            mName = name;
        }

        /**
       * <code>getParameterType</code> 
       *
       * @return a <code>StringBuffer</code> value
       */
        public String getParameterType() {
            return mType;
        }

        /**
       * <code>getParameterName</code>
       *
       * @return a <code>StringBuffer</code> value
       */
        public String getParameterName() {
            return mName;
        }
    }
}
