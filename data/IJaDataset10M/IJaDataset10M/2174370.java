package com.kolszew.swing.actionlaunchpanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.animation.timing.interpolation.Interpolator;
import org.jdesktop.animation.timing.interpolation.KeyFrames;
import org.jdesktop.animation.timing.interpolation.KeyTimes;
import org.jdesktop.animation.timing.interpolation.KeyValues;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.animation.timing.interpolation.SplineInterpolator;
import com.kolszew.swing.actionlaunchpanel.animation.AnimationComputor;
import com.kolszew.swing.actionlaunchpanel.animation.ComputorInDataImpl;
import com.kolszew.swing.actionlaunchpanel.animation.ComputorOutData;
import com.kolszew.swing.actionlaunchpanel.components.AnimateDescriptionLabel;
import com.kolszew.swing.actionlaunchpanel.components.AnimateIcon;
import com.kolszew.swing.actionlaunchpanel.components.AnimateTitleLabel;

/**
 * 
 * Panel which can executing actions and generating animated presentation of
 * actions.
 * <p>
 * Actions must implements
 * {@link com.kolszew.swing.actionlaunchpanel.LunchAction LunchAction}, this
 * interface has default implementation
 * {@link com.kolszew.swing.actionlaunchpanel.AbstractLunchAction
 * AbstractLunchAction}.
 * <p>
 * List of actions must be placed in constructor
 * {@link #ActionLunchPanel(AnimationComputor, List)} or
 * {@link #ActionLunchPanel(AnimationComputor, LunchAction[])}
 * <p>
 * Panel presents actions as animated icons and labels, there are three
 * animation types:
 * <li>startTime - when animation starts</li>
 * <li>focusTime - when action icon gain or lost focus</li>
 * <li>clickTime - when action icon is clicked</li>
 * <p>
 * <br>
 * Details of animation are delegated to
 * {@link com.kolszew.swing.actionlaunchpanel.animation.AnimationComputor
 * AnimationComputor}, this computor is notified with input data
 * {@link com.kolszew.swing.actionlaunchpanel.animation.ComputorInData
 * ComputorInData} which contains all details of animations and panel state and
 * must returns output data
 * {@link com.kolszew.swing.actionlaunchpanel.animation.ComputorOutData
 * ComputorOutData} with computed position of all animations elements.<BR>
 * There are samples (ready for use) computors in package
 * {@link com.kolszew.swing.actionlaunchpanel.animation.sample} <br>
 * <p>
 * In the middle of the panel we can put background image
 * {@link #getBackgroundImage()} <br>
 * <p>
 * Start animation is performed by calling {@link #start()}. <br>
 * <br>
 * 
 * @author Krzysztof Olszewski <kolszew73@o2.pl>
 * 
 */
public class ActionLunchPanel extends JPanel {

    private static final Logger logger = Logger.getLogger(ActionLunchPanel.class);

    private AnimationComputor animationComputor;

    private final List<LunchAction> lunchActions = new ArrayList<LunchAction>();

    private Animator startAnimator;

    private final List<AnimateIcon> animatedIcons = new ArrayList<AnimateIcon>();

    private final List<AnimateTitleLabel> animatedTitleLabels = new ArrayList<AnimateTitleLabel>();

    private final List<AnimateDescriptionLabel> animatedDescriptionLabels = new ArrayList<AnimateDescriptionLabel>();

    private AnimationStub animationStub;

    private BufferedImage backgroundImage;

    private boolean backgroundImageVisible;

    /**
	 * Construct class with specified computor and empty action list
	 * 
	 * @param animationComputor
	 */
    private ActionLunchPanel(AnimationComputor animationComputor) {
        this.animationComputor = animationComputor;
        setBackground(Color.WHITE);
        setLayout(null);
    }

    /**
	 * Construct class with specified computor and copy actions from specified
	 * list
	 * 
	 * @param animationComputor
	 * @param lunchActions
	 */
    public ActionLunchPanel(AnimationComputor animationComputor, List<LunchAction> lunchActions) {
        this(animationComputor);
        if (lunchActions != null) {
            this.lunchActions.addAll(lunchActions);
        }
    }

