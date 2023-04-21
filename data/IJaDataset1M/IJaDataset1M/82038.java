package ramon.util;

public class EnumUtil {

    @SuppressWarnings("unchecked")
    public static Enum<?> getValorEnum(Class clase, String valor) {
        return Enum.valueOf(clase, valor);
    }
}
