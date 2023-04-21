package imtek.optsuite.analysis.zernike;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class that can generates Zernike terms and Java classes
 * that compute this terms.
 * 
 * It uses the following definitions to compute the terms.
 * 
 * U_n_m(r, th) = R_n_m(r) {sin / cos} (m*th)<br/>
 * where R_n_m(r) is <br/>
 * R_n_m(r) = Sum(s=0, n-m/2, (-1)^s * (n-s)! / ( s! ((n-m)/2 - s)! ((n+m)/2 - s)! ) * r^(n-2s)
 * 
 * 
 * @author Alexander Bieber <fleque@users.sourceforge.net>
 */
public class ZernikeTermHelper {

    /**
	 * Returns an ordered list of all ZernikeTerms up to the 
	 * given order.
	 * 
	 * @param order Maximum order of ZernikeTerms to return
	 * @return An ordered list of all ZernikeTerms up to the 
	 * given order.
	 */
    public static List<ZernikeTermDescriptor> getZernikeTerms(int order) {
        List<ZernikeTermDescriptor> result = new ArrayList<ZernikeTermDescriptor>();
        int i = 1;
        for (int n = 0; n <= order; n++) {
            List<ZernikeTermDescriptor> tmpDescs = new ArrayList<ZernikeTermDescriptor>();
            int m = n;
            while (m >= 0) {
                String polHum = getZernikeTermRadialPolynomHuman(n, m);
                String polJava = getZernikeTermRadialPolynomJava(n, m);
                ZernikeTermDescriptor descriptor = new ZernikeTermDescriptor();
                descriptor.setZernikeNo(i++);
                descriptor.setN(n);
                descriptor.setM(m);
                descriptor.setHumanReadable(polHum);
                descriptor.setJavaExpression(polJava);
                tmpDescs.add(descriptor.cloneDescriptor());
                result.add(descriptor.cloneDescriptor());
                m = m - 2;
            }
            for (int j = tmpDescs.size() - 1; j >= 0; j--) {
                ZernikeTermDescriptor descriptor = tmpDescs.get(j);
                if (descriptor.getM() == 0) continue;
                ZernikeTermDescriptor z = descriptor.cloneDescriptor();
                z.setM(-z.getM());
                z.setZernikeNo(i++);
                result.add(z);
            }
        }
        for (ZernikeTermDescriptor term : result) {
            if (term.getM() == 0) {
                term.setJavaExpression("return " + term.getJavaExpression() + ";");
            } else if (term.getM() > 0) {
                term.setHumanReadable(term.getHumanReadable() + " sin(" + Math.abs(term.getM()) + "th)");
                term.setJavaExpression("return " + term.getJavaExpression() + " * Math.sin(" + Math.abs(term.getM()) + " * th);");
            } else if (term.getM() < 0) {
                term.setHumanReadable(term.getHumanReadable() + " cos(" + Math.abs(term.getM()) + "th)");
                term.setJavaExpression("return " + term.getJavaExpression() + " * Math.cos(" + Math.abs(term.getM()) + " * th);");
            }
        }
        return result;
    }

    /**
	 * Human readable version of the radial Polynom
	 */
    private static String getZernikeTermRadialPolynomHuman(int n, int m) {
        String result = "";
        int s = 0;
        for (s = 0; s <= (n - m) / 2; s++) {
            double factor = Math.pow(-1, s) * (factorial(n - s)) / (factorial(s) * factorial((n - m) / 2 - s) * factorial((n + m) / 2 - s));
            ;
            int exp = n - 2 * s;
            if (s != 0) {
                if (factor > 0) result = result + " + ";
            }
            result = result + " " + factor + "r^" + exp;
        }
        return "( " + result + " )";
    }

    /**
	 * Java expression
	 */
    private static String getZernikeTermRadialPolynomJava(int n, int m) {
        String result = "";
        int s = 0;
        for (s = 0; s <= (n - m) / 2; s++) {
            double factor = Math.pow(-1, s) * (factorial(n - s)) / (factorial(s) * factorial((n - m) / 2 - s) * factorial((n + m) / 2 - s));
            ;
            int exp = n - 2 * s;
            if (s != 0) {
                if (factor > 0) result = result + " + ";
            }
            result = result + " " + factor + " * Math.pow(r," + exp + ")";
        }
        return "( " + result + " )";
    }

    private static void writeStreamLn(OutputStream out, String data) throws IOException {
        out.write((data + "\n").getBytes());
    }

    /**
	 * Writes a Java class to the given stream that will have 
	 * one method to calculate each Zerniketerm up to the given
	 * order. Additionally one generic method will be created with a 
	 * switch to all methods.  
	 * 
	 * @param _package The package the class should be put in.
	 * @param order The maximum order of Zernike terms to generate.
	 * @param out The stream to write to.
	 */
    public static void writeJavaClass(String _package, int order, OutputStream out) throws IOException {
        writeStreamLn(out, "package " + _package + ";");
        writeStreamLn(out, "");
        writeStreamLn(out, "");
        writeStreamLn(out, "/**");
        writeStreamLn(out, " * Zernike term calculation class generated by ZernikeTermHelper.");
        writeStreamLn(out, " * This can compute the values of Zernike terms up to order " + order + ".");
        writeStreamLn(out, " * @author Alexander Bieber");
        writeStreamLn(out, " */");
        writeStreamLn(out, "public class ZernikeTerms {");
        List<ZernikeTermDescriptor> terms = getZernikeTerms(order);
        writeStreamLn(out, "");
        writeStreamLn(out, "\tpublic static double getZernikeTermValue(int termNo, double r, double th) {");
        writeStreamLn(out, "\t\tswitch(termNo) {");
        for (ZernikeTermDescriptor term : terms) {
            writeStreamLn(out, "\t\t\tcase " + term.getZernikeNo() + ": return getZernikeTermValue" + term.getZernikeNo() + "(r, th);");
        }
        writeStreamLn(out, "\t\t\tdefault: throw new UnsupportedOperationException(\"The Zernike term \"+termNo+\" is not supported by this class.\");");
        writeStreamLn(out, "\t\t}");
        writeStreamLn(out, "\t}");
        for (ZernikeTermDescriptor term : terms) {
            writeStreamLn(out, "");
            writeStreamLn(out, "\t/**");
            writeStreamLn(out, "\t * Method for calculation of Zernike term " + term.getZernikeNo());
            writeStreamLn(out, "\t * Computes " + term.getHumanReadable());
            writeStreamLn(out, "\t * @return Zernike term " + term.getZernikeNo() + " (" + term.getHumanReadable() + ")");
            writeStreamLn(out, "\t */");
            writeStreamLn(out, "\tpublic static double getZernikeTermValue" + term.getZernikeNo() + "(double r, double th) {");
            writeStreamLn(out, "\t\t" + term.getJavaExpression());
            writeStreamLn(out, "\t}");
        }
        writeStreamLn(out, "}");
    }

    public static void main(String[] args) {
        try {
            writeJavaClass("imtek.optsuite.analysis.zernike", 15, System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Computes the maximum number of Zernike terms when
	 * all terms up to the given order are considered.
	 */
    public static int getMaxZernikeNoForOrder(int order) {
        int result = 0;
        for (int i = 0; i <= order; i++) result += i + 1;
        return result;
    }

    /**
	 * Computes the factorial of the given parameter.
	 * Will throw an {@link IllegalArgumentException}
	 * when the parameter is negative (fac not defined)
	 * or > 20 as the value could not be computed then (Max long would not be enough).
	 */
    public static long factorial(long n) {
        if (n < 0) throw new IllegalArgumentException("Factorial of negative values is not defined."); else if (n > 20) throw new IllegalArgumentException("Overflow error in factorial"); else if (n == 0) return 1; else return n * factorial(n - 1);
    }
}
