package model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Cambiarme el nombre please!!
 * 
 * permite construir a partir de ella PropertyHavers que no necesitan
 * definir nada de sus propiedades salvo el contenido 
 * del array estatico props. Notar que se pide una nomeclatura para 
 * getters y setters adecuada seg�n los ids de propiedades 
 * 
 * definir� los metodos de observer de propiedades segun l contenido 
 * del array props (que la subclase determina). 
 * utilizo reflection para buscar el setter y el getter de una propiedad. 
 * 
 * Se sigue la siguiente forma: Si el id de la propiedad es XXX entonces 
 * se guscan los metodos Object getXXX() y setXXX(Object). 
 * 
 * Tanto si no se encuentran getters/setters o si los tipos de propiedad
 * no son los adecuado se tirar� una runtimeException con info adecuada de 
 * qu� paso
 
 * @author sgx
 *
 */
public abstract class AbstractPropertyHaver extends PropertyHaver {

    public static Property[] props;

    @Override
    public void changePropertyValue(String propId, Object value) {
        int N = props.length;
        boolean found = false;
        for (int i = 0; i < N; i++) {
            if (props[i].id.equals(propId)) {
                found = true;
                break;
            }
        }
        if (!found) throw new RuntimeException("\"" + this.getClass() + " PropertyHaver doesn't recognize the property " + propId); else {
            try {
                Class[] params = { Object.class };
                Method setter = this.getClass().getMethod(getSetterNameFromPropId(propId), params);
                setter.invoke(this, new Object[] { value });
                this.notifyAll(propId, value);
            } catch (SecurityException e) {
                throw new RuntimeException("SecurityException when trying to access method " + getSetterNameFromPropId(propId) + " in class " + this.getClass());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                throw new RuntimeException("NoSuchMethodException when trying to access method " + getSetterNameFromPropId(propId) + " in class " + this.getClass() + ". Read AbstractPropertyHaver API comments on how to name getters and setters. ");
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("IllegalArgumentException when trying to invoke method " + getSetterNameFromPropId(propId) + " in class " + this.getClass() + ". Read AbstractPropertyHaver API comments on how to name getters and setters. ");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("IllegalAccessException when trying to invoke method " + getSetterNameFromPropId(propId) + " in class " + this.getClass() + ". Read AbstractPropertyHaver API comments on how to name getters and setters. ");
            } catch (InvocationTargetException e) {
                throw new RuntimeException("InvocationTargetException when trying to invoke method " + getSetterNameFromPropId(propId) + " in class " + this.getClass() + ". Read AbstractPropertyHaver API comments on how to name getters and setters. The exception says: " + e + " - " + e.getCause());
            }
        }
    }

    public String getSetterNameFromPropId(String propId) {
        return "set" + propId.substring(0, 1).toUpperCase() + propId.substring(1, propId.length());
    }

    public String getGetterNameFromPropId(String propId) {
        return "get" + propId.substring(0, 1).toUpperCase() + propId.substring(1, propId.length());
    }

    @Override
    public Object getPropertyValue(String propId) {
        int N = props.length;
        for (int i = 0; i < N; i++) {
            if (props[i].id.equals(propId)) {
                try {
                    Class[] params = new Class[1];
                    Method setter = this.getClass().getMethod(getGetterNameFromPropId(propId), params);
                    return setter.invoke(this, new Object[] {});
                } catch (SecurityException e) {
                    throw new RuntimeException("SecurityException when trying to access method " + getGetterNameFromPropId(propId) + " in class " + this.getClass());
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("SecurityException when trying to access method " + getGetterNameFromPropId(propId) + " in class " + this.getClass() + ". Read AbstractPropertyHaver API comments on how to name getters and setters. ");
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("IllegalArgumentException when trying to invoke method " + getGetterNameFromPropId(propId) + " in class " + this.getClass() + ". Read AbstractPropertyHaver API comments on how to name getters and setters. ");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("IllegalAccessException when trying to invoke method " + getGetterNameFromPropId(propId) + " in class " + this.getClass() + ". Read AbstractPropertyHaver API comments on how to name getters and setters. ");
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("InvocationTargetException when trying to invoke method " + getGetterNameFromPropId(propId) + " in class " + this.getClass() + ". Read AbstractPropertyHaver API comments on how to name getters and setters. ");
                }
            }
        }
        throw new RuntimeException("\"" + this.getClass() + " PropertyHaver doesn't recognize the property " + propId);
    }

    @Override
    public Property[] getProperties() {
        return props;
    }
}
