package barsuift.simLife.j3d;

import javax.xml.bind.annotation.XmlRootElement;
import barsuift.simLife.State;

@XmlRootElement
public class SimLifeCanvas3DState implements State {

    private boolean fpsShowing;

    public SimLifeCanvas3DState() {
        super();
        this.fpsShowing = false;
    }

    public SimLifeCanvas3DState(boolean fpsShowing) {
        super();
        this.fpsShowing = fpsShowing;
    }

    public boolean isFpsShowing() {
        return fpsShowing;
    }

    public void setFpsShowing(boolean fpsShowing) {
        this.fpsShowing = fpsShowing;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (fpsShowing ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SimLifeCanvas3DState other = (SimLifeCanvas3DState) obj;
        if (fpsShowing != other.fpsShowing) return false;
        return true;
    }

    @Override
    public String toString() {
        return "SimLifeCanvas3DState [fpsShowing=" + fpsShowing + "]";
    }
}
