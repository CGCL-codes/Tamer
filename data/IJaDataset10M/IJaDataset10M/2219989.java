package net.sourceforge.ivi.ui.waveview.format;

public class FormatEntityUtils {

    public static IFormatEntity findEntity(IFormatEntity entity, String name) {
        IFormatEntity sub_entities[] = entity.getEntities();
        for (int i = 0; i < sub_entities.length; i++) {
            if (sub_entities[i].getName().equals(name)) {
                return sub_entities[i];
            }
        }
        return null;
    }

    public static IFormatAttribute findAttribute(IFormatEntity entity, String name) {
        IFormatAttribute attr[] = entity.getAttributes();
        for (int i = 0; i < attr.length; i++) {
            if (attr[i].getName().equals(name)) {
                return attr[i];
            }
        }
        return null;
    }
}
