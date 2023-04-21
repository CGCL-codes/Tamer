package za.org.meraka.dictionarymaker.ui;

/**
 * ProjectCreateInterface
 * 
 * This interface must be implemented by each panel that is a step in the
 * project-create sequence * 
 * 
 * @author avrensbu 2006/02/23
 */
public interface ProjectCreateInterface {

    public String getComponentTitle();

    public boolean getCurrent();

    public void setCurrent(boolean current);

    public boolean isDataCorrect();
}
