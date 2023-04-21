package cn.cam4j.util;

import java.text.DecimalFormat;

public class Money2RMB {

    public static void main(String args[]) {
        System.out.println(moneyToRMB(0322.3));
    }

    public static String moneyToRMB(double d) {
        String[] digit = { "��", "��", "��", "Ԫ", "ʰ", "��", "Ǫ", "��", "ʰ", "��", "Ǫ", "��", "ʰ", "��", "Ǫ" };
        String[] capi = { "��", "Ҽ", "��", "��", "��", "��", "½", "��", "��", "��" };
        DecimalFormat df = new DecimalFormat("###0.00");
        char[] c = df.format(d).toCharArray();
        StringBuffer buf = new StringBuffer();
        String reStr = null;
        int cLength = c.length;
        for (int i = 0; i < c.length; i++) {
            String s = "" + c[i];
            if (s.equals(".")) {
                cLength++;
                continue;
            }
            int cp = Integer.parseInt(s);
            buf.append(capi[cp]);
            buf.append(digit[cLength - i - 1]);
        }
        reStr = buf.toString();
        for (int j = 0; j < 2; j++) {
            reStr = reStr.replaceAll("����", "��");
            reStr = reStr.replaceAll("����", "��");
            reStr = reStr.replaceAll("����", "��");
            reStr = reStr.replaceAll("��Ǫ", "��");
            reStr = reStr.replaceAll("���", "��");
            reStr = reStr.replaceAll("��ʰ", "��");
            reStr = reStr.replaceAll("���", "��");
            reStr = reStr.replaceAll("����", "����");
            reStr = reStr.replaceAll("���", "Ԫ��");
            reStr = reStr.replaceAll("��Ԫ", "Ԫ");
            reStr = reStr.replaceAll("����", "��");
            reStr = reStr.replaceAll("��Ԫ��", "����");
            reStr = reStr.replaceAll("ԪԪ", "Ԫ");
        }
        return reStr;
    }
}
