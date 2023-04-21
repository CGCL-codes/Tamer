package org.compiere.util;

/**
 *      Amount in Words for Thai
 *
 *  @author Sureeraya Limpaibul - http://www.rgagnon.com/javadetails/java-0426.h
tml
 *  @version $Id: AmtInWords_TH.java,v 1.2 2005/04/19 04:43:31 jjanke Exp $
 */
public class AmtInWords_TH implements AmtInWords {

    /**
         *      AmtInWords_TH
		 *
		 *	0.23 = ศูนย์ 23/100
		 *	1.23 = หนึ่งบาท 23/100
		 *	11.45 = สิบเอ็ดบาท 45/100
		 *	121.45 = หนึ่งร้อยยี่สิบเอ็ดบาท 45/100
		 *	1231.56 = หนึ่งพันสองร้อยสามสิบเอ็ดบาท 56/100
		 *	12341.78 = หนึ่งหมื่นสองพันสามร้อยสี่สิบเอ็ดบาท 78/100
		 *	123451.89 = หนึ่งแสนสองหมื่นสามพันสี่ร้อยห้าสิบเอ็ดบาท 89/100
		 *	12234571.90 = สิบสองล้าน สองแสนสามหมื่นสี่พันห้าร้อยเจ็ดสิบเอ็ดบาท 90/100
		 *	123234571.90 = หนึ่งร้อยยี่สิบสามล้าน สองแสนสามหมื่นสี่พันห้าร้อยเจ็ดสิบเอ็ดบาท 90/100
		 *	1987234571.90 = หนึ่งพันเก้าร้อยแปดสิบเจ็ดล้าน สองแสนสามหมื่นสี่พันห้าร้อยเจ็ดสิบเอ็ดบาท 90/100
		 *	0.00 = ศูนย์ 00/100
		 *
         */
    public AmtInWords_TH() {
        super();
    }

    private static final String[] majorNames = { "", "ล้าน " };

    private static final String[] hundredThousandNames = { "", "หนึ่งแสน", "สองแสน", "สามแสน", "สี่แสน", "ห้าแสน", "หกแสน", "เจ็ดแสน", "แปดแสน", "เก้าแสน" };

    private static final String[] tenThousandNames = { "", "หนึ่งหมื่น", "สองหมื่น", "สามหมื่น", "สี่หมื่น", "ห้าหมื่น", "หกหมื่น", "เจ็ดหมื่น", "แปดหมื่น", "เก้าหมื่น" };

    private static final String[] thousandNames = { "", "หนึ่งพัน", "สองพัน", "สามพัน", "สี่พัน", "ห้าพัน", "หกพัน", "เจ็ดพัน", "แปดพัน", "เก้าพัน" };

    private static final String[] hundredNames = { "", "หนึ่งร้อย", "สองร้อย", "สามร้อย", "สี่ร้อย", "ห้าร้อย", "หกร้อย", "เจ็ดร้อย", "แปดร้อย", "เก้าร้อย" };

    private static final String[] tensNames = { "", "สิบ", "ยี่สิบ", "สามสิบ", "สี่สิบ", "ห้าสิบ", "หกสิบ", "เจ็ดสิบ", "แปดสิบ", "เก้าสิบ" };

    private static final String[] numNames = { "", "หนึ่ง", "สอง", "สาม", "สี่", "ห้า", "หก", "เจ็ด", "แปด", "เก้า", "สิบ", "สิบเอ็ด", "สิบสอง", "สิบสาม", "สิบสี่", "สิบห้า", "สิบหก", "สิบเจ็ด", "สิบแปด", "สิบเก้า" };

    /**
         *      Convert Less Than One Thousand
         *      @param number
         *      @return
         */
    private String convertLessThanOneMillion(int number) {
        String soFar = new String();
        System.out.println("[convertLessThanOneMillion] number = " + number);
        if (number != 0) {
            soFar = numNames[number % 10];
            if (number != 1 && soFar.equals("หนึ่ง")) {
                soFar = "เอ็ด";
            }
            number /= 10;
            soFar = tensNames[number % 10] + soFar;
            number /= 10;
            soFar = hundredNames[number % 10] + soFar;
            number /= 10;
            soFar = thousandNames[number % 10] + soFar;
            number /= 10;
            soFar = tenThousandNames[number % 10] + soFar;
            number /= 10;
            soFar = hundredThousandNames[number % 10] + soFar;
            number /= 10;
        }
        if (number == 0) {
            return soFar;
        }
        return numNames[number] + "ร้อยล้าน" + soFar;
    }

