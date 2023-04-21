package kku.cs.fgl.timeline;

import java.util.Vector;

public abstract class AbstractAction {

    /**
	 * ˹�ǧ���� ��͹�ӧҹ (1/1000 sec)
	 */
    private int delay_before = 0;

    /**
	 * ���ҷ����㹡�÷ӧҹ (1/1000 sec)
	 */
    private int duration = 1000;

    /**
	 * Action �Ѵ价��зӧҹ ����� Action ���ӧҹ��
	 */
    private AbstractAction next = null;

    /**
	 * Action ����ѧ�ӧҹ����
	 */
    private boolean active = true;

    private Easing easing = Easing.LINEAR;

    /**
	 * ʶҹТͧ Action
	 */
    protected int state = 0;

    public AbstractAction() {
        this(1000);
    }

    public AbstractAction(int duration) {
        this.duration = duration;
    }

    private int rtime = 0;

    public void update(int time) {
        if (state == 0) {
            if (next != null) next.state = 0;
            state = 1;
            rtime = 0;
            start();
        }
        rtime = rtime + time;
        if (state == 1 && rtime > delay_before) {
            if (active) action(rtime - delay_before);
            if (rtime >= duration + delay_before) state = 3;
        }
        if (state == 3) {
            if (next != null && next.active) {
                next.update(time);
                if (next.state == 4) {
                    state = 4;
                }
            } else {
                state = 4;
            }
        }
    }

    public void start() {
    }

    public abstract void action(int time);

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public AbstractAction getNext() {
        return next;
    }

    public void setNext(AbstractAction next) {
        this.next = next;
    }

    public int getState() {
        return state;
    }

    public int getDelay_before() {
        return delay_before;
    }

    public void setDelay_before(int delay_before) {
        this.delay_before = delay_before;
    }

    public Easing getEasing() {
        return easing;
    }

    public void setEasing(Easing easing) {
        this.easing = easing;
    }

    /**
     * The basic function for easing.
     * @param b the beginning value
     * @param c the value changed
     * @return the eased value
     */
    protected float ease(float b, float c) {
        return this.easing.ease(rtime - delay_before, b, c, duration);
    }
}
