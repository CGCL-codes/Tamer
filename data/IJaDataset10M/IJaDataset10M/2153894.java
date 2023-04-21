package sk.tuke.ess.editor.simulation.simeditbase.objectmodel;

/**
 * Created by IntelliJ IDEA.
 * User: zladovan
 * Date: 5.1.2012
 * Time: 17:25
 * To change this template use File | Settings | File Templates.
 */
public interface Connectable {

    public boolean isObsadene();

    public boolean isIO();

    public boolean canBeConnectedWith(Connectable connectable);

    public String getNazov();
}
