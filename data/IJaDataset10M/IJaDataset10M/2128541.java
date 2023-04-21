package com.mia.sct.transition.strategy;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.swingx.painter.Painter;
import com.mia.sct.transition.ITransitionTimingTarget;
import com.mia.sct.transition.TransitionDirection;

/**
 * DummyTestStrategy
 *
 * @author Devon Bryant
 * @since May 31, 2008
 */
public class DummyTestStrategy implements ITransitionStrategy, TimingTarget {

    JPanel dummyPanel = null;

    protected Map<String, Component> componentCache = null;

    protected List<String> componentNames = null;

    protected ITransitionTimingTarget timingTarget = null;

    protected Animator animator = null;

    public DummyTestStrategy() {
        setupAnimator();
        componentCache = new HashMap<String, Component>();
        componentNames = new ArrayList<String>();
        dummyPanel = new JPanel(new BorderLayout());
        JButton showButton = new JButton("Show Panel X");
        dummyPanel.add(new JLabel("Add Label X"), BorderLayout.NORTH);
        dummyPanel.add(new JLabel("This is panel X"), BorderLayout.CENTER);
        dummyPanel.add(showButton, BorderLayout.SOUTH);
    }

    protected void setupAnimator() {
        animator = new Animator(1000, this);
        animator.setAcceleration(0.2f);
        animator.setDeceleration(0.3f);
    }

    public Component addComponent(String inName, Component inComponent) {
        componentCache.put(inName, inComponent);
        componentNames.add(inName);
        return null;
    }

    public void addTimingTarget(ITransitionTimingTarget inTimingTarget) {
        timingTarget = inTimingTarget;
    }

    public Component getComponent(String inName) {
        return componentCache.get(inName);
    }

    public List<String> getComponentNames() {
        return componentNames;
    }

    public Component getTransitionDisplayComponent() {
        return dummyPanel;
    }

    public boolean isAnimating() {
        return animator.isRunning();
    }

    public Component removeComponent(Component inComponent) {
        return null;
    }

    public void setTransitionDuration(int inDurationMillis) {
    }

    public void transition(String inFromCompName, String inToCompName, TransitionDirection inDirection, ITransitionTimingTarget inAnimationListener) {
        timingTarget = inAnimationListener;
        animator.start();
    }

    public void begin() {
        timingTarget.begin();
    }

    public void end() {
        timingTarget.end();
    }

    public void repeat() {
    }

    public void timingEvent(float inFraction) {
    }

    @SuppressWarnings("unchecked")
    public void setBackgroundPainter(Painter inBackgroundPainter) {
    }

    @SuppressWarnings("unchecked")
    public void setForegroundPainter(Painter inForegroundPainter) {
    }
}
