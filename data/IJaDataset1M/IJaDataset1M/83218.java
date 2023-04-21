package org.base.apps.beans.util;

/**
 * Visits the enum {@link Kind} much the same way a visitor traverses a model.
 * 
 * @deprecated Marking this class as deprecated because it hasn't yet proven useful.
 * @author Kevan Simpson
 */
public class KindSwitch<R, P> {

    public R evaluate(Kind kind, P obj) {
        if (kind == null) {
            return evalNull(obj);
        } else {
            switch(kind) {
                case bool:
                    {
                        return evalBool(obj);
                    }
                case collection:
                    {
                        return evalCollection(obj);
                    }
                case color:
                    {
                        return evalColor(obj);
                    }
                case date:
                    {
                        return evalDate(obj);
                    }
                case enums:
                    {
                        return evalEnums(obj);
                    }
                case font:
                    {
                        return evalFont(obj);
                    }
                case number:
                    {
                        return evalNumber(obj);
                    }
                case object:
                    {
                        return evalObject(obj);
                    }
                default:
                    {
                        return evalString(obj);
                    }
            }
        }
    }

    public R evalObject(P obj) {
        return null;
    }

    public R evalString(P obj) {
        return null;
    }

    public R evalNumber(P obj) {
        return null;
    }

    public R evalFont(P obj) {
        return null;
    }

    public R evalEnums(P obj) {
        return null;
    }

    public R evalDate(P obj) {
        return null;
    }

    public R evalColor(P obj) {
        return null;
    }

    public R evalCollection(P obj) {
        return null;
    }

    public R evalBool(P obj) {
        return null;
    }

    public R evalNull(P obj) {
        return null;
    }
}
