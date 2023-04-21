package adv.runtime.wraper;

/**
 * Alberto Vilches Ratón
 * User: avilches
 * Date: 06-sep-2007
 * Time: 17:52:49
 * To change this template use File | Settings | File Templates.
 */
public class ParserResultWraper {

    private ObjWraper accion;

    private ObjWraper obj1;

    private ObjWraper obj2;

    private FraseWraper frase;

    public ParserResultWraper(ObjWraper accion, ObjWraper obj1, ObjWraper obj2, FraseWraper fraseWraper) {
        this.accion = accion;
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.frase = fraseWraper;
    }

    public ObjWraper getAccion() {
        return accion;
    }

    public ObjWraper getObj1() {
        return obj1;
    }

    public ObjWraper getObj2() {
        return obj2;
    }

    public FraseWraper getFrase() {
        return frase;
    }
}
