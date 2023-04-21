package com.clican.pluto.common.type;

public class CustomType extends Type {

    /**
	 * 
	 */
    private static final long serialVersionUID = -202339774234490860L;

    private String name;

    private String className;

    public CustomType(String className) {
        this.className = className;
        String[] temp = className.split("\\.");
        this.name = temp[temp.length - 1];
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
        String[] temp = className.split("\\.");
        this.name = temp[temp.length - 1];
    }

    public String getName() {
        return name;
    }

    @Override
    public String codecProperty() {
        return "class=" + this.getClass().getName() + ";className=" + className;
    }

    @Override
    public String getDeclareString() {
        return getName();
    }
}
