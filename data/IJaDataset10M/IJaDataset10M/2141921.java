package net.hanjava.widget;

import java.awt.Container;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JWindow;
import net.hanjava.widget.AbstractNotifier.Animation;

/** this class is not an API but an implementation. so, don't use this directly */
class NotifierWindow extends JWindow {

    private AbstractNotifier notifier;

    private JComponent slidingComp = null;

    private Animation animation = null;

    NotifierWindow(AbstractNotifier notifier, Icon backImg) {
        this(notifier, backImg, new SlideUpAnimation());
    }

    /**
	 * @param backImg
	 * @param anim
	 *            null�� ���� �ȵȴ�
	 */
    NotifierWindow(AbstractNotifier notifier, Icon backImg, Animation anim) {
        this.notifier = notifier;
        VoidPanel back = new VoidPanel(backImg);
        setAnimation(anim);
        setContentPane(back);
    }

    void setAnimation(Animation anim) {
        this.animation = anim;
    }

    void init(JComponent slidingComp) {
        this.slidingComp = slidingComp;
        Container cp = getContentPane();
        slidingComp.setSize(getSize());
        cp.setLayout(null);
        cp.add(slidingComp);
        slidingComp.setLocation(0, slidingComp.getHeight());
    }

    void animate() {
        final VoidPanel cp = (VoidPanel) getContentPane();
        if (animation.hasOwnThread()) {
            animateImpl(cp);
        } else {
            Runnable effectRunner = new Runnable() {

                public void run() {
                    animateImpl(cp);
                }
            };
            Thread t = new Thread(effectRunner, "Animator-" + this);
            t.start();
        }
    }

    private void animateImpl(VoidPanel vp) {
        notifier.addActiveWindow(this);
        animation.doAnimation(vp, slidingComp);
        notifier.removeActiveWindow(this);
    }
}
