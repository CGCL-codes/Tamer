package org.openimmunizationsoftware.dqa.validate.immtrac;

public class PfsSupport {

    /**
   * 
   * @param pfsNumber
   * @return
   */
    public static boolean verifyCorrect(String pfsNumber) {
        boolean good = false;
        if (pfsNumber != null && pfsNumber.length() == 10) {
            good = true;
            for (char c : pfsNumber.toCharArray()) {
                if (c < '0' || c > '9') {
                    good = false;
                    break;
                }
            }
            if (good) {
                good = createCheckDigit(pfsNumber.substring(0, 9)) == pfsNumber.charAt(9);
            }
        }
        return good;
    }

    /**
   * This was originally written in C++ for ImmTrac. It has been replicated
   * here in Java. NAB 09/28/2011
   * @param pfs_number
   * @return
   */
    public static char createCheckDigit(String pfs_number) {
        int i = 0;
        int lim = pfs_number.length();
        int one_or_two = 1;
        int product;
        int digit_sum = 0;
        char buf[] = { '\0', '\0' };
        while (i < lim) {
            buf[0] = pfs_number.charAt(i++);
            product = (((int) buf[0]) - ((int) '0')) * one_or_two;
            one_or_two = one_or_two == 2 ? 1 : 2;
            digit_sum += (product < 10) ? product : product - 9;
        }
        return (char) (((int) '0') + ((10 - (digit_sum % 10)) % 10));
    }
}
