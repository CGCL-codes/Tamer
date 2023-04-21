package mysterychess.util;

/**
 *
 * @author Tin Bui-Huy
 */
public interface Task {

    public void perform() throws Exception;

    public String getDescription();
}
