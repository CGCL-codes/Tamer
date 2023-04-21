package rollmadness.particleengine;

import com.jme3.math.Vector3f;

public interface Vector3fFunction {

    Vector3fFunction IDENTITY = new Vector3fFunction() {

        public Vector3f apply(Vector3f data, float dtime, float delta) {
            return data;
        }
    };

    Vector3f apply(Vector3f data, float time, float delta);
}
