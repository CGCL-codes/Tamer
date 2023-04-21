package com.coderdream.chapter21.proxy.sample;

/**
 * <pre>
 * PrintProxy.java
 * </pre>
 * 
 */
public class PrinterProxy implements Printable {

    private String name;

    private Printer real;

    /**
	 * No Pram Construtor
	 */
    public PrinterProxy() {
    }

    /**
	 * @param name
	 */
    public PrinterProxy(String name) {
        this.name = name;
    }

    /**
	 * 命名
	 */
    public synchronized void setPrinterName(String name) {
        if (real != null) {
            real.setPrinterName(name);
        }
        this.name = name;
    }

    /**
	 */
    public String getPrintName() {
        return name;
    }

    /**
	 * 打印
	 */
    public void print(String string) {
        realize();
        real.print(string);
    }

    /**
	 * 产生本人
	 */
    private synchronized void realize() {
        if (real == null) {
            real = new Printer(name);
        }
    }
}
