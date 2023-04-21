package logic.mail;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * This class has all methods necessary to use and convert e-mail addresses.
* @author jarek
*
*/
public class BAddressImpl extends InternetAddress implements BAddress, java.io.Serializable {

    public BAddressImpl(String f) throws AddressException {
        super(f);
    }

    /**
     * @param address The address to set.
     */
    public BAddressImpl(BAddressImpl addres) throws AddressException {
        super(addres.toString());
    }

    public BAddressImpl(InternetAddress addres) throws AddressException {
        super(addres.toString());
    }

    public String toStringInWindow() {
        return getPersonal() + "<" + getAddress() + ">";
    }

    public static String ArrayToString(BAddressImpl[] adr) {
        if ((adr == null) || (adr.length == 0)) return "";
        return InternetAddress.toString((InternetAddress[]) adr);
    }

    private static boolean goodAddress(String s) {
        int i;
        boolean bool = false;
        for (i = 0; i < s.length(); ++i) if ((s.charAt(i) != ' ') || (s.charAt(i) != '	')) bool = true;
        return bool;
    }

    public static String deleteWhiteSpace(String str) {
        int poz1 = 0, poz2 = str.length();
        if ((str == null) || (str == "") || (str == " ") || (str == "  ")) {
            return "";
        }
        while ((poz2 >= poz1 + 1) && (str.charAt(poz1) == ' ')) poz1++;
        while ((poz2 >= poz1 + 1) && (str.charAt(poz2 - 1) == ' ')) poz2--;
        return str.substring(poz1, poz2);
    }

    private static String[] ArrayToStringSimple(String addr) {
        int count = 0, poz1 = 0, poz2 = 0, index = 0;
        if (addr == null) return new String[0];
        if ((addr == "") || (addr == " ") || (addr == "  ") || (addr == "   ") || (addr == "	")) return new String[0];
        String str;
        do {
            poz1 = poz2;
            if (poz1 == 0) {
                poz2 = addr.indexOf(',');
                if (poz2 != -1) str = addr.substring(0, poz2); else str = addr.substring(0);
            } else {
                poz2 = addr.indexOf(',', poz1 + 1);
                if (poz2 != -1) str = addr.substring(poz1 + 1, poz2); else str = addr.substring(poz1 + 1);
            }
            if (goodAddress(str)) count++;
        } while (poz2 != -1);
        String[] ret = new String[count];
        index = 0;
        do {
            poz1 = poz2;
            if (poz1 == 0) {
                poz2 = addr.indexOf(',');
                if (poz2 != -1) str = addr.substring(0, poz2); else str = addr.substring(0);
            } else {
                poz2 = addr.indexOf(',', poz1 + 1);
                if (poz2 != -1) str = addr.substring(poz1 + 1, poz2); else str = addr.substring(poz1 + 1);
            }
            if (goodAddress(str)) {
                ret[index] = str;
                index++;
            }
        } while (poz2 != -1);
        return ret;
    }

    private static BAddressImpl StringToSingleAddress(String str) {
        if (str.indexOf('@') == -1) return null;
        if (str.indexOf('@') < str.indexOf('<')) return null;
        try {
            BAddressImpl addr = new BAddressImpl(str);
            return addr;
        } catch (AddressException e) {
        }
        return null;
    }

    public static DatabaseAddress[] StringToArrayOfDatabaseAddress(String addr) {
        int count = 0, index = 0, i;
        String array[] = ArrayToStringSimple(addr);
        DatabaseAddress[] ret = new DatabaseAddress[array.length];
        for (i = 0; i < array.length; ++i) {
            DatabaseAddress data = new DatabaseAddress();
            BAddressImpl ad = StringToSingleAddress(array[i]);
            if (ad == null) {
                data.personal = array[i];
                data.address = "";
            } else {
                if (ad.getPersonal() != null) data.personal = ad.getPersonal(); else data.personal = "";
                data.address = ad.getAddress();
            }
            ret[i] = data;
        }
        return ret;
    }

    public int compareTo(Object o) {
        BAddress other = (BAddress) o;
        return (toString().compareTo(other.toString()));
    }
}
