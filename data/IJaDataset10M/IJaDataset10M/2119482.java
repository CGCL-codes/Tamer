package com.lubq.lm.util;

/**
 * 用于生成密码的字符集
 * 
 * @author Jhf <jhf@bestwiz.cn>
 * 
 * 
 * @copyright 2006, BestWiz(Dalian) Co.,Ltd
 * @version $Id: PasswordCharsBean.java,v 1.1 2007/05/12 04:17:01 zhangwc Exp $
 */
public class PasswordCharsBean {

    private static final long serialVersionUID = -7367807582970958975L;

    private static final char[] CHARSETS = { 'A', 'B', 'C', 'D', 'E', 'F', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'G', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z', '2', '3', '4', '5', '6', '7', '8', '9' };

    /**
     * 返回指定序号的字符
     * 
     * @param i
     *            序号 (0-52)
     * @return char
     * 
     * @author yaolin <yaolin@bestwiz.cn>
     */
    public static char getAt(int i) {
        return CHARSETS[i];
    }

    /**
     * 返回字符集的大小
     * 
     * @return
     * 
     * @author yaolin <yaolin@bestwiz.cn>
     */
    public static int length() {
        return CHARSETS.length;
    }
}
