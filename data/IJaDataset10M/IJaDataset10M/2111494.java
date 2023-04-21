package net.sf.jbob.core;

public class ViewPojo extends ViewBob {

    public ViewPojo() {
        super();
    }

    public ViewPojo(BindingDescription bindingDescription) {
        super(bindingDescription);
    }

    private static final String VISIBLE = "visible";

    private static final String ENABLED = "enabled";

    private static final String EDITABLE = "editable";

    private PojoUtil pojoUtil = PojoUtil.getInstance();

    @Override
    public Object getValue() {
        return pojoUtil.getProperty(getBindingDescription().getModel().getData(), getViewPath());
    }

    private String getViewPath() {
        return getBindingDescription().getViewPath();
    }

    private Object getView() {
        return getBindingDescription().getView();
    }

    @Override
    public boolean isEditable() {
        Object value = pojoUtil.getProperty(getView(), EDITABLE);
        return value != null && ((Boolean) value).booleanValue();
    }

    @Override
    public boolean isEnabled() {
        Object value = pojoUtil.getProperty(getView(), ENABLED);
        return value != null && ((Boolean) value).booleanValue();
    }

    @Override
    public boolean isVisible() {
        Object value = pojoUtil.getProperty(getView(), VISIBLE);
        return value != null && ((Boolean) value).booleanValue();
    }

    @Override
    public void setEditable(boolean value) {
        pojoUtil.setProperty(getView(), EDITABLE, value);
    }

    @Override
    public void setEnabled(boolean value) {
        pojoUtil.setProperty(getView(), ENABLED, value);
    }

    @Override
    public void setValue(Object value) {
        pojoUtil.setProperty(getView(), getViewPath(), value);
    }

    @Override
    public void setVisible(boolean value) {
        pojoUtil.setProperty(getView(), VISIBLE, value);
    }
}
