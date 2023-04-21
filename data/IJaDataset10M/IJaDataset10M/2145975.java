package patterns;

import javax.vecmath.*;

public class PigmentMap extends ObjectMap {

    public void addEntry(float position, Pigment3D pigment) {
        super.addEntry(position, pigment);
    }

    public Color3f colorAt(float position, float x, float y, float z) {
        if (iState != COMPLETE) throw new IllegalStateException("This colormap is not complete"); else if (position <= 0) return ((Pigment3D) map[0].object).colorAt(x, y, z); else if (position >= 1) return ((Pigment3D) map[map.length - 1].object).colorAt(x, y, z); else {
            int i = lowIndex(position);
            float a = map[i].position;
            float b = map[i + 1].position;
            float s = (position - a) / (b - a);
            Color3f ca = ((Pigment3D) map[i].object).colorAt(x, y, z);
            Color3f cb = ((Pigment3D) map[i + 1].object).colorAt(x, y, z);
            return new Color3f(ca.x + s * (cb.x - ca.x), ca.y + s * (cb.y - ca.y), ca.z + s * (cb.z - ca.z));
        }
    }
}