    /**
         *      Convert
         *      @param number
         *      @return
         */
    private String convert(int number) {
        if (number == 0) {
            return "ศูนย์";
        }
        String prefix = "";
        if (number < 0) {
            number = -number;
            prefix = "ลบ ";
        }
        String soFar = "";
        int place = 0;
        do {
            double d = number % 1000000;
            int n = (int) d;
            if (n != 0) {
                String s = convertLessThanOneMillion(n);
                place = place > 0 ? 1 : 0;
                soFar = s + majorNames[place] + soFar;
            }
            place++;
            number /= 1000000d;
        } while (number > 0);
        return (prefix + soFar).trim();
    }

    /***********************************************************************
***
         *      Get Amount in Words
         *      @param amount numeric amount (352.80)
         *      @return amount in words (three*five*two 80/100)
         */
    public String getAmtInWords(String amount) throws Exception {
        if (amount == null) return amount;
        StringBuffer sb = new StringBuffer();
        int pos = amount.lastIndexOf('.');
        int pos2 = amount.lastIndexOf(',');
        if (pos2 > pos) pos = pos2;
        String oldamt = amount;
        amount = amount.replaceAll(",", "");
        int newpos = amount.lastIndexOf('.');
        if (newpos != -1) {
            int pesos = Integer.parseInt(amount.substring(0, newpos));
            System.out.println(pesos);
            sb.append(convert(pesos)).append("บาท");
            for (int i = 0; i < oldamt.length(); i++) {
                if (pos == i) {
                    String cents = oldamt.substring(i + 1);
                    int stang = Integer.parseInt(cents);
                    if (stang != 0) {
                        sb.append(convert(stang)).append("สตางค์");
                    } else {
                        sb.append("ถ้วน");
                    }
                    break;
                }
            }
        } else {
            int pesos = Integer.parseInt(amount);
            sb.append(convert(pesos)).append("บาท").append("ถ้วน");
        }
        return sb.toString();
    }

    /***********************************************************************
***
         *      Get Amount in Words
         *      @param amount numeric amount (352.80)
         *      @return amount in words (three*five*two 80/100)
         */
    public String getAmtInWords(String amount, String iso) throws Exception {
        if (amount == null) return amount;
        StringBuffer sb = new StringBuffer();
        int pos = amount.lastIndexOf('.');
        int pos2 = amount.lastIndexOf(',');
        if (pos2 > pos) pos = pos2;
        String oldamt = amount;
        amount = amount.replaceAll(",", "");
        int newpos = amount.lastIndexOf('.');
        if (newpos != -1) {
            int pesos = Integer.parseInt(amount.substring(0, newpos));
            System.out.println(pesos);
            if (iso.equals("THB")) {
                sb.append(convert(pesos)).append("บาท");
                for (int i = 0; i < oldamt.length(); i++) {
                    if (pos == i) {
                        String cents = oldamt.substring(i + 1);
                        int stang = Integer.parseInt(cents);
                        if (stang != 0) {
                            sb.append(convert(stang)).append("สตางค์");
                        } else {
                            sb.append("ถ้วน");
                        }
                        break;
                    }
                }
            } else {
                sb.append(convert(pesos)).append("เหรียญ");
                for (int i = 0; i < oldamt.length(); i++) {
                    if (pos == i) {
                        String cents = oldamt.substring(i + 1);
                        int stang = Integer.parseInt(cents);
                        if (stang != 0) {
                            sb.append(convert(stang)).append("เซ็นต์").append(" [" + iso + "]");
                        }
                        break;
                    }
                }
            }
        } else {
            int pesos = Integer.parseInt(amount);
            if (iso.equals("THB")) {
                sb.append(convert(pesos)).append("บาท").append("ถ้วน");
            } else {
                sb.append(convert(pesos)).append("เหรียญ").append(" [" + iso + "]");
            }
        }
        return sb.toString();
    }

    /**
         *      Test Print
         *      @param amt amount
         */
    private void print(String amt) {
        try {
            System.out.println(amt + " = " + getAmtInWords(amt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
         *      Test Print
         *      @param amt amount
         */
    private void print(String amt, String currency) {
        try {
            System.out.println(amt + " = " + getAmtInWords(amt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
         *      Test
         *      @param args ignored
         */
    public static void main(String[] args) {
        AmtInWords_TH aiw = new AmtInWords_TH();
        aiw.print("3,026.00");
        aiw.print("65341.78");
    }
}
