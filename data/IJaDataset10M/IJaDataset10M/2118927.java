package org.jbehave.core.story.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.jbehave.core.exception.VerificationException;
import org.jbehave.core.story.renderer.Renderer;

/**
 * <p>A Scenario describes a series of events, run in a particular
 * context, and for which certain outcomes are expected.</p>
 * 
 * <p>This class allows a scenario to be implemented with
 * instances of particular givens, events and outcomes.
 * Each {@link Given}, {@link Event} or {@link Outcome} should be 
 * represented by a different class, which can be reused in different
 * scenarios.</p>
 * 
 * <p>A scenario's elements can be used to describe it thus:<ul>
 * <li>Given <a context></li>
 * <li>When <an event happens></li>
 * <li>Then <an outcome should occur></li>.
 * </ul></p>
 * 
 * <p>eg:<ul>
 * <li>Given that I already have two crosses in opposite corners</li>
 * <li>When I put a cross in the middle</li>
 * <li>Then I should win the game.</li>
 * </ul></p>
 * 
 * <p>Sometimes a scenario may only finish after several events and
 * the outcomes that result from them. Also, you may want to carry out
 * checks on the way through using Outcomes that are not related to
 * the outcome of the scenario. For instance, you may login to a web app
 * and then ensure you are at the "welcome" page before continuing with
 * the scenario. Each Given, Event and Outcome is considered a {@link Step},
 * and Steps can be chained together in any arbitrary sequence to accomplish
 * this.</p>
 * 
 * <p>Each scenario runs as follows:<ul>
 * <li>Assemble the steps (Givens, Events and Outcomes)</li>
 * <li>Perform each step</li>
 * <li>In reverse order, tidy up each step</li>
 * </ul>
 * 
 * <p>The annotation methods {@link #given(Given)}, {@link #when(Event)} and
 * {@link #then(Outcome)} are used to make the Scenario code easier to read.</p>
 * 
 * <p>ScenarioUsingSteps also gives you easy access to JBehave's MiniMock
 * framework. This allows you to mock out the bits of the system that you
 * haven't written yet. The Scenario will report the use of mocks, but
 * will not consider them a failure.</p>
 *
 * @author <a href="mailto:dan.north@thoughtworks.com">Dan North</a>
 * @author <a href="mailto:liz@thoughtworks.com">Elizabeth Keogh</a>
 */
public abstract class MultiStepScenario implements Scenario {

    private static final String UNSPECIFIED = "Unspecified";

    private static final String SPECIFIED = "Specified";

    private static final String RUN = "Run";

    private static final String CLEANED = "Cleaned";

    private List steps = new ArrayList();

    private String state;

    public MultiStepScenario() {
        state = UNSPECIFIED;
    }

    public final void specify() {
        checkState(UNSPECIFIED);
        specifySteps();
        state = SPECIFIED;
    }

    protected abstract void specifySteps();

    public void run(World world) {
        checkState(SPECIFIED);
        try {
            for (Iterator i = steps.iterator(); i.hasNext(); ) {
                Step step = (Step) i.next();
                step.perform(world);
            }
        } catch (VerificationException e) {
            throw e;
        } catch (Exception e) {
            throw new VerificationException("Exception in Scenario", e);
        } finally {
            state = RUN;
        }
    }

    private void checkState(String expected) {
        if (state != expected) {
            throw new IllegalStateException("Current state is " + state + ", should be " + expected);
        }
    }

    public void cleanUp(World world) {
        if (shouldCleanUp()) {
            for (ListIterator i = steps.listIterator(steps.size()); i.hasPrevious(); ) {
                ((AbstractStep) i.previous()).cleanUp(world);
            }
        }
        state = CLEANED;
    }

    protected boolean shouldCleanUp() {
        return state == RUN;
    }

    public void narrateTo(Renderer renderer) {
        checkState(SPECIFIED);
        renderer.renderScenario(this);
        for (Iterator i = steps.iterator(); i.hasNext(); ) {
            ((Step) i.next()).narrateTo(renderer);
        }
    }

    public boolean containsMocks() {
        for (Iterator i = steps.iterator(); i.hasNext(); ) {
            Step step = (Step) i.next();
            if (step.containsMocks()) {
                return true;
            }
        }
        return false;
    }

    public void verifyMocks() {
    }

    protected void given(Given given) {
        steps.add(new GivenStep(given));
    }

    protected void given(Scenario scenario) {
        scenario.specify();
        steps.add(new GivenStep(new GivenScenario(scenario)));
    }

    protected void when(Event event) {
        steps.add(new EventStep(event));
    }

    protected void then(final Outcome outcome) {
        steps.add(new OutcomeStep(outcome));
        if (outcome instanceof OutcomeWithExpectations) {
            injectAfterGivens(new AbstractStep(outcome) {

                public void perform(World world) {
                    ((OutcomeWithExpectations) outcome).setExpectationsIn(world);
                }
            });
        }
    }

    private void injectAfterGivens(Step step) {
        ListIterator i = steps.listIterator(steps.size());
        while (i.hasPrevious()) {
            Object maybeGivenStep = i.previous();
            if (maybeGivenStep instanceof GivenStep) {
                steps.add(i.nextIndex() + 1, step);
                return;
            }
        }
        steps.add(0, step);
    }
}
