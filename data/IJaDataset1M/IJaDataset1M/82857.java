package supernaturalgame;

public abstract class MovementStrategy {

    public static final int STATE_NONE = 0;

    public static final int STATE_WALKING = 1;

    public static final int STATE_ASCENDING = 2;

    public static final int STATE_FALLING = 3;

    public static final int STATE_ATTACKING = 4;

    public static final int GROUND_RANGE = 10;

    public static final int STAIR_RANGE = 10;

    private Agent agent;

    private Scenery scenery;

    private int state;

    private int hSpeed;

    private int vSpeed;

    private int stateCount;

    private int direction;

    private boolean canJump;

    private int fallThreshold;

    private int fallDist;

    public MovementStrategy() {
        state = STATE_NONE;
        hSpeed = 0;
        vSpeed = 0;
        direction = 1;
        canJump = true;
        fallThreshold = 5;
    }

    public abstract void updateLogic(int currentFrame);

    public void setScenery(Scenery aScenery) {
        scenery = aScenery;
    }

    public void setAgent(Agent anAgent) {
        agent = anAgent;
    }

    public void translateAgent(int speedX, int speedY) {
        translateAgentY(speedY);
        translateAgentX(speedX);
    }

    public boolean translateAgentY(int speed) {
        boolean result = false;
        if (speed != 0) {
            agent.translateY(speed);
            result = true;
            if (!getScenery().isInsideMap(getAgent().getShadow())) {
                agent.translateY(-speed);
                result = false;
            }
        }
        return result;
    }

    public boolean translateAgentX(int speed) {
        boolean result = false;
        if (speed != 0) {
            agent.translateX(speed);
            result = true;
            if (!getScenery().isInsideMap(getAgent().getShadow())) {
                agent.translateY(2);
                if (!getScenery().isInsideMap(getAgent().getShadow())) {
                    agent.translateY(-4);
                    if (!getScenery().isInsideMap(getAgent().getShadow())) {
                        agent.translateY(2);
                        agent.translateX(-speed);
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    protected boolean isGrounded() {
        return true;
    }

    public Agent getAgent() {
        return agent;
    }

    public int getState() {
        return state;
    }

    protected void setState(int newState) {
        this.state = newState;
    }

    public int getHspeed() {
        return hSpeed;
    }

    public void setHspeed(int hspeed) {
        this.hSpeed = hspeed;
    }

    public int getVspeed() {
        return vSpeed;
    }

    public void setVspeed(int vspeed) {
        this.vSpeed = vspeed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getHSpeed() {
        return hSpeed;
    }

    public int getVSpeed() {
        return vSpeed;
    }

    void invertDirection() {
        direction *= -1;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public void setFallDist(int fallDist) {
        this.fallDist = fallDist;
    }

    public void setFallThreshold(int fallThreshold) {
        this.fallThreshold = fallThreshold;
    }

    public void sethSpeed(int hSpeed) {
        this.hSpeed = hSpeed;
    }

    public void setStateCount(int stateCount) {
        this.stateCount = stateCount;
    }

    public void setvSpeed(int vSpeed) {
        this.vSpeed = vSpeed;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public int getFallDist() {
        return fallDist;
    }

    public int getFallThreshold() {
        return fallThreshold;
    }

    public int gethSpeed() {
        return hSpeed;
    }

    public Scenery getScenery() {
        return scenery;
    }

    public int getStateCount() {
        return stateCount;
    }

    public int getvSpeed() {
        return vSpeed;
    }
}
