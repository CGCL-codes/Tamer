package org.soybeanMilk.core;

/**
 * 执行器找不到给定名称的可执行对象时，将抛出该异常
 * @author earthAngry@gmail.com
 * @date 2010-10-28
 */
public class ExecutableNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -1301466907843994358L;

    private String executableName;

    public ExecutableNotFoundException(String executableName) {
        super();
        this.executableName = executableName;
    }

    public String getExecutableName() {
        return executableName;
    }

    public void setExecutableName(String executableName) {
        this.executableName = executableName;
    }
}
