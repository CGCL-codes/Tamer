package com.jspx.io.zip;

/**
 * 这个程序需要自行处理的高层错误异常，引入的目的是方便进行自己的高层异常处理。
 * <p><b>目前这个类仅仅还是一个空壳，没有实际的用处，以后会扩展并完善</b>
 */
public class ZipException extends Exception {

    public static final int ENTRYEXIST = 1;

    private int type = 0;

    /**
     * 构造方法。
     *
     * @param type 异常类型
     */
    public ZipException(int type) {
        this.type = type;
    }

    /**
     * 得到异常的类型。
     *
     * @return 异常的类型
     */
    public int getType() {
        return type;
    }
}
