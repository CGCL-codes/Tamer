package gdi1sokoban.graphic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import gdi1sokoban.graphic.base.Material;
import gdi1sokoban.graphic.base.Mesh;

public class Model implements Renderable {

    HashMap<Material, Mesh> _meshs = new HashMap<Material, Mesh>();

    public Model(HashMap<Material, Mesh> meshs) {
        _meshs = meshs;
    }

    public Set<Material> getMaterials() {
        return _meshs.keySet();
    }

    public Mesh getMesh(Material material) {
        return _meshs.get(material);
    }

    public void render() {
        for (Map.Entry<Material, Mesh> iMesh : _meshs.entrySet()) {
            if (iMesh.getKey() != null) iMesh.getKey().render();
            iMesh.getValue().render();
        }
    }
}