    /**
	 * Construct class with specified computor and copy actions from specified
	 * array
	 * 
	 * @param animationComputor
	 * @param lunchActions
	 */
    public ActionLunchPanel(AnimationComputor animationComputor, LunchAction[] lunchActions) {
        this(animationComputor);
        for (final LunchAction lunchAction : lunchActions) {
            this.lunchActions.add(lunchAction);
        }
    }

    /**
	 * @return the background image
	 */
    public BufferedImage getBackgroundImage() {
        return this.backgroundImage;
    }

    /**
	 * Sets the background image
	 * 
	 * @param image
	 */
    public void setBackgroundImage(BufferedImage image) {
        this.backgroundImage = image;
        repaint();
    }

    /**
	 * Start animation. Can be run many times.
	 * 
	 */
    public void start() {
        if (isStartAnimationRunning()) {
            return;
        }
        createComponents();
        runStartAnimation();
    }

    private void createComponents() {
        removeOldComponents();
        animationStub = new AnimationStub();
        int no = 0;
        for (final LunchAction lunchAction : lunchActions) {
            final AnimateIcon animatedIcon = new AnimateIcon(lunchAction, animationStub, no);
            animatedIcons.add(animatedIcon);
            add(animatedIcon);
            final AnimateTitleLabel animatedTitleLabel = new AnimateTitleLabel(lunchAction);
            animatedTitleLabels.add(animatedTitleLabel);
            add(animatedTitleLabel);
            final AnimateDescriptionLabel animatedDescriptionLabel = new AnimateDescriptionLabel(lunchAction);
            animatedDescriptionLabels.add(animatedDescriptionLabel);
            add(animatedDescriptionLabel);
            no++;
        }
    }

    private void removeOldComponents() {
        for (final AnimateIcon animateIcon : animatedIcons) {
            remove(animateIcon);
        }
        for (final AnimateTitleLabel animateLabel : animatedTitleLabels) {
            remove(animateLabel);
        }
        for (final AnimateDescriptionLabel animateLabel : animatedDescriptionLabels) {
            remove(animateLabel);
        }
        repaint();
        animatedIcons.clear();
        animatedTitleLabels.clear();
        animatedDescriptionLabels.clear();
    }

    private void runStartAnimation() {
        if (animatedIcons.size() == 0) {
            return;
        }
        final Interpolator splines = new SplineInterpolator(0.0f, 1.0f, 1.0f, 1.0f);
        final KeyTimes times = new KeyTimes(0.0f, 1.0f);
        final KeyValues<Float> values = KeyValues.create(0.0f, 1.0f);
        final KeyFrames frames = new KeyFrames(values, times, splines);
        startAnimator = new Animator(1000);
        startAnimator.setRepeatBehavior(RepeatBehavior.LOOP);
        startAnimator.setRepeatCount(1);
        startAnimator.setResolution(10);
        final PropertySetter propertySetter = new PropertySetter(animationStub, "startTime", frames);
        startAnimator.addTarget(propertySetter);
        startAnimator.start();
    }

    private void setInAnimationData(ComputorInDataImpl in, int elementNumber) {
        int size = lunchActions.size();
        in.setElementsCount(size);
        computeInAnimationData(in, elementNumber);
    }

    /**
	 * Compute (using computor) animation input data, generate output data and
	 * assign this output data to all of animation components
	 * 
	 * @param in
	 * @param number
	 */
    private void computeInAnimationData(ComputorInDataImpl in, int number) {
        in.setElementNumber(number);
        ComputorOutData out = animationComputor.compute(in);
        if (animatedIcons.size() > number) {
            AnimateIcon animateIcon = animatedIcons.get(number);
            setOutAnimationData(out, animateIcon);
            AnimateTitleLabel animateTitleLabel = animatedTitleLabels.get(number);
            setOutAnimationData(out, animateTitleLabel);
            AnimateDescriptionLabel animateDescriptionLabel = animatedDescriptionLabels.get(number);
            setOutAnimationData(out, animateDescriptionLabel);
            setOutAnimationData(out);
        }
    }

