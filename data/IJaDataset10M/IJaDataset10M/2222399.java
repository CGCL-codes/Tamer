package hjb.ggj.tasks;

import haframework.draw.Sprite;
import haframework.draw.SpriteFactory;
import haframework.task.Task;
import hjb.ggj.TaskSet;

/**
 * @author hejiabin
 *
 */
public class LogoTask extends Task {

    protected Sprite m_bg = null;

    protected float m_time = 0.0f;

    /**
	 * @desc	constructor
	 */
    public LogoTask() {
    }

    @Override
    public void vBegin() {
        m_bg = SpriteFactory.Singleton().CreateSprite(hjb.ggj.R.drawable.ggj2012logo);
        m_bg.SetUV(0.0f, 0.0f, 1.0f, 1.0f);
        m_time = 0.0f;
    }

    @Override
    public void vMain(float elapsed) {
        m_time += 0.1f;
        if (m_time > 15.0f) {
            this.Stop();
            TaskSet._titleTask.Start(0);
        }
    }

    @Override
    public void vDraw(float elapsed) {
        m_bg.Draw(0, 0, 320, 560);
    }
}
