package edu.whitman.halfway.jigs.gui.jigspace;

import edu.whitman.halfway.jigs.gui.jigspace.jtlrenderer.JSJTLRenderer;
import edu.whitman.halfway.jigs.ImageType;
import edu.whitman.halfway.jigs.gui.pdp.PDPFieldObject;
import edu.whitman.halfway.jigs.gui.jigspace.JSIOHashMap;
import org.apache.log4j.*;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class JigSpaceRenderSystem {

    private static Logger log = Logger.getLogger(JigSpaceRenderSystem.class.getName());

    private static final String headerString = "JSRenderSystem";

    private File rendererPath;

    protected PDPFieldObject[] pdpFields;

    protected ImageType[] imageTypes;

    protected JSRenderer renderer;

    private String category;

    public JigSpaceRenderSystem() {
        pdpFields = new PDPFieldObject[0];
        imageTypes = new ImageType[0];
        renderer = new JSJTLRenderer();
    }

    public ImageType[] getImageTypes() {
        return imageTypes;
    }

    public void setImageTypes(ImageType[] img) {
        imageTypes = img;
    }

    public PDPFieldObject[] getPDPFields() {
        return pdpFields;
    }

    public void setPDPFields(PDPFieldObject[] pdp) {
        pdpFields = pdp;
    }

    public JSRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(JSRenderer r) {
        renderer = r;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String r) {
        category = r;
    }

    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof JigSpaceRenderSystem)) return false;
        JigSpaceRenderSystem other = (JigSpaceRenderSystem) obj;
        log.debug("Checking PDPFields Fields");
        if (pdpFields.length != other.pdpFields.length) return false;
        for (int i = 0; i < pdpFields.length; i++) {
            if (!pdpFields[i].equals(other.pdpFields[i])) return false;
        }
        log.debug("Checking image Type Fields");
        if (imageTypes.length != other.imageTypes.length) return false;
        for (int i = 0; i < imageTypes.length; i++) {
            boolean same = false;
            for (int j = 0; j < imageTypes.length; j++) {
                if (imageTypes[i].equals(other.imageTypes[j])) same = true;
            }
            if (!same) return false;
        }
        if (!renderer.equals(other.renderer)) return false;
        return true;
    }

    public boolean write(File file) {
        return JSIOHashMap.writeMap(file, getMap(), JSIOHashMap.TEMPLATE_HEADER_STRING);
    }

    public JSIOHashMap getMap() {
        JSIOHashMap map = new JSIOHashMap();
        for (int i = 0; i < pdpFields.length; i++) {
            map.put(JSIOHashMap.PDP_FIELD, pdpFields[i].getMap());
        }
        for (int i = 0; i < imageTypes.length; i++) {
            map.put(JSIOHashMap.IMAGE_TYPE, imageTypes[i].getMap());
        }
        map.put(JSIOHashMap.RENDERER_PATH, getRenderer().getPath());
        map.put(JSIOHashMap.RENDERER_OPTIONS, renderer.getMap());
        return map;
    }

    public static JigSpaceRenderSystem read(File file) {
        JigSpaceRenderSystem temp = setMap(JSIOHashMap.readMap(file, JSIOHashMap.TEMPLATE_HEADER_STRING));
        return temp;
    }

    public static JigSpaceRenderSystem setMap(JSIOHashMap map) {
        log.debug("Setting RenderSystem");
        JigSpaceRenderSystem renderSystem = new JigSpaceRenderSystem();
        if (map == null) return renderSystem;
        JSIOHashMap subMap = new JSIOHashMap();
        ArrayList list = new ArrayList();
        while ((subMap = (JSIOHashMap) map.get(JSIOHashMap.PDP_FIELD)) != null) {
            list.add(PDPFieldObject.setMap(subMap));
        }
        renderSystem.setPDPFields((PDPFieldObject[]) list.toArray(new PDPFieldObject[0]));
        list = new ArrayList();
        while ((subMap = (JSIOHashMap) map.get(JSIOHashMap.IMAGE_TYPE)) != null) {
            list.add(ImageType.setMap(subMap));
        }
        renderSystem.setImageTypes((ImageType[]) list.toArray(new ImageType[0]));
        String rendererPath = (String) map.get(JSIOHashMap.RENDERER_PATH);
        try {
            Class rendererClass = Class.forName(rendererPath);
            renderSystem.renderer = (JSRenderer) rendererClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            renderSystem.renderer.setMap((JSIOHashMap) map.get(JSIOHashMap.RENDERER_OPTIONS));
        } catch (Exception e) {
            log.error("Could not load: " + rendererPath);
        }
        return renderSystem;
    }

    public static boolean isJigSpaceRenderSystem(File file) {
        return JSIOHashMap.checkFile(file, JSIOHashMap.TEMPLATE_HEADER_STRING);
    }

    private static boolean isJigSpaceRenderSystem(InputStream in) {
        return JSIOHashMap.checkStream(in, JSIOHashMap.TEMPLATE_HEADER_STRING);
    }
}