    private void setOutAnimationData(ComputorOutData out) {
        setBackgroundImageVisible(out.isBackgroundImageVisible());
    }

    private void setBackgroundImageVisible(boolean backgroundImageVisible) {
        if (this.backgroundImageVisible == backgroundImageVisible) {
            return;
        }
        this.backgroundImageVisible = backgroundImageVisible;
        repaint();
    }

    private void setOutAnimationData(ComputorOutData out, AnimateTitleLabel animateTitleLabel) {
        animateTitleLabel.setVisible(out.isTitleLabelVisible());
        if (!out.isTitleLabelVisible()) {
            return;
        }
        try {
            animateTitleLabel.setBounds(out.getTitleLabelX(), out.getTitleLabelY(), out.getTitleLabelWidth(), out.getTitleLabelHeight());
            animateTitleLabel.setForeground(out.getTitleLabelForeground());
            animateTitleLabel.setHtmlText(out.getTitleLabelHtmlText());
        } finally {
            animateTitleLabel.endChange();
        }
    }

    private void setOutAnimationData(ComputorOutData out, AnimateDescriptionLabel animateDescriptionLabel) {
        animateDescriptionLabel.setVisible(out.isDescriptionVisible());
        if (!out.isDescriptionVisible()) {
            return;
        }
        try {
            animateDescriptionLabel.setBounds(out.getDescriptionLabelX(), out.getDescriptionLabelY(), out.getDescriptionLabelWidth(), out.getDescriptionLabelHeight());
            animateDescriptionLabel.setAlpha(out.getDescriptionLabelAlpha());
        } finally {
            animateDescriptionLabel.endChange();
        }
    }

    private void setOutAnimationData(ComputorOutData out, AnimateIcon animateIcon) {
        try {
            animateIcon.setImageType(out.getImageType());
            animateIcon.setBounds(out.getIconX(), out.getIconY(), out.getIconWidth(), out.getIconHeight());
            animateIcon.setImageBounds(out.getImageX(), out.getImageY(), out.getImageWidth(), out.getImageHeight());
            animateIcon.setShadowVisible(out.isImageShadowVisible());
            if (out.isImageShadowVisible()) {
                animateIcon.setImageShadowBounds(out.getImageShadowX(), out.getImageShadowY(), out.getImageShadowWidth(), out.getImageShadowHeight());
            }
        } finally {
            animateIcon.endChange();
        }
    }

    private boolean isStartAnimationRunning() {
        return (startAnimator != null) && startAnimator.isRunning();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        boolean changed = (getBounds().x != x) || (getBounds().y != y) || (getBounds().width != width) || (getBounds().height != height);
        super.setBounds(x, y, width, height);
        if (changed && (animationStub != null)) {
            animationStub.changed();
        }
    }

    /**
	 * Sets animation computor
	 * 
	 * @param animationComputor
	 */
    public void setAnimationComputor(AnimationComputor animationComputor) {
        this.animationComputor = animationComputor;
    }

    /**
	 * @return animation computor
	 */
    public AnimationComputor getAnimationComputor() {
        return animationComputor;
    }

    /**
	 * Animation stub which centralized all of internal animations signals
	 * 
	 * @author Krzysztof Olszewski <kolszew73@o2.pl>
	 */
    public class AnimationStub {

        private final ComputorInDataImpl[] ins;

        /**
		 * Construct instance
		 */
        private AnimationStub() {
            ins = new ComputorInDataImpl[lunchActions.size()];
            for (int i = 0; i < ins.length; i++) {
                ins[i] = new ComputorInDataImpl();
            }
        }

