package ch.fusun.baron.weather;

/**
 * Updates the water/vapor relation
 */
public class VaporPressureCurveUpdater implements AutomatonUpdater {

    @Override
    public void update(Cube[][][] surroundingCubes, Cube cube) {
        Cube oldCube = surroundingCubes[1][1][1];
        float temperature = oldCube.getTemperature();
        float maxVapour = (temperature * temperature) / 1000.0f;
        if (oldCube.getVapour() < maxVapour) {
            float absorbedWater = oldCube.getWater() - (maxVapour - oldCube.getVapour());
            if (absorbedWater > 0) {
                cube.setVapour(oldCube.getVapour() + absorbedWater);
                cube.setWater(oldCube.getWater() - absorbedWater);
            } else {
                cube.setVapour(oldCube.getVapour() + oldCube.getWater());
                cube.setWater(0);
            }
        } else {
            float releasedVapor = oldCube.getVapour() - maxVapour;
            cube.setVapour(oldCube.getVapour() - releasedVapor);
            cube.setWater(oldCube.getWater() + releasedVapor);
        }
    }
}
