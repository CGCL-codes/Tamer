package be.vds.jtbdive.core.view.util;

import java.util.HashMap;
import java.util.Map;
import be.vds.jtbdive.core.model.DiverRole;

public abstract class DiverRoleImageMapper {

    public static final Map<Integer, String> IMAGE_ROLES = new HashMap<Integer, String>();

    static {
        IMAGE_ROLES.put(DiverRole.ROLE_PALANQUEE_SIMPLE_DIVER, "diver16.png");
        IMAGE_ROLES.put(DiverRole.ROLE_PALANQUEE_FIRST, "diverred16.png");
        IMAGE_ROLES.put(DiverRole.ROLE_PALANQUEE_SECOND, "diverblue16.png");
        IMAGE_ROLES.put(DiverRole.ROLE_PALANQUEE_MEDICAL_SUPPORT, "mediccase16.png");
        IMAGE_ROLES.put(DiverRole.ROLE_PALANQUEE_CAMERA, "camera16.png");
    }
}
