package org.jazzteam.edu.lang.exceptions;

public class VaniyaCheck {

    public void method1(final String otpravitel) throws Exception {
        if (null == otpravitel) {
            throw new VanyaException();
        }
    }

    public void method2(final String otpravitel) throws Exception {
        try {
            method1(otpravitel);
        } catch (VanyaException ve) {
        } catch (PetyaException pe) {
            pe.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
            throw new Exception(t);
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        VaniyaCheck check = new VaniyaCheck();
        check.method2(null);
    }
}
