package sk.tuke.ess.editor.simulation.simeditbase.objectmodel.schema;

import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.primitives.Primitiva;

/**
 * Created by IntelliJ IDEA.
 * User: zladovan
 * Date: 13.2.2012
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */
public interface LinkLine {

    public int getID();

    public void setID(int id);

    public String getName();

    public Primitiva getPrimitiva();

    public Prepojenie getPrepojenie();
}