        /**
		 * Called when start animator generated new <code>startTime</code>
		 * value, call {@link #changed(int)} for all of elements
		 * 
		 * @param startTime
		 *            value represents start animation time state
		 * 
		 */
        public void setStartTime(float startTime) {
            if (logger.isTraceEnabled()) {
                logger.trace("Start animation " + "startTime=" + startTime);
            }
            for (int i = 0; i < ins.length; i++) {
                ComputorInDataImpl in = ins[i];
                in.setStartTime(startTime);
                changed(i);
            }
        }

        /**
		 * Called when focus animator generated new <code>focusTime</code> value
		 * for specified element, call {@link #changed(int)} only for this
		 * element
		 * 
		 * @param focusTime
		 *            value represents focus animation time state
		 * @param elementNumber
		 *            number of element which gained or lost focus
		 * @param isFocusStarting
		 *            true if focus is gained, false if focus is lost
		 */
        public void setFocusTime(float focusTime, int elementNumber, boolean isFocusStarting) {
            if (isStartAnimationRunning()) {
                return;
            }
            if (logger.isTraceEnabled()) {
                logger.trace("Focus animation " + "focusTime=" + focusTime + ", elementNumber=" + elementNumber + ", isFocusStarting=" + isFocusStarting);
            }
            ComputorInDataImpl in = ins[elementNumber];
            in.setFocusTime(focusTime);
            in.setFocusTimeStarting(isFocusStarting);
            changed(elementNumber);
        }

        /**
		 * Called when click animator generated new <code>clickTime</code> value
		 * for specified element, call {@link #changed(int)} only for this
		 * element
		 * 
		 * @param clickTime
		 *            value represents click animation time state
		 * @param elementNumber
		 *            number of element which be clicked
		 */
        public void setClickTime(float clickTime, int elementNumber) {
            if (isStartAnimationRunning()) {
                return;
            }
            if (logger.isTraceEnabled()) {
                logger.trace("Click animation " + "clickTime=" + clickTime + ", elementNumber=" + elementNumber);
            }
            ComputorInDataImpl in = ins[elementNumber];
            in.setClickTime(clickTime);
            changed(elementNumber);
        }

        private void fillInBasicData(ComputorInDataImpl in) {
            in.setAreaHeight(ActionLunchPanel.this.getHeight());
            in.setAreaWidth(ActionLunchPanel.this.getWidth());
            if (ActionLunchPanel.this.lunchActions.size() > 0) {
                in.setSmallImageHeight(ActionLunchPanel.this.lunchActions.get(0).getSmallImage().getHeight());
                in.setSmallImageWidth(ActionLunchPanel.this.lunchActions.get(0).getSmallImage().getWidth());
                in.setBigImageHeight(ActionLunchPanel.this.lunchActions.get(0).getBigImage().getHeight());
                in.setBigImageWidth(ActionLunchPanel.this.lunchActions.get(0).getBigImage().getWidth());
            }
        }

        private void changed(int elementNumber) {
            ComputorInDataImpl in = ins[elementNumber];
            fillInBasicData(in);
            ActionLunchPanel.this.setInAnimationData(in, elementNumber);
        }

        private void changed() {
            for (int i = 0; i < ins.length; i++) {
                changed(i);
            }
        }
    }

    /**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        boolean backgroundImageWillBePainted = backgroundImageVisible && (backgroundImage != null);
        if (backgroundImageWillBePainted) {
            Rectangle rectangle = getCenterImageRectangle();
            g.drawImage(backgroundImage, rectangle.x, rectangle.y, rectangle.width, rectangle.height, null);
        }
    }

    /**
	 * @return
	 */
    private Rectangle getCenterImageRectangle() {
        if (backgroundImage != null) {
            int imgWidth = backgroundImage.getWidth(null);
            int imgHeight = backgroundImage.getHeight(null);
            final int x = (getWidth() - imgWidth) / 2;
            final int y = (getHeight() - imgHeight) / 2 + (getHeight() / 10);
            return new Rectangle(x, y, imgWidth, imgHeight);
        } else {
            return null;
        }
    }
}
