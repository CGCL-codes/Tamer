package gestalt.util.meshcreator;

import gestalt.render.Drawable;

public interface DrawableMeshTranslator {

    boolean isClass(Drawable theDrawable);

    void parse(MeshCreator theParent, Drawable theDrawable);
}
