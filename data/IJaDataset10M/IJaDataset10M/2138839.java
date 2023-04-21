package org.aspectme.cldc.reflect;

import org.aspectme.cldc.reflect.testtypes.Dummy;

public class ExpectedGeneratedReflectME extends ReflectME {

    @Override
    protected Object invoke(int id, Object obj, Object[] params) throws Throwable {
        Object rv = null;
        switch(id) {
            case 0:
                ((Dummy) obj).returnVoid();
                break;
            case 1:
                rv = intToObject(((Dummy) obj).returnInt());
                break;
            case 2:
                ((Dummy) obj).allTypes(objectToBoolean(params[0]), objectToByte(params[1]), objectToChar(params[2]), objectToShort(params[3]), objectToInt(params[4]), objectToLong(params[5]), objectToFloat(params[6]), objectToDouble(params[7]));
                break;
            case 3:
                rv = doubleToObject(((Dummy) obj).returnDouble());
                break;
            default:
                throw new RuntimeException("Method with id " + id + " have not been registered. Run ReflectMEGenerator to generate reflective information.");
        }
        return rv;
    }

    @Override
    protected void registerMethods() {
        registerMethod(0, Dummy.class, "test", new Class[] { Integer.TYPE, String.class }, Integer.TYPE, Modifier.PUBLIC);
    }

    @Override
    protected Object get(int id, Object obj) {
        return null;
    }

    @Override
    protected void set(int id, Object obj, Object value) {
    }
}
